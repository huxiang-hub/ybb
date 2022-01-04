package com.yb.machine.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Description: 工序流程图VO
 * @Author my
 */

@Data
@ApiModel(value = "ProcessFlowVO对象", description = "VIEW")
public class ProcessFlowVO  {
    /**
     * 批次编号
     */
    @ApiModelProperty(value = "批次编号")
    private String wbNo;

    @ApiModelProperty(value = "订单编号")
    private String odNo;

    @ApiModelProperty(value = "订货厂家-客户名称")
    private String cmName;

    @ApiModelProperty(value = "产品名称")
    private String pdName;

    @ApiModelProperty(value = "工序名称顺序")
    private String prNames;

    @ApiModelProperty("计划数与待产数")
    private String planAndArrangeNum;

    @ApiModelProperty("完成数")
    private String countNumAndWasteNum;

    @ApiModelProperty(value = "生产状态")
    private String status;

}
