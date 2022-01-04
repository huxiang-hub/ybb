package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "飞书部门信息")
public class DepartmentInfos implements Serializable {

    @JsonProperty("i18n_name")
    private I18nname i18nname;
    @JsonProperty("chat_id")
    private String chatId;
    private String id;
    @JsonProperty("member_count")
    private Integer memberCount;
    private String name;
    @JsonProperty("open_department_id")
    private String openDepartmentId;
    @JsonProperty("parent_id")
    private String parentId;
    @JsonProperty("parent_open_department_id")
    private String parentOpenDepartmentId;
    @JsonProperty("leader_employee_id")
    private String leaderEmployeeId;
    @JsonProperty("leader_Open_id")
    private String leaderOpenId;
    private Integer status;
}
