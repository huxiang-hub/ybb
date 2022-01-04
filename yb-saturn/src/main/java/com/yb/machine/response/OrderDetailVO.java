package com.yb.machine.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 工序流程图VO
 * @Author my
 */

@Data
@ApiModel(value = "工单详情VO")
public class OrderDetailVO {

    @ApiModelProperty("工单id")
    private Integer id;

    @ApiModelProperty("工单编号")
    private String wbNo;

    @ApiModelProperty("产品名称")
    private String cmName;

    @ApiModelProperty("工序名称")
    private String prName;

    @ApiModelProperty("顺序")
    private Integer sort;

    @ApiModelProperty("物料名称")
    private String materName;

    @ApiModelProperty("计划数")
    private Integer planNum;

    @ApiModelProperty("完成数")
    private Integer countNum;

    @ApiModelProperty("废品数")
    private Integer wasteNum;

    @ApiModelProperty("待排产数")
    private BigDecimal arrangeNum;

    @ApiModelProperty("完成进度")
    private BigDecimal rate;

    @ApiModelProperty("工序id")
    private Integer prId;
}
