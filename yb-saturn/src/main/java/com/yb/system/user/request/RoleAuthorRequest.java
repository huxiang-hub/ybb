package com.yb.system.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 角色授权请求类
 * @Author my
 * @Date Created in 2020/10/21
 */
@ApiModel("角色授权请求类")
@Data
public class RoleAuthorRequest {

    @ApiModelProperty(value = "用户id,必传", required = true)
    @NotNull(message = "用户id 不能为空")
    private List<Integer> id;

    @ApiModelProperty(value = "角色ids")
    private List<Integer> roleIds;
}
