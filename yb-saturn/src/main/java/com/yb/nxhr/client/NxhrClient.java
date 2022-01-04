package com.yb.nxhr.client;

import org.springblade.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author lzb
 * @Date 2020/12/13 13:30
 **/
@FeignClient(value = "yb-nxhr",path = "nxhr", fallback = NxhrClientFallBack.class)
public interface NxhrClient {

    /**
     * 同步排产单（同时会同步工序及设备）
     * @return
     */
    @GetMapping("wbol/sync")
    R sync();

    /**
     * 同步部门并且同步人事表
     * @return
     */
    @GetMapping("bladeUser/syncDeptPerson")
    R syncDeptPerson();

    /**
     * 同步设备
     * @return
     */
    @GetMapping("wbol/syncProcMachine")
    R syncProcMachine();

    /**
     * 同步客户
     * @return
     */
    @GetMapping("customer/syncCustomer")
    R syncCustomer();
}
