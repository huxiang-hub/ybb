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
import java.util.Date;

/**
 * 实体类
 *
 * @author Blade
 * @since 2020-04-17
 */
@Data
@TableName("yb_statis_machoee")
@ApiModel(value = "StatisMachoee对象", description = "StatisMachoee对象")
public class StatisMachoee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * oee统计时间2020-04-15
     */
    @ApiModelProperty(value = "oee统计时间2020-04-15")
    private String oeDate;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String maName;
    /**
     * 人员机长id
     */
    @ApiModelProperty(value = "人员机长id")
    private Integer usId;
    /**
     * 人员姓名
     */
    @ApiModelProperty(value = "人员姓名")
    private String usName;
    /**
     * 1 设备排产2设备定时
     */
    private int oeType;
    /**
     * 排产ID
     */
    private Integer sdId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 执行ID
     */
    private Integer exId;

    private Integer wsId; //设定班次id信息
    /**
     * 班次开始时间
     */
    private Date sfStartTime;

    /**
     * 班次结束时间
     */
    private Date sfEndTime;
    /**
     * 班次结束时间
     */
    private String sfName;

}
