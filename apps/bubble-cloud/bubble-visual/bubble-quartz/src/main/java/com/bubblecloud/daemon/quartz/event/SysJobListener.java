package com.bubblecloud.daemon.quartz.event;

import com.bubblecloud.daemon.quartz.entity.SysJob;
import com.bubblecloud.daemon.quartz.util.TaskInvokUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * 系统任务监听器：用于异步监听并处理定时任务事件
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysJobListener {

	private final TaskInvokUtil taskInvokUtil;

	/**
	 * 处理 Quartz 发布的任务执行事件。
	 * @param event 当前任务执行事件，包含任务配置、触发器和执行元数据
	 */
	@Async
	@Order
	@EventListener(SysJobEvent.class)
	public void comSysJob(SysJobEvent event) {
		SysJob sysJob = event.getSysJob();
		Trigger trigger = event.getTrigger();
		taskInvokUtil.invokMethod(sysJob, trigger, event.getExecutionMetadata());
	}

}
