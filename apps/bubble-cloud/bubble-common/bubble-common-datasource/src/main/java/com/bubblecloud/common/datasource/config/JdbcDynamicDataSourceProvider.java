package com.bubblecloud.common.datasource.config;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.druid.DruidConfig;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.bubblecloud.common.core.constant.enums.YesNoEnum;
import com.bubblecloud.common.datasource.support.DataSourceConstants;
import com.bubblecloud.common.datasource.util.DsConfTypeEnum;
import com.bubblecloud.common.datasource.util.DsJdbcUrlEnum;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2020/2/6
 * <p>
 * 从数据源中获取 配置信息
 */
@Slf4j
public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

	private static final String SQL_LOG_FILTER = "sqlLogFilter";

	private final DruidDataSourceProperties properties;

	private final StringEncryptor stringEncryptor;

	public JdbcDynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
			StringEncryptor stringEncryptor, DruidDataSourceProperties properties) {
		super(defaultDataSourceCreator, properties.getDriverClassName(), properties.getUrl(), properties.getUsername(),
				properties.getPassword());
		this.stringEncryptor = stringEncryptor;
		this.properties = properties;
	}

	/**
	 * 加载所有数据源
	 * @return 所有数据源
	 */
	@Override
	public Map<String, DataSource> loadDataSources() {
		if (!isQueryDsEnabled()) {
			log.debug("spring.datasource.druid.query-ds-enabled=false，已关闭数据库配置表加载");
			return Collections.emptyMap();
		}
		return super.loadDataSources();
	}

	/**
	 * 执行语句获得数据源参数
	 * @param statement 语句
	 * @return 数据源参数
	 * @throws SQLException sql异常
	 */
	@Override
	protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {

		Map<String, DataSourceProperty> map = new HashMap<>(8);

		try {
			// 使用 PreparedStatement 防止 SQL 注入。
			PreparedStatement pstmt = statement.getConnection().prepareStatement(properties.getQueryDsSql());
			pstmt.setString(1, YesNoEnum.NO.getCode()); // del_flag参数

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String name = rs.getString(DataSourceConstants.NAME);
				String username = rs.getString(DataSourceConstants.DS_USER_NAME);
				String password = rs.getString(DataSourceConstants.DS_USER_PWD);
				Integer confType = rs.getInt(DataSourceConstants.DS_CONFIG_TYPE);
				String dsType = rs.getString(DataSourceConstants.DS_TYPE);

				DataSourceProperty property = new DataSourceProperty();
				property.setUsername(username);
				property.setPassword(stringEncryptor.decrypt(password));

				String url;
				// JDBC 配置形式
				DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(dsType);
				if (DsConfTypeEnum.JDBC.getType().equals(confType)) {
					url = rs.getString(DataSourceConstants.DS_JDBC_URL);
				}
				else {
					String host = rs.getString(DataSourceConstants.DS_HOST);
					String port = rs.getString(DataSourceConstants.DS_PORT);
					String dsName = rs.getString(DataSourceConstants.DS_NAME);
					url = String.format(urlEnum.getUrl(), host, port, dsName);
				}

				// Druid Config
				DruidConfig druidConfig = new DruidConfig();
				druidConfig.setProxyFilters(SQL_LOG_FILTER);
				druidConfig.setConnectionErrorRetryAttempts(5);
				druidConfig.setBreakAfterAcquireFailure(true);
				druidConfig.setValidationQuery(urlEnum.getValidationQuery());
				property.setDruid(druidConfig);
				property.setUrl(url);

				map.put(name, property);
			}

			// 关闭PreparedStatement和ResultSet
			rs.close();
			pstmt.close();

		}
		catch (Exception e) {
			log.warn("动态数据源配置表异常:{}", e.getMessage());
		}

		return map;
	}

	/**
	 * 是否开启从数据库配置表加载扩展数据源
	 * @return true 开启（默认）；仅显式配置为 false 时关闭
	 */
	boolean isQueryDsEnabled() {
		return !Boolean.FALSE.equals(properties.getQueryDsEnabled());
	}

}
