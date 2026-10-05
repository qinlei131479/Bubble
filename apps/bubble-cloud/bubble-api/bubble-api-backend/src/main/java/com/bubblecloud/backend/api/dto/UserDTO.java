package com.bubblecloud.backend.api.dto;

import com.bubblecloud.backend.api.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lengleng
 * @date 2017/11/5
 */
@Data
@Schema(description = "系统用户传输对象")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {

	/**
	 * 角色ID
	 */
	@Schema(description = "角色id集合")
	private List<Long> role;

	/**
	 * 部门id
	 */
	@Schema(description = "部门id")
	private Long deptId;

	/**
	 * 部门id集合（支持多部门）
	 */
	@Schema(description = "部门id集合")
	private List<Long> deptIds;

	/**
	 * 岗位ID
	 */
	private List<Long> post;

	/**
	 * 新密码
	 */
	@Schema(description = "新密码")
	private String newpassword1;

	/**
	 * 验证码
	 */
	@Schema(description = "验证码")
	private String code;

}
