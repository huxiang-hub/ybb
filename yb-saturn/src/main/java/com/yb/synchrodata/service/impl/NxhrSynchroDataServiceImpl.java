package com.yb.synchrodata.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yb.nxhr.client.NxhrClient;
import com.yb.synchrodata.constant.NxhrUrlConstant;
import com.yb.synchrodata.constant.TenantConstant;
import com.yb.synchrodata.service.SynchroDataService;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author lzb
 * @Date 2020/12/15 11:41
 **/
@Service(TenantConstant.NXHR)
public class NxhrSynchroDataServiceImpl implements SynchroDataService {

    private static RestTemplate restTemplate = new RestTemplate();

    @Override
    public synchronized R anycWorkbatch(Integer machineId) {
        return restTemplate.getForObject(NxhrUrlConstant.SYNC_WORK_URL, R.class);
    }

    @HystrixCommand(fallbackMethod = "fallbackNxhr")
    @Override
    public synchronized R anycBladeUser() {
        return restTemplate.getForObject(NxhrUrlConstant.SYNC_PERSON_URL, R.class);
    }

    @HystrixCommand(fallbackMethod = "fallbackNxhr")
    @Override
    public synchronized R anycMachine() {
        return restTemplate.getForObject(NxhrUrlConstant.SYNC_MACHINE_URL, R.class);
    }

    @HystrixCommand(fallbackMethod = "fallbackNxhr")
    @Override
    public synchronized R anycDept() {
        return restTemplate.getForObject(NxhrUrlConstant.SYNC_DEPT_URL, R.class);
    }

    @HystrixCommand(fallbackMethod = "fallbackNxhr")
    @Override
    public synchronized R anycProcess() {
        return restTemplate.getForObject(NxhrUrlConstant.SYNC_PROCESS_URL, R.class);
    }

    @HystrixCommand(fallbackMethod = "fallbackNxhr")
    @Override
    public synchronized R anycCustomer() {
        return restTemplate.getForObject(NxhrUrlConstant.SYNC_CUSTOMER_URL, R.class);
    }

    public R fallbackNxhr(Integer machineId) {
        return R.fail("同步失败,调用宁夏和瑞同步服务发生错误");
    }
}
