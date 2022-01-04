package com.anaysis.socket3;

import com.anaysis.common.MachineConstant;
import com.anaysis.executSupervise.MachineSupervise;
import com.anaysis.socket1.CollectData;
import com.anaysis.socket1.MTTCPS;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class IotResolvePacket {

    //处理过程对象内容
    MachineSupervise machineOperate = new MachineSupervise(); //更换执行接口方法

    public IotResolvePacket() {
    }

    /***
     * 传入上报message信息，进行数据存储分析
     * @param coldata
     */
    public boolean resolve_m(IotCollectData coldata, String ip) throws Exception {
        log.info("解析主动上报数据:" + coldata.toString());
        long ctime = System.currentTimeMillis();
        String uuid_s = coldata.getMid();
        //开关机 0停机 1开机
        String status_s = String.valueOf(coldata.getMbs());
        status_s = ("0".equals(status_s)) ? MachineConstant.MA_STOP : status_s;
        //故障停机
        String status = (coldata.getMf().equals(1)) ? MachineConstant.MA_ERR : status_s;
        int num_xlh = coldata.getUindex();
        int num = coldata.getMc();
        coldata.setCurrtime(new Date(ctime));
        //接收即时数据写入到基础表中去；即时数据信息 屏蔽即时信息
        //machineOperate.savemongdb(coldata);
        boolean issucc = machineOperate.extOperate(uuid_s, num_xlh, num, status, ip, ctime, coldata.getMtc(), Integer.valueOf(coldata.getKwh()));
        //设定盒子的在线信息；并且更新最新的在线信息
        if (issucc)
            MTTCPS.BoxOnline.put(uuid_s, ctime);
        return issucc;
    }
}
