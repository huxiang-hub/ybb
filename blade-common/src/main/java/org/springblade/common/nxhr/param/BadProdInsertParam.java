package org.springblade.common.nxhr.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author lzb
 * @Date 2021/3/31 19:30
 **/
@Data
@ApiModel("车间生产数据上报(次品)")
public class BadProdInsertParam {

    @ApiModelProperty("机床名称")
    private String machine;

    @ApiModelProperty("生产工单")
    private String wo;

    @ApiModelProperty("产品编号")
    private String bom;

    @ApiModelProperty("工序名称")
    private String processNo;

    @ApiModelProperty("生产批次(正品报工反馈BatchID)")
    private String batchId;

    @ApiModelProperty("次品数量")
    private String badQty;

    @ApiModelProperty("生产班组")
    private String causeByShifId;

    @ApiModelProperty("致次原因")
    private String cause;

    @ApiModelProperty(hidden = true)
    Map<String, String> param = new LinkedHashMap<>();

    public Map<String, String> buildParam() {
        param.put("Machine", this.machine);
        param.put("WO", this.wo);
        param.put("BOM", this.bom);
        param.put("ProcessNoStr", this.processNo);
        param.put("BatchIDStr", this.batchId);
        param.put("BadQtyStr", this.badQty);
        param.put("CauseByShifID", this.causeByShifId);
        param.put("Cause", this.cause);
//        param.put("ReturnMsg", "");
        return param;
    }
}
