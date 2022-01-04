package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/2/28
 **/
@Data
@ApiModel(value = "库位存放托盘vo")
public class PhoneSeatTrayInfoVO {

    @ApiModelProperty("库位编号")
    private String stNo;

    @ApiModelProperty("容纳拖板数量")
    private Integer capacity;

    @ApiModelProperty("已存放拖板数量")
    private Integer useTrayNumber;

    @ApiModelProperty("存放产品总数")
    private Integer pdTotal;

    @ApiModelProperty("库位托盘信息明细列表")
    List<PhoneSeatTrayInfoRecordVO> phoneSeatTrayInfoRecordVOList;

}
