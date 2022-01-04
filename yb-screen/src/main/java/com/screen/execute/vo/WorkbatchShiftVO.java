package com.screen.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel(value = "计划排产列表，先后顺序", description = "定义屏幕中排产单内容信息")
public class WorkbatchShiftVO {
    @ApiModelProperty(value = "唯一标识")
    private Integer sdId;
    @ApiModelProperty(value = "排程唯一ID")
    private Integer wfId;
    @ApiModelProperty(value = "工单编号")
    private String wbNo;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "客户名称")
    private String cmName;
    @ApiModelProperty(value = "排产单状态：待开工1、已完成4、结束3;如果状态为1和4按钮判断：进入生产，若为3跳转到结束上报弹出页面。")
    private Integer shiftStatus;
    @ApiModelProperty(value = "排程顺序；【暂时可以不用】")
    private Integer sort;
    @ApiModelProperty(value = "执行设备唯一标识")
    private Integer maId;

}
