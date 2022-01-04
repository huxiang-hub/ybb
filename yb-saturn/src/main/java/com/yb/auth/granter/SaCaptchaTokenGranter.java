/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yb.auth.granter;

import com.yb.auth.utils.BladeUserEnum;
import com.yb.auth.utils.SaTokenUtil;
import com.yb.system.user.entity.SaUserInfo;
import com.yb.system.user.service.SaIUserService;
import lombok.AllArgsConstructor;
import org.springblade.common.cache.CacheNames;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码TokenGranter
 *
 * @author Jenny wang
 */
@Component
@AllArgsConstructor
public class SaCaptchaTokenGranter implements SaITokenGranter {

    public static final String GRANT_TYPE = "captcha";

    private SaIUserService userClient; //= SpringUtil.getBean(ISaUserService.class);
    private RedisUtil redisUtil;

    @Override
    public SaUserInfo grant(SaTokenParameter saTokenParameter) {
        userClient = SpringUtil.getBean(SaIUserService.class);
        /*HttpServletRequest request = WebUtil.getRequest();

        String key = request.getHeader(SaTokenUtil.CAPTCHA_HEADER_KEY);
        String code = request.getHeader(SaTokenUtil.CAPTCHA_HEADER_CODE);
        // 获取验证码
        String redisCode = String.valueOf(redisUtil.get(CacheNames.CAPTCHA_KEY + key));
        // 判断验证码
        if (code == null || !StringUtil.equalsIgnoreCase(redisCode, code)) {
            throw new ServiceException(SaTokenUtil.CAPTCHA_NOT_CORRECT);//测试是否返回错误本地操作
        }*/

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
