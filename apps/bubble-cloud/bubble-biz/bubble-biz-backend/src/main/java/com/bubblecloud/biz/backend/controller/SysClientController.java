package com.bubblecloud.biz.backend.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubblecloud.backend.api.dto.SysOauthClientDetailsDTO;
import com.bubblecloud.backend.api.entity.SysOauthClientDetails;
import com.bubblecloud.backend.api.vo.SysOauthClientDetailsVO;
import com.bubblecloud.biz.backend.service.SysOauthClientDetailsService;
import com.bubblecloud.common.core.constant.CommonConstants;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.common.excel.annotation.ResponseExcel;
import com.bubblecloud.common.log.annotation.SysLog;
import com.bubblecloud.common.security.annotation.HasPermission;
import com.bubblecloud.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/client")
@Tag(description = "client", name = "客户端管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysClientController {

	private final SysOauthClientDetailsService clientDetailsService;

	/**
	 * 通过ID查询
	 * @param clientId clientId
	 * @return SysOauthClientDetails
	 */
	@GetMapping("/{clientId}")
	@HasPermission("sys_client_view")
	public R getByClientId(@PathVariable String clientId) {
		SysOauthClientDetails details = clientDetailsService
			.getOne(Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId));
		return R.ok(details == null ? null : toSecureVo(details));
	}

	/**
	 * 简单分页查询
	 * @param page 分页对象
	 * @param sysOauthClientDetails 系统终端
	 * @return
	 */
	@GetMapping("/page")
	@HasPermission("sys_client_view")
	public R getOauthClientDetailsPage(@ParameterObject Page page,
			@ParameterObject SysOauthClientDetails sysOauthClientDetails) {
		LambdaQueryWrapper<SysOauthClientDetails> wrapper = Wrappers.<SysOauthClientDetails>lambdaQuery()
			.like(StrUtil.isNotBlank(sysOauthClientDetails.getClientId()), SysOauthClientDetails::getClientId,
					sysOauthClientDetails.getClientId());
		Page<SysOauthClientDetails> clientPage = clientDetailsService.page(page, wrapper);
		Page<SysOauthClientDetailsVO> securePage = new Page<>(clientPage.getCurrent(), clientPage.getSize(),
				clientPage.getTotal());
		securePage.setRecords(clientPage.getRecords().stream().map(this::toSecureVo).toList());
		return R.ok(securePage);
	}

	/**
	 * 添加
	 * @param clientDetailsDTO 实体
	 * @return success/false
	 */
	@SysLog("添加终端")
	@PostMapping
	@HasPermission("sys_client_add")
	public R add(@Valid @RequestBody SysOauthClientDetailsDTO clientDetailsDTO) {
		return R.ok(clientDetailsService.saveClient(clientDetailsDTO));
	}

	/**
	 * 删除
	 * @param ids ID 列表
	 * @return success/false
	 */
	@SysLog("删除终端")
	@DeleteMapping
	@HasPermission("sys_client_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(clientDetailsService.removeClientByIds(ids));
	}

	/**
	 * 编辑
	 * @param clientDetailsDTO 实体
	 * @return success/false
	 */
	@SysLog("编辑终端")
	@PutMapping
	@HasPermission("sys_client_edit")
	public R update(@Valid @RequestBody SysOauthClientDetailsDTO clientDetailsDTO) {
		return R.ok(clientDetailsService.updateClientById(clientDetailsDTO));
	}

	/**
	 * feign 查询客户端信息的接口
	 * @param clientId 客户端 ID
	 * @return 基本信息
	 */
	@Inner
	@GetMapping("/getClientDetailsById/{clientId}")
	public R getClientDetailsById(@PathVariable String clientId) {
		return R.ok(clientDetailsService.getOne(
				Wrappers.<SysOauthClientDetails>lambdaQuery().eq(SysOauthClientDetails::getClientId, clientId), false));
	}

	/**
	 * 通过条件查询详情
	 * @param clientDetails 查询条件
	 * @return 基本信息
	 */
	@GetMapping("/details")
	@HasPermission("sys_client_view")
	public R getDetails(@ParameterObject SysOauthClientDetails clientDetails) {
		SysOauthClientDetails details = clientDetailsService.getOne(Wrappers.<SysOauthClientDetails>lambdaQuery()
			.eq(SysOauthClientDetails::getClientId, clientDetails.getClientId()));
		return R.ok(details == null ? null : toSecureVo(details));
	}

	/**
	 * 同步缓存字典
	 * @return R
	 */
	@SysLog("同步终端")
	@PutMapping("/sync")
	@HasPermission("sys_client_edit")
	public R sync() {
		return clientDetailsService.syncClientCache();
	}

	/**
	 * 导出所有客户端
	 * @return excel
	 */
	@ResponseExcel
	@SysLog("导出excel")
	@GetMapping("/export")
	@HasPermission("sys_client_view")
	public List<SysOauthClientDetailsVO> export(SysOauthClientDetails sysOauthClientDetails) {
		LambdaQueryWrapper<SysOauthClientDetails> wrapper = Wrappers.<SysOauthClientDetails>lambdaQuery()
			.like(StrUtil.isNotBlank(sysOauthClientDetails.getClientId()), SysOauthClientDetails::getClientId,
					sysOauthClientDetails.getClientId());
		return clientDetailsService.list(wrapper).stream().map(this::toSecureVo).toList();
	}

	private SysOauthClientDetailsVO toSecureVo(SysOauthClientDetails details) {
		String information = details.getAdditionalInformation();
		SysOauthClientDetailsVO vo = new SysOauthClientDetailsVO();
		BeanUtils.copyProperties(details, vo);
		if (StrUtil.isNotBlank(details.getClientSecret())) {
			vo.setClientSecretMasked(SysOauthClientDetailsVO.SECRET_MASK);
		}
		if (StrUtil.isNotBlank(information)) {
			vo.setCaptchaFlag(JSONUtil.parseObj(information).getStr(CommonConstants.CAPTCHA_FLAG));
			vo.setEncFlag(JSONUtil.parseObj(information).getStr(CommonConstants.ENC_FLAG));
			vo.setOnlineQuantity(JSONUtil.parseObj(information).getStr(CommonConstants.ONLINE_QUANTITY));
		}
		return vo;
	}

}
