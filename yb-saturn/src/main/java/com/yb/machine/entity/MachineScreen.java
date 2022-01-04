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

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备关联屏幕（主系统同步数据）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_machine_screen")
@ApiModel(value = "MachineScreen对象", description = "设备关联屏幕（主系统同步数据）")
public class MachineScreen implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer maId;
    private String uuid;
    /**
     * 屏幕型号
     */
    @ApiModelProperty(value = "屏幕型号")
    private String scType;
    /**
     * 屏幕品牌
     */
    @ApiModelProperty(value = "屏幕品牌")
    private String scBrand;
    /**
     * 硬件类型id
     */
    @ApiModelProperty(value = "硬件类型id")
    private Integer hdId;


}
