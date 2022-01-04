package org.springblade.system.user.feign;

import org.springblade.core.tool.api.R;
import org.springblade.system.user.entity.User;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author qinbo
 *  2020/3/24
 */
public interface IChatUserClient {
    String API_PREFIX = "/chat" ;
    /**
     * author qinbo
     * @param phoneNum
     * @return
     */
    @GetMapping("/register")
    public R loginByPhoneCode(String phoneNum);
    /**
     * 提交短信验证码，在数据库保存User
     * author qinbo
     * @param user
     * @return
     */
    @GetMapping(API_PREFIX+"/user-register")
    public R getPhoneCode(User user);
}
