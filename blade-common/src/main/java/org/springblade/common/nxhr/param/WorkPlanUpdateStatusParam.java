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
@ApiModel("修改排程单状态")
public class WorkPlanUpdateStatusParam {

    @ApiModelProperty("生产工单")
    private String wo;

    @ApiModelProperty("产品编号")
    private String bom;

    @ApiModelProperty("工序号")
    private String processNo;

    @ApiModelProperty("项")
    private String item;

    @ApiModelProperty("状态值（已排程状态：Scheduled）")
    private String status;

    @ApiModelProperty(hidden = true)
    Map<String, String> param = new LinkedHashMap<>();

    public Map<String, String> buildParam() {
        param.put("WO", this.wo);
        param.put("BOM", this.bom);
        param.put("ProcessNoStr", this.processNo);
        param.put("ItemStr", this.item);
        param.put("Status", this.status);
//        param.put("ReturnMsg", "");
        return param;
    }

}
