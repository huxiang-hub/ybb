package org.springblade.message.feign;


import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Component;

@Component
public class FzClientFallBack implements FzClient {

    @Override
    public R updateBladeUser() {
        return R.fail("fail");
    }

    @Override
    public R updateDept() {
        return R.fail("fail");
    }

    @Override
    public R updateMachine() {
        return R.fail("fail");
    }

    @Override
    public R updateProcessMachlink() {
        return  R.fail("fail");
    }

    @Override
    public R updateProcessWorkinfo() {
        return R.fail("服务暂不可用");
    }

    @Override
    public R updateCrmCustomer() {
        return R.fail("服务暂不可用");
    }

    @Override
    public R updateOrderInfo() {
        return R.fail("服务暂不可用");
    }

    @Override
    public R updatePdClassify() {
        return R.fail("服务暂不可用");
    }

    @Override
    public R updateWorkbatch() {return R.fail("服务暂不可用");}

    @Override
    public R updateWorkbatchById(Integer Id) {return R.fail("服务暂不可用");}
}
