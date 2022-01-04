package com.yb.xy.utils;

import org.springblade.core.tool.api.R;
import org.springblade.message.feign.XyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/*****
 * 调用远程微服务反馈结果信息
 */
@Component
public class XyUtils {

    @Autowired
    private XyClient xyClient;

    private static XyUtils xyUtils;



    @PostConstruct
    public void init() {
        xyUtils = this;
        xyUtils.xyClient = this.xyClient;
    }

    public static void updateBladeUser() {
        xyUtils.xyClient.updateBladeUser();
    }

    public static void updateDept() {
        xyUtils.xyClient.updateDept();
    }

    public static void updateCrmCustomer() {
        xyUtils.xyClient.updateCrmCustomer();
    }

    public static void updateMachine() {
        xyUtils.xyClient.updateMachine();
    }

    public static void updateOrderInfo() {
        xyUtils.xyClient.updateOrderInfo();
    }

    public static void updatePdClassify() {
        xyUtils.xyClient.updatePdClassify();
    }

    public static void updateProcessMachlink() {
        xyUtils.xyClient.updateProcessMachlink();
    }

    public static void updateProcessWorkinfo() {
        xyUtils.xyClient.updateProcessWorkinfo();
    }

    /****
     * 页面分设备进行数据同步操作
     * @param maId
     */
    public static void updateWorkbatch(Integer maId) {
        xyUtils.xyClient.updateWorkbatch(maId);
    }

    public static void updateWorkbatchById(Integer Id) {
            xyUtils.xyClient.updateWorkbatchById(Id);
    }

    public static void updateWorkbatchStatus() {  xyUtils.xyClient.updateWorkbatchStatus(); }

//    public static R noImport() {
//        return xyUtils.xyClient.noImport();
//    }

}
