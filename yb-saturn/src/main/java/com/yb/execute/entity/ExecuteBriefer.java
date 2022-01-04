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
 * 生产执行上报信息_yb_execute_briefer实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_execute_briefer")
@ApiModel(value = "ExecuteBriefer对象", description = "生产执行上报信息_yb_execute_briefer")
public class ExecuteBriefer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 执行表状态id
     */
    @ApiModelProperty(value = "执行表状态id")
    private Integer esId;

    private Integer wfId;

    private Integer sdId;
    /**
     * 生产状态：1已完成0未完成
     */
    @ApiModelProperty(value = "生产状态：1已完成0未完成")
    private Integer status;
    /**
     * 设备采集数
     */
    @ApiModelProperty(value = "设备采集数")
    private Integer boxNum;
    /**
     * 上报作业数量
     */
    @ApiModelProperty(value = "上报作业数量")
    private Integer productNum;
    /**
     * 上报正品数量
     */
    @ApiModelProperty(value = "上报正品数量")
    private Integer countNum;
    /**
     * 废品数量
     */
    @ApiModelProperty(value = "废品数量")
    private Integer wasteNum;
    /**
     * 原上报作业数量
     */
    @ApiModelProperty(value = "原上报作业数量")
    private Integer alterProductNum;
    /**
     * 原上报正品数量
     */
    @ApiModelProperty(value = "原上报正品数量")
    private Integer alterCountNum;

    /**
     * 原废品数量
     */
    @ApiModelProperty(value = "原废品数量")
    private Integer alterWasteNum;

    /**
     * 准备数量
     */
    private Integer readyNum;
    /**
     * 停机废品
     */
    private Integer stopWaste;

    /**
     * 上工序废品数量
     */
    @ApiModelProperty(value = "废品数量")
    private Integer upwasteNum;
    /**
     * 上上工序废品数量
     */
    @ApiModelProperty(value = "废品数量")
    private Integer uppwasteNum;

    /**
     * 开始时间（设备工单开始时间）
     */
    private Date startTime;
    /**
     * 结束时间（结束时间）
     */
    private Date endTime;
    /**
     * 是否处理上报事件0未上报1已上报
     */
    private Integer handle;
    /**
     * 处理上报时间
     */
    private Date handleTime;

    private Integer handleUsid;   //处理上报人员

    private String usIds;   //参与人员；前后用竖线分隔

    private Date createAt;

    /**
     * 执行单Id
     */
    private Integer exId;
    /**
     * JSJ 独有
     */
    private Integer timeSet;
    /**
     * 审核状态 0 未审核 1 通过 2未通过
     */
    private Integer exStatus;

}
