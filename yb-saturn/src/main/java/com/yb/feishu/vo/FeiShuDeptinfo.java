package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "飞书部门列表")
public class FeiShuDeptinfo implements Serializable {

    @JsonProperty("department_infos")
    private List<DepartmentInfos> departmentInfos;
    @JsonProperty("invalid_departments")
    private InvalidDepartments invalidDepartments;
}
