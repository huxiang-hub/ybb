package com.anaysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.anaysis.entity.HrMaterial;
import com.anaysis.sqlservermapper.HrMaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author lzb
 * @Date 2021/1/10
 **/
@Service
public class HrMaterialService {

    @Autowired
    private HrMaterialMapper hrMaterialMapper;

    public HrMaterial selectByBarCode(String barCode) {
        return hrMaterialMapper.selectByBarCode(barCode);
    }
}
