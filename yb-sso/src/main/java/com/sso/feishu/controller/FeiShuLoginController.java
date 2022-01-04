package com.sso.feishu.controller;

import com.sso.chatapi.utils.ObjectMapperUtil;
import com.sso.feishu.config.FeiShuURLConstant;
import com.sso.feishu.util.FeiShuAccessTokenUtil;
import com.sso.feishu.util.HttpClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/FeiShuLogin")
@Api(tags = "飞书登录_FeiShuLogin")
public class FeiShuLoginController {

    @PostMapping(value = "/feiShuLogin")
    @ApiOperation(value = "飞书登录 ")
    public R feiShuLogin(String code) {

        String appAccessToken = FeiShuAccessTokenUtil.getAppAccessToken();
        Map<String, String> map = new HashMap<>();
        map.put("app_access_token", appAccessToken);
        map.put("grant_type", "authorization_code");
        map.put("code", code);
        FeiShuLoginController feiShuLogin = ObjectMapperUtil.toObject(HttpClientUtil.sendHttpPost(FeiShuURLConstant.ACCESS_TOKEN, map), FeiShuLoginController.class);
        return R.data(feiShuLogin);
    }
}
