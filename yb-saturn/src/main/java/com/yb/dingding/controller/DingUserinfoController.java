package com.yb.dingding.controller;

import com.yb.dingding.service.DingUserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/DingUserinfo")
public class DingUserinfoController {

    @Autowired
    private DingUserinfoService dingUserinfoService;
}
