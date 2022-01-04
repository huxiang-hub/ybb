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
package com.yb.stroe.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 仓库历史台账实体类
 *
 * @author BladeX
 * @since 2021-01-28
 */
@Data
@TableName("yb_store_inventoryhis")
@ApiModel(value = "仓库历史台账StoreInventoryhis对象", description = "仓库历史台账StoreInventoryhis对象")
public class StoreInventoryhis implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 仓库台账id
     */
    @ApiModelProperty(value = "仓库台账id")
    private Integer siId;
    /**
     * 存储类型
     */
    @ApiModelProperty(value = "存储类型")
    private Integer stType;
    /**
     * 库位id
     */
    @ApiModelProperty(value = "库位id")
    private Integer stId;
    /**
     * 库位编号
     */
    @ApiModelProperty(value = "库位编号")
    private String stNo;
    /**
     * 入库尺寸
     */
    @ApiModelProperty(value = "入库尺寸")
    private String stSize;
    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id")
    private String mlId;
    /**
     * 托版产品数量
     */
    @ApiModelProperty(value = "托版产品数量")
    private Integer etPdnum;
    /**
     * 托板标识卡id
     */
    @ApiModelProperty(value = "托板标识卡id")
    private Integer etId;
    /**
     * 托板占用位置数量：单位：板
     */
    @ApiModelProperty(value = "托板占用位置数量：单位：板")
    private Integer layNum;
    /**
     * 状态1占用2锁定待入库3移库
     */
    @ApiModelProperty(value = "状态1占用2锁定待入库3移库")
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    /**
     * 迁移时间
     */
    @ApiModelProperty(value = "迁移时间")
    private LocalDateTime createTime;


}
