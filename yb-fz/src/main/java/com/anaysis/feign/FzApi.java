package com.anaysis.feign;


import com.anaysis.common.DigestUtil;
import com.anaysis.common.PartsinfoUtil;
import com.anaysis.entity.*;
import com.anaysis.entity.json.JsonsRootBean;
import com.anaysis.entity.json.Leftstyle;
import com.anaysis.entity.json.Linelist;
import com.anaysis.entity.json.Nodelist;
import com.anaysis.mysqlmapper.*;
import com.anaysis.sqlservermapper.*;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.message.feign.FzClient;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/fz")
public class FzApi implements FzClient {
    @Autowired
    private FzSaUserMapper fzSaUserMapper;
    @Autowired
    private SaUserMapper saUserMapper;
    @Autowired
    private BaseDeptInfoMapper baseDeptInfoMapper;
    @Autowired
    private FzDeptMapper fzDeptMapper;
    @Autowired
    private BaseStaffinfoMapper baseStaffinfoMapper;
    @Autowired
    private FzStaffinfoMapper fzStaffinfoMapper;
    @Autowired
    private BaseStaffextMapper baseStaffextMapper;
    @Autowired
    private FzStaffextMapper fzStaffextMapper;
    @Autowired
    private FzProessWorkinfoDao fzProessWorkinfoMapper;
    @Autowired
    private ProessWorkinfoMapper proessWorkinfoMapper;
    @Autowired
    private ProessClasslinkMapper proessClasslinkMapper;
    @Autowired
    private ProcessClassifyMapper processClassifyMapper;
    @Autowired
    private FzCrmCustomerDao fzCrmCustomerMapper;
    @Autowired
    private CrmCustomerMapper crmCustomerMapper;
    @Autowired
    private FzOrderOrdinfoDao fzOrderOrdinfoMapper;
    @Autowired
    private OrderOrdinfoMapper orderOrdinfoMapper;
    @Autowired
    private OrderWorkbatchMapper orderWorkbatchMapper;
    @Autowired
    private ActsetCkflowMapper actsetCkflowMapper;
    @Autowired
    private ActsetCheckLogMapper actsetCheckLogMapper;
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
    private FzMachineDao fzMachineDao;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private ProcessMachlinkMapper processMachlinkMapper;
    @Autowired
    private FzWorkBatchMapper fzWorkBatchMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private WorkbatchOrdoeeMapper workbatchOrdoeeMapper;
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
        //??????bladeuser
        saveFzBladeUser();
        //??????staffInfo
        saveFzStaffInfo();
        //??????staffext
        saveStaffext();
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateMachine")
    public R updateMachine() {
        List<MachineMainfo> machineMainfos = fzMachineDao.getMachineList();
        if (machineMainfos != null && machineMainfos.size() > 0) {
            for (MachineMainfo machineMainfo : machineMainfos) {
                try {
                    MachineMainfo machine = machineMainfoMapper.selectById(machineMainfo.getId());
                    if (machine == null) {
                        machineMainfoMapper.insert(machineMainfo);
                        List<ProcessMachlink> processMachlinks = fzMachineDao.getProcessMachlink(machineMainfo.getId());
                        if (processMachlinks.size() > 0 && processMachlinks != null) {
                            for (ProcessMachlink processMachlink : processMachlinks) {
                                ProcessMachlink processMach = processMachlinkMapper.selectById(processMachlink.getId());
                                if (processMach == null) {
                                    if (processMachlink.getPrepareTime() == null) {
                                        processMachlink.setPrepareTime(0);
                                    }
                                    machineMainfo.setProId(processMachlink.getPrId());
                                    machineMainfoMapper.updateById(machineMainfo);
                                    processMachlinkMapper.insert(processMachlink);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
//        ????????????
        List<ProcessWorkinfo> list = fzProessWorkinfoMapper.list();
//        ????????????
        if (!Func.isEmpty(list)) {
            list.stream().forEach(processWorkinfo -> {
//                ??????????????????????????????
                if (Func.isEmpty(proessWorkinfoMapper.selectById(processWorkinfo.getId()))) {
                    proessWorkinfoMapper.insert(processWorkinfo);
                    //        ????????????????????????
//                    ????????????????????????
                    proessClasslinkMapper.insert(fzProessWorkinfoMapper.getClasslink(processWorkinfo.getId()));
                }
            });
        }
        return R.fail("????????????????????????!");
    }

    @Override
    @GetMapping("/updateCrmCustomer")
    public R updateCrmCustomer() {
//        ??????
        List<CrmCustomer> list = fzCrmCustomerMapper.list();
//        ??????
        if (!Func.isEmpty(list)) {
            list.stream().forEach(crmCustomer -> {
//                ???????????????????????????
                if (Func.isEmpty(crmCustomerMapper.selectById(crmCustomer.getId()))) {
                    crmCustomerMapper.insert(crmCustomer);
                }
            });
        }
        return R.fail("?????????????????????");
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @return
     */
    @Override
    @GetMapping("/updateOrderInfo")
    public R updateOrderInfo() {
//        ??????????????????
        List<ProdClassify> prodClassifyList = fzOrderOrdinfoMapper.getProdClassify();
//        ??????????????????
        if (!Func.isEmpty(prodClassifyList)) {
            Stream.iterate(0, i -> i + 1).limit(prodClassifyList.size()).forEach(i -> {
                if (Func.isEmpty(prodClassifyMapper.selectById(prodClassifyList.get(i).getId()))) {
                    int j = i + 1;
                    if (j < 10) {
                        prodClassifyList.get(i).setClassify("0" + j);
                    } else {
                        prodClassifyList.get(i).setClassify("" + j);
                    }
                    prodClassifyMapper.insert(prodClassifyList.get(i));
                }
            });
        }
        //        ????????????
        List<ProcessWorkinfo> listWorkinfo = fzProessWorkinfoMapper.list();
//        ????????????
        if (!Func.isEmpty(listWorkinfo)) {
            listWorkinfo.stream().forEach(processWorkinfo -> {
//                ??????????????????????????????
                if (Func.isEmpty(proessWorkinfoMapper.selectById(processWorkinfo.getId()))) {
                    proessWorkinfoMapper.insert(processWorkinfo);
                    //        ????????????????????????
//                    ????????????????????????
                    proessClasslinkMapper.insert(fzProessWorkinfoMapper.getClasslink(processWorkinfo.getId()));
                }
            });
        }
//        ????????????
        List<OrderOrdinfo> list = fzOrderOrdinfoMapper.list();
        if (!Func.isEmpty(list)) {
            list.stream().forEach(orderOrdinfo -> {
                if (Func.isEmpty(orderOrdinfoMapper.selectById(orderOrdinfo.getId()))) {
//                    ??????????????????
                    ProdPdinfo prodPdinfo = fzOrderOrdinfoMapper.getProdPdinfo(orderOrdinfo.getPdId());
                    if (!Func.isEmpty(prodPdinfo)) {
                        orderOrdinfoMapper.insert(orderOrdinfo);
                        //                                ????????????????????????
                        OrderWorkbatch orderWorkbatch = new OrderWorkbatch();
                        orderWorkbatch.setOdId(orderOrdinfo.getId());
                        orderWorkbatch.setOdNo(orderOrdinfo.getOdNo());
                        orderWorkbatch.setBatchNo(orderOrdinfo.getOdNo() + "_1");
                        orderWorkbatch.setPlanNum(orderOrdinfo.getOdCount());
                        orderWorkbatch.setStatus(5);//?????????
                        orderWorkbatch.setUserId(1);
                        orderWorkbatch.setWaste(0);
                        orderWorkbatch.setCloseTime(orderOrdinfo.getLimitDate());
                        orderWorkbatchMapper.insert(orderWorkbatch);
//                    ???????????????????????????
//                    ???????????????
                        ActsetCkflow actsetCkflow = actsetCkflowMapper.selectById(15);
//                    ??????????????????
                        ActsetCheckLog actsetCheckLog = new ActsetCheckLog();
                        actsetCheckLog.setAwId(15);
                        actsetCheckLog.setDbId(orderOrdinfo.getId());
                        actsetCheckLog.setUsId(actsetCkflow.getUsId());
                        actsetCheckLog.setStatus(1);//????????????????????????
                        actsetCheckLogMapper.insert(actsetCheckLog);
                        if (Func.isEmpty(prodPdinfoMapper.selectById(prodPdinfo.getId()))) {
                            //                            ??????????????????????????????
                            List<MaterMtinfo> materMtinfos = fzOrderOrdinfoMapper.getMaterMtinfo(prodPdinfo.getId());
                            if (!Func.isEmpty(materMtinfos)) {
                                materMtinfos.stream().forEach(materMtinfo -> {
//                ??????????????????
                                    if (Func.isEmpty(materMtinfoMapper.selectById(materMtinfo.getId()))) {
                                        materMtinfoMapper.insert(materMtinfo);
                                    }
                                });
                            }
//                    ??????????????????????????????????????????????????????
                            ProdPartsinfoVO prodPartsinfoVO = fzOrderOrdinfoMapper.getProdPdinfoVO(prodPdinfo.getId());
                            prodPartsinfoVO.setIDAPComp(null);
//                    ??????????????????
                            List<ProdPartsinfoVO> prodPartsinfoVOS = fzOrderOrdinfoMapper.getProdPartsinfo(prodPdinfo.getId());
                            if (!Func.isEmpty(prodPartsinfoVOS)) {
                                prodPartsinfoVOS.stream().forEach(prodPartsinfo -> {

//                            ??????????????????????????????
                                    if (prodPartsinfo.getIDAPComp().intValue() == prodPartsinfoVO.getIDAComp().intValue()) {
                                        prodPartsinfo.setIDAPComp(null);
                                    }
//                            ?????????????????????
                                    List<MaterProdlink> materProdlinks = fzOrderOrdinfoMapper.getMaterProdlinkByAPComp(prodPartsinfo.getIDAComp(), prodPartsinfo.getPdId());
                                    if (!Func.isEmpty(materProdlinks)) {
                                        prodPartsinfo.setMaterProdlinks(materProdlinks);
                                    }
                                });
                            }
                            prodPartsinfoVOS.add(prodPartsinfoVO);
//                    ??????????????????
                            if (!Func.isEmpty(prodPartsinfoVOS)) {
                                prodPartsinfoVOS.stream().forEach(prodPartsinfoVO1 -> {
                                    List<ProdProcelink> prodProcelinkList = fzOrderOrdinfoMapper.getProdProcelink(prodPartsinfoVO1.getIDAComp());
                                    if (!Func.isEmpty(prodProcelinkList)) {
                                        prodPartsinfoVO1.setProdProcelinks(prodProcelinkList);
                                    }
                                });
                            }
//                    ????????????
                            prodPdinfoMapper.insert(prodPdinfo);
//                    ????????????????????????????????????
//                    ??????????????????
                            List<ProdPartsinfoVO> ls = PartsinfoUtil.listGetStree(prodPartsinfoVOS);
                            savepartsinfo(ls, null);
                            saveProdPartsinfo(ls);
//                            ptLeft = 100;
//                            ptTop = 10;
//                            mtLeft = 20;
//                            mtTop = 10;
//                            prLeft = 200;
//                            prTop = 10;
//                            JsonsRootBean jsonsRootBean = new JsonsRootBean();
//                            jsonsRootBean.setName("");
//                            JsonsRootBean jsons = getPdinfoJSON(ls, jsonsRootBean);
//                            String modelJson = JSONObject.toJSONString(jsons);
//                            System.out.println(modelJson);
//                            prodPdinfo.setModelJson(modelJson);
//                            prodPdinfoMapper.updateById(prodPdinfo);
                        }
                    }
                }
            });
        }
        return R.fail("??????????????????");
    }

    @Override
    @GetMapping("/updatePdClassify")
    public R updatePdClassify() {
        //        ??????????????????
        List<ProdClassify> prodClassifyList = fzOrderOrdinfoMapper.getProdClassify();
//        ??????????????????
        if (!Func.isEmpty(prodClassifyList)) {
            Stream.iterate(0, i -> i + 1).limit(prodClassifyList.size()).forEach(i -> {
                if (Func.isEmpty(prodClassifyMapper.selectById(prodClassifyList.get(i).getId()))) {
                    int j = i + 1;
                    if (j < 10) {
                        prodClassifyList.get(i).setClassify("0" + j);
                    } else {
                        prodClassifyList.get(i).setClassify("" + j);
                    }
                    prodClassifyMapper.insert(prodClassifyList.get(i));
                }
            });
        }
        return R.fail("????????????????????????");
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    @GetMapping("/updateWorkbatch")
    public R updateWorkbatch() {
//       ??????????????????id
        List<Integer> iGzzxIds = fzWorkBatchMapper.listIGzzxId();
        for (Integer iGzzxId : iGzzxIds) {
//            ??????????????????????????????
            List<WorkbatchOrdlink> list = fzWorkBatchMapper.listByIGzzxId(iGzzxId);
            if (!Func.isEmpty(list)) {
                list.stream().forEach(workbatchOrdlink -> {
                    workbatchOrdlink.setSdDate(workbatchOrdlink.getSdDate().substring(0, 10));
//                ??????????????????
                    WorkbatchOrdlink ordlink = workbatchOrdlinkMapper.selectById(workbatchOrdlink.getId());
                    if (Func.isEmpty(ordlink)) {
//                        ??????????????????????????????
//                        ??????????????????
                        Map<String, Object> ordMap = new HashMap<>();
                        ordMap.put("od_no", workbatchOrdlink.getOdNo());
                        List<OrderOrdinfo> ordinfos = orderOrdinfoMapper.selectByMap(ordMap);
//                        ??????????????????????????????
                        if (Func.isEmpty(ordinfos)) {
//                        ?????????,??????????????????
                            System.out.println(workbatchOrdlink);
                            OrderOrdinfo orderOrdinfo = fzOrderOrdinfoMapper.listByOdNo(workbatchOrdlink.getOdNo());
                            workbatchOrdlink.setCloseTime(orderOrdinfo.getLimitDate());
//                    ??????????????????
                            ProdPdinfo prodPdinfo = fzOrderOrdinfoMapper.getProdPdinfo(orderOrdinfo.getPdId());
//                            ??????????????????
                            if (prodPdinfo.getPcId() != null) {
                                workbatchOrdlink.setPdType(prodClassifyMapper.selectById(prodPdinfo.getPcId()).getClName());
                            }
                            if (!Func.isEmpty(prodPdinfo)) {
                                orderOrdinfoMapper.insert(orderOrdinfo);
//                                ????????????????????????
                                OrderWorkbatch orderWorkbatch = new OrderWorkbatch();
                                orderWorkbatch.setOdId(orderOrdinfo.getId());
                                orderWorkbatch.setOdNo(orderOrdinfo.getOdNo());
                                orderWorkbatch.setBatchNo(orderOrdinfo.getOdNo() + "_1");
                                orderWorkbatch.setPlanNum(orderOrdinfo.getOdCount());
                                orderWorkbatch.setStatus(5);//?????????
                                orderWorkbatch.setUserId(1);
                                orderWorkbatch.setWaste(0);
                                orderWorkbatch.setCloseTime(orderOrdinfo.getLimitDate());
                                orderWorkbatchMapper.insert(orderWorkbatch);
                                workbatchOrdlink.setWbId(orderWorkbatch.getId());
//                    ???????????????????????????
//                    ???????????????
                                ActsetCkflow actsetCkflow = actsetCkflowMapper.selectById(15);
//                    ??????????????????
                                ActsetCheckLog actsetCheckLog = new ActsetCheckLog();
                                actsetCheckLog.setAwId(15);
                                actsetCheckLog.setDbId(orderOrdinfo.getId());
                                actsetCheckLog.setUsId(actsetCkflow.getUsId());
                                actsetCheckLog.setStatus(1);//????????????????????????
                                actsetCheckLogMapper.insert(actsetCheckLog);
                                if (Func.isEmpty(prodPdinfoMapper.selectById(prodPdinfo.getId()))) {
                                    //                            ??????????????????????????????
                                    List<MaterMtinfo> materMtinfos = fzOrderOrdinfoMapper.getMaterMtinfo(prodPdinfo.getId());
                                    if (!Func.isEmpty(materMtinfos)) {
                                        materMtinfos.stream().forEach(materMtinfo -> {
//                ??????????????????
                                            if (Func.isEmpty(materMtinfoMapper.selectById(materMtinfo.getId()))) {
                                                materMtinfoMapper.insert(materMtinfo);
                                            }
                                        });
                                    }
//                    ??????????????????????????????????????????????????????
                                    ProdPartsinfoVO prodPartsinfoVO = fzOrderOrdinfoMapper.getProdPdinfoVO(prodPdinfo.getId());
                                    prodPartsinfoVO.setIDAPComp(null);
//                    ??????????????????
                                    List<ProdPartsinfoVO> prodPartsinfoVOS = fzOrderOrdinfoMapper.getProdPartsinfo(prodPdinfo.getId());
                                    if (!Func.isEmpty(prodPartsinfoVOS)) {
                                        prodPartsinfoVOS.stream().forEach(prodPartsinfo -> {

//                            ??????????????????????????????
                                            if (prodPartsinfo.getIDAPComp().intValue() == prodPartsinfoVO.getIDAComp().intValue()) {
                                                prodPartsinfo.setIDAPComp(null);
                                            }
//                            ?????????????????????
                                            List<MaterProdlink> materProdlinks = fzOrderOrdinfoMapper.getMaterProdlinkByAPComp(prodPartsinfo.getIDAComp(), prodPartsinfo.getPdId());
                                            if (!Func.isEmpty(materProdlinks)) {
                                                prodPartsinfo.setMaterProdlinks(materProdlinks);
                                            }
                                        });
                                    }
                                    prodPartsinfoVOS.add(prodPartsinfoVO);
//                    ??????????????????
                                    if (!Func.isEmpty(prodPartsinfoVOS)) {
                                        prodPartsinfoVOS.stream().forEach(prodPartsinfoVO1 -> {
                                            List<ProdProcelink> prodProcelinkList = fzOrderOrdinfoMapper.getProdProcelink(prodPartsinfoVO1.getIDAComp());
                                            if (!Func.isEmpty(prodProcelinkList)) {
                                                prodPartsinfoVO1.setProdProcelinks(prodProcelinkList);
                                            }
                                        });
                                    }
//                    ????????????
                                    prodPdinfoMapper.insert(prodPdinfo);
//                    ????????????????????????????????????
//                    ??????????????????
                                    List<ProdPartsinfoVO> ls = PartsinfoUtil.listGetStree(prodPartsinfoVOS);
                                    savepartsinfo(ls, null);
                                    saveProdPartsinfo(ls);
                                }
                            }
//                            ?????????????????????id ?????? ??????id?????????
                            List<ProdPartsinfo> prodPartsinfos = prodPartsinfoMapper.byPdIdAndPtName(workbatchOrdlink.getPartName(), prodPdinfo.getId());
                            if (!Func.isEmpty(prodPartsinfos) && prodPartsinfos.size() > 1) {
//                                ??????????????????
                                prodPartsinfos.stream().forEach(partsinfo -> {
                                    Integer ptId = prodPartsinfoMapper.byPId(partsinfo.getId());
                                    if (ptId == null) {
                                        workbatchOrdlink.setPtId(partsinfo.getId());
                                    }
                                });
                            } else if (!Func.isEmpty(prodPartsinfos) && prodPartsinfos.size() == 1) {
                                workbatchOrdlink.setPtId(prodPartsinfos.get(0).getId());
                            }

                        } else {
                            workbatchOrdlink.setCloseTime(ordinfos.get(0).getLimitDate());
//                        ??????????????????????????????
//                        ??????????????????????????????????????????id?????????
                            Map<String, Object> pdMap = new HashMap<>();
                            pdMap.put("pd_no", workbatchOrdlink.getPdCode());
                            List<ProdPdinfo> pdList = prodPdinfoMapper.selectByMap(pdMap);
                            if (pdList.get(0).getPcId() != null) {
                                workbatchOrdlink.setPdType(prodClassifyMapper.selectById(pdList.get(0).getPcId()).getClName());
                            }
//                            ????????????
                            Map<String, Object> wbMap = new HashMap<>();
                            wbMap.put("od_no", workbatchOrdlink.getOdNo());
                            List<OrderWorkbatch> workbatches = orderWorkbatchMapper.selectByMap(wbMap);
                            if (!Func.isEmpty(workbatches)) {
                                workbatchOrdlink.setWbId(workbatches.get(0).getId());
                            }
                            List<ProdPartsinfo> prodPartsinfos = prodPartsinfoMapper.byPdIdAndPtName(workbatchOrdlink.getPartName(), pdList.get(0).getId());
                            if (!Func.isEmpty(prodPartsinfos) && prodPartsinfos.size() > 1) {
//                                ??????????????????
                                prodPartsinfos.stream().forEach(partsinfo -> {
                                    Integer ptId = prodPartsinfoMapper.byPId(partsinfo.getId());
                                    if (ptId == null) {
                                        workbatchOrdlink.setPtId(partsinfo.getId());
                                    }
                                });
                            } else if (!Func.isEmpty(prodPartsinfos) && prodPartsinfos.size() == 1) {
                                workbatchOrdlink.setPtId(prodPartsinfos.get(0).getId());
                            }
                        }
//                        ??????????????????
                        Integer sdSort = workbatchOrdlinkMapper.getSdSort();
                        if (sdSort == null) {
                            sdSort = 1;
                        } else {
                            sdSort++;
                        }
                        workbatchOrdlink.setSdSort(sdSort);
                        workbatchOrdlinkMapper.insert(workbatchOrdlink);

                        WorkbatchOrdoee workbatchOrdoee = new WorkbatchOrdoee();

//                        ???????????????????????????
                        Map<String, Object> machMap = new HashMap<>();
                        machMap.put("ma_id", workbatchOrdlink.getMaId());
                        machMap.put("pr_id", workbatchOrdlink.getPrId());
                        List<ProcessMachlink> Machlinks = processMachlinkMapper.selectByMap(machMap);
                        if (!Func.isEmpty(Machlinks)) {
                            workbatchOrdoee.setSpeed(Machlinks.get(0).getSpeed());
                        }

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
                        workbatchOrdoee.setQualityNum(1);
                        workbatchOrdoee.setDifficultNum(1.0);
                        workbatchOrdoee.setProducePreTime(0);
                        workbatchOrdoeeMapper.insert(workbatchOrdoee);
                    }
                });
            }
        }
        return R.fail("??????????????????");
    }

    /**
     * ????????????id??????????????????
     *
     * @param Id
     * @return
     */
    @Override
    @GetMapping("/updateWorkbatchById")
    public R updateWorkbatchById(Integer Id) {
        //????????????id????????????????????????
        WorkbatchOrdlink ordlinkLocal = workbatchOrdlinkMapper.selectById(Id);
        if (ordlinkLocal != null && ordlinkLocal.getId() > 0) {
            //???????????????fz????????????
            WorkbatchOrdlink ordlinkFZ = fzWorkBatchMapper.selectById(Id);
            ordlinkLocal.setPlanNum(ordlinkFZ.getPlanNum());
            ordlinkLocal.setPlanNumber(ordlinkFZ.getPlanNumber());
            ordlinkLocal.setCompleteNum(ordlinkFZ.getCompleteNum());
            ordlinkLocal.setIncompleteNum(ordlinkFZ.getIncompleteNum());
            ordlinkLocal.setExtraNum(ordlinkFZ.getExtraNum());
            //??????
            workbatchOrdlinkMapper.updateById(ordlinkLocal);//????????????
        }
        return R.fail("?????????????????????");
    }


    @Override
    @GetMapping("/updateDept")
    @Transactional
    public R updateDept() {
        List<BaseDeptInfo> list = fzDeptMapper.list();
        if (!list.isEmpty()) {
            for (BaseDeptInfo deptInfo : list) {
                try {
                    BaseDeptInfo baseDeptInfo = baseDeptInfoMapper.selectById(deptInfo.getId());
                    if (baseDeptInfo == null) {
                        deptInfo.setPId(1);
                        deptInfo.setClassify(2);
                        baseDeptInfoMapper.insert(deptInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return R.success("ok");
    }


    private void saveStaffext() {
        List<BaseStaffext> staffextList = baseStaffextMapper.selectList(null);
        List<BaseStaffext> fzStaffextList = fzStaffextMapper.list();
        if (!staffextList.isEmpty()) {
            List<Integer> ids = staffextList.stream().map(BaseStaffext::getId).collect(Collectors.toList());
            fzStaffextList = fzStaffextList.stream().filter(o -> {
                return !ids.contains(o.getId());
            }).collect(Collectors.toList());
            if (fzStaffextList.isEmpty()) {
                fzStaffextList.forEach(o -> {
                    o.setSfId(o.getId());
                    baseStaffextMapper.insert(o);
                });
            }
        } else {
            fzStaffextList.forEach(o -> {
                o.setSfId(o.getId());
                baseStaffextMapper.insert(o);
            });
        }
    }

    private void saveFzBladeUser() {
        List<SaUser> saUsers = fzSaUserMapper.getUserList();
        List<SaUser> userList = saUserMapper.selectList(null);
        if (!userList.isEmpty()) {
            //????????????????????????id
            List<Integer> ids = userList.stream().map(SaUser::getId).collect(Collectors.toList());
            saUsers = saUsers.stream().filter(o -> {
                return !ids.contains(o.getId());
            }).collect(Collectors.toList());
            //????????????
            if (!saUsers.isEmpty()) {
                for (SaUser saUser : saUsers) {
                    try {
                        saUser.setPassword(DigestUtil.sha1(saUser.getPassword()));
                        saUser.setTenantId("000000");
                        saUser.setRoleId("13");
                        saUser.setStatus(1);
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
                o.setPassword(DigestUtil.sha1(o.getPassword()));
                o.setTenantId("000000");
                o.setRoleId("13");
                o.setStatus(1);
                saUserMapper.insert(o);
            });
        }
    }

    private void saveFzStaffInfo() {
        List<BaseStaffinfo> baseStaffinfos = baseStaffinfoMapper.selectList(null);
        List<BaseStaffinfo> fzBaseStaffinfos = fzStaffinfoMapper.list();
        if (!baseStaffinfos.isEmpty()) {
            //????????????????????????id
            List<Integer> ids = baseStaffinfos.stream().map(BaseStaffinfo::getId).collect(Collectors.toList());
            fzBaseStaffinfos = fzBaseStaffinfos.stream().filter(o -> {
                return !ids.contains(o.getId());
            }).collect(Collectors.toList());
            if (!fzBaseStaffinfos.isEmpty()) {
                fzBaseStaffinfos.forEach(o -> {
                    o.setUserId(o.getId());
                    baseStaffinfoMapper.insert(o);
                });
            }
        } else {
            fzBaseStaffinfos.forEach(o -> {
                o.setUserId(o.getId());
                baseStaffinfoMapper.insert(o);
            });
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
}
