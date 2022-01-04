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

/**
 * OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_statis_regular")
@ApiModel(value = "StatisRegular对象", description = "OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）")
public class StatisRegular implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单统计id
     */
    @ApiModelProperty(value = "订单统计id")
    private Integer soId;
    /**
     * 工作日期
     */
    @ApiModelProperty(value = "工作日期")
    private LocalDate workDate;
    /**
     * 工作时间
     */
    @ApiModelProperty(value = "工作时间")
    private LocalTime workTime;
    /**
     * 当前停留过程（LNMOPQRA）
     */
    @ApiModelProperty(value = "当前停留过程（LNMOPQRA）")
    private String currStay;
    /**
     * 生产准备时间L（秒计算）
     */
    @ApiModelProperty(value = "生产准备时间L（秒计算）")
    private Integer prepareStay;
    /**
     * 自主维修N1
     */
    @ApiModelProperty(value = "自主维修N1")
    private Integer repairStay;
    /**
     * 设备故障N2（生产设备故障，公共设备故障）
     */
    @ApiModelProperty(value = "设备故障N2（生产设备故障，公共设备故障）")
    private Integer faultStay;
    /**
     * 品质故障M
     */
    @ApiModelProperty(value = "品质故障M")
    private Integer qualityStay;
    /**
     * 计划停机-换型时间O1
     */
    @ApiModelProperty(value = "计划停机-换型时间O1")
    private Integer replacemodStay;
    /**
     * 计划停机-计划、停机O2
     */
    @ApiModelProperty(value = "计划停机-计划、停机O2")
    private Integer planStay;
    /**
     * 管理停止P-待机、清洗、等料、交接班、等指示
     */
    @ApiModelProperty(value = "管理停止P-待机、清洗、等料、交接班、等指示")
    private Integer manageStay;
    /**
     * 磨损更换Q-清理油墨、清洗（更换）管道、加液、循环、其他
     */
    @ApiModelProperty(value = "磨损更换Q-清理油墨、清洗（更换）管道、加液、循环、其他")
    private Integer abrasionStay;
    /**
     * 休息吃饭R
     */
    @ApiModelProperty(value = "休息吃饭R")
    private Integer restStay;
    /**
     * 出勤时间A
     */
    @ApiModelProperty(value = "出勤时间A")
    private Integer workStay;
    /**
     * 可用稼动时间B*
     */
    @ApiModelProperty(value = "可用稼动时间B*")
    private Integer utilizeStay;
    /**
     * 计划稼动时间C*
     */
    @ApiModelProperty(value = "计划稼动时间C*")
    private Integer planutilizeStay;
    /**
     * 实际稼动时间D*
     */
    @ApiModelProperty(value = "实际稼动时间D*")
    private Integer factutilizeStay;
    /**
     * 时间稼动率E*
     */
    @ApiModelProperty(value = "时间稼动率E*")
    private BigDecimal utilizeRate;
    /**
     * 加工数-任务数
     */
    @ApiModelProperty(value = "加工数-任务数")
    private Integer taskCount;
    /**
     * 良品数-无缺陷
     */
    @ApiModelProperty(value = "良品数-无缺陷")
    private Integer nodefectCount;
    /**
     * 作业数F
     */
    @ApiModelProperty(value = "作业数F")
    private Integer workCount;
    /**
     * 良品率G*
     */
    @ApiModelProperty(value = "良品率G*")
    private BigDecimal yieldRate;
    /**
     * 实际能力生产性H*每小时生产速度取整
     */
    @ApiModelProperty(value = "实际能力生产性H*每小时生产速度取整")
    private Integer factSpeed;
    /**
     * 理论能力生产性I
     */
    @ApiModelProperty(value = "理论能力生产性I")
    private Integer normalSpeed;
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
    private LocalDateTime createAt;


}
