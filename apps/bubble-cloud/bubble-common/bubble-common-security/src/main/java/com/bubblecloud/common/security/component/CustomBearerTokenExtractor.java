package com.bubblecloud.common.security.component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

	private boolean allowFormEncodedBodyParameter = false;

	private boolean allowUriQueryParameter = true;

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
		String parameterToken = isParameterTokenSupportedForRequest(request) ? resolveFromRequestParameters(request)
				: null;

		if (authorizationHeaderToken != null) {
			if (parameterToken != null) {
				BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens");
				throw new OAuth2AuthenticationException(error);
			}
			return authorizationHeaderToken;
		}

		if (parameterToken != null && isParameterTokenEnabledForRequest(request)) {
			return parameterToken;
		}
		return null;
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

	private static String resolveFromRequestParameters(HttpServletRequest request) {
		String[] values = request.getParameterValues("access_token");
		if (values == null || values.length == 0) {
			return null;
		}
		if (values.length == 1) {
			return values[0];
		}
		BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens");
		throw new OAuth2AuthenticationException(error);
	}

	private boolean isParameterTokenSupportedForRequest(HttpServletRequest request) {
		return ("POST".equals(request.getMethod())
				&& MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
				|| "GET".equals(request.getMethod());
	}

	private boolean isParameterTokenEnabledForRequest(HttpServletRequest request) {
		return (allowFormEncodedBodyParameter && "POST".equals(request.getMethod())
				&& MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
				|| (allowUriQueryParameter && "GET".equals(request.getMethod()));
	}

}
