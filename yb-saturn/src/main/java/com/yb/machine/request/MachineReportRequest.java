package com.yb.machine.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description:
 * @Author my
 */
@ApiModel("设备报表请求")
@Data
public class MachineReportRequest {

    @ApiModelProperty(value = "设备id",required = true)
    @NotNull(message = "设备Id不能")
    private Integer maId;

    @ApiModelProperty("班次id")
    private Integer wsId;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
