package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2021/4/22 09:15
 **/
@ApiModel("审核id对象")
@Data
public class ProcessInstanceVO {

    @ApiModelProperty("审核进度id")
    private String processInstanceId;

    @ApiModelProperty(value = "审核类型1、盘点2、报废、3机长")
    private Integer spMold;
}
