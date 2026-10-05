package com.bubblecloud.common.security.util;

import cn.hutool.core.util.StrUtil;
import com.bubblecloud.common.core.constant.SecurityConstants;
import com.bubblecloud.common.security.service.CustomUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取Authentication Token
	 * @return
	 */
	public String getToken() {
		Authentication authentication = SecurityUtils.getAuthentication();
		if (authentication instanceof BearerTokenAuthentication bearerTokenAuthentication) {
			return bearerTokenAuthentication.getToken().getTokenValue();
		}
		return null;
	}

	/**
	 * 获取用户
	 * @param authentication
	 * @return CustomUser
	 * <p>
	 */
	public CustomUser getUser(Authentication authentication) {
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof CustomUser) {
			return (CustomUser) principal;
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public CustomUser getUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
	}

	/**
	 * 获取用户角色信息
	 * @return 角色集合
	 */
	public List<Long> getRoleIds() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Long> roleIds = new ArrayList<>();
		authorities.stream()
			.filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
			.forEach(granted -> {
				String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
				roleIds.add(Long.parseLong(id));
			});
		return roleIds;
	}

}
