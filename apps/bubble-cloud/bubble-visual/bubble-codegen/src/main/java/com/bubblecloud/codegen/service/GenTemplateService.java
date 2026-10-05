package com.bubblecloud.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubblecloud.codegen.entity.GenTemplateEntity;
import com.bubblecloud.common.core.util.R;

/**
 * 模板
 *
 * @author qinlei
 * @date 2025/05/31
 */
public interface GenTemplateService extends IService<GenTemplateEntity> {

	/**
	 * 检查版本
	 * @return {@link R }
	 */
	R checkVersion();

	/**
	 * 在线更新
	 * @return {@link R }
	 */
	R onlineUpdate();

}
