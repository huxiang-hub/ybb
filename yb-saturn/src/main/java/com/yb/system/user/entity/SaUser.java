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
package com.yb.system.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.TenantEntity;

import java.util.Date;

/**
 * 实体类
 *
 * @author Jenny wang
 */
@Data
@TableName("blade_user")
@EqualsAndHashCode(callSuper = true)
public class SaUser extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String name;
    /**
     * 真名
     */
    private String realName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机
     */
    private String phone;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 角色id
     */
    private String roleId;
    /**
            * 部门id
     */
    private String deptId;

    /**
     * 扩充信息（yb_base_staffinfo ）
     */
    @TableField(exist = false)
    private String jobNum;

    /******
     * 印聊号信息
     */
    private Integer chatNo;


    /***************************************************************
     * 用户登录权限token所用对象信息
     */

    /**
     * 客户端id
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String clientId;

    /**
     * 用户id
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private Integer userId;
//    /**
//     * 租户ID
//     */
//    @TableField(exist = false)
//    @ApiModelProperty(hidden = true)
//    private String tenantId;
    /**
     * 昵称
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String userName;
    /**
     * 角色名
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String roleName;


}
