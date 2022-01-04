package com.anaysis.service.impl;

import com.anaysis.entity.*;
import com.anaysis.mysqlmapper.MachineMainfoMapper;
import com.anaysis.mysqlmapper.ProcessWorkinfoMapper;
import com.anaysis.mysqlmapper.WorkbatchOrdlinkMapper;
import com.anaysis.service.IProcessWorkinfoService;
import com.anaysis.service.IProdPdinfoService;
import com.anaysis.service.IWorkbatchOrdlinkService;
import com.anaysis.sqlservermapper.HrWorkbatchOrdlinkMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lzb
 * @Date 2020/11/25 11:23
 **/
@Service
public class WorkbatchOrdlinkServiceImpl extends ServiceImpl<WorkbatchOrdlinkMapper, WorkbatchOrdlink> implements IWorkbatchOrdlinkService {
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private HrWorkbatchOrdlinkMapper hrWorkbatchOrdlinkMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private ProcessWorkinfoMapper processWorkinfoMapper;
    @Autowired
    private IProcessWorkinfoService processWorkinfoService;
    @Autowired
    private IProdPdinfoService prodPdinfoService;


    public List<String> getErpId() {
        return workbatchOrdlinkMapper.getErpId();
    }


    /**
     * 同步排产单（同步排产单之前先同步设备和工序）
     */
    @Override
    public void sync() {
        List<WorkbatchOrdlink> workbatchOrdlinks;
        List<String> notImport = this.notImport();
        if (notImport.size() > 0) {
            workbatchOrdlinks = hrWorkbatchOrdlinkMapper.getByErpIds(notImport);
        } else {
            return;
        }
        List<ProdPdinfo> pdinfos = prodPdinfoService.list(Wrappers.<ProdPdinfo>lambdaQuery().select(ProdPdinfo::getErpId,ProdPdinfo::getId));
        Map<String, Integer> pdMap = pdinfos.stream().collect(Collectors.toMap(ProdPdinfo::getErpId, ProdPdinfo::getId, (k1, k2) -> k2));
        List<ProcessWorkinfo> processWorkinfos = processWorkinfoMapper.selectList(null);
        Map<String, Integer> maIdMap = machineMainfoMapper.selectList(null).stream().collect(Collectors.toMap(MachineMainfo::getErpId, MachineMainfo::getId, (k1, k2) -> k2));
        Map<String, Integer> prIdMap = processWorkinfos.stream().collect(Collectors.toMap(ProcessWorkinfo::getPrName, ProcessWorkinfo::getId, (k1, k2) -> k2));
        Map<String, String> bomProcess = processWorkinfoService.bomProcess();
        ArrayList<WorkbatchOrdlink> list = new ArrayList<>();
        for (WorkbatchOrdlink workbatchOrdlink : workbatchOrdlinks) {
            String size = getSize(workbatchOrdlink.getLength(), workbatchOrdlink.getWidth(), workbatchOrdlink.getHeight());
            workbatchOrdlink.setPdSize(size);
            workbatchOrdlink.setOperateSize(size);
            workbatchOrdlink.setStatus("-1");
            workbatchOrdlink.setPrRoute(bomProcess.get(workbatchOrdlink.getPdCode()));
            workbatchOrdlink.setMaId(maIdMap.get(workbatchOrdlink.getErpMaId()));
            workbatchOrdlink.setPrId(prIdMap.get(shuiYinJiaoYinCheck(workbatchOrdlink)));
            workbatchOrdlink.setPrName(shuiYinJiaoYinCheck(workbatchOrdlink));
            workbatchOrdlink.setRunStatus(0);
            workbatchOrdlink.setWbNo(workbatchOrdlink.getOdNo());
            workbatchOrdlink.setPdId(pdMap.get(workbatchOrdlink.getErpPdId()));
            workbatchOrdlink.setIncompleteNum(workbatchOrdlink.getPlanNum());
            workbatchOrdlink.setArrangeNum(workbatchOrdlink.getPlanNum());
            list.add(workbatchOrdlink);
        }
        ArrayList<WorkbatchOrdlink> collect = list.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() ->
                        new TreeSet<WorkbatchOrdlink>(Comparator.comparing(WorkbatchOrdlink::getErpId))), ArrayList::new));
        saveBatch(collect);
    }

    /**
     * 水印胶印工序判断
     * @param workbatchOrdlink
     * @return 返回工序名，如果是印刷工序，当工单号包含S时代表水印
     */
    public String shuiYinJiaoYinCheck(WorkbatchOrdlink workbatchOrdlink) {
        if ("印刷".equals(workbatchOrdlink.getPrName())) {
            if (workbatchOrdlink.getOdNo().contains("S")) {
                return "水印";
            }
        }
        return workbatchOrdlink.getPrName();
    }


    @Override
    public List<WorkbatchOrdlink> notImportWorkPlan(List<String> workOrderNotImport) {
        if (workOrderNotImport.size() > 0) {
            return hrWorkbatchOrdlinkMapper.getByErpIds(workOrderNotImport);
        } else {
            return null;
        }
    }

    /**
     * @return 返回未导入排产单erp的ObjID
     */
    public List<String> notImport() {
        List<String> myErpIds = workbatchOrdlinkMapper.getAllErpIds();
        List<String> yieldErpIds = workbatchOrdlinkMapper.getAllNoYieldErpIds();
        List<String> hrErpIds = hrWorkbatchOrdlinkMapper.getAllErpIds();
        List<String> notImports = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        List<String> delList1 = myErpIds.stream().filter(o -> !hrErpIds.contains(o)).collect(Collectors.toList());
        List<String> collect2 = delList1.stream().filter(o -> !yieldErpIds.contains(o)).collect(Collectors.toList());
        if (!collect2.isEmpty()) {
            workbatchOrdlinkMapper.delete(Wrappers.<WorkbatchOrdlink>lambdaQuery().in(WorkbatchOrdlink::getErpId, collect2));
        }
        return notImports;
    }

    /**
     * 检查erp拆分情况并更新拆分的工单
     */
    private void checkUpdate() {
        List<WorkbatchOrdlink> list = workbatchOrdlinkMapper.selectList(Wrappers.<WorkbatchOrdlink>lambdaQuery().select(WorkbatchOrdlink::getErpId, WorkbatchOrdlink::getPlanNum));
        List<String> erpIds = list.stream().map(WorkbatchOrdlink::getErpId).collect(Collectors.toList());
        List<WorkbatchOrdlink> splitWorkOrds = hrWorkbatchOrdlinkMapper.getSplit(erpIds);
        for (WorkbatchOrdlink splitWorkOrd : splitWorkOrds) {
            WorkbatchOrdlink w = workbatchOrdlinkMapper.selectOne(Wrappers.<WorkbatchOrdlink>lambdaQuery().eq(WorkbatchOrdlink::getErpId, splitWorkOrd.getErpId()));
            if (!w.getPlanNum().equals(splitWorkOrd.getPlanNum())) {
                w.setPlanNum(splitWorkOrd.getPlanNum());
                w.setPlanNumber(splitWorkOrd.getPlanNum());
                w.setExtraNum(splitWorkOrd.getExtraNum());
                workbatchOrdlinkMapper.updateById(w);
            }
        }
    }

    /**
     * @param l 长
     * @param w 宽
     * @param h 高
     * @return 根据长宽高返回尺寸, 如：l:1,w:2,h:3 -----> 1*2*3
     */
    private String getSize(Double l, Double w, Double h) {
        Integer length = null;
        Integer width = null;
        Integer height = null;
        String size = null;
        if (null != l) {
            length = l.intValue();
        }
        if (null != w) {
            width = w.intValue();
        }
        if (null != h) {
            height = h.intValue();
        }
        if (null != length && null != height && null != width) {
            size = length + "*" + width + "*" + height;
        } else if (null != length && null != width) {
            size = length + "*" + width;
        }
        return size;
    }

}
