package com.yb.stroe.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author lzb
 * @Date 2021/3/26 08:42
 **/
@Data
@ApiModel("根据数量入库对象")
public class InInventoryByNumberVO {

    @ApiModelProperty("执行单id")
    private Integer exId;

    @ApiModelProperty("入库数量")
    private Integer number;

    @ApiModelProperty("存储类型（数据字典1、半成品2、成品 3、原料4、辅料5、备品备件）")
    private Integer stType;

    @ApiModelProperty("托盘占用位置数量,单位：板")
    private Integer layNum;

    @ApiModelProperty("库区id")
    private Integer areaId;
}