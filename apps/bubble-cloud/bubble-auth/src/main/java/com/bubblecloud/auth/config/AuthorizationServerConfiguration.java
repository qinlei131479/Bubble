package com.bubblecloud.auth.config;

import com.bubblecloud.auth.support.CustomeOAuth2AccessTokenGenerator;
import com.bubblecloud.auth.support.core.CustomeOAuth2TokenCustomizer;
import com.bubblecloud.auth.support.core.FormIdentityLoginConfigurer;
import com.bubblecloud.auth.support.core.CustomDaoAuthenticationProvider;
import com.bubblecloud.auth.support.core.CustomOAuth2RefreshTokenAuthenticationConverter;
import com.bubblecloud.auth.support.filter.FormLoginValidateCodeFilter;
import com.bubblecloud.auth.support.filter.PasswordDecoderFilter;
import com.bubblecloud.auth.support.filter.ValidateCodeFilter;
import com.bubblecloud.auth.support.handler.CustomAuthenticationFailureEventHandler;
import com.bubblecloud.auth.support.handler.CustomAuthenticationSuccessEventHandler;
import com.bubblecloud.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.bubblecloud.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.bubblecloud.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationConverter;
import com.bubblecloud.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationProvider;
import com.bubblecloud.common.core.constant.SecurityConstants;
import com.bubblecloud.common.security.component.BootCorsProperties;
import com.bubblecloud.common.security.handler.FormAuthenticationFailureHandler;
import com.bubblecloud.common.security.handler.SsoLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * @author lengleng
 * @date 2022/5/27
 * <p>
 * 认证服务器配置
 */
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfiguration {

	private final OAuth2AuthorizationService authorizationService;

	private final PasswordDecoderFilter passwordDecoderFilter;

	private final ValidateCodeFilter validateCodeFilter;

	private final FormLoginValidateCodeFilter formLoginValidateCodeFilter;

	private final BootCorsProperties corsProperties;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			CustomAuthenticationSuccessEventHandler successEventHandler,
			CustomAuthenticationFailureEventHandler failureEventHandler) throws Exception {
		// 配置授权服务器的安全策略，只有/oauth2/**的请求才会走如下的配置
		http.securityMatcher("/oauth2/**");
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

		// 增加验证码过滤器
		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
		// 增加授权码登录表单验证码过滤器
		http.addFilterBefore(formLoginValidateCodeFilter, UsernamePasswordAuthenticationFilter.class);
		// 增加密码解密过滤器
		http.addFilterBefore(passwordDecoderFilter, UsernamePasswordAuthenticationFilter.class);

		http.with(authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {// 个性化认证授权端点
			tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter()) // 注入自定义的授权认证Converter
				.accessTokenResponseHandler(successEventHandler) // 登录成功处理器
				.errorResponseHandler(failureEventHandler);// 登录失败处理器
		}).clientAuthentication(oAuth2ClientAuthenticationConfigurer -> // 个性化客户端认证
		oAuth2ClientAuthenticationConfigurer.errorResponseHandler(failureEventHandler))// 处理客户端认证异常
			.authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint// 授权码端点个性化confirm页面
				.consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)), Customizer.withDefaults())
			.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated());

		// 设置 Token 存储的策略
		http.with(authorizationServerConfigurer.authorizationService(authorizationService)// redis存储token的实现
			.authorizationServerSettings(
					AuthorizationServerSettings.builder().issuer(SecurityConstants.PIG_LICENSE).build()),
				Customizer.withDefaults());

		// 设置授权码模式登录页面
		http.with(new FormIdentityLoginConfigurer(), Customizer.withDefaults());
		DefaultSecurityFilterChain securityFilterChain = http.build();

		// 单体版本：根据配置启用CORS跨域支持（仅在security.cors.enabled=true时生效）
		if (Boolean.TRUE.equals(corsProperties.getEnabled())) {
			http.cors(cors -> cors.configurationSource(request -> {
				UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
				CorsConfiguration corsConfiguration = new CorsConfiguration();
				corsProperties.getAllowedOriginPatterns().forEach(corsConfiguration::addAllowedOriginPattern);
				corsProperties.getAllowedHeaders().forEach(corsConfiguration::addAllowedHeader);
				corsProperties.getAllowedMethods().forEach(corsConfiguration::addAllowedMethod);
				corsConfiguration.setAllowCredentials(corsProperties.getAllowCredentials());
				source.registerCorsConfiguration(corsProperties.getPathPattern(), corsConfiguration);
				return corsConfiguration;
			}));
		}

		// 注入自定义授权模式实现
		addCustomOAuth2GrantAuthenticationProvider(http);
		return securityFilterChain;
	}

	/**
	 * 令牌生成规则实现 </br>
	 * client:username:uuid
	 *
	 * @return OAuth2TokenGenerator
	 */
	@Bean
	public OAuth2TokenGenerator oAuth2TokenGenerator() {
		CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();
		// 注入Token 增加关联用户信息
		accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());
		return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new FormAuthenticationFailureHandler();
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new SsoLogoutSuccessHandler();
	}

	/**
	 * request -> xToken 注入请求转换器
	 *
	 * @return DelegatingAuthenticationConverter
	 */
	private AuthenticationConverter accessTokenRequestConverter() {
		return new DelegatingAuthenticationConverter(Arrays.asList(
				new OAuth2ResourceOwnerPasswordAuthenticationConverter(),
				new OAuth2ResourceOwnerSmsAuthenticationConverter(), new CustomOAuth2RefreshTokenAuthenticationConverter(),
				new OAuth2ClientCredentialsAuthenticationConverter(),
				new OAuth2AuthorizationCodeAuthenticationConverter()));
	}

	/**
	 * 注入授权模式实现提供方
	 * <p>
	 * 1. 密码模式 </br>
	 * 2. 短信登录 </br>
	 */
	@SuppressWarnings("unchecked")
	private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

		OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
				authenticationManager, authorizationService, oAuth2TokenGenerator());

		OAuth2ResourceOwnerSmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new OAuth2ResourceOwnerSmsAuthenticationProvider(
				authenticationManager, authorizationService, oAuth2TokenGenerator());

		// 处理 UsernamePasswordAuthenticationToken
		http.authenticationProvider(new CustomDaoAuthenticationProvider());
		// 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
		http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
		// 处理 OAuth2ResourceOwnerSmsAuthenticationToken
		http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
	}


	/**
	 * 配置 CORS 跨域资源共享
	 *
	 * @return UrlBasedCorsConfigurationSource CORS配置源
	 */
	private UrlBasedCorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		// 从配置文件读取允许的源模式
		corsProperties.getAllowedOriginPatterns().forEach(corsConfiguration::addAllowedOriginPattern);
		// 从配置文件读取允许的请求头
		corsProperties.getAllowedHeaders().forEach(corsConfiguration::addAllowedHeader);
		// 从配置文件读取允许的HTTP方法
		corsProperties.getAllowedMethods().forEach(corsConfiguration::addAllowedMethod);
		// 从配置文件读取是否允许携带凭证
		corsConfiguration.setAllowCredentials(corsProperties.getAllowCredentials());

		// 注册CORS配置到指定路径
		source.registerCorsConfiguration(corsProperties.getPathPattern(), corsConfiguration);

		return source;
	}

}
