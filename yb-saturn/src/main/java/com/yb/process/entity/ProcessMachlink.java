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
package com.yb.process.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备工序关联表yb_process_machlink实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_process_machlink")
@ApiModel(value = "ProcessMachlink对象", description = "设备工序关联表yb_process_machlink")
public class ProcessMachlink implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 工序分类id
     */
    @ApiModelProperty(value = "工序分类id")
    private Integer pyId;
    /**
     * 工序id
     */
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    /**
     * 标准时速
     */
    @ApiModelProperty(value = "标准时速")
    private Integer speed;
    /**
     * 标准换膜时间
     */
    @ApiModelProperty(value = "标准换膜时间")
    private Integer prepareTime;
    /**
     * 持续运行规则时间
     */
    @ApiModelProperty(value = "持续运行规则时间")
    private Integer keepRun;

}
