package org.springblade.common.nxhr.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author lzb
 * @Date 2021/3/31
 **/
@Data
@ApiModel("成品入库新增")
public class StoreFinishedProdInsertParam {

    @ApiModelProperty("工单唯一标记")
    private String woByBom;

    @ApiModelProperty("入库数量")
    private String  receiveQtys;

    @ApiModelProperty("入库日期")
    private String receiveDates;

    @ApiModelProperty("库区")
    private String location;

    @ApiModelProperty("库位")
    private String locSub;

    @ApiModelProperty("条码")
    private String barcode;

    @ApiModelProperty("托盘号")
    private String palleteNo;

    @ApiModelProperty("0——不自动确认；1——自动确认")
    private String autoConfirmeds;

    @ApiModelProperty("入库班组")
    private String shiftId;

    @ApiModelProperty("工单号")
    private String wo;

    @ApiModelProperty("产品编号")
    private String bom;

    @ApiModelProperty(hidden = true)
    Map<String, String> param = new LinkedHashMap<>();

    public Map<String, String> buildParam() {
        param.put("WOByBOM", this.woByBom);
        param.put("ReceiveQtyS", this.receiveQtys);
        param.put("ReceiveDateS", this.receiveDates);
        param.put("Location", this.location);
        param.put("LocSub", this.locSub);
        param.put("Barcode", this.barcode);
        param.put("PalleteNo", this.palleteNo);
        param.put("AutoConfirmedS", this.autoConfirmeds);
        param.put("ShiftID", this.shiftId);
        param.put("WO", this.wo);
        param.put("BOM", this.bom);
        return param;
    }

}
