package com.yb.synchrodata.service.impl;

import com.yb.synchrodata.constant.TenantConstant;
import com.yb.synchrodata.service.SynchroDataService;
import org.springblade.core.tool.api.R;
import org.springblade.message.feign.XyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author lzb
 * @Date 2020/12/15 11:40
 **/
@Service(TenantConstant.JMXY)
public class XySynchroDataServiceImpl implements SynchroDataService {

    @Autowired
    private XyClient xyClient;

    @Override
    public R anycWorkbatch(Integer machineId) {
        // 小于0则同步所有
        if (null == machineId) {
            machineId = -1;
        }
        return xyClient.updateWorkbatch(machineId);
    }

    @Override
    public R anycBladeUser() {
        return xyClient.updateBladeUser();
    }

    @Override
    public R anycMachine() {
        return xyClient.updateMachine();
    }

    @Override
    public R anycDept() {
        return xyClient.updateDept();
    }

    @Override
    public R anycProcess() {
        return xyClient.updateProcessWorkinfo();
    }

    @Override
    public R anycCustomer() {
        return xyClient.updateCrmCustomer();
    }
}
