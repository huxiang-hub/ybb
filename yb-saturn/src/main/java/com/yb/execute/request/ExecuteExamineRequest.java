package com.yb.execute.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "提交审核数据可以拍照和提交修改数据", description = "执行审核操作")
public class ExecuteExamineRequest {
    @ApiModelProperty(value = "上报的bfId唯一标识")
    @NotNull(message = "上报工单不能为空")
    Integer bfId;
    @ApiModelProperty(value = "托盘id")
    Integer tyId;
    @ApiModelProperty(value = "库存台账Id")
    Integer siId;
    @ApiModelProperty(value = "审核人id")
    Integer exUserid;
    @ApiModelProperty(value = "图片urls,多个用'｜'隔开")
    String exPics;
    @ApiModelProperty(value = "修改库位")
    String modifyStNo;
    @ApiModelProperty(value = "修改后数量")
    Integer modifyNumber;
    @ApiModelProperty(value = "修改类型1、盘点2、报废、3机长")
    String exMold;

}
