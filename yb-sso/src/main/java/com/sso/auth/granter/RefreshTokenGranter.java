package com.sso.auth.granter;


import com.sso.system.entity.SaUserInfo;
import com.sso.system.service.SaIUserService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springblade.core.launch.constant.TokenConstant;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * RefreshTokenGranter
 *
 * @author Chill
 */
@Component
@AllArgsConstructor
public class RefreshTokenGranter implements SaITokenGranter {

	public static final String GRANT_TYPE = "refresh_token";

	private SaIUserService userClient;

	@Override
	public SaUserInfo grant(SaTokenParameter tokenParameter) {
		userClient = SpringUtil.getBean(SaIUserService.class);

		String grantType = tokenParameter.getArgs().getStr("grantType");
		String refreshToken = tokenParameter.getArgs().getStr("refreshToken");
		SaUserInfo userInfo = null;
		if (Func.isNoneBlank(grantType, refreshToken) && grantType.equals(TokenConstant.REFRESH_TOKEN)) {
			Claims claims = SecureUtil.parseJWT(refreshToken);
			String tokenType = Func.toStr(Objects.requireNonNull(claims).get(TokenConstant.TOKEN_TYPE));
			if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
				userInfo = userClient.userInfo(Func.toLong(claims.get(TokenConstant.USER_ID)));
			}
		}
		return userInfo;
	}
}
