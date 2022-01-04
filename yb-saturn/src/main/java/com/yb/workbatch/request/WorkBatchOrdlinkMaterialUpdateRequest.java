package com.yb.workbatch.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/26 9:21
 */
@ApiModel("排产料，备注，最终时间交期修改请求")
@Data
public class WorkBatchOrdlinkMaterialUpdateRequest {

    /**
     * 排产id
     */
    @ApiModelProperty(value = "排产id(必传)", required = true)
    @NotNull(message = "排产id不能为空")
    private Integer sdId;

    /**
     * 备注1
     */
    @ApiModelProperty(value = "备注1")
    private String remarks;

    /**
     * 备注2
     */
    @ApiModelProperty(value = "备注2")
    private String secondRemark;

    /**
     * 要求原料入库时间
     */
    @ApiModelProperty(value = "要求原料入库时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date mainIngredientTime;

    /**
     * 要求辅料入库时间
     */
    @ApiModelProperty(value = "要求辅料入库时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ingredientTime;

    /**
     * 最终交期
     */
    @ApiModelProperty(value = "最终交期")
    private String finalTime;

    /**
     * 计划工时
     */
    @ApiModelProperty(value = "计划工时")
    private Integer planTotalTime;
}
