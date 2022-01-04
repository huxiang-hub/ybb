package com.yb.mater.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yb.mater.entity.ExecuteMaterials;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecuteMaterialsVO对象", description = "物料退料管理_yb_execute_offmater")
public class ExecuteMaterialsVO extends ExecuteMaterials {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "设备名称")
    private String maName;
    @ApiModelProperty(value = "班次名称")
    private String wsName;

    @ApiModelProperty(value = "库位id")
    @TableField(exist = false)
    private Integer seatId;

    @ApiModelProperty(value = "库位编码")
    private String  storePlace;

    @ApiModelProperty(value = "第几拖")
    @TableField(exist = false)
    private Integer trayNo;

    @ApiModelProperty(value = "托盘标识编码")
    private Integer tdNo;

    @ApiModelProperty(value = "物料状态：1当前工单的上工序物料，2非当前工单上工序物料")
    @TableField(exist = false)
    private Integer materialStatus = 1;

    @ApiModelProperty(value = "是否有退料")
    private boolean isOffmat = false;

}
