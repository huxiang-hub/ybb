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
import java.time.LocalDateTime;

/**
 * 预警定义_yb_supervise_warning实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_supervise_warning")
@ApiModel(value = "SuperviseWarning对象", description = "预警定义_yb_supervise_warning")
public class SuperviseWarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 关联id_不同业务不同关联表id唯一值
     */
    @ApiModelProperty(value = "关联id_不同业务不同关联表id唯一值")
    private Integer rtId;
    /**
     * 不同类型信息：业务表不同1、计划预警
     */
    @ApiModelProperty(value = "不同类型信息：业务表不同1、计划预警")
    private Integer wrType;
    /**
     * 预警级别1预警2警告3超期
     */
    @ApiModelProperty(value = "预警级别1预警2警告3超期")
    private Integer wrLeve;
    /**
     * 预警时间
     */
    @ApiModelProperty(value = "预警时间")
    private LocalDateTime wrTime;
    /**
     * 提前多久单位秒
     */
    @ApiModelProperty(value = "提前多久单位秒")
    private Integer early;
    /**
     * 人员id
     */
    @ApiModelProperty(value = "人员id")
    private Integer usId;
    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Integer dpId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createAt;


}
