package com.yb.algorithm;

import com.yb.common.DateUtil;

import java.util.Date;

/****
 * 执行OEE的计算方法
 */
public class ExecutAlgorithm {
    //计算OEE的统计算法并且插入到对应的数据库表中。
    //获取当前的各个指标参数值信息内容
    //数据分析表：yb_statis_ordinfo  、yb_statis_ordfull

    /****
     * 设备自身的数据量统计信息
     * @param maId  设备关联新
     * @param algDate 分析统计日期2020-03-14
     * @param regularTime  yb_statis_machalg 设备的数据
     */
    public void algorithm(String maId, String algDate, String regularTime) {

        Date algtime = DateUtil.toDate(algDate + " " + regularTime, "yyyy-MM-dd HH:mm");
        Date algatartTime = DateUtil.addDayForDate(algtime, 1);//获取前一天的数据信息。





    }

//    public static void main(String[] args) {
//        String algDate = "2020-12-03";
//        String regularTime = "03:03";
//        Date algtime = DateUtil.toDate(algDate + " " + regularTime, "yyyy-MM-dd HH:mm");
//        System.out.println("test::::时间控制:" + algtime + "增加后的:" + DateUtil.addDayForDate(algtime, -1));
//    }

}
