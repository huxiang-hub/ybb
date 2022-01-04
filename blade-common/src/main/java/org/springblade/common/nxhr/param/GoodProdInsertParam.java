package org.springblade.common.nxhr.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;

/**
 * @Author lzb
 * @Date 2021/3/31
 **/
@Data
@ApiModel("车间生产数据上报(正品)")
public class GoodProdInsertParam {

    @ApiModelProperty("机床名称")
    private String machine;

    @ApiModelProperty("生产工单")
    private String wo;

    @ApiModelProperty("产品编号")
    private String bom;

    @ApiModelProperty("工序号")
    private String processNo;

//    @ApiModelProperty("输出(生产批次(次品调用时需要传入)")
//    private String batchIDStr;

    @ApiModelProperty("取自排程单")
    private String item;

    @ApiModelProperty("正品数量")
    private String goodQty;

    @ApiModelProperty("生产班组")
    private String shifId;

    @ApiModelProperty("生产结束时间")
    private String prodFinishDataTime;

    @ApiModelProperty("生开始束时间")
    private String prodStartDataTime;

    @ApiModelProperty(hidden = true)
    HashMap<String, String> param = new HashMap<>();



    public HashMap<String, String> buildParam() {
        param.put("Machine", this.machine);
        param.put("WO", this.wo);
        param.put("BOM", this.bom);
        param.put("ProcessNoStr", this.processNo);
        param.put("ItemStr", this.item);
        param.put("GoodQtyStr", this.goodQty);
        param.put("ShiftID", this.shifId);
        param.put("ProdFinishDateTimeStr", this.prodFinishDataTime);
        param.put("ProdStartDateTimeStr", this.prodStartDataTime);
        param.put("BatchID", "");
        param.put("ReturnMsg", "");
        return param;
    }
}
