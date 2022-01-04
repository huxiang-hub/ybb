package com.anaysis.executSupervise;

import com.anaysis.common.SpringUtil;
import com.anaysis.common.StringUtils;
import com.anaysis.dynamicData.datasource.DBIdentifier;
import com.anaysis.dynamicData.dbmgr.ProjectDBMgr;
import com.anaysis.entity.ExeDownCacheEntity;
import com.anaysis.entity.ExeQualityCacheEntity;
import com.anaysis.entity.ExeStatusChangeCacheEntity;
import com.anaysis.entity.ExecutProcessEntity;
import com.anaysis.executSupervise.entity.ExesetFault;
import com.anaysis.executSupervise.entity.ExesetQuality;
import com.anaysis.executSupervise.entity.SuperviseExecute;
import com.anaysis.executSupervise.mapper.SuperviseBoxinfoMapper;
import com.anaysis.executSupervise.service.ExesetFaultService;
import com.anaysis.executSupervise.service.ExesetQualityService;
import com.anaysis.executSupervise.service.SuperviseBoxinfoService;
import com.anaysis.executSupervise.service.SuperviseExecuteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ExecutConstant {

    public static SuperviseExecuteService executService = (SuperviseExecuteService) SpringUtil.getBean(SuperviseExecuteService.class);
    public static ExesetFaultService faultService = (ExesetFaultService) SpringUtil.getBean(ExesetFaultService.class);
    public static ExesetQualityService qualityService = (ExesetQualityService) SpringUtil.getBean(ExesetQualityService.class);
    public static SuperviseBoxinfoService boxInfoService = (SuperviseBoxinfoService) SpringUtil.getBean(SuperviseBoxinfoService.class);
    public static SuperviseBoxinfoMapper boxinfoMapper = (SuperviseBoxinfoMapper) SpringUtil.getBean(SuperviseBoxinfoMapper.class);

    //加载设备的开始信息数据信息【订单的状态信息：正式生产状态】
    public static Map<String, ExecutProcessEntity> machineProc = new HashMap<>();

    //停机缓存map
    public static Map<String, ExeDownCacheEntity> maDowncache = new HashMap<>();

    //状态变化缓存map
    public static Map<String, ExeStatusChangeCacheEntity> maStatusCache = new HashMap<>();

    //质检缓存map
    public static Map<String, ExeQualityCacheEntity> qualityCache = new HashMap();

    //盒子租户缓存map
    public static Map<String, String> boxTenantCache = new ConcurrentHashMap();


//    ExecutConstant() {
//        //初始化的时候需要判断状态表设备为正在运行的设备信息:正在运行为C状态 TODO WANG 设定执行正式生产的设备状态"C"
//        ProjectDBMgr.instance();
//        Map<String, String> dbNameMap = ProjectDBMgr.getDbNameMap();
//        dbNameMap.forEach((k, v) -> {
//            DBIdentifier.setProjectCode(k);
//            List<SuperviseExecute> ls = executService.getExecutListByStatus("C");
//            if (ls != null) {
//                for (SuperviseExecute execut : ls) {
//                    //数据库查询的信息进行循环，并且加入到缓存中；初始化进行处理，服务断时进行加载操作
//                    ExecutProcessEntity ex = new ExecutProcessEntity();
//                    ex.setMaId(execut.getMaId());//当前的设备id信息
//                    ex.setCurrNum(execut.getCurrNum());//当前状态的总计数信息
//                    ex.setModel(execut.getModel());//质量巡检类型
//                    ex.setLimitNum(execut.getLimitNum());//质量检测数量限制
//                    ex.setQualityTime(execut.getQualityTime());//质量检测时间限制
//                    ex.setStartTime(execut.getStartTime());//数据库设定的开始时间
//                    ex.setLimitTime(execut.getLimitTime());//弹窗限制时间
//                    ex.setSyslimitTime(execut.getSyslimitTime());//系统限制时间
//                    machineProc.put(execut.getUuid(), ex); //放入当前正在执行订单信息对象
//                }
//            }
//        });
//
//    }

    /*****
     * 设置设备的过程
     * @param uuid
     */
    public static ExecutProcessEntity setMachineProc(String uuid) {
        //如果当前设备已有缓存，登录的时候需要清除当前uuid之前的缓存。
        if (machineProc.get(uuid) != null) {
            machineProc.remove(uuid);
        }
        SuperviseExecute execut = executService.getExecutByBno(uuid);
        //停机弹窗设置条件
        ExesetFault fault = faultService.getFaultSetByUuid(uuid);
        //质检弹窗设置条件
        ExesetQuality quality = qualityService.getQualitySetByUuid(uuid);
        ExecutProcessEntity ex = new ExecutProcessEntity();
        ex.setMaId(execut.getMaId());//当前的设备id信息
        ex.setCurrNum(execut.getCurrNum());//当前状态的总计数信息

        if (quality == null) {
            //质检数据表中没有该数据uuid信息
            return null;
        }

        ex.setModel(quality.getModel());
        ex.setQualityTime(quality.getLimitTime());
        ex.setLimitNum(quality.getLimitNum());
        ex.setQualityDisapper(quality.getDisappear());//弹窗消失时间

        ex.setStartTime(execut.getStartTime());//设备停机开始时间
        ex.setLimitTime(fault.getLimitTime());//弹窗限制时间
        ex.setFaultDisapper(fault.getDisappear());//弹窗消失时间
        ex.setSyslimitTime(fault.getSyslimitTime());//系统限制时间
        machineProc.put(execut.getUuid(), ex); //放入当前正在执行订单信息对象
        return ex;
    }
}
