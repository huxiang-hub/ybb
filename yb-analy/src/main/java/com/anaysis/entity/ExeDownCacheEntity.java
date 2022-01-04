package com.anaysis.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author by SUMMER
 * @date 2020/3/14.
 */
@Data
public class ExeDownCacheEntity {

    //设备的状态
    private String status;
    //设备停机开始时间
    private Date startTime;
    //弹框标志位
    private Boolean flag;
    //系统超时记录
    private Boolean sysflag;

    private Integer sysFaultId;//yb_execute_fault
}
