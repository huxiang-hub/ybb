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
package org.springblade.saturn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备当前状态表boxinfo-视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_supervise_execute")
@ApiModel(value = "SuperviseBoxinfo对象", description = "设备当前状态表boxinfo-视图")
public class SuperviseExecute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 盒子唯一标识
     */

    @ApiModelProperty(value = "盒子唯一标识uuid")

    private String uuid;
    /**
     * 地址
     */
    @ApiModelProperty(value = "设备Id")
    private Integer maId;
    /**
     * 状态1运行2停机3故障4离线
     */
    @ApiModelProperty(value = "批次ID")
    private Integer wbId;

    @ApiModelProperty(value = "批次ID")
    private Integer sdId;
    /**
     * 计数器数字
     */
    @ApiModelProperty(value = "计数器数字")
    private Integer readyNum;
    /**
     * 当天数量
     */
    @ApiModelProperty(value = "当前正式开始计数")
    private Integer currNum;
    /**
     * 当天数量
     */
    @ApiModelProperty(value = "当前正式开始计数")
    private Integer startNum;
    /**
     * 当天数量
     */
    @ApiModelProperty(value = "当前正式开始计数")
    private Integer endNum;
    /**
     * 执行状态：人事A 生产准备B 正式生产C 结束生产D
     */
    @ApiModelProperty(value = "执行状态：人事A 生产准备B 正式生产C 结束生产D")
    private String exeStatus;
    /**
     * 执行状态：事件：上班A1 下班A2
     * 接单B1保养B2换模B3
     * 正式生产C1停机C2质检C3
     * 结束生产D1生产上报D2
     */
    @ApiModelProperty(value = "事件：上班A1 下班A2 \n" +
            "接单B1保养B2换模B3\n" +
            "正式生产C1停机C2质检C3\n" +
            "结束生产D1生产上报D2")
    private String event;
    /**
     * 机台操作人员
     */
    @ApiModelProperty(value = "机台操作人员")
    private Integer operator;
    /**
     * 当前时速
     */
    @ApiModelProperty(value = "状态开始时间")
    private Date startTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

    /**
     * 当前订单生产的人员
     */
    @ApiModelProperty(value = "当前订单生产的人员")
    private String usIds;

    private Integer esId;

}
