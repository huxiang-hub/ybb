package com.sso.auth.controller;


import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

/**
 * 认证模块
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@RequestMapping("demo")
public class DemoController {


    @GetMapping("a")
    public R get() {
        return R.data("11111");
    }
}
