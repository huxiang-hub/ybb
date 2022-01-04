package com.anaysis.systask;

import com.anaysis.executSupervise.MachineSupervise;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class CleanTask {
    /**
     * 每天凌晨执行一次
     * 缓存数据进行凌晨清零操作
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanTask() {
        MachineSupervise.isHourly = new HashMap<>();
        System.out.println("统计数据清理操作：" + new Date());
    }




//    public static void main(String[] args) {
//        //System.out.println(DateUtil.refNowDay().replace("-",""));
//        Date end = DateUtil.toDate("2020-03-21 20:27:31", null);
//        Date star = DateUtil.toDate("2020-03-21 20:26:22", null);
//        int secod = DateUtil.calLastedTime(end.getTime(), star);
//        int cont = 50;
//        Double sdb = (secod > 0) ? cont * 3600 / secod : 0d;
//        System.out.println("生产差值secod:" + secod + "速度为:speed:" + sdb);
//    }
}
