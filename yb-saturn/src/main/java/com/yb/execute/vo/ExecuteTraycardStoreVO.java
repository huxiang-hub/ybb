package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "查询对应的托盘数据请求参数信息对象", description = "生产托盘标识卡信息_yb_execute_traycard")
public class ExecuteTraycardStoreVO {

    @ApiModelProperty(value = "托盘唯一标识")
    private String tyIds;
    @ApiModelProperty(value = "工序唯一标识")
    private Integer prId;
    @ApiModelProperty(value = "工单号")
    private String wbNo;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "工单工序唯一标识")
    private Integer sdId;
}
