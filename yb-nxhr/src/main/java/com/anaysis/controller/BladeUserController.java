package com.anaysis.controller;

import com.anaysis.service.IBaseDeptinfoService;
import com.anaysis.service.IBladeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lzb
 * @Date 2020/12/4 13:17
 **/
@Api(tags = "同步系统用户、员工")
@RestController
@RequestMapping("bladeUser")
public class BladeUserController {

    @Autowired
    private IBladeUserService bladeUserService;
    @Autowired
    private IBaseDeptinfoService baseDeptinfoService;

    @ApiOperation("同步系统用户及员工")
    @GetMapping("syncDeptPerson")
    public R syncDeptPerson() {
        baseDeptinfoService.syn();
        bladeUserService.sync();
//        bladeUserService.syncDeptStaff();
        return R.success("同步成功");
    }

    @ApiOperation("同步部门")
    @GetMapping("sync/dept")
    public R syncDept() {
        baseDeptinfoService.syn();
        return R.success("同步成功");
    }

    @ApiOperation("同步车间员工：车间员工有一部分只有姓名")
    @GetMapping("sync/deptStaff")
    public R syncDeptStaff() {
        bladeUserService.syncDeptStaff();
        return R.success("同步成功");
    }
}
