package com.yb.execute.vo;

import com.yb.execute.entity.ExecuteBriefer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "机台上未进行上报的数据信息内容")
public class ExecuteBrieferOrdlinkVO extends ExecuteBriefer {

    @ApiModelProperty(value = "工单编号")
    String wbNo;
    @ApiModelProperty(value = "产品名称")
    String pdName;
    @ApiModelProperty(value = "设备名称")
    String maName;
    @ApiModelProperty(value = "用户名")
    String usName;
    @ApiModelProperty(value = "执行日期")
    String targetDay;
    @ApiModelProperty(value = "班次名称")
    String wsName;
    @ApiModelProperty(value = "上报表对应工序ID")
    Integer prId;
    @ApiModelProperty(value = "客户名称")
    String cmName;
    @ApiModelProperty(value = "计划总数（含废品）")
    Integer planNum;
    @ApiModelProperty(value = "计划废品")
    Integer wasteNum;

}
