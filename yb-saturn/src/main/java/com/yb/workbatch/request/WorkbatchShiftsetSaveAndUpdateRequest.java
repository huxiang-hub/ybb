package com.yb.workbatch.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/10/19 14:13
 */
@ApiModel("班次设定新增或修改请求")
@Data
public class WorkbatchShiftsetSaveAndUpdateRequest {

    @ApiModelProperty("id不传为新增")
    private Integer id;

    @ApiModelProperty(value = "班次id(必传)", required = true)
    @NotNull(message = "班次id不能为空")
    private Integer wsId;

    @ApiModelProperty(value = "班次名称(必传)", required = true)
    @NotBlank(message = "班次名称")
    private String ckName;

    /**
     * 公司为空、部门id、工序id、设备id
     */
    @ApiModelProperty(value = "车间id")
    private Integer dbId;

    /**
     * 分类：1公司2车间部门3工序4设备
     */
    @ApiModelProperty(value = "分类：1公司2车间部门3工序4设备(必传)", required = true)
    @NotNull(message = "分类不能为空")
    private Integer model;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "开始日期不能为空")
    private Date startDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "结束日期不能为空")
    private Date endDate;

    /**
     * 上班持续时间
     */
    @ApiModelProperty(value = "上班持续时间（分）上班工时减去吃饭的工时")
    private Integer stayTime;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "上班时间不能为空")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "下班时间不能为空")
    private Date endTime;

    /**
     * 吃饭时间
     */
    @ApiModelProperty(value = "吃饭时间合计（分）")
    private Integer mealStay;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    private Integer usId;

    @ApiModelProperty("第一次吃饭时间用~拼接开始结束时间：如:08:00~20:00")
    private String mealOnetime;

    @ApiModelProperty("第二次吃饭时间")
    private String mealSecondtime;

    @ApiModelProperty("第三次吃饭时间")
    private String mealThirdtime;

}
