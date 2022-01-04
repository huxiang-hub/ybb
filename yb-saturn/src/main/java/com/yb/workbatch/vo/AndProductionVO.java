package com.yb.workbatch.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 再排查
 */
@Data
public class AndProductionVO implements Serializable {

    private Integer id;
    /*排产日期*/
    private String sdDate;
    /*计划数*/
    private Integer planNum;
    /*换膜时间*/
    private Integer mouldStay;
    /*班次id*/
    private Integer wsId;
    /*速度*/
    private Integer speed;
    /*设备id*/
    private Integer maId;

}
