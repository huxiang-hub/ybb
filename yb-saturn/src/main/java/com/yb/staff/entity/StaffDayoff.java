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
package com.yb.staff.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 人事请假管理_yb_staff_dayoff实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_staff_dayoff")
@ApiModel(value = "StaffDayoff对象", description = "人事请假管理_yb_staff_dayoff")
public class StaffDayoff implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer usId;
    /**
     * 请假类型：1事假2病假3倒休4产假5其他
     */
    @ApiModelProperty(value = "请假类型：1事假2病假3倒休4产假5其他")
    private LocalDate model;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDate startDate;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDate endDate;
    /**
     * 期间时长3.5天（三天半）
     */
    @ApiModelProperty(value = "期间时长3.5天（三天半）")
    private BigDecimal duration;
    /**
     * 状态1起草2提交(待审批)3同意4不同意
     */
    @ApiModelProperty(value = "状态1起草2提交(待审批)3同意4不同意")
    private Integer status;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private LocalDateTime createAt;


}
