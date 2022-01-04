package com.screen.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "排程单详情信息", description = "详情内容：工序详情信息")
public class WorkbatchShiftProcessVO extends WorkbatchShiftVO {
//    @ApiModelProperty(value = "排程单的详细信息对象")
//    WorkbatchShiftVO shiftInfo;

    @ApiModelProperty( value = "上到工序的名称；")
    String upProceName;
    @ApiModelProperty( value = "上到工序的id；")
    Integer upPrId;
    @ApiModelProperty( value = "上到工序完成入库数量；")
    Integer upPrFinishNum;

    @ApiModelProperty(value="本工序计划数量(含放数)")
    Integer planNum;
    @ApiModelProperty(value = "最大损耗数量")
    Integer wasteNum;
    @ApiModelProperty(value = "本工艺细节说明")
    Integer proceDetail;


}

