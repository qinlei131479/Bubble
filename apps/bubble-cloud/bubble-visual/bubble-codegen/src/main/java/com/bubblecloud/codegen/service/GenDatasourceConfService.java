package com.bubblecloud.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubblecloud.codegen.entity.GenDatasourceConf;

import java.util.Map;

/**
 * 数据源表
 *
 * @author qinlei
 * @date 2025/05/31
 */
public interface GenDatasourceConfService extends IService<GenDatasourceConf> {

	/**
	 * 保存数据源并且加密
	 * @param genDatasourceConf
	 * @return
	 */
	Boolean saveDsByEnc(GenDatasourceConf genDatasourceConf);

	/**
	 * 更新数据源
	 * @param genDatasourceConf
	 * @return
	 */
	Boolean updateDsByEnc(GenDatasourceConf genDatasourceConf);

	/**
	 * 更新动态数据的数据源列表
	 * @param datasourceConf
	 * @return
	 */
	void addDynamicDataSource(GenDatasourceConf datasourceConf);

	/**
	 * 按名称把配置表中的数据源挂到动态路由上。
	 * 未加载时不能回退到主库，否则切换数据源仍会查到同一批表。
	 * @param dsName 数据源名称
	 */
	void ensureDynamicDataSource(String dsName);

	/**
	 * 校验数据源配置是否有效
	 * @param datasourceConf 数据源信息
	 * @return 有效/无效
	 */
	Boolean checkDataSource(GenDatasourceConf datasourceConf);

	/**
	 * 查询数据库解析插件加载状态
	 * @return 数据源类型与解析插件状态
	 */
	Map<String, Boolean> listParserPlugins();

	/**
	 * 通过数据源名称删除
	 * @param dsIds 数据源ID
	 * @return
	 */
	Boolean removeByDsId(Long[] dsIds);

}
