package com.anaysis.service.impl;

import com.anaysis.entity.BoxinfoViewEntity;
import com.anaysis.fz.mapper.BoxinfoViewMapper;
import com.anaysis.service.BoxinfoViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoxinfoViewServiceImpl implements BoxinfoViewService {
    @Autowired
    private BoxinfoViewMapper boxinfoViewDAO;

    @Override
    public List<BoxinfoViewEntity> getList(){
        List<BoxinfoViewEntity> blist = boxinfoViewDAO.getlist();
        if(blist!=null && blist.size()>0)
            return blist;
        else
            return null;
    }
}
