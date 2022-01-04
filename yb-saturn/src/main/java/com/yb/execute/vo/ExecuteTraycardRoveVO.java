package com.yb.execute.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 重新生成标识卡,删除托盘时所需的处理数据
 */
@Data
public class ExecuteTraycardRoveVO implements Serializable {

    @ApiModelProperty(value = "已变红的台数")
    private Integer redNum;
    @ApiModelProperty(value = "变红剩余的")
    private Integer planNum;
    @ApiModelProperty(value = "未变红的总台数")
    private Integer totalNum;
}
