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
 * 产品信息（product）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_prod_pdinfo")
@ApiModel(value = "ProdPdinfo对象", description = "产品信息（product）")
public class ProdPdinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号")
    private String pdNo;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
    /**
     * 是否启用1是0否
     */
    @ApiModelProperty(value = "是否启用1是0否")
    private Integer isUsed;
    /**
     * 工序展示图
     */
    @ApiModelProperty(value = "工序展示图")
    private String procePic;
    /**
     * 产品分类
     */
    @ApiModelProperty(value = "产品分类")
    private Integer pcId;
    /**
     * 模型对象的记录
     */
    @ApiModelProperty(value = "模型对象的记录")
    private String modelJson;
    /**
     * ERP的UUID
     */
    @ApiModelProperty(value = "模型对象的记录")
    private String erpId;
    @ApiModelProperty(value="产品尺寸")
    private String pdSize;
}
