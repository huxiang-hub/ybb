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
package com.yb.process.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 工序参数关联表_yb_proc_paramlink实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_process_paramlink")
@ApiModel(value = "ProcessParamlink对象", description = "工序参数关联表_yb_proc_paramlink")
public class ProcessParamlink implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 工序id
     */
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    /**
     * 参数名称；页面对象name
     */
    @ApiModelProperty(value = "参数名称；页面对象name")
    private String prname;
    /**
     * 标题显示名称
     */
    @ApiModelProperty(value = "标题显示名称")
    private String title;
    /**
     * 数据类型：字符串/数值
     */
    @ApiModelProperty(value = "数据类型：字符串/数值")
    private String dataType;
    /**
     * 编辑框类型：下拉框/下拉框
     */
    @ApiModelProperty(value = "编辑框类型：下拉框/下拉框")
    private String editType;
    /**
     * 枚举类型：字符串
     */
    @ApiModelProperty(value = "枚举类型：字符串")
    private String enumType;
    /**
     * 枚举数据
     */
    @ApiModelProperty(value = "枚举数据")
    private String enumData;
    /**
     * 父节点id；上级节点
     */
    @ApiModelProperty(value = "父节点id；上级节点")
    private Integer pid;
    /**
     * 值信息
     */
    @ApiModelProperty(value = "值信息")
    private String dvalue;
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
