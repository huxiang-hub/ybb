package com.yb.feishu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "yb_feishu_userinfo")
@ApiModel(value = "飞书用户对象 ")
public class FeishuUserinfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "姓名 用户名")
    private String name;
    @ApiModelProperty(value = "用户名拼音")
    private String namePy;
    @ApiModelProperty(value = "英文名")
    private String enName;
    @ApiModelProperty(value = "用户的 employee_id，申请了\"获取用户 user_id\"权限的应用返回该字段")
    private String employeeId;
    @ApiModelProperty(value = "工号")
    private String employeeNo;
    @ApiModelProperty(value = "用户的 open_id")
    private String openId;
    @ApiModelProperty(value = "用户统一ID，申请了\"获取用户统一ID\"权限后返回")
    private String unionId;
    @ApiModelProperty(value = "状态：bit0：1冻结0未冻结bit1：1离职0在职bit2：1未激活0已激活★'")
    private String status;
    @ApiModelProperty(value = "员工类型：1:正式员工；2:实习生；3:外包；4:劳务；5:顾问★")
    private String employeeType;
    @ApiModelProperty(value = "头像相片路径")
    private String avatar;
    @ApiModelProperty(value = "头像小图")
    private String avatar_72;
    @ApiModelProperty(value = "性别1男2女")
    private String gender;
    @ApiModelProperty(value = "邮件")
    private String email;
    @ApiModelProperty(value = "手机")
    private String mobile;
    @ApiModelProperty(value = "个性签名")
    private String description;
    @ApiModelProperty(value = "国家")
    private String country;
    @ApiModelProperty(value = "城市")
    private String city;
    @ApiModelProperty(value = "工位")
    private String workStation;
    @ApiModelProperty(value = "是否是企业超级管理员 true")
    private String isTenantManager;
    @ApiModelProperty(value = "入职时间")
    private String joinTime;
    @ApiModelProperty(value = "更新时间")
    private String updateTime;
    @ApiModelProperty(value = "用户直接领导的 employee_id，企业自建应用返回，应用商店应用申请了 employee_id 权限时才返回")
    private String leaderEmployeeId;
    @ApiModelProperty(value = "用户直接领导的 open_id")
    private String leaderOpenId;
    @ApiModelProperty(value = "用户直接领导的 union_id,申请了\"获取用户统一ID\"权限后返回")
    private String leaderUnionId;
    @ApiModelProperty(value = "用户所在部门自定义 ID列表，用户可能同时存在于多个部门")
    private String departments;
    @ApiModelProperty(value = "用户所在部门 openID 列表，用户可能同时存在于多个部门")
    private String openDepartments;
    @ApiModelProperty(value = "用户的自定义属性信息 该字段返回的每一个属性包括自定义属性 ID 和自定义属性值。\n企业开放了自定义用户属性且为该用户设置了自定义属性的值，才会返回该字段")
    private String customAttrs;
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;


}
