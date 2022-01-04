package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "创建或修改定时事件对象")
public class SuperviseIntervalalgEventVO implements Serializable {

    @ApiModelProperty(value = "保留多长时间内的数据, 数量")
    private Integer num = 3;
    @ApiModelProperty(value = "保留多长时间内的数据, 单位")
    private String unit = "MONTH";

}
