package com.yb.system.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

/**
 * @Description: 保存or修改请求类
 * @Author my
 * @Date Created in 2020/10/21
 */
@ApiModel("保存or修改请求类")
@Data
public class SaveAndUpdateRequest {

    @ApiModelProperty("id,修改时传入")
    private Integer id;

    @ApiModelProperty(value = "姓名，必传", required = true)
    @NotBlank(message = "姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "账号，必传(为姓名的全拼)", required = true)
    @NotBlank(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "状态：0停用 1正常", required = true)
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "角色id（多个角色用逗号分隔）")
    private String roleIds;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "生日")
    private Date birthday;

}
