package com.sso.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/12/7 9:55
 */
@Data
@ApiModel("人脸员工详情vo")
public class StaffInfoVO {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("工号")
    private String jobnum;

    @ApiModelProperty("真实名称")
    private String realName;

    @ApiModelProperty("部门名称")
    private String dpName;

    @ApiModelProperty("岗位")
    private String jobs;

    @ApiModelProperty("头像路径")
    private String avatar;

    @JsonIgnore
    private Integer bcId;
}
