package com.sso.auth.granter;

import lombok.AllArgsConstructor;
import org.springblade.core.secure.exception.SecureException;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TokenGranterBuilder
 *
 * @author Chill
 */
@AllArgsConstructor
public class SaTokenGranterBuilder {

	/**
	 * TokenGranter缓存池
	 */
	private static Map<String, SaITokenGranter> granterPool = new ConcurrentHashMap<>();

	static {
		granterPool.put(PasswordTokenGranter.GRANT_TYPE, SpringUtil.getBean(PasswordTokenGranter.class));
		granterPool.put(CaptchaTokenGranter.GRANT_TYPE, SpringUtil.getBean(CaptchaTokenGranter.class));
		granterPool.put(RefreshTokenGranter.GRANT_TYPE, SpringUtil.getBean(RefreshTokenGranter.class));
	}

	/**
	 * 获取TokenGranter
	 *
	 * @param grantType 授权类型
	 * @return ITokenGranter
	 */
	public static SaITokenGranter getGranter(String grantType) {
		SaITokenGranter tokenGranter = granterPool.get(Func.toStr(grantType, PasswordTokenGranter.GRANT_TYPE));
		if (tokenGranter == null) {
			throw new SecureException("no grantType was found");
		} else {
			return tokenGranter;
		}
	}

}
