package com.yb.execute.vo;

import com.yb.execute.entity.ExecutePutStore;
import lombok.Data;

@Data
public class ExecutePutStoreVO extends ExecutePutStore {
    /**
     * 员工名字
     */
    private String name;
    /**
     * 批次编号
     */
    private String batchNo;
    /**
     * 订单名称
     */
    private String odName;
    /**
     * 需要其他入库的数量
     */
    private Integer newPutNum;
    /**
     * 需要其他入库的地址
     */
    private String newPutAddr;

}
