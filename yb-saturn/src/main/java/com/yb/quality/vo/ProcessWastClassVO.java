package com.yb.quality.vo;

import com.yb.quality.entity.QualityWastClass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/13 10:40
 **/
@Data
@ApiModel("工序-废品分类vo")
public class ProcessWastClassVO {

    @ApiModelProperty("工序id")
    private Integer prId;

    @ApiModelProperty("工序名")
    private String prName;

    @ApiModelProperty("废品分类list")
    private List<QualityWastClass> list;
}
