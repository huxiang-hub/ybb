package com.sso.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 *
 * @author Jenny wang
 */
@Data
@ApiModel(description = "用户信息")
public class SaUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户基础信息
     */
    @ApiModelProperty(value = "用户")
    private SaUser saUser;

    /**
     * 权限标识集合
     */
    @ApiModelProperty(value = "权限集合")
    private List<String> permissions;

    /**
     * 角色集合
     */
    @ApiModelProperty(value = "角色集合")
    private List<String> roles;



}
