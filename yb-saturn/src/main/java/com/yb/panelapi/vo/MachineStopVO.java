package com.yb.panelapi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/10/12 9:09
 */
@ApiModel("设备停机列表VO")
@Data
public class MachineStopVO {

    @ApiModelProperty("客户名称")
    private String cmName;

    @ApiModelProperty("工单编号")
    private String wbNo;

    @ApiModelProperty("停机列表信息")
    List<MachineStopListVO> machineStopListVOS;
}
