package com.bubblecloud.daemon.quartz.config;

import com.bubblecloud.daemon.quartz.constants.CustomQuartzEnum;
import com.bubblecloud.daemon.quartz.service.SysJobService;
import com.bubblecloud.daemon.quartz.util.TaskUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 郑健楠
 * <p>
 * 初始化加载定时任务
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomInitQuartzJob implements InitializingBean {

	private final SysJobService sysJobService;

	private final TaskUtil taskUtil;

	private final Scheduler scheduler;

	@Override
	public void afterPropertiesSet() throws Exception {
		sysJobService.list().forEach(sysjob -> {
			if (CustomQuartzEnum.JOB_STATUS_RELEASE.getType().equals(sysjob.getJobStatus())) {
				taskUtil.removeJob(sysjob, scheduler);
			}
			else if (CustomQuartzEnum.JOB_STATUS_RUNNING.getType().equals(sysjob.getJobStatus())) {
				taskUtil.resumeJob(sysjob, scheduler);
			}
			else if (CustomQuartzEnum.JOB_STATUS_NOT_RUNNING.getType().equals(sysjob.getJobStatus())) {
				taskUtil.pauseJob(sysjob, scheduler);
			}
			else {
				taskUtil.removeJob(sysjob, scheduler);
			}
		});
	}

}
