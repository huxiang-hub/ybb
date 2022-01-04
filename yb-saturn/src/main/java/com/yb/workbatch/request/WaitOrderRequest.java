package com.yb.workbatch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "WaitOrderRequest参数", description = "手机获取工单列表参数信息")
public class WaitOrderRequest {
    @ApiModelProperty(value = "设备ID")
    Integer maId;
    @ApiModelProperty(value = "工序id")
    Integer prId;
    @ApiModelProperty(value = "工单排产日期")
    String sdDate;
    @ApiModelProperty(value = "产品名称")
    String pdName;
    @ApiModelProperty(value = "工单编号")
    String wbNo;
    @ApiModelProperty(value = "班次id白班/夜班")
    Integer wsId;
    @ApiModelProperty(value = "开始时间")
    Date startDate;
    @ApiModelProperty(value = "结束时间")
    Date endDate;
}
