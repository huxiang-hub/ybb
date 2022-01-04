package com.anaysis.systask;

import com.anaysis.common.DateUtil;
import com.anaysis.executSupervise.MachineSupervise;
import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.socket1.MTTCPS;
import com.anaysis.socket2.TCPS;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RunTimeout implements Runnable {

    //设置超时的时间，记录为离线状态；单位毫秒 默认五分钟一检测
    public static long timespace = 5 * 60 * 1000;

    public static long intervaltime = 2 * 60 * 1000;//如果间隔2分钟没有收到数据表示设备已经离线。

    public static Map<Integer, Runnable> exeRunport = new HashMap<>();//导入多个监听端口对象信息

    MachineSupervise machineOperate = new MachineSupervise(); //更换执行接口方法


    @Override
    public void run() {
        setOffline();//执行循环检查工作
    }

    public void setOffline() {
        int num = 1;
        //设置循环
        while (true) {
            try {
                Thread.sleep(timespace);//间隔时间进行检查
            } catch (InterruptedException e) {
                log.error("error1:" + e.getMessage());
                continue;
            }
            long ctime = System.currentTimeMillis();
            //Map<String, Long> catmap = new HashMap<>();
            //循环导入监听端口信息
            for (Map.Entry<Integer, Runnable> entry : exeRunport.entrySet()) {
                //不同的端口判定不同的对象，加载不同的在线数据信息
                if (entry.getKey() == 8881) {  //(宝锋8801)
                    Map<String, Long> noOnline = new HashMap<>();
                    TCPS runobj = (TCPS) entry.getValue();
                    for (Map.Entry<String, Long> timeout : runobj.BoxOnline.entrySet()) {
                        boolean istimeout = checkTimeout(timeout.getValue(), timeout.getKey(), ctime);
                        if (!istimeout) {
                            noOnline.put(timeout.getKey(), timeout.getValue());
                        } else
                            num++;
                    }
                    runobj.BoxOnline.putAll(noOnline);
                }
                //另个端口监听接口内容
                if (entry.getKey() == 8882) {  //(宝锋8802)
                    Map<String, Long> noOnline = new HashMap<>();
                    MTTCPS runobj = (MTTCPS) entry.getValue();
                    for (Map.Entry<String, Long> timeout : runobj.BoxOnline.entrySet()) {
                        boolean istimeout = checkTimeout(timeout.getValue(), timeout.getKey(), ctime);
                        if (!istimeout) {
                            noOnline.put(timeout.getKey(), timeout.getValue());
                        } else
                            num++;
                    }
                    runobj.BoxOnline.putAll(noOnline);
                    // catmap.putAll(runobj.BoxOnline);
                }
            }

            log.info("缓存检查是否离线=end===================num:" + num);
            num++;
        }

    }


    /***
     * 检查缓存的map信息中是否有已经超时的数据信息，进行离线操作。
     * @param lasttime
     * @param guid
     */
    private boolean checkTimeout(Long lasttime, String guid, long ctime) {
        //long lst = lk.getLastTime();
        //判断是否超时，超过timeout
        if (DateUtil.diffIntervalTime(lasttime, ctime, intervaltime)) {
            SuperviseBoxinfo bx = new SuperviseBoxinfo();
            bx.setUuid(guid);
            //写mongdb离线时间
            machineOperate.saveOffline(bx, ctime);
            return true;
        }
        return false;
    }


}
