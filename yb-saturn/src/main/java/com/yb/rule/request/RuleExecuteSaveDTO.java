package com.yb.rule.request;

import com.yb.common.constant.LocalEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/9/18 16:13
 */
@ApiModel("规则设定DTO")
@Data
public class RuleExecuteSaveDTO implements Serializable {

    @ApiModelProperty("数据库字段名")
    @NotBlank
    private String field;

    @ApiModelProperty("中文显示名称")
    private String name;


    @ApiModelProperty(value = "字段条件", required = true)
    @NotBlank(message = "条件不能为空")
    private String ruleSettingCondition;


    @ApiModelProperty(value = "字段值", required = true)
    @NotBlank(message = "值不能为空")
    private String value;

    @ApiModelProperty(value = "字段条件类型", required = true)
    @NotNull(message = "字段条件类型不能为空")
    private LocalEnum.RuleSettingType ruleSettingType;

    @ApiModelProperty(value = "左括号", required = true)
    @NotNull(message = "左括号不能为空")
    private Boolean leftParenthesis;

    @ApiModelProperty(value = "右括号", required = true)
    @NotNull(message = "右括号不能为空")
    private Boolean rightParenthesis;

    private String other;
}
