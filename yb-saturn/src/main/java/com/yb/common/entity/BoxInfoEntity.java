package com.yb.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author by SUMMER
 * @date 2020/3/14.
 */
@Data
public class BoxInfoEntity {
    private String uuid;
    //盒子设备状态
    private String status;
    //一天数值
    private int numberOfDay;
    //实时数值
    private int number;
    private String mac;
    private String machineName;
    private Date updateAt;
    private String deptName;
    private Long macId;
    private String brand;
    private String model;

    //额外的，不计入数据库
    private String speed;
    //不计入数据库
    private int increment;
    //不计入数据库
    private Date startTime;
    //不计入数据库
    private String processValue;
    //不计入数据库
    private Integer processId;

}

