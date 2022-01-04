package com.anaysis.socket1;

import com.anaysis.common.DateTimeUtil;
import com.anaysis.common.MachineConstant;
import com.anaysis.executSupervise.MachineSupervise;
import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.entity.SuperviseRegular;
import com.anaysis.executSupervise.mapper.SuperviseBoxinfoMapper;
import com.anaysis.executSupervise.mapper.SuperviseRegularMapper;
import com.anaysis.yLong.YiLongCollectData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@Component
public class MTResolvePacket {

    //处理过程对象内容
    //MachineOperate machineOperate = new MachineOperate();
    @Autowired
    MachineSupervise machineOperate;
    @Autowired
    private SuperviseRegularMapper superviseRegularMapper;
    @Autowired
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;

    public MTResolvePacket() {
    }

    /***
     * 传入上报message信息，进行数据存储分析
     * @param coldata
     */
    public boolean resolve_m(CollectData coldata, String ip) throws Exception {
//        log.info(formatter.format(new Date()));
        log.debug("解析主动上报数据:" + coldata.toString());
        long ctime = System.currentTimeMillis();
        String uuid_s = coldata.getMid();
        //开关机 0停机 1开机
        String status_s = String.valueOf(coldata.getMbs());
        status_s = ("0".equals(status_s)) ? MachineConstant.MA_STOP : status_s;
        //故障停机
        String status = (coldata.getMf().equals(1)) ? MachineConstant.MA_ERR : status_s;
        int num_xlh = coldata.getUindex();
        int num = coldata.getMc();
        Date sendTime = coldata.getMtc();
        coldata.setCurrtime(new Date(ctime));
        //接收即时数据写入到基础表中去；即时数据信息 屏蔽即时信息
//        machineOperate.savemongdb(coldata);
        if (coldata.getKwh() == null) {
            coldata.setKwh("0");
        }

        boolean issucc = machineOperate.extOperate(uuid_s, num_xlh, num, status, ip, ctime, sendTime, Integer.valueOf(coldata.getKwh()));
        //设定盒子的在线信息；并且更新最新的在线信息
        if (issucc) {
            MTTCPS.BoxOnline.put(uuid_s, ctime);
        }
        return issucc;
    }

    /***
     * 传入上报message信息，进行数据存储分析
     * @param coldata
     */
    public boolean yiLongResolve_m(YiLongCollectData coldata) throws ParseException {
        log.info("解析艺龙数据:" + coldata.toString());

        long ctime = System.currentTimeMillis();
        String uuid_s = coldata.getDevID();
        YiLongCollectData.data data = coldata.getData();
        // 0停机 1开机 2空转
        String status_s = String.valueOf(data.getState());
        String status = ("0".equals(status_s)) ? MachineConstant.MA_STOP : status_s;
        //故障停机
        Integer num_xlh = Integer.valueOf(coldata.getIndex());
        int num = Integer.valueOf(data.getOutc());
        Date sendTime = DateTimeUtil.format(coldata.getTimes(), DateTimeUtil.DEFAULT_DATE_TIME_FORMATTER);

        boolean issucc = machineOperate.yiLongExtOperate(uuid_s, num_xlh, num, status, ctime, sendTime, Integer.valueOf(data.getSpeed()));
        //设定盒子的在线信息；并且更新最新的在线信息
        if (issucc) {
            MTTCPS.BoxOnline.put(uuid_s, ctime);
        }
        log.info("解析艺龙数据完成:" + coldata.toString());
        return issucc;
    }

    public void yiLongHistoryData(YiLongCollectData yiLongCollectData) {
        YiLongCollectData.data data = yiLongCollectData.getData();
        String targetDay = DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER);
        SuperviseBoxinfo boxinfo = superviseBoxinfoMapper.getBoxInfoByBno(yiLongCollectData.getDevID());

        SuperviseRegular superviseRegular = new SuperviseRegular();
        superviseRegular.setTargetHour(data.getTimes());
        superviseRegular.setCreateAt(new Date());
        superviseRegular.setTargetDay(targetDay);
        superviseRegular.setPcout(data.getCount());
        superviseRegular.setUuid(yiLongCollectData.getDevID());
        if (boxinfo != null) {
            superviseRegular.setMaId(boxinfo.getMaId());
        }
        superviseRegularMapper.insert(superviseRegular);
        log.info("存储一小时历史产量成功:[uuid:{}, count:{}, time:{}, date:{}]", yiLongCollectData.getDevID(), data.getCount(), data.getTimes(), yiLongCollectData.getTimes());
    }
}
