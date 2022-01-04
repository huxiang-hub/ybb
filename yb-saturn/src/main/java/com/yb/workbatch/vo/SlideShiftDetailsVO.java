package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/22 9:45
 */
@ApiModel("滑动详情班次VO")
@Data
public class SlideShiftDetailsVO {

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("排产日期")
    private String sdDate;

    @ApiModelProperty("班次名称")
    private String ckName;

    /**
     * 开始生产时间
     */
    private Date proBeginTime;

    /**
     * 结束时间
     */
    private Date proFinishTime;

    /**
     * 计划数
     */
    private Integer planNum;

    /**
     * 上报数
     */
    private Integer reportNum;

    /**
     * 排产id
     */
    @ApiModelProperty(value = "排产id")
    private Integer sdId;

    @ApiModelProperty(value = "已完成数量")
    private Integer finishNum;

    @ApiModelProperty(value = "良品数")
    private Integer countNum;

    private String status;

    /**
     * 班次开始时间
     */
    private Date startTime;
    /**
     * 班次结束时间
     */
    private Date endTime;

    private String shiftStatus;
}
