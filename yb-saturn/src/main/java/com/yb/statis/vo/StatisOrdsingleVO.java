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
package com.yb.statis.vo;

import com.yb.statis.entity.StatisOrdsingle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StatisOrdsingleVO对象", description = "OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）")
public class StatisOrdsingleVO extends StatisOrdsingle {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "oee统计时间2020-04-15")
    private String oeDate;
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "设备名称")
    private String maName;
    @ApiModelProperty(value = "人员机长id")
    private Integer usId;
    @ApiModelProperty(value = "人员姓名")
    private String usName;
    @ApiModelProperty(value = "班次名称")
    private String sfName;
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
     * 标准质检次数
     */
    @ApiModelProperty(value = "质检次数")
    private Integer standQualityNum;
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
    private Integer StandPrepareStay;

    /**
     * 标准保养次数
     */
    @ApiModelProperty(value = "标准保养次数")
    private Integer standMaintainNum;

    /**
     * 标准保养时长
     */
    @ApiModelProperty(value = "标准保养时长")
    private Integer standMaintainTime;


    /**
     * 标准换膜次数
     */
    @ApiModelProperty(value = "标准换膜次数")
    private Integer standMouldNum;

    /**
     * 标准换膜时长
     */
    @ApiModelProperty(value = "标准换膜时长")
    private Integer standMouldTime;

    private Integer prId;
    private Integer dpId;
    /**
     * 排产开始时间
     */
    private Date ordStart;
    /**
     * 排产结束时间
     */
    private Date ordEnd;
}
