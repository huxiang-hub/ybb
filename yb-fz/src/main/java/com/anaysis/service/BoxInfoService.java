package com.anaysis.service;

import com.anaysis.entity.BoxInfoEntity;
import com.anaysis.entity.ErpMachineEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoxInfoService {
    List<BoxInfoEntity> getList();
}
