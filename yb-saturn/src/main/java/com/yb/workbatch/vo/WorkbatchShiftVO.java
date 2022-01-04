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
package com.yb.workbatch.vo;

import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftinfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 排产班次执行表_yb_workbatch_shiftinfo（日志表）视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkbatchShiftinfoVO对象", description = "")
public class WorkbatchShiftVO extends WorkbatchShift {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("工单总计划数（含放数）")
    private Integer planNum;
    @ApiModelProperty("差数")
    private Integer difficutlNum;
    @ApiModelProperty("班次开始时间")
    private Date classStartTime;
    @ApiModelProperty("班次结束时间")
    private Date classEndTime;
    @ApiModelProperty("休息停留时长（分钟）")
    private Integer mealStay;
    @ApiModelProperty("产品名称")
    private String pdName;
    @ApiModelProperty("工单编号")
    private String wbNo;
    @ApiModelProperty("工单ID")
    private Integer wbId;
}
