package com.anaysis.service.impl;

import com.anaysis.entity.BoxInfoEntity;
import com.anaysis.entity.ErpMachineEntity;
import com.anaysis.fz.mapper.BoxInfoMapper;
import com.anaysis.service.BoxInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoxInfoServiceImpl implements BoxInfoService {
    @Autowired
    private BoxInfoMapper boxInfoMapper;

    @Override
    public BoxInfoEntity getByMac(String mac) {
        List<BoxInfoEntity> blist = boxInfoMapper.getbymac(mac);
        if(blist!=null && blist.size()>0)
            return blist.get(0);
        else
            return null;
    }

    @Override
    public List<BoxInfoEntity> getList(){
        List<BoxInfoEntity> blist = boxInfoMapper.getlist();
        if(blist!=null && blist.size()>0) {
            return blist;
        }else {
            return null;
        }
    }

    @Override
    public int updateById(BoxInfoEntity boxInfoEntity){
        return boxInfoMapper.update(boxInfoEntity);
    }

    @Override
    public int saveOrUpdate(BoxInfoEntity boxInfoEntity){
        if (boxInfoEntity.getId() == null) {
            return boxInfoMapper.save(boxInfoEntity);
        }else{
            //获取历史数据后进行速度计算信息

            return boxInfoMapper.update(boxInfoEntity);
        }
    }

    @Override
    public ErpMachineEntity getByBno(String bno){
        return boxInfoMapper.getByBno(bno);
    }
}
