package com.anaysis.feign;


import com.anaysis.common.DeptUtil;
import com.anaysis.config.MsgConfig;
import com.anaysis.entity.*;
import com.anaysis.entity.json.JsonsRootBean;
import com.anaysis.entity.json.Leftstyle;
import com.anaysis.entity.json.Linelist;
import com.anaysis.entity.json.Nodelist;
import com.anaysis.mysqlmapper.*;
import com.anaysis.sqlservermapper.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.message.feign.XyClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/6/12 17:22
 */
@RestController
@RequestMapping("/xy")
@Slf4j
public class XyApi implements XyClient {
    @Autowired
    private XySaUserMapper xySaUserMapper;
    @Autowired
    private SaUserMapper saUserMapper;
    @Autowired
    private BaseDeptInfoMapper baseDeptInfoMapper;
    @Autowired
    private XyDeptMapper xyDeptMapper;
    @Autowired
    private BaseStaffinfoMapper baseStaffinfoMapper;
    @Autowired
    private XyStaffinfoMapper xyStaffinfoMapper;
    @Autowired
    private BaseStaffextMapper baseStaffextMapper;
    @Autowired
    private XyStaffextMapper xyStaffextMapper;
    @Autowired
    private XyProessWorkinfoDao xyProessWorkinfoMapper;
    @Autowired
    private ProessWorkinfoMapper proessWorkinfoMapper;
    @Autowired
    private ProessClasslinkMapper proessClasslinkMapper;
    @Autowired
    private ProcessClassifyMapper processClassifyMapper;
    @Autowired
    private XyCrmCustomerDao xyCrmCustomerMapper;
    @Autowired
    private CrmCustomerMapper crmCustomerMapper;
    @Autowired
    private XyOrderOrdinfoDao xyOrderOrdinfoMapper;
    @Autowired
    private ProdClassifyMapper prodClassifyMapper;
    @Autowired
    private MaterProdlinkMapper materProdlinkMapper;
    @Autowired
    private MaterMtinfoMapper materMtinfoMapper;
    @Autowired
    private MaterClassfiyMapper materClassfiyMapper;
    @Autowired
    private ProdPdinfoMapper prodPdinfoMapper;
    @Autowired
    private ProdPartsinfoMapper prodPartsinfoMapper;
    @Autowired
    private ProdProcelinkMapper prodProcelinkMapper;
    @Autowired
    private XyMachineDao xyMachineDao;
    @Autowired
    private MachineClassifyMapper machineClassifyMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private ProcessMachlinkMapper processMachlinkMapper;
    @Autowired
    private XyWorkBatchMapper xyWorkBatchMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private WorkbatchOrdoeeMapper workbatchOrdoeeMapper;
    @Autowired
    private WorkbatchExpcutMapper workbatchExpcutMapper;
    @Autowired
    private WorkbatchExpprintMapper workbatchExpprintMapper;
    @Autowired
    private WorkbatchProgressMapper workbatchProgressMapper;
    @Autowired
    private HBCeShiMapper hbCeShiMapper;
    @Autowired
    private DingClient dingClient;
    //????????????
    private Integer ptLeft = 100;
    private Integer ptTop = 10;
    private Integer mtLeft = 20;
    private Integer mtTop = 10;
    private Integer prLeft = 200;
    private Integer prTop = 10;

