package com.yb.synchrodata.constant;

/**
 * @Author lzb
 * @Date 2020/12/16 11:23
 **/
public interface NxhrUrlConstant {
    /**
     * 基础路径
     */
    String BASE_URL = "http://hrjt.nxhrgf.com:52028/nxhr/";
    /**
     * 同步排产单
     */
    String SYNC_WORK_URL = BASE_URL + "wbol/sync";
    /**
     * 同步人员
     */
    String SYNC_PERSON_URL = BASE_URL + "bladeUser/syncDeptPerson";
    /**
     * 同步部门
     */
    String SYNC_DEPT_URL = BASE_URL + "bladeUser/sync/dept";
    /**
     * 同步客户
     */
    String SYNC_CUSTOMER_URL = BASE_URL + "customer/syncCustomer";
    /**
     * 同步设备
     */
    String SYNC_MACHINE_URL = BASE_URL + "wbol/syncProcMachine";
    /**
     * 同步工序
     */
    String SYNC_PROCESS_URL = BASE_URL + "wbol/syncProcMachine";
    /**
     * 获取物料信息
     */
//    String GET_MATERIAL_URL = "http://127.0.0.1:2028/nxhr/" + "material/selectByBarCode";
    String GET_MATERIAL_URL = BASE_URL + "material/selectByBarCode";
}
