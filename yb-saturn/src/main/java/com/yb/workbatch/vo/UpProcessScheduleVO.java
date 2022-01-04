package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/17 9:28
 */

@Data
@ApiModel("上工序进度VO")
public class UpProcessScheduleVO {

    @ApiModelProperty("工单id")
    private Integer id;
    @ApiModelProperty("计划数")
    private Integer planNum;

    @ApiModelProperty("完成数")
    private Integer completNum;

    @ApiModelProperty("状态")
    private String status;
}
