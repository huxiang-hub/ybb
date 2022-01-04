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
package com.yb.panel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 机台自定义_yb_panel_customize实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_panel_customize")
@ApiModel(value = "PanelCustomize对象", description = "机台自定义_yb_panel_customize")
public class PanelCustomize implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private Integer pId;
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private int maId;
    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private Integer muId;
    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer sort;
    /**
     * 状态 1启用0停用
     */
    @ApiModelProperty(value = "状态 0停用1启用")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;


}
