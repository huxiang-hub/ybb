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
package com.vim.chatapi.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 人员表_yb_base_staffinfo实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_base_staffinfo")
@ApiModel(value = "BaseStaffinfo对象", description = "人员表_yb_base_staffinfo")
public class BaseStaffinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 工号（系统生成-也可以手工修改）
     */
    @ApiModelProperty(value = "工号（系统生成-也可以手工修改）")
    private String jobnum;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String phone;
    /**
     * 部门名称ID--车间
     */
    @ApiModelProperty(value = "部门名称ID--车间")
    private Integer dpId;
    /**
     * 类型：管理1/生产2/
     */
    @ApiModelProperty(value = "类型：管理1/生产2/")
    private Integer mold;
    /**
     * 岗位（角色）1.机长2.班长3.车间主管4排产员
     */
    @ApiModelProperty(value = "岗位（角色）1.机长2.班长3.车间主管4排产员")
    private Integer jobs;
    /**
     * 劳动类型：1正式员工2临时员工3试用期4实习员工
     */
    @ApiModelProperty(value = "劳动类型：1正式员工2临时员工3试用期4实习员工")
    private Integer laborer;
    /**
     * 工龄（入职时间）
     */
    @ApiModelProperty(value = "工龄（入职时间）")
    private Date hireTime;
    /**
     * 生产型：工种|竖线分隔
     */
    @ApiModelProperty(value = "生产型：工种|竖线分隔")
    private String processes;
    /**
     * 是否启用1、停用0
     */
    @ApiModelProperty(value = "是否启用1、停用0")
    private Integer isUsed;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;


    @ApiModelProperty(value = "用户ID")
    private Integer userId;


}
