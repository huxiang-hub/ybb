package com.yb.stroe.vo;

import com.yb.stroe.entity.StoreSeat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StoreSeatVO extends StoreSeat {
    /*总台数*/
    @ApiModelProperty(value = "托盘数量")
    private Integer totalNum;
}
