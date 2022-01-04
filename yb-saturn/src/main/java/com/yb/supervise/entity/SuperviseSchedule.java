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
package com.yb.supervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 订单进度表（进度表-执行）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_supervise_schedule")
@ApiModel(value = "SuperviseSchedule对象", description = "订单进度表（进度表-执行）")
public class SuperviseSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Integer orderId;
    /**
     * 订单批次id
     */
    @ApiModelProperty(value = "订单批次id")
    private Integer batchId;
    /**
     * 订单批次id
     */
    @ApiModelProperty(value = "部件id")
    private Integer ptId;
    /**
     * 订单批次id
     */
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    /**
     * 设备id   对应的某个工序工序
     */
    @ApiModelProperty(value = "设备id   对应的某个工序工序")
    private Integer mId;
    /**
     * 上报成本数量
     */
    @ApiModelProperty(value = "上报成本数量")
    private Integer reportNum;
    /**
     * 废品数量
     */
    @ApiModelProperty(value = "废品数量")
    private Integer wasteNum;
    /**
     * 实时统计数量
     */
    @ApiModelProperty(value = "实时统计数量")
    private Integer nowNum;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
    /**
     * 合计(百分比)
     */
    @ApiModelProperty(value = "合计(百分比)")
    private BigDecimal total;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;


}
