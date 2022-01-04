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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_statis_ordsingle")
@ApiModel(value = "StatisOrdsingle对象", description = "OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计）")
public class StatisOrdsingle implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单统计id
     */
    @ApiModelProperty(value = "订单统计id")
    private Integer soId;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /**
     * 出勤时间A -间隔停留时间（分）真实出勤时间
     */
    @ApiModelProperty(value = "出勤时间A -间隔停留时间（分）真实出勤时间")
    private Integer workStay;
    /**
     * 设备保养B2（分计算）
     */
    @ApiModelProperty(value = "设备保养B2（分计算）")
    private Integer maintainStay;

    @ApiModelProperty(value = "设备保养次数（分计算）")
    private Integer maintainNum;
    /**
     * 换模次数(盒子记录)
     */
    @ApiModelProperty(value = "换模次数(盒子记录)")
    private Integer mouldNum;
    /**
     * 换模时长B1(分计算)
     */
    @ApiModelProperty(value = "换模时长B1(分计算)")
    private Integer mouldStay;

    /**
     * 生产准备时间L（分计算）实际时间
     */
    @ApiModelProperty(value = "生产准备时间L（分计算）实际时间")
    private Integer prepareStay;
    /**
     * 标准生产时间分钟（标准出勤-休息开会吃饭）
     */
    @ApiModelProperty(value = "标准生产时间分钟（标准出勤-休息开会吃饭）")
    private Integer standardRuntime;
    /**
     * 计划稼动时间C*
     */
    @ApiModelProperty(value = "计划稼动时间C*")
    private Integer planutilizeStay;
    /**
     * 计划稼动时间C*
     */
    @ApiModelProperty(value = "计划稼动时间C*")
    private Integer jsjPlanutilizeStay;
    /**
     * 实际稼动时间D*
     */
    @ApiModelProperty(value = "实际稼动时间D*")
    private Integer factutilizeStay;
    /**
     * 良品数-无缺陷
     */
    @ApiModelProperty(value = "良品数-无缺陷")
    private Integer nodefectCount;
    /**
     * 废品数量
     */
    @ApiModelProperty(value = "废品数量")
    private Integer watesCount;
    /**
     * 加工数-任务数（计划订单要求总数+损耗数）
     */
    @ApiModelProperty(value = "加工数-任务数（计划订单要求总数+损耗数）")
    private Integer taskCount;
    /**
     * 作业数F（实际作业总数）
     */
    @ApiModelProperty(value = "作业数F（实际作业总数）")
    private Integer workCount;
    /**
     * 盒子计数（硬件记录总数）
     */
    @ApiModelProperty(value = "盒子计数（硬件记录总数）")
    private Integer boxNum;
    /**
     * 质检次数
     */
    @ApiModelProperty(value = "质检次数")
    private Integer qualityNum;
    /**
     * 实际能力生产性H*每小时生产速度取整（班次总数和总时长）
     */
    @ApiModelProperty(value = "实际能力生产性H*每小时生产速度取整（班次总数和总时长）")
    private Integer factSpeed;
    /**
     * 设备故障C2-N（生产设备故障，公共设备故障）
     */
    @ApiModelProperty(value = "设备故障C2-N（生产设备故障，公共设备故障）")
    private Integer faultStay;
    /**
     * 品质故障C2-M
     */
    @ApiModelProperty(value = "品质故障C2-M")
    private Integer qualityStay;
    /**
     * 计划停机C2-O
     */
    @ApiModelProperty(value = "计划停机C2-O")
    private Integer planStay;
    /**
     * 管理停止C2-P
     */
    @ApiModelProperty(value = "管理停止C2-P")
    private Integer manageStay;
    /**
     * 磨损更换C2-Q
     */
    @ApiModelProperty(value = "磨损更换C2-Q")
    private Integer abrasionStay;
    /**
     * 休息吃饭C2-R
     */
    @ApiModelProperty(value = "休息吃饭C2-R")
    private Integer restStay;
    /**
     * 停机合计分钟
     */
    @ApiModelProperty(value = "停机合计分钟")
    private Integer stayTotal;
    /**
     * 时间稼动率E*
     */
    @ApiModelProperty(value = "时间稼动率E*")
    private BigDecimal utilizeRate;
    /**
     * 良品率G*
     */
    @ApiModelProperty(value = "良品率G*")
    private BigDecimal yieldRate;
    /**
     * 性能稼动率J*
     */
    @ApiModelProperty(value = "性能稼动率J*")
    private BigDecimal performRate;
    /**
     * OEE设备综合效率K*
     */
    @ApiModelProperty(value = "OEE设备综合效率K*")
    private BigDecimal gatherRate;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

    /**
     * 标准时间
     */
    @ApiModelProperty(value = "标准时间")
    private Integer standTime;



}
