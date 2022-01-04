package com.yb.system.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yb.system.role.entity.Role;
import com.yb.system.role.mapper.SaRoleMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/10/21 11:06
 */
@ApiModel("系统管理用户分页VO")
@Data
public class SysUserPageVO {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty("性别:(1:男，0女)")
    private Integer sex;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("角色相关信息")
    private List<Role> role;

    @ApiModelProperty("角色id集合")
    @JsonIgnore
    private String roleIds;

    @ApiModelProperty("印聊号")
    private Integer chatNo;

    @ApiModelProperty("状态:(0禁用，1启用)")
    private Integer status;

    @ApiModelProperty("微信id")
    private String unionId;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("部门id  ")
    private String deptId;
}
