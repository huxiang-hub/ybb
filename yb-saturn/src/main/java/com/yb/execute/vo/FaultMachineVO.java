package com.yb.execute.vo;

import com.yb.execute.entity.ExecuteFault;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "设备停机返回数据")
public class FaultMachineVO extends ExecuteFault {
    @ApiModelProperty(value = "上报人")
    private String usName;
    @ApiModelProperty(value = "停机事由")
    private String fname;

}
