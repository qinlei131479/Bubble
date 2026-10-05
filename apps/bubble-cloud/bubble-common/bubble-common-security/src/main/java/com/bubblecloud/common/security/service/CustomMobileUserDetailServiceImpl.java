package com.bubblecloud.common.security.service;

import com.bubblecloud.backend.api.dto.UserInfo;
import com.bubblecloud.backend.api.feign.RemoteUserService;
import com.bubblecloud.common.core.constant.SecurityConstants;
import com.bubblecloud.common.core.util.R;
import com.bubblecloud.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author aeizzz
 */
@Slf4j
@RequiredArgsConstructor
public class CustomMobileUserDetailServiceImpl implements CustomUserDetailsService {

	private final UserDetailsService pigDefaultUserDetailsServiceImpl;

	private final RemoteUserService remoteUserService;

	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String phone) {
		R<UserInfo> result = remoteUserService.social(phone);
		return getUserDetails(RetOps.of(result).getData());
	}

	@Override
	public UserDetails loadUserByUser(CustomUser pigUser) {
		return pigDefaultUserDetailsServiceImpl.loadUserByUsername(pigUser.getUsername());
	}

	/**
	 * 支持所有的 mobile 类型
	 * @param clientId 目标客户端
	 * @param grantType 授权类型
	 * @return true/false
	 */
	@Override
	public boolean support(String clientId, String grantType) {
		return SecurityConstants.GRANT_MOBILE.equals(grantType);
	}

}
