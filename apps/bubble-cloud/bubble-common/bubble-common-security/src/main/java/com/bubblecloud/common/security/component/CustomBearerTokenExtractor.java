package com.bubblecloud.common.security.component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.StringUtils;

/**
 * 请求令牌解析器。
 *
 * @author caiqy
 * @date 2020.05.15
 */
public class CustomBearerTokenExtractor implements BearerTokenResolver {

	private static final Pattern AUTHORIZATION_PATTERN =
			Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$", Pattern.CASE_INSENSITIVE);

	private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;

	private final PermitAllUrlProperties urlProperties;

	public CustomBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
		this.urlProperties = urlProperties;
	}

	@Override
	public String resolve(HttpServletRequest request) {
		if (urlProperties.isPermitAll(request)) {
			return null;
		}

		String authorizationHeaderToken = resolveFromAuthorizationHeader(request);
		String webSocketCookieToken = resolveFromWebSocketCookie(request);

		if (authorizationHeaderToken != null) {
			if (webSocketCookieToken != null) {
				BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens");
				throw new OAuth2AuthenticationException(error);
			}
			return authorizationHeaderToken;
		}

		return webSocketCookieToken;
	}

	private String resolveFromAuthorizationHeader(HttpServletRequest request) {
		String authorization = request.getHeader(bearerTokenHeaderName);
		if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
			return null;
		}

		Matcher matcher = AUTHORIZATION_PATTERN.matcher(authorization);
		if (!matcher.matches()) {
			BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
			throw new OAuth2AuthenticationException(error);
		}
		return matcher.group("token");
	}

	private static String resolveFromWebSocketCookie(HttpServletRequest request) {
		if (!"websocket".equalsIgnoreCase(request.getHeader(HttpHeaders.UPGRADE))) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if ("token".equals(cookie.getName()) && StringUtils.hasText(cookie.getValue())) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
