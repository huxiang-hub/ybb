package com.sso.auth.granter;


import com.sso.enums.BladeUserEnum;
import com.sso.system.entity.SaUserInfo;
import com.sso.system.service.SaIUserService;
import com.sso.utils.SaTokenUtil;
import lombok.AllArgsConstructor;
import org.springblade.common.cache.CacheNames;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码TokenGranter
 *
 * @author Chill
 */
@Component
@AllArgsConstructor
public class CaptchaTokenGranter implements SaITokenGranter {

    public static final String GRANT_TYPE = "captcha";

    private SaIUserService userClient;
    private RedisUtil redisUtil;


    @Override
    public SaUserInfo grant(SaTokenParameter tokenParameter) {
        userClient = SpringUtil.getBean(SaIUserService.class);

        String tenantId = tokenParameter.getArgs().getStr("tenantId");
        String account = tokenParameter.getArgs().getStr("account");
        String password = tokenParameter.getArgs().getStr("password");
        SaUserInfo userInfo = null;
        if (Func.isNoneBlank(account, password)) {
            // 获取用户类型
            String userType = tokenParameter.getArgs().getStr("userType");
            System.out.println(userType);
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
