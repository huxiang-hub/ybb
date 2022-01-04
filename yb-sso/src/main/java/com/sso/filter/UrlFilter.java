package com.sso.filter;

import com.sso.dynamicData.datasource.DBIdentifier;
import com.sso.provider.SaAuthProvider;
import org.springblade.core.tool.utils.RedisUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.core.tool.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "first", urlPatterns = {"/*"},asyncSupported = true)
public class UrlFilter implements Filter {

    @Autowired
    private RedisUtil redisUtil;
    private final String ACCESSTOKEN = "x-access-token";

    public void init(FilterConfig filterConfig) throws ServletException {
        //System.out.println("----------------------->过滤器被创建");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse respon = (HttpServletResponse) response;
        String pathUrl = req.getRequestURI();
        StringBuffer requestURL = req.getRequestURL();
        //获取cookie中保存的token令牌
//        String cookieValToken = WebUtil.getCookieVal(ACCESSTOKEN);
        //获取redis保存的令牌
//        String account = (String) redisUtil.get(ACCESSTOKEN);
//        String redisToken = (String) redisUtil.get(account);
//        if (!StringUtil.isEmpty(cookieValToken)) {
//            if (!redisToken.equals(cookieValToken)) {
//                //todo
//                // WebUtil.setCookie(respon, ACCESSTOKEN, null, 0);
//                // throw new XjxccException("账号在其他设备登录,请重新登录", 402);
//            }
//        }
        boolean isExit = SaAuthProvider.getDefaultSkipUrl().stream().map(url -> url.replace(SaAuthProvider.TARGET, SaAuthProvider.REPLACEMENT)).anyMatch(pathUrl::contains);
        System.out.println("--------------------->filter：请求地址" + requestURL + ":::::::isExit:" + isExit);
        System.out.println("--------------------->filter：请求地址" + pathUrl + ":::::::isExit:" + isExit);


        String tenant = WebUtil.getCookieVal("tenant_id");
        if (StringUtil.isEmpty(tenant)) {
            tenant = req.getHeader("tenant_id");//获取请求头
        }
        tenant = getTenant(requestURL);
        String parameter = request.getParameter("tenant");//获取请求参数
        if (!StringUtil.isEmpty(parameter)) {
            // 1.租户验证时访问的数据库
            tenant = "tenant";
        }
        if (StringUtil.isEmpty(parameter) && tenant == null) {
            // 2.获取需要处理的参数,租户未验证默认的数据库
            tenant = "000000";
        }
        DBIdentifier.setProjectCode(tenant);//不同租户信息不一致就跳转不同数据库中去
        if (!isExit) {  //表示不存在拦截URL中
            chain.doFilter(request, response);
        } else {
            //
            chain.doFilter(request, response);
        }
        //filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("----------------------->过滤器被销毁");
    }

    public static String getTenant(StringBuffer uri) {
        String url = uri.toString();
        if (url.contains("nxjsj.")) {
            return "nxjsj";
        } else if (url.contains("fuli.")) {
            return "fuli";
        } else if (url.contains("baofeng.")) {
            return "baofeng";
        } else if (url.contains("xingyi.")) {
            return "xingyi";
        } else if (url.contains("hbhr.")) {
            return "hbhr";
        } else if (url.contains("demo.")) {
            return "demo";
        }else if (url.contains("nxhr.")) {
            return "nxhr";
        }
        else if (url.contains("yilong.")) {
            return "yilong";
        }
        else if (url.contains("yintong.")) {
            return "yintong";
        }
        {
            return "000000";
        }
    }
}
