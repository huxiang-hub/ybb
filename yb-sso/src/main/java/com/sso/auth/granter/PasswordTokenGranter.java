package com.sso.auth.granter;


import com.sso.enums.BladeUserEnum;
import com.sso.system.entity.SaUserInfo;
import com.sso.system.service.SaIUserService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

/**
 * PasswordTokenGranter
 *
 * @author Chill
 */
@Component
@AllArgsConstructor
public class PasswordTokenGranter implements SaITokenGranter {

	public static final String GRANT_TYPE = "password";

	private SaIUserService userClient;
	@Override
	public SaUserInfo grant(SaTokenParameter tokenParameter) {
		String tenantId = tokenParameter.getArgs().getStr("tenantId");
		String account = tokenParameter.getArgs().getStr("account");
		String password = tokenParameter.getArgs().getStr("password");
		SaUserInfo userInfo = null;
		if (Func.isNoneBlank(account, password)) {
			// 获取用户类型
			String userType = tokenParameter.getArgs().getStr("userType");
			R<SaUserInfo> result;
			// 根据不同用户类型调用对应的接口返回数据，用户可自行拓展
			if (userType.equals(BladeUserEnum.WEB.getName())) {
				userInfo = userClient.userInfo(tenantId, account, DigestUtil.encrypt(password));
			} else if (userType.equals(BladeUserEnum.APP.getName())) {
				userInfo = userClient.userInfo(tenantId, account, DigestUtil.encrypt(password));
			} else {
				userInfo = userClient.userInfo(tenantId, account, DigestUtil.encrypt(password));
			}
		}
		return userInfo;
	}

}
