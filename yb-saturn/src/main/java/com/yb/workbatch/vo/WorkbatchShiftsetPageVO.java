package com.yb.workbatch.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/10/23 14:01
 */
@ApiModel("班次设定分页VO")
@Data
public class WorkbatchShiftsetPageVO {

    private Integer id;

    @ApiModelProperty(value = "绑定的班次id")
    private Integer wsId;

    /**
     * 公司为空、部门id、工序id、设备id
     */
    @ApiModelProperty(value = "分类对应的id")
    private Integer dbId;

    /**
     * 分类：1公司2车间部门3工序4设备
     */
    @ApiModelProperty(value = "分类：1公司2车间部门3工序4设备")
    private Integer model;

    /**
     * 班次名称：
     */
    @ApiModelProperty(value = "班次名称")
    private String ckName;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date startDate;
    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date endDate;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern="HH:mm")
    @JsonFormat(pattern="HH:mm",timezone="GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern="HH:mm")
    @JsonFormat(pattern="HH:mm",timezone="GMT+8")
    private Date endTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String userName;

    /**
     * 吃饭时间
     */
    @ApiModelProperty(value = "吃饭时间")
    private Integer mealStay;

    @ApiModelProperty("第一次吃饭时间")
    private String mealOnetime;

    @ApiModelProperty("第二次吃饭时间")
    private String mealSecondtime;

    @ApiModelProperty("第三次吃饭时间")
    private String mealThirdtime;
}
