package com.anaysis.socket2;

import com.anaysis.common.StringUtil;
import com.anaysis.executSupervise.MachineSupervise;
import com.anaysis.socket1.CollectData;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class ResolvePacket {

    //处理过程对象内容
    //MachineOperate machineOperate = new MachineOperate();
    MachineSupervise machineOperate = new MachineSupervise(); //更换执行接口方法

    public ResolvePacket() {
    }

    /***
     * 传入上报message信息，进行数据存储分析
     * @param message
     */
    public boolean resolve_m(String message, String ip) throws Exception {
//        log.info(formatter.format(new Date()));
        log.info("解析主动上报数据:" + message);
        //System.out.println("解析主动上报数据:" + message);
        char[] message_a = message.toCharArray();

        //设备GUID
        char[] uuid_a = Arrays.copyOfRange(message_a, 0, 24);

        //序列号
        char[] xlh_a = Arrays.copyOfRange(message_a, 28, 36);

        //数据1 状态
        char[] status_a = Arrays.copyOfRange(message_a, 39, 40);

        //数据 2 计数
        char[] num_a = Arrays.copyOfRange(message_a, 54, 62);

        String uuid_s = String.valueOf(uuid_a);
        String xlh_s = StringUtil.cleanBlank(String.valueOf(xlh_a));
        String status_s = StringUtil.cleanBlank(String.valueOf(status_a));
        String num_s = StringUtil.cleanBlank(String.valueOf(num_a));

        String status = resolve_sta(status_s);//返回数字状态 1run，2stop，3error
//        System.out.println("status:" + status);
        int num_xlh = Integer.parseInt(new BigInteger(xlh_s, 16).toString());
        int num = Integer.parseInt(new BigInteger(num_s, 16).toString());

        long ctime = System.currentTimeMillis();
        CollectData coldata = new CollectData();
        coldata.setUindex(num_xlh);
        coldata.setMid(uuid_s);
        coldata.setMc(num);
        if (!status.equals("3"))
            coldata.setMbs(Integer.parseInt(status));//0为停机、1为正常运行
        else {
            coldata.setMf(1);//设备故障, 0为无故障，1为故障
        }
        coldata.setMtc(new Date(ctime));
        coldata.setCurrtime(new Date(ctime));
        coldata.setKwh("-0");
       // machineOperate.savemongdb(coldata);
        if (coldata.getKwh() == null) {
            coldata.setKwh("0");
        }
        boolean issucc = machineOperate.extOperate(uuid_s, num_xlh, num, status, ip, ctime, new Date(), Integer.valueOf(coldata.getKwh()));
        //设定盒子的在线信息；并且更新最新的在线信息
        if (issucc)
            TCPS.BoxOnline.put(uuid_s, ctime);
        return issucc;
    }

    /****
     * 根据传入状态值信息判断设备当前状态信息，并且返回对应值信息： 1run，2stop，3error
     * @param status_s
     * @return
     */
    public String resolve_sta(String status_s) {
        //状态2进制
        String status_by = Integer.toBinaryString(Integer.parseInt(status_s, 16));

        //补位
        if (status_by.length() == 1) {
            status_by = "000" + status_by;
        } else if (status_by.length() == 2) {
            status_by = "00" + status_by;
        } else if (status_by.length() == 3) {
            status_by = "0" + status_by;
        }

//        log.info("返回状态二进制" + status_by);

        char[] sta_arrays = status_by.toCharArray();

        char[] d_error = Arrays.copyOfRange(sta_arrays, 0, 1);
        char[] d_stop = Arrays.copyOfRange(sta_arrays, 1, 2);
        char[] d_run = Arrays.copyOfRange(sta_arrays, 2, 3);

        String error_s = String.valueOf(d_error);
        String stop_s = String.valueOf(d_stop);
        String run_s = String.valueOf(d_run);

        String status = "0";
        //解析信号
        if ("1".equals(run_s)) {
            status = "1";//标识运行
        }
        if ("1".equals(stop_s)) {
            status = "2";//标识停止
        }
        if ("1".equals(error_s)) {
            status = "3";//标识故障
        }

        return status;
    }


}
