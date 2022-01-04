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

/**
 * 印联盒（本租户的盒子），由总表分发出去实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_machine_mixbox")
@ApiModel(value = "MachineMixbox对象", description = "印联盒（本租户的盒子），由总表分发出去")
public class MachineMixbox implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 盒子编号
     */
    @ApiModelProperty(value = "盒子编号")
    private String uuid;
    /**
     * 硬件型号id
     */
    @ApiModelProperty(value = "硬件型号id")
    private Integer hdId;
    /**
     * 生产批次
     */
    @ApiModelProperty(value = "生产批次")
    private String batch;
    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    private String remark;
    /**
     * 是否激活
     */
    @ApiModelProperty(value = "是否激活")
    private Integer active;
    /**
     * 仓库状态
     */
    @ApiModelProperty(value = "仓库状态")
    private Integer depository;
    /**
     * mac地址
     */
    @ApiModelProperty(value = "mac地址")
    private String mac;
    /**
     * 创建时间 例如：2020-01-08
     */
    @ApiModelProperty(value = "创建时间 例如：2020-01-08")
    private String createAt;


}
