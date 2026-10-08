package com.bubblecloud.agi.api.vo;

import com.bubblecloud.agi.api.entity.Supplier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
	 * 不含凭据值的AI供应商安全展示对象。
 */
@Data
@Schema(description = "AI供应商")
public class SupplierVO {

	public static final String SECRET_MASK = "******";

	private Long id;

	private String name;

	private String description;

	private String logo;

	private String apiApplyDomain;

	@Schema(description = "API域名")
	private String apiDomain;

	@Schema(description = "API Key掩码，不返回明文")
	private String apiKeyMasked;

	private String status;

	private String createBy;

	private LocalDateTime createTime;

	private String updateBy;

	private LocalDateTime updateTime;

	@Schema(description = "API Key是否已配置")
	private boolean apiKeyConfigured;

	@Schema(description = "API域名是否已配置")
	private boolean apiDomainConfigured;

	public static SupplierVO from(Supplier supplier) {
		if (supplier == null) {
			return null;
		}
		SupplierVO vo = new SupplierVO();
		BeanUtils.copyProperties(supplier, vo);
		vo.setApiKeyConfigured(supplier.getApiKey() != null && !supplier.getApiKey().isBlank());
		vo.setApiDomainConfigured(supplier.getApiDomain() != null && !supplier.getApiDomain().isBlank());
		vo.setApiKeyMasked(vo.isApiKeyConfigured() ? SECRET_MASK : "");
		return vo;
	}
}
