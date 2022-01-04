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
package org.springblade.saturn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备_yb_mach_mainfo实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_machine_mainfo")
@ApiModel(value = "MachineMainfo对象", description = "设备_yb_mach_mainfo")
public class MachineMainfo implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备型号id
     */
    @ApiModelProperty(value = "设备型号id")
    private Integer mtId;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String mno;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String name;
    /**
     * 车间Id
     */
    @ApiModelProperty(value = "车间Id")
    private Integer dpId;
    /**
     * 工序分类---指定主要工序类型
     */
    @ApiModelProperty(value = "指定主要工序类型")
        private Integer proId;
    /**
     * 设备id别名
     */
    @TableField(exist = false)
    private Integer maId;
    /**
     * 是否为工序接单操作0按设备接单1是按工序接单；
     */

    @ApiModelProperty(value = "是否为工序接单操作0按设备接单1是按工序接单；")
    private Integer isRecepro;

}
