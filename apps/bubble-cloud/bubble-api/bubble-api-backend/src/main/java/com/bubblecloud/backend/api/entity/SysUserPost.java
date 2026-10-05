package com.bubblecloud.backend.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

/**
 * 用户岗位表实体类
 *
 * @author lengleng
 * @date 2025/06/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserPost extends Model<SysUserPost> {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 岗位ID
	 */
	@Schema(description = "岗位id")
	private Long postId;

}
