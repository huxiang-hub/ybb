package com.yb.interceptor;


import com.alibaba.fastjson.JSON;
import com.yb.auth.secure.util.SaSecureUtil;
import org.springblade.common.exception.CommonException;
import org.springblade.common.resubmit.ReSubmitStatus;
import org.springblade.common.resubmit.annotion.ReSubmit;
import org.springblade.common.resubmit.service.ReSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 防重复提交拦截器
 * @Author my
 * @Date Created in 2020/6/10
 */
@Component
public class ReSubmitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ReSubmitService reSubmitService;

    /**
     * 存储参数名-Key
     */
    private final String RESUBMIT_PARAMS = "resubmitParams";

    /**
     * 存储时间戳名-Key
     */
    private final String RESUBMIT_TIME = "resubmitTime";

    /**
     * 间隔时间：15S
     */
    private final Long INTERVAL_TIME = 15 * 1000L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ReSubmit reSubmit;
        // 获取防重复提交的注解
        if (handler instanceof HandlerMethod) {
            reSubmit = ((HandlerMethod) handler).getMethodAnnotation(ReSubmit.class);
        } else {
            return super.preHandle(request, response, handler);
        }

        // 方法注解需要
        if (reSubmit != null && reSubmit.isValidate() && isReSubmit(request)) {
            throw new CommonException(ReSubmitStatus.RE_SUBMIT.value(), ReSubmitStatus.RE_SUBMIT.getReasonPhrase());
        }
        return true;
    }

    /**
     * 验证是否重复提交
     *
     * @param request 请求
     * @return true or false
     */
    private Boolean isReSubmit(HttpServletRequest request) {
        // 请求地址
        String url = request.getRequestURI();

        // 设置缓存Key; userId + url 或者 IP + url
        String key = SaSecureUtil.getUserId() + ":" + url;

        // 1、获取本次请求数据
        String nowParams = JSON.toJSONString(request.getParameterMap());
        Map<String, Object> nowDataMap = new HashMap<>(2);
        nowDataMap.put(RESUBMIT_PARAMS, nowParams);
        nowDataMap.put(RESUBMIT_TIME, System.currentTimeMillis());

        // 2、获取上次次请求数据
        Map<String, Object> preDataMap = reSubmitService.getByKey(key);
        System.out.println(preDataMap);
        // 3、提交过数据
        // 4、对比时间，超过2S则放行
        // 5、对比参数
        if (preDataMap != null && !preDataMap.isEmpty() && isOverTime(nowDataMap, preDataMap) && isSameParams(nowDataMap, preDataMap)) {
            return true;
        }
        // 存一下数据
        reSubmitService.setByKey(key, nowDataMap);
        preDataMap = reSubmitService.getByKey(key);
        System.out.println(preDataMap);
        return false;
    }


    /**
     * 判断参数是否相同
     *
     * @param nowMap
     * @param preMap
     * @return
     */
    private boolean isSameParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(RESUBMIT_PARAMS);
        String preParams = (String) preMap.get(RESUBMIT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间是否超过规定时间
     *
     * @param nowMap
     * @param preMap
     * @return
     */
    private boolean isOverTime(Map<String, Object> nowMap, Map<String, Object> preMap) {
        long time1 = (Long) nowMap.get(RESUBMIT_TIME);
        long time2 = (Long) preMap.get(RESUBMIT_TIME);
        if ((time1 - time2) < (this.INTERVAL_TIME)) {
            return true;
        }
        return false;
    }

}
