package com.bubblecloud.biz.agi.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bubblecloud.agi.api.dto.DatasourceTestDTO;
import com.bubblecloud.agi.api.vo.DatasourceTestResultVO;
import com.bubblecloud.agi.api.vo.TableInfoVO;
import com.bubblecloud.agi.api.entity.DatasourceTableField;
import com.bubblecloud.common.core.exception.CheckedException;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * JDBC 连接工具类
 *
 * @author BubbleCloud
 * @date 2026-03-02
 */
@Slf4j
public class JdbcUtils {

	private static final Pattern HOST_NAME_PATTERN = Pattern
		.compile("^[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*$");

	private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[A-Za-z0-9_][A-Za-z0-9_$.@-]{0,127}$");

	private static final Pattern EXTRA_VALUE_PATTERN = Pattern.compile("^[A-Za-z0-9_.,:+/-]{1,128}$");

	private static final Map<DsType, List<String>> SAFE_EXTRA_PARAMETERS = Map.of(
			DsType.MYSQL, List.of("characterEncoding", "useSSL", "allowPublicKeyRetrieval", "serverTimezone",
					"connectTimeout", "socketTimeout", "zeroDateTimeBehavior"),
			DsType.DORIS, List.of("characterEncoding", "useSSL", "allowPublicKeyRetrieval", "serverTimezone",
					"connectTimeout", "socketTimeout", "zeroDateTimeBehavior"),
			DsType.STARROCKS, List.of("characterEncoding", "useSSL", "allowPublicKeyRetrieval", "serverTimezone",
					"connectTimeout", "socketTimeout", "zeroDateTimeBehavior"),
			DsType.PG, List.of("ssl", "sslmode", "connectTimeout", "socketTimeout", "applicationName",
					"currentSchema"),
			DsType.ORACLE, List.of("oracle.net.CONNECT_TIMEOUT", "oracle.jdbc.ReadTimeout"),
			DsType.SQL_SERVER, List.of("encrypt", "trustServerCertificate", "loginTimeout", "connectTimeout",
					"socketTimeout", "applicationName"),
			DsType.CK, List.of("connect_timeout", "socket_timeout", "database"),
			DsType.DM, List.of("connectTimeout", "socketTimeout", "schema"));

	/**
	 * 数据源类型（code、驱动、URL 构建规则）
	 */
	private enum DsType {
		MYSQL("mysql", "com.mysql.cj.jdbc.Driver", true) {
			@Override
			String buildJdbcUrl(DatasourceTestDTO dto) {
				return StrUtil.format(
						"jdbc:mysql://{}:{}/{}?characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true",
						dto.getHost(),
						dto.getPort(),
						StrUtil.blankToDefault(dto.getDsName(), "")
				);
			}
		},
		PG("pg", "org.postgresql.Driver", false) {
			@Override
			String buildJdbcUrl(DatasourceTestDTO dto) {
				return StrUtil.format(
						"jdbc:postgresql://{}:{}/{}",
						dto.getHost(),
						dto.getPort(),
						StrUtil.blankToDefault(dto.getDsName(), "")
				);
			}
		},
		ORACLE("oracle", "oracle.jdbc.OracleDriver", false) {
			@Override
			String buildJdbcUrl(DatasourceTestDTO dto) {
				String mode = dto.getMode();
				if (StrUtil.equals("sid", mode)) {
					return StrUtil.format(
							"jdbc:oracle:thin:@{}:{}:{}",
							dto.getHost(),
							dto.getPort(),
							StrUtil.blankToDefault(dto.getDsName(), "ORCL")
					);
				}
				return StrUtil.format(
						"jdbc:oracle:thin:@//{}:{}/{}",
						dto.getHost(),
						dto.getPort(),
						StrUtil.blankToDefault(dto.getDbSchema(), "ORCL")
				);
			}
		},
		SQL_SERVER("sqlServer", "com.microsoft.sqlserver.jdbc.SQLServerDriver", false) {
			@Override
			String buildJdbcUrl(DatasourceTestDTO dto) {
				return StrUtil.format(
						"jdbc:sqlserver://{}:{};database={};characterEncoding=UTF-8",
						dto.getHost(),
						dto.getPort(),
						StrUtil.blankToDefault(dto.getDsName(), "")
				);
			}
		},
		CK("ck", "com.clickhouse.jdbc.ClickHouseDriver", true) {
			@Override
			String buildJdbcUrl(DatasourceTestDTO dto) {
				return StrUtil.format(
						"jdbc:clickhouse://{}:{}/{}",
						dto.getHost(),
						dto.getPort(),
						StrUtil.blankToDefault(dto.getDsName(), "default")
				);
			}
		},
		DM("dm", "dm.jdbc.driver.DmDriver", false) {
			@Override
			String buildJdbcUrl(DatasourceTestDTO dto) {
				return StrUtil.format(
						"jdbc:dm://{}:{}/{}",
						dto.getHost(),
						dto.getPort(),
						StrUtil.blankToDefault(dto.getDsName(), "")
				);
			}
		},
		DORIS("doris", "com.mysql.cj.jdbc.Driver", true) {
			@Override
			String buildJdbcUrl(DatasourceTestDTO dto) {
				return StrUtil.format(
						"jdbc:mysql://{}:{}/{}",
						dto.getHost(),
						dto.getPort(),
						StrUtil.blankToDefault(dto.getDsName(), "")
				);
			}
		},
		STARROCKS("starrocks", "com.mysql.cj.jdbc.Driver", true) {
			@Override
			String buildJdbcUrl(DatasourceTestDTO dto) {
				return StrUtil.format(
						"jdbc:mysql://{}:{}/{}",
						dto.getHost(),
						dto.getPort(),
						StrUtil.blankToDefault(dto.getDsName(), "")
				);
			}
		};

