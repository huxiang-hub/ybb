package com.yb.panelapi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/10/12 9:09
 */
@ApiModel("设备停机列表信息VO")
@Data
public class MachineStopListVO {

    @ApiModelProperty("客户名称")
    private String clientName;

    @ApiModelProperty("工单编号")
    private String wbNo;

    @ApiModelProperty("停机开始时间")
    private Date startTime;

    @ApiModelProperty("停机结束时间")
    private Date endTime;

    @ApiModelProperty("确认人")
    private String confirmor;

    @ApiModelProperty("停机原因")
    private String stopReason;

}
