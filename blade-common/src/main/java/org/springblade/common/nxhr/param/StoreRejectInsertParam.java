package org.springblade.common.nxhr.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author lzb
 * @Date 2021/3/31
 **/
@Data
@ApiModel("成品退车间新增")
public class StoreRejectInsertParam {

    @ApiModelProperty("产品编号")
    private String bom;

    @ApiModelProperty("条码")
    private String barcode;

    @ApiModelProperty("托盘号")
    private String palleteNo;

    @ApiModelProperty("退库数量")
    private String rejectQtyS;

    @ApiModelProperty("退库日期")
    private String rejectDateS;

    @ApiModelProperty("退库原因")
    private String rejectFor;

    @ApiModelProperty("退库班组")
    private String rejectFrom;

    @ApiModelProperty(hidden = true)
    Map<String, String> param = new LinkedHashMap<>();

    public Map<String, String> buildParam() {
        param.put("BOM", this.bom);
        param.put("Barcode", this.barcode);
        param.put("PalleteNo", this.palleteNo);
        param.put("RejectQtyS", this.rejectQtyS);
        param.put("RejectDateS", this.rejectDateS);
        param.put("RejectFor", this.rejectFor);
        param.put("RejectFrom", this.rejectFrom);
        return param;
    }

}
