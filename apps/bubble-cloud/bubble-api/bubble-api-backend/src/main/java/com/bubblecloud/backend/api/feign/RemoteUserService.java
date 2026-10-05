package com.bubblecloud.backend.api.feign;

import com.bubblecloud.backend.api.dto.UserInfo;
import com.bubblecloud.common.core.constant.ServiceNameConstants;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author lengleng
 * @date 2026-02-10
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.BACKEND_SERVICE)
public interface RemoteUserService {

	/**
	 * 通过用户名查询用户、角色信息
	 * @param username 用户名
	 * @return R
	 */
	@NoToken
	@GetMapping("/user/info/{username}")
	R<UserInfo> info(@PathVariable String username);

	/**
	 * 通过社交账号或手机号查询用户、角色信息
	 * @param inStr appid@code
	 * @return
	 */
	@NoToken
	@GetMapping("/social/info/{inStr}")
	R<UserInfo> social(@PathVariable String inStr);

	/**
	 * 锁定用户
	 * @param username 用户名
	 * @return
	 */
	@NoToken
	@PutMapping("/user/lock/{username}")
	R<Boolean> lockUser(@PathVariable String username);

}
