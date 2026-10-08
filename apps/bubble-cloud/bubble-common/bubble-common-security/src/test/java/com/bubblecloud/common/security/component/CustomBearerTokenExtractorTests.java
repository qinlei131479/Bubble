package com.bubblecloud.common.security.component;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class CustomBearerTokenExtractorTests {

	@Test
	void shouldAcceptAuthorizationHeader() {
		CustomBearerTokenExtractor extractor = new CustomBearerTokenExtractor(new PermitAllUrlProperties());
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer token-value");

		assertThat(extractor.resolve(request)).isEqualTo("token-value");
	}

	@Test
	void shouldIgnoreCookieForRegularHttpRequest() {
		CustomBearerTokenExtractor extractor = new CustomBearerTokenExtractor(new PermitAllUrlProperties());
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(new Cookie("token", "cookie-token"));

		assertThat(extractor.resolve(request)).isNull();
	}

	@Test
	void shouldAcceptCookieForWebSocketUpgrade() {
		CustomBearerTokenExtractor extractor = new CustomBearerTokenExtractor(new PermitAllUrlProperties());
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(HttpHeaders.UPGRADE, "websocket");
		request.setCookies(new Cookie("token", "cookie-token"));

		assertThat(extractor.resolve(request)).isEqualTo("cookie-token");
	}

	@Test
	void shouldIgnoreLegacyQueryToken() {
		CustomBearerTokenExtractor extractor = new CustomBearerTokenExtractor(new PermitAllUrlProperties());
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("access_token", "url-token");

		assertThat(extractor.resolve(request)).isNull();
	}
}
