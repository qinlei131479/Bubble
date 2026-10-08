package com.bubblecloud.biz.agi.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubblecloud.biz.agi.service.SupplierService;
import com.bubblecloud.common.mybatis.base.Pg;
import com.bubblecloud.common.mybatis.base.Req;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.common.log.annotation.SysLog;
import com.bubblecloud.agi.api.entity.SupplierModel;
import com.bubblecloud.agi.api.entity.Supplier;
import com.bubblecloud.agi.api.vo.SupplierModelVO;
import com.bubblecloud.agi.api.vo.SupplierVO;
import com.bubblecloud.biz.agi.service.SupplierModelService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.bubblecloud.common.security.annotation.HasPermission;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI供应商模型表
 *
 * @author Rampart
 * @date 2026-02-12 14:04:58
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/supplierModel")
@Tag(description = "supplierModel", name = "供应商模型管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SupplierModelController {

	private final SupplierModelService supplierModelService;
	private final SupplierService supplierService;

	/**
	 * 分页查询
	 *
	 * @param pg  分页对象
	 * @param req AI供应商模型表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("agi_supplierModel_view")
	public R<Page<SupplierModelVO>> page(@ParameterObject Pg pg, @ParameterObject SupplierModel req) {
		pg.addOrderDefault(OrderItem.desc("t.id"));
		Page<SupplierModel> page = supplierModelService.findPg(pg, req);
		Page<SupplierModelVO> safePage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
		safePage.setRecords(page.getRecords().stream().map(this::toSafeVO).toList());
		return R.ok(safePage);
	}

	/**
	 * 通过条件查询AI供应商模型表
	 *
	 * @param req 查询条件
	 * @return R  对象列表
	 */
	@Operation(summary = "通过条件查询", description = "通过条件查询对象")
	@GetMapping("/details")
	@HasPermission("agi_supplierModel_view")
	public R<List<SupplierModelVO>> details(@ParameterObject SupplierModel req) {
		List<SupplierModel> list = supplierModelService.list(Wrappers.query(req));
		return R.ok(list.stream().map(this::toSafeVO).toList());
	}

	private SupplierModelVO toSafeVO(SupplierModel model) {
		Supplier supplier = supplierService.getById(model.getSupplierId());
		SupplierModelVO vo = SupplierModelVO.from(model, false, false);
		if (supplier != null) {
			vo.setSupplierName(supplier.getName());
			vo.setApiKeyConfigured(supplier.getApiKey() != null && !supplier.getApiKey().isBlank());
			vo.setApiDomainConfigured(supplier.getApiDomain() != null && !supplier.getApiDomain().isBlank());
			vo.setApiDomain(supplier.getApiDomain());
			vo.setApiKeyMasked(vo.isApiKeyConfigured() ? SupplierVO.SECRET_MASK : "");
		}
		return vo;
	}

	/**
	 * 新增AI供应商模型表
	 *
	 * @param req AI供应商模型表
	 * @return R
	 */
	@Operation(summary = "新增供应商模型", description = "新增供应商模型")
	@SysLog("新增供应商模型")
	@PostMapping
	@HasPermission("agi_supplierModel_add")
	public R create(@RequestBody @Validated({Req.Create.class}) SupplierModel req) {
		return R.ok(supplierModelService.create(req));
	}

	/**
	 * 修改AI供应商模型表
	 *
	 * @param req AI供应商模型表
	 * @return R
	 */
	@Operation(summary = "修改供应商模型", description = "修改供应商模型")
	@SysLog("修改供应商模型")
	@PutMapping
	@HasPermission("agi_supplierModel_edit")
	public R update(@RequestBody @Validated({Req.Update.class}) SupplierModel req) {
		return R.ok(supplierModelService.update(req));
	}

	/**
	 * 通过id删除AI供应商模型表
	 *
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除供应商模型", description = "通过id删除供应商模型")
	@SysLog("通过id删除供应商模型")
	@DeleteMapping
	@HasPermission("agi_supplierModel_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(supplierModelService.removeBatchByIds(CollUtil.toList(ids)));
	}

}
