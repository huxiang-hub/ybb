package com.yb.dingding.controller;

import com.yb.dingding.service.DingDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/DingDept")
public class DingDeptController {

    @Autowired
    private DingDeptService dingDeptService;
}
