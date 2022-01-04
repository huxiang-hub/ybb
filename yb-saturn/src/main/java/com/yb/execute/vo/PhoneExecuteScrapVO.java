package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2021/3/4
 **/
@Data
@ApiModel("手机端报废列表对象")
public class PhoneExecuteScrapVO {

    @ApiModelProperty("报废id")
    private Integer id;

    @ApiModelProperty("工单编号")
    private String wbNo;

    @ApiModelProperty("产品名")
    private String pdName;

    @ApiModelProperty("报废理由")
    private String spReason;

    @ApiModelProperty("设备名")
    private String maName;

    @ApiModelProperty("上报人")
    private String realName;

    @ApiModelProperty("工序名")
    private String prName;

    @ApiModelProperty("白班夜班")
    private String ckName;

    @ApiModelProperty("报废提交时间")
    private String createAt;

    @ApiModelProperty("报废数量")
    private Integer spNum;

    @ApiModelProperty("客户名")
    private String cmName;

    @ApiModelProperty("报废图片")
    private String spPicture;

    @ApiModelProperty("工单总量")
    private Integer planNum;

}
