package com.yb.execute.vo;

import com.yb.execute.entity.ExecuteWaste;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/11
 **/
@Data
@ApiModel("工序对应废品vo")
public class ExecuteWasteNumberVO {
    @ApiModelProperty("工序名")
    private String prName;
    @ApiModelProperty("工序id")
    private Integer prId;
    @ApiModelProperty("工序排序")
    private Integer prSort;

    @ApiModelProperty("废品类型数量")
    private List<ExecuteWaste> wasteList;
}
