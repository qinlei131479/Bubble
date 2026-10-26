package com.bubblecloud.common.sentinel.handle;

import com.bubblecloud.common.core.constant.CommonConstants;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link GlobalBizExceptionHandler} 数据库异常脱敏测试
 */
class GlobalBizExceptionHandlerTests {

	private final GlobalBizExceptionHandler handler = new GlobalBizExceptionHandler();

	/**
	 * MyBatis 包装的 DataAccessException 消息携带完整 SQL 与敏感列名，必须返回固定文案而不是回显
	 */
	@Test
	void handleDataAccessExceptionMustNotExposeSql() {
		String sql = "select u.password, u.salt from sys_user u where u.username = 'admin'";
		BadSqlGrammarException exception = new BadSqlGrammarException("badSqlGrammar", sql,
				new SQLException("syntax error"));

		var result = handler.handleDataAccessException(exception);

		assertThat(result.getCode()).isEqualTo(CommonConstants.FAIL);
		assertThat(result.getMsg()).isEqualTo("数据库调用异常，请联系管理员处理");
		assertThat(result.getMsg()).doesNotContain("select").doesNotContain("password");
	}

}
