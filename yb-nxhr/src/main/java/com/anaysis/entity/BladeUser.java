package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lzb
 * @date 2020-11-25
 */
@Data
@TableName("blade_user")
@Accessors(chain = true)
@ApiModel(value = "用户表BladeUser实体")
public class BladeUser implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "租户ID")
    @TableField("tenant_id")
    private String tenantId;


    @ApiModelProperty(value = "账号")
    @TableField("account")
    private String account;


    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;


    @ApiModelProperty(value = "昵称")
    @TableField("name")
    private String name;


    @ApiModelProperty(value = "真名")
    @TableField("real_name")
    private String realName;


    @ApiModelProperty(value = "头像")
    @TableField("avatar")
    private String avatar;


    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;


    @ApiModelProperty(value = "手机")
    @TableField("phone")
    private String phone;


    @ApiModelProperty(value = "生日")
    @TableField("birthday")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;


    @ApiModelProperty(value = "性别")
    @TableField("sex")
    private Integer sex;


    @ApiModelProperty(value = "角色id")
    @TableField("role_id")
    private String roleId;


    @ApiModelProperty(value = "部门id")
    @TableField("dept_id")
    private String deptId;


    @ApiModelProperty(value = "创建人")
    @TableField("create_user")
    private Integer createUser;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "修改人")
    @TableField("update_user")
    private Integer updateUser;


    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Integer status;


    @ApiModelProperty(value = "是否已删除")
    @TableField("is_deleted")
    private Integer isDeleted;


    @ApiModelProperty(value = "")
    @TableField("chat_no")
    private Integer chatNo;


    @ApiModelProperty(value = "现在住址")
    @TableField("curraddr")
    private String curraddr;


    @ApiModelProperty(value = "用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的")
    @TableField("unionid")
    private String unionid;


    @ApiModelProperty(value = "erp的uuid")
    @TableField("erp_id")
    private String erpId;


    @ApiModelProperty(value = "erp的部门uuid")
    @TableField("dp_erp")
    private String dpErp;

    @ApiModelProperty(value = "班组名")
    @TableField(exist = false)
    private String shift;

}
