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
package com.yb.statis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体类
 *
 * @author Blade
 * @since 2020-04-17
 */
@Data
@TableName("yb_statis_oeeset")
@ApiModel(value = "StatisOeeset对象", description = "StatisOeeset对象")
public class StatisOeeset implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设置类型：1设备oee标准2订单oee标准
     */
    @ApiModelProperty(value = "设置类型：1设备oee标准2订单oee标准")
    private Integer stType;
    /**
     * 数据唯一主键
     */
    @ApiModelProperty(value = "数据唯一主键")
    private Integer dbId;
    /**
     * 班次开始时间-计划（订单类型留空）
     */
    @ApiModelProperty(value = "班次开始时间-计划（订单类型留空）")
    private Date sfStarttime;
    /**
     * 班次结束时间-计划（订单类型留空）
     */
    @ApiModelProperty(value = "班次结束时间-计划（订单类型留空）")
    private Date sfEndtime;
    /**
     * 班次时间（订单类型留空）
     */
    @ApiModelProperty(value = "班次时间（订单类型留空）")
    private Integer sfStaytime;
    /**
     * 休息次数
     */
    @ApiModelProperty(value = "休息次数")
    private Integer resetNum;
    /**
     * 休息时间分钟
     */
    @ApiModelProperty(value = "休息时间分钟")
    private Integer resetTime;
    /**
     * 保养次数
     */
    @ApiModelProperty(value = "保养次数")
    private Integer maintainNum;
    /**
     * 保养总时长分钟
     */
    @ApiModelProperty(value = "保养总时长分钟")
    private Integer maintainTime;
    /**
     * 换模次数
     */
    @ApiModelProperty(value = "换模次数")
    private Integer mouldNum;
    /**
     * 换模总时长分钟
     */
    @ApiModelProperty(value = "换模总时长分钟")
    private Integer mouldTime;
    /**
     * 质检次数
     */
    @ApiModelProperty(value = "质检次数")
    private Integer qualityNum;
    /**
     * 理论能力生产性(班次为所有工序的平均值)
     */
    @ApiModelProperty(value = "理论能力生产性(班次为所有工序的平均值)")
    private Integer normalSpeed;
    /**
     * 当班的计划生产总数（计划总数：应缴数+损耗数）
     */
    @ApiModelProperty(value = "当班的计划生产总数（计划总数：应缴数+损耗数）")
    private Integer shiftsTotalnum;
    /**
     * 工序速度；名称分隔如：烫金:3500/模切:4500
     */
    @ApiModelProperty(value = "工序速度；名称分隔如：烫金:3500/模切:4500")
    private String prSpeed;
    /**
     * 损耗总数
     */
    @ApiModelProperty(value = "损耗总数")
    private Integer wasteNum;
    /**
     * 平均难易程度
     */
    @ApiModelProperty(value = "平均难易程度")
    private BigDecimal diffNum;
    /**
     * 生产准备总时长（计划）
     */
    @ApiModelProperty(value = "生产准备总时长（计划）")
    private Integer prepareStay;
    /**
     * 标准生产时间
     */
    private Integer standTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;


}