		private final String code;
		private final String driverClass;
		private final boolean schemaFromDbName;

		DsType(String code, String driverClass, boolean schemaFromDbName) {
			this.code = code;
			this.driverClass = driverClass;
			this.schemaFromDbName = schemaFromDbName;
		}

		abstract String buildJdbcUrl(DatasourceTestDTO dto);

		String getCode() {
			return code;
		}

		String getDriverClass() {
			return driverClass;
		}

		boolean isSchemaFromDbName() {
			return schemaFromDbName;
		}

		static DsType ofCode(String code) {
			if (StrUtil.isBlank(code)) {
				return null;
			}
			for (DsType type : DsType.values()) {
				if (StrUtil.equals(type.code, code)) {
					return type;
				}
			}
			return null;
		}
	}

	private static DsType resolveDsType(DatasourceTestDTO dto) {
		return ObjectUtil.isNull(dto) ? null : DsType.ofCode(dto.getDsType());
	}

	/**
	 * 校验用户输入并构建JDBC URL。
	 */
	public static String buildJdbcUrl(DatasourceTestDTO dto, boolean allowPrivateNetwork) {
		String extraJdbc = validate(dto, allowPrivateNetwork);
		DsType dsType = resolveDsType(dto);
		if (ObjectUtil.isNull(dto) || StrUtil.isBlank(dto.getDsType()) || StrUtil.isBlank(dto.getHost())
				|| ObjectUtil.isNull(dto.getPort()) || StrUtil.isBlank(dto.getDsName())) {
			throw new CheckedException("数据源配置不完整");
		}
		if (ObjectUtil.isNull(dsType)) {
			throw new CheckedException("不支持的数据源类型: " + dto.getDsType());
		}
		StringBuilder url = new StringBuilder(dsType.buildJdbcUrl(dto));
		if (StrUtil.isNotBlank(extraJdbc)) {
			if (dsType == DsType.SQL_SERVER) {
				url.append(";").append(extraJdbc);
			} else {
				url.append(url.indexOf("?") >= 0 ? "&" : "?").append(extraJdbc);
			}
		}
		return url.toString();
	}

