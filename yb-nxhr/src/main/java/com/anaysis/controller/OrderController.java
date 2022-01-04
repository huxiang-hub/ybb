package com.anaysis.controller;

import com.anaysis.service.ICrmCustomerService;
import com.anaysis.service.IOrderOrdinfoService;
import com.anaysis.service.IProdPdinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lzb
 * @Date 2020/12/4 15:05
 **/
@Api(tags = "订单导入")
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderOrdinfoService orderOrdinfoService;
    @Autowired
    private IProdPdinfoService prodPdinfoService;
    @Autowired
    private ICrmCustomerService crmCustomerService;

    @ApiOperation("订单导入")
    @GetMapping("sync")
    public R syncOrd() {
        // 产品、客户、订单同步执行
        prodPdinfoService.sync();
        crmCustomerService.sync();
        orderOrdinfoService.sync();
        return R.success("同步成功");
    }

    @ApiOperation("产品导入")
    @GetMapping("sync/prod")
    public R syncProduct() {
        prodPdinfoService.sync();
        return R.success("同步成功");
    }
}
