package com.bubblecloud.daemon.quartz.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubblecloud.daemon.quartz.entity.SysJobLog;
import com.bubblecloud.daemon.quartz.service.SysJobLogService;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * @author frwcloud
 * @author qinlei
 * @date 2025/05/31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-job-log")
@Tag(description = "sys-job-log", name = "定时任务日志")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysJobLogController {

	private final SysJobLogService sysJobLogService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysJobLog 定时任务执行日志表
	 * @return
	 */
	@GetMapping("/page")
	@Operation(description = "分页定时任务日志查询")
	@HasPermission({ "job_sys_job_add", "job_sys_job_edit", "job_sys_job_export" })
	public R getSysJobLogPage(Page page, SysJobLog sysJobLog) {
		return R.ok(sysJobLogService.page(page, Wrappers.query(sysJobLog)));
	}

	@DeleteMapping
	@Operation(description = "批量删除日志")
	@HasPermission("job_sys_job_del")
	public R deleteLogs(@RequestBody Long[] ids) {
		return R.ok(sysJobLogService.removeBatchByIds(CollUtil.toList(ids)));
	}

}
