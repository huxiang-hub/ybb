package com.anaysis.controller;

import com.anaysis.service.ICrmCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lzb
 * @Date 2020/12/4 13:03
 **/
@Api(tags = "客户导入")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICrmCustomerService crmCustomerService;

    @ApiOperation("同步客户")
    @GetMapping("syncCustomer")
    public R syncCustomer() {
        crmCustomerService.sync();
        return R.success("同步成功");
    }
}
