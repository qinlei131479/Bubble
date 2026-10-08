package com.bubblecloud.biz.agi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 用户配置的 AGI JDBC 数据源安全选项。
 * 值来自 Nacos 中的 agi.datasource.allow-private-network，配置变更后刷新生效。
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "agi.datasource")
public class DatasourceSecurityProperties {

	/**
	 * 允许访问回环地址、RFC1918 私网、唯一本地地址和共享地址段。
	 * Nacos 未配置该项时为 true。即使开启，链路本地地址、组播地址和云厂商元数据地址仍然拒绝。
	 */
	private boolean allowPrivateNetwork = true;
}
