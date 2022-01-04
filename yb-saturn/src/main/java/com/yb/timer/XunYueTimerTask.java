package com.yb.timer;

import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.mapper.SuperviseBoxinfoMapper;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.xunyue.mapper.ExecuteFormalcMapper;
import com.yb.xunyue.mapper.ExecutePreparebMapper;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.core.tool.utils.Func;
import org.springblade.message.feign.XyClient;
import org.springblade.system.feign.XunYueClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author my
 * #Description
 */

@Slf4j
@Component
@EnableScheduling
public class XunYueTimerTask {

    /**
     * 1分钟一次
     */
    private final String CRON = "0 */1 * * * ?";
    @Autowired
    private XunYueClient xunYueClient;

    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;

    @Autowired
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;

    @Autowired
    private ExecuteFormalcMapper executeFormalcMapper;

    @Autowired
    private ExecutePreparebMapper executePreparebMapper;

    /**
     * 更新讯越erp设备实时信息 暂时1分钟一次
     */
   // @Scheduled(cron = CRON)
    public void task() {
        log.info("开始执行讯越定时任务");

        DBIdentifier.setProjectCode("xunyue");

        List<SuperviseBoxinfo> superviseBoxinfos = superviseBoxinfoMapper.selectList(null);
        if (!superviseBoxinfos.isEmpty()) {
            for (SuperviseBoxinfo superviseBoxinfo : superviseBoxinfos) {
                SuperviseExecute executeOrder = superviseExecuteMapper.getExecuteOrder(superviseBoxinfo.getMaId());
                Integer wStatus = null;
                if (executeOrder.getExeStatus().equals(GlobalConstant.ProType.BEFOREPRO_STATUS.getType())) {
                    wStatus = 1;
                } else if (executeOrder.getExeStatus().equals(GlobalConstant.ProType.INPRO_STATUS.getType())) {
                    wStatus = 2;
                }else{
                    wStatus=3;
                }
                if (Func.isNotBlank(superviseBoxinfo.getWbNo())) {
                    xunYueClient.updateBoxNum(superviseBoxinfo.getMaId(), superviseBoxinfo.getWbNo(), executeOrder.getExId(), superviseBoxinfo.getNumber(), superviseBoxinfo.getStatus(), wStatus, BigDecimal.valueOf(superviseBoxinfo.getDspeed()), executeOrder.getStartTime());
                    int count = 0;
                    if (wStatus!=null&&wStatus == 1) {
                        count = executePreparebMapper.getCount(executeOrder.getExId());
                        xunYueClient.update(superviseBoxinfo.getMaId(), superviseBoxinfo.getWbNo(), wStatus, count);
                    } else if (wStatus!=null&&wStatus == 2) {
                        count = executeFormalcMapper.getCount(executeOrder.getExId());
                        xunYueClient.update(superviseBoxinfo.getMaId(), superviseBoxinfo.getWbNo(), wStatus, count);
                    }
                }
            }
        }
        log.info("讯越定时任执行完成");
    }
}
