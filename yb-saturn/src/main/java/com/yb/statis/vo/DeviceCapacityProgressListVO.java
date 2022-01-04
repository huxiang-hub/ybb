package com.yb.statis.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/22 20:03
 */
@Data
@ApiModel("设备产能总进度VO")
public class DeviceCapacityProgressListVO {

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty("生产单号")
    private String wbNo;

    @ApiModelProperty("客户名称")
    private String cmName;

    @ApiModelProperty("产品名称")
    private String pdName;

    @ApiModelProperty("当前订单完成数")
    private Integer currentFinishNum;

    @ApiModelProperty("当前订单达成率")
    private BigDecimal currentRate;

    @ApiModelProperty("当班次排产数")
    private Integer planNum;

    @ApiModelProperty("当班次完成数")
    private Integer finishNum;

    @ApiModelProperty("当班次达成率")
    private BigDecimal rate;

    @JsonIgnore
    private Integer maId;
}
