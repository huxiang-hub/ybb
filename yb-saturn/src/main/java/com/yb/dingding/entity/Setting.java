package com.yb.dingding.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Auto-generated: 2020-11-09 9:50:35
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Setting implements Serializable {

    private int permit_late_minutes;
    private int absenteeism_late_minutes;
    private String is_off_duty_free_check;
    private Rest_begin_time rest_begin_time;
    private int work_time_minutes;
    private Rest_end_time rest_end_time;
    private int serious_late_minutes;
    private long class_setting_id;

}
