package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "通讯录授权对象")
public class ScopeGet implements Serializable {
    @ApiModelProperty(value = "已授权部门自定义 ID 列表，授权范围为全员可见时返回的是当前企业的所有一级部门列表")
    @JsonProperty("authed_departments")
    private List<String> authedDepartments;
    @ApiModelProperty(value = "已授权用户 employee_id 列表，应用申请了获取用户user_id 权限时返回；当授权范围为全员可见时返回的是当前企业所有顶级部门用户列表")
    @JsonProperty("authed_employee_ids")
    private List<String> authedEmployeeIds;
    @ApiModelProperty(value = "已授权部门 openID 列表，授权范围为全员可见时返回的是当前企业的所有一级部门列表")
    @JsonProperty("authed_open_departments")
    private List<String> authedOpenDepartments;
    @ApiModelProperty(value = "已授权用户 open_id 列表；当授权范围为全员可见时返回的是当前企业所有顶级部门用户列表")
    @JsonProperty("authed_open_ids")
    private List<String> authedOpenIds;
}
