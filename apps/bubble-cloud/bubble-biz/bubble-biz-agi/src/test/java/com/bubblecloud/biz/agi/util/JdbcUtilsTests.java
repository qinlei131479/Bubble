package com.bubblecloud.biz.agi.util;

import com.bubblecloud.agi.api.dto.DatasourceTestDTO;
import com.bubblecloud.common.core.exception.CheckedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
	 * 用户配置的AGI JDBC URL安全测试。
 */
class JdbcUtilsTests {

	@Test
	void buildJdbcUrlShouldRejectPrivateTargetByDefault() {
		DatasourceTestDTO dto = mysqlDto("127.0.0.1");

		assertThatThrownBy(() -> JdbcUtils.buildJdbcUrl(dto, false)).isInstanceOf(CheckedException.class)
			.hasMessage("数据源主机指向受限网络地址");
	}

	@Test
	void buildJdbcUrlShouldAllowConfiguredPrivateTarget() {
		DatasourceTestDTO dto = mysqlDto("localhost");

		assertThatCode(() -> JdbcUtils.buildJdbcUrl(dto, true)).doesNotThrowAnyException();
	}

	@Test
	void buildJdbcUrlShouldAlwaysRejectMetadataTarget() {
		DatasourceTestDTO dto = mysqlDto("169.254.169.254");

		assertThatThrownBy(() -> JdbcUtils.buildJdbcUrl(dto, true)).isInstanceOf(CheckedException.class)
			.hasMessage("数据源主机指向受限网络地址");
	}

	@Test
	void buildJdbcUrlShouldRejectHostInjection() {
		DatasourceTestDTO dto = mysqlDto("example.com:3306");

		assertThatThrownBy(() -> JdbcUtils.buildJdbcUrl(dto, false)).isInstanceOf(CheckedException.class)
			.hasMessage("数据源主机不合法");
	}

	@Test
	void buildJdbcUrlShouldRejectDatabaseNameInjection() {
		DatasourceTestDTO dto = mysqlDto("example.com");
		dto.setDsName("safe?allowLoadLocalInfile=true");

		assertThatThrownBy(() -> JdbcUtils.buildJdbcUrl(dto, false)).isInstanceOf(CheckedException.class)
			.hasMessage("数据库名称不合法");
	}

	@Test
	void buildJdbcUrlShouldRejectUnsafeExtraParameter() {
		DatasourceTestDTO dto = mysqlDto("example.com");
		dto.setExtraJdbc("allowLoadLocalInfile=true");

		assertThatThrownBy(() -> JdbcUtils.buildJdbcUrl(dto, false)).isInstanceOf(CheckedException.class)
			.hasMessage("额外JDBC参数不允许: allowLoadLocalInfile");
	}

	@Test
	void buildJdbcUrlShouldUseSafeWhitelistedParameters() {
		DatasourceTestDTO dto = mysqlDto("example.com");
		dto.setExtraJdbc("serverTimezone=Asia/Shanghai&useSSL=true");

		String url = JdbcUtils.buildJdbcUrl(dto, false);

		assertThat(url).isEqualTo("jdbc:mysql://example.com:3306/test_db"
				+ "?characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true"
				+ "&serverTimezone=Asia/Shanghai&useSSL=true");
	}

	@Test
	void buildJdbcUrlShouldUseSemicolonForSqlServerParameters() {
		DatasourceTestDTO dto = new DatasourceTestDTO();
		dto.setDsType("sqlServer");
		dto.setHost("example.com");
		dto.setPort(1433);
		dto.setDsName("test_db");
		dto.setExtraJdbc("encrypt=true");

		String url = JdbcUtils.buildJdbcUrl(dto, false);

		assertThat(url).isEqualTo("jdbc:sqlserver://example.com:1433;database=test_db;"
				+ "characterEncoding=UTF-8;encrypt=true");
	}

	private DatasourceTestDTO mysqlDto(String host) {
		DatasourceTestDTO dto = new DatasourceTestDTO();
		dto.setDsType("mysql");
		dto.setHost(host);
		dto.setPort(3306);
		dto.setDsName("test_db");
		return dto;
	}
}
