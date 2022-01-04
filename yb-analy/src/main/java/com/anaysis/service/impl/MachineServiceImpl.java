package com.anaysis.service.impl;

import com.anaysis.entity.MachineEntity;
import com.anaysis.executSupervise.entity.ClearZeroEntity;
import com.anaysis.mongodb.MongoDbs;
import com.anaysis.service.MachineService;
import com.anaysis.socket1.BladeData;
import com.anaysis.socket1.CollectData;
import com.anaysis.socket1.ConmachRs;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class MachineServiceImpl implements MachineService {
    @Autowired
    @Qualifier(value = "primaryMongoTemplate")
    protected MongoTemplate mongoTemplate;
    @Resource
    @Qualifier(value = "fuliTemplate")
    protected MongoTemplate fuliMongoTemplate;
    @Resource
    @Qualifier(value = "baofengMongoTemplate")
    protected MongoTemplate baofengMongoTemplate;
    @Resource
    @Qualifier(value = "xingyiMongoTemplate")
    protected MongoTemplate xingyiMongoTemplate;
    @Resource
    @Qualifier(value = "hbhrMongoTemplate")
    protected MongoTemplate hbhrMongoTemplate;
    @Resource
    @Qualifier(value = "nxjsjMongoTemplate")
    protected MongoTemplate nxjsjMongoTemplate;
    @Resource
    @Qualifier(value = "demoMongoTemplate")
    protected MongoTemplate demoMongoTemplate;
    @Resource
    @Qualifier(value = "nxhrMongoTemplate")
    protected MongoTemplate nxhrMongoTemplate;

    @Override
    public boolean addMachine(String tenantId, MachineEntity machineEntity) {
        //判断数据源
        if (machineEntity.getB_id() == "" || machineEntity.getB_id() == null)
            return false;
        try {
            return getMongoTemplate(tenantId).save(machineEntity) != null;
        } catch (Exception e) {
            //写入mongdb数据出现异常，表示网络断开，所以缓存保存数据信息到本地mysql数据库中
            e.printStackTrace();
        }
        return false;
    }

    /****
     * 新增加基础数据写入功能，记录即时数据信息
     * */
    @Override
    public boolean addBasedb(String tenantId, CollectData coldata) {
        if (coldata.getMid() == "" || coldata.getMid() == null)
            return false;
        try {
            return getMongoTemplate(tenantId).save(coldata) != null;
        } catch (Exception e) {
            //写入mongdb数据出现异常，表示网络断开，所以缓存保存数据信息到本地mysql数据库中
            log.info("write-error。uid：" + coldata.getMid() + "::error-msg:" + e.getMessage());
        }
        return false;
    }


    /******
     * 设定对应的换版记录数据信息内容
     * @param bladeData
     * @return
     */
    @Override
    public boolean addBladeData(BladeData bladeData) {
        if (bladeData.getMid() == "" || bladeData.getMid() == null)
            return false;
        try {
            return mongoTemplate.save(bladeData) != null;
        } catch (Exception e) {
            //写入mongdb数据出现异常，表示网络断开，所以缓存保存数据信息到本地mysql数据库中
            log.info("write-error。uid：" + bladeData.getMid() + "::error-msg:" + e.getMessage());
        }
        return false;
    }
    /******
     * 设定对应的换版记录数据信息内容
     * @param conmachrs
     * @return
     */
    @Override
    public boolean addConmachRs(ConmachRs conmachrs) {
        if (conmachrs.getUuid() == "" || conmachrs.getMsg() == null)
            return false;
        try {
            return mongoTemplate.save(conmachrs) != null;
        } catch (Exception e) {
            //写入mongdb数据出现异常，表示网络断开，所以缓存保存数据信息到本地mysql数据库中
            log.info("write-error。uid：" + conmachrs.getUuid() + "::error-msg:" + e.getMessage());
        }
        return false;
    }

    /*****
     * 清零操作记录管理
     * @param clearZeroEntity
     * @return
     */
    @Override
    public boolean clearZero(ClearZeroEntity clearZeroEntity) {
        if (clearZeroEntity.getB_id() == "" || clearZeroEntity.getB_id() == null)
            return false;
        try {
            return getMongoTemplate(clearZeroEntity.getB_id()).save(clearZeroEntity) != null;
        } catch (Exception e) {
            //写入mongdb数据出现异常，表示网络断开，所以缓存保存数据信息到本地mysql数据库中
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据租户id获取mongodb模板
     *
     * @param tenantId
     * @return
     */
    MongoTemplate getMongoTemplate(String tenantId) {
        String str = MongoDbs.map.get(tenantId);
        if (Func.isBlank(str)) {
            return mongoTemplate;
        }
        switch (str) {
            case ("baofeng"):
                return baofengMongoTemplate;
            case ("fuli"):
                return fuliMongoTemplate;
            case ("xingyi"):
                return xingyiMongoTemplate;
            case ("hbhr"):
                return hbhrMongoTemplate;
            case ("nxjsj"):
                return nxjsjMongoTemplate;
            case ("demo"):
                return demoMongoTemplate;
            case("nxhr"):
                return nxhrMongoTemplate;
            default:
                return mongoTemplate;
        }
    }
}
