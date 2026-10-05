package com.bubblecloud.biz.backend.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubblecloud.backend.api.dto.SysLogDTO;
import com.bubblecloud.backend.api.entity.SysLog;
import com.bubblecloud.backend.api.vo.PreLogVO;
import com.bubblecloud.biz.backend.service.SysLogService;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.common.excel.annotation.ResponseExcel;
import com.bubblecloud.common.security.annotation.HasPermission;
import com.bubblecloud.common.security.annotation.Inner;
import com.bubblecloud.common.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2017-11-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/log")
@Tag(description = "log", name = "日志管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysLogController {

	private final SysLogService sysLogService;

	/**
	 * 简单分页查询
	 * @param page 分页对象
	 * @param sysLog 系统日志
	 * @return
	 */
	@GetMapping("/page")
	@HasPermission("sys_log_view")
	public R getLogPage(@ParameterObject Page page, @ParameterObject SysLogDTO sysLog) {
		return R.ok(sysLogService.getLogByPage(page, sysLog));
	}

	/**
	 * 统计三十天内的数据
	 * @return R
	 */
	@GetMapping("/sum")
	public R getLogSum() {
		return R.ok(sysLogService.getLogSum());
	}

	/**
	 * 批量删除日志
	 * @param ids ID
	 * @return success/false
	 */
	@DeleteMapping
	@HasPermission("sys_log_del")
	public R removeByIds(@RequestBody Long[] ids) {
		return R.ok(sysLogService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 插入日志
	 * @param sysLog 日志实体
	 * @return success/false
	 */
	@Inner
	@PostMapping("/save")
	public R save(@Valid @RequestBody SysLogDTO sysLog) {
		return R.ok(sysLogService.saveLog(sysLog));
	}

	/**
	 * 批量插入前端异常日志
	 * @param preLogVoList 日志实体
	 * @return success/false
	 */
	@PostMapping("/logs")
	public R saveBatchLogs(@RequestBody List<PreLogVO> preLogVoList) {
		return R.ok(sysLogService.saveBatchLogs(preLogVoList));
	}

	/**
	 * 导出excel 表格
	 * @param sysLog 查询条件
	 * @return
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_log_export")
	public List<SysLog> export(SysLog sysLog, Long[] ids) {
		return sysLogService
			.list(Wrappers.lambdaQuery(sysLog).in(Objects.nonNull(ids), SysLog::getId, CollUtil.toList(ids)));
	}

}
