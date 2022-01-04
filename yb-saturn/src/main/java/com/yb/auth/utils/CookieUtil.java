package com.yb.auth.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

	/**
	 * 编辑工具API方法,通过cookie的名称,获取cookie的值
	 */
	public static String getCooKieValue(HttpServletRequest request, String cookieName) {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if(null != cookies) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(cookieName)) {
					value = cookie.getValue();
					break;
				}
			}
		}
		return value;
	}


	public static void addCookie(
			HttpServletRequest request,
			HttpServletResponse response,
			String cookieName,
			String cookieValue,
			int seconds,
			String domain) {

		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(seconds);
		cookie.setPath("/");
		cookie.setDomain(domain);
		response.addCookie(cookie);
	}

}
