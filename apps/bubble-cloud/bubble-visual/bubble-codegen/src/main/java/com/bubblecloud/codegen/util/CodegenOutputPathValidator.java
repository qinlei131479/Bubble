package com.bubblecloud.codegen.util;

import cn.hutool.core.util.StrUtil;
import com.bubblecloud.codegen.config.CodeGenDefaultProperties;
import com.bubblecloud.common.core.exception.CheckedException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
	 * 将代码生成输出限制在显式配置的根目录内。
 */
@Component
public class CodegenOutputPathValidator {

	private static final Pattern WINDOWS_ABSOLUTE_PATH = Pattern.compile("^[A-Za-z]:[/\\\\].*");

	private final List<Path> allowedRoots;

	public CodegenOutputPathValidator(CodeGenDefaultProperties properties) {
		this.allowedRoots = resolveAllowedRoots(properties.getAllowedOutputRoots());
		if (allowedRoots.isEmpty()) {
			throw new IllegalStateException("未配置可用的代码生成输出根目录");
		}
	}

	/**
	 * 校验路径检查接口使用的目录。
	 */
	public Path validateDirectory(String path) {
		Path candidate = validateOutputFile(path);
		if (!Files.isDirectory(candidate)) {
			throw new CheckedException("路径不存在或不是目录");
		}
		return candidate;
	}

	/**
	 * 校验文件路径，并确保每个已存在的路径段都位于允许的根目录内。
	 */
	public Path validateOutputFile(String path) {
		if (StrUtil.isBlank(path)) {
			throw new CheckedException("路径无效");
		}
		final Path candidate;
		try {
			candidate = Path.of(path).toAbsolutePath().normalize();
		}
		catch (Exception e) {
			throw new CheckedException("路径无效");
		}

		for (Path root : allowedRoots) {
			Path existingAncestor = candidate;
			while (existingAncestor != null && !Files.exists(existingAncestor)) {
				existingAncestor = existingAncestor.getParent();
			}
			if (existingAncestor == null) {
				continue;
			}
			try {
				Path realAncestor = existingAncestor.toRealPath();
				Path resolvedCandidate = realAncestor.resolve(existingAncestor.relativize(candidate)).normalize();
				if (resolvedCandidate.startsWith(root)) {
					return resolvedCandidate;
				}
			}
			catch (IOException ignored) {
				// 尝试下一个已配置的根目录。
			}
		}
		throw new CheckedException("路径不在允许的代码生成目录内");
	}

	/**
	 * 将渲染后的路径转换为安全的ZIP相对路径。
	 */
	public String validateZipEntry(String path) {
		if (StrUtil.isBlank(path)) {
			throw new CheckedException("代码文件名无效");
		}
		String normalizedSeparators = path.replace('\\', '/');
		if (normalizedSeparators.startsWith("/") || WINDOWS_ABSOLUTE_PATH.matcher(normalizedSeparators).matches()) {
			throw new CheckedException("代码文件名不能是绝对路径");
		}
		Path normalized;
		try {
			normalized = Path.of(normalizedSeparators).normalize();
		}
		catch (Exception e) {
			throw new CheckedException("代码文件名无效");
		}
		String entry = normalized.toString().replace('\\', '/');
		if (StrUtil.isBlank(entry) || entry.equals("..") || entry.startsWith("../")) {
			throw new CheckedException("代码文件名包含路径穿越");
		}
		return entry;
	}

	private List<Path> resolveAllowedRoots(List<String> configuredRoots) {
		List<Path> roots = new ArrayList<>();
		if (configuredRoots != null) {
			for (String configuredRoot : configuredRoots) {
				if (StrUtil.isBlank(configuredRoot)) {
					continue;
				}
				addRealDirectory(roots, Path.of(configuredRoot));
			}
		}
		if (roots.isEmpty()) {
			Path current = Path.of("").toAbsolutePath().normalize();
			Path repositoryRoot = findGitRoot(current);
			addRealDirectory(roots, repositoryRoot == null ? current : repositoryRoot);
		}
		return List.copyOf(roots);
	}

	private void addRealDirectory(List<Path> roots, Path root) {
		try {
			Path realRoot = root.toAbsolutePath().normalize().toRealPath();
			if (Files.isDirectory(realRoot) && realRoot.getParent() != null && !roots.contains(realRoot)) {
				roots.add(realRoot);
			}
		}
		catch (IOException ignored) {
			// 忽略无效的已配置根目录；只有没有可用根目录时启动才失败。
		}
	}

	private Path findGitRoot(Path directory) {
		Path current = directory;
		while (current != null) {
			if (Files.isDirectory(current.resolve(".git"))) {
				return current;
			}
			current = current.getParent();
		}
		return null;
	}
}
