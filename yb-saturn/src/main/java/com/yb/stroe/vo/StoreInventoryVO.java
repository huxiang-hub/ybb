package com.yb.stroe.vo;

import com.yb.stroe.entity.StoreInventory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StoreInventoryVO对象", description = "StoreInventoryVO对象")
public class StoreInventoryVO extends StoreInventory {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "工单号")
    private String wbNo;
    @ApiModelProperty(value = "产品名称")
    private String pdName;

}
