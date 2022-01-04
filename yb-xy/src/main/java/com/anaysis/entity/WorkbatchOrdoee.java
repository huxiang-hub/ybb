package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("yb_workbatch_ordoee")
@ApiModel(value = "WorkbatchOrdoee对象", description = "生产排产设置OEE标准_yb_workbatch_ordoee")
public class WorkbatchOrdoee {
    /**
     * 生产排产设置OEE标准ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 排产ID
     */
    @ApiModelProperty(value = "排产ID")
    private Integer wkId;
    /**
     * 输入部件id
     */
    @ApiModelProperty(value = "输入部件id")
    private Integer beforePtid;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件名称")
    private String beforePtname;
    /**
     * 部件编号
     */
    @ApiModelProperty(value = "部件编号")
    private String beforePtno;
//    /**
//     * 保养类型字典：1日保2周保3月保4季保5半年保6年保
//     */
//    @ApiModelProperty(value = "保养类型字典：1日保2周保3月保4季保5半年保6年保")
//    private Integer maintain;
//    /**
//     * 保养时长（分）
//     */
//    @ApiModelProperty(value = "保养时长(分)")
//    private Integer maintainStay;
//    /**
//     * 保养次数
//     */
//    @ApiModelProperty(value = "保养次数")
//    private Integer maintainNum;
    /**
     * 换模时长（分）
     */
    @ApiModelProperty(value = "换模时长（分）")
    private  Integer mouldStay;
    /**
     * 换模次数
     */
    @ApiModelProperty(value = "换模次数")
    private Integer mouldNum;
//    /**
//     * 吃饭时间（分
//     */
//    @ApiModelProperty(value = "吃饭时间（分）")
//    private Integer mealStay;
//    /**
//     * 吃饭次数
//     */
//    @ApiModelProperty(value = "吃饭次数")
//    private Integer mealNum;

//    /**
//     * 质检次数
//     */
//    @ApiModelProperty(value = "质检次数")
//    private Integer qualityNum;

    /**
     * 质检次数
     */
    @ApiModelProperty(value = "难易程度")
    private Double difficultNum;

    /**
     *生产准备时间
     */
    @ApiModelProperty(value = "生产准备时间")
    private  Integer producePreTime;

    @ApiModelProperty(value = "速度小时(设备关联工序)")
    private Integer speed;

    @ApiModelProperty(value = "计划总时长")
    private Integer planTotalTime;

    /**
     * ordoee的计划用时
     */
    @ApiModelProperty(value = "ordoee的计划用时")
    private Integer planTime;
    /**
     * erp的小时产能，速度
     */
    @ApiModelProperty(value = "erp的小时产能，速度")
    private Integer erpSpeed;
}
