package com.anaysis.socket1;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class BladeData {
    @Id
    private String id;      //mongdb流水标志位
    String mid;     //设备uuid
    String uindex;      //通讯序列号
    String mc;      //刀版计数值
    Date mtb;     //设备开机时间
    Date mtc;     //设备当前时间
    Date ntime;//设置当前系统时间
}
