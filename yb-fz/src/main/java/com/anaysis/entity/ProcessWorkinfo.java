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
 * 工序表--租户的工序内容（可以依据行业模版同步）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_process_workinfo")
@ApiModel(value = "ProcessWorkinfo对象", description = "工序表--租户的工序内容（可以依据行业模版同步）")
public class ProcessWorkinfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.INPUT)
    private Integer id;
    /**
     * 工序名称(比对同步数据关键字)
     */
    @ApiModelProperty(value = "工序名称(比对同步数据关键字)")
    private String prName;
    /**
     * 工序编号
     */
    @ApiModelProperty(value = "工序编号")
    private String prNo;
    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer sort;
    /**
     * 状态 1启用0停用
     */
    @ApiModelProperty(value = "状态 1启用0停用")
    private Integer status;
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
