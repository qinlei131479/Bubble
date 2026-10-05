package com.bubblecloud.common.feign.annotation;

import com.bubblecloud.common.feign.CustomFeignClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 启用 Feign 客户端注解。默认扫描 com.bubblecloud，并带上 Jackson 2 消息转换器预热配置。
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableCustomFeignClients {

	/**
	 * {@link #basePackages()} 属性的别名。
	 * @return 基础包路径数组
	 */
	String[] value() default {};

	/**
	 * 扫描注解组件的基础包路径。默认只扫描 Bubble 包。
	 * @return 基础包路径数组
	 */
	@AliasFor(annotation = EnableFeignClients.class)
	String[] basePackages() default { "com.bubblecloud" };

	/**
	 * {@link #basePackages()} 的类型安全替代。
	 * @return 用于确定扫描包的类
	 */
	@AliasFor(annotation = EnableFeignClients.class)
	Class<?>[] basePackageClasses() default {};

	/**
	 * 所有 Feign 客户端的默认配置。显式覆盖时请保留 {@link CustomFeignClientConfiguration}，否则会丢掉 Jackson 2 转换器预热。
	 * @return 配置类
	 * @see FeignClientsConfiguration
	 */
	@AliasFor(annotation = EnableFeignClients.class)
	Class<?>[] defaultConfiguration() default { CustomFeignClientConfiguration.class };

	/**
	 * 显式列出的 Feign 客户端。非空时关闭包扫描。
	 * @return 客户端类
	 */
	@AliasFor(annotation = EnableFeignClients.class)
	Class<?>[] clients() default {};

}
