package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "飞书用户对象")
public class UserInfos implements Serializable {
    @JsonProperty("avatar_240")
    private String avatar240;
    @JsonProperty("avatar_640")
    private String avatar640;
    @JsonProperty("avatar_72")
    private String avatar72;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String city;
    private String country;
    @JsonProperty("custom_attrs")
    private CustomAttrs customAttrs;
    private List<String> departments;
    private String description;
    private String email;
    @JsonProperty("employee_id")
    private String employeeId;
    @JsonProperty("employee_no")
    private String employeeNo;
    @JsonProperty("employee_type")
    private Integer employeeType;
    @JsonProperty("en_name")
    private String enName;
    @JsonProperty("is_tenant_manager")
    private Boolean isTenantManager;
    private String mobile;
    private String name;
    @JsonProperty("name_py")
    private String namePy;
    @JsonProperty("open_departments")
    private List<String> openDepartments;
    @JsonProperty("open_id")
    private String openId;
    private Integer status;
    @JsonProperty("union_id")
    private String unionId;
    @JsonProperty("update_time")
    private Integer updateTime;
    @JsonProperty("work_station")
    private String workStation;
    @JsonProperty("gender")
    private Integer gender;
    @JsonProperty("join_time")
    private String joinTime;
    @JsonProperty("leader_employee_id")
    private String leaderEmployeeId;
    @JsonProperty("leader_open_id")
    private String leaderOpenId;
    @JsonProperty("leader_union_id")
    private String leaderUnionId;
}
