package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "打印标识卡数据")
public class TraycardDataVO implements Serializable {

    @ApiModelProperty(value = "班次名称")
    private String ckName;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
//    @ApiModelProperty(value = "结束时间")
//    private String proFinishTime;
    @ApiModelProperty(value = "工单编号")
    private String wbNo;
    @ApiModelProperty(value = "设备名称")
    private String maName;
    @ApiModelProperty(value = "工序名称")
    private String prName;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "用户")
    private String usName;
    @ApiModelProperty(value = "库位")
    private String stNo;
    @ApiModelProperty(value = "数量")
    private Integer num;
    @ApiModelProperty(value = "第几台（版号）")
    private Integer trayNo;
    @ApiModelProperty(value = "tdNo")
    private String tdNo;
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "打印次数")
    private Integer printNum;
    @ApiModelProperty(value = "库位id")
    private Integer mpId;
}
