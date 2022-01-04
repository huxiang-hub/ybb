package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "飞书设置语言对象")
public class I18nname implements Serializable {

    @JsonProperty("en_us")
    private String enUs;
    @JsonProperty("ja_jp")
    private String jaJp;
    @JsonProperty("zh_cn")
    private String zhCn;
}
