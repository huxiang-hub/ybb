package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "托盘跟进记录返回对象")
public class TrayCardSumVO implements Serializable {

    @ApiModelProperty(value = "排产id")
    private Integer sdId;
    @ApiModelProperty(value = "标识符: 1 为当前所在工序")
    private Integer identifier;
    @ApiModelProperty(value = "工序名称")
    private String prName;
    @ApiModelProperty(value = "位置")
    private String stNo;
    @ApiModelProperty(value = "负责人名称")
    private String usName;
    @ApiModelProperty(value = "计划总数量")
    private Integer planNum;
    @ApiModelProperty(value = "已完成数")
    private Integer finishNum;
    @ApiModelProperty(value = "托盘总台数")
    private Integer totalNum;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

}
