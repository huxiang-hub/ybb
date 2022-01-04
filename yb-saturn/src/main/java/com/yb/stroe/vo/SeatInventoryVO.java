package com.yb.stroe.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author lzb
 * @Date 2020/9/24 10:47
 **/
@Data
@ApiModel(value = "库位-台账vo")
public class SeatInventoryVO {

    @ApiModelProperty(value = "库位id")
    private Integer seatId;
    @ApiModelProperty(value = "库区id")
    private Integer srId;
    @ApiModelProperty(value = "库区编号")
    private String srNo;
    @ApiModelProperty(value = "类型（数据字典1、半成品2、成品 3、原料4、辅料5、备品备件）")
    private Integer stType;
    @ApiModelProperty(value = "库位编号")
    private String stNo;
    @ApiModelProperty(value = "库位尺寸占地")
    private String size;
    @ApiModelProperty(value = "可放层数：数字下拉")
    private Integer layer;
    @ApiModelProperty(value = "托盘数量：数字下拉")
    private Integer trayNum;
    @ApiModelProperty(value = "库位容量")
    private Integer capacity;
    @ApiModelProperty(value = "选择顺序（数字先后，放满为准）")
    private Integer sort;
    @ApiModelProperty(value = "库位x轴数量")
    private Integer xNum;
    @ApiModelProperty(value = "库位y轴数量")
    private Integer yNum;
    @ApiModelProperty(value = "已经占用托板数量")
    private Integer useNum;
    @ApiModelProperty(value = "剩余可用托板数量")
    private Integer usableNum;
    @ApiModelProperty(value = "1占用，2锁定待入库，3移库")
    private Integer status;
    @ApiModelProperty(value = "锁定待入库数量")
    private Integer lockNum;
}
