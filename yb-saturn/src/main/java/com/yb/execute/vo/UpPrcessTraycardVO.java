package com.yb.execute.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UpPrcessTraycardVO implements Serializable {

    @ApiModelProperty(value = "当前排产单id")
    private Integer nowaDayWfId;
    @ApiModelProperty(value = "上工序托盘信息")
    private List<UpPrTraycardVO> upPrTraycardVOList;
}
