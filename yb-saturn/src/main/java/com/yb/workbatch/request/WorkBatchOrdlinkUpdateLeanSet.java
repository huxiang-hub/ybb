package com.yb.workbatch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/25 18:44
 */
@ApiModel("精益生产设置更新请求")
@Data
public class WorkBatchOrdlinkUpdateLeanSet {

    @ApiModelProperty("排产id")
    private Integer sdId;

    @ApiModelProperty("排产日期")
    private String sdDate;

    @ApiModelProperty("换型时间")
    private Integer mouldStay;

    @ApiModelProperty("标准产能（速度）")
    private Integer speed;

    @ApiModelProperty("数量")
    private Integer planNum;

    @ApiModelProperty("预计总时长")
    private Integer planTotalTime;

    @ApiModelProperty("放数")
    private Integer wasteNum;

    @ApiModelProperty("班次排产id")
    private Integer wfId;
}
