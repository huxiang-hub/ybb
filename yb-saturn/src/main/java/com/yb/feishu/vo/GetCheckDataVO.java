package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "获取飞书打卡流水对象")
public class GetCheckDataVO implements Serializable {

    private int errcode;
    private String errmsg;
    @JsonProperty("invalidEmployeeNos")
    private List<String> invalidEmployeeNos;
    @JsonProperty("unauthorizedEmployeeNos")
    private List<String> unauthorizedEmployeeNos;
    @JsonProperty("recordResult")
    private List<CheckDataVO> recordresult;
}
