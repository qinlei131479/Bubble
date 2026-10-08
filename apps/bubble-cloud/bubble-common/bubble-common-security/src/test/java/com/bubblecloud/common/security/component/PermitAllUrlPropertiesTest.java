package com.bubblecloud.common.security.component;

import com.bubblecloud.common.core.util.SpringContextHolder;
import com.bubblecloud.common.security.annotation.Inner;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PermitAllUrlPropertiesTest {

	private ApplicationContext originalContext;

	@BeforeEach
	void setUp() throws Exception {
		originalContext = (ApplicationContext) contextField().get(null);
	}

	@AfterEach
	void tearDown() throws Exception {
		contextField().set(null, originalContext);
	}

	@Test
	void shouldOnlyPermitInnerUrlsMarkedAsInner() throws Exception {
		RequestMappingHandlerMapping mapping = mock(RequestMappingHandlerMapping.class);
		when(mapping.getHandlerMethods()).thenReturn(Map.of(requestMapping("/inner-true"), handler("innerTrue"),
				requestMapping("/inner-false"), handler("innerFalse")));

		ApplicationContext context = mock(ApplicationContext.class);
		when(context.getBean("requestMappingHandlerMapping")).thenReturn(mapping);
		contextField().set(null, context);

		PermitAllUrlProperties properties = new PermitAllUrlProperties();
		properties.afterPropertiesSet();

		assertThat(properties.isPermitAll("/inner-true")).isTrue();
		assertThat(properties.isPermitAll("/inner-false")).isFalse();
		assertThat(properties.isPermitAll("/actuator/health")).isTrue();
		assertThat(properties.isPermitAll("/actuator/env")).isFalse();
		assertThat(properties.isPermitAll("/register/user")).isTrue();
		assertThat(properties.isPermitAll("/system/config")).isTrue();
		assertThat(properties.isPermitAll("/sysMessage/send/smsCode")).isTrue();
	}

	private RequestMappingInfo requestMapping(String path) {
		return RequestMappingInfo.paths(path).build();
	}

	private HandlerMethod handler(String methodName) throws NoSuchMethodException {
		return new HandlerMethod(new TestController(), TestController.class.getDeclaredMethod(methodName));
	}

	private static Field contextField() throws NoSuchFieldException {
		Field field = SpringContextHolder.class.getDeclaredField("applicationContext");
		field.setAccessible(true);
		return field;
	}

	static class TestController {

		@Inner(true)
		public void innerTrue() {
		}

		@Inner(false)
		public void innerFalse() {
		}
	}
}
