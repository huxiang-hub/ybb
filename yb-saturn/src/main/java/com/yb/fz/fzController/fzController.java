package com.yb.fz.fzController;

import com.yb.fz.utils.FzUtils;
import org.springblade.core.tool.api.R;
import org.springblade.message.feign.FzClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fz")
public class fzController implements FzClient {
    /**
     *用户，人员
     * @return
     */
    @Override
    @GetMapping("/updateBladeUser")
    public R updateBladeUser() {
        FzUtils.updateBladeUser();
        return R.success("ok");
    }

    /**
     *部门
     * @return
     */
    @Override
    @GetMapping("/updateDept")
    public R updateDept() {
        FzUtils.updateDept();
        return R.success("ok");
    }

    /**
     *设备和设备对应信息
     * @return
     */
    @Override
    @GetMapping("/updateMachine")
    public R updateMachine() {
        FzUtils.updateMachine();
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateProcessMachlink")
    public R updateProcessMachlink() {
        FzUtils.updateProcessMachlink();
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateProcessWorkinfo")
    public R updateProcessWorkinfo() {
        FzUtils.updateProcessWorkinfo();
        return R.success("ok");
    }

    /**
     *客户
     * @return
     */
    @Override
    @GetMapping("/updateCrmCustomer")
    public R updateCrmCustomer() {
        FzUtils.updateCrmCustomer();
        return R.success("ok");
    }

    /**
     *订单
     * @return
     */
    @Override
    @GetMapping("/updateOrderInfo")
    public R updateOrderInfo() {
        FzUtils.updateOrderInfo();
        return R.success("ok");
    }

    @Override
    @GetMapping("/updatePdClassify")
    public R updatePdClassify() {
        FzUtils.updatePdClassify();
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateWorkbatch")
    public R updateWorkbatch() {
        FzUtils.updateWorkbatch();
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateWorkbatchById")
    public R updateWorkbatchById(@RequestParam(name = "Id")Integer Id) {
        FzUtils.updateWorkbatchById(Id);
        return R.success("ok");
    }
}

