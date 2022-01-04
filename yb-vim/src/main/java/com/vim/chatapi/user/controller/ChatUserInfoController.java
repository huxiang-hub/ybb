package com.vim.chatapi.user.controller;

import com.vim.chatapi.user.service.IChatUserService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.system.feign.IChatSysTenantClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by SUMMER
 * @date 2020/3/25.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/tenant")
public class ChatUserInfoController {

    private IChatSysTenantClient sysTenantClient;

    private IChatUserService iChatUserService;


    @GetMapping("/test")
    public R test() {
        return R.success("成功");
    }

    @GetMapping("/getTenantList")
    public R getTenantList(String factoryName) {

        return sysTenantClient.getTenantList(factoryName);
    }



    @GetMapping("/getTenant")
    public R getTenant(String factoryName) {

        return (sysTenantClient.getTenant(factoryName));
    }





}
