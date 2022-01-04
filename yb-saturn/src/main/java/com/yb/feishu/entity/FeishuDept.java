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
@TableName(value = "yb_feishu_dept")
@ApiModel(value = "飞书部门对象")
public class FeishuDept implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "飞书唯一id")
    private String fsId;
    @ApiModelProperty(value = "部门 openID")
    private String openDepartmentId;
    @ApiModelProperty(value = "部门名称")
    private String name;
    @ApiModelProperty(value = "部门群ID")
    private String chatId;
    @ApiModelProperty(value = "部门成员数量")
    private String memberCount;
    @ApiModelProperty(value = "父部门自定义 ID")
    private String parentId;
    @ApiModelProperty(value = "父部门 openID")
    private String parentOpenDepartmentId;
    @ApiModelProperty(value = "部门状态，0 无效，1 有效")
    private String status;
    @ApiModelProperty(value = "部门负责人 employee_id，申请了获取用户 user_id权限的应用返回该字段")
    private String leaderEmployeeId;
    @ApiModelProperty(value = "部门负责人 open_id")
    private String leaderOpenId;
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
}
