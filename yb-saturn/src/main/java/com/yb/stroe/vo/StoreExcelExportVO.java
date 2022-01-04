package com.yb.stroe.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author lzb
 * @Date 2021/3/20
 **/
@Data
@ApiModel("库存导出excel对象")
public class StoreExcelExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("工序")
    private String prName;

    @ApiModelProperty("工单号")
    private String odNo;

    @ApiModelProperty("产品名")
    private String pdName;

    @ApiModelProperty("库存量")
    private String num;

    @ApiModelProperty("仓库")
    private String srName;

}
