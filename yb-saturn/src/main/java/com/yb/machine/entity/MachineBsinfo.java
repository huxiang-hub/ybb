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
package com.yb.machine.entity;

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
 * 设备扩展信息_yb_machine_bsinfo实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_machine_bsinfo")
@ApiModel(value = "MachineBsinfo对象", description = "设备扩展信息_yb_machine_bsinfo")
public class MachineBsinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer maId;
    /**
     * 设备出厂编号
     */
    @ApiModelProperty(value = "设备出厂编号")
    private String serialno;
    /**
     * 出厂日期
     */
    @ApiModelProperty(value = "出厂日期")
    private String outDate;
    /**
     * 设备重量（单位V）
     */
    @ApiModelProperty(value = "设备重量（单位V）")
    private Integer weight;
    /**
     * 外形尺寸(长*宽*高单位cm)
     */
    @ApiModelProperty(value = "外形尺寸(长*宽*高单位cm)")
    private String size;
    /**
     * 设备功率（单位KW）
     */
    @ApiModelProperty(value = "设备功率（单位KW）")
    private BigDecimal power;
    /**
     * 设备电压（单位V）
     */
    @ApiModelProperty(value = "设备电压（单位V）")
    private Integer voltage;
    /**
     * 厂家电话
     */
    @ApiModelProperty(value = "厂家电话")
    private String phone;
    /**
     * 联系方式（李经理-1380000）
     */
    @ApiModelProperty(value = "联系方式（李经理-1380000）")
    private String contact;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;


}
