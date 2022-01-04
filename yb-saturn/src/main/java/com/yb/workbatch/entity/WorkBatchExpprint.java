package com.yb.workbatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_workbatch_expprint")
public class WorkBatchExpprint implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "排产ID")
    private Integer sdId;
    @ApiModelProperty(value = "版类")
    private String versionClass;
    @ApiModelProperty(value = "总色数")
    private Integer colorNum;
    @ApiModelProperty(value = "印色")
    private String paintColour;
    @ApiModelProperty(value = "印刷文件编号")
    private String ctpNo;
    @ApiModelProperty(value = "下资料袋时间CTP")
    private Date ctpTime;
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    @ApiModelProperty(value = "是否校板0不校板1校板")
    private Integer isCompare;
    @ApiModelProperty(value = "看色描述")
    private String colorDesc;
    @ApiModelProperty(value = "同印工艺")
    private String craftSame;
    @ApiModelProperty(value = "推送日期")
    private Date pushDate;
    @ApiModelProperty(value = "不装水油底纸1是0否")
    private Integer basePaper;
    @ApiModelProperty(value = "同系列生产1是0否")
    private Integer sameSeries;
    @ApiModelProperty(value = "含同印生产否1是0否")
    private Integer includePrint;
    @ApiModelProperty(value = "印刷机台名称")
    private String printStation;
}
