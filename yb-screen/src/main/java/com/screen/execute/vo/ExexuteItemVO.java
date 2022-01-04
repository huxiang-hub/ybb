package com.screen.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "执行选项项目内容（弹窗的信息）", description = "选择生产准备需要做的那些项目内容列表，然后响应对应的事件")
public class ExexuteItemVO {

    @ApiModelProperty(value = "当前工单执工序分类唯一标识")
    @NotNull(message = "工序分类不能为空")
    Integer pyId;
    @ApiModelProperty(value = "设备唯一标识")
    Integer maId;
    @ApiModelProperty(value = "当前工单执行工序唯一id，可选填")
    Integer prId;

    @ApiModelProperty(value = "按钮的对象信息")
    List<ExexuteIteminfoVO> itemList;

}
