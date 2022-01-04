package com.yb.machine.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author my
 */
@ApiModel("订单标识卡VO")
@Data
public class OrderTrayCardVO {

    @ApiModelProperty("托盘号")
    private Integer trayNo;

    @ApiModelProperty("托盘数")
    private Integer trayNum;

    @ApiModelProperty("库区位置")
    private Integer storePlace;

    @ApiModelProperty("操作人")
    private Integer userName;


}
