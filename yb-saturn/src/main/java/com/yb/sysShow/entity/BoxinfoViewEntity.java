package com.yb.sysShow.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author by summer
 * @date 2020/5/27.
 */
@Data
public class BoxinfoViewEntity {
    private String uuid;
    private String status;
    private String mac;
    private String macId;
    private int number;
    private int numberOfday;
    private Double dspeed;
    private String machineName;
    private Date updateAt;
    private String brand;
    private String model;

}
