package com.yb.system.user.controller;


import com.yb.system.user.service.TenantAnalyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 服务类
 *
 * @author 解析对外接口
 */
@RestController
@AllArgsConstructor
    @RequestMapping("/tenantAnaly")
@Api(value = "解析对外接口")
public class TenantAnalyController extends BladeController {

    @Autowired
    private TenantAnalyService tenantAnalyService;

    @GetMapping("/getBoxInfo")
    @ApiOperation(value = "获取盒子信息")
    public R getBoxInfo(String tenantId) {

        return tenantAnalyService.getAnalyTenantBoxInfo(tenantId);
    }
}
