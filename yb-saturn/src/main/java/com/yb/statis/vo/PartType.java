package com.yb.statis.vo;

import lombok.Data;

@Data
public class PartType {

    private Integer hour;
    private Integer startmin;
    private Integer endmin;
    private Integer type;//时间间隔状态 1、生产准备时间2、正式生产3、休息吃饭 4、无效状态
    private Integer wfId;
    private Integer diffmin;//中间间隔分钟数
}
