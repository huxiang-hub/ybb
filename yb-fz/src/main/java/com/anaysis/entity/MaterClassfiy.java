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
@TableName("yb_mater_classfiy")
@ApiModel(value = "MaterClassfiy对象", description = "物料列表_yb_mater_classfiy")
public class MaterClassfiy implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 物料分类
     */
    @ApiModelProperty(value = "物料分类")
    private String mcType;
    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String mcName;
    /**
     * 物料编号
     */
    @ApiModelProperty(value = "物料编号")
    private String mcNo;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;

}