	/**
	 * 校验数据源字段，但不构建JDBC URL。
	 */
	public static String validate(DatasourceTestDTO dto, boolean allowPrivateNetwork) {
		if (ObjectUtil.isNull(dto)) {
			throw new CheckedException("数据源配置不能为空");
		}
		DsType dsType = resolveDsType(dto);
		if (ObjectUtil.isNull(dsType)) {
			throw new CheckedException("不支持的数据源类型: " + dto.getDsType());
		}
		if (ObjectUtil.isNull(dto.getPort()) || dto.getPort() < 1 || dto.getPort() > 65535) {
			throw new CheckedException("数据源端口不合法");
		}
		if (StrUtil.isNotBlank(dto.getMode()) && !StrUtil.equalsAny(dto.getMode(), "sid", "service_name")) {
			throw new CheckedException("Oracle连接模式不合法");
		}
		if (StrUtil.isBlank(dto.getDsName()) || !IDENTIFIER_PATTERN.matcher(dto.getDsName()).matches()) {
			throw new CheckedException("数据库名称不合法");
		}
		if (StrUtil.isNotBlank(dto.getDbSchema()) && !IDENTIFIER_PATTERN.matcher(dto.getDbSchema()).matches()) {
			throw new CheckedException("Schema名称不合法");
		}
		if (ObjectUtil.isNotNull(dto.getTimeout()) && (dto.getTimeout() < 1 || dto.getTimeout() > 60)) {
			throw new CheckedException("数据源超时时间必须在1-60秒之间");
		}

		String host = normalizeAndValidateHost(dto.getHost());
		validateResolvedAddresses(host, allowPrivateNetwork);
		dto.setHost(host);
		return normalizeExtraJdbc(dto.getExtraJdbc(), dsType);
	}

	/**
	 * 测试数据库连接
	 */
	public static DatasourceTestResultVO testConnection(DatasourceTestDTO dto, boolean allowPrivateNetwork) {
		DatasourceTestResultVO result = new DatasourceTestResultVO();
		String jdbcUrl;
		try {
			jdbcUrl = buildJdbcUrl(dto, allowPrivateNetwork);
		}
		catch (CheckedException e) {
			result.setConnected(false);
			result.setErrorMessage(e.getMessage());
			return result;
		}
		DsType dsType = resolveDsType(dto);
		if (ObjectUtil.isNull(dsType) || StrUtil.isBlank(dsType.getDriverClass())) {
			result.setConnected(false);
			result.setErrorMessage("未找到数据源类型对应的驱动: " + dto.getDsType());
			return result;
		}

		String driverClass = dsType.getDriverClass();
		Connection connection = null;
		try {
			// 加载驱动
			Class.forName(driverClass);
			connection = DriverManager.getConnection(jdbcUrl, dto.getUsername(), dto.getPassword());
			// 获取数据库信息
			DatabaseMetaData metaData = connection.getMetaData();
			result.setConnected(true);
			result.setDatabaseName(metaData.getDatabaseProductName());
			result.setDatabaseVersion(metaData.getDatabaseProductVersion());
			result.setErrorMessage(null);

			log.info("数据库连接测试成功: 数据源类型={}, 数据源ID={}", dto.getDsType(), dto.getId());

		} catch (ClassNotFoundException e) {
			result.setConnected(false);
			result.setErrorMessage("未找到数据库驱动: " + driverClass);
			log.error("数据库驱动未找到: {}", driverClass, e);
		} catch (SQLException e) {
			result.setConnected(false);
			result.setErrorMessage("数据库连接失败: " + e.getMessage());
			log.error("数据库连接失败: 数据源类型={}, 数据源ID={}", dto.getDsType(), dto.getId(), e);
		} finally {
			closeQuietly(connection);
		}
		return result;
	}

	/**
	 * 获取数据库表详细信息（包含表注释）
	 */
	public static List<TableInfoVO> getTableInfo(DatasourceTestDTO dto, boolean allowPrivateNetwork) {
		List<TableInfoVO> tableInfoList = new ArrayList<>();

		String jdbcUrl = buildJdbcUrl(dto, allowPrivateNetwork);

		DsType dsType = resolveDsType(dto);
		if (ObjectUtil.isNull(dsType) || StrUtil.isBlank(dsType.getDriverClass())) {
			return tableInfoList;
		}

		String driverClass = dsType.getDriverClass();
		Connection connection = null;
		ResultSet rs = null;

		try {
			Class.forName(driverClass);
			connection = DriverManager.getConnection(
					jdbcUrl,
					dto.getUsername(),
					dto.getPassword()
			);

			DatabaseMetaData metaData = connection.getMetaData();
			String schema = dsType.isSchemaFromDbName() ? dto.getDsName() : dto.getDbSchema();

			rs = metaData.getTables(
					schema,
					null,
					"%",
					new String[]{"TABLE", "VIEW"}
			);

			while (rs.next()) {
				TableInfoVO tableInfo = new TableInfoVO();
				tableInfo.setTableName(rs.getString("TABLE_NAME"));
				tableInfo.setTableComment(rs.getString("REMARKS") != null ? rs.getString("REMARKS") : "");
				tableInfo.setTableType(rs.getString("TABLE_TYPE"));
				tableInfoList.add(tableInfo);
			}

			log.info("获取数据库表详细信息成功: 数据源类型={}, 数据源ID={}, 表数量={}", dto.getDsType(), dto.getId(),
					tableInfoList.size());

		} catch (Exception e) {
			log.error("获取数据库表详细信息失败: 数据源类型={}, 数据源ID={}", dto.getDsType(), dto.getId(), e);
		} finally {
			closeQuietly(rs);
			closeQuietly(connection);
		}

		return tableInfoList;
	}

