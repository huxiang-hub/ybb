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
package com.yb.execute.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备停机故障记录表_yb_execute_fault实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_execute_fault")
@ApiModel(value = "ExecuteFault对象", description = "设备停机故障记录表_yb_execute_fault")
public class ExecuteFault implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 执行id
     */
    @ApiModelProperty(value = "执行id")
    private Integer esId;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 排产班次id
     */
    @ApiModelProperty(value = "排产班次id")
    private Integer wfId;
    /**
     * 班次id
     */
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    /**
     * 上报人,登录人id
     */
    @ApiModelProperty(value = "上报人,登录人id")
    private Integer usId;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startAt;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endAt;
    /**
     * 持续时长（sec）
     */
    @ApiModelProperty(value = "持续时长（sec）")
    private Integer duration;
    /**
     * 类别：1工单内停机、0停机间隔未接单
     */
    @ApiModelProperty(value = "类别：1工单内停机、0停机间隔未接单")
    private Integer ef_type;
    /**
     * 设备故障N、品质故障M、计划停机O、管理停止P、磨损更换Q
     */
    @ApiModelProperty(value = "设备故障N、品质故障M、计划停机O、管理停止P、磨损更换Q")
    private String status;
    /**
     * 设备故障N：自主维修N1、生产设备故障N2、公共设备故障N3
     * 品质故障M：品质损失故障M1
     * 计划停机O：换型停机O1、装置停止O2、计划停机O3、试验停机O4
     * 管理停止P：待机P1、清洗P2、等料P3、交接班P4、等指示P5
     * 磨损更换Q：清理油墨Q1、清洗（更换）管道Q2、加液Q3、循环Q4、其他Q5
     */
    @ApiModelProperty(value = "设备故障N：自主维修N1、生产设备故障N2、公共设备故障N3 " +
            "品质故障M：品质损失故障M1 " +
            "计划停机O：换型停机O1、装置停止O2、计划停机O3、试验停机O4 " +
            "管理停止P：待机P1、清洗P2、等料P3、交接班P4、等指示P5 " +
            "磨损更换Q：清理油墨Q1、清洗（更换）管道Q2、加液Q3、循环Q4、其他Q5")
    private String classify;
    /**
     * 原因类型；数据字典
     */
    @ApiModelProperty(value = "原因类型；数据字典")
    private String reasons;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remake;
    /**
     * 方式1主动上报2被动上报
     */
    @ApiModelProperty(value = "方式1主动上报2被动上报，默认为被动上报2")
    private Integer way;
    /***
     *
     */
    @ApiModelProperty(value = "类型：1弹出框0系统记录-无es_id")
    private Integer model;
    /***
     * 间隔时间分钟;开始时间和结束时间间隔
     */
    @ApiModelProperty(value = "间隔时间分钟")
    private Integer delayTime;

    /**
     * 处理状态
     */
    @ApiModelProperty(value = "是否处理停机弹窗事件")
    private Integer handle;

    @ApiModelProperty(value = "处理确认时间")
    private Date handleTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;

}
