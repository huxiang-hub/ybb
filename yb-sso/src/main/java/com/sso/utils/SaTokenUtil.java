package com.sso.utils;

import com.sso.auth.secure.SaAuthInfo;
import com.sso.auth.secure.SaTokenInfo;
import com.sso.auth.secure.constant.SaTokenConstant;
import com.sso.system.entity.SaUser;
import com.sso.system.entity.SaUserInfo;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.RedisUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证工具类
 *
 * @author Jenny wang
 */
public class SaTokenUtil {

    public final static String CAPTCHA_HEADER_KEY = "Captcha-Key";
    public final static String CAPTCHA_HEADER_CODE = "Captcha-Code";
    public final static String CAPTCHA_NOT_CORRECT = "验证码不正确";
    public final static String TENANT_HEADER_KEY = "Tenant-Id";
    public final static String DEFAULT_TENANT_ID = "000000";
    public final static String USER_TYPE_HEADER_KEY = "User-Type";
    public final static String DEFAULT_USER_TYPE = "web";
    public final static String USER_NOT_FOUND = "用户名或密码错误";
    public final static String USER_RE_FOUND = "该用户已被登录";
    public final static String HEADER_KEY = "authorization";
    public final static String HEADER_PREFIX = "Basic ";
    public final static String DEFAULT_AVATAR = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";

    //redis 缓存信息
    private static RedisUtil redisUtil;
    /**
     * 创建认证token
     *
     * @param saUserInfo 用户信息
     * @return token
     */
    public static SaAuthInfo createAuthInfo(SaUserInfo saUserInfo) {
        SaUser saUser = saUserInfo.getSaUser();

        //设置jwt参数
        Map<String, String> param = new HashMap<>(16);
        param.put(SaTokenConstant.TOKEN_TYPE, SaTokenConstant.ACCESS_TOKEN);
        param.put(SaTokenConstant.TENANT_ID, saUser.getTenantId());
        param.put(SaTokenConstant.USER_ID, Func.toStr(saUser.getId()));
        param.put(SaTokenConstant.ROLE_ID, saUser.getRoleId());
        param.put(SaTokenConstant.ACCOUNT, saUser.getAccount());
        param.put(SaTokenConstant.USER_NAME, saUser.getRealName());
        param.put(SaTokenConstant.ROLE_NAME, Func.join(saUserInfo.getRoles()));

        SaTokenInfo accessToken = SaSecureUtil.createJWT(param, "audience", "issuser", SaTokenConstant.ACCESS_TOKEN);
        SaAuthInfo authInfo = new SaAuthInfo();
        authInfo.setAccount(saUser.getAccount());
        authInfo.setUserName(saUser.getRealName());
        authInfo.setAuthority(Func.join(saUserInfo.getRoles()));
        authInfo.setAccessToken(accessToken.getToken());
        authInfo.setExpiresIn(accessToken.getExpire());
        authInfo.setRefreshToken(createRefreshToken(saUserInfo).getToken());
        authInfo.setTokenType(SaTokenConstant.BEARER);
        authInfo.setLicense(SaTokenConstant.LICENSE_NAME);
        return authInfo;
    }

    /**
     * 创建refreshToken
     *
     * @param saUserInfo 用户信息
     * @return refreshToken
     */
    private static SaTokenInfo createRefreshToken(SaUserInfo saUserInfo) {
        SaUser saUser = saUserInfo.getSaUser();
        Map<String, String> param = new HashMap<>(16);
        param.put(SaTokenConstant.TOKEN_TYPE, SaTokenConstant.REFRESH_TOKEN);
        param.put(SaTokenConstant.USER_ID, Func.toStr(saUser.getId()));
        return SaSecureUtil.createJWT(param, "audience", "issuser", SaTokenConstant.REFRESH_TOKEN);
    }

}
