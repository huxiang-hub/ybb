package com.yb.workbatch.vo;

import com.yb.workbatch.entity.WorkbatchOrdlink;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/1/5
 **/
@Data
@ApiModel(value = "生产进度vo")
public class WorksTempoVO {

    @ApiModelProperty("工单编号")
    private String odNo;

    @ApiModelProperty("客户简称")
    private String customer;

    @ApiModelProperty("产品名称")
    private String prodName;

    @ApiModelProperty("工单对应工序生产情况")
    List<WorkbatchOrdlink> workbatchOrdlinkList;

}
