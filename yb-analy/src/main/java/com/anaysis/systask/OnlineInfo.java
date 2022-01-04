package com.anaysis.systask;

import com.anaysis.executSupervise.entity.SuperviseBoxinfo;

import java.util.HashMap;
import java.util.Map;

public interface OnlineInfo extends Runnable {

    //设置在线的盒子信息和最后的时间
    public static Map<String, Long>  BoxOnline = new HashMap<>();
    public static Map<String, SuperviseBoxinfo> ErrdataBoxinfo = new HashMap<>();

}