	/**
	 * 获取表字段信息
	 */
	public static List<DatasourceTableField> getTableFields(DatasourceTestDTO dto, String tableName,
			boolean allowPrivateNetwork) {
		List<DatasourceTableField> fields = new ArrayList<>();

		String jdbcUrl = buildJdbcUrl(dto, allowPrivateNetwork);
		if (StrUtil.isBlank(tableName) || !IDENTIFIER_PATTERN.matcher(tableName).matches()) {
			throw new CheckedException("表名不合法");
		}

		DsType dsType = resolveDsType(dto);
		if (ObjectUtil.isNull(dsType) || StrUtil.isBlank(dsType.getDriverClass())) {
			return fields;
		}

		String driverClass = dsType.getDriverClass();
		Connection connection = null;
		ResultSet rs = null;

		try {
			Class.forName(driverClass);
			connection = DriverManager.getConnection(
					jdbcUrl,
					dto.getUsername(),
					dto.getPassword()
			);

			DatabaseMetaData metaData = connection.getMetaData();
			String schema = dsType.isSchemaFromDbName() ? dto.getDsName() : dto.getDbSchema();

			rs = metaData.getColumns(schema, null, tableName, "%");

			int order = 0;
			while (rs.next()) {
				DatasourceTableField field = new DatasourceTableField();
				field.setFieldName(rs.getString("COLUMN_NAME"));
				field.setFieldType(rs.getString("TYPE_NAME"));
				String remark = rs.getString("REMARKS") != null ? rs.getString("REMARKS") : "";
				field.setFieldComment(remark);
				// 自定义注释默认等于字段注释
				field.setCustomComment(remark);
				field.setWeight(order++);
				fields.add(field);
			}

			log.info("获取表字段信息成功: {}.{} - {} 个字段", schema, tableName, fields.size());

		} catch (Exception e) {
			log.error("获取表字段信息失败: {}.{}", dto.getDsName(), tableName, e);
		} finally {
			closeQuietly(rs);
			closeQuietly(connection);
		}

		return fields;
	}

	private static String normalizeAndValidateHost(String rawHost) {
		if (StrUtil.isBlank(rawHost)) {
			throw new CheckedException("数据源主机不能为空");
		}
		String host = rawHost.trim();
		if (host.length() > 255) {
			throw new CheckedException("数据源主机不合法");
		}
		if (host.startsWith("[") || host.endsWith("]")) {
			if (host.length() < 4 || host.charAt(0) != '[' || host.charAt(host.length() - 1) != ']') {
				throw new CheckedException("IPv6主机格式不合法");
			}
			host = host.substring(1, host.length() - 1);
			try {
				if (!(InetAddress.getByName(host) instanceof Inet6Address)) {
					throw new CheckedException("IPv6主机格式不合法");
				}
			}
			catch (UnknownHostException e) {
				throw new CheckedException("IPv6主机格式不合法");
			}
			return "[" + host.toLowerCase(Locale.ROOT) + "]";
		}
		if (host.indexOf(':') >= 0 || !HOST_NAME_PATTERN.matcher(host).matches()) {
			throw new CheckedException("数据源主机不合法");
		}
		return host.toLowerCase(Locale.ROOT);
	}

