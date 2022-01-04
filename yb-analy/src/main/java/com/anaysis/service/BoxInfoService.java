package com.anaysis.service;

import com.anaysis.entity.BoxInfoEntity;
import com.anaysis.entity.ErpMachineEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoxInfoService {
    BoxInfoEntity getByMac(String mac);
    List<BoxInfoEntity> getList();
    int updateById(BoxInfoEntity boxInfoEntity);
    int saveOrUpdate(BoxInfoEntity boxInfoEntity);
    ErpMachineEntity getByBno(String bno);
}
