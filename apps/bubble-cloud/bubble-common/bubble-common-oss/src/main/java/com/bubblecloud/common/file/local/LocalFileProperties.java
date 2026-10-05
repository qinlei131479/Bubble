package com.bubblecloud.common.file.local;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;

/**
 * 本地文件 配置信息
 *
 * @author lengleng
 * <p>
 * bucket 设置公共读权限
 */
@Data
@ConfigurationProperties(prefix = "local")
public class LocalFileProperties {

	/**
	 * 是否开启
	 */
	@Deprecated
	private boolean enable;

	/**
	 * 默认路径
	 */
	private String basePath;

	@Deprecated
	@DeprecatedConfigurationProperty(reason = "使用 file.type=local 替代 file.local.enable", replacement = "file.type")
	public boolean isEnable() {
		return enable;
	}

}