	private static void validateResolvedAddresses(String host, boolean allowPrivateNetwork) {
		InetAddress[] addresses;
		try {
			addresses = InetAddress.getAllByName(host);
		}
		catch (UnknownHostException e) {
			throw new CheckedException("数据源主机无法解析");
		}
		if (addresses.length == 0) {
			throw new CheckedException("数据源主机无法解析");
		}
		for (InetAddress address : addresses) {
			if (isAlwaysRejectedAddress(address) || isKnownMetadataAddress(address)
					|| (!allowPrivateNetwork && isPrivateNetworkAddress(address))) {
				throw new CheckedException("数据源主机指向受限网络地址");
			}
		}
	}

	private static boolean isAlwaysRejectedAddress(InetAddress address) {
		return address.isAnyLocalAddress() || address.isLinkLocalAddress() || address.isMulticastAddress();
	}

	private static boolean isPrivateNetworkAddress(InetAddress address) {
		if (address.isLoopbackAddress() || address.isSiteLocalAddress()) {
			return true;
		}
		byte[] bytes = address.getAddress();
		if (address instanceof Inet6Address) {
			return (bytes[0] & 0xfe) == 0xfc || (bytes[0] == 0x20 && bytes[1] == 0x01);
		}
		int first = bytes[0] & 0xff;
		int second = bytes[1] & 0xff;
		return first == 100 && second >= 64 && second <= 127 || first == 198 && (second == 18 || second == 19);
	}

	private static boolean isKnownMetadataAddress(InetAddress address) {
		byte[] bytes = address.getAddress();
		if (bytes.length != 4) {
			return bytes.length == 16 && bytes[0] == (byte) 0xfd && bytes[1] == 0x00 && bytes[2] == 0x00
					&& bytes[3] == 0x00 && bytes[4] == 0x00 && bytes[5] == 0x00 && bytes[6] == 0x00
					&& bytes[7] == 0x00 && bytes[8] == 0x00 && bytes[9] == 0x00 && bytes[10] == 0x00
					&& bytes[11] == 0x00 && bytes[12] == (byte) 0xec && bytes[13] == 0x02;
		}
		return (bytes[0] & 0xff) == 169 && (bytes[1] & 0xff) == 254 && (bytes[2] & 0xff) == 169
				&& (bytes[3] & 0xff) == 254
				|| (bytes[0] & 0xff) == 100 && (bytes[1] & 0xff) == 100 && (bytes[2] & 0xff) == 100
						&& (bytes[3] & 0xff) == 200;
	}

	private static String normalizeExtraJdbc(String extraJdbc, DsType dsType) {
		if (StrUtil.isBlank(extraJdbc)) {
			return "";
		}
		String input = extraJdbc.trim();
		if (input.length() > 512) {
			throw new CheckedException("额外JDBC参数过长");
		}
		List<String> safeNames = SAFE_EXTRA_PARAMETERS.getOrDefault(dsType, List.of());
		Map<String, String> parameters = new LinkedHashMap<>();
		for (String segment : input.split("[&;]")) {
			if (StrUtil.isBlank(segment)) {
				continue;
			}
			int separatorIndex = segment.indexOf('=');
			if (separatorIndex <= 0 || separatorIndex == segment.length() - 1) {
				throw new CheckedException("额外JDBC参数格式不合法");
			}
			String name = segment.substring(0, separatorIndex).trim();
			String value = segment.substring(separatorIndex + 1).trim();
			String normalized = name.toLowerCase(Locale.ROOT);
			if (safeNames.stream().noneMatch(item -> item.toLowerCase(Locale.ROOT).equals(normalized))) {
				throw new CheckedException("额外JDBC参数不允许: " + name);
			}
			if (!EXTRA_VALUE_PATTERN.matcher(value).matches() || parameters.containsKey(normalized)) {
				throw new CheckedException("额外JDBC参数值不合法");
			}
			parameters.put(normalized, name + "=" + value);
		}
		if (parameters.isEmpty()) {
			throw new CheckedException("额外JDBC参数格式不合法");
		}
		return String.join(dsType == DsType.SQL_SERVER ? ";" : "&", parameters.values());
	}

	/**
	 * 关闭连接
	 */
	private static void closeQuietly(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.warn("关闭数据库连接失败", e);
			}
		}
	}

	/**
	 * 关闭结果集
	 */
	private static void closeQuietly(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.warn("关闭结果集失败", e);
			}
		}
	}
}
