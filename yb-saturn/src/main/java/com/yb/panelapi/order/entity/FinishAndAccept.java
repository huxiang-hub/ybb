package com.yb.panelapi.order.entity;

import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "结束接受订单参数对象")
public class FinishAndAccept implements Serializable {
    @ApiModelProperty(value = "设备id", required = true)
    private Integer maId;
    @ApiModelProperty(value = "接受订单所需sdId", required = true)
    private Integer sdId;
    @ApiModelProperty(value = "接受订单所需用户id", required = true)
    private Integer usId;
//    @ApiModelProperty(value = "结束订单所需执行id")
//    private Integer infoId;
    @ApiModelProperty(value = "接受订单所需wfId", required = true)
    private Integer wfId;
}
