package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "标识卡数据")
public class TraycardTextVO implements Serializable {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "打印次数")
    private Integer printNum;
    @ApiModelProperty(value = "库位id")
    private Integer mpId;
    @ApiModelProperty(value = "生产时间")
    private String productTime;
    @ApiModelProperty(value = "打印时间")
    private Date printTime;
    @ApiModelProperty(value = "工序名")
    private String prName;
    @ApiModelProperty(value = "设备名")
    private String maName;
    @ApiModelProperty(value = "机长")
    private String usName;
    @ApiModelProperty(value = "班次")
    private String wsName;
    @ApiModelProperty(value = "数量")
    private Integer num;
    @ApiModelProperty(value = "工单")
    private String wbNo;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "库位")
    private String stNo;
    @ApiModelProperty(value = "第几台（版号）")
    private Integer trayNo;
    @ApiModelProperty(value = "托盘编号")
    private String tdNo;
}
