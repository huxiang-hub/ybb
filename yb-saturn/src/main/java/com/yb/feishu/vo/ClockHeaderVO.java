package com.yb.feishu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "飞书考勤打卡请求头对象")
public class ClockHeaderVO implements Serializable {
    @ApiModelProperty(value = "签名")
    private String authorization;
    @ApiModelProperty(value = "考勤打卡的appKey")
    private String appKey;
    @ApiModelProperty(value = "32位 随机字符串")
    private String signatureNonce;
    @ApiModelProperty(value = "UTC Unix time 时间戳（自 UTC 时间1970年1月1号开始的秒数）如 1571889060， 表示当前签名验证码的过期时间。")
    private long expiredTimes;
}
