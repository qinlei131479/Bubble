package com.bubblecloud.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.bubblecloud.daemon.quartz.entity.SysJob;
import com.bubblecloud.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务可执行jar反射实现
 *
 * @author qinlei
 * @date 2025/05/31
 */
@Slf4j
@Component("jarTaskInvok")
public class JarTaskInvok implements ITaskInvok {

	/**
	 * jar 任务允许的根目录（可选）。配置后仅允许执行该目录内的jar；
	 * 为空时仅做基础校验，保持存量任务兼容。
	 */
	@Value("${spring.quartz.security.jar-base-dir:}")
	private String jarBaseDir;

	@Override
	public void invokMethod(SysJob sysJob) throws TaskException {
		String executePath = sysJob.getExecutePath();
		File jar = validateJarPath(executePath);
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.directory(jar.getParentFile());
		List<String> commands = new ArrayList<>();
		commands.add("java");
		commands.add("-jar");
		commands.add(jar.getAbsolutePath());
		if (StrUtil.isNotEmpty(sysJob.getMethodParamsValue())) {
			commands.add(sysJob.getMethodParamsValue());
		}
		processBuilder.command(commands);
		try {
			processBuilder.start();
		}
		catch (IOException e) {
			log.error("定时任务jar反射执行异常,执行任务：{}", executePath);
			throw new TaskException("定时任务jar反射执行异常,执行任务：" + executePath);
		}
	}

	/**
	 * 校验 jar 路径，防止通过定时任务执行任意文件：
	 * 仅允许 .jar 文件；拒绝路径跳转（..）；配置了 jar-base-dir 时必须位于该目录内。
	 * @param executePath 任务配置的执行路径
	 * @return 校验通过的jar文件
	 */
	private File validateJarPath(String executePath) throws TaskException {
		if (StrUtil.isBlank(executePath)) {
			throw new TaskException("定时任务jar路径不能为空");
		}
		if (executePath.contains("..")) {
			log.error("Security violation: jar task path contains '..': {}", executePath);
			throw new TaskException("定时任务jar路径不合法");
		}
		if (!executePath.toLowerCase().endsWith(".jar")) {
			log.error("Security violation: jar task path is not a jar file: {}", executePath);
			throw new TaskException("定时任务仅允许执行jar文件");
		}

		File jar = new File(executePath);
		if (!jar.isFile()) {
			throw new TaskException("定时任务jar文件不存在: " + executePath);
		}

		if (StrUtil.isNotBlank(jarBaseDir)) {
			try {
				String canonical = jar.getCanonicalPath();
				String base = new File(jarBaseDir).getCanonicalPath();
				if (!canonical.startsWith(base + File.separator)) {
					log.error("Security violation: jar task outside base dir: {}", executePath);
					throw new TaskException("定时任务jar必须位于配置目录内: " + jarBaseDir);
				}
			}
			catch (IOException e) {
				throw new TaskException("定时任务jar路径解析失败: " + executePath);
			}
		}
		return jar;
	}

}
