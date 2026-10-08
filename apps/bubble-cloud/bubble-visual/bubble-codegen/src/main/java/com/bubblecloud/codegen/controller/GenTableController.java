package com.bubblecloud.codegen.controller;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubblecloud.codegen.service.GenTableColumnService;
import com.bubblecloud.codegen.service.GenTableService;
import com.bubblecloud.codegen.entity.GenTable;
import com.bubblecloud.codegen.entity.GenTableColumnEntity;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.common.excel.annotation.ResponseExcel;
import com.bubblecloud.common.log.annotation.SysLog;
import com.bubblecloud.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 列属性
 *
 * @author qinlei
 * @date 2025/05/31
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/table")
@Tag(description = "table", name = "列属性管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenTableController {

	private final GenTableColumnService tableColumnService;

	private final GenTableService tableService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param table 列属性
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("codegen_template_view")
	public R getTablePage(Page page, GenTable table) {
		return R.ok(tableService.queryTablePage(page, table));
	}

	/**
	 * 通过id查询表信息（代码生成设置 + 表 + 字段设置）
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	@HasPermission("codegen_template_view")
	public R getTable(@PathVariable("id") Long id) {
		return R.ok(tableService.getById(id));
	}

	/**
	 * 查询数据源所有表
	 * @param dsName 数据源
	 */
	@GetMapping("/list/{dsName}")
	@HasPermission("codegen_template_view")
	public R listTable(@PathVariable("dsName") String dsName) {
		return R.ok(tableService.queryTableList(dsName));
	}

	/**
	 * 获取表信息
	 * @param dsName 数据源
	 * @param tableName 表名称
	 */
	@GetMapping("/{dsName}/{tableName}")
	@HasPermission("codegen_template_view")
	public R<GenTable> getTable(@PathVariable("dsName") String dsName, @PathVariable String tableName) {
		if (SqlInjectionUtils.check(tableName)) {
			log.warn("代码生成检测到非法表名，dsName: {}, tableName: {}", dsName, tableName);
			return R.failed("非法内容");
		}
		return R.ok(tableService.queryOrBuildTable(dsName, tableName));
	}

	/**
	 * 查询表DDL语句
	 * @param dsName 数据源
	 * @param tableName 表名称
	 */
	@GetMapping("/column/{dsName}/{tableName}")
	@HasPermission("codegen_template_view")
	public R getColumn(@PathVariable("dsName") String dsName, @PathVariable String tableName) throws Exception {
		if (SqlInjectionUtils.check(tableName)) {
			log.warn("代码生成检测到非法表名，dsName: {}, tableName: {}", dsName, tableName);
			return R.failed("非法内容");
		}
		return R.ok(tableService.queryTableColumn(dsName, tableName));
	}

	/**
	 * 查询表DDL语句
	 * @param dsName 数据源
	 * @param tableName 表名称
	 */
	@GetMapping("/ddl/{dsName}/{tableName}")
	@HasPermission("codegen_template_view")
	public R getDdl(@PathVariable("dsName") String dsName, @PathVariable String tableName) throws Exception {
		if (SqlInjectionUtils.check(tableName)) {
			log.warn("代码生成检测到非法表名，dsName: {}, tableName: {}", dsName, tableName);
			return R.failed("非法内容");
		}
		return R.ok(tableService.queryTableDdl(dsName, tableName));
	}

	/**
	 * 同步表信息
	 * @param dsName 数据源名称
	 * @param tableName 表名称
	 * @return 操作结果
	 */
	@GetMapping("/sync/{dsName}/{tableName}")
	@HasPermission("codegen_template_add")
	public R<GenTable> syncTable(@PathVariable("dsName") String dsName, @PathVariable String tableName) {
		if (SqlInjectionUtils.check(tableName)) {
			log.warn("代码生成检测到非法表名，dsName: {}, tableName: {}", dsName, tableName);
			return R.failed("非法内容");
		}
		return R.ok(tableService.syncTable(dsName, tableName));
	}

	/**
	 * 修改列属性
	 * @param table 列属性
	 * @return R
	 */
	@Operation(summary = "修改列属性", description = "修改列属性")
	@SysLog("修改列属性")
	@PutMapping
	@HasPermission("codegen_template_edit")
	public R updateById(@RequestBody GenTable table) {
		return R.ok(tableService.updateById(table));
	}

	/**
	 * 修改表字段数据
	 * @param dsName 数据源
	 * @param tableName 表名称
	 * @param tableFieldList 字段列表
	 */
	@PutMapping("/field/{dsName}/{tableName}")
	@HasPermission("codegen_template_edit")
	public R<String> updateTableField(@PathVariable("dsName") String dsName, @PathVariable String tableName,
			@RequestBody List<GenTableColumnEntity> tableFieldList) {
		tableColumnService.updateTableField(dsName, tableName, tableFieldList);
		return R.ok();
	}

	/**
	 * 导出excel 表格
	 * @param table 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("codegen_template_export")
	public List<GenTable> export(GenTable table) {
		// 切换至对应数据源
		DynamicDataSourceContextHolder.push(table.getDsName());
		return tableService.list(Wrappers.query(table));
	}

}
