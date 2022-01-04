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
package com.yb.prod.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * I.1	 产品部件表_yb_prod_partsinfo
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_prod_partsinfo")
@ApiModel(value = "ProdPartsinfo对象", description = " 产品部件表（product）")
public class ProdPartsinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 父节点ID
     */
    @ApiModelProperty(value = "父节点ID")
    private Integer pid;
    /**
     * 产品ID
     */
    @ApiModelProperty(value = " 产品ID")
    private Integer pdId;
    /**
     * 部件类型
     */
    @ApiModelProperty(value = "部件类型")
    private Integer ptType;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件名称")
    private String ptName;
    /**
     * 部件编号
     */
    @ApiModelProperty(value = "部件编号")
    private String ptNo;
    /**
     * 1原料2部件3原料+部件
     */
    @ApiModelProperty(value = "部件组成方式")
    private Integer ptClassify;
    /**
     * 部件编号
     */
    @ApiModelProperty(value = "部件编号")
    private String ptIds;
    /**
     * 1产品,2模板
     */
    @ApiModelProperty(value = "1产品,2模板")
    private Integer pdType;
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
