package com.yb.stroe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 仓库的占位_yb_store_seat
 */
@Data
@TableName("yb_store_seat")
public class StoreSeat implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /*库区id*/
    @ApiModelProperty(value = "库区id")
    private Integer srId;
    /*库区编号*/
    @ApiModelProperty(value = "库区编号")
    private String srNo;
    /*类型（数据字典1、半成品2、成品 3、原料4、辅料5、备品备件）*/
    @ApiModelProperty(value = "类型（数据字典1、半成品2、成品 3、原料4、辅料5、备品备件）")
    private Integer stType;
    /*库位编号*/
    @ApiModelProperty(value = "库位编号")
    private String stNo;

    /*库位尺寸占地*/
    @ApiModelProperty(value = "库位尺寸占地")
    private String size;
    /*可放层数：数字下拉*/
    @ApiModelProperty(value = "可放层数：数字下拉")
    private Integer layer;
    /*设备类型*/
    @ApiModelProperty(value = "设备类型")
    private Integer maType;
    /*托盘数量：数字下拉*/
    @ApiModelProperty(value = "托盘数量：数字下拉")
    private Integer trayNum;
    /*库位容量*/
    @ApiModelProperty(value = "库位容量")
    private Integer capacity;
    /*选择顺序（数字先后，放满为准）*/
    @ApiModelProperty(value = "选择顺序（数字先后，放满为准）")
    private Integer sort;
    /*库位x轴数量*/
    @ApiModelProperty(value = "库位x轴数量")
    private Integer xNum;
    /*库位y轴数量*/
    @ApiModelProperty(value = "库位y轴数量")
    private Integer yNum;
    /*用户id*/
    @ApiModelProperty(value = "用户id")
    private Integer usId;
    /*创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /*更新时间*/
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

}
