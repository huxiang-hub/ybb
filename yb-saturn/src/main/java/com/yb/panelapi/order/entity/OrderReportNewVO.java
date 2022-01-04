package com.yb.panelapi.order.entity;

import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.vo.ExecuteBrieferVO;
import com.yb.panelapi.waste.entity.QualityBfwaste;
import com.yb.panelapi.waste.vo.QualityBfwasteVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "最新上报对象数据信息")
public class OrderReportNewVO {
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    //需要上报的上报表的id
    @ApiModelProperty(value = "排程排产BfId唯一标识")
    private Integer bfId;

    @ApiModelProperty(value = "上报对象usId")
    private Integer usId;

    @ApiModelProperty(value = "执行班组人员usIds")
    private String usIds;

    /**
     * 上报作业数量
     */
    @ApiModelProperty(value = "上报作业数量")
    private Integer productNum;
    /**
     * 上报正品数量
     */
    @ApiModelProperty(value = "上报正品数量")
    private Integer countNum;
    /**
     * 废品数量
     */
    @ApiModelProperty(value = "废品数量")
    private Integer wasteNum;



    //废品种类
    @ApiModelProperty(value = "废品填报数据")
    private List<QualityBfwasteVO> wastes;

    @ApiModelProperty(value = "是否自动交接班换班")
    private Integer isAuto;
}
