package com.yb.nxhr.client;

import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Component;

/**
 * @Author lzb
 * @Date 2020/12/15 14:00
 **/
@Component
public class NxhrClientFallBack implements NxhrClient {

    @Override
    public R sync() {
        return R.fail("数据同步服务不可用");
    }

    @Override
    public R syncDeptPerson() {
        return R.fail("数据同步服务不可用");
    }

    @Override
    public R syncProcMachine() {
        return R.fail("数据同步服务不可用");
    }

    @Override
    public R syncCustomer() {
        return R.fail("数据同步服务不可用");
    }
}
