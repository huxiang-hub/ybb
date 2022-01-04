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

import lombok.AllArgsConstructor;
import org.springblade.core.secure.exception.SecureException;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TokenGranterBuilder
 *
 * @author Jenny wang
 */
@AllArgsConstructor
public class SaTokenGranterBuilder {

	/**
	 * TokenGranter缓存池
	 */
	private static Map<String, SaITokenGranter> granterPool = new ConcurrentHashMap<>();

	static {
		granterPool.put(SaPasswordTokenGranter.GRANT_TYPE, SpringUtil.getBean(SaPasswordTokenGranter.class));
		granterPool.put(SaCaptchaTokenGranter.GRANT_TYPE, SpringUtil.getBean(SaCaptchaTokenGranter.class));
		granterPool.put(SaRefreshTokenGranter.GRANT_TYPE, SpringUtil.getBean(SaRefreshTokenGranter.class));
	}

	/**
	 * 获取TokenGranter
	 *
	 * @param grantType 授权类型
	 * @return ITokenGranter
	 */
	public static SaITokenGranter getGranter(String grantType) {
		SaITokenGranter tokenGranter = granterPool.get(Func.toStr(grantType, SaPasswordTokenGranter.GRANT_TYPE));
		if (tokenGranter == null) {
			throw new SecureException("no grantType was found");
		} else {
			return tokenGranter;
		}
	}

}
