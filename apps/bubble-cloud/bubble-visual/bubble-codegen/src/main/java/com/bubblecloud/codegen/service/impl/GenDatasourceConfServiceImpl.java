package com.bubblecloud.codegen.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.druid.DruidConfig;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubblecloud.codegen.service.GenDatasourceConfService;
import com.bubblecloud.codegen.entity.GenDatasourceConf;
import com.bubblecloud.codegen.mapper.GenDatasourceConfMapper;
import com.bubblecloud.codegen.service.GenDatasourceConfService;
import com.bubblecloud.codegen.util.JdbcUrlSecurityValidator;
import com.bubblecloud.common.core.util.SpringContextHolder;
import com.bubblecloud.common.datasource.util.DsConfTypeEnum;
import com.bubblecloud.common.datasource.util.DsJdbcUrlEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据源表
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenDatasourceConfServiceImpl extends ServiceImpl<GenDatasourceConfMapper, GenDatasourceConf>
		implements GenDatasourceConfService {

	private final StringEncryptor stringEncryptor;

	private final DataSourceCreator druidDataSourceCreator;

	/**
	 * 保存数据源并且加密
	 * @param conf
	 * @return
	 */
	@Override
	public Boolean saveDsByEnc(GenDatasourceConf conf) {
		// 校验配置合法性
		if (!checkDataSource(conf)) {
			return Boolean.FALSE;
		}

		// 添加动态数据源
		addDynamicDataSource(conf);

		// 更新数据库配置
		conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
		this.baseMapper.insert(conf);
		return Boolean.TRUE;
	}

	/**
	 * 更新数据源
	 * @param conf 数据源信息
	 * @return
	 */
	@Override
	public Boolean updateDsByEnc(GenDatasourceConf conf) {
		// 密码为空时，从数据库查询原始密码并解密，用于连接校验
		if (StrUtil.isBlank(conf.getPassword())) {
			GenDatasourceConf dbConf = this.baseMapper.selectById(conf.getId());
			conf.setPassword(stringEncryptor.decrypt(dbConf.getPassword()));
		}

		if (!checkDataSource(conf)) {
			return Boolean.FALSE;
		}
		// 先移除
		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		dynamicRoutingDataSource.removeDataSource(baseMapper.selectById(conf.getId()).getName());

		// 再添加
		addDynamicDataSource(conf);

		// 更新数据库配置
		conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
		this.baseMapper.updateById(conf);
		return Boolean.TRUE;
	}

	/**
	 * 通过数据源名称删除
	 * @param dsIds 数据源ID
	 * @return
	 */
	@Override
	public Boolean removeByDsId(Long[] dsIds) {
		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		this.baseMapper.selectByIds(CollUtil.toList(dsIds))
			.forEach(ds -> dynamicRoutingDataSource.removeDataSource(ds.getName()));
		this.baseMapper.deleteByIds(CollUtil.toList(dsIds));
		return Boolean.TRUE;
	}

	/**
	 * 添加动态数据源
	 * @param conf 数据源信息
	 */
	@Override
	public void addDynamicDataSource(GenDatasourceConf conf) {
		JdbcUrlSecurityValidator.validate(conf.getUrl());
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setPoolName(conf.getName());
		dataSourceProperty.setUrl(conf.getUrl());
		dataSourceProperty.setUsername(conf.getUsername());
		dataSourceProperty.setPassword(conf.getPassword());

		// 增加 ValidationQuery 参数
		DruidConfig druidConfig = new DruidConfig();
		DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(conf.getDsType());
		druidConfig.setValidationQuery(urlEnum.getValidationQuery());
		dataSourceProperty.setDruid(druidConfig);
		DataSource dataSource = druidDataSourceCreator.createDataSource(dataSourceProperty);

		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		dynamicRoutingDataSource.addDataSource(dataSourceProperty.getPoolName(), dataSource);
	}

	/**
	 * 按名称把配置表中的数据源挂到动态路由上。
	 * @param dsName 数据源名称
	 */
	@Override
	public void ensureDynamicDataSource(String dsName) {
		DynamicRoutingDataSource routingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		if (routingDataSource.getDataSources().containsKey(dsName)) {
			return;
		}

		GenDatasourceConf conf = this.getOne(
				Wrappers.<GenDatasourceConf>lambdaQuery().eq(GenDatasourceConf::getName, dsName), false);
		if (conf == null) {
			throw new IllegalArgumentException("DataSource not found: " + dsName);
		}

		conf.setPassword(stringEncryptor.decrypt(conf.getPassword()));
		this.checkDataSource(conf);
		this.addDynamicDataSource(conf);
	}

	/**
	 * 校验数据源配置是否有效
	 * @param conf 数据源信息
	 * @return 有效/无效
	 */
	@Override
	public Boolean checkDataSource(GenDatasourceConf conf) {
		String url;
		// JDBC 配置形式
		if (DsConfTypeEnum.JDBC.getType().equals(conf.getConfType())) {
			url = conf.getUrl();
		}
		else if (DsJdbcUrlEnum.MSSQL.getDbName().equals(conf.getDsType())) {
			// 主机形式 sql server 特殊处理
			DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(conf.getDsType());
			url = String.format(urlEnum.getUrl(), conf.getHost(), conf.getPort(), conf.getDsName());
		}
		else {
			DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(conf.getDsType());
			url = String.format(urlEnum.getUrl(), conf.getHost(), conf.getPort(), conf.getDsName());
		}

		conf.setUrl(url);
		JdbcUrlSecurityValidator.validate(url);

		try (Connection connection = DriverManager.getConnection(url, conf.getUsername(), conf.getPassword())) {
		}
		catch (SQLException e) {
			log.error("数据源配置 {} , 获取链接失败", conf.getName(), e);
			throw new RuntimeException("数据库配置错误，链接失败");
		}
		return Boolean.TRUE;
	}

	/**
	 * 查询数据库解析插件加载状态
	 * @return 数据源类型与解析插件状态
	 */
	@Override
	public Map<String, Boolean> listParserPlugins() {
		Map<String, Boolean> result = new LinkedHashMap<>();
		for (DsJdbcUrlEnum dsEnum : DsJdbcUrlEnum.values()) {
			result.put(dsEnum.getDbName(), isClassPresent(dsEnum.getAnylineAdapter()));
		}
		return result;
	}

	private boolean isClassPresent(String className) {
		try {
			ClassUtil.loadClass(className, false);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

}
