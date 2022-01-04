package com.anaysis.service.impl;

import com.anaysis.entity.ProcessWorkinfo;
import com.anaysis.service.FzProessWorkinfoService;
import com.anaysis.sqlservermapper.FzProessWorkinfoDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FzProessWorkinfoServiceImpl implements FzProessWorkinfoService {

    private FzProessWorkinfoDao fzProessWorkinfoDao;
    @Override
    public List<ProcessWorkinfo> list() {
//        查询所有工序
        return fzProessWorkinfoDao.list();
    }
}
