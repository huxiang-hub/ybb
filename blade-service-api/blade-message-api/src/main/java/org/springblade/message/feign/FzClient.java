package org.springblade.message.feign;

import org.springblade.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/6/13 15:04
 */
@FeignClient(value = "yb-fz", fallback = FzClientFallBack.class)
public interface FzClient {
    /**
     * 更新保存登录用户
     *
     * @return
     */
    @GetMapping("/fz/updateBladeUser")
    R updateBladeUser();

    /**
     * 更新保存部门
     *
     * @return
     */
    @GetMapping("/fz/updateDept")
    R updateDept();

    @GetMapping("/fz/updateMachine")
    R updateMachine();

    @GetMapping("/fz/updateProcessMachlink")
    R updateProcessMachlink();

    /**
     * 更新保存工序
     * @return
     */
    @GetMapping("/fz/updateProcessWorkinfo")
    R updateProcessWorkinfo();

    /**
     * 更新保存客户
     */
    @GetMapping("/fz/updateCrmCustomer")
    R updateCrmCustomer();

    /**
     * 更新保存订单及其产品相关信息
     */
    @GetMapping("/fz/updateOrderInfo")
    R updateOrderInfo();
    /**
     * 更新保存产品分类
     */
    @GetMapping("/fz/updatePdClassify")
    R updatePdClassify();

    /**
     * 更新保存排产
     */
    @GetMapping("/fz/updateWorkbatch")
    R updateWorkbatch();

    /**
     *更新排产
     * @param Id
     * @return
     */
    @GetMapping("/fz/updateWorkbatchById")
    R updateWorkbatchById(Integer Id);
}
