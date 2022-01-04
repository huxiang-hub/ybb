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
 * 产品物料关系（materiel）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_mater_prodlink")
@ApiModel(value = "MaterProdlink对象", description = "产品物料关系（materiel）")
public class MaterProdlink implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 部件id
     */
    @ApiModelProperty(value = "部件id")
    private Integer pdId;
    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id")
    private Integer mlId;
    /**
     * 物料数量
     */
    @ApiModelProperty(value = "物料数量")
    private Integer mtNum;
    /**
     * 产品数量
     */
    @ApiModelProperty(value = "产品数量")
    private Integer pdNum;
    /**
     * 材质
     */
    @ApiModelProperty(value = "材质")
    private String material;
    /**
     * 型号
     */
    @ApiModelProperty(value = "型号")
    private String model;
    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String specification;
    /**
     * 尺寸
     */
    @ApiModelProperty(value = "尺寸")
    private String size;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;


}
