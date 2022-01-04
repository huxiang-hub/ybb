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

import com.yb.system.user.entity.SaUserInfo;
import com.yb.system.user.service.SaIUserService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springblade.core.launch.constant.TokenConstant;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * RefreshTokenGranter
 *
 * @author Jenny wang
 */
@Component
@AllArgsConstructor
public class SaRefreshTokenGranter implements SaITokenGranter {

    public static final String GRANT_TYPE = "refresh_token";
    private SaIUserService userClient ;//= SpringUtil.getBean(ISaUserService.class);
    ;

    @Override
    public SaUserInfo grant(SaTokenParameter saTokenParameter) {
        userClient = SpringUtil.getBean(SaIUserService.class);
        String grantType = saTokenParameter.getArgs().getStr("grantType");
        String refreshToken = saTokenParameter.getArgs().getStr("refreshToken");
        SaUserInfo saUserInfo = null;
        if (Func.isNoneBlank(grantType, refreshToken) && grantType.equals(TokenConstant.REFRESH_TOKEN)) {
            Claims claims = SecureUtil.parseJWT(refreshToken);
            String tokenType = Func.toStr(Objects.requireNonNull(claims).get(TokenConstant.TOKEN_TYPE));
            if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
                R<SaUserInfo> result = userClient.saUserInfo(Func.toLong(claims.get(TokenConstant.USER_ID)));
                saUserInfo = result.isSuccess() ? result.getData() : null;
            }
        }
        return saUserInfo;
    }
}
