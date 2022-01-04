package com.sso.panelapi.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("临时虚拟表")
public class CompanyInfo implements Serializable {
    @ApiModelProperty(value = "车间名字")
    private String deptName;
    @ApiModelProperty(value = "设备名字")
    private String name;
    @ApiModelProperty(value = "设备品牌")
    private String brand;
    @ApiModelProperty(value = "设备规格")
    private String model;
    @ApiModelProperty(value = "设备型号")
    private String mno;
    @ApiModelProperty(value = "工序")
    private String proType;

}
