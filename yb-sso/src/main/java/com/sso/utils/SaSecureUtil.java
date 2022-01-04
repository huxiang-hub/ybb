package com.sso.utils;

import com.sso.auth.secure.SaTokenInfo;
import com.sso.auth.secure.constant.SaTokenConstant;
import com.sso.system.entity.SaUser;
import com.sso.system.service.SaIUserService;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import org.springblade.common.exception.CommonException;
import org.springblade.core.secure.constant.SecureConstant;
import org.springblade.core.secure.exception.SecureException;
import org.springblade.core.secure.provider.IClientDetails;
import org.springblade.core.tool.utils.*;
import org.springframework.http.HttpStatus;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

public class SaSecureUtil {

    private static final String BLADE_USER_REQUEST_ATTR = "_YBSATURN_USER_REQUEST_ATTR_";

    private final static String HEADER = SaTokenConstant.HEADER;
    private final static String BEARER = SaTokenConstant.BEARER;
    private final static String ACCOUNT = SaTokenConstant.ACCOUNT;
    private final static String USER_ID = SaTokenConstant.USER_ID;
    private final static String ROLE_ID = SaTokenConstant.ROLE_ID;
    private final static String USER_NAME = SaTokenConstant.USER_NAME;
    private final static String ROLE_NAME = SaTokenConstant.ROLE_NAME;
    private final static String TENANT_ID = SaTokenConstant.TENANT_ID;
    private final static String CLIENT_ID = SaTokenConstant.CLIENT_ID;
    private final static Integer AUTH_LENGTH = SaTokenConstant.AUTH_LENGTH;
    private static String BASE64_SECURITY = Base64.getEncoder().encodeToString(SaTokenConstant.SIGN_KEY.getBytes(Charsets.UTF_8));
    //验证客户端服务器是否有效
    private static SaIUserService clientDetailsService;
    //redis 缓存信息
    private static RedisUtil redisUtil;

    static {
        clientDetailsService = SpringUtil.getBean(SaIUserService.class);
    }

