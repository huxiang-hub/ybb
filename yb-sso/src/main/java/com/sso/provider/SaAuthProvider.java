package com.sso.provider;

import org.springblade.core.launch.constant.TokenConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 鉴权配置
 *
 * @author Jenny wang
 */
public class SaAuthProvider {

	public static String TARGET = "/**";
	public static String REPLACEMENT = "";
	public static String AUTH_KEY = TokenConstant.HEADER;
	private static List<String> defaultSkipUrl = new ArrayList<>();

	static {
		defaultSkipUrl.add("/example");
		defaultSkipUrl.add("/token/**");
		defaultSkipUrl.add("/captcha/**");
		defaultSkipUrl.add("/actuator/health/**");
		defaultSkipUrl.add("/v2/api-docs/**");
		defaultSkipUrl.add("/v2/api-docs-ext/**");
		defaultSkipUrl.add("/auth/**");
		defaultSkipUrl.add("/log/**");
		defaultSkipUrl.add("/menu/routes");
		defaultSkipUrl.add("/menu/auth-routes");
		defaultSkipUrl.add("/order/create/**");
		defaultSkipUrl.add("/storage/deduct/**");
		defaultSkipUrl.add("/error/**");
		defaultSkipUrl.add("/assets/**");
		/*****************新增过滤器地址*****************************************/
		defaultSkipUrl.add("/sso/token/**");
		defaultSkipUrl.add("/sso/captcha/**");
		defaultSkipUrl.add("/sso/failed/**");
		defaultSkipUrl.add("/sso/logout/**");
		defaultSkipUrl.add("/sso/plapi/**");
		defaultSkipUrl.add("/api/**");
	}

	/**
	 * 默认无需鉴权的API
	 */
	public static List<String> getDefaultSkipUrl() {
		return defaultSkipUrl;
	}

}
