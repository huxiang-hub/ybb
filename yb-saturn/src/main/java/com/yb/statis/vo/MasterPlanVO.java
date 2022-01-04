package com.yb.statis.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: 主计划vo
 * @Author my
 * @Date Created in 2020/7/23 19:14
 */
@ApiModel("主计划VO")
@Data
public class MasterPlanVO {

    /**
     * 工单编号
     */
    @ApiModelProperty("工单编号")
    private String wbNo;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String clientName;

    /**
     * 额外信息
     */
    @ApiModelProperty("额外信息")
    private List<MasterPlanExeVO> masterPlanExeVOS;

    @JsonIgnore
    private Integer wbId;

}
