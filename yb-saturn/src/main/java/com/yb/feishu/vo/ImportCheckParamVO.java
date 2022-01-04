package com.yb.feishu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "飞书导入打卡参数对象")
public class ImportCheckParamVO implements Serializable {
    @ApiModelProperty(value = "授权内的员工工号或 employeeId，employeeKeyType 用于区分是工号还是 employeeId", required = true)
    private String employeeNo;
    @ApiModelProperty(value = "操作人的工号或 employeeId，employeeKeyType 用于区分是工号还是 employeeId employeeId，employeeKeyType 用于区分是工号还是 employeeId")
    private String operatorEmployeeNo;
    @ApiModelProperty(value = "该值为 1 表示 employeeNos 为 employeeId 列表，其它值为工号列表")
    private int employeeKeyType = 1;
    @ApiModelProperty(value = "打卡流水记录")
    private List<ImportCheckVO> checkRecords;

}
