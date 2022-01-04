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
package com.sso.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统数据字典_yb_base_dict实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_base_dict")
@ApiModel(value = "BaseDict对象", description = "系统数据字典_yb_base_dict")
public class BaseDict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 父级编号
     */
    @ApiModelProperty(value = "父级编号")
    private Integer pId;
    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    private String dtName;
    /**
     * 字典执行
     */
    @ApiModelProperty(value = "字典执行")
    private String dtValue;
    /**
     * 字典分类
     */
    @ApiModelProperty(value = "字典分类")
    private String dtType;
    /**
     * 字段说明
     */
    @ApiModelProperty(value = "字段说明")
    private String remark;
    /**
     * 排序（升序）
     */
    @ApiModelProperty(value = "排序（升序）")
    private Integer sort;
    /**
     * 是否删除1是0否
     */
    @ApiModelProperty(value = "是否删除1是0否")
    private Integer isdel;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;


}
