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
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 人员排班表_yb_workbatch_staffwk实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_workbatch_staffwk")
@ApiModel(value = "WorkbatchStaffwk对象", description = "人员排班表_yb_workbatch_staffwk")
public class WorkbatchStaffwk implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 人员id
     */
    @ApiModelProperty(value = "人员id")
    private Integer usId;
    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Integer dpId;
    /**
     * 考勤日期
     */
    @ApiModelProperty(value = "考勤日期")
    private String wkDate;
    /**
     * 班次名称
     */
    @ApiModelProperty(value = "班次名称")
    private String shift;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalTime startAt;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalTime endAt;
    /**
     * 考勤状态：1未上班2已上班
     */
    @ApiModelProperty(value = "考勤状态：1未上班2已上班")
    private Integer status;
    /**
     * 类型：1迟到2早退3事假4病假5产假
     */
    @ApiModelProperty(value = "类型：1迟到2早退3事假4病假5产假")
    private Integer classify;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createAt;


}
