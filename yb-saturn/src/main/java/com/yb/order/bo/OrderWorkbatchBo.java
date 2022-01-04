package com.yb.order.bo;


import com.yb.order.entity.OrderWorkbatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderWorkbatchBo  extends OrderWorkbatch {
  @ApiModelProperty(value = "owb独特的statues")
  private  Integer owbStatus;

  @ApiModelProperty(value = "id")
  private  Integer wbId;
}
