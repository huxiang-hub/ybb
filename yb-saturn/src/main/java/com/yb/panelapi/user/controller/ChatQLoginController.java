//package com.yb.panelapi.user.controller;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.Callable;
//import java.util.concurrent.TimeUnit;
//import javax.servlet.http.HttpSession;
//import com.alibaba.fastjson.JSON;
//import com.yb.base.service.IBaseStaffinfoService;
//import com.yb.common.CreateQcodeUtil;
//import com.yb.panelapi.execute.controller.AddAideController;
//import com.yb.panelapi.user.configs.ReceiverConfig;
//import com.yb.panelapi.user.service.UserLoginService;
//import com.yb.system.user.entity.SaUser;
//import com.yb.system.user.service.SaIUserService;
//import com.yb.panelapi.user.utils.R;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.DispatcherServlet;
///**
// * 控制器
// *
// * @author 刘冬博客http://www.cnblogs.com/GoodHelper
// *
// */
//@Controller
//@RequestMapping("/plapi")
//public class ChatQLoginController {
//    /**
//     * 存储个人信息
//     */
//    Map<String,Object> result = new HashMap();
//    private static final String LOGIN_KEY = "key.value.login.";
//    @Autowired
//    private UserLoginService iUserLoginService;
//    @Autowired
//    private ReceiverConfig receiver;
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//    @Autowired
//    private UserLoginController loginController;
//    @Autowired
//    private AddAideController aideController;
//    @Autowired
//    private SaIUserService saIUserService;
//    @Autowired
//    private IBaseStaffinfoService staffinfoService;
//
//    @Autowired
//    public ServletRegistrationBean dispatcherServlet() {
//        ServletRegistrationBean registration = new ServletRegistrationBean(new DispatcherServlet(),
//                "/");
//            registration.setAsyncSupported(true);
//        return registration;
//    }
//    /**
//     * 获取二维码
//     *
//     * @return
//     */
//    @GetMapping("/login/getQrCode")
//    @ResponseBody
//    public Map<String, Object> getQrCode(Integer type ,Integer maId,String leaderId) throws Exception {
//        Map<String, Object> result = new HashMap<>();
//        String flag ="";
//        if (type==1) {  //扫码登录
//            /*从数据库拿到厂区ID（factory_id）和设备ID（ma_id）,生成二维码返回给前端固定的（数据从前端来）*/
//            String tenantId="000000";
//            tenantId = iUserLoginService.getFactoryTenantId(tenantId);//工厂的租据
//            result.put("tenantId", tenantId);
//            result.put("maId", maId);
//            flag = "yb-login";
//        }else if (type==2) {  //添加助理
//            flag = "yb-aide";
//            result.put("leaderId", leaderId);
//        }
//        String loginId = UUID.randomUUID().toString();
//        // app端登录地址
//        result.put("loginId", loginId);
//        result.put("flag", flag);
//        String code = JSON.toJSONString(result);//map转json字符串
//        result.put("image", CreateQcodeUtil.createQrCode(code));
//        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//        opsForValue.set(LOGIN_KEY + loginId, loginId, 5, TimeUnit.MINUTES);
//        return result;
//    }
//
//    /**
//     * app二维码登录地址方法
//     *
//     * @param loginId
//     * @param
//     * @return
//     */
//    @PostMapping("/login/setUser")
//    public @ResponseBody R setUser(String loginId, Integer userId,Integer maId) {
//        R result = new R();
//        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//        String value = opsForValue.get(LOGIN_KEY + loginId);
//        if (value != null) {
//            SaUser  user =  new SaUser();
//                    user.setId(userId);
//            result = loginController.AjaxLogin(user,maId);
//            // 保存认证信息
//            opsForValue.set(LOGIN_KEY + loginId, result.toString(), 1, TimeUnit.MINUTES);
//            // 发布登录广播消息
//            redisTemplate.convertAndSend(ReceiverConfig.TOPIC_NAME, loginId);
//        }
//        this.result = result;
//        return result;
//    }
//
//    /**
//     * app二维码添加助理
//     *
//     * @param loginId
//     * @param
//     * @return
//     */
//    @PostMapping("/aide/setUser")
//    public @ResponseBody R addAide(String loginId, Integer userId,String leaderId) throws SQLException {
//        R result = new R();
//        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//        String value = opsForValue.get(LOGIN_KEY + loginId);
//        if (value != null) {
//                result = aideController.addAide(leaderId,null,null,userId);
//            // 保存认证信息
//            opsForValue.set(LOGIN_KEY + loginId, result.toString(), 1, TimeUnit.MINUTES);
//                // 发布登录广播消息
//            redisTemplate.convertAndSend(ReceiverConfig.TOPIC_NAME_AIDE, loginId);
//        }
//        this.result = result;
//        return result;
//    }
//    /**
//     * 等待二维码扫码结果的长连接
//     *
//     * @param loginId
//     * @param session
//     * @return
//     */
//    @RequestMapping("/login/getResponse/{loginId}")
//    public @ResponseBody Callable<Map<String, Object>> getResponse(@PathVariable String loginId,HttpSession session) {
//        // 非阻塞
//        Callable<Map<String, Object>> callable = () -> {
//            Map<String, Object> result = new HashMap<>();
//            result.put("loginId", loginId);
//            try {
//                ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//                String user = opsForValue.get(LOGIN_KEY + loginId);
//                // 长时间不扫码，二维码失效。需重新获二维码
//                if (user == null) {
//                    result.put("success", false);
//                    result.put("stats", "refresh");
//                    return result;
//                }
//                // 已扫描
//                if (!user.equals(loginId)) {
//                    this.result.put("success", true);
//                    this.result.put("stats", "ok");
//                    return this.result;
//                }
//                // 等待二维码被扫
//                try {
//                    // 线程等待30秒
//                    receiver.getLoginLatch(loginId).await(15, TimeUnit.SECONDS);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                result.put("success", false);
//                result.put("stats", "waiting");
//                return result;
//            } finally {
//                // 移除登录请求
//                receiver.removeLoginLatch(loginId);
//            }
//        };
//        return callable;
//    }
//
//}