package com.yb.yilong.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.support.Query;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author my
 * #Description
 */
@ApiModel("设备停机数据分页请求")
@Data
public class MachineDownPageRequest extends Query {


    @NotNull(message = "设备id 不能为空")
    @ApiModelProperty("设备id")
    Integer maId;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date startTime;

    @ApiModelProperty("结束时间时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date endTime;
}
