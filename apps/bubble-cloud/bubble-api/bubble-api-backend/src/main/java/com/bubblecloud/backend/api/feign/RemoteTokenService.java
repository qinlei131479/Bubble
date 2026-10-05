package com.bubblecloud.backend.api.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubblecloud.common.core.constant.ServiceNameConstants;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/4
 */
@FeignClient(contextId = "remoteTokenService", value = ServiceNameConstants.AUTH_SERVICE)
public interface RemoteTokenService {

	/**
	 * 分页查询token 信息
	 * @param params 分页参数
	 * @return page
	 */
	@NoToken
	@PostMapping("/token/page")
	R<Page> getTokenPage(@RequestBody Map<String, Object> params);

	/**
	 * 删除token
	 * @param token token
	 * @return
	 */
	@NoToken
	@DeleteMapping("/token/remove/{token}")
	R<Boolean> removeTokenById(@PathVariable("token") String token);

}
