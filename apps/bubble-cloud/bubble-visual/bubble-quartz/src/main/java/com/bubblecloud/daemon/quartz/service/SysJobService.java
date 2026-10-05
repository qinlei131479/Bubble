package com.bubblecloud.daemon.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.daemon.quartz.entity.SysJob;

/**
 * 定时任务调度表
 *
 * @author qinlei
 * @date 2025/05/31
 */
public interface SysJobService extends IService<SysJob> {

	/**
	 * 检查任务配置
	 * @param field 字段
	 * @param sysJob sys 作业
	 * @return {@link R }
	 */
	R checkJob(String field, SysJob sysJob);

}
