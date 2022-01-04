package com.anaysis.service.impl;

import com.anaysis.entity.BomProcess;
import com.anaysis.entity.ProcessClassify;
import com.anaysis.entity.ProcessClasslink;
import com.anaysis.mysqlmapper.ProcessWorkinfoMapper;
import com.anaysis.service.IProcessClassifyService;
import com.anaysis.service.IProcessClasslinkService;
import com.anaysis.sqlservermapper.HrProcessMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anaysis.entity.ProcessWorkinfo;
import com.anaysis.service.IProcessWorkinfoService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lzb
 * @date 2020-11-25
 */

@Service
public class ProcessWorkinfoServiceImpl extends ServiceImpl<ProcessWorkinfoMapper, ProcessWorkinfo> implements IProcessWorkinfoService {

    @Autowired
    private ProcessWorkinfoMapper processWorkinfoMapper;
    @Autowired
    private HrProcessService hrProcessService;
    @Autowired
    private IProcessClassifyService processClassifyService;
    @Autowired
    private IProcessClasslinkService processClasslinkService;
    @Autowired
    private HrProcessMapper hrProcessMapper;

    @Override
    public void syn() {
        List<String> myErpIds = processWorkinfoMapper.getAllErps();
        List<String> hrErpIds = new ArrayList<>();
        List<String> delErpIds = myErpIds.stream().filter(o -> !hrErpIds.contains(o)).collect(Collectors.toList());
        List<String> addErpIds = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        List<ProcessWorkinfo> processWorkinfos = hrProcessService.getByErpIds(addErpIds);
        for (ProcessWorkinfo processWorkinfo : processWorkinfos) {
            processWorkinfoMapper.insert(processWorkinfo);
            ProcessClassify processClassify = new ProcessClassify();
            // 保存工序类型
            processClassifyService.save(processClassify);
            ProcessClasslink processClasslink = new ProcessClasslink();
            processClasslink.setPrId(processWorkinfo.getId());
            processClasslink.setPyId(processClassify.getId());
            // 保存工序与工序类型关联表
            processClasslinkService.save(processClasslink);
        }
    }

    /**
     * 同步工序
     */
    @Override
    public void sync() {
        List<String> addPrNames = this.notImport();
        for (String prName : addPrNames) {
            ProcessWorkinfo processWorkinfo = new ProcessWorkinfo();
            processWorkinfo.setPrName(prName);
            processWorkinfoMapper.insert(processWorkinfo);
        }
    }

    @Override
    public ProcessWorkinfo getByErpId(String erpId) {
        return processWorkinfoMapper.getByErpId(erpId);
    }

    @Override
    public List<ProcessWorkinfo> getByErpIds(List<String> processErpIds) {
        return processWorkinfoMapper.getByErpIds(processErpIds);
    }

    public List<String> notImport() {
        List<String> myErpPrName = processWorkinfoMapper.getAllErps();
        List<String> hrErpPrName = hrProcessMapper.getAllErps();
        List<String> notImports = hrErpPrName.stream().filter(o -> !myErpPrName.contains(o)).collect(Collectors.toList());
        return notImports;
    }

    /**
     * 查询产品编号和工序路线关系
     * @return 【key: 产品bom，value：工序路线 a-->b-->c】
     */
    @Override
    public Map<String, String> bomProcess() {
        List<BomProcess> list = hrProcessMapper.getBomProcess();
        Map<String, List<BomProcess>> collect = list.parallelStream().collect(Collectors.groupingBy(BomProcess::getBom));
        HashMap<String, String> map = new HashMap<>();
        collect.forEach((k, v) -> {
            Stream<BomProcess> sorted = v.stream().sorted(Comparator.comparing(BomProcess::getProcessNo));
            StringBuilder process = new StringBuilder();
            sorted.forEach((b) -> {
                if (!"".equals(process.toString())) {
                    process.append("-->").append(b.getProcessCode());
                }else {
                    process.append(b.getProcessCode());
                }
            });
            map.put(k, process.toString());
        });
        return map;
    }



}