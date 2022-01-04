package com.yb.supervise.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 订单进度请求类
 * @Author my
 * @Date Created in 2020/7/9 15:34
 */
@ApiModel("订单进度请求类")
@Data
public class SuperviseOrderScheduleRequest {

    /**
     * 订单名称
     */
    @ApiModelProperty("订单名称")
    private String odName;

    /**
     * 批次编号
     */
    @ApiModelProperty("批次编号")
    private String batchNo;

    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private String odNo;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态 0 未执行  1 正在执行 2 已完成")
    private Integer orderStatus;

    /**
     * 批次状态
     */
    @ApiModelProperty(value = "批次状态1新建2正在生产3部分生产4完成5已排产")
    private Integer batchStatus;
}
