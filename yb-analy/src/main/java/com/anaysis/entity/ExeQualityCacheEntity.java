package com.anaysis.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author by SUMMER
 * @date 2020/3/16.
 */
@Data
public class ExeQualityCacheEntity {

    //生产计数
    private Integer number;
    //正式生产开始时间
    private Date startTime;
    //弹框标志位
    private Boolean flag;
    //执行表id
    private Integer esId;
    //质量巡检类型（时间，数量）
    private Integer model;
}
