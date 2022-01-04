/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yb.workbatch.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * VIEW实体类
 *
 * @author BladeX
 * @since 2021-01-11
 */
@Data
@TableName("v_workbyma")
@ApiModel(value = "Workbyma对象", description = "VIEW")
public class Workbyma implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号")
    private Integer maId;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String maName;
    /**
     * 设备分类：maType 数据字典
     */
    @ApiModelProperty(value = "设备分类：maType 数据字典")
    private String maType;

    /**
     * 设备排序：maType 数据字典
     */
    @ApiModelProperty(value = "设备排序")
    private String maSort;

    @ApiModelProperty(value = "执行表主键")
    private Integer exId;
    /**
     * 班次ID
     */
    @ApiModelProperty(value = "班次ID")
    private Integer wsId;

    @ApiModelProperty(value = "排程主键唯一")
    private Integer wfId;
    /**
     * 执行开始时间
     */
    @ApiModelProperty(value = "执行开始时间")
    private LocalDateTime startTime;
    /**
     * c1开始时间
     */
    @ApiModelProperty(value = "c1开始时间")
    private LocalDateTime exeTime;
    /**
     * 执行结束时间
     */
    @ApiModelProperty(value = "执行结束时间")
    private LocalDateTime endTime;
    /**
     * 排产日期
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @ApiModelProperty(value = "排产日期")
    private String sdDate;

    @ApiModelProperty(value = "排产顺序")
    private String sdSort;
    @ApiModelProperty(value = "批次单编号")
    private String wbNo;
    @ApiModelProperty(value = "操作人员")
    private String usIds;

}
