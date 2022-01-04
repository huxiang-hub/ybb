package com.yb.statis.vo;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lzb
 * @Date 2020/9/15 17:33
 **/
@Data
public class PointType {
    private Integer hour;

    private Integer point;
    /* 0整点的开始0分，1生产开始，2准备结束 3生产开始和结束，4生产结束，5吃饭开始，6吃饭结束; 8整点的结束60分 */
    private Integer type;
    /* 排产单主键id */
    private Integer wfId;

    private String mealtime;//吃饭时间 12:00~13:15

}
