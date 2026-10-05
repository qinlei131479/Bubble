package com.bubblecloud.common.log.event;

import com.bubblecloud.backend.api.dto.SysLogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @author lengleng 系统日志事件
 */
public class SysLogEvent extends ApplicationEvent {

	public SysLogEvent(SysLogDTO source) {
		super(source);
	}

}
