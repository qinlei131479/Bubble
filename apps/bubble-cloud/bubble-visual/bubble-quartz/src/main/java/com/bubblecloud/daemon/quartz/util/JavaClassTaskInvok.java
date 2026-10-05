package com.bubblecloud.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.bubblecloud.daemon.quartz.entity.SysJob;
import com.bubblecloud.daemon.quartz.constants.QuartzEnum;
import com.bubblecloud.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 定时任务java class反射实现
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Component("javaClassTaskInvok")
@Slf4j
public class JavaClassTaskInvok implements ITaskInvok {

	private final JobSecurityValidator jobSecurityValidator;

	public JavaClassTaskInvok(JobSecurityValidator jobSecurityValidator) {
		this.jobSecurityValidator = jobSecurityValidator;
	}

	@Override
	public void invokMethod(SysJob sysJob) throws TaskException {
		// Security validation before reflection
		// 在执行反射之前进行安全验证
		String securityError = jobSecurityValidator.validateJobConfig(sysJob.getClassName(), sysJob.getMethodName(),
				sysJob.getMethodParamsValue());

		if (securityError != null) {
			log.error("Security validation failed during job execution: {}", securityError);
			throw new TaskException("安全验证失败: " + securityError);
		}

		Object obj;
		Class clazz;
		Method method;
		Object returnValue;
		try {
			if (StrUtil.isNotEmpty(sysJob.getMethodParamsValue())) {
				clazz = Class.forName(sysJob.getClassName());
				obj = clazz.newInstance();
				method = clazz.getDeclaredMethod(sysJob.getMethodName(), String.class);
				returnValue = method.invoke(obj, sysJob.getMethodParamsValue());
			}
			else {
				clazz = Class.forName(sysJob.getClassName());
				obj = clazz.newInstance();
				method = clazz.getDeclaredMethod(sysJob.getMethodName());
				returnValue = method.invoke(obj);
			}
			if (StrUtil.isEmpty(returnValue.toString())
					|| QuartzEnum.JOB_LOG_STATUS_FAIL.getType().equals(returnValue.toString())) {
				log.error("定时任务javaClassTaskInvok异常,执行任务：{}", sysJob.getClassName());
				throw new TaskException("定时任务javaClassTaskInvok业务执行失败,任务：" + sysJob.getClassName());
			}
		}
		catch (ClassNotFoundException e) {
			log.error("定时任务java反射类没有找到,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射类没有找到,执行任务：" + sysJob.getClassName());
		}
		catch (IllegalAccessException e) {
			log.error("定时任务java反射类异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射类异常,执行任务：" + sysJob.getClassName());
		}
		catch (InstantiationException e) {
			log.error("定时任务java反射类异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射类异常,执行任务：" + sysJob.getClassName());
		}
		catch (NoSuchMethodException e) {
			log.error("定时任务java反射执行方法名异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射执行方法名异常,执行任务：" + sysJob.getClassName());
		}
		catch (InvocationTargetException e) {
			log.error("定时任务java反射执行异常,执行任务：{}", sysJob.getClassName());
			throw new TaskException("定时任务java反射执行异常,执行任务：" + sysJob.getClassName());
		}

	}

}
