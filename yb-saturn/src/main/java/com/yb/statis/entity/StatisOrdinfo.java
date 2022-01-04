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
import java.time.LocalDateTime;
import java.util.Date;

/**
 * OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_statis_ordinfo")
@ApiModel(value = "StatisOrdinfo对象", description = "OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表")
public class StatisOrdinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Integer odId;

    /**
     * 排查Id
     */
    @ApiModelProperty(value = "排查Id")
    private Integer sdId;
    /**
     * oee统计时间2020-04-15
     */
    @ApiModelProperty(value = "oee统计时间2020-04-15")
    private String oeDate;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String odNum;
    /**
     * 批次id
     */
    @ApiModelProperty(value = "批次id")
    private Integer wbId;
    /**
     * 批次编号
     */
    @ApiModelProperty(value = "批次编号")
    private String wbNum;
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
     * 工序id
     */
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String prName;
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
     * 班次执行id
     */
    @ApiModelProperty(value = "班次执行id")
    private Integer sfId;
    /**
     * 班次名称
     */
    @ApiModelProperty(value = "班次名称")
    private String sfName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 实际出勤人数
     */
    @ApiModelProperty(value = "实际出勤人数")
    private  Double dutyNum;
    /**
     * 排产开始时间
     */
    private Date ordStart;
    /**
     * 排产结束时间
     */
    private Date ordEnd;
    /**
     * 执行单Id
     */
    private Integer exId;

}
