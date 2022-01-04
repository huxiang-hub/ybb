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
package com.yb.mater.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料退料管理_yb_execute_offmater实体类
 *
 * @author BladeX
 * @since 2021-01-18
 */
@Data
@TableName("yb_execute_offmater")
@ApiModel(value = "ExecuteOffmater对象", description = "物料退料管理_yb_execute_offmater")
public class ExecuteOffmater implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id")
    private String matId;
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String matName;
    /**
     * 物料使用数量
     */
    @ApiModelProperty(value = "物料使用数量")
    private Integer matNum;
    /**
     * 原始总数
     */
    @ApiModelProperty(value = "原始总数")
    private Integer totalNum;
    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;
    /**
     * 物料条码
     */
    @ApiModelProperty(value = "物料条码")
    private String barCode;
    /**
     * 打印次数
     */
    @ApiModelProperty(value = "打印次数")
    private Integer printNum;
    /**
     * 打印时间
     */
    @ApiModelProperty(value = "打印时间")
    private LocalDateTime printTime;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 排程单id
     */
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    /**
     * 班次信息
     */
    @ApiModelProperty(value = "排产单id")
    private Integer wfId;
    /**
     * 操作用户
     */
    @ApiModelProperty(value = "操作用户")
    private Integer usId;
    /**
     * 上料id
     */
    @ApiModelProperty(value = "上料id")
    private Integer upId;
    /**
     * 托盘标识卡id
     */
    @ApiModelProperty(value = "托盘标识卡id")
    private Integer etId;
    /**
     * 库位id
     */
    @ApiModelProperty(value = "库位id")
    private Integer seatId;
    /**
     * 状态：1退料、2出库
     */
    @ApiModelProperty(value = "状态：1退料、2出库")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createAt;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateAt;
    /**
     * 库位
     */
    @ApiModelProperty(value = "库位")
    @TableField(exist = false)
    private String storeSeatNo;

    @ApiModelProperty(value = "操作人")
    @TableField(exist = false)
    private String usName;


}
