package org.springblade.message.feign;

import org.springblade.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/6/13 15:04
 */
@FeignClient(value = "yb-xy", fallback = XyClientFallBack.class)
public interface XyClient {
    /**
     * 更新保存登录用户
     *
     * @return
     */
    @GetMapping("/xy/updateBladeUser")
    R updateBladeUser();

    /**
     * 更新保存部门
     *
     * @return
     */
    @GetMapping("/xy/updateDept")
    R updateDept();

    @GetMapping("/xy/updateMachine")
    R updateMachine();

    @GetMapping("/xy/updateProcessMachlink")
    R updateProcessMachlink();

    /**
     * 更新保存工序
     * @return
     */
    @GetMapping("/xy/updateProcessWorkinfo")
    R updateProcessWorkinfo();

    /**
     * 更新保存客户
     */
    @GetMapping("/xy/updateCrmCustomer")
    R updateCrmCustomer();

    /**
     * 更新保存订单及其产品相关信息
     */
    @GetMapping("/xy/updateOrderInfo")
    R updateOrderInfo();
    /**
     * 更新保存产品分类
     */
    @GetMapping("/xy/updatePdClassify")
    R updatePdClassify();

    /**
     * 更新保存排产
     */
    @GetMapping("/xy/updateWorkbatch")
    R updateWorkbatch(@RequestParam(name = "maId") Integer maId);

    /**
     *更新排产
     * @param Id
     * @return
     */
    @GetMapping("/xy/updateWorkbatchById")
    R updateWorkbatchById(Integer Id);
    /**
     *更新排产
     * @return
     */
    @GetMapping("/xy/updateWorkbatchStatus")
    R updateWorkbatchStatus();

}
