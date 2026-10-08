package com.bubblecloud.backend.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
	 * 管理端使用的OAuth客户端响应对象，刻意不包含客户端密钥。
 */
@Data
@Schema(description = "OAuth客户端信息（不含密钥）")
public class SysOauthClientDetailsVO {

	public static final String SECRET_MASK = "******";

	@Schema(description = "id")
	private Long id;

	@Schema(description = "客户端id")
	private String clientId;

	@Schema(description = "客户端密钥掩码，不返回明文")
	private String clientSecretMasked;

	@Schema(description = "资源id列表")
	private String resourceIds;

	@Schema(description = "作用域")
	private String scope;

	@Schema(description = "授权方式")
	private String[] authorizedGrantTypes;

	@Schema(description = "回调地址")
	private String webServerRedirectUri;

	@Schema(description = "权限列表")
	private String authorities;

	@Schema(description = "请求令牌有效时间")
	private Integer accessTokenValidity;

	@Schema(description = "刷新令牌有效时间")
	private Integer refreshTokenValidity;

	@Schema(description = "扩展信息")
	private String additionalInformation;

	@Schema(description = "是否自动放行")
	private String autoapprove;

	@Schema(description = "验证码开关")
	private String captchaFlag;

	@Schema(description = "前端密码传输是否加密")
	private String encFlag;

	@Schema(description = "客户端允许同时在线数量")
	private String onlineQuantity;

	@Schema(description = "创建人")
	private String createBy;

	@Schema(description = "修改人")
	private String updateBy;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

}
