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
import java.time.LocalDateTime;

/**
 * 考勤表_yb_staff_uscheck实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_staff_uscheck")
@ApiModel(value = "StaffUscheck对象", description = "考勤表_yb_staff_uscheck")
public class StaffUscheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id（可选）
     */
    @ApiModelProperty(value = "设备id（可选）")
    private Integer maId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer usId;
    /**
     * 班次
     */
    @ApiModelProperty(value = "班次")
    private String classNum;
    /**
     * 上班1或下班0
     */
    @ApiModelProperty(value = "上班1或下班0")
    private Integer status;
    /**
     * 打卡时间
     */
    @ApiModelProperty(value = "打卡时间")
    private LocalDateTime checkTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createAt;


}
