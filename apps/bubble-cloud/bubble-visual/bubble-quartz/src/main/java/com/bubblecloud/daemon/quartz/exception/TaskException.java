package com.bubblecloud.daemon.quartz.exception;

/**
 * 定时任务异常
 *
 * @author qinlei
 * @date 2025/05/31
 */
public class TaskException extends Exception {

	public TaskException() {
		super();
	}

	public TaskException(String msg) {
		super(msg);
	}

}
