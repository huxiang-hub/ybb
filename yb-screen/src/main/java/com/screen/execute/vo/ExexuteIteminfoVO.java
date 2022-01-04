package com.screen.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "执行选项项目内容（弹窗按钮单个信息）", description = "选择生产准备需要做的那些项目内容列表，然后响应对应的事件")
public class ExexuteIteminfoVO {

    @ApiModelProperty(value = "执行选项列表唯一标识")
    String itemId;
    @ApiModelProperty(value = "执行选项列表")
    String itemName;
    @ApiModelProperty(value = "请求事件Url事件")
    String itemUrl;
    @ApiModelProperty(value = "本系统请求参数")
    String itemParam;
    @ApiModelProperty(value = "本系统请求类型：POST或者GET方式")
    String itemType;
    @ApiModelProperty(value = "执行外部接入exurl请求")
    String exeUrl;
    @ApiModelProperty(value = "执行请求所带参数及参数信息：可以json、字符串、参数")
    String exeParam;
    @ApiModelProperty(value = "执行外部接入系统请求：POST或者GET方式")
    String exeType;

}

