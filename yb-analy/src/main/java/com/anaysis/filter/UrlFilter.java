//package com.anaysis.filter;
//
//import com.anaysis.dynamicData.datasource.DBIdentifier;
//import com.anaysis.filter.provider.SaAuthProvider;
//import org.springblade.core.tool.utils.RedisUtil;
//import org.springblade.core.tool.utils.StringUtil;
//import org.springblade.core.tool.utils.WebUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter(filterName = "first", urlPatterns = {"/*"},asyncSupported = true)
//public class UrlFilter implements Filter {
//
//    @Autowired
//    private RedisUtil redisUtil;
//    private final String ACCESSTOKEN = "x-access-token";
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        System.out.println("----------------------->过滤器被创建");
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse respon = (HttpServletResponse) response;
//        String pathUrl = req.getRequestURI();
//        //获取cookie中保存的token令牌
//        String cookieValToken = WebUtil.getCookieVal(ACCESSTOKEN);
//        //获取redis保存的令牌
//        String account = (String)redisUtil.get(ACCESSTOKEN);
//        String redisToken = (String)redisUtil.get(account);
//        if(!StringUtil.isEmpty(cookieValToken)){
//            if(!redisToken.equals(cookieValToken)){
//            }
//        }
//        boolean isExit = SaAuthProvider.getDefaultSkipUrl().stream().map(url -> url.replace(SaAuthProvider.TARGET, SaAuthProvider.REPLACEMENT)).anyMatch(pathUrl::contains);
//        System.out.println("--------------------->filter：请求地址" + pathUrl + ":::::::isExit:" + isExit);
//        String tenant = "";
//        //获取租户对应信息
//        SaHttpServletRequestWrapper requestWrapper1 = new SaHttpServletRequestWrapper(req);
//        tenant = (tenant == null || tenant.length() <= 0) ? requestWrapper1.getParameter("tna") : ""; //获取tna租户对应信息内容通过参数反馈
//        if (tenant == null || tenant.length() <= 0) {
//            // 1.获取需要处理的参数
//            tenant = "baofeng";//默认宝峰数据库信息
//        }
//        DBIdentifier.setProjectCode(tenant);//不同租户信息不一致就跳转不同数据库中去
//        if (!isExit) {  //表示不存在拦截URL中
//            chain.doFilter(request, response);
//        } else {
//            chain.doFilter(request, response);
//        }
//    }
//
//    @Override
//    public void destroy() {
//        System.out.println("----------------------->过滤器被销毁");
//    }
//}
