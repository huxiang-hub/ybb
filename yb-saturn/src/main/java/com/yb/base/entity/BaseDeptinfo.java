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
package com.yb.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门结构_yb_ba_dept实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_base_deptinfo")
@ApiModel(value = "BaseDeptinfo对象", description = "部门结构_yb_ba_dept")
public class BaseDeptinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 当为空的时候表示公司信息
     */
    @ApiModelProperty(value = "当为空的时候表示公司信息")
    private Integer pId;
    /**
     * 可以填写公司信息--公司编号
     */
    @ApiModelProperty(value = "可以填写公司信息--公司编号")
    private String dpName;

    private String dpNum;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    /**
     * 部门全称
     */
    @ApiModelProperty(value = "部门全称")
    private String fullName;
    /**
     * 管理1/生产2
     */
    @ApiModelProperty(value = "管理1/生产2")
    private Integer classify;
    /**
     * 排列顺序：默认100顺序；变小就在前面，变大就在后面
     */
    @ApiModelProperty(value = "排列顺序：默认100顺序；变小就在前面，变大就在后面")
    private Integer sort;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 是否已删除
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除")
    private Integer isDeleted;


}
