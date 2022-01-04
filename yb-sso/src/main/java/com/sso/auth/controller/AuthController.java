//package com.oss.auth.controller;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.oss.auth.granter.ITokenGranter;
//import com.oss.auth.granter.TokenGranterBuilder;
//import com.oss.auth.granter.TokenParameter;
//import com.oss.user.entity.User;
//import com.oss.user.entity.UserInfo;
//import com.oss.user.mapper.UserMapper;
//import com.oss.utils.TokenUtil;
//import com.wf.captcha.SpecCaptcha;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.AllArgsConstructor;
//import org.springblade.common.cache.CacheNames;
//import org.springblade.core.secure.AuthInfo;
//import org.springblade.core.tool.api.R;
//import org.springblade.core.tool.support.Kv;
//import org.springblade.core.tool.utils.Func;
//import org.springblade.core.tool.utils.RedisUtil;
//import org.springblade.core.tool.utils.WebUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
///**
// * 认证模块
// *
// * @author Chill
// */
//@RestController
//@AllArgsConstructor
//@Api(value = "用户授权认证", tags = "授权接口")
//public class AuthController {
//
//    private RedisUtil redisUtil;
//    @Autowired
//    private UserMapper userMapper;
//
//    @PostMapping("token")
//    @ApiOperation(value = "获取认证token", notes = "传入租户ID:tenantId,账号:account,密码:password")
//    public R<AuthInfo> token(@ApiParam(value = "授权类型", required = true) @RequestParam(defaultValue = "password", required = false) String grantType,
//                             @ApiParam(value = "刷新令牌") @RequestParam(required = false) String refreshToken,
//                             @ApiParam(value = "租户ID", required = true) @RequestParam(defaultValue = "000000", required = false) String tenantId,
//                             @ApiParam(value = "账号") @RequestParam(required = false) String account,
//                             @ApiParam(value = "密码") @RequestParam(required = false) String password) {
//
//        String userType = Func.toStr(WebUtil.getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE);
//        TokenParameter tokenParameter = new TokenParameter();
//        tokenParameter.getArgs().set("tenantId", tenantId)
//                .set("account", account)
//                .set("password", password)
//                .set("grantType", grantType)
//                .set("refreshToken", refreshToken)
//                .set("userType", userType);
//
//        ITokenGranter granter = TokenGranterBuilder.getGranter(grantType);
//        //印聊用户登录情况区分，登录后验证租户进行跳转，如果为000000表示没有租户不进行跳转
//        UserInfo userInfo = granter.grant(tokenParameter);
//
//        if (userInfo == null || userInfo.getUser() == null || userInfo.getUser().getId() == null) {
//            return R.fail(TokenUtil.USER_NOT_FOUND);
//        }
//        AuthInfo authInfo = TokenUtil.createAuthInfo(userInfo);
//
//        return R.data(authInfo);
//    }
//
//    @GetMapping("/captcha")
//    @ApiOperation(value = "获取验证码")
//    public R<Kv> captcha() {
//        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
//        String verCode = specCaptcha.text().toLowerCase();
//        String key = UUID.randomUUID().toString();
//        redisUtil.set(CacheNames.CAPTCHA_KEY + key, 12, 30L, TimeUnit.MINUTES);
//        //清除登录时创建的cookie
//        // 将key和base64返回给前端
//        return R.data(Kv.init().set("key", key).set("image", "data:image/png;base64,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"));
//        //return R.data(Kv.init().set("key", key).set("image", specCaptcha.toBase64()));
//        // 存入redis并设置过期时间为30分钟
////        redisUtil.set(CacheNames.CAPTCHA_KEY + key, verCode, 30L, TimeUnit.MINUTES);
////        // 将key和base64返回给前端
////        return R.data(Kv.init().set("key", key).set("image", specCaptcha.toBase64()));
//    }
//
//    @GetMapping("a")
//    public R get() {
//        List<User> users = userMapper.selectList(new QueryWrapper<>());
//        return R.data(users);
//    }
//}
