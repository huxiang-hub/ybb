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
 * 物料列表_yb_mater_matinfo实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_mater_mtinfo")
@ApiModel(value = "MaterMtinfo对象", description = "物料列表_yb_mater_matinfo")
public class MaterMtinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String mlName;
    /**
     * 物料编号（同步字段）
     */
    @ApiModelProperty(value = "物料编号（同步字段）")
    private String mlNo;
    /**
     * 物料分类（同步字段）
     */
    @ApiModelProperty(value = "物料分类（同步字段）")
    private Integer mcId;
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
     * 物料类型1、大宗原料2、生产辅料
     */
    @ApiModelProperty(value = "物料类型1、大宗原料2、生产辅料")
    private Integer mold;
    /**
     * 尺寸
     */
    @ApiModelProperty(value = "尺寸")
    private String specification;
    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String size;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;
    /**
     * 厂家
     */
    @ApiModelProperty(value = "厂家")
    private String manufactor;
    /**
     * 本地数据1是0否（0非本租户信息，为行业同步数据）
     */
    @ApiModelProperty(value = "本地数据1是0否（0非本租户信息，为行业同步数据）")
    private Integer islocal;
    /**
     * 是否删除1是0否（若为行业同步数据表示非物理删除，做删除标志，表示不能更新同步数据）
     */
    @ApiModelProperty(value = "是否删除1是0否（若为行业同步数据表示非物理删除，做删除标志，表示不能更新同步数据）")
    private Integer isdel;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间")
    private Date updateAt;


}
