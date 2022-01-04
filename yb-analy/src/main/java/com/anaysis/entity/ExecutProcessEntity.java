package com.anaysis.entity;

import lombok.Data;

import java.util.Date;

/***
 * 缓存对象，并不是数据库对应数据
 */
@Data
public class ExecutProcessEntity {
    //设备id信息
    private Integer maId;
    //设备停机开始时间
    private Date startTime;
    //当前订单的数量
    private Integer currNum;
    //停机弹窗的时间
    private Integer limitTime;
    //停机弹窗消息时间
    private Integer faultDisapper;
    //限制停机系统记录的时间
    private Integer syslimitTime;
    //质量巡检类型
    private Integer model;
    //质量检测数量限制
    private Integer limitNum;
    //质量检测时间限制
    private Integer qualityTime;
    //质量弹窗消失时间  间隔
    private Integer qualityDisapper;
}
