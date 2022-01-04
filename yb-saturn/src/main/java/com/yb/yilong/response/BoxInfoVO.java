package com.yb.yilong.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 盒子实时信息vo
 * @Author my
 */
@ApiModel("盒子实时信息vo")
@Data
public class BoxInfoVO {

    @ApiModelProperty("设备唯一标识")
    private Integer maId;

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("工单编号")
    private String wbNo;

    @ApiModelProperty("【状态 1运行 2停机 3故障4离线5等待】")
    private String status;

    @ApiModelProperty("当前产量")
    private Integer number;

    @ApiModelProperty("工单状态 【B：接单，C:正式生产 D:结束生产】")
    private String wbStatus;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("执行单唯一标识")
    private Integer exId;
}
