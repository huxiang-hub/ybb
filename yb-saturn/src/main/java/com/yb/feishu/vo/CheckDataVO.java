package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "飞书打卡流水对象")
public class CheckDataVO implements Serializable {

    @JsonProperty("recordId")
    private long recordId;
    @JsonProperty("employeeNo")
    private String employeeNo;
    @JsonProperty("checkTime")
    private long checkTime;
    private String comment;
    private double longitude;
    private double latitude;
    private String ssid;
    private String bssid;
    @JsonProperty("checkLocationName")
    private String checkLocationName;
    @JsonProperty("isField")
    private boolean isField;
    @JsonProperty("isWifiCheck")
    private boolean isWifiCheck;
    private int type;
    @JsonProperty("photoUrls")
    private List<String> photoUrls;
    @JsonProperty("creatorEmployeeNo")
    private String creatorEmployeeNo;
}
