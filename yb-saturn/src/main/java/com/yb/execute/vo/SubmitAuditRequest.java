package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author longzhengbin
 */
@Data
@ApiModel(value = "手机盘点-提交审核清单参数对象")
public class SubmitAuditRequest implements Serializable {

    @ApiModelProperty(value = "上报id")
    private Integer bfId;
    @ApiModelProperty(value = "操作提交人")
    private Integer usId;
    @ApiModelProperty(value = "审核类型1、盘点2、报废、3机长")
    private Integer spMold;
    @ApiModelProperty(value = "其他原因")
    private String spOther;
    @ApiModelProperty(value = "报废原因；下拉选择；选项：数据字典选择")
    private String spReason;
    @ApiModelProperty(value = "图片ids,逗号隔开")
    private String imgIds;

    @ApiModelProperty(value = "审核流程模板名称")
    private String templateName;
    @ApiModelProperty(value = "用户的钉钉id", required = true)
    private String userId;
    @ApiModelProperty(value = "跳转的路径")
    private String url;
    @ApiModelProperty(value = "输入框名称")
    private String rowName;
//    @ApiModelProperty(value = "审核流程模板中的输入框名称和值,key为名称,value为值")
//    private Map<String, String> map;
}
