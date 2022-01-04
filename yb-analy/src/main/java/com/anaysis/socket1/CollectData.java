package com.anaysis.socket1;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Data
//采集器数据返回json
public class CollectData implements Serializable {
    @Id
    private String id;//mongdb流水标志位
    String mid;    //设备ID-64位
    Integer uindex; //序号
    Integer mat;    //总运行时间s
    Integer mbt;    //正常运行时间s
    Integer mst;    //停机时间s
    Integer mft;    //故障时间s
    Integer mbs;    //设备状态，0为停机、1为正常运行
    Integer mf; //设备故障, 0为无故障，1为故障
    Integer mc; //设备计数s
    Date mtb; //"2019-10-02 12:12:12",  //开机时间
    Date mtc; //"2019-10-02 12:12:12",  //当前时间
    String kwh;//功耗 电表，
    Integer speed;//速度

    Date currtime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
