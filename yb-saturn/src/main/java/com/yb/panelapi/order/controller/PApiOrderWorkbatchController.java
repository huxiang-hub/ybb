package com.yb.panelapi.order.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.order.service.IOrderWorkbatchService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/plapi/orderworkbatch")
public class PApiOrderWorkbatchController {


    @Autowired
    private IOrderWorkbatchService orderWorkbatchService;
    /**
     * 订单展示界面接口
     */
    @PostMapping("/getOrderByInfo")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "查询", notes = "根据前段页面传过来的设备的id")
    public R getOrderByInfo(Integer mId, Integer wbId) {
        if(mId==null){
            R.fail("请输入正确的型号ID");
        }
        return R.data(orderWorkbatchService.getOrderByInfo(mId,wbId));
    }

}
