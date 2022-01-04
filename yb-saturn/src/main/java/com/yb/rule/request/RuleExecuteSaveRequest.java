package com.yb.rule.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @Description:
 * @Author my
 * @Date Created in 2020/9/18 15:05
 */
@ApiModel("规则设定请求类")
@Data
public class RuleExecuteSaveRequest {

    @ApiModelProperty("id,修改时传入")
    private Integer id;

    @ApiModelProperty("字段条件集合")
    @NotEmpty
    List<RuleExecuteSaveDTO> ruleExecuteSaveDTOS;

    @ApiModelProperty("设备id")
    @NotNull
    private Integer maId;

    @ApiModelProperty("类型：1换型时间、2标准产能")
    @NotNull
    private Integer rpType;

    @ApiModelProperty("更新换型时间(分钟)（当前默认换型）分钟")
    private Integer mouldTime;

    @ApiModelProperty("标准时速（小时）标准产能")
    private Integer capacity;

}
