package com.sso.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "导入钉钉考勤的参数对象")
public class DingAttendance implements Serializable {

    @ApiModelProperty(value = "考勤机名", required = true)
    @JsonProperty("device_name")
    private String deviceName;
    @ApiModelProperty(value = "考勤机ID",required = true)
    @JsonProperty("device_id")
    private String deviceId;
    @ApiModelProperty(value = "员工打卡的时间，单位毫秒。Unix时间戳", required = true)
    @JsonProperty("user_check_time")
    private Long userCheckTime;
    @ApiModelProperty(value = "打卡备注图片地址，必须是公网可访问的地址")
    @JsonProperty("photo_url")
    private String photoUrl;
    @ApiModelProperty(value = "用户的钉钉id", required = true)
    private String userid;
}