    @Override
    @GetMapping("/updateBladeUser")
    @Transactional
    public R updateBladeUser() {
        try {
            //??????bladeuser
            saveFzBladeUser();
            //??????staffInfo
            saveFzStaffInfo();
            //??????staffext
            saveStaffext();
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// ??????????????????????????????
            massage(stackTraceElement);
            return R.fail("????????????");
        }
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateMachine")
    public R updateMachine() {
//        ??????????????????/??????
        List<MachineClassify> classifyList = xyMachineDao.getMachineClassify();
        List<MachineClassify> classifies = machineClassifyMapper.selectList(null);

        if (!classifies.isEmpty()) {
            //??????????????????????????????/??????
            List<String> erpIds = classifies.stream().map(MachineClassify::getErpId).collect(Collectors.toList());
            classifyList = classifyList.stream().filter(o -> {
                return !erpIds.contains(o.getErpId());
            }).collect(Collectors.toList());
            if (!classifyList.isEmpty()) {
                classifyList.forEach(o -> {
                    machineClassifyMapper.insert(o);
                });
            }
        } else {
            classifyList.forEach(o -> {
                machineClassifyMapper.insert(o);
            });
        }

        List<MachineMainfoVO> machineMainfos = xyMachineDao.getMachineList();
        if (!Func.isEmpty(machineMainfos)) {
            for (MachineMainfoVO machineMainfovo : machineMainfos) {
                try {
                    Map<String, Object> machineMap = new HashMap<>();
                    machineMap.put("erp_id", machineMainfovo.getErpId());
                    if (Func.isEmpty(machineMainfoMapper.selectByMap(machineMap))) {
                        MachineMainfo machineMainfo = new MachineMainfo();
                        machineMainfo.setMno(machineMainfovo.getMno());
                        machineMainfo.setName(machineMainfovo.getName());
                        machineMainfo.setErpId(machineMainfovo.getErpId());
                        machineMainfo.setIsRecepro(0);//????????????????????????
                        machineMainfo.setMtId(machineClassifyMapper.getByerpId(machineMainfovo.getMtErp()));//??????????????????
                        machineMainfo.setDpId(baseDeptInfoMapper.getByDpNum(machineMainfovo.getDpNum()));//????????????????????????
                        //??????????????????
                        List<ProcessMachlinkVO> processMachlinks = xyMachineDao.getProcessMachlink(machineMainfovo.getErpId());
                        machineMainfo.setProId(!Func.isEmpty(processMachlinks) ? proessWorkinfoMapper.getByerpId(processMachlinks.get(0).getPrErp()) : null);
//                        ??????????????????
                        machineMainfoMapper.insert(machineMainfo);
                        if (!Func.isEmpty(processMachlinks)) {
                            for (ProcessMachlinkVO processMachlinkvo : processMachlinks) {
                                ProcessMachlink processMachlink = new ProcessMachlink();
                                processMachlink.setMaId(machineMainfo.getId());
                                processMachlink.setPrId(proessWorkinfoMapper.getByerpId(processMachlinkvo.getPrErp()));
                                processMachlink.setSpeed(processMachlinkvo.getSpeed());
                                if (processMachlinkvo.getPrepareTime() == null) {
                                    processMachlink.setPrepareTime(0);
                                } else {
                                    processMachlink.setPrepareTime(processMachlinkvo.getPrepareTime());
                                }
                                processMachlinkMapper.insert(processMachlink);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        List<MachineMainfo> list = machineMainfoMapper.selectList(null);
        list.stream().forEach(machineMainfo -> {
            //??????????????????
            List<ProcessMachlinkVO> processMachlinks = xyMachineDao.getProcessMachlink(machineMainfo.getErpId());
            processMachlinks.stream().forEach(processMachlinkVO -> {
                Map<String, Object> map = new HashMap<>();
                map.put("ma_id", machineMainfo.getId());
                Integer prId = proessWorkinfoMapper.getByerpId(processMachlinkVO.getPrErp());
                map.put("pr_id", prId);
                if (Func.isEmpty(processMachlinkMapper.selectByMap(map))) {
//                        ??????????????????
                    ProcessMachlink processMachlink = new ProcessMachlink();
                    processMachlink.setMaId(machineMainfo.getId());
                    processMachlink.setPrId(prId);
                    processMachlink.setSpeed(processMachlinkVO.getSpeed());
                    processMachlink.setPrepareTime(processMachlinkVO.getPrepareTime());
                    processMachlinkMapper.insert(processMachlink);
                }
            });
        });
        return R.success("ok");
    }

    @Override
    public R updateProcessMachlink() {
        return null;
    }


    @Override
    @GetMapping("/updateProcessWorkinfo")
    @Transactional
    public R updateProcessWorkinfo() {
        try {
            //        ??????????????????
            List<ProcessClassify> classifies = xyProessWorkinfoMapper.getClassifyList();
            List<ProcessClassify> classifys = processClassifyMapper.selectList(null);
            if (!Func.isEmpty(classifys)) {
                List<String> erpIds = classifys.stream().map(ProcessClassify::getErpId).collect(Collectors.toList());
                classifies = classifies.stream().filter(o -> {
                    return !erpIds.contains(o.getErpId());
                }).collect(Collectors.toList());
                classifies.forEach(o -> {
                    processClassifyMapper.insert(o);
                });
            } else {
                classifies.forEach(o -> {
                    processClassifyMapper.insert(o);
                });
            }
            //        ????????????
            List<ProcessWorkinfoVO> list = xyProessWorkinfoMapper.list();
            List<ProcessWorkinfo> workinfos = proessWorkinfoMapper.selectList(null);
            if (!Func.isEmpty(workinfos)) {
                List<String> erpIds = workinfos.stream().map(ProcessWorkinfo::getErpId).collect(Collectors.toList());
                list = list.stream().filter(o -> {
                    return !erpIds.contains(o.getErpId());
                }).collect(Collectors.toList());
                list.forEach(o -> {
                    //????????????
                    ProcessWorkinfo processWorkinfo = new ProcessWorkinfo();
                    processWorkinfo.setErpId(o.getErpId());
                    processWorkinfo.setPrName(o.getPrName());
                    processWorkinfo.setPrNo(o.getPrNo());
                    processWorkinfo.setStatus(o.getStatus());
                    processWorkinfo.setIsdel(o.getIsdel());
                    proessWorkinfoMapper.insert(processWorkinfo);
                    //        ????????????????????????
                    ProcessClassify processClassify = processClassifyMapper.getClassifypyNum(o.getPyNum());
                    ProcessClasslink processClasslink = new ProcessClasslink();
                    if (!Func.isEmpty(processClassify)) {
                        processClasslink.setPrId(processWorkinfo.getId());
                        processClasslink.setPyId(processClassify.getId());
                        //                    ????????????????????????
                        proessClasslinkMapper.insert(processClasslink);
                    }
                });
            } else {
                list.forEach(o -> {
                    //????????????
                    ProcessWorkinfo processWorkinfo = new ProcessWorkinfo();
                    processWorkinfo.setErpId(o.getErpId());
                    processWorkinfo.setPrName(o.getPrName());
                    processWorkinfo.setPrNo(o.getPrNo());
                    processWorkinfo.setStatus(o.getStatus());
                    processWorkinfo.setIsdel(o.getIsdel());
                    proessWorkinfoMapper.insert(processWorkinfo);
                    //        ????????????????????????
                    ProcessClassify processClassify = processClassifyMapper.getClassifypyNum(o.getPyNum());
                    ProcessClasslink processClasslink = new ProcessClasslink();
                    if (!Func.isEmpty(processClassify)) {
                        processClasslink.setPrId(processWorkinfo.getId());
                        processClasslink.setPyId(processClassify.getId());
                        //                    ????????????????????????
                        proessClasslinkMapper.insert(processClasslink);
                    }
                });
            }

            // ???????????????????????????
            List<MaterClassfiy> materClassfiyList = xyOrderOrdinfoMapper.getMaterClassify();
            List<MaterClassfiy> materClassfiys = materClassfiyMapper.selectList(null);
            if (!Func.isEmpty(materClassfiys)) {
                List<String> erpIds = materClassfiys.stream().map(MaterClassfiy::getErpId).collect(Collectors.toList());
                materClassfiyList = materClassfiyList.stream().filter(o -> {
                    return !erpIds.contains(o.getErpId());
                }).collect(Collectors.toList());
                materClassfiyList.forEach(o -> {
                    materClassfiyMapper.insert(o);
                });
            } else {
                materClassfiyList.forEach(o -> {
                    materClassfiyMapper.insert(o);
                });
            }

            List<MaterMtinfoVO> materMtinfoVOList = xyOrderOrdinfoMapper.getMaterMtinfo();
            List<MaterMtinfo> materMtinfos = materMtinfoMapper.selectList(null);

            if (!Func.isEmpty(materMtinfos)) {
                List<String> erpIds = materMtinfos.stream().map(MaterMtinfo::getErpId).collect(Collectors.toList());
                materMtinfoVOList = materMtinfoVOList.stream().filter(o -> {
                    return !erpIds.contains(o.getErpId());
                }).collect(Collectors.toList());
                materMtinfoVOList.forEach(o -> {
                    MaterMtinfo materMtinfo = new MaterMtinfo();
                    o.setMcId(materClassfiyMapper.getByerpId(o.getMcErp()));
                    BeanUtils.copyProperties(o, materMtinfo);
                    materMtinfoMapper.insert(materMtinfo);
                });
            } else {
                materMtinfoVOList.forEach(o -> {
                    MaterMtinfo materMtinfo = new MaterMtinfo();
                    o.setMcId(materClassfiyMapper.getByerpId(o.getMcErp()));
                    BeanUtils.copyProperties(o, materMtinfo);
                    materMtinfoMapper.insert(materMtinfo);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// ??????????????????????????????
            massage(stackTraceElement);
            return R.fail("????????????");
        }
        return R.success("??????????????????????????????!");
    }

    @Override
    @GetMapping("/updateCrmCustomer")
    public R updateCrmCustomer() {
        try {
            //        ?????? ????????????
            List<CrmCustomer> list = xyCrmCustomerMapper.list();
            List<CrmCustomer> crm = crmCustomerMapper.selectList(null);
            if (!Func.isEmpty(crm)) {
                List<String> erpIds = crm.stream().map(CrmCustomer::getErpId).collect(Collectors.toList());
                list = list.stream().filter(o -> {
                    return !erpIds.contains(o.getErpId());
                }).collect(Collectors.toList());
                list.stream().forEach(crmCustomer -> {
                    crmCustomerMapper.insert(crmCustomer);
                });
            } else {
                list.stream().forEach(crmCustomer -> {
                    crmCustomerMapper.insert(crmCustomer);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// ??????????????????????????????
            massage(stackTraceElement);
            return R.fail("????????????");
        }
        return R.success("?????????????????????");
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @return
     */
    @Override
    @GetMapping("/updateOrderInfo")
    public R updateOrderInfo() {
        return R.success("??????????????????");
    }

    @Override
    @GetMapping("/updatePdClassify")
    public R updatePdClassify() {
        try {
            //        ??????????????????
            List<ProdClassify> prodClassifyList = xyOrderOrdinfoMapper.getProdClassify();
            //        ??????????????????
            if (!Func.isEmpty(prodClassifyList)) {
                prodClassifyList.stream().forEach(prodClassify -> {
                    Map<String, Object> prodClassMap = new HashMap<>();
                    prodClassMap.put("erp_id", prodClassify.getErpId());
                    if (Func.isEmpty(prodClassifyMapper.selectByMap(prodClassMap))) {
                        prodClassifyMapper.insert(prodClassify);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// ??????????????????????????????
            massage(stackTraceElement);
            return R.fail("????????????");
        }
        return R.success("????????????????????????");
    }

    /**
     * ?????? ???????????????????????????
     *
     * @return
     */
//    @Override
    @GetMapping("/updateWorkbatch1")
    public R updateWorkbatch1(Integer maId) {
        try {
//            ????????????
            updateOrdlink();
            List<WorkbatchOrdlinkVO> workbatchOrdlinkList = new ArrayList<>();
            if (maId != null && maId > 0) {
                // ????????????id????????????????????????id??????
                MachineMainfo machine = machineMainfoMapper.selectById(maId);
                workbatchOrdlinkList = xyWorkBatchMapper.listByerpId(machine.getErpId());
            } else {
                // ??????id???????????????????????????erpId
                List<MachineMainfo> machines = machineMainfoMapper.selectList(null);
//                workbatchOrdlinkList = xyWorkBatchMapper.listByIGzzxId(machines);//sql todo
            }
            // ??????????????? tod
            List<WorkbatchOrdlink> workbatchs = workbatchOrdlinkMapper.selectList(null);
            if (!Func.isEmpty(workbatchs)) {
                //???????????????????????????
                List<String> erpIds = workbatchs.stream().map(WorkbatchOrdlink::getErpId).collect(Collectors.toList());
                workbatchOrdlinkList = workbatchOrdlinkList.stream().filter(o -> {
                    return !erpIds.contains(o.getErpId());
                }).collect(Collectors.toList());

                if (!workbatchOrdlinkList.isEmpty()) { // erp?????????????????????????????????????????????
                    workbatchOrdlinkList.stream().forEach(o -> {
                        try {
                            newWorkbatchordlink(o);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } else {
                if (!workbatchOrdlinkList.isEmpty()) {
                    workbatchOrdlinkList.stream().forEach(o -> {
                        try {
                            newWorkbatchordlink(o);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// ??????????????????????????????
            massage(stackTraceElement);
            return R.fail("????????????");
        }

        return R.success("??????????????????");
    }

    /**
     * @param maId
     * @return
     * @author lzb
     */
    @Override
    @GetMapping("/updateWorkbatch")
    public R updateWorkbatch(Integer maId) {
        List<String> xyIds;
        List<String> myIds;
        List<String> maIds = new ArrayList<>();
        try {
            if (maId != null && maId > 0) {
                // ???????????????????????????
                String m = maId.toString();
                MachineMainfo machineMainfo = machineMainfoMapper.selectById(m);
                maIds.add(machineMainfo.getErpId());
                myIds = workbatchOrdlinkMapper.getErpIdByMaId(m);
            } else {
                // ????????????
                maIds = machineMainfoMapper.getErpIds();
                myIds = workbatchOrdlinkMapper.getErpId();
            }
            List<String> xyAllIds = xyWorkBatchMapper.getAllIdsByMaIds(maIds);
            xyIds = xyWorkBatchMapper.getIdsByMaIds(maIds);
            List<String> finalMyIds = myIds;
            List<String> addIds = xyIds.stream().filter(i -> !finalMyIds.contains(i)).collect(Collectors.toList());
            List<String> moreIds = myIds.stream().filter(i -> !xyAllIds.contains(i)).collect(Collectors.toList());
            // ?????????????????????
            deleteWorkbatch(moreIds);
            List<WorkbatchOrdlinkVO> workbatchOrdlinkList = new ArrayList<>();
            // list?????????1000??????
            Integer MAX_NUMBER = 1000;
            int limit = (addIds.size() + MAX_NUMBER - 1) / MAX_NUMBER;
            List<List<String>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> addIds.stream().skip(a * MAX_NUMBER).limit(MAX_NUMBER).parallel().collect(Collectors.toList())).collect(Collectors.toList());
            for (List<String> ids : splitList) {
                workbatchOrdlinkList.addAll(xyWorkBatchMapper.getByIds(ids));
            }
            log.info("=========?????????????????????{}???==============", workbatchOrdlinkList.size());
            if (!workbatchOrdlinkList.isEmpty()) { // erp?????????????????????????????????????????????
                workbatchOrdlinkList.forEach(o -> {
                    try {
                        newWorkbatchordlink(o);
                    } catch (Exception e) {
                        dingClient.send("??????erpId??????" + o.getErpId() + "???????????????????????????????????????" + e.getMessage(), "InternalH5");
                        log.error("=======??????????????????erpId??????{}=======\n???????????????{}", o.getErpId(), e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// ??????????????????????????????
            massage(stackTraceElement);
            return R.fail("????????????");
        }
        return R.success("??????????????????");
    }


    /**
     * ?????????????????????
     * 1??????????????????????????????shift????????????????????????10???????????????????????????????????????????????????????????????shift????????????
     * 2?????????????????????????????????
     *
     * @param erpIds
     */
    private void deleteWorkbatch(List<String> erpIds) {
        if (erpIds.size() > 0) {
            for (String erpId : erpIds) {
                List<WorkbatchOrdlink> list = workbatchOrdlinkMapper.selectByErpId(erpId);
                if (null != list && list.size() > 0) {
                    for (WorkbatchOrdlink w : list) {
                        List<Integer> shiftIds = workbatchOrdlinkMapper.selectWorbatchShift(w.getId());
                        if (null != shiftIds && shiftIds.size() > 0) {
                            w.setStatus("10");
                            workbatchOrdlinkMapper.updateById(w);
                        } else {
                            workbatchOrdlinkMapper.deleteById(w.getId());
                        }
                    }
                }
            }
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????id???
     *
     * @return
     */
//    @Override
    @GetMapping("/noImport")
    public R noImport() {
        return R.data(noImportXyData());
    }

    /**
     * ??????erpId??????????????????????????????????????????????????????????????????????????????
     *
     * @param erpId
     * @return
     */
    @GetMapping("/updateByErpId")
    public R updateByErpId(String erpId) {
        WorkbatchOrdlinkVO workbatchOrdlinkVO = xyWorkBatchMapper.selectOneByErpId(erpId);
        if (null == workbatchOrdlinkVO) {
            return R.fail("??????????????????");
        }
        try {
            newWorkbatchordlink(workbatchOrdlinkVO);
            return R.success("????????????");
        } catch (ParseException e) {
            log.info("==========??????erpId???{}??????????????????\n{}===============", erpId, e.getMessage());
            e.printStackTrace();
        }
        return R.fail("????????????");
    }

    /**
     * ??????????????????xy?????????ids
     *
     * @return
     */
    public List<String> noImportXyData() {
        List<String> maIds = machineMainfoMapper.getErpIds();
        List<String> xyIds = xyWorkBatchMapper.getIdsByMaIds(maIds);
        List<String> myIds = workbatchOrdlinkMapper.getErpId();
        xyIds.removeAll(myIds);
        return xyIds;
    }


    /**
     * ????????????
     */
    private void updateOrdlink() {
        // ??????????????????-1???????????????????????????
        List<det> list = workbatchOrdlinkMapper.selectByStatusReturnErpId("-1");
        if (!list.isEmpty()) {
            // ?????????????????????erpId
            List<MachineMainfo> machines = machineMainfoMapper.selectList(null);
            // ??????erp????????????erpId
            List<String> erpIds = xyWorkBatchMapper.erpAll(machines);//sql
            list = list.stream().filter(o -> {
                return !erpIds.contains(o.getErpId());
            }).collect(Collectors.toList());
            List<Integer> ids = list.stream().map(p -> p.getId()).collect(Collectors.toList());
            // ???????????????
            if (!Func.isEmpty(ids)) {
                workbatchOrdlinkMapper.deleteBatchIds(ids);
            }
        }

    }


//    private void workbatchordlink (WorkbatchOrdlinkVO o) {
////                ????????????????????????
//            if (Func.isEmpty(orderOrdinfoMapper.getByOdNo(o.getOdNo()))) {
////                    ??????????????????
//                OrderOrdinfoVO orderOrdinfovo = xyOrderOrdinfoMapper.listByOdNo(o.getOdNo());
//                orderOrdinfovo.setLimitDate(o.getCloseTime());
////                    ???????????????????????????????????????
//                if (!Func.isEmpty(prodPdinfoMapper.getByerpId(o.getPdErp()))) {
//                        ProdPdinfo prodPdinfo = prodPdinfoMapper.getByerpId(o.getPdErp());
//                        Map<String,Object> partMap = new HashMap<>();
//                        partMap.put("pd_id",prodPdinfo.getId());
//                        orderOrdinfovo.setPdId(prodPdinfo.getId());
//                        List<ProdPartsinfo> partsinfoList = prodPartsinfoMapper.selectByMap(partMap);
//                        partsinfoList.stream().forEach(parts -> {
//                            if (o.getPartErp() == null || o.getPartErp().equals("")) {
//                                if (parts.getErpId().equals(o.getPdErp())) {
////                                    ??????????????????
//                                    ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getOneProdPartsinfo(o.getPdErp1(),o.getPdErp());
//                                    if (!Func.isEmpty(prodPartsinfoVO)){
//                                        o.setPtSize(prodPartsinfoVO.getPtSize());
//                                    }
//                                    o.setPtId(parts.getId());
//                                    o.setPtNo(parts.getPtNo());
//                                }
//                            }else {
//                                if (parts.getErpId().equals(o.getPartErp())) {
////                                    ??????????????????
//                                    ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getOneProdPartsinfo(o.getPdErp1(),o.getPartErp());
//                                    if (!Func.isEmpty(prodPartsinfoVO)){
//                                        o.setPtSize(prodPartsinfoVO.getPtSize());
//                                    }
//                                    o.setPtId(parts.getId());
//                                    o.setPtNo(parts.getPtNo());
//                                }
//                            }
//                            Map<String, Object> processMap = new HashMap<>();
//                            processMap.put("pt_id", parts.getId());
//                            List<ProdProcelink> procelinkList = prodProcelinkMapper.selectByMap(processMap);
//                            procelinkList.forEach(procelink -> {
//                                ProcessWorkinfo processWorkinfo = proessWorkinfoMapper.selectById(procelink.getPrId());
//                                if (o.getPartErp() == null || o.getPartErp().equals("")) {
//                                    if ((!Func.isEmpty(processWorkinfo)) && o.getPdErp().equals(processWorkinfo.getErpId())) {
//                                        o.setPrId(processWorkinfo.getId());
//                                    }
//                                }else {
//                                    if ((!Func.isEmpty(processWorkinfo)) && o.getPrErp().equals(processWorkinfo.getErpId())) {
//                                        o.setPrId(processWorkinfo.getId());
//                                    }
//                                }
//                            });
//                            if (o.getPrId() == null) {
//                                ProdProcelink prodlink = new ProdProcelink();
//                                prodlink.setSortNum(99);
//                                prodlink.setPtId(o.getPtId());
//                                prodlink.setPrId(proessWorkinfoMapper.getByerpId("999999"));
//                                prodlink.setDiffLevel(0.0);
//                                prodlink.setWasteRate(0.0);
//                                prodlink.setIsUsed(1);
//                                prodProcelinkMapper.insert(prodlink);
//                                o.setPrId(proessWorkinfoMapper.getByerpId("999999"));
//                            }
//                        });
//                }else {
////                        ??????????????????(??????)
//                    ProdPdinfoVO pdinfoVO = xyOrderOrdinfoMapper.getProdPdinfo(o.getPdCode());
////                        ????????????
//                    ProdClassify prodClassify = prodClassifyMapper.getByerpId(pdinfoVO.getPcErp());
//                    ProdPdinfo pdinfo = new ProdPdinfo();
//                    pdinfo.setPdNo(pdinfoVO.getPdNo());
//                    pdinfo.setPdName(pdinfoVO.getPdName());
//                    pdinfo.setErpId(pdinfoVO.getErpId());
//                    if (!Func.isEmpty(prodClassify)) {
//                        pdinfo.setPcId(prodClassify.getId());
//                    }
////                        ????????????
//                    prodPdinfoMapper.insert(pdinfo);
//                    orderOrdinfovo.setPdId(pdinfo.getId());
////                        ???????????????????????????????????????????????????????????????????????????
////                        ????????????
//                    List<ProdPartsinfoVO> prodPartsinfoVOS = xyOrderOrdinfoMapper.getProdPartsinfo(o.getPdCode());
////                        ????????????????????????
//                    if (!Func.isEmpty(prodPartsinfoVOS)) {
//                        prodPartsinfoVOS.stream().forEach(prodPartsinfoVO -> {
////                            if (o.getPartErp().equals(prodPartsinfoVO.getErpId())) {
////                                ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
////                                BeanUtils.copyProperties(prodPartsinfoVO, prodPartsinfo);
////                                prodPartsinfo.setPdId(pdinfo.getId());
////                                prodPartsinfoMapper.insert(prodPartsinfo);
////                                o.setPtSize(prodPartsinfoVO.getPtSize());
////                                o.setPtId(prodPartsinfo.getId());
////                                o.setPtNo(prodPartsinfo.getPtNo());
////
////                                List<MaterProdlinkVO> materProdlinkVOS = xyOrderOrdinfoMapper.getMaterProdlinkByAPComp(o.getErpId());
////                                List<MaterProdlinkVO> materProdlinkVOSFL = xyOrderOrdinfoMapper.getMaterProdlinkByAPCompFL(o.getErpId());
////                                if (!Func.isEmpty(materProdlinkVOS)) {
////                                    materProdlinkVOS.stream().forEach(materProdlinkVO -> {
////                                    MaterProdlink materProdlink = new MaterProdlink();
////                                    BeanUtils.copyProperties(materProdlinkVO, materProdlink);
////                                    materProdlink.setPdId(prodPartsinfo.getId());
////                                    materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
////                                    materProdlinkMapper.insert(materProdlink);
////                                        MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
////                                        if (!Func.isEmpty(materMtinfo)) {
////                                            o.setMaterialName(materMtinfo.getMlName());
////                                    }
////                                    });
////                                }
////                                if (!Func.isEmpty(materProdlinkVOSFL)) {
////                                    materProdlinkVOSFL.stream().forEach(materProdlinkVO -> {
////                                        MaterProdlink materProdlink = new MaterProdlink();
////                                        BeanUtils.copyProperties(materProdlinkVO, materProdlink);
////                                        materProdlink.setPdId(prodPartsinfo.getId());
////                                        materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
////                                        materProdlinkMapper.insert(materProdlink);
////                                        if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
////                                            MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
////                                            if (!Func.isEmpty(materMtinfo)) {
////                                                o.setIngredientName(materMtinfo.getMlName());
////                                            }
////                                        }
////                                    });
////                                }
////
//////                          ?????????????????????
////                                List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getProdProcelink(o.getPartErp());
////                                if (!Func.isEmpty(prodProcelinkVOS)) {
////                                    int a = 0;
////                                    for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
////                                        ProdProcelink prodProcelink = new ProdProcelink();
////                                        BeanUtils.copyProperties(prodProcelinkVO, prodProcelink);
////                                        prodProcelink.setPtId(prodPartsinfo.getId());
////                                        prodProcelink.setSortNum(a++);
////                                        prodProcelink.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
////                                        prodProcelinkMapper.insert(prodProcelink);
////                                        if (o.getPrErp().equals(prodProcelinkVO.getPrErp())) {
////                                            o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
////                                        }
////                                    }
////                                }
////                            }
//                            ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
//                            BeanUtils.copyProperties(prodPartsinfoVO, prodPartsinfo);
//                            prodPartsinfo.setPdId(pdinfo.getId());
//                            prodPartsinfoMapper.insert(prodPartsinfo);
//                            if (o.getPartErp().equals(prodPartsinfoVO.getErpId())) {
//                                o.setPtSize(prodPartsinfoVO.getPtSize());
//                                o.setPtId(prodPartsinfo.getId());
//                                o.setPtNo(prodPartsinfo.getPtNo());
//                            }
////                          ???????????????
//                            List<MaterProdlinkVO> materProdlinkVOS = xyOrderOrdinfoMapper.getMaterProdlinkByAPComp(o.getErpId());
//                            List<MaterProdlinkVO> materProdlinkVOSFL = xyOrderOrdinfoMapper.getMaterProdlinkByAPCompFL(o.getErpId());
//                            if (!Func.isEmpty(materProdlinkVOS)) {
//                                materProdlinkVOS.stream().forEach(materProdlinkVO -> {
//                                    MaterProdlink materProdlink = new MaterProdlink();
//                                    BeanUtils.copyProperties(materProdlinkVO, materProdlink);
//                                    materProdlink.setPdId(prodPartsinfo.getId());
//                                    materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
//                                    materProdlinkMapper.insert(materProdlink);
//                                    if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
//                                        MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                                        if (!Func.isEmpty(materMtinfo)) {
//                                            o.setMaterialName(materMtinfo.getMlName());
//                                        }
//                                    }
//                                });
//                            }
//                            if (!Func.isEmpty(materProdlinkVOSFL)) {
//                                materProdlinkVOSFL.stream().forEach(materProdlinkVO -> {
//                                    MaterProdlink materProdlink = new MaterProdlink();
//                                    BeanUtils.copyProperties(materProdlinkVO, materProdlink);
//                                    materProdlink.setPdId(prodPartsinfo.getId());
//                                    materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
//                                    materProdlinkMapper.insert(materProdlink);
//                                    if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
//                                        MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                                        if (!Func.isEmpty(materMtinfo)) {
//                                            o.setIngredientName(materMtinfo.getMlName());
//                                        }
//                                    }
//                                });
//                            }
////                          ?????????????????????
//                            List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getProdProcelink(o.getPartErp());
//                            if (!Func.isEmpty(prodProcelinkVOS)) {
//                                int a = 0;
//                                int b = 0;
//                                for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
//                                    if(b == 0 && prodProcelinkVO.getLb() == 2) {
////                                        ??????????????????
//                                        ProdProcelink prodProcelink = new ProdProcelink();
//                                        prodProcelink.setDiffLevel(0.0);
//                                        prodProcelink.setWasteRate(0.0);
//                                        prodProcelink.setIsUsed(1);
//                                        prodProcelink.setPtId(prodPartsinfo.getId());
//                                        prodProcelink.setSortNum(a++);
//                                        prodProcelink.setPrId(proessWorkinfoMapper.getByerpId("999999"));
//                                        prodProcelinkMapper.insert(prodProcelink);
//                                        b = 1;
//                                        if (o.getPartErp().equals(prodPartsinfo.getErpId()) && o.getPrErp().equals("999999")) {
//                                            o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                        }
//                                    }
//                                    ProdProcelink prodProcelink = new ProdProcelink();
//                                    BeanUtils.copyProperties(prodProcelinkVO, prodProcelink);
//                                    prodProcelink.setPtId(prodPartsinfo.getId());
//                                    prodProcelink.setSortNum(a++);
//                                    prodProcelink.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                    prodProcelinkMapper.insert(prodProcelink);
//                                    if (o.getPartErp().equals(prodPartsinfo.getErpId()) && o.getPrErp().equals(prodProcelinkVO.getPrErp())) {
//                                        o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                    }
//                                }
//                            }
//                        });
//                    }
////                    ????????????????????????????????????????????????
////                      ?????????????????????????????????
//                    if ((xyOrderOrdinfoMapper.getPdw(o.getWbNo())+ xyOrderOrdinfoMapper.getPdy(o.getWbNo())) > 0) {
////                        ????????????????????????
//                        ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
//                        prodPartsinfo.setErpId(o.getPdErp());
//                        prodPartsinfo.setPtName("??????");
//                        prodPartsinfo.setPtClassify(1);
//                        prodPartsinfo.setPdType(1);
//                        prodPartsinfo.setPdId(pdinfo.getId());
//                        prodPartsinfoMapper.insert(prodPartsinfo);
//                        if (o.getPartErp() == null || o.getPartErp() == "") {
//                            o.setPtSize(o.getPtSize());
//                            o.setPtId(prodPartsinfo.getId());
//                            o.setPtNo(prodPartsinfo.getPtNo());
//                        }
////                        ?????????????????????
//                        List<MaterProdlinkVO>  materList = xyOrderOrdinfoMapper.getMaterProdlinkByPd(o.getWbNo());
//                        List<MaterProdlinkVO>  materFLList = xyOrderOrdinfoMapper.getMaterProdlinkByPdFL(o.getWbNo());
//                        if (!Func.isEmpty(materList)) {
//                            materList.stream().forEach(materProdlinkVO -> {
//                                MaterProdlink materProdlink = new MaterProdlink();
//                                BeanUtils.copyProperties(materProdlinkVO, materProdlink);
//                                materProdlink.setPdId(prodPartsinfo.getId());
//                                materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
//                                materProdlinkMapper.insert(materProdlink);
//                                if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
//                                    MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                                    if (!Func.isEmpty(materMtinfo)) {
//                                        o.setMaterialName(materMtinfo.getMlName());
//                                    }
//                                }
//                            });
//                        }
//                        if (!Func.isEmpty(materFLList)) {
//                            materFLList.stream().forEach(materProdlinkVO -> {
//                                MaterProdlink materProdlink = new MaterProdlink();
//                                BeanUtils.copyProperties(materProdlinkVO, materProdlink);
//                                materProdlink.setPdId(prodPartsinfo.getId());
//                                materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
//                                materProdlinkMapper.insert(materProdlink);
//                                if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
//                                    MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                                    if (!Func.isEmpty(materMtinfo)) {
//                                        o.setIngredientName(materMtinfo.getMlName());
//                                    }
//                                }
//                            });
//                        }
////                        ?????????????????????
//                        List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getlinkW(o.getWbNo());
//                        List<ProdProcelinkVO> prodProcelinkVOSY = xyOrderOrdinfoMapper.getlinkY(o.getWbNo());
//                        if(Func.isEmpty(prodProcelinkVOS)){
//                            prodProcelinkVOS = new ArrayList<>();
//                        }
//                        prodProcelinkVOS.addAll(prodProcelinkVOSY);
//                        if (!Func.isEmpty(prodProcelinkVOS)) {
//                            int a = 0;
//                            for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
//                                ProdProcelink prodProcelink = new ProdProcelink();
//                                BeanUtils.copyProperties(prodProcelinkVO, prodProcelink);
//                                prodProcelink.setPtId(prodPartsinfo.getId());
//                                prodProcelink.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                prodProcelinkMapper.insert(prodProcelink);
//                                if (o.getPrErp().equals(prodProcelinkVO.getPrErp())) {
//                                    o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                }
//                            }
//                        }
//                    }
//                }
////                   ????????????
//                OrderOrdinfo orderOrdinfo = new OrderOrdinfo();
//                BeanUtils.copyProperties(orderOrdinfovo, orderOrdinfo);
//                orderOrdinfo.setOdCount(o.getOdCount());
//                CrmCustomer crmCustomer = crmCustomerMapper.getByerpId(o.getCmErp());
//                if (!Func.isEmpty(crmCustomer)) {
//                    orderOrdinfo.setCmId(crmCustomer.getId());
//                    orderOrdinfo.setCmShortname(crmCustomer.getCmShortname());
//                    o.setCmName(crmCustomer.getCmShortname());
//                }
//                orderOrdinfo.setUsId(saUserMapper.getUserIdByerpId(o.getUsErp()));
//                orderOrdinfoMapper.insert(orderOrdinfo);
//                ProdProcelink link = new ProdProcelink();
////                    ???????????????????????????
////                    ???????????????
//                ActsetCkflow actsetCkflow = actsetCkflowMapper.selectById(15);
////                    ??????????????????
//                ActsetCheckLog actsetCheckLog = new ActsetCheckLog();
//                actsetCheckLog.setAwId(15);
//                actsetCheckLog.setDbId(orderOrdinfo.getId());
//                actsetCheckLog.setUsId(actsetCkflow.getUsId());
//                actsetCheckLog.setStatus(1);//????????????????????????
//                actsetCheckLogMapper.insert(actsetCheckLog);
////                    ?????????
//                OrderWorkbatchVO orderWorkbatchVO = xyOrderOrdinfoMapper.getOrderWorkbatch(o.getWbNo());
//                OrderWorkbatch orderWorkbatch = new OrderWorkbatch();
//                BeanUtils.copyProperties(orderWorkbatchVO,orderWorkbatch);
//                if (orderWorkbatchVO.getOdNo() != null && orderWorkbatchVO.getOdNo() != ""){
//                    OrderOrdinfo orderOrdinfo1 = orderOrdinfoMapper.getByOdNo(orderWorkbatchVO.getOdNo());
//                    if (!Func.isEmpty(orderOrdinfo1)){
//                        orderWorkbatch.setOdId(orderOrdinfo1.getId());
//                    }
//                }
//                orderWorkbatch.setUserId(saUserMapper.getUserIdByerpId(orderWorkbatchVO.getUsErp()));
//                OrderWorkbatch w = orderWorkbatchMapper.getByerpId(o.getWbNo());
//                if (Func.isEmpty(w)) {
//                    orderWorkbatchMapper.insert(orderWorkbatch);
//                    o.setWbId(orderWorkbatch.getId());
//                    link.setWbId(orderWorkbatch.getId());
//                    link.setWbNo(orderWorkbatch.getBatchNo());
////                    if (o.getPtId() == null) {
//////                            ????????????????????????
////                        prodProcelinkMapper.insert(link);
////                    }
//                }else{
//                    o.setWbId(w.getId());
//                    link.setWbId(w.getId());
//                    link.setWbNo(w.getBatchNo());
////                    if (o.getPtId() == null) {
//////                            ????????????????????????
////                        prodProcelinkMapper.insert(link);
////                    }
//                }
//                if(o.getPtId() != null){
//                    WorkbatchOrdlink workbatchOrdlink = new WorkbatchOrdlink();
//                    BeanUtils.copyProperties(o,workbatchOrdlink);
//                    workbatchOrdlink.setCmName(orderOrdinfo.getCmName());
//                    workbatchOrdlink.setPdType(prodClassifyMapper.getByerpId(o.getTypeErp()).getClName());
//                    MachineMainfo machineMainfo = machineMainfoMapper.getByerpId(o.getMaErp());
//                    workbatchOrdlink.setDpId(machineMainfo.getDpId());
//                    workbatchOrdlink.setMaId(machineMainfo.getId());
////                        ??????????????????
//                    MachineClassify machineClassify = machineClassifyMapper.selectById(machineMainfo.getMtId());
//                    workbatchOrdlink.setUsId(saUserMapper.getUserIdByerpId(o.getUsErp()));
//                    workbatchOrdlinkMapper.insert(workbatchOrdlink);
//                    if (machineClassify.getModel().equals("????????????")) {
////                            ???????????????
//                        WorkBatchExpprint workBatchExpprint = new WorkBatchExpprint();
//                        workBatchExpprint.setSdId(workbatchOrdlink.getId());
//                        workBatchExpprint.setColorNum(o.getColorNum());
//                        workBatchExpprint.setCtpNo(o.getCtpNo());
//                        workBatchExpprint.setCtpTime(o.getCtpTime());
//                        workBatchExpprint.setPaintColour(o.getPaintColour());
//                        workBatchExpprint.setVersioncClass(o.getVersioncClass());
//                        workbatchExpprintMapper.insert(workBatchExpprint);
//                    }else if (machineClassify.getModel().equals("????????????")) {
////                            ?????????????????????
//                        WorkBatchExpcut workBatchExpcut = new WorkBatchExpcut ();
//                        workBatchExpcut.setSdId(workbatchOrdlink.getId());
//                        workBatchExpcut.setCutNo(o.getCtpNo());
//                        workbatchExpcutMapper.insert(workBatchExpcut);
//                    }
//
//                    WorkbatchOrdoee workbatchOrdoee = new WorkbatchOrdoee();
//                    workbatchOrdoee.setWkId(workbatchOrdlink.getId());
//                    workbatchOrdoee.setBeforePtid(workbatchOrdlink.getPtId());
//                    workbatchOrdoee.setBeforePtname(workbatchOrdlink.getPartName());
//                    workbatchOrdoee.setBeforePtno(workbatchOrdlink.getPtNo());
////                        workbatchOrdoee.setMaintain(1);
////                        workbatchOrdoee.setMaintainStay(0);
////                        workbatchOrdoee.setMaintainNum(1);
//                    workbatchOrdoee.setMouldStay(0);
//                    workbatchOrdoee.setMouldNum(1);
////                        workbatchOrdoee.setMealStay(0);
////                        workbatchOrdoee.setMealNum(1);
////                    workbatchOrdoee.setQualityNum(1);
//                    workbatchOrdoee.setDifficultNum(1.0);
//                    workbatchOrdoee.setProducePreTime(0);
//                    workbatchOrdoee.setPlanTime(o.getPlanTime());
//                    workbatchOrdoee.setErpSpeed(o.getErpSpeed());
//                    workbatchOrdoeeMapper.insert(workbatchOrdoee);
//                }
//            }else {
////                    ??????
////                    ????????????????????????
//                OrderWorkbatch orderWorkbatch = orderWorkbatchMapper.getByerpId(o.getWbNo());
//                ProdProcelink link = new ProdProcelink();
//                if (Func.isEmpty(orderWorkbatch)) {
////                            ???????????????????????????
//                    OrderWorkbatchVO orderWorkbatchVO = xyOrderOrdinfoMapper.getOrderWorkbatch(o.getWbNo());
//                    OrderWorkbatch workbatch = new OrderWorkbatch();
//                    BeanUtils.copyProperties(orderWorkbatchVO,workbatch);
//                    if (orderWorkbatchVO.getOdNo() != null && orderWorkbatchVO.getOdNo() != ""){
//                        OrderOrdinfo orderOrdinfo1 = orderOrdinfoMapper.getByOdNo(orderWorkbatchVO.getOdNo());
//                        if (!Func.isEmpty(orderOrdinfo1)){
//                            workbatch.setOdId(orderOrdinfo1.getId());
//                        }
//                    }
//                    workbatch.setUserId(saUserMapper.getUserIdByerpId(orderWorkbatchVO.getUsErp()));
//                    orderWorkbatchMapper.insert(workbatch);
//                    o.setWbId(workbatch.getId());
//                    link.setWbId(workbatch.getId());
//                    link.setWbNo(workbatch.getBatchNo());
//                }else {
//                    o.setWbId(orderWorkbatch.getId());
//                    link.setWbId(orderWorkbatch.getId());
//                    link.setWbNo(orderWorkbatch.getBatchNo());
//                }
////                        ????????????
//                ProdPdinfo prodPdinfos = prodPdinfoMapper.getByerpId(o.getPdErp());
//                if (!Func.isEmpty(prodPdinfos)) {
//                    ProdPdinfo prodPdinfo = prodPdinfoMapper.getByerpId(o.getPdErp());
//                    Map<String,Object> partMap = new HashMap<>();
//                    partMap.put("pd_id",prodPdinfo.getId());
//                    List<ProdPartsinfo> partsinfoList = prodPartsinfoMapper.selectByMap(partMap);
//                    partsinfoList.stream().forEach(parts -> {
//                        if (o.getPartErp() == null || o.getPartErp().equals("")) {
//                            if (parts.getErpId().equals(o.getPdErp())) {
////                                    ??????????????????
//                                ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getOneProdPartsinfo(o.getPdErp1(),o.getPdErp());
//                                if (!Func.isEmpty(prodPartsinfoVO)){
//                                    o.setPtSize(prodPartsinfoVO.getPtSize());
//                                }
//                                o.setPtId(parts.getId());
//                                o.setPtNo(parts.getPtNo());
//                            }
//                        }else {
//                            if (parts.getErpId().equals(o.getPartErp())) {
////                                    ??????????????????
//                                ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getOneProdPartsinfo(o.getPdErp1(),o.getPartErp());
//                                if (!Func.isEmpty(prodPartsinfoVO)){
//                                    o.setPtSize(prodPartsinfoVO.getPtSize());
//                                }
//                                o.setPtId(parts.getId());
//                                o.setPtNo(parts.getPtNo());
//                            }
//                        }
//                        Map<String, Object> processMap = new HashMap<>();
//                        processMap.put("pt_id", parts.getId());
//                        List<ProdProcelink> procelinkList = prodProcelinkMapper.selectByMap(processMap);
//                        procelinkList.forEach(procelink -> {
//                            ProcessWorkinfo processWorkinfo = proessWorkinfoMapper.selectById(procelink.getPrId());
//                            if (o.getPartErp() == null || o.getPartErp().equals("")) {
//                                if ((!Func.isEmpty(processWorkinfo)) && o.getPdErp().equals(processWorkinfo.getErpId())) {
//                                    o.setPrId(processWorkinfo.getId());
//                                }
//                            }else {
//                                if ((!Func.isEmpty(processWorkinfo)) && o.getPrErp().equals(processWorkinfo.getErpId())) {
//                                    o.setPrId(processWorkinfo.getId());
//                                }
//                            }
//                        });
//                        if (o.getPrId() == null) {
//                            ProdProcelink prodlink = new ProdProcelink();
//                            prodlink.setSortNum(99);
//                            prodlink.setPtId(o.getPtId());
//                            prodlink.setPrId(proessWorkinfoMapper.getByerpId("999999"));
//                            prodlink.setDiffLevel(0.0);
//                            prodlink.setWasteRate(0.0);
//                            prodlink.setIsUsed(1);
//                            prodProcelinkMapper.insert(prodlink);
//                            o.setPrId(proessWorkinfoMapper.getByerpId("999999"));
//                        }
//                    });
//                }else {
////                        ??????????????????(??????)
//                    ProdPdinfoVO pdinfoVO = xyOrderOrdinfoMapper.getProdPdinfo(o.getPdCode());
////                        ????????????
//                    ProdClassify prodClassify = prodClassifyMapper.getByerpId(pdinfoVO.getPcErp());
//                    ProdPdinfo pdinfo = new ProdPdinfo();
//                    pdinfo.setPdNo(pdinfoVO.getPdNo());
//                    pdinfo.setPdName(pdinfoVO.getPdName());
//                    pdinfo.setErpId(pdinfoVO.getErpId());
//                    if (!Func.isEmpty(prodClassify)) {
//                        pdinfo.setPcId(prodClassify.getId());
//                    }
////                        ????????????
//                    prodPdinfoMapper.insert(pdinfo);
////                        ???????????????????????????????????????????????????????????????????????????
////                        ????????????
//                    List<ProdPartsinfoVO> prodPartsinfoVOS = xyOrderOrdinfoMapper.getProdPartsinfo(o.getPdCode());
////                        ????????????????????????
//                    if (!Func.isEmpty(prodPartsinfoVOS)) {
//                        prodPartsinfoVOS.stream().forEach(prodPartsinfoVO -> {
////                            if (o.getPartErp().equals(prodPartsinfoVO.getErpId())) {
////                                ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
////                                BeanUtils.copyProperties(prodPartsinfoVO, prodPartsinfo);
////                                prodPartsinfo.setPdId(pdinfo.getId());
////                                prodPartsinfoMapper.insert(prodPartsinfo);
////                                o.setPtSize(prodPartsinfoVO.getPtSize());
////                                o.setPtId(prodPartsinfo.getId());
////                                o.setPtNo(prodPartsinfo.getPtNo());
////
////                                List<MaterProdlinkVO> materProdlinkVOS = xyOrderOrdinfoMapper.getMaterProdlinkByAPComp(o.getErpId());
////                                List<MaterProdlinkVO> materProdlinkVOSFL = xyOrderOrdinfoMapper.getMaterProdlinkByAPCompFL(o.getErpId());
////                                if (!Func.isEmpty(materProdlinkVOS)) {
////                                    materProdlinkVOS.stream().forEach(materProdlinkVO -> {
////                                    MaterProdlink materProdlink = new MaterProdlink();
////                                    BeanUtils.copyProperties(materProdlinkVO, materProdlink);
////                                    materProdlink.setPdId(prodPartsinfo.getId());
////                                    materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
////                                    materProdlinkMapper.insert(materProdlink);
////                                        MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
////                                        if (!Func.isEmpty(materMtinfo)) {
////                                            o.setMaterialName(materMtinfo.getMlName());
////                                    }
////                                    });
////                                }
////                                if (!Func.isEmpty(materProdlinkVOSFL)) {
////                                    materProdlinkVOSFL.stream().forEach(materProdlinkVO -> {
////                                        MaterProdlink materProdlink = new MaterProdlink();
////                                        BeanUtils.copyProperties(materProdlinkVO, materProdlink);
////                                        materProdlink.setPdId(prodPartsinfo.getId());
////                                        materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
////                                        materProdlinkMapper.insert(materProdlink);
////                                        if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
////                                            MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
////                                            if (!Func.isEmpty(materMtinfo)) {
////                                                o.setIngredientName(materMtinfo.getMlName());
////                                            }
////                                        }
////                                    });
////                                }
////
//////                          ?????????????????????
////                                List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getProdProcelink(o.getPartErp());
////                                if (!Func.isEmpty(prodProcelinkVOS)) {
////                                    int a = 0;
////                                    for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
////                                        ProdProcelink prodProcelink = new ProdProcelink();
////                                        BeanUtils.copyProperties(prodProcelinkVO, prodProcelink);
////                                        prodProcelink.setPtId(prodPartsinfo.getId());
////                                        prodProcelink.setSortNum(a++);
////                                        prodProcelink.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
////                                        prodProcelinkMapper.insert(prodProcelink);
////                                        if (o.getPrErp().equals(prodProcelinkVO.getPrErp())) {
////                                            o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
////                                        }
////                                    }
////                                }
////                            }
//                            ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
//                            BeanUtils.copyProperties(prodPartsinfoVO, prodPartsinfo);
//                            prodPartsinfo.setPdId(pdinfo.getId());
//                            prodPartsinfoMapper.insert(prodPartsinfo);
//                            if (o.getPartErp().equals(prodPartsinfoVO.getErpId())) {
//                                o.setPtSize(prodPartsinfoVO.getPtSize());
//                                o.setPtId(prodPartsinfo.getId());
//                                o.setPtNo(prodPartsinfo.getPtNo());
//                            }
////                          ???????????????
//                            List<MaterProdlinkVO> materProdlinkVOS = xyOrderOrdinfoMapper.getMaterProdlinkByAPComp(o.getErpId());
//                            List<MaterProdlinkVO> materProdlinkVOSFL = xyOrderOrdinfoMapper.getMaterProdlinkByAPCompFL(o.getErpId());
//                            if (!Func.isEmpty(materProdlinkVOS)) {
//                                materProdlinkVOS.stream().forEach(materProdlinkVO -> {
//                                    MaterProdlink materProdlink = new MaterProdlink();
//                                    BeanUtils.copyProperties(materProdlinkVO, materProdlink);
//                                    materProdlink.setPdId(prodPartsinfo.getId());
//                                    materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
//                                    materProdlinkMapper.insert(materProdlink);
//                                    if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
//                                        MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                                        if (!Func.isEmpty(materMtinfo)) {
//                                            o.setMaterialName(materMtinfo.getMlName());
//                                        }
//                                    }
//                                });
//                            }
//                            if (!Func.isEmpty(materProdlinkVOSFL)) {
//                                materProdlinkVOSFL.stream().forEach(materProdlinkVO -> {
//                                    MaterProdlink materProdlink = new MaterProdlink();
//                                    BeanUtils.copyProperties(materProdlinkVO, materProdlink);
//                                    materProdlink.setPdId(prodPartsinfo.getId());
//                                    materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
//                                    materProdlinkMapper.insert(materProdlink);
//                                    if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
//                                        MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                                        if (!Func.isEmpty(materMtinfo)) {
//                                            o.setIngredientName(materMtinfo.getMlName());
//                                        }
//                                    }
//                                });
//                            }
////                          ?????????????????????
//                            List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getProdProcelink(o.getPartErp());
//                            if (!Func.isEmpty(prodProcelinkVOS)) {
//                                int a = 0;
//                                int b = 0;
//                                for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
//                                    if(b == 0 && prodProcelinkVO.getLb() == 2) {
////                                        ??????????????????
//                                        ProdProcelink prodProcelink = new ProdProcelink();
//                                        prodProcelink.setDiffLevel(0.0);
//                                        prodProcelink.setWasteRate(0.0);
//                                        prodProcelink.setIsUsed(1);
//                                        prodProcelink.setPtId(prodPartsinfo.getId());
//                                        prodProcelink.setSortNum(a++);
//                                        prodProcelink.setPrId(proessWorkinfoMapper.getByerpId("999999"));
//                                        prodProcelinkMapper.insert(prodProcelink);
//                                        b = 1;
//                                        if (o.getPartErp().equals(prodPartsinfo.getErpId()) && o.getPrErp().equals("999999")) {
//                                            o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                        }
//                                    }
//                                    ProdProcelink prodProcelink = new ProdProcelink();
//                                    BeanUtils.copyProperties(prodProcelinkVO, prodProcelink);
//                                    prodProcelink.setPtId(prodPartsinfo.getId());
//                                    prodProcelink.setSortNum(a++);
//                                    prodProcelink.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                    prodProcelinkMapper.insert(prodProcelink);
//                                    if (o.getPartErp().equals(prodPartsinfo.getErpId()) && o.getPrErp().equals(prodProcelinkVO.getPrErp())) {
//                                        o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                    }
//                                }
//                            }
//                        });
//                    }
////                    ????????????????????????????????????????????????
////                      ?????????????????????????????????
//                    if ((xyOrderOrdinfoMapper.getPdw(o.getWbNo())+ xyOrderOrdinfoMapper.getPdy(o.getWbNo())) > 0) {
////                        ????????????????????????
//                        ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
//                        prodPartsinfo.setErpId(o.getPdErp());
//                        prodPartsinfo.setPtName("??????");
//                        prodPartsinfo.setPtClassify(1);
//                        prodPartsinfo.setPdType(1);
//                        prodPartsinfo.setPdId(pdinfo.getId());
//                        prodPartsinfoMapper.insert(prodPartsinfo);
//                        if (o.getPartErp() == null || o.getPartErp() == "") {
//                            o.setPtSize(o.getPtSize());
//                            o.setPtId(prodPartsinfo.getId());
//                            o.setPtNo(prodPartsinfo.getPtNo());
//                        }
////                        ?????????????????????
//                        List<MaterProdlinkVO>  materList = xyOrderOrdinfoMapper.getMaterProdlinkByPd(o.getWbNo());
//                        List<MaterProdlinkVO>  materFLList = xyOrderOrdinfoMapper.getMaterProdlinkByPdFL(o.getWbNo());
//                        if (!Func.isEmpty(materList)) {
//                            materList.stream().forEach(materProdlinkVO -> {
//                                MaterProdlink materProdlink = new MaterProdlink();
//                                BeanUtils.copyProperties(materProdlinkVO, materProdlink);
//                                materProdlink.setPdId(prodPartsinfo.getId());
//                                materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
//                                materProdlinkMapper.insert(materProdlink);
//                                if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
//                                    MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                                    if (!Func.isEmpty(materMtinfo)) {
//                                        o.setMaterialName(materMtinfo.getMlName());
//                                    }
//                                }
//                            });
//                        }
//                        if (!Func.isEmpty(materFLList)) {
//                            materFLList.stream().forEach(materProdlinkVO -> {
//                                MaterProdlink materProdlink = new MaterProdlink();
//                                BeanUtils.copyProperties(materProdlinkVO, materProdlink);
//                                materProdlink.setPdId(prodPartsinfo.getId());
//                                materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
//                                materProdlinkMapper.insert(materProdlink);
//                                if (o.getPartErp().equals(prodPartsinfo.getErpId())) {
//                                    MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                                    if (!Func.isEmpty(materMtinfo)) {
//                                        o.setIngredientName(materMtinfo.getMlName());
//                                    }
//                                }
//                            });
//                        }
////                        ?????????????????????
//                        List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getlinkW(o.getWbNo());
//                        List<ProdProcelinkVO> prodProcelinkVOSY = xyOrderOrdinfoMapper.getlinkY(o.getWbNo());
//                        if(Func.isEmpty(prodProcelinkVOS)){
//                            prodProcelinkVOS = new ArrayList<>();
//                        }
//                        prodProcelinkVOS.addAll(prodProcelinkVOSY);
//                        if (!Func.isEmpty(prodProcelinkVOS)) {
//                            int a = 0;
//                            for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
//                                ProdProcelink prodProcelink = new ProdProcelink();
//                                BeanUtils.copyProperties(prodProcelinkVO, prodProcelink);
//                                prodProcelink.setPtId(prodPartsinfo.getId());
//                                prodProcelink.setSortNum(a++);
//                                prodProcelink.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                prodProcelinkMapper.insert(prodProcelink);
//                                if (o.getPrErp().equals(prodProcelinkVO.getPrErp())) {
//                                    o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
//                                }
//                            }
//                        }
//                    }
//                }
//                if(o.getPtId() != null){
//                    WorkbatchOrdlink workbatchOrdlink = new WorkbatchOrdlink();
//                    BeanUtils.copyProperties(o,workbatchOrdlink);
//                    workbatchOrdlink.setPdType(prodClassifyMapper.getByerpId(o.getTypeErp()).getClName());
//                    workbatchOrdlink.setDpId(machineMainfoMapper.getByerpId(o.getMaErp()).getDpId());
//                    workbatchOrdlink.setMaId(machineMainfoMapper.getByerpId(o.getMaErp()).getId());
//                    workbatchOrdlinkMapper.insert(workbatchOrdlink);
//
//                    WorkbatchOrdoee workbatchOrdoee = new WorkbatchOrdoee();
//                    workbatchOrdoee.setWkId(workbatchOrdlink.getId());
//                    workbatchOrdoee.setBeforePtid(workbatchOrdlink.getPtId());
//                    workbatchOrdoee.setBeforePtname(workbatchOrdlink.getPartName());
//                    workbatchOrdoee.setBeforePtno(workbatchOrdlink.getPtNo());
////                        workbatchOrdoee.setMaintain(1);
////                        workbatchOrdoee.setMaintainStay(0);
////                        workbatchOrdoee.setMaintainNum(1);
//                    workbatchOrdoee.setMouldStay(0);
//                    workbatchOrdoee.setMouldNum(1);
////                        workbatchOrdoee.setMealStay(0);
////                        workbatchOrdoee.setMealNum(1);
////                    workbatchOrdoee.setQualityNum(1);
//                    workbatchOrdoee.setDifficultNum(1.0);
//                    workbatchOrdoee.setProducePreTime(0);
//                    workbatchOrdoee.setPlanTime(o.getPlanTime());
//                    workbatchOrdoee.setErpSpeed(o.getErpSpeed());
//                    workbatchOrdoeeMapper.insert(workbatchOrdoee);
//                }
//            }
//    }


    /**
     * =====================================================??????????????????start
     *
     * @param o
     */
    private void newWorkbatchordlink(WorkbatchOrdlinkVO o) throws ParseException {
        // ??????????????????(??????????????????)
        o.setFinalTime(xyOrderOrdinfoMapper.getFinalTime(o.getOdNo(), o.getWbNo(), o.getCrmOdNo()));
        // ????????????????????????
        ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
        // ???????????????????????????????????????
        if (StringUtils.isNotBlank(o.getPartErp())) {
            prodPartsinfo = prodPartsinfoMapper.getByerpId(o.getPartErp());
        }
        // ????????????????????????
        if (!Func.isEmpty(prodPartsinfo)) {
            // ??????,???????????????id?????????????????????
            // ??????????????????
//            ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getOneProdPartsinfo(o.getPdErp1(),o.getPdErp());
            o.setPtId(prodPartsinfo.getId());
            o.setPtNo(prodPartsinfo.getPtNo());
            // ????????????id??????????????????
            Map<String, Object> processMap = new HashMap<>();
            processMap.put("pt_id", prodPartsinfo.getId());
            List<ProdProcelink> procelinkList = prodProcelinkMapper.selectByMap(processMap);
            procelinkList.forEach(procelink -> {
                ProcessWorkinfo processWorkinfo = proessWorkinfoMapper.selectById(procelink.getPrId());
                // ?????????????????????????????????????????????uuid????????????erp???uuid
                if ((!Func.isEmpty(processWorkinfo)) && o.getPrErp().equals(processWorkinfo.getErpId())) {
                    o.setPrId(processWorkinfo.getId());
                }
            });
        } else {
            // ???????????????????????? ???????????????????????????
            if (StringUtils.isBlank(o.getPartErp())) {
                // ??????????????????
                // ????????????????????????????????????????????????
                // ?????????????????????????????????
                if ((xyOrderOrdinfoMapper.getPdw(o.getWbNo()) + xyOrderOrdinfoMapper.getPdy(o.getWbNo())) > 0) {
                    // ????????????????????????
                    ProdPartsinfo partsinfo = new ProdPartsinfo();
                    partsinfo.setErpId(o.getPdErp());
                    partsinfo.setPtName("??????");
                    partsinfo.setPtClassify(1);
                    partsinfo.setPdType(1);
                    prodPartsinfoMapper.insert(partsinfo);
                    o.setPtId(partsinfo.getId());
                    o.setPtNo(partsinfo.getPtNo());
                    // ?????????????????????
                    List<MaterProdlinkVO> materList = xyOrderOrdinfoMapper.getMaterProdlinkByPd(o.getWbNo());
                    List<MaterProdlinkVO> materFLList = xyOrderOrdinfoMapper.getMaterProdlinkByPdFL(o.getWbNo());
                    List<String> erpIds = materFLList.stream().map(MaterProdlinkVO::getMtErp).collect(Collectors.toList());
                    // ?????????????????????
                    materList = materList.stream().filter(mater -> {
                        return !erpIds.contains(mater.getMtErp());
                    }).collect(Collectors.toList());
                    if (!Func.isEmpty(materList)) {
                        materList.stream().forEach(materProdlinkVO -> {
                            MaterProdlink materProdlink = new MaterProdlink();
                            BeanUtils.copyProperties(materProdlinkVO, materProdlink);
                            materProdlink.setPdId(partsinfo.getId());
                            materProdlink.setPrId(proessWorkinfoMapper.getByerpId(o.getPrErp()));
                            materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
                            materProdlinkMapper.insert(materProdlink);
//                            MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                            if (!Func.isEmpty(materMtinfo)) {
//                                o.setMaterialName(materMtinfo.getMlName());
//                            }
                        });
                    }
                    // ??????????????????
                    if (!Func.isEmpty(materFLList)) {
                        materFLList.stream().forEach(materProdlinkVO -> {
                            MaterProdlink materProdlink = new MaterProdlink();
                            BeanUtils.copyProperties(materProdlinkVO, materProdlink);
                            materProdlink.setPdId(partsinfo.getId());
                            materProdlink.setPrId(proessWorkinfoMapper.getByerpId(o.getPrErp()));
                            materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
                            materProdlinkMapper.insert(materProdlink);
//                            MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                                if (!Func.isEmpty(materMtinfo)) {
//                                    o.setIngredientName(materMtinfo.getMlName());
//                                }
                        });
                    }
                    // ?????????????????????
                    List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getlinkW(o.getWbNo());// ???????????????
                    List<ProdProcelinkVO> prodProcelinkVOSY = xyOrderOrdinfoMapper.getlinkY(o.getWbNo());// ???????????????
                    if (Func.isEmpty(prodProcelinkVOS)) {
                        prodProcelinkVOS = new ArrayList<>();
                    }
                    prodProcelinkVOS.addAll(prodProcelinkVOSY);
                    if (!Func.isEmpty(prodProcelinkVOS)) {
                        int a = 0;
                        for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
                            ProdProcelink prodProcelink = new ProdProcelink();
                            BeanUtils.copyProperties(prodProcelinkVO, prodProcelink);
                            prodProcelink.setPtId(partsinfo.getId());
                            prodProcelink.setSortNum(a++);
                            prodProcelink.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
                            prodProcelinkMapper.insert(prodProcelink);
                            if (o.getPrErp().equals(prodProcelinkVO.getPrErp())) {
                                o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
                            }
                        }
                    }
                }
            } else {
                // ????????????
                ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getPartBySCDHBJID(o.getPartErp());
                ProdPartsinfo parts = new ProdPartsinfo();
                BeanUtils.copyProperties(prodPartsinfoVO, parts);
                prodPartsinfoMapper.insert(parts);
                o.setPtId(parts.getId());
                o.setPtNo(parts.getPtNo());
                // ??????????????????
                // ?????????????????????
                List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getProdProcelink(o.getPartErp());
                if (!Func.isEmpty(prodProcelinkVOS)) {
                    int a = 0;
                    int b = 0;
                    for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
                        if (b == 0 && prodProcelinkVO.getLb() == 2) {//???2??????????????????
//                                        ??????????????????
                            ProdProcelink prodProcelink = new ProdProcelink();
                            prodProcelink.setDiffLevel(0.0);
                            prodProcelink.setWasteRate(0.0);
                            prodProcelink.setIsUsed(1);
                            prodProcelink.setPtId(parts.getId());
                            prodProcelink.setSortNum(a++);
                            prodProcelink.setPrId(proessWorkinfoMapper.getByerpId("999999"));
                            prodProcelinkMapper.insert(prodProcelink);
                            b = 1;//?????????????????????
                            if (o.getPrErp().equals("999999")) {
                                o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
                            }
                        }
                        ProdProcelink prodProcelink = new ProdProcelink();
                        BeanUtils.copyProperties(prodProcelinkVO, prodProcelink);
                        prodProcelink.setPtId(parts.getId());
                        prodProcelink.setSortNum(a++);
                        prodProcelink.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
                        prodProcelinkMapper.insert(prodProcelink);
                        if (o.getPrErp().equals(prodProcelinkVO.getPrErp())) {
                            o.setPrId(proessWorkinfoMapper.getByerpId(prodProcelinkVO.getPrErp()));
                        }
                    }
                }


                // ?????????????????????
                List<MaterProdlinkVO> materProdlinkVOS = xyOrderOrdinfoMapper.getMaterProdlinkByAPComp(o.getErpId());
                List<MaterProdlinkVO> materProdlinkVOSFL = xyOrderOrdinfoMapper.getMaterProdlinkByAPCompFL(o.getErpId());
                List<String> erpIds = materProdlinkVOSFL.stream().map(MaterProdlinkVO::getMtErp).collect(Collectors.toList());
                materProdlinkVOS = materProdlinkVOS.stream().filter(mater -> {
                    return !erpIds.contains(mater.getMtErp());
                }).collect(Collectors.toList());
                if (!Func.isEmpty(materProdlinkVOS)) {
                    materProdlinkVOS.stream().forEach(materProdlinkVO -> {
                        MaterProdlink materProdlink = new MaterProdlink();
                        BeanUtils.copyProperties(materProdlinkVO, materProdlink);
                        materProdlink.setPdId(parts.getId());
                        materProdlink.setPrId(proessWorkinfoMapper.getByerpId(materProdlinkVO.getPrErp()));
                        materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
                        materProdlinkMapper.insert(materProdlink);
//                        if (o.getPrErp().equals(materProdlinkVO.getPrErp())) {
//                            MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                            if (!Func.isEmpty(materMtinfo)) {
//                                o.setMaterialName(materMtinfo.getMlName());
//                            }
//                        }
                    });
                }
                if (!Func.isEmpty(materProdlinkVOSFL)) {
                    materProdlinkVOSFL.stream().forEach(materProdlinkVO -> {
                        MaterProdlink materProdlink = new MaterProdlink();
                        BeanUtils.copyProperties(materProdlinkVO, materProdlink);
                        materProdlink.setPdId(parts.getId());
                        materProdlink.setPrId(proessWorkinfoMapper.getByerpId(materProdlinkVO.getPrErp()));
                        materProdlink.setMlId(materMtinfoMapper.getByErpId(materProdlinkVO.getMtErp()));
                        materProdlinkMapper.insert(materProdlink);
//                        if (o.getPrErp().equals(materProdlinkVO.getPrErp())) {
//                            MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
//                            if (!Func.isEmpty(materMtinfo)) {
//                                o.setIngredientName(materMtinfo.getMlName());
//                            }
//                        }
                    });
                }
            }

        }

        WorkbatchOrdlink workbatchOrdlink = new WorkbatchOrdlink();
        BeanUtils.copyProperties(o, workbatchOrdlink);
        if (workbatchOrdlink.getCloseTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String close = sdf.format(workbatchOrdlink.getCloseTime());
            workbatchOrdlink.setCloseTime(sdf1.parse(close));
        }
        if (o.getPtCD() != 0.0f) {
            workbatchOrdlink.setPtSize(o.getPtCD() + "*" + o.getPtKD());
        }
        CrmCustomer crm = crmCustomerMapper.getByerpId(o.getCmErp());
        if (!Func.isEmpty(crm)) {
            workbatchOrdlink.setCmName(crm.getCmName());
        }
        ProdClassify classify = prodClassifyMapper.getByerpId(o.getTypeErp());
        if (!Func.isEmpty(classify)) {
            workbatchOrdlink.setPdType(classify.getClName());
        }
        MachineMainfo machineMainfo = machineMainfoMapper.getByerpId(o.getMaErp());
        workbatchOrdlink.setDpId(machineMainfo.getDpId());
        workbatchOrdlink.setMaId(machineMainfo.getId());
//                        ??????????????????
        MachineClassify machineClassify = machineClassifyMapper.selectById(machineMainfo.getMtId());
        workbatchOrdlink.setUsId(saUserMapper.getUserIdByerpId(o.getUsErp()));
        if (StringUtils.isNotBlank(workbatchOrdlink.getIngredientName())) {
            workbatchOrdlink.setIngredientName(workbatchOrdlink.getIngredientName().replaceAll("[\r\n]", ""));
        }
//        ??????erpId??????????????????
        List<Integer> ids = workbatchOrdlinkMapper.selectByEroId(workbatchOrdlink.getErpId());
        if (null == ids || ids.size() == 0) {
            workbatchOrdlinkMapper.insert(workbatchOrdlink);
        }
//        if (!Func.isEmpty(machineClassify) && machineClassify.getModel().equals("????????????")) {
//                            ???????????????
//        if (o.getVersionClass() != null  && !o.getVersionClass().equals("")) {
        WorkBatchExpprint workBatchExpprint = new WorkBatchExpprint();
        BeanUtils.copyProperties(o, workBatchExpprint);
        workBatchExpprint.setSdId(workbatchOrdlink.getId());
//            workBatchExpprint.setColorNum(o.getColorNum());
//            workBatchExpprint.setCtpNo(o.getCtpNo());
//            workBatchExpprint.setCtpTime(o.getCtpTime());
//            workBatchExpprint.setPaintColour(o.getPaintColour());
//            workBatchExpprint.setVersionClass(o.getVersionClass());
        workbatchExpprintMapper.insert(workBatchExpprint);
//        }
//        }else if (!Func.isEmpty(machineClassify) && machineClassify.getModel().equals("????????????")) {
//                            ?????????????????????
//        if (o.getCutNo() != null  && !o.getCutNo().equals("")) {
        WorkBatchExpcut workBatchExpcut = new WorkBatchExpcut();
        workBatchExpcut.setSdId(workbatchOrdlink.getId());
        workBatchExpcut.setCutNo(o.getCutNo());
        workbatchExpcutMapper.insert(workBatchExpcut);
//        }
//        }

        WorkbatchOrdoee workbatchOrdoee = new WorkbatchOrdoee();
        workbatchOrdoee.setWkId(workbatchOrdlink.getId());
        workbatchOrdoee.setBeforePtid(workbatchOrdlink.getPtId());
        workbatchOrdoee.setBeforePtname(workbatchOrdlink.getPartName());
        workbatchOrdoee.setBeforePtno(workbatchOrdlink.getPtNo());
//                        workbatchOrdoee.setMaintain(1);
//                        workbatchOrdoee.setMaintainStay(0);
//                        workbatchOrdoee.setMaintainNum(1);
        workbatchOrdoee.setMouldStay(0);
        workbatchOrdoee.setMouldNum(1);
//                        workbatchOrdoee.setMealStay(0);
//                        workbatchOrdoee.setMealNum(1);
//                    workbatchOrdoee.setQualityNum(1);
        workbatchOrdoee.setDifficultNum(1.0);
        workbatchOrdoee.setProducePreTime(0);
        workbatchOrdoee.setPlanTime(o.getPlanTime());
        workbatchOrdoee.setErpSpeed(o.getErpSpeed());
        workbatchOrdoeeMapper.insert(workbatchOrdoee);

        addWorkbatchProgress(workbatchOrdlink);
    }

//==========================================================????????????end

    /**
     * ????????????id??????????????????(??????)
     *
     * @param Id
     * @return
     * @deprecated ????????????
     */
    @Override
    @GetMapping("/updateWorkbatchById")
    public R updateWorkbatchById(Integer Id) {
//        ????????????id????????????????????????
        WorkbatchOrdlink ordlinkLocal = workbatchOrdlinkMapper.selectById(Id);
//        ???????????????fz????????????
        WorkbatchOrdlink ordlinkFZ = xyWorkBatchMapper.selectById(Id);
        ordlinkLocal.setPlanNum(ordlinkFZ.getPlanNum());
        ordlinkLocal.setPlanNumber(ordlinkFZ.getPlanNumber());
        ordlinkLocal.setCompleteNum(ordlinkFZ.getCompleteNum());
        ordlinkLocal.setIncompleteNum(ordlinkFZ.getIncompleteNum());
        ordlinkLocal.setExtraNum(ordlinkFZ.getExtraNum());
        ordlinkLocal.setUpdateAt(new Date());
        //??????
//        workbatchOrdlinkMapper.updateById(ordlinkLocal);
        return R.success("?????????????????????-??????????????????");
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @GetMapping("/updateWorkbatchStatus")
    public R updateWorkbatchStatus() {
        //        ???????????????
        xyWorkBatchMapper.creatTMP();
        List<tmp> tmpList = workbatchOrdlinkMapper.selectByStatus("-1");
//        ???????????????
        insertList(tmpList);
//        xyWorkBatchMapper.insterTMP(tmpList);
//        ??????????????????????????????????????????????????????
        List<String> uuids = xyWorkBatchMapper.selectStatusListw();
        List<String> uuids2 = xyWorkBatchMapper.selectStatusListy();
        if (Func.isEmpty(uuids)) {
            uuids = new ArrayList<>();
        }
        uuids.addAll(uuids2);
//        ????????????????????????
        if (!Func.isEmpty(uuids)) {
            workbatchOrdlinkMapper.setStatus(uuids);
        }
//        ???????????????
        xyWorkBatchMapper.dropTMP();
        return R.success("??????????????????????????????");
    }

    /**
     * ???????????????
     *
     * @param list
     */
    private void insertList(List<tmp> list) {
        int insertLength = list.size();
        int i = 0;
        while (insertLength > 600) {
            xyWorkBatchMapper.insterTMP(list.subList(i, i + 600));
            i = i + 600;
            insertLength = insertLength - 600;
        }
        if (insertLength > 0) {
            xyWorkBatchMapper.insterTMP(list.subList(i, i + insertLength));
        }
    }

    @GetMapping("/getIntrgerList")
    public R getIntrgerList() {
        return R.data(hbCeShiMapper.ceshiList());
    }


    @Override
    @GetMapping("/updateDept")
    @Transactional
    public R updateDept() {
        try {
            //        ????????????
            List<BaseDeptInfoVo> list = xyDeptMapper.list();
            if (!list.isEmpty()) {
                List<BaseDeptInfoVo> deptList = DeptUtil.getTree(list);
                saveDeptTree(deptList, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// ??????????????????????????????
            massage(stackTraceElement);
            return R.fail("????????????");
        }

        return R.success("ok");
    }

    private void saveDeptTree(List<BaseDeptInfoVo> deptList, Integer Pid) {
        for (BaseDeptInfoVo baseDeptInfoVo : deptList) {
            Map<String, Object> deptMap = new HashMap<>();
            deptMap.put("erp_id", baseDeptInfoVo.getErpId());
            if (Func.isEmpty(baseDeptInfoMapper.selectByMap(deptMap))) {
                BaseDeptInfo baseDeptInfo = new BaseDeptInfo();
                baseDeptInfo.setClassify(baseDeptInfoVo.getClassify());
                baseDeptInfo.setPId(Pid);
                baseDeptInfo.setDpName(baseDeptInfoVo.getDpName());
                baseDeptInfo.setDpNum(baseDeptInfoVo.getDpNum());
                baseDeptInfo.setFullName(baseDeptInfoVo.getFullName());
                baseDeptInfo.setIsDeleted(baseDeptInfoVo.getIsDeleted());
                baseDeptInfo.setRemark(baseDeptInfoVo.getRemark());
                baseDeptInfo.setSort(baseDeptInfoVo.getSort());
                baseDeptInfo.setTenantId(baseDeptInfoVo.getTenantId());
                baseDeptInfo.setErpId(baseDeptInfoVo.getErpId());
                baseDeptInfo.setErpPid(baseDeptInfoVo.getErpPid());
                baseDeptInfoMapper.insert(baseDeptInfo);
                baseDeptInfoVo.setId(baseDeptInfo.getId());
            }
            if (!Func.isEmpty(baseDeptInfoVo.getChildren())) {
                saveDeptTree(baseDeptInfoVo.getChildren(), baseDeptInfoVo.getId());
            } else {
                continue;
            }
        }
    }


    private void saveStaffext() {
        List<BaseStaffext> staffextList = baseStaffextMapper.selectList(null);
        List<BaseStaffext> fzStaffextList = xyStaffextMapper.list();
        if (!staffextList.isEmpty()) {
            List<String> ids = staffextList.stream().map(BaseStaffext::getErpId).collect(Collectors.toList());
            fzStaffextList = fzStaffextList.stream().filter(o -> {
                return !ids.contains(o.getErpId());
            }).collect(Collectors.toList());
            if (!Func.isEmpty(fzStaffextList)) {
                fzStaffextList.forEach(o -> {
                    BaseStaffinfo baseStaffinfo = baseStaffinfoMapper.getByerpId(o.getErpId());
                    if (!Func.isEmpty(baseStaffinfo)) {
                        o.setSfId(baseStaffinfo.getId());
                    }
                    baseStaffextMapper.insert(o);
                });
            }
        } else {
            fzStaffextList.forEach(o -> {
                BaseStaffinfo baseStaffinfo = baseStaffinfoMapper.getByerpId(o.getErpId());
                if (!Func.isEmpty(baseStaffinfo)) {
                    o.setSfId(baseStaffinfo.getId());
                }
                baseStaffextMapper.insert(o);
            });
        }
    }

    private void saveFzBladeUser() {
        List<SaUser> saUsers = xySaUserMapper.getUserList();
        List<SaUser> userList = saUserMapper.selectList(null);
        if (!userList.isEmpty()) {
            //????????????????????????id
            List<String> erpIds = userList.stream().map(SaUser::getErpId).collect(Collectors.toList());
            saUsers = saUsers.stream().filter(o -> {
                return !erpIds.contains(o.getErpId());
            }).collect(Collectors.toList());
            //????????????
            if (!saUsers.isEmpty()) {
                for (SaUser saUser : saUsers) {
                    try {
//                        ?????????????????????123
                        saUser.setPassword("adcd7048512e64b48da55b027577886ee5a36350");
                        saUser.setTenantId("xingyi");
                        saUser.setRoleId("13");
//                    ??????????????????
                        BaseDeptInfo deptInfo = baseDeptInfoMapper.getByerpId(saUser.getDpErp());
                        if (!Func.isEmpty(deptInfo)) {
                            saUser.setDeptId(deptInfo.getId() + "");
                        }
                        saUserMapper.insert(saUser);
                        //??????????????????

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            //?????????????????????
            saUsers.forEach(o -> {
                o.setPassword("adcd7048512e64b48da55b027577886ee5a36350");
                o.setTenantId("xingyi");
                o.setRoleId("13");
//                    ??????????????????
                BaseDeptInfo deptInfo = baseDeptInfoMapper.getByerpId(o.getDpErp());
                if (!Func.isEmpty(deptInfo)) {
                    o.setDeptId(deptInfo.getId() + "");
                }
                saUserMapper.insert(o);
            });
        }
        List<SaUser> xyUsers = xySaUserMapper.getUserList();
        Map<String, Object> map = new HashMap<>();
        map.put("is_deleted", 0);
        List<SaUser> users = saUserMapper.selectByMap(map);
        if (!Func.isEmpty(users)) {
            //????????????????????????id
            List<String> erpIds = xyUsers.stream().map(SaUser::getErpId).collect(Collectors.toList());
            users = users.stream().filter(o -> {
                return !erpIds.contains(o.getErpId());
            }).collect(Collectors.toList());
            if (!Func.isEmpty(users)) {
                users.stream().forEach(user -> {
                    if (user.getId() != 1) {
                        user.setIsDeleted(1);
                        saUserMapper.updateById(user);
                    }
                });
            }
        }
    }

    private void saveFzStaffInfo() {
        List<BaseStaffinfo> baseStaffinfos = baseStaffinfoMapper.selectList(null);
        List<BaseStaffinfo> fzBaseStaffinfos = xyStaffinfoMapper.list();
        if (!baseStaffinfos.isEmpty()) {
            //????????????????????????id
            List<String> erpIds = baseStaffinfos.stream().map(BaseStaffinfo::getErpId).collect(Collectors.toList());
            fzBaseStaffinfos = fzBaseStaffinfos.stream().filter(o -> {
                return !erpIds.contains(o.getErpId());
            }).collect(Collectors.toList());
            if (!fzBaseStaffinfos.isEmpty()) {
                fzBaseStaffinfos.forEach(o -> {
                    o.setUserId(saUserMapper.getUserIdByerpId(o.getErpId()));
//                    ??????????????????
                    BaseDeptInfo deptInfo = baseDeptInfoMapper.getByerpId(o.getDpErp());
                    if (!Func.isEmpty(deptInfo)) {
                        o.setDpId(deptInfo.getId());
                    }
                    baseStaffinfoMapper.insert(o);
                });
            }
        } else {
            fzBaseStaffinfos.forEach(o -> {
                o.setUserId(saUserMapper.getUserIdByerpId(o.getErpId()));
//                    ??????????????????
                BaseDeptInfo deptInfo = baseDeptInfoMapper.getByerpId(o.getDpErp());
                if (!Func.isEmpty(deptInfo)) {
                    o.setDpId(deptInfo.getId());
                }
                baseStaffinfoMapper.insert(o);
            });
        }

        List<BaseStaffinfo> xyBaseStaffinfos = xyStaffinfoMapper.list();
        Map<String, Object> map = new HashMap<>();
        map.put("is_used", 1);
        List<BaseStaffinfo> Staffinfos = baseStaffinfoMapper.selectByMap(map);
        if (!Func.isEmpty(Staffinfos)) {
            List<String> erpIds = xyBaseStaffinfos.stream().map(BaseStaffinfo::getErpId).collect(Collectors.toList());
            Staffinfos = Staffinfos.stream().filter(o -> {
                return !erpIds.contains(o.getErpId());
            }).collect(Collectors.toList());
            if (!Func.isEmpty(Staffinfos)) {
                Staffinfos.stream().forEach(info -> {
                    if (info.getId() != 1) {
                        info.setIsUsed(0);
                        baseStaffinfoMapper.updateById(info);
                    }
                });
            }
        }
    }

    private void savepartsinfo(List<ProdPartsinfoVO> list, Integer pId) {
        for (ProdPartsinfoVO partsinfoVO : list) {
            ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
            prodPartsinfo.setPid(pId);
            prodPartsinfo.setPdId(partsinfoVO.getPdId());
            prodPartsinfo.setPtName(partsinfoVO.getPtName());
            prodPartsinfo.setPtNo(partsinfoVO.getPtNo());
            prodPartsinfo.setPtClassify(1);
            prodPartsinfo.setPtType(1);
            prodPartsinfoMapper.insert(prodPartsinfo);
            partsinfoVO.setId(prodPartsinfo.getId());
            partsinfoVO.setPid(pId);
            if (!Func.isEmpty(partsinfoVO.getMaterProdlinks())) {
                partsinfoVO.getMaterProdlinks().stream().forEach(mater -> {
                    mater.setPdId(partsinfoVO.getId());
                });
            }
            if (!Func.isEmpty(partsinfoVO.getProdProcelinks())) {
                partsinfoVO.getProdProcelinks().stream().forEach(procelink -> {
                    procelink.setPtId(partsinfoVO.getId());
                });
            }
            if (!Func.isEmpty(partsinfoVO.getChildren())) {
                savepartsinfo(partsinfoVO.getChildren(), partsinfoVO.getId());
            } else {
                continue;
            }
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param list
     */
    private void saveProdPartsinfo(List<ProdPartsinfoVO> list) {
        for (ProdPartsinfoVO partsinfoVO : list) {
//            ??????????????????
            if (!Func.isEmpty(partsinfoVO.getMaterProdlinks())) {
                for (MaterProdlink materProdlink : partsinfoVO.getMaterProdlinks()) {
                    materProdlinkMapper.insert(materProdlink);
                }
            }
//            ?????????????????????
            if (!Func.isEmpty(partsinfoVO.getProdProcelinks())) {
                for (ProdProcelink prodProcelink : partsinfoVO.getProdProcelinks()) {
                    prodProcelink.setPtId(partsinfoVO.getId());
                    prodProcelink.setRemarks("");
                    prodProcelink.setPrParam("[]");
                    prodProcelink.setDiffLevel(0.00);
                    prodProcelink.setWasteRate(0.00);
                    prodProcelinkMapper.insert(prodProcelink);
                }
            }
            if (!Func.isEmpty(partsinfoVO.getChildren())) {
                saveProdPartsinfo(partsinfoVO.getChildren());
            } else {
                continue;
            }
        }
    }

    private JsonsRootBean getPdinfoJSON(List<ProdPartsinfoVO> list, JsonsRootBean jsonsRootBean) {
        for (ProdPartsinfoVO prodPartsinfoVO : list) {
            if (!Func.isEmpty(prodPartsinfoVO.getChildren())) {
//              ??????????????????????????????
                getPdinfoJSON(prodPartsinfoVO.getChildren(), jsonsRootBean);
            } else {
//                ???????????????json??????
//
                Nodelist node = new Nodelist();
                node.setUuid(UUID.randomUUID().toString());
                node.setLeft(ptLeft + "px");
                node.setTop(ptTop + "px");
                ptTop = ptTop + 100;
                node.setShow(true);
                node.setId(prodPartsinfoVO.getId());
                node.setPtName(prodPartsinfoVO.getPtName());
                node.setPtUuid(UUID.randomUUID().toString());
                node.setPtType(prodPartsinfoVO.getPtType());
                node.setType(2);//?????????????????????
                node.setIco("el-icon-plus");
                node.setName(prodPartsinfoVO.getPtName());
                node.setPtClassify(prodPartsinfoVO.getPtClassify());//todo ???????????????????????????
                node.setReUuid(node.getUuid());
                if (jsonsRootBean.getNodeList() == null || Func.isEmpty(jsonsRootBean.getNodeList())) {
                    jsonsRootBean.setNodeList(new ArrayList<>());
                }
                jsonsRootBean.getNodeList().add(node);
                if (!Func.isEmpty(prodPartsinfoVO.getMaterProdlinks())) {
                    for (MaterProdlink materProdlink : prodPartsinfoVO.getMaterProdlinks()) {
                        //                    ??????????????????????????????json?????????????????????json??????
                        Nodelist nodeMater = new Nodelist();
                        nodeMater.setUuid(UUID.randomUUID().toString());
                        nodeMater.setShow(true);
                        nodeMater.setId(materProdlink.getMlId());
//                        ????????????
                        nodeMater.setLeft(mtLeft + "px");
                        nodeMater.setTop(mtTop + "px");
                        mtTop = mtTop + 10;
                        MaterMtinfo materMtinfo = materMtinfoMapper.selectById(materProdlink.getMlId());
                        nodeMater.setMlNo(materMtinfo.getMlNo() == null ? "" : materMtinfo.getMlNo());
                        nodeMater.setMlName(materMtinfo.getMlName());
                        nodeMater.setMcId(materMtinfo.getMcId());
                        nodeMater.setMaterial(materMtinfo.getMaterial());
                        nodeMater.setModel(materMtinfo.getModel());
                        nodeMater.setSpecification(materMtinfo.getSpecification());
                        nodeMater.setSize(materMtinfo.getSize());
                        nodeMater.setBrand(materMtinfo.getBrand());
                        nodeMater.setManufactor(materMtinfo.getManufactor());
                        nodeMater.setIslocal(materMtinfo.getIslocal());
                        nodeMater.setIsdel(materMtinfo.getIsdel());
                        nodeMater.setCreateAt(materMtinfo.getCreateAt());
                        nodeMater.setUpdateAt(materMtinfo.getUpdateAt());
                        nodeMater.setIco("el-icon-plus");
                        nodeMater.setName(materMtinfo.getMlName());
                        nodeMater.setType(1);//???????????????
                        nodeMater.setMcName(materMtinfo.getMcId() != null ? materClassfiyMapper.selectById(materMtinfo.getMcId()).getMcName() : null);
                        Leftstyle leftstyle = new Leftstyle();
                        leftstyle.setBackgroundColor("#409EFF");
                        nodeMater.setLeftStyle(leftstyle);
                        nodeMater.setPtUuid(node.getUuid());//???????????????uuid
                        nodeMater.setMlNum(materProdlink.getMtNum() + "");
                        jsonsRootBean.getNodeList().add(nodeMater);
//                        ????????????
                        Linelist line = new Linelist();
                        line.setFrom(nodeMater.getUuid());
                        line.setTo(node.getUuid());
                        if (jsonsRootBean.getLineList() == null || Func.isEmpty(jsonsRootBean.getLineList())) {
                            jsonsRootBean.setLineList(new ArrayList<>());
                        }
                        jsonsRootBean.getLineList().add(line);
                    }
                    ;
                    if (!Func.isEmpty(prodPartsinfoVO.getProdProcelinks())) {
                        String from = node.getUuid();
                        for (ProdProcelink procelink : prodPartsinfoVO.getProdProcelinks()) {
                            Nodelist nodeProce = new Nodelist();
                            nodeProce.setUuid(UUID.randomUUID().toString());
                            nodeProce.setLeft(prLeft + "px");
                            nodeProce.setTop(prTop + "px");
                            prTop = prTop + 10;

                            nodeProce.setShow(true);
                            ProcessWorkinfo processWorkinfo = proessWorkinfoMapper.selectById(procelink.getPrId());//??????????????????
                            nodeProce.setId(processWorkinfo.getId());
                            nodeProce.setPrName(processWorkinfo.getPrName());
                            nodeProce.setPrNo(processWorkinfo.getPrNo());
                            nodeProce.setSort(procelink.getSortNum());
                            nodeProce.setStatus(processWorkinfo.getStatus());
                            nodeProce.setIslocal(processWorkinfo.getIslocal());
                            nodeProce.setIsdel(processWorkinfo.getIsdel());
                            nodeProce.setCreateAt(processWorkinfo.getCreateAt());
                            nodeProce.setUpdateAt(processWorkinfo.getUpdateAt());
//                            ????????????????????????
                            Map map = new HashMap<>();
                            map.put("pr_id", processWorkinfo.getId());
                            List<ProcessClasslink> link = proessClasslinkMapper.selectByMap(map);
                            ProcessClassify classify = processClassifyMapper.selectById(link.get(0).getPyId());
                            nodeProce.setPyName(classify.getPyName());
                            nodeProce.setDiffLevel(procelink.getDiffLevel());
                            nodeProce.setWasteRate(procelink.getWasteRate());
                            nodeProce.setIco("el-icon-plus");
                            nodeProce.setName(processWorkinfo.getPrName());
                            nodeProce.setType(3);//???????????????
                            Leftstyle leftStyle = new Leftstyle();
                            leftStyle.setBackgroundColor("#67C23A");
                            nodeProce.setLeftStyle(leftStyle);
                            nodeProce.setPrParam(procelink.getPrParam());
                            nodeProce.setRemarks(procelink.getRemarks());
                            nodeProce.setDifficulty("0");
                            nodeProce.setLossRate("0");
                            nodeProce.setPtUuid(node.getUuid());
                            jsonsRootBean.getNodeList().add(nodeProce);
//                            ????????????
                            Linelist line = new Linelist();
                            line.setFrom(from);
                            line.setTo(node.getUuid());
                            if (jsonsRootBean.getLineList() == null || Func.isEmpty(jsonsRootBean.getLineList())) {
                                jsonsRootBean.setLineList(new ArrayList<>());
                            }
                            jsonsRootBean.getLineList().add(line);
                            from = nodeProce.getUuid();
                        }
                        ;
                    }
                    continue;
                }
            }
        }
        ;
        return jsonsRootBean;
    }

    /**
     * ????????????
     * ?????????????????????37??????????????????????????????????????????????????????23??????????????????????????????????????????
     */
//    @Scheduled(cron="0 0 12 * * ?")
    @Scheduled(cron = "0 0 12,23 * * ?")
    public void taskEndFissionWanted() {
        dingClient.send("???????????????????????????---??????---", "InternalH5");
        log.info("????????????????????????12:00??????23:00?????????--star-----------------??????------------------");
//        ????????????
        updateDept();
//        ????????????
        updateBladeUser();
//        ????????????
        updateCrmCustomer();
//        ??????/??????
        updateProcessWorkinfo();
//        ????????????
        updatePdClassify();
//          ????????????
        updateWorkbatch(-1);
        // ??????????????????
        updateWorkbatchStatus();
        dingClient.send("???????????????????????????---??????---", "InternalH5");
        log.info("????????????????????????12:00??????23:00?????????--end-----------------??????------------------");
    }

    /**
     * ????????????
     *
     * @param stackTraceElement
     */
    private static void massage(StackTraceElement stackTraceElement) {
        String error = stackTraceElement.getFileName() + "-" + stackTraceElement.getMethodName() + "-" + stackTraceElement.getLineNumber();
        MsgConfig.sendStringMessages("15086235562", "??????????????????pro", error);
    }

    /**
     * ????????????
     *
     * @param message
     */
    private static void message(String message) {
        MsgConfig.sendStringMessages("15086235562", "pro-???????????????????????????????????????", message);
    }

    /**
     * ?????????????????????
     */
    public void addWorkbatchProgress(WorkbatchOrdlink workbatchOrdlink) {
        Date date = new Date();
        Integer status = 0;
        Integer totalTime = 0;//???????????????
        Integer stayTime = null;//??????????????????
        Integer planCount = null;//????????????
        Date finishTime = null;

        String wbNo = workbatchOrdlink.getWbNo();
        Integer wbId = workbatchOrdlink.getWbId();
        Integer prId = workbatchOrdlink.getPrId();
        String prName = workbatchOrdlink.getPrName();
        Integer ptId = workbatchOrdlink.getPtId();
        String cmName = workbatchOrdlink.getCmName();
        String ptName = workbatchOrdlink.getPartName();
        String pdName = workbatchOrdlink.getPdName();
        Integer sdId = workbatchOrdlink.getId();
        Integer maId = workbatchOrdlink.getMaId();
        Integer planNum = workbatchOrdlink.getPlanNum();//????????????
        Integer completeNum = workbatchOrdlink.getCompleteNum();//????????????
        completeNum = completeNum == null ? 0 : completeNum;
        planNum = planNum == null ? 0 : planNum;
        Date closeTime = workbatchOrdlink.getCloseTime();//????????????
        if (!StringUtil.isEmpty(wbNo)) {
            /*??????????????????????????????*/
            List<WorkbatchProgress> workbatchProgressList =
                    workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("wb_no", wbNo).eq("wp_type", 1));
            if (workbatchProgressList.isEmpty()) {//??????????????????,??????????????????
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//????????????
                workbatchProgress.setCreateAt(date);//????????????
                workbatchProgress.setFinishTime(finishTime);//??????????????????
                workbatchProgress.setLimitTime(closeTime);//????????????(????????????)
                workbatchProgress.setPdId(null);//??????id
                workbatchProgress.setPdName(pdName);//????????????
                workbatchProgress.setPlanCount(planNum);//????????????
                workbatchProgress.setRealCount(completeNum);//???????????????
                workbatchProgress.setSdId(sdId);//??????id
                workbatchProgress.setStatus(status);//?????????0?????????1?????????2?????????3????????????4????????????5??????6??????
                workbatchProgress.setMaId(maId);//??????id
                workbatchProgress.setTotalTime(totalTime);//???????????????(??????)
                workbatchProgress.setUpdateAt(date);//????????????
                workbatchProgress.setWarning(1);//????????????1???36??????2???24??????
                workbatchProgress.setWbId(wbId);//??????id
                workbatchProgress.setWbNo(wbNo);//????????????
                workbatchProgress.setWpType(1);//????????????1?????????????????????2???????????????3???????????????
                workbatchProgressMapper.insert(workbatchProgress);
            } else {//????????????????????????????????????
                WorkbatchProgress progress = workbatchProgressList.get(0);
                planCount = progress.getPlanCount();
                Integer realCount = progress.getRealCount();//???????????????
                realCount = realCount == null ? 0 : realCount;
                if (planCount == null) {
                    planCount = 0;
                }
                planCount += planNum;
                realCount += completeNum;
                progress.setPlanCount(planCount);//????????????
                progress.setRealCount(realCount);//????????????
                workbatchProgressMapper.updateById(progress);//????????????????????????????????????
            }
            /*??????????????????????????????????????????*/
            List<WorkbatchProgress> progressList = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                    .eq("pt_name", ptName).eq("wb_no", wbNo).eq("wp_type", 2));
            if (progressList.isEmpty()) {//??????????????????,?????????
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//????????????
                workbatchProgress.setCreateAt(date);//????????????
                workbatchProgress.setFinishTime(finishTime);//??????????????????
                workbatchProgress.setLimitTime(closeTime);//????????????(????????????)
                workbatchProgress.setPdId(null);//??????id
                workbatchProgress.setPdName(pdName);//????????????
                workbatchProgress.setPtId(ptId);//??????id
                workbatchProgress.setPtName(ptName);//????????????
                workbatchProgress.setPlanCount(planNum);//????????????
                workbatchProgress.setRealCount(completeNum);//???????????????
                workbatchProgress.setMaId(maId);//??????id
                workbatchProgress.setSdId(sdId);//??????id
                workbatchProgress.setWarning(1);//????????????1???36??????2???24??????
                workbatchProgress.setStatus(status);//?????????0?????????1?????????2?????????3????????????4????????????5??????6??????
                workbatchProgress.setUpdateAt(date);//????????????
                workbatchProgress.setWbId(wbId);//??????id
                workbatchProgress.setWbNo(wbNo);//????????????
                workbatchProgress.setWpType(2);//????????????1?????????????????????2???????????????3???????????????
                workbatchProgressMapper.insert(workbatchProgress);
            } else {//????????????????????????????????????
                WorkbatchProgress workbatchProgress = progressList.get(0);
                Integer rCount = workbatchProgress.getRealCount();
                Integer pCount = workbatchProgress.getPlanCount();
                rCount = rCount == null ? 0 : rCount;
                pCount = pCount == null ? 0 : pCount;
                pCount += planNum;
                rCount += completeNum;
                workbatchProgress.setRealCount(rCount);//???????????????
                workbatchProgress.setPlanCount(pCount);//???????????????
                workbatchProgressMapper.updateById(workbatchProgress);
            }
            List<WorkbatchProgress> progressList1 = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                    .eq("pr_id", prId).eq("pt_name", ptName).eq("ma_id", maId).eq("wb_no", wbNo).eq("wp_type", 2));
            if (progressList1.isEmpty()) {
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//????????????
                workbatchProgress.setCreateAt(date);//????????????
                workbatchProgress.setFinishTime(finishTime);//??????????????????
                workbatchProgress.setLimitTime(closeTime);//????????????(????????????)
                workbatchProgress.setPdId(null);//??????id
                workbatchProgress.setPdName(pdName);//????????????
                workbatchProgress.setPlanCount(planNum);//????????????
                workbatchProgress.setPrId(prId);//??????id
                workbatchProgress.setPrName(prName);//????????????
                workbatchProgress.setMaId(maId);//??????id
                workbatchProgress.setPtId(ptId);//??????id
                workbatchProgress.setPtName(ptName);//????????????
                workbatchProgress.setRealCount(completeNum);//???????????????
                workbatchProgress.setSdId(sdId);//??????id
                workbatchProgress.setStatus(status);//?????????0?????????1?????????2?????????3????????????4????????????5??????6??????
                workbatchProgress.setStayTime(stayTime);//??????????????????(??????)
                workbatchProgress.setTotalTime(totalTime);//???????????????(??????)
                workbatchProgress.setUpdateAt(date);//????????????
                workbatchProgress.setWarning(1);//????????????1???36??????2???24??????
                workbatchProgress.setWbId(wbId);//??????id
                workbatchProgress.setWbNo(wbNo);//????????????
                workbatchProgress.setWpType(3);//????????????1?????????????????????2???????????????3???????????????
                workbatchProgressMapper.insert(workbatchProgress);
            }
        }
    }


}
