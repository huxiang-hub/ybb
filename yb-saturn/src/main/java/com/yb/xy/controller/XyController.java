package com.yb.xy.controller;

import com.yb.xy.utils.XyUtils;
import org.springblade.core.tool.api.R;
import org.springblade.message.feign.XyClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/xy")
public class XyController implements XyClient {
    /**
     * 用户，人员
     *
     * @return
     */
    @Override
    @GetMapping("/updateBladeUser")
    public R updateBladeUser() {
        XyUtils.updateBladeUser();
        return R.success("ok");
    }

    /**
     * 部门
     *
     * @return
     */
    @Override
    @GetMapping("/updateDept")
    public R updateDept() {
        XyUtils.updateDept();
        return R.success("ok");
    }

    /**
     * 设备和设备对应信息
     *
     * @return
     */
    @Override
    @GetMapping("/updateMachine")
    public R updateMachine() {
        XyUtils.updateMachine();
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateProcessMachlink")
    public R updateProcessMachlink() {
        XyUtils.updateProcessMachlink();
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateProcessWorkinfo")
    public R updateProcessWorkinfo() {
        XyUtils.updateProcessWorkinfo();
        return R.success("ok");
    }

    /**
     * 客户
     *
     * @return
     */
    @Override
    @GetMapping("/updateCrmCustomer")
    public R updateCrmCustomer() {
        XyUtils.updateCrmCustomer();
        return R.success("ok");
    }

    /**
     * 订单
     *
     * @return
     */
    @Override
    @GetMapping("/updateOrderInfo")
    public R updateOrderInfo() {
        XyUtils.updateOrderInfo();
        return R.success("ok");
    }

    @Override
    @GetMapping("/updatePdClassify")
    public R updatePdClassify() {
        XyUtils.updatePdClassify();
        return R.success("ok");
    }

    /*****
     * 界面调用同步按钮，选择某个设备进行数据同步操作
     * @param maId
     * @return
     */
    @Override
    @GetMapping("/updateWorkbatch")
    public R updateWorkbatch(@RequestParam(name = "maId") Integer maId) {
        XyUtils.updateWorkbatch(maId);
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateWorkbatchById")
    public R updateWorkbatchById(@RequestParam(name = "Id") Integer Id) {
        XyUtils.updateWorkbatchById(Id);
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateWorkbatchStatus")
    public R updateWorkbatchStatus() {
        XyUtils.updateWorkbatchStatus();
        return R.success("ok");
    }

}

