package com.yb.prod.bo;

import com.yb.prod.entity.ProdPdinfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProdPdinfoBo extends ProdPdinfo {
    @ApiModelProperty(value = "id")
    private Integer pdId;
}
