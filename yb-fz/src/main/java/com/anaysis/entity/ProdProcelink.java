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

/**
 * 产品对应工序关联表实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_prod_procelink")
@ApiModel(value = "ProdProcelink对象", description = "产品对应工序关联表")
public class ProdProcelink implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 部件id
     */
    @ApiModelProperty(value = "部件id")
    private Integer ptId;
    /**
     * 工序id
     */
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    /**
     * 工序要求
     */
    @ApiModelProperty(value = "工序要求")
    private String remarks;
    /**
     * 工序参数
     */
    @ApiModelProperty(value = "工序参数")
    private String prParam;
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
    /**
     * 工序损耗率
     */
    @ApiModelProperty(value = "工序损耗率")
    private Double wasteRate;
    /**
     * 难易程度
     */
    @ApiModelProperty(value = "难易程度")
    private Double diffLevel;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortNum;
    /**
     * 关键点1起始工序2过程工序3合并工序4结束工序
     */
    @ApiModelProperty(value = "关键点1起始工序2过程工序3合并工序4结束工序")
    private Integer point;
    /**
     * 是否启用1启用0停用
     */
    @ApiModelProperty(value = "是否启用1启用0停用")
    private Integer isUsed;


}
