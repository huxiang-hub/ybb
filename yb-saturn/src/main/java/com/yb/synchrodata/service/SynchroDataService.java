package com.yb.synchrodata.service;

import org.springblade.core.tool.api.R;

/**
 * @Author lzb
 * @Date 2020/12/15 11:39
 **/
public interface SynchroDataService {
    /**
     * 同步排产单
     * @param machineId
     * @return
     */
    R anycWorkbatch(Integer machineId);

    /**
     * 同步人员
     * @return
     */
    R anycBladeUser();

    /**
     * 同步设备
     * @return
     */
    R anycMachine();

    /**
     * 同步部门
     * @return
     */
    R anycDept();

    /**
     * 同步工序
     * @return
     */
    R anycProcess();

    /**
     * 同步客户
     * @return
     */
    R anycCustomer();
}
