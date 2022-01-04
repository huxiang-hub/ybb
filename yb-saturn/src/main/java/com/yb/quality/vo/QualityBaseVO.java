package com.yb.quality.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "机台质量巡检选择项对象")
public class QualityBaseVO {
    @ApiModelProperty(value = "表中唯一标识id")
    Integer id;
    @ApiModelProperty(value = "客户名称")
    String cmName;
    @ApiModelProperty(value = "产品名称")
    String pdName;
    @ApiModelProperty(value = "工单编号")
    String wbNo;
    @ApiModelProperty(value = "排产ID")
    Integer wfId;
    @ApiModelProperty(value = "设备唯一id")
    Integer maId;
    @ApiModelProperty(value = "设备名称")
    String maName;
    @ApiModelProperty(value = "操作人ID")
    Integer usId;
    @ApiModelProperty(value = "操作人名称")
    String usName;
    @ApiModelProperty(value = "图片id")
    String imgId;
    @ApiModelProperty(value = "图片路径")
    String picUrl;
    @ApiModelProperty(value = "生产数量")
    Integer productionNum;
    @ApiModelProperty(value = "报告状态")
    Integer reportStatus;
    @ApiModelProperty(value = "抽检数量")
    Integer quantityDeclared;
    @ApiModelProperty(value = "报废数量")
    Integer scrapQuantity;
    @ApiModelProperty(value = "开始时间")
    Date startAt;
    @ApiModelProperty(value = "结束时间")
    Date endAt;
    @ApiModelProperty(value = "创建时间")
    Date createAt;
    @ApiModelProperty(value = "更新时间")
    Date updateAt;
    @ApiModelProperty(value = "表的全名")
    String tabName;
}
