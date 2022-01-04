package com.yb.machine.vo;

import lombok.Data;

@Data
public class CompanyVO {
    /**
     * 公司名称
     */
    private String fName;
    /**
     * 所属车间
     */
    private String dpName;
    /**
     * 工序名称
     */
    private String PrName;
    /**
     * 品牌
     */
    private String brand; /**
     * 型号
     */
    private String model;

    /* 设备名称
     */
    private String maName;


}
