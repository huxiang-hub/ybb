package com.yb.execute.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *停机时间段
 */
@Data
public class DowntimeVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /*开始时间*/
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    /*结束时间*/
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
