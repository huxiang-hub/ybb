package com.anaysis.service.impl;

import com.anaysis.entity.MaterClassfiy;
import com.anaysis.mysqlmapper.MaterClassfiyMapper;
import com.anaysis.mysqlmapper.MaterMtinfoMapper;
import com.anaysis.sqlservermapper.HrMaterMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anaysis.entity.MaterMtinfo;
import com.anaysis.service.IMaterMtinfoService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzb
 * @date 2020-11-30
 */

@Service
public class MaterMtinfoServiceImpl extends ServiceImpl<MaterMtinfoMapper, MaterMtinfo> implements IMaterMtinfoService {

    @Autowired
    private MaterMtinfoMapper materMtinfoMapper;
    @Autowired
    private MaterClassfiyMapper materClassfiyMapper;
    @Autowired
    private HrMaterMapper hrMaterMapper;

    @Override
    public void syn() {
        List<String> myErpIds = materMtinfoMapper.getAllErpIds();
        List<String> hrErpIds = hrMaterMapper.getAllErpIds();
        List<String> addErpIds = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        List<MaterMtinfo> materMtinfos = hrMaterMapper.getByErpIds(addErpIds);
        for (MaterMtinfo materMtinfo : materMtinfos) {
            MaterClassfiy materClassfiy = hrMaterMapper.getMaterClassfiy(materMtinfo.getErpId());
            MaterClassfiy mc = materClassfiyMapper.getByErpIds(materClassfiy.getErpId());
            if (null == mc) { // 不存在则保存物料分类
                materClassfiyMapper.insert(materClassfiy);
                materMtinfo.setMcId(materClassfiy.getId());
            } else { // 存在则物料与物料分类关联
                materMtinfo.setMcId(mc.getId());
            }
            materMtinfoMapper.insert(materMtinfo);
        }
    }

    @Override
    public MaterMtinfo getByErpId(String erpId) {
        return null;
    }
}