package com.yb.execute.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UpPrTraycardVO implements Serializable {

    @ApiModelProperty(value = "上工序排产单id")
    private Integer upWfId;
    @ApiModelProperty("上工序排产单日期")
    private String sdDate;
    @ApiModelProperty(value = "上工序托盘信息")
    private List<ExecuteTraycardVO> executeTraycardVOList;
}
