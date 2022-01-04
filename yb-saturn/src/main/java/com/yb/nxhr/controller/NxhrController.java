package com.yb.nxhr.controller;

import com.yb.nxhr.client.NxhrClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lzb
 * @Date 2020/12/13 13:34
 **/
@Api(tags = "宁夏和瑞同步接口")
@RestController
@RequestMapping("nxhr")
public class NxhrController {
    @Autowired
    private NxhrClient nxhrClient;

    @ApiOperation("同步工序、设备、排产单")
    @GetMapping("/syncWorkOrd")
    public R sync() {
        nxhrClient.sync();
        return R.success("同步成功");
    }

    @ApiOperation("同步部门并且同步人事表")
    @GetMapping("/syncDeptPerson")
    public R syncDeptPerson() {
        nxhrClient.syncDeptPerson();
        return R.success("同步成功");
    }

}
