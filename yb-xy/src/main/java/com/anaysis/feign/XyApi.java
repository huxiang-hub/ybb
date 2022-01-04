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
    //部件坐标
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
            //存储bladeuser
            saveFzBladeUser();
            //存储staffInfo
            saveFzStaffInfo();
            //存储staffext
            saveStaffext();
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
            massage(stackTraceElement);
            return R.fail("更新失败");
        }
        return R.success("ok");
    }

    @Override
    @GetMapping("/updateMachine")
    public R updateMachine() {
//        导入设备类型/型号
        List<MachineClassify> classifyList = xyMachineDao.getMachineClassify();
        List<MachineClassify> classifies = machineClassifyMapper.selectList(null);

        if (!classifies.isEmpty()) {
            //系统已存在的设备类型/型号
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
                        machineMainfo.setIsRecepro(0);//默认按照谁被接单
                        machineMainfo.setMtId(machineClassifyMapper.getByerpId(machineMainfovo.getMtErp()));//查询设备型号
                        machineMainfo.setDpId(baseDeptInfoMapper.getByDpNum(machineMainfovo.getDpNum()));//查询设备所属车间
                        //查询设备工序
                        List<ProcessMachlinkVO> processMachlinks = xyMachineDao.getProcessMachlink(machineMainfovo.getErpId());
                        machineMainfo.setProId(!Func.isEmpty(processMachlinks) ? proessWorkinfoMapper.getByerpId(processMachlinks.get(0).getPrErp()) : null);
//                        保存设备信息
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
            //查询设备工序
            List<ProcessMachlinkVO> processMachlinks = xyMachineDao.getProcessMachlink(machineMainfo.getErpId());
            processMachlinks.stream().forEach(processMachlinkVO -> {
                Map<String, Object> map = new HashMap<>();
                map.put("ma_id", machineMainfo.getId());
                Integer prId = proessWorkinfoMapper.getByerpId(processMachlinkVO.getPrErp());
                map.put("pr_id", prId);
                if (Func.isEmpty(processMachlinkMapper.selectByMap(map))) {
//                        不存在则插入
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
            //        查询工序分类
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
            //        查询工序
            List<ProcessWorkinfoVO> list = xyProessWorkinfoMapper.list();
            List<ProcessWorkinfo> workinfos = proessWorkinfoMapper.selectList(null);
            if (!Func.isEmpty(workinfos)) {
                List<String> erpIds = workinfos.stream().map(ProcessWorkinfo::getErpId).collect(Collectors.toList());
                list = list.stream().filter(o -> {
                    return !erpIds.contains(o.getErpId());
                }).collect(Collectors.toList());
                list.forEach(o -> {
                    //保存工序
                    ProcessWorkinfo processWorkinfo = new ProcessWorkinfo();
                    processWorkinfo.setErpId(o.getErpId());
                    processWorkinfo.setPrName(o.getPrName());
                    processWorkinfo.setPrNo(o.getPrNo());
                    processWorkinfo.setStatus(o.getStatus());
                    processWorkinfo.setIsdel(o.getIsdel());
                    proessWorkinfoMapper.insert(processWorkinfo);
                    //        查询此工序的分类
                    ProcessClassify processClassify = processClassifyMapper.getClassifypyNum(o.getPyNum());
                    ProcessClasslink processClasslink = new ProcessClasslink();
                    if (!Func.isEmpty(processClassify)) {
                        processClasslink.setPrId(processWorkinfo.getId());
                        processClasslink.setPyId(processClassify.getId());
                        //                    保存次工序的分类
                        proessClasslinkMapper.insert(processClasslink);
                    }
                });
            } else {
                list.forEach(o -> {
                    //保存工序
                    ProcessWorkinfo processWorkinfo = new ProcessWorkinfo();
                    processWorkinfo.setErpId(o.getErpId());
                    processWorkinfo.setPrName(o.getPrName());
                    processWorkinfo.setPrNo(o.getPrNo());
                    processWorkinfo.setStatus(o.getStatus());
                    processWorkinfo.setIsdel(o.getIsdel());
                    proessWorkinfoMapper.insert(processWorkinfo);
                    //        查询此工序的分类
                    ProcessClassify processClassify = processClassifyMapper.getClassifypyNum(o.getPyNum());
                    ProcessClasslink processClasslink = new ProcessClasslink();
                    if (!Func.isEmpty(processClassify)) {
                        processClasslink.setPrId(processWorkinfo.getId());
                        processClasslink.setPyId(processClassify.getId());
                        //                    保存次工序的分类
                        proessClasslinkMapper.insert(processClasslink);
                    }
                });
            }

            // 导入物料与物料类型
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
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
            massage(stackTraceElement);
            return R.fail("操作失败");
        }
        return R.success("工序物料相关导入成功!");
    }

    @Override
    @GetMapping("/updateCrmCustomer")
    public R updateCrmCustomer() {
        try {
            //        查询 导入客户
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
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
            massage(stackTraceElement);
            return R.fail("操作失败");
        }
        return R.success("客户导入成功！");
    }

    /**
     * 订单相关的产品信息，工序信息，部件信息，物料信息，产品类型，产品类型，工序类型，工序与工序类型相关
     *
     * @return
     */
    @Override
    @GetMapping("/updateOrderInfo")
    public R updateOrderInfo() {
        return R.success("订单导入成功");
    }

    @Override
    @GetMapping("/updatePdClassify")
    public R updatePdClassify() {
        try {
            //        查询产品分类
            List<ProdClassify> prodClassifyList = xyOrderOrdinfoMapper.getProdClassify();
            //        保存产品分类
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
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
            massage(stackTraceElement);
            return R.fail("操作失败");
        }
        return R.success("产品分类导入成功");
    }

    /**
     * 更新 导入排产表内容信息
     *
     * @return
     */
//    @Override
    @GetMapping("/updateWorkbatch1")
    public R updateWorkbatch1(Integer maId) {
        try {
//            删除排产
            updateOrdlink();
            List<WorkbatchOrdlinkVO> workbatchOrdlinkList = new ArrayList<>();
            if (maId != null && maId > 0) {
                // 如果设备id不为空则根据设备id查询
                MachineMainfo machine = machineMainfoMapper.selectById(maId);
                workbatchOrdlinkList = xyWorkBatchMapper.listByerpId(machine.getErpId());
            } else {
                // 设备id为空查询所有设备的erpId
                List<MachineMainfo> machines = machineMainfoMapper.selectList(null);
//                workbatchOrdlinkList = xyWorkBatchMapper.listByIGzzxId(machines);//sql todo
            }
            // 查询排产单 tod
            List<WorkbatchOrdlink> workbatchs = workbatchOrdlinkMapper.selectList(null);
            if (!Func.isEmpty(workbatchs)) {
                //系统已存在的排产单
                List<String> erpIds = workbatchs.stream().map(WorkbatchOrdlink::getErpId).collect(Collectors.toList());
                workbatchOrdlinkList = workbatchOrdlinkList.stream().filter(o -> {
                    return !erpIds.contains(o.getErpId());
                }).collect(Collectors.toList());

                if (!workbatchOrdlinkList.isEmpty()) { // erp系统比我们系统多的排产单不为空
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
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
            massage(stackTraceElement);
            return R.fail("更新失败");
        }

        return R.success("排产导入成功");
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
                // 只需要同步当前设备
                String m = maId.toString();
                MachineMainfo machineMainfo = machineMainfoMapper.selectById(m);
                maIds.add(machineMainfo.getErpId());
                myIds = workbatchOrdlinkMapper.getErpIdByMaId(m);
            } else {
                // 同步所有
                maIds = machineMainfoMapper.getErpIds();
                myIds = workbatchOrdlinkMapper.getErpId();
            }
            List<String> xyAllIds = xyWorkBatchMapper.getAllIdsByMaIds(maIds);
            xyIds = xyWorkBatchMapper.getIdsByMaIds(maIds);
            List<String> finalMyIds = myIds;
            List<String> addIds = xyIds.stream().filter(i -> !finalMyIds.contains(i)).collect(Collectors.toList());
            List<String> moreIds = myIds.stream().filter(i -> !xyAllIds.contains(i)).collect(Collectors.toList());
            // 验证并删除排产
            deleteWorkbatch(moreIds);
            List<WorkbatchOrdlinkVO> workbatchOrdlinkList = new ArrayList<>();
            // list分切，1000一组
            Integer MAX_NUMBER = 1000;
            int limit = (addIds.size() + MAX_NUMBER - 1) / MAX_NUMBER;
            List<List<String>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> addIds.stream().skip(a * MAX_NUMBER).limit(MAX_NUMBER).parallel().collect(Collectors.toList())).collect(Collectors.toList());
            for (List<String> ids : splitList) {
                workbatchOrdlinkList.addAll(xyWorkBatchMapper.getByIds(ids));
            }
            log.info("=========本次同步数据：{}条==============", workbatchOrdlinkList.size());
            if (!workbatchOrdlinkList.isEmpty()) { // erp系统比我们系统多的排产单不为空
                workbatchOrdlinkList.forEach(o -> {
                    try {
                        newWorkbatchordlink(o);
                    } catch (Exception e) {
                        dingClient.send("同步erpId为【" + o.getErpId() + "】时发生错误，错误原因为：" + e.getMessage(), "InternalH5");
                        log.error("=======导入异常订单erpId为：{}=======\n异常原因：{}", o.getErpId(), e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
            massage(stackTraceElement);
            return R.fail("更新失败");
        }
        return R.success("排产导入成功");
    }


    /**
     * 验证并删除排产
     * 1、兴艺完工的并且我们shift表有的修改状态为10（强制完工），如果兴艺已完工，我们还未生成shift，则删除
     * 2、兴艺不存在则直接删除
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
     * 未导入排产单（查询出未同步到我们系统的排产单id）
     *
     * @return
     */
//    @Override
    @GetMapping("/noImport")
    public R noImport() {
        return R.data(noImportXyData());
    }

    /**
     * 根据erpId同步一条数据（仅仅只是同步，用于检查同步出错的数据）
     *
     * @param erpId
     * @return
     */
    @GetMapping("/updateByErpId")
    public R updateByErpId(String erpId) {
        WorkbatchOrdlinkVO workbatchOrdlinkVO = xyWorkBatchMapper.selectOneByErpId(erpId);
        if (null == workbatchOrdlinkVO) {
            return R.fail("同步数据为空");
        }
        try {
            newWorkbatchordlink(workbatchOrdlinkVO);
            return R.success("同步成功");
        } catch (ParseException e) {
            log.info("==========同步erpId为{}的数据出错：\n{}===============", erpId, e.getMessage());
            e.printStackTrace();
        }
        return R.fail("同步出错");
    }

    /**
     * 查询未导入的xy排产单ids
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
     * 之前版本
     */
    private void updateOrdlink() {
        // 查询本地所有-1（待排产）的排产单
        List<det> list = workbatchOrdlinkMapper.selectByStatusReturnErpId("-1");
        if (!list.isEmpty()) {
            // 查询所有设备的erpId
            List<MachineMainfo> machines = machineMainfoMapper.selectList(null);
            // 查询erp所有订单erpId
            List<String> erpIds = xyWorkBatchMapper.erpAll(machines);//sql
            list = list.stream().filter(o -> {
                return !erpIds.contains(o.getErpId());
            }).collect(Collectors.toList());
            List<Integer> ids = list.stream().map(p -> p.getId()).collect(Collectors.toList());
            // 删除排产单
            if (!Func.isEmpty(ids)) {
                workbatchOrdlinkMapper.deleteBatchIds(ids);
            }
        }

    }


//    private void workbatchordlink (WorkbatchOrdlinkVO o) {
////                查询订单是否存在
//            if (Func.isEmpty(orderOrdinfoMapper.getByOdNo(o.getOdNo()))) {
////                    查找订单信息
//                OrderOrdinfoVO orderOrdinfovo = xyOrderOrdinfoMapper.listByOdNo(o.getOdNo());
//                orderOrdinfovo.setLimitDate(o.getCloseTime());
////                    查询产品对应的产品是否存在
//                if (!Func.isEmpty(prodPdinfoMapper.getByerpId(o.getPdErp()))) {
//                        ProdPdinfo prodPdinfo = prodPdinfoMapper.getByerpId(o.getPdErp());
//                        Map<String,Object> partMap = new HashMap<>();
//                        partMap.put("pd_id",prodPdinfo.getId());
//                        orderOrdinfovo.setPdId(prodPdinfo.getId());
//                        List<ProdPartsinfo> partsinfoList = prodPartsinfoMapper.selectByMap(partMap);
//                        partsinfoList.stream().forEach(parts -> {
//                            if (o.getPartErp() == null || o.getPartErp().equals("")) {
//                                if (parts.getErpId().equals(o.getPdErp())) {
////                                    查询部件规格
//                                    ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getOneProdPartsinfo(o.getPdErp1(),o.getPdErp());
//                                    if (!Func.isEmpty(prodPartsinfoVO)){
//                                        o.setPtSize(prodPartsinfoVO.getPtSize());
//                                    }
//                                    o.setPtId(parts.getId());
//                                    o.setPtNo(parts.getPtNo());
//                                }
//                            }else {
//                                if (parts.getErpId().equals(o.getPartErp())) {
////                                    查询部件规格
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
////                        查询产品信息(编号)
//                    ProdPdinfoVO pdinfoVO = xyOrderOrdinfoMapper.getProdPdinfo(o.getPdCode());
////                        查询类型
//                    ProdClassify prodClassify = prodClassifyMapper.getByerpId(pdinfoVO.getPcErp());
//                    ProdPdinfo pdinfo = new ProdPdinfo();
//                    pdinfo.setPdNo(pdinfoVO.getPdNo());
//                    pdinfo.setPdName(pdinfoVO.getPdName());
//                    pdinfo.setErpId(pdinfoVO.getErpId());
//                    if (!Func.isEmpty(prodClassify)) {
//                        pdinfo.setPcId(prodClassify.getId());
//                    }
////                        保存产品
//                    prodPdinfoMapper.insert(pdinfo);
//                    orderOrdinfovo.setPdId(pdinfo.getId());
////                        保存产品对应的部件，部件对应的工序，部件对应的物料
////                        查询部件
//                    List<ProdPartsinfoVO> prodPartsinfoVOS = xyOrderOrdinfoMapper.getProdPartsinfo(o.getPdCode());
////                        部件只发现有一层
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
//////                          查询部件的工序
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
////                          查询的物料
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
////                          查询部件的工序
//                            List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getProdProcelink(o.getPartErp());
//                            if (!Func.isEmpty(prodProcelinkVOS)) {
//                                int a = 0;
//                                int b = 0;
//                                for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
//                                    if(b == 0 && prodProcelinkVO.getLb() == 2) {
////                                        插入印刷工序
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
////                    查询产品工序（产品作为部件保存）
////                      查询是否有产品作为部件
//                    if ((xyOrderOrdinfoMapper.getPdw(o.getWbNo())+ xyOrderOrdinfoMapper.getPdy(o.getWbNo())) > 0) {
////                        保存产品作为部件
//                        ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
//                        prodPartsinfo.setErpId(o.getPdErp());
//                        prodPartsinfo.setPtName("产品");
//                        prodPartsinfo.setPtClassify(1);
//                        prodPartsinfo.setPdType(1);
//                        prodPartsinfo.setPdId(pdinfo.getId());
//                        prodPartsinfoMapper.insert(prodPartsinfo);
//                        if (o.getPartErp() == null || o.getPartErp() == "") {
//                            o.setPtSize(o.getPtSize());
//                            o.setPtId(prodPartsinfo.getId());
//                            o.setPtNo(prodPartsinfo.getPtNo());
//                        }
////                        查询产品的物料
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
////                        查询产品的工序
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
////                   保存订单
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
////                    查询当前审核的信息
////                    查询到审核
//                ActsetCkflow actsetCkflow = actsetCkflowMapper.selectById(15);
////                    插入审核列表
//                ActsetCheckLog actsetCheckLog = new ActsetCheckLog();
//                actsetCheckLog.setAwId(15);
//                actsetCheckLog.setDbId(orderOrdinfo.getId());
//                actsetCheckLog.setUsId(actsetCkflow.getUsId());
//                actsetCheckLog.setStatus(1);//设置状态为待审核
//                actsetCheckLogMapper.insert(actsetCheckLog);
////                    查工单
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
//////                            插入一条工序关联
////                        prodProcelinkMapper.insert(link);
////                    }
//                }else{
//                    o.setWbId(w.getId());
//                    link.setWbId(w.getId());
//                    link.setWbNo(w.getBatchNo());
////                    if (o.getPtId() == null) {
//////                            插入一条工序关联
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
////                        判断设备类型
//                    MachineClassify machineClassify = machineClassifyMapper.selectById(machineMainfo.getMtId());
//                    workbatchOrdlink.setUsId(saUserMapper.getUserIdByerpId(o.getUsErp()));
//                    workbatchOrdlinkMapper.insert(workbatchOrdlink);
//                    if (machineClassify.getModel().equals("印刷设备")) {
////                            保存扩展表
//                        WorkBatchExpprint workBatchExpprint = new WorkBatchExpprint();
//                        workBatchExpprint.setSdId(workbatchOrdlink.getId());
//                        workBatchExpprint.setColorNum(o.getColorNum());
//                        workBatchExpprint.setCtpNo(o.getCtpNo());
//                        workBatchExpprint.setCtpTime(o.getCtpTime());
//                        workBatchExpprint.setPaintColour(o.getPaintColour());
//                        workBatchExpprint.setVersioncClass(o.getVersioncClass());
//                        workbatchExpprintMapper.insert(workBatchExpprint);
//                    }else if (machineClassify.getModel().equals("模切设备")) {
////                            保存对应扩展表
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
////                    存在
////                    查询工单是否存在
//                OrderWorkbatch orderWorkbatch = orderWorkbatchMapper.getByerpId(o.getWbNo());
//                ProdProcelink link = new ProdProcelink();
//                if (Func.isEmpty(orderWorkbatch)) {
////                            不存在，插入此工单
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
////                        查询产品
//                ProdPdinfo prodPdinfos = prodPdinfoMapper.getByerpId(o.getPdErp());
//                if (!Func.isEmpty(prodPdinfos)) {
//                    ProdPdinfo prodPdinfo = prodPdinfoMapper.getByerpId(o.getPdErp());
//                    Map<String,Object> partMap = new HashMap<>();
//                    partMap.put("pd_id",prodPdinfo.getId());
//                    List<ProdPartsinfo> partsinfoList = prodPartsinfoMapper.selectByMap(partMap);
//                    partsinfoList.stream().forEach(parts -> {
//                        if (o.getPartErp() == null || o.getPartErp().equals("")) {
//                            if (parts.getErpId().equals(o.getPdErp())) {
////                                    查询部件规格
//                                ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getOneProdPartsinfo(o.getPdErp1(),o.getPdErp());
//                                if (!Func.isEmpty(prodPartsinfoVO)){
//                                    o.setPtSize(prodPartsinfoVO.getPtSize());
//                                }
//                                o.setPtId(parts.getId());
//                                o.setPtNo(parts.getPtNo());
//                            }
//                        }else {
//                            if (parts.getErpId().equals(o.getPartErp())) {
////                                    查询部件规格
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
////                        查询产品信息(编号)
//                    ProdPdinfoVO pdinfoVO = xyOrderOrdinfoMapper.getProdPdinfo(o.getPdCode());
////                        查询类型
//                    ProdClassify prodClassify = prodClassifyMapper.getByerpId(pdinfoVO.getPcErp());
//                    ProdPdinfo pdinfo = new ProdPdinfo();
//                    pdinfo.setPdNo(pdinfoVO.getPdNo());
//                    pdinfo.setPdName(pdinfoVO.getPdName());
//                    pdinfo.setErpId(pdinfoVO.getErpId());
//                    if (!Func.isEmpty(prodClassify)) {
//                        pdinfo.setPcId(prodClassify.getId());
//                    }
////                        保存产品
//                    prodPdinfoMapper.insert(pdinfo);
////                        保存产品对应的部件，部件对应的工序，部件对应的物料
////                        查询部件
//                    List<ProdPartsinfoVO> prodPartsinfoVOS = xyOrderOrdinfoMapper.getProdPartsinfo(o.getPdCode());
////                        部件只发现有一层
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
//////                          查询部件的工序
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
////                          查询的物料
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
////                          查询部件的工序
//                            List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getProdProcelink(o.getPartErp());
//                            if (!Func.isEmpty(prodProcelinkVOS)) {
//                                int a = 0;
//                                int b = 0;
//                                for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
//                                    if(b == 0 && prodProcelinkVO.getLb() == 2) {
////                                        插入印刷工序
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
////                    查询产品工序（产品作为部件保存）
////                      查询是否有产品作为部件
//                    if ((xyOrderOrdinfoMapper.getPdw(o.getWbNo())+ xyOrderOrdinfoMapper.getPdy(o.getWbNo())) > 0) {
////                        保存产品作为部件
//                        ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
//                        prodPartsinfo.setErpId(o.getPdErp());
//                        prodPartsinfo.setPtName("产品");
//                        prodPartsinfo.setPtClassify(1);
//                        prodPartsinfo.setPdType(1);
//                        prodPartsinfo.setPdId(pdinfo.getId());
//                        prodPartsinfoMapper.insert(prodPartsinfo);
//                        if (o.getPartErp() == null || o.getPartErp() == "") {
//                            o.setPtSize(o.getPtSize());
//                            o.setPtId(prodPartsinfo.getId());
//                            o.setPtNo(prodPartsinfo.getPtNo());
//                        }
////                        查询产品的物料
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
////                        查询产品的工序
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
     * =====================================================最新排产导入start
     *
     * @param o
     */
    private void newWorkbatchordlink(WorkbatchOrdlinkVO o) throws ParseException {
        // 查询最终交期(计划交货日期)
        o.setFinalTime(xyOrderOrdinfoMapper.getFinalTime(o.getOdNo(), o.getWbNo(), o.getCrmOdNo()));
        // 查询部件是否存在
        ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
        // 查询本地库部件信息是否存在
        if (StringUtils.isNotBlank(o.getPartErp())) {
            prodPartsinfo = prodPartsinfoMapper.getByerpId(o.getPartErp());
        }
        // 部件本地是否存在
        if (!Func.isEmpty(prodPartsinfo)) {
            // 存在,查询出工序id，设置进排产表
            // 查询部件规格
//            ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getOneProdPartsinfo(o.getPdErp1(),o.getPdErp());
            o.setPtId(prodPartsinfo.getId());
            o.setPtNo(prodPartsinfo.getPtNo());
            // 根据部件id查询对应工序
            Map<String, Object> processMap = new HashMap<>();
            processMap.put("pt_id", prodPartsinfo.getId());
            List<ProdProcelink> procelinkList = prodProcelinkMapper.selectByMap(processMap);
            procelinkList.forEach(procelink -> {
                ProcessWorkinfo processWorkinfo = proessWorkinfoMapper.selectById(procelink.getPrId());
                // 如果对应工序不为空且排产单工序uuid等于华博erp的uuid
                if ((!Func.isEmpty(processWorkinfo)) && o.getPrErp().equals(processWorkinfo.getErpId())) {
                    o.setPrId(processWorkinfo.getId());
                }
            });
        } else {
            // 本地库部件不存在 为空可能为产品排产
            if (StringUtils.isBlank(o.getPartErp())) {
                // 产品作为部件
                // 查询产品工序（产品作为部件保存）
                // 查询是否有产品作为部件
                if ((xyOrderOrdinfoMapper.getPdw(o.getWbNo()) + xyOrderOrdinfoMapper.getPdy(o.getWbNo())) > 0) {
                    // 保存产品作为部件
                    ProdPartsinfo partsinfo = new ProdPartsinfo();
                    partsinfo.setErpId(o.getPdErp());
                    partsinfo.setPtName("产品");
                    partsinfo.setPtClassify(1);
                    partsinfo.setPdType(1);
                    prodPartsinfoMapper.insert(partsinfo);
                    o.setPtId(partsinfo.getId());
                    o.setPtNo(partsinfo.getPtNo());
                    // 查询产品的物料
                    List<MaterProdlinkVO> materList = xyOrderOrdinfoMapper.getMaterProdlinkByPd(o.getWbNo());
                    List<MaterProdlinkVO> materFLList = xyOrderOrdinfoMapper.getMaterProdlinkByPdFL(o.getWbNo());
                    List<String> erpIds = materFLList.stream().map(MaterProdlinkVO::getMtErp).collect(Collectors.toList());
                    // 物料过滤掉辅料
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
                    // 辅料信息存储
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
                    // 查询产品的工序
                    List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getlinkW(o.getWbNo());// 未完成工序
                    List<ProdProcelinkVO> prodProcelinkVOSY = xyOrderOrdinfoMapper.getlinkY(o.getWbNo());// 已完成工序
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
                // 保存部件
                ProdPartsinfoVO prodPartsinfoVO = xyOrderOrdinfoMapper.getPartBySCDHBJID(o.getPartErp());
                ProdPartsinfo parts = new ProdPartsinfo();
                BeanUtils.copyProperties(prodPartsinfoVO, parts);
                prodPartsinfoMapper.insert(parts);
                o.setPtId(parts.getId());
                o.setPtNo(parts.getPtNo());
                // 查询部件工序
                // 查询部件的工序
                List<ProdProcelinkVO> prodProcelinkVOS = xyOrderOrdinfoMapper.getProdProcelink(o.getPartErp());
                if (!Func.isEmpty(prodProcelinkVOS)) {
                    int a = 0;
                    int b = 0;
                    for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOS) {
                        if (b == 0 && prodProcelinkVO.getLb() == 2) {//为2就是印后工序
//                                        插入印刷工序
                            ProdProcelink prodProcelink = new ProdProcelink();
                            prodProcelink.setDiffLevel(0.0);
                            prodProcelink.setWasteRate(0.0);
                            prodProcelink.setIsUsed(1);
                            prodProcelink.setPtId(parts.getId());
                            prodProcelink.setSortNum(a++);
                            prodProcelink.setPrId(proessWorkinfoMapper.getByerpId("999999"));
                            prodProcelinkMapper.insert(prodProcelink);
                            b = 1;//判断已插入状态
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


                // 部件物料及辅料
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
//                        判断设备类型
        MachineClassify machineClassify = machineClassifyMapper.selectById(machineMainfo.getMtId());
        workbatchOrdlink.setUsId(saUserMapper.getUserIdByerpId(o.getUsErp()));
        if (StringUtils.isNotBlank(workbatchOrdlink.getIngredientName())) {
            workbatchOrdlink.setIngredientName(workbatchOrdlink.getIngredientName().replaceAll("[\r\n]", ""));
        }
//        查询erpId是否已经存在
        List<Integer> ids = workbatchOrdlinkMapper.selectByEroId(workbatchOrdlink.getErpId());
        if (null == ids || ids.size() == 0) {
            workbatchOrdlinkMapper.insert(workbatchOrdlink);
        }
//        if (!Func.isEmpty(machineClassify) && machineClassify.getModel().equals("印刷设备")) {
//                            保存扩展表
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
//        }else if (!Func.isEmpty(machineClassify) && machineClassify.getModel().equals("模切设备")) {
//                            保存对应扩展表
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

//==========================================================最新排产end

    /**
     * 根据排产id更新排产信息(无用)
     *
     * @param Id
     * @return
     * @deprecated 取消掉用
     */
    @Override
    @GetMapping("/updateWorkbatchById")
    public R updateWorkbatchById(Integer Id) {
//        查询当前id的本地数据库信息
        WorkbatchOrdlink ordlinkLocal = workbatchOrdlinkMapper.selectById(Id);
//        查询排产在fz中的信息
        WorkbatchOrdlink ordlinkFZ = xyWorkBatchMapper.selectById(Id);
        ordlinkLocal.setPlanNum(ordlinkFZ.getPlanNum());
        ordlinkLocal.setPlanNumber(ordlinkFZ.getPlanNumber());
        ordlinkLocal.setCompleteNum(ordlinkFZ.getCompleteNum());
        ordlinkLocal.setIncompleteNum(ordlinkFZ.getIncompleteNum());
        ordlinkLocal.setExtraNum(ordlinkFZ.getExtraNum());
        ordlinkLocal.setUpdateAt(new Date());
        //更新
//        workbatchOrdlinkMapper.updateById(ordlinkLocal);
        return R.success("排产更新至最新-已经取消使用");
    }

    /**
     * 更新排产单状态
     *
     * @return
     */
    @GetMapping("/updateWorkbatchStatus")
    public R updateWorkbatchStatus() {
        //        创建临时表
        xyWorkBatchMapper.creatTMP();
        List<tmp> tmpList = workbatchOrdlinkMapper.selectByStatus("-1");
//        分批次插入
        insertList(tmpList);
//        xyWorkBatchMapper.insterTMP(tmpList);
//        关联查询出被修改未已完成状态的排产单
        List<String> uuids = xyWorkBatchMapper.selectStatusListw();
        List<String> uuids2 = xyWorkBatchMapper.selectStatusListy();
        if (Func.isEmpty(uuids)) {
            uuids = new ArrayList<>();
        }
        uuids.addAll(uuids2);
//        批量修改未已完成
        if (!Func.isEmpty(uuids)) {
            workbatchOrdlinkMapper.setStatus(uuids);
        }
//        删除临时表
        xyWorkBatchMapper.dropTMP();
        return R.success("排产状态同步更新完成");
    }

    /**
     * 分批次插入
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
            //        部门更新
            List<BaseDeptInfoVo> list = xyDeptMapper.list();
            if (!list.isEmpty()) {
                List<BaseDeptInfoVo> deptList = DeptUtil.getTree(list);
                saveDeptTree(deptList, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
            massage(stackTraceElement);
            return R.fail("更新失败");
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
            //系统已存在的用户id
            List<String> erpIds = userList.stream().map(SaUser::getErpId).collect(Collectors.toList());
            saUsers = saUsers.stream().filter(o -> {
                return !erpIds.contains(o.getErpId());
            }).collect(Collectors.toList());
            //存储用户
            if (!saUsers.isEmpty()) {
                for (SaUser saUser : saUsers) {
                    try {
//                        默认初始化密码123
                        saUser.setPassword("adcd7048512e64b48da55b027577886ee5a36350");
                        saUser.setTenantId("xingyi");
                        saUser.setRoleId("13");
//                    查询所属部门
                        BaseDeptInfo deptInfo = baseDeptInfoMapper.getByerpId(saUser.getDpErp());
                        if (!Func.isEmpty(deptInfo)) {
                            saUser.setDeptId(deptInfo.getId() + "");
                        }
                        saUserMapper.insert(saUser);
                        //存储员工数据

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            //不存在直接保存
            saUsers.forEach(o -> {
                o.setPassword("adcd7048512e64b48da55b027577886ee5a36350");
                o.setTenantId("xingyi");
                o.setRoleId("13");
//                    查询所属部门
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
            //系统已离职的用户id
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
            //系统已存在的用户id
            List<String> erpIds = baseStaffinfos.stream().map(BaseStaffinfo::getErpId).collect(Collectors.toList());
            fzBaseStaffinfos = fzBaseStaffinfos.stream().filter(o -> {
                return !erpIds.contains(o.getErpId());
            }).collect(Collectors.toList());
            if (!fzBaseStaffinfos.isEmpty()) {
                fzBaseStaffinfos.forEach(o -> {
                    o.setUserId(saUserMapper.getUserIdByerpId(o.getErpId()));
//                    查询所属部门
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
//                    查询所属部门
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
     * 递归保存部件与部件相关的工序物料
     *
     * @param list
     */
    private void saveProdPartsinfo(List<ProdPartsinfoVO> list) {
        for (ProdPartsinfoVO partsinfoVO : list) {
//            保存对应物料
            if (!Func.isEmpty(partsinfoVO.getMaterProdlinks())) {
                for (MaterProdlink materProdlink : partsinfoVO.getMaterProdlinks()) {
                    materProdlinkMapper.insert(materProdlink);
                }
            }
//            保存对应给工序
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
//              只有子类部件在流程中
                getPdinfoJSON(prodPartsinfoVO.getChildren(), jsonsRootBean);
            } else {
//                生产部件的json对象
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
                node.setType(2);//确定类型为部件
                node.setIco("el-icon-plus");
                node.setName(prodPartsinfoVO.getPtName());
                node.setPtClassify(prodPartsinfoVO.getPtClassify());//todo 金世纪暂无其它情况
                node.setReUuid(node.getUuid());
                if (jsonsRootBean.getNodeList() == null || Func.isEmpty(jsonsRootBean.getNodeList())) {
                    jsonsRootBean.setNodeList(new ArrayList<>());
                }
                jsonsRootBean.getNodeList().add(node);
                if (!Func.isEmpty(prodPartsinfoVO.getMaterProdlinks())) {
                    for (MaterProdlink materProdlink : prodPartsinfoVO.getMaterProdlinks()) {
                        //                    转换对应部件的物料的json串对象与连线的json对象
                        Nodelist nodeMater = new Nodelist();
                        nodeMater.setUuid(UUID.randomUUID().toString());
                        nodeMater.setShow(true);
                        nodeMater.setId(materProdlink.getMlId());
//                        查询物料
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
                        nodeMater.setType(1);//类型为物料
                        nodeMater.setMcName(materMtinfo.getMcId() != null ? materClassfiyMapper.selectById(materMtinfo.getMcId()).getMcName() : null);
                        Leftstyle leftstyle = new Leftstyle();
                        leftstyle.setBackgroundColor("#409EFF");
                        nodeMater.setLeftStyle(leftstyle);
                        nodeMater.setPtUuid(node.getUuid());//等于部件的uuid
                        nodeMater.setMlNum(materProdlink.getMtNum() + "");
                        jsonsRootBean.getNodeList().add(nodeMater);
//                        添加连线
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
                            ProcessWorkinfo processWorkinfo = proessWorkinfoMapper.selectById(procelink.getPrId());//查询工序信息
                            nodeProce.setId(processWorkinfo.getId());
                            nodeProce.setPrName(processWorkinfo.getPrName());
                            nodeProce.setPrNo(processWorkinfo.getPrNo());
                            nodeProce.setSort(procelink.getSortNum());
                            nodeProce.setStatus(processWorkinfo.getStatus());
                            nodeProce.setIslocal(processWorkinfo.getIslocal());
                            nodeProce.setIsdel(processWorkinfo.getIsdel());
                            nodeProce.setCreateAt(processWorkinfo.getCreateAt());
                            nodeProce.setUpdateAt(processWorkinfo.getUpdateAt());
//                            查询工序分类名称
                            Map map = new HashMap<>();
                            map.put("pr_id", processWorkinfo.getId());
                            List<ProcessClasslink> link = proessClasslinkMapper.selectByMap(map);
                            ProcessClassify classify = processClassifyMapper.selectById(link.get(0).getPyId());
                            nodeProce.setPyName(classify.getPyName());
                            nodeProce.setDiffLevel(procelink.getDiffLevel());
                            nodeProce.setWasteRate(procelink.getWasteRate());
                            nodeProce.setIco("el-icon-plus");
                            nodeProce.setName(processWorkinfo.getPrName());
                            nodeProce.setType(3);//类型为工序
                            Leftstyle leftStyle = new Leftstyle();
                            leftStyle.setBackgroundColor("#67C23A");
                            nodeProce.setLeftStyle(leftStyle);
                            nodeProce.setPrParam(procelink.getPrParam());
                            nodeProce.setRemarks(procelink.getRemarks());
                            nodeProce.setDifficulty("0");
                            nodeProce.setLossRate("0");
                            nodeProce.setPtUuid(node.getUuid());
                            jsonsRootBean.getNodeList().add(nodeProce);
//                            添加连线
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
     * 定时更新
     * 基本上统计需要37分钟时间进行同步操作；今天晚上调整为23点更新，再做跟踪数据更新时间
     */
//    @Scheduled(cron="0 0 12 * * ?")
    @Scheduled(cron = "0 0 12,23 * * ?")
    public void taskEndFissionWanted() {
        dingClient.send("兴艺定时同步任务：---开始---", "InternalH5");
        log.info("定时任务生成中午12:00晚上23:00点执行--star-----------------开始------------------");
//        部门更新
        updateDept();
//        人员相关
        updateBladeUser();
//        客户相关
        updateCrmCustomer();
//        工序/物料
        updateProcessWorkinfo();
//        产品分类
        updatePdClassify();
//          排产相关
        updateWorkbatch(-1);
        // 更新排产状态
        updateWorkbatchStatus();
        dingClient.send("兴艺定时同步任务：---结束---", "InternalH5");
        log.info("定时任务生成中午12:00晚上23:00点执行--end-----------------结束------------------");
    }

    /**
     * 异常短信
     *
     * @param stackTraceElement
     */
    private static void massage(StackTraceElement stackTraceElement) {
        String error = stackTraceElement.getFileName() + "-" + stackTraceElement.getMethodName() + "-" + stackTraceElement.getLineNumber();
        MsgConfig.sendStringMessages("15086235562", "兴艺数字印刷pro", error);
    }

    /**
     * 异常短信
     *
     * @param message
     */
    private static void message(String message) {
        MsgConfig.sendStringMessages("15086235562", "pro-兴艺数字印刷同步插入异常：", message);
    }

    /**
     * 新增主计划数据
     */
    public void addWorkbatchProgress(WorkbatchOrdlink workbatchOrdlink) {
        Date date = new Date();
        Integer status = 0;
        Integer totalTime = 0;//总计划时间
        Integer stayTime = null;//工序剩余时间
        Integer planCount = null;//计划总量
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
        Integer planNum = workbatchOrdlink.getPlanNum();//计划总量
        Integer completeNum = workbatchOrdlink.getCompleteNum();//已完成数
        completeNum = completeNum == null ? 0 : completeNum;
        planNum = planNum == null ? 0 : planNum;
        Date closeTime = workbatchOrdlink.getCloseTime();//截止时间
        if (!StringUtil.isEmpty(wbNo)) {
            /*查询该批次下的主计划*/
            List<WorkbatchProgress> workbatchProgressList =
                    workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("wb_no", wbNo).eq("wp_type", 1));
            if (workbatchProgressList.isEmpty()) {//判断是否存在,不存在则创建
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//客户名称
                workbatchProgress.setCreateAt(date);//创建时间
                workbatchProgress.setFinishTime(finishTime);//实际交货时间
                workbatchProgress.setLimitTime(closeTime);//限制时间(截止时间)
                workbatchProgress.setPdId(null);//产品id
                workbatchProgress.setPdName(pdName);//产品名称
                workbatchProgress.setPlanCount(planNum);//计划总量
                workbatchProgress.setRealCount(completeNum);//已完成数量
                workbatchProgress.setSdId(sdId);//排产id
                workbatchProgress.setStatus(status);//状态：0已导入1已排产2已生产3部分完成4全部完成5挂起6驳回
                workbatchProgress.setMaId(maId);//设备id
                workbatchProgress.setTotalTime(totalTime);//总计划时间(分钟)
                workbatchProgress.setUpdateAt(date);//修改时间
                workbatchProgress.setWarning(1);//预警状态1级36小时2级24小时
                workbatchProgress.setWbId(wbId);//批次id
                workbatchProgress.setWbNo(wbNo);//批次编号
                workbatchProgress.setWpType(1);//进度类型1、批次工单类型2、部件类型3、工序类型
                workbatchProgressMapper.insert(workbatchProgress);
            } else {//如果存在则修改计划总数量
                WorkbatchProgress progress = workbatchProgressList.get(0);
                planCount = progress.getPlanCount();
                Integer realCount = progress.getRealCount();//已完成数量
                realCount = realCount == null ? 0 : realCount;
                if (planCount == null) {
                    planCount = 0;
                }
                planCount += planNum;
                realCount += completeNum;
                progress.setPlanCount(planCount);//计划总数
                progress.setRealCount(realCount);//已完成数
                workbatchProgressMapper.updateById(progress);//修改工单编号的计划总数量
            }
            /*存在则查询部件主计划是否存在*/
            List<WorkbatchProgress> progressList = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                    .eq("pt_name", ptName).eq("wb_no", wbNo).eq("wp_type", 2));
            if (progressList.isEmpty()) {//如果部件为空,则创建
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//客户名称
                workbatchProgress.setCreateAt(date);//创建时间
                workbatchProgress.setFinishTime(finishTime);//实际交货时间
                workbatchProgress.setLimitTime(closeTime);//限制时间(截止时间)
                workbatchProgress.setPdId(null);//产品id
                workbatchProgress.setPdName(pdName);//产品名称
                workbatchProgress.setPtId(ptId);//部件id
                workbatchProgress.setPtName(ptName);//部件名称
                workbatchProgress.setPlanCount(planNum);//计划总量
                workbatchProgress.setRealCount(completeNum);//已完成数量
                workbatchProgress.setMaId(maId);//设备id
                workbatchProgress.setSdId(sdId);//排产id
                workbatchProgress.setWarning(1);//预警状态1级36小时2级24小时
                workbatchProgress.setStatus(status);//状态：0已导入1已排产2已生产3部分完成4全部完成5挂起6驳回
                workbatchProgress.setUpdateAt(date);//修改时间
                workbatchProgress.setWbId(wbId);//批次id
                workbatchProgress.setWbNo(wbNo);//批次编号
                workbatchProgress.setWpType(2);//进度类型1、批次工单类型2、部件类型3、工序类型
                workbatchProgressMapper.insert(workbatchProgress);
            } else {//如果存在则修改计划总数量
                WorkbatchProgress workbatchProgress = progressList.get(0);
                Integer rCount = workbatchProgress.getRealCount();
                Integer pCount = workbatchProgress.getPlanCount();
                rCount = rCount == null ? 0 : rCount;
                pCount = pCount == null ? 0 : pCount;
                pCount += planNum;
                rCount += completeNum;
                workbatchProgress.setRealCount(rCount);//部件完成数
                workbatchProgress.setPlanCount(pCount);//部件计划数
                workbatchProgressMapper.updateById(workbatchProgress);
            }
            List<WorkbatchProgress> progressList1 = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                    .eq("pr_id", prId).eq("pt_name", ptName).eq("ma_id", maId).eq("wb_no", wbNo).eq("wp_type", 2));
            if (progressList1.isEmpty()) {
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//客户名称
                workbatchProgress.setCreateAt(date);//创建时间
                workbatchProgress.setFinishTime(finishTime);//实际交货时间
                workbatchProgress.setLimitTime(closeTime);//限制时间(截止时间)
                workbatchProgress.setPdId(null);//产品id
                workbatchProgress.setPdName(pdName);//产品名称
                workbatchProgress.setPlanCount(planNum);//计划总量
                workbatchProgress.setPrId(prId);//工序id
                workbatchProgress.setPrName(prName);//工序名称
                workbatchProgress.setMaId(maId);//设备id
                workbatchProgress.setPtId(ptId);//部件id
                workbatchProgress.setPtName(ptName);//部件名称
                workbatchProgress.setRealCount(completeNum);//已完成数量
                workbatchProgress.setSdId(sdId);//排产id
                workbatchProgress.setStatus(status);//状态：0已导入1已排产2已生产3部分完成4全部完成5挂起6驳回
                workbatchProgress.setStayTime(stayTime);//工序剩余时间(分钟)
                workbatchProgress.setTotalTime(totalTime);//总计划时间(分钟)
                workbatchProgress.setUpdateAt(date);//修改时间
                workbatchProgress.setWarning(1);//预警状态1级36小时2级24小时
                workbatchProgress.setWbId(wbId);//批次id
                workbatchProgress.setWbNo(wbNo);//批次编号
                workbatchProgress.setWpType(3);//进度类型1、批次工单类型2、部件类型3、工序类型
                workbatchProgressMapper.insert(workbatchProgress);
            }
        }
    }


}
