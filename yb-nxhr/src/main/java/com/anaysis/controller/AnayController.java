package com.anaysis.controller;

import com.anaysis.service.impl.BaseClassinfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lzb
 * @Date 2020/11/25 14:11
 **/
@Api(tags = "同步班组")
@RestController
@RequestMapping("/sync")
public class AnayController {

    @Autowired
    private BaseClassinfoServiceImpl classinfoService;


    @ApiOperation("同步班组:一次性同步，待定车间需要自己维护")
    @GetMapping("syncClassinfo")
    public R<Object> syncClassinfo() {
        classinfoService.sync();
        return R.success("同步成功");
    }

}
