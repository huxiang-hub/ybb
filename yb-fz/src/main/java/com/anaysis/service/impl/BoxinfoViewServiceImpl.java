package com.anaysis.service.impl;

import com.anaysis.entity.BoxinfoViewEntity;
import com.anaysis.mysqlmapper.BoxinfoViewDAO;
import com.anaysis.service.BoxinfoViewService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BoxinfoViewServiceImpl implements BoxinfoViewService {
    @Resource
    private BoxinfoViewDAO boxinfoViewDAO;

    @Override
    public List<BoxinfoViewEntity> getList(){
        List<BoxinfoViewEntity> blist = boxinfoViewDAO.getlist();
        if(blist!=null && blist.size()>0)
            return blist;
        else
            return null;
    }
}
