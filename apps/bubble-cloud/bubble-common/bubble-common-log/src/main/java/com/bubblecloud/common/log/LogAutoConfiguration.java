package com.bubblecloud.common.log;

import com.bubblecloud.backend.api.feign.RemoteLogService;
import com.bubblecloud.common.log.config.CustomLogProperties;
import com.bubblecloud.common.log.aspect.SysLogAspect;
import com.bubblecloud.common.log.event.SysLogListener;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lengleng
 * @date 2018/6/28
 * <p>
 * 日志自动配置
 */
@EnableAsync
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CustomLogProperties.class)
@ConditionalOnProperty(value = "pig.log.enabled", matchIfMissing = true)
public class LogAutoConfiguration {

	@Bean
	public SysLogListener sysLogListener(CustomLogProperties logProperties, RemoteLogService remoteLogService) {
		return new SysLogListener(remoteLogService, logProperties);
	}

	@Bean
	public SysLogAspect sysLogAspect() {
		return new SysLogAspect();
	}

}
