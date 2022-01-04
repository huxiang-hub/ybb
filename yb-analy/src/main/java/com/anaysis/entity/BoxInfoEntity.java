package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class BoxInfoEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String mac;
    private String sip;
    private String address;
    private String status;
    private int number;
    private int numberOfDay;
    private int xlh;
    private Double dspeed;//与上个状态的速度保存，当前设备运行状态

    private Date createAt;
    private Date updateAt;
}
