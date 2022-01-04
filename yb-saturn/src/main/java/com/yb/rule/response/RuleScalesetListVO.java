package com.yb.rule.response;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import lombok.Data;

/**
 * @author my
 * @date 2020-09-22
 * Description: 标准产能维度设置_yb_rule_scalesetVO
 */
@Data
@ApiModel("标准产能维度设置 VO")
public class RuleScalesetListVO {

    private Integer id;
    /**
     * 对应的字段名；
     */
    @ApiModelProperty(value = "对应的字段名；")
    private String rsColumn;
    /**
     * 对应的字段中文名；
     */
    @ApiModelProperty(value = "对应的字段中文名")
    private String name;
}
