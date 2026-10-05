package com.bubblecloud.daemon.quartz.event;

import com.bubblecloud.daemon.quartz.entity.SysJobLog;
import com.bubblecloud.daemon.quartz.service.SysJobLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 系统任务日志监听器：用于异步监听并处理定时任务日志事件
 *
 * @author frwcloud
 * @author qinlei
 * @date 2025/05/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysJobLogListener {

	private final SysJobLogService sysJobLogService;

	@Async
	@Order
	@EventListener(SysJobLogEvent.class)
	public void saveSysJobLog(SysJobLogEvent event) {
		SysJobLog sysJobLog = event.getSysJobLog();
		// 只保存发布状态的任务日志
		if (Objects.nonNull(sysJobLog.getJobId())) {
			sysJobLogService.save(sysJobLog);
		}
		log.info("执行定时任务日志");
	}

}
