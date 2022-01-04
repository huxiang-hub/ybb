package com.yb.sysShow.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author by summer
 * @date 2020/5/27.
 */
@Data
public class BoxCleanLogEntity {
    private Integer id;
    private String uuid;
    private String status;
    private Integer number;
    private Integer number_of_day;
    private Double dspeed;
    private Date update_at;
    private Date op_date;
    private Date clean_time;
}
