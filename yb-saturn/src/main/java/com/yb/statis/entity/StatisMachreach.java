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
import java.math.BigDecimal;
import java.util.Date;

/**
 * OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_statis_machreach")
@ApiModel(value = "StatisMachreach", description = "yb_statis_machreach")
public class StatisMachreach implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "上报ID")
    private Integer ebId;

    @ApiModelProperty(value = "oee统计时间2020-04-15")
    private String targetDate;

    @ApiModelProperty(value = "设备ID")
    private Integer maId;

    @ApiModelProperty(value = "设备名称")
    private String maName;

    @ApiModelProperty(value = "操作人")
    private Integer usId;

    @ApiModelProperty(value = "机长姓名")
    private String usName;

    @ApiModelProperty(value = "排产单ID")
    private Integer sdId;

    @ApiModelProperty(value = "平均达成率")
    private BigDecimal reachRate;

    @ApiModelProperty(value = "计划数量")
    private Integer planNum;

    @ApiModelProperty(value = "盒子数量")
    private Integer boxNum;

    @ApiModelProperty(value = "良品数")
    private Integer countNum;

    @ApiModelProperty(value = "废品数")
    private Integer wasteNum;

    @ApiModelProperty(value = "良品率")
    private BigDecimal yieldRate;

    @ApiModelProperty(value = "结束时间")
    private Date createAt;

    @ApiModelProperty(value = "上报时间")
    private Date updateAt;

    @ApiModelProperty(value = "已完成几个工单")
    private Integer wfFinishnum;

    @ApiModelProperty(value = "班次排产总个数")
    private Integer wfNum;

    @ApiModelProperty(value = "班次排产总计数")
    private Integer wfTotalnum;

    @ApiModelProperty(value = "班次id")
    private Integer wsId;

    @ApiModelProperty(value = "排产班次id")
    private Integer wfId;

}
