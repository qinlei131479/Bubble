package com.bubblecloud.common.file.local;

import com.bubblecloud.common.file.core.FileProperties;
import com.bubblecloud.common.file.core.FileTemplate;
import com.bubblecloud.common.file.core.LocalFileTypeCondition;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * aws 自动配置类
 *
 * @author lengleng
 * @author 858695266
 */
@AllArgsConstructor
public class LocalFileAutoConfiguration {

	private final FileProperties properties;

	@Bean
	@ConditionalOnMissingBean(LocalFileTemplate.class)
	@Conditional(LocalFileTypeCondition.class)
	public FileTemplate localFileTemplate() {
		return new LocalFileTemplate(properties);
	}

}
