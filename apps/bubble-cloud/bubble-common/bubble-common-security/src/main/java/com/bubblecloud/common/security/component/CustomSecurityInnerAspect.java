package com.bubblecloud.common.security.component;

import cn.hutool.core.util.StrUtil;
import com.bubblecloud.common.core.constant.SecurityConstants;
import com.bubblecloud.common.security.annotation.Inner;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

/**
 * @author lengleng
 * @date 2022-06-04
 *
 * 服务间接口不鉴权处理逻辑
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class CustomSecurityInnerAspect implements Ordered {

	private final HttpServletRequest request;

	@SneakyThrows
	@Before("@within(inner) || @annotation(inner)")
	public void around(JoinPoint point, Inner inner) {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (Objects.isNull(attrs)) {
			return;
		}

		String header = request.getHeader(SecurityConstants.FROM);
		if (inner == null) {
			Class<?> clazz = point.getTarget().getClass();
			inner = AnnotationUtils.findAnnotation(clazz, Inner.class);
		}

		if (inner.value() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
			log.warn("访问接口 {} 没有权限", point.getSignature().getName());
			throw new AccessDeniedException("Access is denied");
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

}
