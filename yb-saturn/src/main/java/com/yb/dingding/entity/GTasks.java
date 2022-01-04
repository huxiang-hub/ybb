package com.yb.dingding.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "待办任务列表对象")
public class GTasks  implements Serializable {

    @ApiModelProperty(value = "AGREE：同意, REFUS：拒绝, 当status为CANCELED时，不需要传result。\n" +
            "返回参数")
    private String result;
    @ApiModelProperty("待办任务ID。")
    private Long taskId;
    @ApiModelProperty(value = "任务状态:CANCELED：取消, COMPLETED：完成, 取消不需要result参数,完成必须要")
    private String status;
}