    /**
     * 获取用户信息
     *
     * @return BladeUser
     */
    public static SaUser getUser() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }
        // 优先从 request 中获取
        Object bladeUser = request.getAttribute(BLADE_USER_REQUEST_ATTR);
        if (bladeUser == null) {
            bladeUser = getUser(request);
            if (bladeUser != null) {
                // 设置到 request 中
                request.setAttribute(BLADE_USER_REQUEST_ATTR, bladeUser);
            }
        }
        return (SaUser) bladeUser;
    }

    /**
     * 获取用户信息
     *
     * @param request request
     * @return BladeUser
     */
    public static SaUser getUser(HttpServletRequest request) {
        Claims claims = getClaims(request);
        if (claims == null) {
            return null;
        }
        String clientId = Func.toStr(claims.get(SaSecureUtil.CLIENT_ID));
        Integer userId = Func.toInt(claims.get(SaSecureUtil.USER_ID));
        String tenantId = Func.toStr(claims.get(SaSecureUtil.TENANT_ID));
        String roleId = Func.toStr(claims.get(SaSecureUtil.ROLE_ID));
        String account = Func.toStr(claims.get(SaSecureUtil.ACCOUNT));
        String roleName = Func.toStr(claims.get(SaSecureUtil.ROLE_NAME));
        String userName = Func.toStr(claims.get(SaSecureUtil.USER_NAME));
        SaUser bladeUser = new SaUser();
        bladeUser.setClientId(clientId);
        bladeUser.setUserId(userId);
        bladeUser.setTenantId(tenantId);
        bladeUser.setAccount(account);
        bladeUser.setRoleId(roleId);
        bladeUser.setRoleName(roleName);
        bladeUser.setUserName(userName);
        return bladeUser;
    }


    /**
     * 获取用户id
     *
     * @return userId
     */
    public static Integer getUserId() {
        SaUser user = getUser();
        return (null == user) ? -1 : user.getUserId();
    }

    /**
     * 获取用户id
     *
     * @param request request
     * @return userId
     */
    public static Integer getUserId(HttpServletRequest request) {
        SaUser user = getUser(request);
        return (null == user) ? -1 : user.getUserId();
    }

    /**
     * 获取用户账号
     *
     * @return userAccount
     */
    public static String getUserAccount() {
        SaUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getAccount();
    }

    /**
     * 获取用户账号
     *
     * @param request request
     * @return userAccount
     */
    public static String getUserAccount(HttpServletRequest request) {
        SaUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getAccount();
    }

    /**
     * 获取用户名
     *
     * @return userName
     */
    public static String getUserName() {
        SaUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getUserName();
    }

    /**
     * 获取用户名
     *
     * @param request request
     * @return userName
     */
    public static String getUserName(HttpServletRequest request) {
        SaUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getUserName();
    }

    /**
     * 获取用户角色
     *
     * @return userName
     */
    public static String getUserRole() {
        SaUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getRoleName();
    }

    /**
     * 获取用角色
     *
     * @param request request
     * @return userName
     */
    public static String getUserRole(HttpServletRequest request) {
        SaUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getRoleName();
    }

    /**
     * 获取租户ID
     *
     * @return tenantId
     */
    public static String getTenantId() {
        SaUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getTenantId();
    }

    /**
     * 获取租户ID
     *
     * @param request request
     * @return tenantId
     */
    public static String getTenantId(HttpServletRequest request) {
        SaUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getTenantId();
    }

    /**
     * 获取客户端id
     *
     * @return tenantId
     */
    public static String getClientId() {
        SaUser user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getClientId();
    }

    /**
     * 获取客户端id
     *
     * @param request request
     * @return tenantId
     */
    public static String getClientId(HttpServletRequest request) {
        SaUser user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getClientId();
    }

    /**
     * 获取Claims
     *
     * @param request request
     * @return Claims
     */
    public static Claims getClaims(HttpServletRequest request) {
        String auth = request.getHeader(SaSecureUtil.HEADER);
        if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
            String headStr = auth.substring(0, 6).toLowerCase();
            if (headStr.compareTo(SaSecureUtil.BEARER) == 0) {
                auth = auth.substring(7);
                return SaSecureUtil.parseJWT(auth);
            }
        }
        return null;
    }

    /**
     * 获取请求头
     *
     * @return header
     */
    public static String getHeader() {
        return getHeader(Objects.requireNonNull(WebUtil.getRequest()));
    }

    /**
     * 获取请求头
     *
     * @param request request
     * @return header
     */
    public static String getHeader(HttpServletRequest request) {
        return request.getHeader(HEADER);
    }

    /**
     * 解析jsonWebToken
     *
     * @param jsonWebToken jsonWebToken
     * @return Claims
     */
    public static Claims parseJWT(String jsonWebToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY))
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (ExpiredJwtException e) {
            throw new CommonException(HttpStatus.FORBIDDEN.value(), "登录信息已过期，请重新登录");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 创建令牌
     *
     * @param user      user
     * @param audience  audience
     * @param issuer    issuer
     * @param tokenType tokenType
     * @return jwt
     */
    public static SaTokenInfo createJWT(Map<String, String> user, String audience, String issuer, String tokenType) {

        String[] tokens = extractAndDecodeHeader();
        assert tokens.length == 2;
        String clientId = tokens[0];
        String clientSecret = tokens[1];

        // 获取客户端信息
        IClientDetails clientDetails = clientDetails(clientId);

        // 校验客户端信息
        if (!validateClient(clientDetails, clientId, clientSecret)) {
            throw new SecureException("客户端认证失败!");
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
//        System.out.println("::::::::::::::::::::::::::::::::1:::::::::::::::::::::::::::::::");
        //生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的类
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken")
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);

//        System.out.println("::::::::::::::::::::::::::::::::2:::::::::::::::::::::::::::::::");
        //设置JWT参数
        user.forEach(builder::claim);

        //设置应用id
        builder.claim(CLIENT_ID, clientId);

        //添加Token过期时间
        long expireMillis;
        if (tokenType.equals(SaTokenConstant.ACCESS_TOKEN)) {
            expireMillis = clientDetails.getAccessTokenValidity() * 1000;
        } else if (tokenType.equals(SaTokenConstant.REFRESH_TOKEN)) {
            expireMillis = clientDetails.getRefreshTokenValidity() * 1000;
        } else {
            expireMillis = getExpire();
        }

//        System.out.println("::::::::::::::::::::::::::::::::3:::::::::::::::::::::::::::::::");
        long expMillis = nowMillis + expireMillis;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);

//        System.out.println("::::::::::::::::::::::::::::::::4:::::::::::::::::::::::::::::::");
        // 组装Token信息
        SaTokenInfo tokenInfo = new SaTokenInfo();
        tokenInfo.setToken(builder.compact());
        tokenInfo.setExpire((int) expireMillis / 1000);

        System.out.println("::::::::::::::::::::::::::::::::5:::::::::::::::::::::::::::::::");
        return tokenInfo;
    }

    /**
     * 获取过期时间(次日凌晨3点)
     *
     * @return expire
     */
    public static long getExpire() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 3);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - System.currentTimeMillis();
    }

    /**
     * 客户端信息解码
     */
    @SneakyThrows
    public static String[] extractAndDecodeHeader() {
        // 获取请求头客户端信息
        String header = Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecureConstant.BASIC_HEADER_KEY);
        header = Func.toStr(header).replace(SecureConstant.BASIC_HEADER_PREFIX_EXT, SecureConstant.BASIC_HEADER_PREFIX);
        if (!header.startsWith(SecureConstant.BASIC_HEADER_PREFIX)) {
            throw new SecureException("No client information in request header");
        }
        byte[] base64Token = header.substring(6).getBytes(Charsets.UTF_8_NAME);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, Charsets.UTF_8_NAME);
        int index = token.indexOf(StringPool.COLON);
        if (index == -1) {
            throw new RuntimeException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, index), token.substring(index + 1)};
        }
    }

    /**
     * 获取请求头中的客户端id
     */
    public static String getClientIdFromHeader() {
        String[] tokens = extractAndDecodeHeader();
        assert tokens.length == 2;
        return tokens[0];
    }

    /**
     * 获取客户端信息
     *
     * @param clientId 客户端id
     * @return clientDetails
     */
    private static IClientDetails clientDetails(String clientId) {
        return clientDetailsService.loadClientByClientId(clientId);
    }

    /**
     * 校验Client
     *
     * @param clientId     客户端id
     * @param clientSecret 客户端密钥
     * @return boolean
     */
    private static boolean validateClient(IClientDetails clientDetails, String clientId, String clientSecret) {
        if (clientDetails != null) {
            return StringUtil.equals(clientId, clientDetails.getClientId()) && StringUtil.equals(clientSecret, clientDetails.getClientSecret());
        }
        return false;
    }
}
