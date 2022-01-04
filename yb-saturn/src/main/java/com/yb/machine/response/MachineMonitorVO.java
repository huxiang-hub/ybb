package com.yb.machine.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/11/17 15:17
 */
@ApiModel("设备监控vo类")
@Data
public class MachineMonitorVO {

    @ApiModelProperty("设备编码")
    private String mno;

    @ApiModelProperty("设备id")
    private Integer maId;

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("当前工序名称")
    private String prName;

    @ApiModelProperty("部门名称")
    private String dpName;

    @ApiModelProperty("开机时间")
    private Integer openTime;

    @ApiModelProperty("当前产量")
    private Integer nowCapacity;

    @ApiModelProperty("当前订单")
    private String currOrder;

    @ApiModelProperty("状态：1运行2停机3故障4离线5等待")
    private String status;

    @ApiModelProperty("是否接单 0未接单 1已接单")
    private Integer blnAccept;

    @ApiModelProperty("设备转速")
    private BigDecimal speed;

    @ApiModelProperty("开机人员")
    private String openUserName;

    @ApiModelProperty("当班计数")
    private Integer number;

    @ApiModelProperty("当班完成单数")
    private Integer finishOrderNum;

    @ApiModelProperty("当班总单数")
    private Integer allOrderNum;

    @ApiModelProperty("当班能耗")
    private String energyNum;

    @ApiModelProperty("当月能耗")
    private String monthEnergyNum;

    @JsonIgnore
    private String userIds;

}
