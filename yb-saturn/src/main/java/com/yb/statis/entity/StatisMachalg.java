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

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_statis_ordfull")
@ApiModel(value = "StatisMachalg对象", description = "OEE数据信息表_yb_statis_ordfull（OEE的全量统计-状态变更及中间定时5分钟已上报记录）")
public class StatisMachalg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 统计日期格式20200314
     */
    @ApiModelProperty(value = "统计日期格式20200314")
    private Integer algDate;

    /**
     * 定时统计时间格式03:03
     */
    @ApiModelProperty(value = "定时统计时间格式03:03")
    private Integer regularTime;

    /**
     *设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private Integer maId;

    /**
     *运行累计时间（秒）
     */
    @ApiModelProperty(value = "运行累计时间（秒）")
    private Integer runStay;

    /**
     *运行次数
     */
    @ApiModelProperty(value = "运行次数")
    private Integer runNum;

    /**
     *停机累计时间（秒）
     */
    @ApiModelProperty(value = "停机累计时间（秒）")
    private Integer stopStay;

    /**
     *停机次数
     */
    @ApiModelProperty(value = "停机次数")
    private Integer stopNum;

    /**
     *故障累计时间（秒）
     */
    @ApiModelProperty(value = "故障累计时间（秒）")
    private Integer faultStay;

    /**
     *故障累计次数（秒）
     */
    @ApiModelProperty(value = "故障累计次数")
    private Integer faultNum;

    /**
     *换模累计时间（秒）
     */
    @ApiModelProperty(value = "换模累计时间（秒）")
    private Integer exchangeStay;

    /**
     *换模累计次数
     */
    @ApiModelProperty(value = "换模累计次数")
    private Integer exchangeNum;

    /**
     *空转累计时间（秒）
     */
    @ApiModelProperty(value = "空转累计时间（秒）")
    private Integer blankrunStay;

    /**
     *空转次数
     */
    @ApiModelProperty(value = "空转次数")
    private Integer blankrunNum;

    /**
     *
     */
    @ApiModelProperty(value = "开机时间")
    private Integer startTime;

    /**
     *
     */
    @ApiModelProperty(value = "结束时间")
    private Integer endTime;

    /**
     *作业数F
     */
    @ApiModelProperty(value = "作业数F")
    private Integer workCount;

    /**
     *良品数-无缺陷
     */
    @ApiModelProperty(value = "良品数-无缺陷")
    private Integer nodefectCount;

    /**
     *良品率G*
     */
    @ApiModelProperty(value = "良品率G*")
    private Integer yieldRate;

    /**
     *实际能力生产性H*每小时生产速度取整
     */
    @ApiModelProperty(value = "实际能力生产性H*每小时生产速度取整")
    private Integer factSpeed;

    /**
     *理论能力生产性I
     */
    @ApiModelProperty(value = "理论能力生产性I")
    private Integer normalSpeed;

    /**
     *性能稼动率J*
     */
    @ApiModelProperty(value = "性能稼动率J*")
    private Integer performRate;

    /**
     *时间稼动率E*
     */
    @ApiModelProperty(value = "时间稼动率E*")
    private Integer utilizeRate;

    /**
     *OEE设备综合效率K*
     */
    @ApiModelProperty(value = "OEE设备综合效率K*")
    private Integer gatherRate;

    /**
     *创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Integer createAt;

    /**
     *更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Integer updateAt;


}
