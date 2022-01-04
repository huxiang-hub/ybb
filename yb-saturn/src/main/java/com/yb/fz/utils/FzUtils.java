package com.yb.fz.utils;

import org.springblade.message.feign.FzClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FzUtils {

    @Autowired
    private FzClient fzClient;

    private static FzUtils fzUtils;

    @PostConstruct
    public void init() {
        fzUtils = this;
        fzUtils.fzClient = this.fzClient;
    }

    public static void updateBladeUser() {
        fzUtils.fzClient.updateBladeUser();
    }

    public static void updateDept() {
        fzUtils.fzClient.updateDept();
    }

    public static void updateCrmCustomer() {
        fzUtils.fzClient.updateCrmCustomer();
    }

    public static void updateMachine() {
        fzUtils.fzClient.updateMachine();
    }

    public static void updateOrderInfo() {
        fzUtils.fzClient.updateOrderInfo();
    }

    public static void updatePdClassify() {
        fzUtils.fzClient.updatePdClassify();
    }

    public static void updateProcessMachlink() {
        fzUtils.fzClient.updateProcessMachlink();
    }

    public static void updateProcessWorkinfo() {
        fzUtils.fzClient.updateProcessWorkinfo();
    }

    public static void updateWorkbatch() {
        fzUtils.fzClient.updateWorkbatch();
    }

    public static void updateWorkbatchById(Integer Id) {
            fzUtils.fzClient.updateWorkbatchById(Id);
    }

}
