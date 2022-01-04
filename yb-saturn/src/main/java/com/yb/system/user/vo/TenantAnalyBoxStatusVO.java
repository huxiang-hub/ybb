package com.yb.system.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/15 11:53
 */
@ApiModel("租户解析盒子状态vo")
@Data
public class TenantAnalyBoxStatusVO {

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("设备状态")
    private String status;

   @ApiModelProperty("排产计划数量")
    private Integer planNum;

    @ApiModelProperty("当前工单已生产数量")
    private Integer currNum;

    /**
     * 今日产量
     */
    @ApiModelProperty(value = "今日产量")
    private Integer numberOfDay;

    /**
     * 当前时速
     */
    @ApiModelProperty(value = "当前时速")
    private Double dspeed;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String pdName;

    /**
     * 工单单号
     */
    @ApiModelProperty(value = "工单单号")
    private String wbNo;

    /**
     * 工单开始时间
     */
    @ApiModelProperty(value = "工单开始时间")
    private Date startTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

}
