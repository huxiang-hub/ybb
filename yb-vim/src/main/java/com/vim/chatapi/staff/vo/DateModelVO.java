package com.vim.chatapi.staff.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "", description = "考勤记录查询时间接收参数MODEL")
public class DateModelVO {
    /***
     * 开始时段
     */
    private String startDate;
    /**
     * 结束时段
     */
    private String endDate;
    /**
     * 用户的id
     */
    private Integer usId;
}
