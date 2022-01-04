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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 排产班次设定_yb_workbatch_shifts（班次）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_workbatch_shiftset")
@ApiModel(value = "WorkbatchShiftset对象", description = "排产班次设定_yb_workbatch_shifts（班次）")
public class WorkbatchShiftset implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "ws_id")
    private Integer wsId;

    /**
     * 公司为空、部门id、工序id、设备id
     */
    @ApiModelProperty(value = "车间id")
    private Integer dbId;
    /**
     * 分类：1公司2车间部门3工序4设备
     */
    @ApiModelProperty(value = "分类：1公司2车间部门3工序4设备")
    private Integer model;
    /**
     * H类分类信息：
     */
    @ApiModelProperty(value = "班次名称")
    private String ckName;
    /**
     * 区间时间（秒）
     */
    @ApiModelProperty(value = "区间时间（秒）")
    private Integer stayTime;
    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date startDate;
    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date endDate;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Integer usId;
    /**
     * 吃饭时间
     */
    @ApiModelProperty(value = "吃饭时间")
    private Integer mealStay;

    @ApiModelProperty("第一次吃饭时间")
    private String mealOnetime;//第一次吃饭
    @ApiModelProperty("第二次吃饭时间")
    private String mealSecondtime;//第二次吃饭
    @ApiModelProperty("第三次吃饭时间")
    private String mealThirdtime;//第三次吃饭
//    /**
//     * 保养时间
//     */
//    @ApiModelProperty(value = "保养时间")
//    private Integer maintainStay;
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


}
