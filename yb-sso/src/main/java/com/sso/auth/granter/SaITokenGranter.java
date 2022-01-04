package com.sso.auth.granter;

import com.sso.system.entity.SaUserInfo;

/**
 * 授权认证统一接口.
 *
 * @author Chill
 */
public interface SaITokenGranter {

	/**
	 * 获取用户信息
	 *
	 * @param tokenParameter 授权参数
	 * @return UserInfo
	 */
	SaUserInfo grant(SaTokenParameter tokenParameter);

}
