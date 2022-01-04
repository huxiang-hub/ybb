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
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * VIEW实体类
 *
 * @author BladeX
 * @since 2021-01-15
 */
@Data
@TableName("v_ordexecute")
@ApiModel(value = "Ordexecute对象", description = "VIEW")
public class Ordexecute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 批次编号
     */
    @ApiModelProperty(value = "批次编号")
    private String wbNo;

    @ApiModelProperty(value = "订单编号")
    private String odNo;
    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private Integer pdId;
    /**
     * 订货厂家-客户名称
     */
    @ApiModelProperty(value = "订货厂家-客户名称")
    private String cmName;

    @TableField("prNames")
    private String prnames;

    @TableField("planAndArrangeNum")
    private String planandarrangenum;

    @TableField("countNum")
    private String countnum;


}
