package com.bubblecloud.agi.api.vo;

import com.bubblecloud.agi.api.entity.SupplierModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
	 * 不含凭据值的AI供应商模型安全展示对象。
 */
@Data
@Schema(description = "AI供应商模型")
public class SupplierModelVO {

	private Long id;

	private Long supplierId;

	private String name;

	private String baseModel;

	private String modelType;

	private String contextLength;

	private String description;

	private String defaultFlag;

	private String extConfig;

	private String createBy;

	private LocalDateTime createTime;

	private String updateBy;

	private LocalDateTime updateTime;

	private String supplierName;

	@Schema(description = "API域名")
	private String apiDomain;

	@Schema(description = "API Key掩码，不返回明文")
	private String apiKeyMasked;

	@Schema(description = "API Key是否已配置")
	private boolean apiKeyConfigured;

	@Schema(description = "API域名是否已配置")
	private boolean apiDomainConfigured;

	public static SupplierModelVO from(SupplierModel model, boolean apiKeyConfigured, boolean apiDomainConfigured) {
		if (model == null) {
			return null;
		}
		SupplierModelVO vo = new SupplierModelVO();
		BeanUtils.copyProperties(model, vo);
		vo.setApiKeyConfigured(apiKeyConfigured);
		vo.setApiDomainConfigured(apiDomainConfigured);
		return vo;
	}
}
