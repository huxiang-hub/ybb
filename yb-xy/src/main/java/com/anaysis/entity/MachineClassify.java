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
package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备型号_yb_mach_classify实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_machine_classify")
@ApiModel(value = "MachineClassify对象", description = "设备型号_yb_mach_classify")
public class MachineClassify implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 型号
     */
    @ApiModelProperty(value = "型号")
    private String model;
    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String specs;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;
    /**
     * 出厂标准速度
     */
    @ApiModelProperty(value = "出厂标准速度")
    private Integer speed;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 厂家
     */
    @ApiModelProperty(value = "厂家")
    private String manufacturer;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 设备类型UUID
     */
    @ApiModelProperty(value = "设备类型UUID")
    private String erpId;
}
