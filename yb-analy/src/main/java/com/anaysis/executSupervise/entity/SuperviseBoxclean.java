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
package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 设备清零日志表实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_supervise_boxclean")
@ApiModel(value = "SuperviseBoxclean对象", description = "设备清零日志表")
public class SuperviseBoxclean implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 盒子唯一标识
     */
    @ApiModelProperty(value = "盒子唯一标识")
    private String uuid;
    /**
     * 状态1运行2停机3故障4离线
     */
    @ApiModelProperty(value = "状态1运行2停机3故障4离线")
    private String status;
    /**
     * 计数器数字
     */
    @ApiModelProperty(value = "计数器数字")
    private Integer number;
    /**
     * 当天数量
     */
    @ApiModelProperty(value = "当天数量")
    private Integer numberOfDay;
    /**
     * 当前时速
     */
    @ApiModelProperty(value = "当前时速")
    private Double dspeed;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
    /**
     * 操作日期
     */
    @ApiModelProperty(value = "操作日期")
    private Date opDate;
    /**
     * 清零时间
     */
    @ApiModelProperty(value = "清零时间")
    private Date cleanTime;
}
