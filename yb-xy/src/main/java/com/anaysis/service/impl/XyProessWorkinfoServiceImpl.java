package com.anaysis.service.impl;

import com.anaysis.entity.ProcessWorkinfoVO;
import com.anaysis.service.XyProessWorkinfoService;
import com.anaysis.sqlservermapper.XyProessWorkinfoDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XyProessWorkinfoServiceImpl implements XyProessWorkinfoService {

    private XyProessWorkinfoDao XyProessWorkinfoDao;
    @Override
    public List<ProcessWorkinfoVO> list() {
//        查询所有工序
        return XyProessWorkinfoDao.list();
    }
}
