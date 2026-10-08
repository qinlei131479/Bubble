package com.bubblecloud.common.security.component;

import cn.hutool.core.util.ReUtil;
import com.bubblecloud.common.core.util.SpringContextHolder;
import com.bubblecloud.common.security.annotation.Inner;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 资源服务器对外直接暴露URL配置类。
 *
 * @author lengleng
 * @date 2025/05/31
 */
@ConfigurationProperties(prefix = "security.oauth2.ignore")
public class PermitAllUrlProperties implements InitializingBean, ApplicationListener<ApplicationReadyEvent> {

	private static final Pattern PATH_VARIABLE = Pattern.compile("\\{(.*?)\\}");

	private static final String[] DEFAULT_IGNORE_URLS =
			new String[] { "/actuator/health/**", "/actuator/liveness/**", "/actuator/readiness/**", "/error" };

	private static final String[] PUBLIC_AUTH_AND_CONFIG_URLS = new String[] { "/register/**", "/mobile/**",
			"/system/config", "/param/publicValue/**", "/param/publicValues", "/social/getLoginAppList",
			"/sysMessage/send/smsCode", "/sys-file/oss/file", "/user/details" };

	@Getter
	private final CopyOnWriteArrayList<String> urls = new CopyOnWriteArrayList<>();

	private final PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 上次扫描时的 Handler 数量。Boot 4 可能在映射注册完成前触发扫描，
	 * 数量变化后需要重新收集 @Inner 路径。
	 */
	private volatile int scannedHandlerCount = -1;

	@Override
	public void afterPropertiesSet() {
		urls.addAllAbsent(Arrays.asList(DEFAULT_IGNORE_URLS));
		urls.addAllAbsent(Arrays.asList(PUBLIC_AUTH_AND_CONFIG_URLS));
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		scanInnerUrls();
	}

	/**
	 * 判断请求是否配置或标记为免鉴权。
	 * @param request 当前请求
	 * @return 是否免鉴权
	 */
	public boolean isPermitAll(HttpServletRequest request) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		return isPermitAll(path);
	}

	public boolean isPermitAll(String path) {
		scanInnerUrls();
		return urls.stream().anyMatch(url -> pathMatcher.match(url, path));
	}

	/**
	 * Boot 4 在 advisor sorting 阶段会提前初始化安全配置，不能在 afterPropertiesSet 中
	 * 获取 RequestMappingHandlerMapping。映射可能在首次扫描之后才注册完成，因此按
	 * Handler 数量变化重新收集，避免把空结果冻结成最终白名单。
	 */
	private synchronized void scanInnerUrls() {
		RequestMappingHandlerMapping mapping;
		try {
			mapping = SpringContextHolder.getBean("requestMappingHandlerMapping");
		}
		catch (Exception ex) {
			return;
		}

		Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
		int handlerCount = handlerMethods.size();
		if (handlerCount == 0 || handlerCount == scannedHandlerCount) {
			return;
		}

		Set<String> innerUrls = new HashSet<>();
		handlerMethods.forEach((info, handlerMethod) -> {
			Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
			Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);
			if (method == null && controller == null) {
				return;
			}
			if (method != null ? !method.value() : !controller.value()) {
				return;
			}

			info.getPatternValues().forEach(url -> innerUrls.add(ReUtil.replaceAll(url, PATH_VARIABLE, "*")));
		});
		urls.addAllAbsent(innerUrls);
		scannedHandlerCount = handlerCount;
	}

}
