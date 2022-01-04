package com.anaysis.service.impl;

import com.anaysis.entity.BaseDeptinfo;
import com.anaysis.mysqlmapper.BaseDeptinfoMapper;
import com.anaysis.service.IBaseDeptinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.swing.UIManager.get;

/**
 * @author lzb
 * @date 2020-11-25
 */

@Service
public class BaseDeptinfoServiceImpl extends ServiceImpl<BaseDeptinfoMapper, BaseDeptinfo> implements IBaseDeptinfoService {

    @Autowired
    private BaseDeptinfoMapper deptinfoMapper;
    @Autowired
    private HrDeptService hrDeptService;

    @Override
    public void syn() {
        List<BaseDeptinfo> list;
        List<String> notImport = this.notImport();
        if (notImport.size() > 0) {
            list = hrDeptService.getByErpIds(notImport);
        } else {
            return;
        }
        Map<String, Integer> deptLink = deptLink();
        // 先同步最顶层
        List<BaseDeptinfo> collect = list.stream().filter(d -> "001".equals(d.getErpId())).collect(Collectors.toList());
        if (collect.size() > 0) {
            BaseDeptinfo deptinfo = collect.get(0);
            list.remove(deptinfo);
            deptinfo.setPId(0);
            deptinfo.setTenantId("nxhr");
            deptinfoMapper.insert(deptinfo);
            deptLink.put(deptinfo.getErpId(), deptinfo.getId());
        }
        saveDept(list, deptLink);
    }

    public void saveDept(List<BaseDeptinfo> list, Map<String, Integer> deptLink) {
        List<BaseDeptinfo> collect = list.stream().filter(d -> deptLink.containsKey(d.getErpPid())).collect(Collectors.toList());
        for (BaseDeptinfo deptinfo : collect) {
            deptinfo.setPId(deptLink.get(deptinfo.getErpPid()));
            deptinfo.setTenantId("nxhr");
            deptinfoMapper.insert(deptinfo);
            deptLink.put(deptinfo.getErpId(), deptinfo.getId());
        }
        list.removeAll(collect);
        if (list.size() > 0) {
            saveDept(list, deptLink);
        }
    }

    @Override
    public Map<String, Integer> deptLink() {
        List<BaseDeptinfo> depts = this.list();
        HashMap<String, Integer> deptMap = new HashMap<>();
        for (BaseDeptinfo dept : depts) {
            deptMap.put(dept.getErpId(), dept.getId());
        }
        return deptMap;
    }

    public List<String> notImport() {
        List<String> myErpIds = deptinfoMapper.getErpIds();
        List<String> hrErpIds = hrDeptService.getAllErpId();
        List<String> notImport = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        return notImport;
    }
}