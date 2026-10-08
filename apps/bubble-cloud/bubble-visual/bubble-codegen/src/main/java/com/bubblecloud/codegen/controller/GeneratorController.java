package com.bubblecloud.codegen.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.bubblecloud.codegen.service.GeneratorService;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.common.security.annotation.HasPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 *
 * @author qinlei
 * @date 2025/05/31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/generator")
public class GeneratorController {

	private final GeneratorService generatorService;

	/**
	 * ZIP 下载生成代码
	 * @param tableIds 数据表ID
	 * @param response 流输出对象
	 */
	@SneakyThrows
	@GetMapping("/download")
	@HasPermission("codegen_template_view")
	public void download(String tableIds, HttpServletResponse response) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		// 生成代码
		for (String tableId : tableIds.split(StrUtil.COMMA)) {
			generatorService.downloadCode(Long.parseLong(tableId), zip);
		}

		IoUtil.close(zip);

		// zip压缩包数据
		byte[] data = outputStream.toByteArray();

		response.reset();
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=code.zip");
		response.setContentLength(data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IoUtil.write(response.getOutputStream(), false, data);
	}

	/**
	 * 目标目录生成代码
	 */
	@ResponseBody
	@GetMapping("/code")
	@HasPermission("codegen_template_add")
	public R<String> code(String tableIds) throws Exception {
		// 生成代码
		for (String tableId : tableIds.split(StrUtil.COMMA)) {
			generatorService.generatorCode(Long.valueOf(tableId));
		}

		return R.ok();
	}

	/**
	 * 检测生成路径是否存在
	 * @param path 待检测路径
	 * @return true 表示路径存在且为目录
	 */
	@GetMapping("/check-path")
	@HasPermission("codegen_template_add")
	public R<Boolean> checkPath(String path) {
		return R.ok(generatorService.checkPath(path));
	}

	/**
	 * 预览代码
	 * @param tableId 表ID
	 * @return
	 */
	@SneakyThrows
	@GetMapping("/preview")
	@HasPermission("codegen_template_view")
	public List<Map<String, String>> preview(Long tableId) {
		return generatorService.preview(tableId);
	}

}
