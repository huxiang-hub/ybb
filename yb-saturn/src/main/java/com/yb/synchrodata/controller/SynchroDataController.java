package com.yb.synchrodata.controller;

import com.yb.synchrodata.context.SynchroDataContext;
import com.yb.synchrodata.service.SynchroDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lzb
 * @Date 2020/12/14 09:40
 **/
@Api(tags = "同步统一入口")
@RestController
@RequestMapping("/synchroData")
public class SynchroDataController {

    @Autowired
    private SynchroDataContext synchroDataContext;

    @ApiOperation("同步排产单")
    @GetMapping("workbatch")
    public R anycWorkbatch(@ApiParam(value = "租户id") @RequestParam(name = "tenantId") String tenantId,
                           @ApiParam(value = "设备id, 不传同步所有") @RequestParam(name = "maId", required = false) Integer maId) {
        SynchroDataService synchroDataService = synchroDataContext.getSynchroDataService(tenantId);
        return synchroDataService.anycWorkbatch(maId);
    }

    @ApiOperation("同步用户")
    @GetMapping("user")
    public R anycBladeUser(@ApiParam(value = "租户id") @RequestParam(name = "tenantId") String tenantId) {
        SynchroDataService synchroDataService = synchroDataContext.getSynchroDataService(tenantId);
        return synchroDataService.anycBladeUser();
    }

    @ApiOperation("同步设备")
    @GetMapping("machine")
    public R anycMachine(@ApiParam(value = "租户id") @RequestParam(name = "tenantId") String tenantId) {
        SynchroDataService synchroDataService = synchroDataContext.getSynchroDataService(tenantId);
        return synchroDataService.anycMachine();
    }

    @ApiOperation("同步部门")
    @GetMapping("dept")
    public R anycDept(@ApiParam(value = "租户id") @RequestParam(name = "tenantId") String tenantId) {
        SynchroDataService synchroDataService = synchroDataContext.getSynchroDataService(tenantId);
        return synchroDataService.anycDept();
    }

    @ApiOperation("同步工序")
    @GetMapping("process")
    public R anycProcess(@ApiParam(value = "租户id") @RequestParam(name = "tenantId") String tenantId) {
        SynchroDataService synchroDataService = synchroDataContext.getSynchroDataService(tenantId);
        return synchroDataService.anycProcess();
    }

    @ApiOperation("同步客户")
    @GetMapping("customer")
    public R anycCustomer(@ApiParam(value = "租户id") @RequestParam(name = "tenantId") String tenantId) {
        SynchroDataService synchroDataService = synchroDataContext.getSynchroDataService(tenantId);
        return synchroDataService.anycCustomer();
    }
}
