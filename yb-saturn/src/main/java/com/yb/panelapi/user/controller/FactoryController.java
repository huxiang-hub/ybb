package com.yb.panelapi.user.controller;

import com.yb.panelapi.user.mapper.BaseFactoryMapper;
import com.yb.panelapi.user.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/factory")
public class FactoryController {

    @Autowired
    private BaseFactoryMapper baseFactoryMapper;

    /**
     * 获取厂区名
     * @return
     */
    @GetMapping("getName")
    public R getName() {
        return R.ok(baseFactoryMapper.getFactoryName(), "获取厂区名成功");
    }
}
