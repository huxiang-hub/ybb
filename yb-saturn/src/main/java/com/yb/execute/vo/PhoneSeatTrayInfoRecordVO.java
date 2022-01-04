package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2021/2/28
 **/
@Data
@ApiModel(value = "库位的托盘信息")
public class PhoneSeatTrayInfoRecordVO {

    @ApiModelProperty("第几托盘")
    private String trayNo;

    @ApiModelProperty("托盘编号")
    private String tdNo;

    @ApiModelProperty("产品名")
    private String pdName;

    @ApiModelProperty("产品张数")
    private Integer pdTotal;

    @ApiModelProperty("上报人")
    private String reportName;

    @ApiModelProperty("排产日期")
    private String sdDate;

    @ApiModelProperty("白班夜班")
    private String ckName;


}
