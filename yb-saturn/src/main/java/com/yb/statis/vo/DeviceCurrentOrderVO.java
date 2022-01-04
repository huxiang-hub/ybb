package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/22 20:03
 */
@Data
@ApiModel("设备当前排产单进度信息VO")
public class DeviceCurrentOrderVO {

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("生产单号")
    private String wbNo;

    @ApiModelProperty("客户名称")
    private String clientName;

    @ApiModelProperty("产品名称")
    private String pdName;

    @ApiModelProperty("操作时间")
    private Date updateAt;

    @ApiModelProperty("执行时间")
    private Integer exeTime;

    @ApiModelProperty("完成数")
    private Integer finishNum;

    @ApiModelProperty("排产数")
    private Integer planNum;

    @ApiModelProperty("当前完成数")
    private BigDecimal currNum;

    @ApiModelProperty("达成率")
    private BigDecimal rate;

    @ApiModelProperty("订单执行状态")
    private String exeStatus ;

    @ApiModelProperty("设备状态")
    private String status ;

    @ApiModelProperty("日期")
    private String sdDate ;
}
