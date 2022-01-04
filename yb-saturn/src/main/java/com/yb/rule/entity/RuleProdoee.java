/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.rule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品规则模型表实体类
 */
@Data
@TableName("yb_rule_prodoee")
@ApiModel(value = "RuleProdoee对象", description = "产品规则模型表")
public class RuleProdoee implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *产品分类(可选)
     */
    @ApiModelProperty(value = "产品分类(可选)")
    private Integer pcId;
    /**
     *设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 产品尺寸(可选)长宽格式:1200*400'
     */
    @ApiModelProperty(value = "产品尺寸(可选)长宽格式:1200*400'")
    private String pdSize;
    /**
     * 工序ID
     */
    @ApiModelProperty(value = "工序ID")
    private Integer PrId;
    /**
     * 产品长(mm毫米)
     */
    @ApiModelProperty(value = "产品长(mm毫米)")
    private Integer pdLength;
    /**
     * 产品宽(mm毫米)
     */
    @ApiModelProperty(value = "产品宽(mm毫米)")
    private Integer pdWidth;
    /**
     * 尺寸最大值(mm毫米)
     */
    @ApiModelProperty(value = "尺寸最大值(mm毫米)")
    private Integer sizeMax;
    /**
     * 尺寸最小值(mm毫米)
     */
    @ApiModelProperty(value = "尺寸最小值(mm毫米)")
    private Integer sizeMin;
    /**
     * 物料克数要求范围(克)
     */
    @ApiModelProperty(value = "物料克数要求范围(克)")
    private Integer material;
    /**
     * 物料最大克数
     */
    @ApiModelProperty(value = "物料最大克数")
    private Integer mateMax;
    /**
     * 物料最小克数
     */
    @ApiModelProperty(value = "物料最小克数")
    private Integer mateMin;
    /**
     * 计划最大数(张)
     */
    @ApiModelProperty(value = "计划最大数(张)")
    private Integer plannumMax;
    /**
     * 计划最小数(张)
     */
    @ApiModelProperty(value = "计划最小数(张)")
    private Integer plannumMin;

    /**
     * 标准时速(小时)
     */
    @ApiModelProperty(value = "标准时速(小时)")
    private Integer speed;

    /**
     * 固定准备时间(分钟)(当前默认换膜)分钟
     */
    @ApiModelProperty(value = "固定准备时间(分钟)(当前默认换膜)分钟")
    private Integer prepareTime;
    /**
     * '状态:0停用1启用'
     */
    @ApiModelProperty(value = "状态:0停用1启用")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "尺寸规则类型 1、长+宽2、长*宽")
    private Integer model;
}
