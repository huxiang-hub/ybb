/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.auth.granter;

import com.yb.auth.utils.BladeUserEnum;
import com.yb.system.user.entity.SaUserInfo;
import com.yb.system.user.service.SaIUserService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.stereotype.Component;

/**
 * PasswordTokenGranter
 *
 * @author Jenny wang
 */
@Component
@AllArgsConstructor
public class SaPasswordTokenGranter implements SaITokenGranter {

	public static final String GRANT_TYPE = "password";
	private SaIUserService userClient;//= SpringUtil.getBean(ISaUserService.class);;

	@Override
	public SaUserInfo grant(SaTokenParameter saTokenParameter) {
		userClient = SpringUtil.getBean(SaIUserService.class);
		String tenantId = saTokenParameter.getArgs().getStr("tenantId");
		String account = saTokenParameter.getArgs().getStr("account");
		String password = saTokenParameter.getArgs().getStr("password");
		SaUserInfo saUserInfo = null;
		if (Func.isNoneBlank(account, password)) {
			// 获取用户类型
			String userType = saTokenParameter.getArgs().getStr("userType");
			R<SaUserInfo> result;
			// 根据不同用户类型调用对应的接口返回数据，用户可自行拓展
			if (userType.equals(BladeUserEnum.WEB.getName())) {
				result = userClient.saUserInfo(tenantId, account, DigestUtil.encrypt(password));
			} else if (userType.equals(BladeUserEnum.APP.getName())) {
				result = userClient.saUserInfo(tenantId, account, DigestUtil.encrypt(password));
			} else {
				result = userClient.saUserInfo(tenantId, account, DigestUtil.encrypt(password));
			}
			saUserInfo = result.isSuccess() ? result.getData() : null;
		}
		return saUserInfo;
	}

}
