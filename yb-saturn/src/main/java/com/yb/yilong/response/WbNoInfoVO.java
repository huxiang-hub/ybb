package com.yb.yilong.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2021/1/6 16:11
 */
@ApiModel("实时工单详情信息")
@Data
public class WbNoInfoVO {

    @ApiModelProperty("执行单唯一标识")
    private Integer exId;

    @ApiModelProperty("设备id")
    private Integer maId;

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("盒子今日产量")
    private Integer numberOfDay;

    @ApiModelProperty("当前生产数量")
    private Integer currNum;

    @ApiModelProperty("工单盒子计数")
    private Integer boxNum;

    @ApiModelProperty("速度/小时")
    private BigDecimal dspeed;

    @ApiModelProperty("状态1：运行2：停机3：等待4：离线")
    private Integer status;

    @ApiModelProperty("是否接单(0未接单， 1已接单)")
    private Integer blnAccept;

    @ApiModelProperty("工单编号")
    private String wbNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("接单时间")
    private Date acceptTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("正式生产时间")
    private Date proTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("结束生产时间")
    private Date finishTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("上报时间")
    private Date reportTime;

    @ApiModelProperty("操作人")
    private String operator;
}
