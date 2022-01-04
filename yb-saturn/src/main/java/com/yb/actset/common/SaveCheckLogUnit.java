package com.yb.actset.common;

import com.yb.actset.entity.ActsetCheckLog;
import com.yb.actset.entity.ActsetCkflow;
import com.yb.actset.mapper.ActsetCheckLogMapper;
import com.yb.actset.mapper.ActsetCkflowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class SaveCheckLogUnit {
    @Autowired
    private ActsetCheckLogMapper actsetCheckLogMapper;

    //单位毫秒    
    public static long TIMEUNIT = 1 * 60 * 1000;

    private static SaveCheckLogUnit saveCheckLogUnit;
    @PostConstruct
    private void init() {
        saveCheckLogUnit = this;
        saveCheckLogUnit.actsetCheckLogMapper = this.actsetCheckLogMapper;
    }

    /**
     * 每个事件需要待审核就得调用存方法，初始化一条待审核记录
     * @param asType
     * @param awType
     * @param orderId
     * @return
     */
    public synchronized static boolean saveCheckLog(String asType, String awType, Integer orderId) {
        //找到审核信息
        ActsetCkflow actsetCkflow =
                saveCheckLogUnit.actsetCheckLogMapper.getAwIdByType(asType,awType);
        if (actsetCkflow!=null) {
            //初始化审核记录方法
            ActsetCheckLog checkLog = new ActsetCheckLog();
            checkLog.setUsId(actsetCkflow.getUsId());//设置审核人
            checkLog.setStatus(StaticFinaly.CHECK_ING);//设置审核状态
            checkLog.setDbId(orderId);//设置被审核的主键 产品，订单...
            checkLog.setCreateAt(new Date());//设置审核时间
            checkLog.setAwId(actsetCkflow.getId());//设置审核流程主键
            saveCheckLogUnit.actsetCheckLogMapper.insert(checkLog);
            return true;
        }
        return false;
    }
}
