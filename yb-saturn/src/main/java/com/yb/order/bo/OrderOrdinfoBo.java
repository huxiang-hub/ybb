package com.yb.order.bo;

import com.yb.order.entity.OrderOrdinfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderOrdinfoBo extends OrderOrdinfo {
    @ApiModelProperty(value = "id")
    private Integer odId;
}
