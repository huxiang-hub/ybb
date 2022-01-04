package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "托盘流程单返回对象")
public class FlowCardVO implements Serializable {

    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "库区")
    private String srNo;
    @ApiModelProperty(value = "库位")
    private String stNo;
//    @ApiModelProperty(value = "嵌套/包装二维码")
//    private String stNo;
//    @ApiModelProperty(value = "良/次品")
//    private String stNo;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "客户名称")
    private String cmName;
    @ApiModelProperty(value = "订单编号")
    private String odNo;
    @ApiModelProperty(value = "工单号")
    private String wbNo;
    @ApiModelProperty(value = "成品名称")
    private String ptName;
    @ApiModelProperty(value = "设备名称")
    private String maName;
    @ApiModelProperty(value = "数量")
    private Integer trayNum;
    @ApiModelProperty(value = "托盘跟进记录")
    private List<TrayCardSumVO> trayCardSumVOList;
}
