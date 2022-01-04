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
package com.yb.workbatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 排产班次执行表_yb_workbatch_shiftinfo（日志表）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_workbatch_shiftinfo")
@ApiModel(value = "WorkbatchShiftinfo对象", description = "排产班次执行表_yb_workbatch_shiftinfo（日志表）")
public class WorkbatchShiftinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 班次id
     */
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    /**
     * （公司为空、部门id、工序id、设备id）
     */
    @ApiModelProperty(value = "（公司为空、部门id、工序id、设备id）")
    private Integer dbId;
    /**
     * 分类：1公司2车间部门3工序4设备
     */
    private Integer model;
    /**
     * 是否考勤1出勤0缺勤
     */
    private Integer isWork;
    /**
     * 考勤计划日期
     */
    private Date checkDate;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * H类分类信息：/白班1/晚班2/夜班3
     */
    @ApiModelProperty(value = "H类分类信息：/白班1/晚班2/夜班3")
    private String ckName;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 区间时间（秒）
     */
    private Integer stayTime;
    /**
     * 考勤人
     */
    private Integer usId;
    /**
     * 考勤状态：1正常2迟到3早退4异常5请假
     */
    private Integer ckStatus;
    /**
     * 创建时间
     */
    private Date createAt;

}
