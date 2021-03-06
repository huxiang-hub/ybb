package com.yb.workbatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.barcodeUtils.barCodeUtil;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.base.mapper.BaseDeptinfoMapper;
import com.yb.common.DateUtil;
import com.yb.common.constant.OrderStatusConstant;
import com.yb.common.entity.WorkbatchOrdlinkYS;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.mapper.*;
import com.yb.execute.vo.ExecuteBrieferOrdlinkVO;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.mater.entity.MaterBatchlink;
import com.yb.mater.entity.MaterProdlink;
import com.yb.mater.mapper.MaterBatchlinkMapper;
import com.yb.mater.mapper.MaterMtinfoMapper;
import com.yb.mater.mapper.MaterProdlinkMapper;
import com.yb.mater.vo.MaterMtinfoVO;
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.mapper.OrderOrdinfoMapper;
import com.yb.order.mapper.OrderWorkbatchMapper;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.panelapi.common.TempEntity;
import com.yb.panelapi.common.UpdateStateUtils;
import com.yb.panelapi.request.OrderAcceptRequest;
import com.yb.panelapi.user.utils.R;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.mapper.ProcessClassifyMapper;
import com.yb.process.mapper.ProcessMachlinkMapper;
import com.yb.process.mapper.ProcessWorkinfoMapper;
import com.yb.prod.entity.ProdClassify;
import com.yb.prod.entity.ProdPartsinfo;
import com.yb.prod.mapper.ProdClassifyMapper;
import com.yb.prod.mapper.ProdPartsinfoMapper;
import com.yb.prod.mapper.ProdPdinfoMapper;
import com.yb.prod.mapper.ProdProcelinkMapper;
import com.yb.prod.vo.ProdPdinfoVO;
import com.yb.prod.vo.ProdProcelinkVO;
import com.yb.rule.entity.RuleProdoee;
import com.yb.rule.mapper.RuleProdoeeMapper;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.excelUtils.DataByPage;
import com.yb.statis.excelUtils.JxlsUtils;
import com.yb.statis.excelUtils.Page;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.statis.service.IStatisMachreachService;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.entity.StoreOutlog;
import com.yb.stroe.mapper.StoreInlogMapper;
import com.yb.stroe.mapper.StoreInventoryMapper;
import com.yb.stroe.mapper.StoreOutlogMapper;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.vo.InInventoryByNumberVO;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.mapper.SuperviseBoxinfoMapper;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.supervise.mapper.SuperviseExerunMapper;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.*;
import com.yb.workbatch.excelUtils.BarcodeUtil;
import com.yb.workbatch.excelUtils.ExportlUtil;
import com.yb.workbatch.mapper.*;
import com.yb.workbatch.request.WaitOrderRequest;
import com.yb.workbatch.service.IWorkbatchOrdlinkNewService;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.vo.*;
import com.yb.workbatch.wrapper.WorkbatchOrdlinkWrapper;
import com.yb.workbatch.wrapper.WorkbatchOrdoeeWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.common.exception.CommonException;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * ???????????????yb_workbatch_ordlink ???????????????
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
@Slf4j
public class WorkbatchOrdlinkServiceImpl extends ServiceImpl<WorkbatchOrdlinkMapper, WorkbatchOrdlink> implements IWorkbatchOrdlinkService {


    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ExecuteStateMapper stateMapper;
    @Autowired
    private ProdPartsinfoMapper prodPartsinfoMapper;
    @Autowired
    private WorkbatchOrdlinkMapper ordlinkMapper;
    @Autowired
    private ExecuteBrieferMapper brieferMapper;
    @Autowired
    private SuperviseExecuteMapper executeMapper;
    @Autowired
    private ExecuteWasteMapper wasteMapper;
    @Autowired
    private ExecuteInfoMapper infoMapper;
    @Autowired
    private ProdPdinfoMapper prodPdinfoMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchOrdoeeMapper workbatchOrdoeeMapper;
    @Autowired
    private ProcessWorkinfoMapper processWorkinfoMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private WorkbatchordoeeMaintainMapper workbatchordoeeMaintainMapper;
    @Autowired
    private WorkbatchordoeeMealMapper workbatchordoeeMealMapper;
    @Autowired
    private WorkbatchordoeeMouldMapper workbatchordoeeMouldMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private ProcessMachlinkMapper processMachlinkMapper;
    @Autowired
    private OrderOrdinfoMapper orderOrdinfoMapper;
    @Autowired
    private IOrderWorkbatchService orderWorkbatchService;
    @Autowired
    private IStatisMachreachService statisMachreachService;
    @Autowired
    private BaseDeptinfoMapper baseDeptinfoMapper;
    @Autowired
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;
    @Autowired
    private SuperviseExerunMapper superviseExerunMapper;
    @Autowired
    private RuleProdoeeMapper ruleProdoeeMapper;
    @Autowired
    private ProdClassifyMapper prodClassifyMapper;
    @Autowired
    private StatisOrdreachMapper statisOrdreachMapper;
    @Autowired
    private ProdProcelinkMapper prodProcelinkMapper;
    @Autowired
    private MaterProdlinkMapper materProdlinkMapper;
    @Autowired
    private OrderWorkbatchMapper orderWorkbatchMapper;
    @Autowired
    private MaterBatchlinkMapper materBatchlinkMapper;
    @Autowired
    private MaterMtinfoMapper materMtinfoMapper;
    @Autowired
    private WorkbatchProgressMapper workbatchProgressMapper;
    @Autowired
    private ExecuteTraycardMapper executeTraycardMapper;
    @Autowired
    private StoreInventoryMapper storeInventoryMapper;
    @Autowired
    private StoreInlogMapper storeInlogMapper;
    @Autowired
    private StoreOutlogMapper storeOutlogMapper;
    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;
    @Autowired
    private ProcessClassifyMapper processClassifyMapper;
    @Autowired
    private SuperviseExestopMapper superviseExestopMapper;
    @Autowired
    private IWorkbatchOrdlinkNewService iWorkbatchOrdlinkNewService;
    @Autowired
    private ExecuteBrieferMapper executeBrieferMapper;
    @Autowired
    private IStoreInventoryService storeInventoryService;
    @Autowired
    private RedissonClient redisson;

    @Autowired
    private ProcessMachlinkMapper proceMalinkMapper;//???????????????????????????????????????

    private final String lockKey = "FINISH_ORDER_LOCK";

    @Override
    public IPage<WorkbatchOrdlinkVO> selectWorkbatchOrdlinkPage(IPage<WorkbatchOrdlinkVO> page, WorkbatchOrdlinkVO workbatchOrdlink) {
        return page.setRecords(baseMapper.selectWorkbatchOrdlinkPage(page, workbatchOrdlink));
    }

    @Override
    public IPage<PlanProduceModelVO> planProducePage(IPage<PlanProduceModelVO> page, WorkbatchOrdlinkVO workbatchOrdlink) {
        return page.setRecords(baseMapper.planProducePage(page, workbatchOrdlink));
    }

    @Override
    public IPage<PlanProduceModelVO> planProduceToActSetPage(IPage<PlanProduceModelVO> page, WorkbatchOrdlinkVO workbatchOrdlink) {
        return page.setRecords(baseMapper.planProduceToActSetPage(page, workbatchOrdlink));
    }

    /**
     * ???????????????????????????????????????ID
     * ???????????????????????????
     */
    @Override
    public List<WorkbatchOrdlinkVO> getOrderList(WorkbatchOrdlink workbatchOrdlink) {
        //?????????????????????????????????????????????
        if (workbatchOrdlink.getDpId() == null) {
            MachineMainfo machineMainfo = machineMainfoMapper.selectById(workbatchOrdlink.getMaId());
            if (machineMainfo == null) {
                throw new CommonException(HttpStatus.NOT_FOUND.value(), "????????????????????????????????????????????????");
            }
            workbatchOrdlink.setDpId(machineMainfo.getDpId());//??????????????????id????????????
        }
        //
        List<WorkbatchOrdlinkVO> workbatchOrdlink1 = ordlinkMapper.findWorkbatchOrdlink(workbatchOrdlink, null, null);
        for (WorkbatchOrdlinkVO workbatchOrdlinkVO : workbatchOrdlink1) {
            workbatchOrdlinkVO.setMaId(workbatchOrdlink.getMaId());
        }
//        List<WorkbatchOrdlink> ordlink = ordlinkMapper.findNotHaveMaidWorkbatchOrdlink(workbatchOrdlink, null, null);
//
//        if (workbatchOrdlink1.isEmpty()) {
//            workbatchOrdlink1 = new ArrayList<>();
//        }
//        if (!ordlink.isEmpty()) {
//            workbatchOrdlink1.addAll(ordlink);
//        }
        return workbatchOrdlink1;
    }


    @Override
    public List<WorkbatchShiftListVO> getOrderListNew(Integer maId, String startDate, String endDate) {
        //?????????????????????????????????????????????
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
        if (machineMainfo == null) {
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "????????????????????????????????????????????????");
        }
        //????????????????????????
        WaitOrderRequest wkquery = new WaitOrderRequest();
        wkquery.setMaId(maId);

        //?????????????????????????????????????????????
        List<WorkbatchShiftListVO> wbatchList = ordlinkMapper.findWorkbatchOrdlinkNew(wkquery, startDate, endDate);

        return wbatchList;
    }

    @Override
    public List<WorkbatchOrdlinkVO> getOrderListByMatype(WorkbatchOrdlink workbatchOrdlink) {
        WorkbatchOrdlink wklink = workbatchOrdlink;
        wklink.setMaId(null);//????????????????????????
        List<WorkbatchOrdlinkVO> workbatchOrdlink1 = ordlinkMapper.findWorkbatchOrdlinkByMatype(wklink, null, null);
        for (WorkbatchOrdlinkVO workbatchOrdlinkVO : workbatchOrdlink1) {
            workbatchOrdlinkVO.setMaId(workbatchOrdlink.getMaId());
        }
        return workbatchOrdlink1;
    }


    /****
     * ?????????????????????pyId?????????????????????
     * @param maId
     * @return
     */
    @Override
    public List<WorkbatchShiftListVO> getOrderListByPrids(Integer maId, String startDate, String endDate) {

        WorkbatchOrdlink wklink = new WorkbatchOrdlink();
        wklink.setMaId(null);//????????????????????????
        String prIds = proceMalinkMapper.getPridsBymaId(maId);//??????????????????????????????
        List<WorkbatchShiftListVO> wbatchlink = ordlinkMapper.getOrderListByPrids(prIds, startDate, endDate);
        //??????????????????????????????????????????????????????????????????????????????id???????????????
        for (WorkbatchShiftListVO wbatchlinkvo : wbatchlink) {
            wbatchlinkvo.setMaId(maId);
        }
        return wbatchlink;
    }

    @Override
    public WorkbatchOrdlinkVO getRunOrder(Integer maId) {
        WorkbatchOrdlink runOrder = WorkbatchOrdlink.getInstanceProduction();
        runOrder.setMaId(maId);
        return ordlinkMapper.getRunOrder(runOrder);
    }

    /*****
     * ?????????????????????
     * @param orderAccept
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R acceptOrderBywfid(OrderAcceptRequest orderAccept) {
        Integer wfId = orderAccept.getWfId();
        Integer maId = orderAccept.getMaId();
        Integer usId = orderAccept.getUsId();

        //ordlinkVO ????????????id????????????????????????????????????????????????wyn????????????
        if (wfId != null && maId != null) {
            MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
            Integer isRecepro = (machineMainfo != null) ? machineMainfo.getIsRecepro() : 0;//?????????
            //????????????????????????????????????;??????????????????
            if (isRecepro != 1) {
                Map<String, Object> exeMainfo = superviseExecuteMapper.findMainfoBywfId(wfId);
                if (exeMainfo != null && exeMainfo.size() > 0) {
                    Integer cnum = ((Long) exeMainfo.get("cnum")).intValue();
                    String maName = (String) exeMainfo.get("maName");
                    if (cnum > 0) {
                        log.info("??????????????????[cnum:{}]????????????[maName:{}]", cnum, maName);
                        throw new CommonException(HttpStatus.NOT_FOUND.value(), "??????" + cnum + "?????????????????????????????????" + maName + "???????????????????????????");
                    }
                }
            }
        }

        //????????????????????????????????????????????????????????????????????????????????????
        SuperviseExecute superviseExecute = superviseExecuteMapper.getExecuteOrder(maId);
        //SuperviseBoxinfo boxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //????????????????????????????????????
        // List<WorkbatchShift> workbatchShifts = workbatchShiftMapper.getProOrder(ordlinkVO.getMaId());
        if ("B".equals(superviseExecute.getExeStatus()) || "C".equals(superviseExecute.getExeStatus())) {
            log.info("???????????????????????????????????????:[maId:{}]", maId);
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "???????????????????????????????????????????????????????????????");
        }
        //??????????????????????????????????????????
        WorkbatchShiftVO wbatchshift = workbatchShiftMapper.getBywfId(wfId);
        //???????????????
        Date currDate = new Date();
        //??????????????????????????????????????????????????????????????????????????? ???????????????????????????
        WorkbatchShiftset shiftset = workbatchShiftsetMapper.getShiftsetBymaId(maId);
        if (shiftset == null || shiftset.getId() == null) {
            shiftset = new WorkbatchShiftset();
            shiftset.setWsId(44);//??????????????????????????????id?????????????????????????????????
            shiftset.setStartTime(currDate);
        }
        String targetDay = (shiftset.getStartTime() != null) ? DateUtil.refNowDay(shiftset.getStartTime()) : DateUtil.refNowDay(currDate);
        //??????????????????????????????
        ExecuteInfo executeInfo = saveExcuteInfoNew(maId, wbatchshift, currDate, wbatchshift.getWbNo(), shiftset.getWsId(), targetDay, usId);

        //????????????????????????
        SuperviseBoxinfo boxInfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //????????????????????????????????????????????????????????????????????????
        if (wbatchshift != null) {
            superviseExecute.setOperator(usId);
            superviseExecute.setWfId(wfId);
            superviseExecute.setWbNo(wbatchshift.getWbNo());
            superviseExecute.setSdId(wbatchshift.getSdId());
            superviseExecute.setOperator(usId);
            superviseExecute.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());//?????????B1???????????????
            superviseExecute.setExeStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());//?????????B???????????????
            superviseExecute.setExId(executeInfo.getId());
            superviseExecute.setUpdateAt(currDate);
            superviseExecute.setCurrNum(0);//????????????????????????
            superviseExecute.setStartTime(currDate);
            int startNum = boxInfo.getNumberOfDay() == null ? 0 : boxInfo.getNumberOfDay();
            superviseExecute.setStartNum(startNum);
            superviseExecute.setEndNum(startNum);
            superviseExecute.setEndTime(currDate);
            superviseExecute.setWbId(wbatchshift.getWbId());
            executeMapper.update(superviseExecute);
        }

        System.out.print("===================================3");

        WorkbatchOrdlink ordlink = new WorkbatchOrdlink();
        ordlink.setId(wbatchshift.getSdId());
        ordlink.setRunStatus(1);//??????????????????
        ordlink.setStatus("1");//0:????????????1???????????????2???????????????
        updateAcceptByUsId(ordlink);//???????????????????????????
        System.out.print("===================================4-1");
        //?????????????????????????????????????????????
        if (wbatchshift != null) {
            wbatchshift.setShiftStatus(1);//0:????????????1???????????????2???????????????  3?????????????????????????????? 4??????????????????????????? ??????????????????????????????
            wbatchshift.setStatus("1");
            workbatchShiftMapper.updateById(wbatchshift);
        }
        System.out.print("===================================6");
        // SendMsgUtils.sendToUser(usIds, JSON.toJSONString(temp), ChatType.MAC_ACCEPTORDER.getType());//??????????????????????????????????????????
        System.out.print("===================================7");
        //????????????
        boxInfo.setBlnAccept(1);
        boxInfo.setWbNo(ordlink.getWbNo());
        superviseBoxinfoMapper.updateById(boxInfo);
        return R.ok(executeInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R acceptOrderNew(WorkbatchOrdlinkVO ordlinkVO) {
        Integer maId = ordlinkVO.getMaId();  //??????id
        Integer sdId = ordlinkVO.getId();  //??????id
        Integer wsId = ordlinkVO.getWsId();  //????????????id
        Integer wfId = ordlinkVO.getWfId();  //?????????id

        //ordlinkVO ????????????id????????????????????????????????????????????????wyn????????????
        if (wfId != null && maId != null) {
            MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
            Integer isRecepro = machineMainfo.getIsRecepro();
            //????????????????????????????????????;??????????????????
            if (isRecepro != 1) {
                Map<String, Object> exeMainfo = superviseExecuteMapper.findMainfoBywfId(wfId);
                if (exeMainfo != null && exeMainfo.size() > 0) {
                    Integer cnum = ((Long) exeMainfo.get("cnum")).intValue();
                    String maName = (String) exeMainfo.get("maName");
                    if (cnum > 0) {
                        log.info("??????????????????[cnum:{}]????????????[maName:{}]", cnum, maName);
                        throw new CommonException(HttpStatus.NOT_FOUND.value(), "??????" + cnum + "?????????????????????????????????" + maName + "???????????????????????????");
                    }
                }
            }
        }


        SuperviseExecute superviseExecute = superviseExecuteMapper.selectOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
        //TODO:???????????????????????????,????????????;???????????????????????????
        WorkbatchProgress workbatchProgress =
                workbatchProgressMapper.selectOne(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("wp_type", 3));
        if (workbatchProgress != null) {
            workbatchProgress.setStatus(2);//??????????????????
            workbatchProgressMapper.updateById(workbatchProgress);
            System.out.print("===================================4-2");
        }
        //????????????????????????????????????
        // List<WorkbatchShift> workbatchShifts = workbatchShiftMapper.getProOrder(ordlinkVO.getMaId());


        if ("B".equals(superviseExecute.getExeStatus()) || "C".equals(superviseExecute.getExeStatus())) {
            log.info("???????????????????????????????????????:[maId:{}]", maId);
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "???????????????????????????????????????????????????????????????");
        }

        ExecuteState newState = new ExecuteState();
        Date currDate = new Date();
        String sdDate = ordlinkVO.getSdDate();
        //????????????id???????????????????????????
        SuperviseExecute executeOrder = executeMapper.getExecuteOrder(maId);
        String usIds = (executeOrder != null) ? executeOrder.getUsIds() : "all";
        newState.setTeamId(usIds);   //????????????????????????id
        newState.setMaId(maId);   //??????id
        newState.setSdId(sdId);   //??????id
        //???????????????????????????
        //WorkbatchShift wshift = getWorkbatchShift(currDate, maId, wsId, sdDate, sdId, ordlinkVO.getPlanNum(), ordlinkVO.getWfId());

        newState.setWfId(ordlinkVO.getWfId());//????????????????????????wfId???????????????
        newState.setWbId(ordlinkVO.getWbId());   //??????id
        newState.setUsId(ordlinkVO.getUsId());
        newState.setStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());    //????????????
        newState.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());   //??????
        newState.setStartAt(currDate);
        newState.setCreateAt(currDate);

        System.out.print("===================================2");
        WorkbatchOrdlink workbatchOrdlink = ordlinkMapper.selectOne(new QueryWrapper<WorkbatchOrdlink>().eq("id", sdId));

        //??????????????????????????????
        ExecuteInfo executeInfo = saveExcuteInfo(ordlinkVO, maId, sdId, newState, currDate, workbatchOrdlink);

        System.out.print("===================================1");
        //??????????????????????????????????????????
        UpdateStateUtils.updateSupervise(newState, executeInfo);


        System.out.print("===================================3");
        Integer infoId = executeInfo.getId();
//        TempEntity temp = new TempEntity();
//        temp.setStartTime(currDate);
//        temp.setOrderName(ordlinkMapper.getOrderName(sdId));
        //?????????????????????
        workbatchOrdlink.setRunStatus(1);//??????????????????
        Integer usId = ordlinkVO.getUsId();
        System.out.print("===================================4");
        if (usId != null) {//?????????id
            workbatchOrdlink.setUsId(usId);
        }
        updateAcceptByUsId(workbatchOrdlink);//???????????????????????????
        System.out.print("===================================4-1");
        //?????????????????????????????????????????????
        WorkbatchShift wshift = workbatchShiftMapper.selectById(ordlinkVO.getWfId());
        if (wshift != null) {
            wshift.setShiftStatus(1);//0:????????????1???????????????2???????????????  3?????????????????????????????? 4??????????????????????????? ??????????????????????????????
            wshift.setStatus("1");
            workbatchShiftMapper.updateById(wshift);
        }

        System.out.print("===================================5");

        System.out.print("===================================58");
        //?????????????????????????????????ee
        ProcessMachlink processMachlink = processMachlinkMapper.getEntityByPrMt(maId, workbatchOrdlink.getPrId());
        if (processMachlink == null) {
            log.error("?????????????????????:maId:{}, prId:{}", maId, workbatchOrdlink.getPrId());
            List<ProcessMachlink> list = processMachlinkMapper.selectList(new QueryWrapper<ProcessMachlink>().eq("ma_id", maId));
            if (!list.isEmpty()) {
                ProcessMachlink processMachlink1 = list.get(0);
                processMachlink1.setPrId(workbatchOrdlink.getPrId());
                processMachlink1.setMaId(maId);
                processMachlink1.setPrepareTime(processMachlink1.getPrepareTime());
                processMachlink1.setSpeed(processMachlink1.getSpeed());
                processMachlinkMapper.insert(processMachlink1);
                processMachlink = processMachlink1;
                log.error("????????????????????????,????????????????????????????????????:maId:{}, prId:{}", maId, workbatchOrdlink.getPrId());
            }
        }

        System.out.print("===================================6");
        // SendMsgUtils.sendToUser(usIds, JSON.toJSONString(temp), ChatType.MAC_ACCEPTORDER.getType());//??????????????????????????????????????????
        System.out.print("===================================7");
        //???????????????????????????
        WorkbatchOrdlink ordlink = workbatchOrdlinkMapper.selectById(sdId);
        //????????????????????????
        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //????????????
        superviseBoxinfo.setBlnAccept(1);
        superviseBoxinfo.setWbNo(ordlink.getWbNo());
        superviseBoxinfoMapper.updateById(superviseBoxinfo);
        return R.ok(infoId);
    }


    private void setMainProgress(Integer sdId) {
        //TODO:???????????????????????????,????????????;???????????????????????????
        WorkbatchProgress workbatchProgress =
                workbatchProgressMapper.selectOne(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("wp_type", 3));
        if (workbatchProgress != null) {
            workbatchProgress.setStatus(2);//??????????????????
            workbatchProgressMapper.updateById(workbatchProgress);
            System.out.print("===================================4-2");
        }
    }

    /**
     * ????????????
     *
     * @param ordlinkVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R acceptOrder(WorkbatchOrdlinkVO ordlinkVO) {
        Integer maId = ordlinkVO.getMaId();  //??????id
        Integer sdId = ordlinkVO.getId();  //??????id
        Integer wsId = ordlinkVO.getWsId();  //????????????id
        SuperviseExecute superviseExecute = superviseExecuteMapper.selectOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
        //TODO:???????????????????????????,????????????;???????????????????????????
        WorkbatchProgress workbatchProgress =
                workbatchProgressMapper.selectOne(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("wp_type", 3));
        if (workbatchProgress != null) {
            workbatchProgress.setStatus(2);//??????????????????
            workbatchProgressMapper.updateById(workbatchProgress);
            System.out.print("===================================4-2");
        }
        //????????????????????????????????????
        // List<WorkbatchShift> workbatchShifts = workbatchShiftMapper.getProOrder(ordlinkVO.getMaId());


        if ("B".equals(superviseExecute.getExeStatus()) || "C".equals(superviseExecute.getExeStatus())) {
            log.info("???????????????????????????????????????:[maId:{}]", maId);
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "???????????????????????????????????????");
        }
//        if (StringUtils.isNotBlank(ordlinkVO.getOdNo())) {
//            OrderOrdinfo orderOrdinfo = orderOrdinfoMapper.getOrderInfoByodNo(ordlinkVO.getOdNo());
//            if (orderOrdinfo != null) {
//                Integer ordId = orderOrdinfo.getId(); //??????id
//                //??????????????????????????????
//                if (!orderOrdinfo.getProductionState().equals(OrderStatusConstant.RUN_STATUS_PRODUCTION)) {
//                    orderOrdinfo.setProductionState(OrderStatusConstant.RUN_STATUS_PRODUCTION);
//                    orderOrdinfoMapper.updateById(orderOrdinfo);
//                    newState.setOdId(ordId);  //??????id
//                }
//            }
//        }
        ExecuteState newState = new ExecuteState();
        Date currDate = new Date();
        String sdDate = ordlinkVO.getSdDate();
        //????????????id???????????????????????????
        SuperviseExecute executeOrder = executeMapper.getExecuteOrder(maId);
        String usIds = (executeOrder != null) ? executeOrder.getUsIds() : "all";
        newState.setTeamId(usIds);   //????????????????????????id
        newState.setMaId(maId);   //??????id
        newState.setSdId(sdId);   //??????id
        //???????????????????????????
        //WorkbatchShift wshift = getWorkbatchShift(currDate, maId, wsId, sdDate, sdId, ordlinkVO.getPlanNum(), ordlinkVO.getWfId());

        newState.setWfId(ordlinkVO.getWfId());//????????????????????????wfId???????????????
        newState.setWbId(ordlinkVO.getWbId());   //??????id
        newState.setUsId(ordlinkVO.getUsId());
        newState.setStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());    //????????????
        newState.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());   //??????
        newState.setStartAt(currDate);
        newState.setCreateAt(currDate);

        System.out.print("===================================2");
        WorkbatchOrdlink workbatchOrdlink = ordlinkMapper.selectOne(new QueryWrapper<WorkbatchOrdlink>().eq("id", sdId));

        //??????????????????????????????
        ExecuteInfo executeInfo = saveExcuteInfo(ordlinkVO, maId, sdId, newState, currDate, workbatchOrdlink);

        System.out.print("===================================1");
        //??????????????????????????????????????????
        UpdateStateUtils.updateSupervise(newState, executeInfo);


        System.out.print("===================================3");
        Integer infoId = executeInfo.getId();
//        TempEntity temp = new TempEntity();
//        temp.setStartTime(currDate);
//        temp.setOrderName(ordlinkMapper.getOrderName(sdId));
        //?????????????????????
        workbatchOrdlink.setRunStatus(1);//??????????????????
        Integer usId = ordlinkVO.getUsId();
        System.out.print("===================================4");
        if (usId != null) {//?????????id
            workbatchOrdlink.setUsId(usId);
        }
        updateAcceptByUsId(workbatchOrdlink);//???????????????????????????
        System.out.print("===================================4-1");
        //?????????????????????????????????????????????
        WorkbatchShift wshift = workbatchShiftMapper.selectById(ordlinkVO.getWfId());
        if (wshift != null) {
            wshift.setShiftStatus(1);//0:????????????1???????????????2???????????????  3?????????????????????????????? 4??????????????????????????? ??????????????????????????????
            wshift.setStatus("1");
            workbatchShiftMapper.updateById(wshift);
        }

        System.out.print("===================================5");
//        //??????C1????????????
//        SuperviseBoxinfo boxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
//        SuperviseExerun exeRunCache = superviseExerunMapper.selectOne(new QueryWrapper<SuperviseExerun>().eq("ma_id", boxinfo.getMaId()));
//        if (exeRunCache != null) {
//            //???????????????????????????
//            superviseExerunMapper.deleteById(exeRunCache);
//        }
//        SuperviseExerun superviseExerun = new SuperviseExerun();
//        superviseExerun.setCreateAt(new Date());
//        superviseExerun.setMaId(maId);
//        superviseExerun.setSdId(sdId);
//        if (boxinfo != null) {
//            superviseExerun.setNumber(boxinfo.getNumber());
//            superviseExerun.setUsIds(boxinfo.getUsIds());
//            superviseExerun.setUuid(boxinfo.getUuid());
//        }
//        superviseExerun.setRegular(processMachlink.getKeepRun() == null ? 10 : processMachlink.getKeepRun());//???????????????????????????10??????????????????????????????
//        superviseExerunMapper.insert(superviseExerun);//???????????????????????????????????????
        System.out.print("===================================58");
        //?????????????????????????????????ee
        ProcessMachlink processMachlink = processMachlinkMapper.getEntityByPrMt(maId, workbatchOrdlink.getPrId());
        if (processMachlink == null) {
            log.error("?????????????????????:maId:{}, prId:{}", maId, workbatchOrdlink.getPrId());
            List<ProcessMachlink> list = processMachlinkMapper.selectList(new QueryWrapper<ProcessMachlink>().eq("ma_id", maId));
            if (!list.isEmpty()) {
                ProcessMachlink processMachlink1 = list.get(0);
                processMachlink1.setPrId(workbatchOrdlink.getPrId());
                processMachlink1.setMaId(maId);
                processMachlink1.setPrepareTime(processMachlink1.getPrepareTime());
                processMachlink1.setSpeed(processMachlink1.getSpeed());
                processMachlinkMapper.insert(processMachlink1);
                processMachlink = processMachlink1;
                log.error("????????????????????????,????????????????????????????????????:maId:{}, prId:{}", maId, workbatchOrdlink.getPrId());
            }
        }

        System.out.print("===================================6");
        // SendMsgUtils.sendToUser(usIds, JSON.toJSONString(temp), ChatType.MAC_ACCEPTORDER.getType());//??????????????????????????????????????????
        System.out.print("===================================7");
        //???????????????????????????
        WorkbatchOrdlink ordlink = workbatchOrdlinkMapper.selectById(sdId);
        if (ordlink != null) {
            //???????????????
            String upProcess = ordlink.getUpPorcess();
            List<WorkbatchOrdlink> upProcessOrdlink = workbatchOrdlinkMapper.selectList(new QueryWrapper<WorkbatchOrdlink>().eq("wb_no", ordlink.getWbNo()).eq("up_porcess", ordlink.getUpPorcess()));
            if (!upProcessOrdlink.isEmpty()) {
                List<Integer> sdIds = upProcessOrdlink.stream().map(WorkbatchOrdlink::getId).collect(toList());
                List<WorkbatchShift> workbatchShiftList = workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>().in("sd_id", sdIds));
                List<Integer> wfIds = workbatchShiftList.stream().map(WorkbatchShift::getId).collect(toList());
                List<ExecuteTraycard> executeTraycards = executeTraycardMapper.selectList(new QueryWrapper<ExecuteTraycard>().in("wf_id", wfIds));
                if (!executeTraycards.isEmpty()) {
                    List<Integer> ids = executeTraycards.stream().map(ExecuteTraycard::getId).collect(toList());
                    List<StoreInventory> storeInventories = storeInventoryMapper.selectBatchIds(ids);
                    if (!storeInventories.isEmpty()) {
                        storeInventories.forEach(o -> {
                            StoreOutlog storeOutlog = new StoreOutlog();
                            storeOutlog.setCreateAt(LocalDateTime.now());
                            storeOutlog.setEtId(o.getEtId());
                            storeOutlog.setStType(o.getStType());
                            storeOutlog.setStId(o.getStId());
                            storeOutlog.setStNo(o.getStNo());
                            storeOutlog.setStSize(o.getStSize());
                            storeOutlog.setMlId(o.getMlId());
                            storeOutlog.setOperateType(1);
                            storeOutlog.setLayNum(o.getLayNum());
                            storeOutlogMapper.insert(storeOutlog);
                            //??????????????????
                            executeTraycardMapper.deleteById(o);
                        });
                    }
                }
            }
        }
        //????????????????????????
        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //????????????
        superviseBoxinfo.setBlnAccept(1);
        superviseBoxinfo.setWbNo(ordlink.getWbNo());
        superviseBoxinfoMapper.updateById(superviseBoxinfo);
        return R.ok(infoId);
    }

    private ExecuteInfo saveExcuteInfo(WorkbatchOrdlinkVO ordlinkVO, Integer maId, Integer sdId, ExecuteState
            newState, Date currDate, WorkbatchOrdlink workbatchOrdlink) {
        ExecuteInfo executeInfo = new ExecuteInfo();
        executeInfo.setMaId(maId);
        executeInfo.setSdId(sdId);
        executeInfo.setWfId(newState.getWfId());//??????????????????id??????
        executeInfo.setStartTime(currDate);
        executeInfo.setCreateAt(currDate);
        executeInfo.setStatus(0);   //??????????????????????????????????????? 0?????????1????????????2???????????????3????????????????????????
        executeInfo.setWsId(ordlinkVO.getWsId());
//        executeInfo.setSfStartTime(ordlinkVO.getSfStartTime());
//        executeInfo.setSfEndTime(ordlinkVO.getSfEndTime());
        executeInfo.setTargetDay(DateUtil.refNowDay());
        executeInfo.setWbNo(workbatchOrdlink.getWbNo()); //TODO WYN????????????????????????????????? ????????????ordlink???????????????
        if (ordlinkVO.getUsId() != null)
            executeInfo.setUsId(ordlinkVO.getUsId().toString());
        infoMapper.insert(executeInfo);//???????????????????????????
        return executeInfo;
    }

    /****
     * ???????????????????????????
     * @param maId
     * @param wbshift
     * @param currDate
     * @param wbNo
     * @param wsId
     * @param targetDay
     * @return
     */
    private ExecuteInfo saveExcuteInfoNew(Integer maId, WorkbatchShift wbshift, Date currDate, String wbNo, Integer wsId, String targetDay, Integer usId) {
        ExecuteInfo executeInfo = new ExecuteInfo();
        executeInfo.setMaId(maId);
        executeInfo.setSdId(wbshift.getSdId());
        executeInfo.setWfId(wbshift.getId());//??????????????????id?????????wbshift?????????????????????
        executeInfo.setStartTime(currDate);
        executeInfo.setCreateAt(currDate);
        executeInfo.setStatus(0);   //??????????????????????????????????????? 0?????????1????????????2???????????????3????????????????????????
        executeInfo.setWsId(wsId);//???????????????????????????
        executeInfo.setWbNo(wbNo);
//        executeInfo.setSfStartTime(ordlinkVO.getSfStartTime());
//        executeInfo.setSfEndTime(ordlinkVO.getSfEndTime());
        executeInfo.setTargetDay(targetDay);
        executeInfo.setWbNo(wbNo); //TODO WYN????????????????????????????????? ????????????ordlink???????????????  ???????????????yilong???????????????????????????????????????
        if (usId != null)
            executeInfo.setUsId(usId.toString());//??????????????????????????????
        infoMapper.insert(executeInfo);//???????????????????????????

        return executeInfo;
    }

    private int updateAcceptByUsId(WorkbatchOrdlink ordlink) {
        return workbatchOrdlinkMapper.updateAcceptByUsId(ordlink.getId(), ordlink.getRunStatus());
    }

    /*****
     * ?????????????????????????????????????????????
     * @return
     */
    private WorkbatchShift getWorkbatchShift(Date nowday, Integer maId, Integer wsId, String targetDay, Integer
            sdId, Integer planum, Integer wfId) {
        if (wsId == null || wsId <= 0) {
            WorkbatchShiftset wfset = workbatchShiftsetMapper.findByMaIdWsTime(maId, DateUtil.refNowDay(nowday));
            wsId = wfset.getId();
        }
        WorkbatchShift wshift = workbatchShiftMapper.selectById(wfId);
        if (wshift == null || wshift.getId() == null) {
            wshift = new WorkbatchShift();
            wshift.setWsId(wsId);
            wshift.setSdId(sdId);
            wshift.setSdDate(targetDay);
            wshift.setPlanNum(planum);
            workbatchShiftMapper.insert(wshift);//???????????????????????????????????????????????????????????????????????????????????????
        }
        return wshift;
    }


    /**
     * ?????????????????????????????????
     *
     * @param ordlinkVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R proOrder(WorkbatchOrdlinkVO ordlinkVO) {
        WorkbatchOrdlinkShiftVO w = workbatchOrdlinkMapper.getWorkbatchOrd(ordlinkVO.getId(), ordlinkVO.getSdDate(), ordlinkVO.getWsId(), ordlinkVO.getMaId(), ordlinkVO.getWfId());
        Date currTime = new Date();
        ordlinkVO.setCompleteNum(w.getCompleteNum());
        Integer sdId = ordlinkVO.getId();
        Integer maId = ordlinkVO.getMaId();
        Integer wsId = ordlinkVO.getWsId();//???????????????wsId????????????
        String sdDate = ordlinkVO.getSdDate();//????????????????????????
        Integer ordId = null;
        if (StringUtils.isNotBlank(ordlinkVO.getOdNo())) {
            OrderOrdinfo orderOrdinfo = orderOrdinfoMapper.getOrderInfoByodNo(ordlinkVO.getOdNo());
            if (orderOrdinfo != null) {
                //??????id
                ordId = orderOrdinfo.getId();
            }
        }

        //????????????????????????
        SuperviseExecute executeOrder = getExecuteOrder(maId);
        Integer exId = executeOrder.getExId();
        ordlinkVO.setInfoId(exId);
        //???????????????????????????
        ExecuteState newState = new ExecuteState();
        String usIds = executeOrder.getUsIds();
        newState.setStartAt(currTime);
        newState.setTeamId(usIds);   //????????????????????????id
        newState.setMaId(maId);   //??????id
        newState.setSdId(sdId);   //??????id
        newState.setOdId(ordId);  //??????id

        //??????????????????????????????????????????????????????????????????
        // WorkbatchShift wshift = getWorkbatchShift(currTime, maId, wsId, sdDate, sdId, ordlinkVO.getPlanNum(), ordlinkVO.getWfId());
        Integer wfId = ordlinkVO.getWfId();
        newState.setWfId(wfId);//??????????????????id??????
        newState.setWbId(ordlinkVO.getWbId());   //??????id
        newState.setTeamId(executeOrder.getUsIds());
        newState.setUsId(ordlinkVO.getUsId());
        newState.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());   //?????????
        newState.setEvent(GlobalConstant.ProType.PRODUCT_EVENT.getType());    //??????????????????
        newState.setCreateAt(currTime);
        UpdateStateUtils.updateSupervise(newState, null);//???????????????????????????????????????exId?????????????????????
        //????????????????????????
//        ordlinkVO.setStatus(OrderStatusConstant.STATUS_PRODUCTION);
//        ordlinkVO.setRunStatus(OrderStatusConstant.RUN_STATUS_PRODUCTION);
        ordlinkVO.setActuallyStarttime(currTime);
//        List<ExecuteBriefer> executeBrieferList =
//                brieferMapper.selectList(new QueryWrapper<ExecuteBriefer>().eq("wf_id", wfId));
//        Integer completeNum = 0;
//        for (ExecuteBriefer executeBriefer : executeBrieferList) {
//            Integer productNum = executeBriefer.getProductNum();//?????????
//            productNum = productNum == null ? 0 : productNum;
//            completeNum += productNum;
//        }
//        ordlinkVO.setCompleteNum(completeNum);//???????????????????????????
        ordlinkVO.setIncompleteNum(ordlinkVO.getPlanNumber());
        ordlinkVO.setPlanNum(ordlinkVO.getPlanNumber());
        ordlinkVO.setStatus("9");
        ordlinkMapper.updateById(ordlinkVO);
        WorkbatchShift wshift = workbatchShiftMapper.selectById(ordlinkVO.getWfId());
        if (wshift != null && wshift.getId() != null) {
            wshift.setShiftStatus(1);//-1:????????????0:????????????1???????????????2???????????????  3?????????????????????????????? 4??????????????????????????? ??????????????????????????????
            wshift.setStatus("2");//?????????0??????1??????2????????????3?????????4?????????5????????? -1?????????ERP?????? 6??????7?????????8????????????9?????????
            workbatchShiftMapper.updateById(wshift);
        }

        //???????????????C1????????????
        ExecuteInfo executeInfo = infoMapper.selectById(ordlinkVO.getInfoId());
        executeInfo.setStatus(1);//?????????0?????????1????????????2???????????????3???????????????
        executeInfo.setExeTime(currTime);
        infoMapper.updateById(executeInfo);
//        /***
//         * ?????? ????????????
//         */
//        SendMsgUtils.sendToUser(usIds, JSON.toJSONString(temp),
//                ChatType.MAC_PROORDER.getType());
        return R.ok(ordlinkVO);
    }


    /*****
     * ?????????????????????????????????
     * @param maId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R proOrderNew(Integer maId) {
        //?????????????????????????????????????????????????????????????????????????????????????????????
        SuperviseExecute supexecute = superviseExecuteMapper.getExecuteOrder(maId);

        System.out.println("--------------------->supexecute:::maId:" + maId + ":::::::proOrderNew:");
        if (supexecute != null && supexecute.getExeStatus() != null && !"B".equalsIgnoreCase(supexecute.getExeStatus())) {
            return R.error("????????????????????????????????????.");
        }

        Integer exId = supexecute.getExId();
        Date currTime = new Date();
        //???????????????C1????????????
        ExecuteInfo executeInfo = infoMapper.selectById(exId);
        if (executeInfo == null || executeInfo.getId() == null) {
            return R.error("??????????????????????????????????????????");
        }
        executeInfo.setStatus(1);//?????????0?????????1????????????2???????????????3???????????????
        executeInfo.setExeTime(currTime);
        infoMapper.updateById(executeInfo);

        //???????????????????????????
        ExecuteState newState = new ExecuteState();
        String usIds = supexecute.getUsIds();
        newState.setStartAt(currTime);
        newState.setTeamId(usIds);   //????????????????????????id
        newState.setMaId(maId);   //??????id
        newState.setSdId(executeInfo.getSdId());   //??????id
        newState.setWbId(executeInfo.getWfId());  //??????id

        //??????????????????????????????????????????????????????????????????
        // WorkbatchShift wshift = getWorkbatchShift(currTime, maId, wsId, sdDate, sdId, ordlinkVO.getPlanNum(), ordlinkVO.getWfId());
        Integer wfId = executeInfo.getWfId();
        newState.setWfId(wfId);//??????????????????id??????
//        newState.setWbId(ordlinkVO.getWbId());   //??????id
        newState.setTeamId(usIds);
        String optId = executeInfo.getUsId();
        Integer optIdInt = (optId != null && optId.length() > 0) ? Integer.parseInt(optId) : null;
        newState.setUsId(optIdInt);
        newState.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());   //?????????
        newState.setEvent(GlobalConstant.ProType.PRODUCT_EVENT.getType());    //??????????????????
        newState.setCreateAt(currTime);
        UpdateStateUtils.updateSupervise(newState, null);//???????????????????????????????????????exId?????????????????????


        WorkbatchShift wshift = workbatchShiftMapper.selectById(wfId);
        if (wshift != null && wshift.getId() != null) {
            wshift.setShiftStatus(1);//-1:????????????0:????????????1???????????????2???????????????  3?????????????????????????????? 4??????????????????????????? ??????????????????????????????
            wshift.setStatus("2");//?????????0??????1??????2????????????3?????????4?????????5????????? -1?????????ERP?????? 6??????7?????????8????????????9?????????
            workbatchShiftMapper.updateById(wshift);
        }
        return R.ok("???????????????????????????????????????");
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R finishOrder(Integer maId, Integer sdId, Integer usId, Integer exId, Integer wfId, Integer exStatus) {
        RLock rlock = redisson.getLock(lockKey);
        try{
            rlock.lock(90, TimeUnit.SECONDS);
            if (exStatus == null) {
                exStatus = 3;//???????????????????????????
            }
            List<ExecuteBriefer> executeBrieferList =
                    executeBrieferMapper.selectList(new QueryWrapper<ExecuteBriefer>().eq("ex_id", exId));
            if (!executeBrieferList.isEmpty()) {
                return R.ok("????????????????????????");
            }
            Date currTime = new Date();
            //????????????????????????
            SuperviseExecute executeOrder = getExecuteOrder(maId);
            Integer currNum = executeOrder.getCurrNum();//????????????
            //???????????????
            new Thread(() -> {
                List<WorkbatchProgress> gressList =
                        workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("wp_type", 3));
                if (!gressList.isEmpty()) {
                    updateWorkbatchProgress(currNum, gressList);
                }
            }).start();
            String usIds = executeOrder.getUsIds();
            Integer orderId = null;
//        OrderWorkbatch orderWorkbatch = orderWorkbatchService.getById(sdId);
//        if (orderWorkbatch != null) {
//            orderId = orderWorkbatch.getOdId();
//        }
            //????????????id ????????????id
            ExecuteState newState = getExecuteState(maId, sdId, usId, wfId, currTime, executeOrder, usIds, orderId);
            //????????????????????????
            WorkbatchOrdlink ordlink = ordlinkMapper.selectById(sdId);
//        ordlink.setStatus(OrderStatusConstant.STATUS_PRODUCTION);
            if (ordlink != null) {
                ordlink.setRunStatus(OrderStatusConstant.RUN_STATUS_NOREPORT);    //?????????
                ordlinkMapper.updateById(ordlink);
            }

            WorkbatchShift wshift = workbatchShiftMapper.selectById(wfId);
            if (wshift != null) {
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                Integer exeByother = getExecuteOrderByother(maId);
                //????????????????????????????????????????????????????????????????????????
                if (exeByother < 1) {
                    //????????????????????????????????????????????????
                    wshift.setShiftStatus(OrderStatusConstant.RUN_STATUS_NOREPORT);//?????????3
                    wshift.setStatus(OrderStatusConstant.STATUS_PRODUCTION); //????????????2
                    workbatchShiftMapper.updateById(wshift);//???????????????????????????
                }
            }

            //??????????????????????????????
            ExecuteInfo executeInfo = infoMapper.selectById(executeOrder.getExId());
            if (executeInfo != null) {
                executeInfo.setEndTime(currTime);//D1??????????????????
                executeInfo.setStatus(exStatus);  //????????????????????????????????????//0?????????1????????????2???????????????3????????????????????????
                infoMapper.updateById(executeInfo);
            }

            /**
             * ?????????????????????????????????
             */
            ExecuteBriefer briefer = new ExecuteBriefer();
            briefer.setSdId(sdId);
            briefer.setBoxNum(executeOrder.getCurrNum());
            briefer.setEsId(newState.getId());
            briefer.setWfId(newState.getWfId());//?????????????????????ID??????
            briefer.setStartTime(executeOrder.getStartTime());  //??????????????????
            briefer.setEndTime(currTime);
            briefer.setCreateAt(currTime);
            briefer.setHandle(0);   //?????????????????????
            briefer.setExId(executeInfo.getId());  //????????????????????????Id
            briefer.setReadyNum(executeOrder.getReadyNum());
            if (brieferMapper.insert(briefer) <= 0) {
                return R.error("?????????????????????????????????");
            }
            try {
                statisMachreachService.createShiftStatisMachreach(briefer.getId(), wshift, ordlink.getMaId(), executeOrder.getCurrNum());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //????????????????????????
            SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
            //????????????????????????
            superviseBoxinfo.setBlnAccept(0);
            superviseBoxinfo.setWbNo(null);
            superviseBoxinfoMapper.updateById(superviseBoxinfo);
            //?????????????????????0
            executeOrder.setReadyNum(0);
            executeOrder.setReadyTime(null);
            executeOrder.setEvent(GlobalConstant.ProType.FINISH_EVENT.getType());
            executeOrder.setExeStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());
            executeOrder.setEndTime(currTime);
            superviseExecuteMapper.update(executeOrder);

            if (0 != executeOrder.getCurrNum()) {
                //?????????????????????
                Integer upSdId = workbatchOrdlinkMapper.getUpProcessSdIdByWfId(wfId);
                if (upSdId != null) {
                    storeInventoryService.outInventory(upSdId, executeOrder.getCurrNum());
                }
                InInventoryByNumberVO inInventoryRequest = new InInventoryByNumberVO();
                inInventoryRequest.setExId(exId);
                inInventoryRequest.setNumber(executeOrder.getCurrNum());
                inInventoryRequest.setStType(1);
                storeInventoryService.inInventoryByNumber(inInventoryRequest);
            }
            return R.ok();
        }finally {
            rlock.unlock();
        }
    }


    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R finishOrderNew(Integer maId) {
        //, Integer sdId, Integer usId, Integer exId, Integer wfId, Integer exStatus
        //????????????????????????????????????????????????????????????
        Integer exStatus = 3; //????????????????????????
//        if(exStatus == null){
//            exStatus = 3;//???????????????????????????
//        }
        //????????????????????????????????????????????????????????????????????????????????????????????????
        SuperviseExecute execute = executeMapper.getExecuteOrder(maId);
        Integer exId = execute.getExId();
        //?????????????????????????????????????????????????????????
        ExecuteBriefer executeBriefer = executeBrieferMapper.getByExId(exId);
        if (executeBriefer != null && executeBriefer.getId() != null) {
            return R.ok("????????????????????????");
        }

        Date currTime = new Date();
        //????????????????????????
//        SuperviseExecute executeOrder = getExecuteOrder(maId);

        //??????????????????????????????
        ExecuteInfo executeInfo = infoMapper.selectById(exId);
        if (executeInfo == null || executeInfo.getId() == null)
            return R.error("????????????????????????");
        Integer sdId = executeInfo.getSdId();
        Integer wfId = executeInfo.getWfId();

//        Integer currNum = execute.getCurrNum();//????????????

//        String usIds = execute.getUsIds();
//        Integer orderId = null;

        //????????????id ????????????id??  ??????????????????????????????????????????
//        ExecuteState newState = getExecuteState(maId, sdId, usId, wfId, currTime, execute, usIds, null);
        //????????????????????????
//        WorkbatchOrdlink ordlink = ordlinkMapper.selectById(sdId);
//        ordlink.setStatus(OrderStatusConstant.STATUS_PRODUCTION);
//        if (ordlink != null) {
//            ordlink.setRunStatus(OrderStatusConstant.RUN_STATUS_NOREPORT);    //?????????
//            ordlinkMapper.updateById(ordlink);
//        }


        //????????????shift??????????????????
        WorkbatchShift wshift = workbatchShiftMapper.selectById(wfId);
        if (wshift != null) {
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            Integer exeByother = getExecuteOrderByother(maId);
            //????????????????????????????????????????????????????????????????????????
            if (exeByother < 1) {
                //????????????????????????????????????????????????
                wshift.setShiftStatus(OrderStatusConstant.RUN_STATUS_NOREPORT);//?????????3
                wshift.setStatus(OrderStatusConstant.STATUS_PRODUCTION); //????????????2
                workbatchShiftMapper.updateById(wshift);//???????????????????????????
            }
        }

        //??????????????????????????????
        if (executeInfo != null) {
            executeInfo.setEndTime(currTime);//D1??????????????????
            executeInfo.setStatus(exStatus);  //????????????????????????????????????//0?????????1????????????2???????????????3????????????????????????
            infoMapper.updateById(executeInfo);
        }

        /**
         * ?????????????????????????????????
         */
        ExecuteBriefer briefer = new ExecuteBriefer();
        briefer.setSdId(sdId);
        briefer.setBoxNum(execute.getCurrNum());
//        briefer.setEsId(newState.getId());
        briefer.setWfId(wfId);//?????????????????????ID??????
        briefer.setStartTime(execute.getStartTime());  //??????????????????
        briefer.setEndTime(currTime);
        briefer.setCreateAt(currTime);
        briefer.setHandle(0);   //?????????????????????
        briefer.setExId(exId);  //????????????????????????Id
        briefer.setReadyNum(execute.getReadyNum());
        //?????????????????????
        if (brieferMapper.insert(briefer) <= 0) {
            return R.error("?????????????????????????????????");
        }

        try {
            //??????????????????????????????
            statisMachreachService.createShiftStatisMachreach(briefer.getId(), wshift, maId, execute.getCurrNum());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //????????????????????????
        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //????????????????????????
        superviseBoxinfo.setBlnAccept(0);
        superviseBoxinfo.setWbNo(null);
        superviseBoxinfoMapper.updateById(superviseBoxinfo);
        //?????????????????????0
        execute.setReadyNum(0);
        execute.setReadyTime(null);
        execute.setEvent(GlobalConstant.ProType.FINISH_EVENT.getType());//??????D1????????????
        execute.setExeStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());//???????????????D
        execute.setEndTime(currTime);
        //??????????????????
        execute.setExId(null);
        execute.setSdId(null);
        execute.setWfId(null);
        execute.setWbId(null);
        superviseExecuteMapper.updateById(execute);
        return R.ok();
    }

    private ExecuteState getExecuteState(Integer maId, Integer sdId, Integer usId, Integer wfId, Date
            currTime, SuperviseExecute executeOrder, String usIds, Integer orderId) {
        ExecuteState newState = new ExecuteState();
        newState.setTeamId(usIds);   //????????????????????????id
        newState.setMaId(maId);   //??????id
        newState.setSdId(sdId);   //??????id
        newState.setWfId(wfId);//??????????????????id??????

        newState.setOdId(orderId); //??????id
        newState.setUsId(usId);

        newState.setWbId(executeOrder.getWbId());   //??????id
        newState.setStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());   //??????????????????
        newState.setEvent(GlobalConstant.ProType.FINISH_EVENT.getType());     //??????????????????
        newState.setStartAt(currTime);
        newState.setCreateAt(currTime);
        //?????????????????????????????????????????????????????????????????????
        UpdateStateUtils.updateSupervise(newState, null);//????????????????????????????????????????????????B1???????????????????????????
        return newState;
    }

    private void updateWorkbatchProgress(Integer currNum, List<WorkbatchProgress> gressList) {
        currNum = currNum == null ? 0 : currNum;
        WorkbatchProgress workbatchProgress = gressList.get(0);
        String wbNo = workbatchProgress.getWbNo();//????????????
        String ptName = workbatchProgress.getPtName();//????????????
        /*????????????*/
        List<WorkbatchProgress> workbatchProgressList = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                .eq("wb_no", wbNo).eq("pt_name", ptName).eq("wp_type", 2));
        /*????????????*/
        List<WorkbatchProgress> progressList =
                workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("wb_no", wbNo).eq("wp_type", 1));

        Integer planCount = workbatchProgress.getPlanCount();//????????????
        Double rate = 0.0;
        if (planCount != null || planCount != 0) {
            rate = currNum / (double) planCount;
        }
        Integer totalTime = workbatchProgress.getTotalTime();//???????????????
        totalTime = totalTime == null ? 0 : totalTime;
        Integer time = totalTime - (int) (totalTime * rate);//????????????
        workbatchProgress.setStayTime(time);//????????????
        workbatchProgress.setStatus(3);
        workbatchProgress.setRealCount(currNum);//???????????????
        workbatchProgress.setUpdateAt(new Date());

        if (!workbatchProgressList.isEmpty()) {
            WorkbatchProgress progress = workbatchProgressList.get(0);
            Integer realCount = progress.getRealCount();//????????????
            realCount = realCount == null ? 0 : realCount;
            realCount += currNum;
            progress.setRealCount(realCount);
            progress.setUpdateAt(new Date());
            workbatchProgressMapper.updateById(progress);
        }
        if (!progressList.isEmpty()) {
            WorkbatchProgress progress = progressList.get(0);
            Integer realCount = progress.getRealCount();//????????????
            realCount = realCount == null ? 0 : realCount;
            realCount += currNum;
            progress.setRealCount(realCount);
            progress.setUpdateAt(new Date());
            workbatchProgressMapper.updateById(progress);
        }
        workbatchProgressMapper.updateById(workbatchProgress);
    }

    /**
     * ?????????????????????
     *
     * @param maId
     * @param sdId
     * @param executeOrder
     * @param ordlink
     * @param executeInfo
     * @param currTime
     */
    private void saveReachRate(Integer maId, Integer sdId, SuperviseExecute executeOrder, WorkbatchOrdlinkVO
            ordlink, ExecuteInfo executeInfo, Date currTime) {
        Calendar calendar = Calendar.getInstance();
        String targetDay = DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER);
        int targetHour = calendar.get(Calendar.HOUR_OF_DAY); // ??????24????????????
        int targetMin = calendar.get(Calendar.MINUTE); // ???
        if (targetMin > 55 || targetMin < 5) {
            targetMin = 0;
        }
        if (targetMin > 40 && targetMin < 50) {
            targetMin = 45;
        }
        if (targetMin > 25 && targetMin < 35) {
            targetMin = 30;
        }
        if (targetMin > 10 && targetMin < 20) {
            targetMin = 15;
        }
        //??????????????????
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
        //???????????????
        StatisOrdreach statisOrdreach = new StatisOrdreach();
        statisOrdreach.setTargetDay(targetDay);
        statisOrdreach.setTargetHour(targetHour);
        statisOrdreach.setTargetMin(targetMin);
        statisOrdreach.setMaId(maId);
        statisOrdreach.setMaName(machineMainfo.getName());
        statisOrdreach.setPdName(ordlink.getPdName());
        //statisOrdreach.setSdId(ordlink.getId().toString());
        statisOrdreach.setExId(executeInfo.getId());
        //??????oee??????
        WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.getOeeBySdId(sdId);
        //?????????????????????
        Integer currNum = executeOrder.getCurrNum();
        //?????????????????????
        BigDecimal planSpeed = BigDecimal.valueOf(workbatchOrdoee.getSpeed() / 60).setScale(2);
        BigDecimal prePlanTime = BigDecimal.valueOf(workbatchOrdoee.getPlanTotalTime());
        //??????????????????
        BigDecimal planNum = prePlanTime.multiply(planSpeed);
        //???????????????
        BigDecimal reachRate = BigDecimal.ZERO;
        try {
            reachRate = BigDecimal.valueOf(currNum).divide(planNum, 2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            log.error("????????????????????????????????????????????????0:[maId:{}, sdId:{}]", maId, sdId);
        }
        statisOrdreach.setReachRate(reachRate);
        statisOrdreach.setPlanNum(planNum.toString());
        statisOrdreach.setRealCount(currNum);
        statisOrdreach.setWbNo(ordlink.getWbNo());
        //??????????????????
//        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(ordlink.getWsId());
        WorkbatchShiftset workbatchShift = workbatchShiftsetMapper.selectByMaid(ordlink.getWsId(), ordlink.getMaId());
        statisOrdreach.setWsId(workbatchShift.getId());
        statisOrdreach.setWsName(workbatchShift.getCkName());
        statisOrdreach.setCreateAt(currTime);

        statisOrdreachMapper.insert(statisOrdreach);
    }


    /**
     * ???????????????????????????
     *
     * @param maId
     * @return
     */
    @Override
    public int getCurrNum(Integer maId) {
        Integer currNum = executeMapper.getCurrNum(maId);
        if (currNum == null) {
            currNum = 0;
        }
        return currNum;
    }

    /**
     * ??????????????????
     *
     * @param sdId
     * @return
     */
    @Override
    public String getOrderName(Integer sdId) {
        return ordlinkMapper.getOrderName(sdId);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @Override
    public List<WorkbatchOrdlinkVO> getNoReportOrder(Integer maId) {
        List<WorkbatchOrdlinkVO> list = null;
        //?????????????????????????????????????????????????????????
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
        if (machineMainfo != null && machineMainfo.getIsRecepro() == 1) {
            list = ordlinkMapper.getNoReportOrderByIsRecepro(maId);
        } else {
            /**
             * ????????????????????????id
             */
            list = ordlinkMapper.getNoReportOrder(maId);
        }
        if (list != null) {
            for (WorkbatchOrdlinkVO ordlink1 : list) {
                Integer bfId = ordlink1.getBid();
                ExecuteBriefer executeBriefer = executeBrieferMapper.selectOne(Wrappers.<ExecuteBriefer>lambdaQuery().select(ExecuteBriefer::getExId).eq(ExecuteBriefer::getId, bfId));
                List<ExecuteTraycard> executeTraycards = executeTraycardMapper.selectList(Wrappers.<ExecuteTraycard>lambdaQuery().eq(ExecuteTraycard::getExId, executeBriefer.getExId()));
                int trayTotalNum = executeTraycards.stream().mapToInt(ExecuteTraycard::getTrayNum).sum();
                Integer sdId = ordlink1.getId();
                Double faultTime = stateMapper.selectFaultTimeById(maId, sdId);
                if (faultTime == null) {
                    faultTime = 0.0;
                }
                Double proTime = stateMapper.selectProTimeById(maId, sdId);
                if (proTime == null) {
                    proTime = 0.0;
                }
                Integer wasteNum = wasteMapper.selectWasteBySdId(sdId);
                if (wasteNum == null) {
                    wasteNum = 0;
                }
                ordlink1.setIncompleteNum(ordlink1.getShiftNum());
                ordlink1.setCompleteNum(ordlink1.getFinishNum());
                ordlink1.setWasteNum(wasteNum);
                ordlink1.setProTime(proTime);
                ordlink1.setFaltTime(faultTime);
                ordlink1.setSdId(sdId);
                ordlink1.setPtName(ordlink1.getPartName());//????????????
                ordlink1.setTrayTotalNum(trayTotalNum);
            }
        }

        return list;
    }


    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @Override
    public List<ExecuteBrieferOrdlinkVO> getNoReportOrderNew(Integer maId) {
        List<ExecuteBrieferOrdlinkVO> list = executeBrieferMapper.notReportBrieferList(maId);
        return list;
    }

    @Override
    public List<TempEntity> getFaultAndQuality(Integer maId, String event) {
        return ordlinkMapper.getFaultAndQuality(maId, event);
    }

    @Override
    public List<WorkbatchOrdlinkVO> getMaAssignment(String startTime, Integer maId, String ckName) {
        if (startTime.length() > 10) {
            startTime = startTime.substring(0, 9);
        }
        List<WorkbatchOrdlinkVO> maAssignments = ordlinkMapper.getMaAssignment(startTime, maId, ckName);
        if (!maAssignments.isEmpty()) {
            for (WorkbatchOrdlinkVO workbatchOrdlinkVO : maAssignments) {
                /*????????????????????????*/
                BaseDeptinfo baseDeptinfo = baseDeptinfoMapper.selectById(workbatchOrdlinkVO.getDpId());
                workbatchOrdlinkVO.setDpName(baseDeptinfo.getDpName());//????????????
                /*????????????oee??????*/
                WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.selectOne(new QueryWrapper<WorkbatchOrdoee>().eq("wk_id", workbatchOrdlinkVO.getId()));
                if (workbatchOrdoee != null) {
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("wk_oee_id", workbatchOrdoee.getId());
                    /*????????????oee??????????????????*/
                    List workbatchordoeeMaintainList = workbatchordoeeMaintainMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMaintainList(workbatchordoeeMaintainList);
                    /*????????????oee??????????????????*/
                    List workbatchordoeeMealList = workbatchordoeeMealMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMealList(workbatchordoeeMealList);
                    /*????????????oee????????????*/
                    List workbatchordoeeMouldList = workbatchordoeeMouldMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMouldList(workbatchordoeeMouldList);
                    /*????????????oee??????*/
                    workbatchOrdlinkVO.setWorkbatchOrdoee(workbatchOrdoee);
                }
            }
        }

        return maAssignments;
    }


    @Override
    public List<WorkbatchOrdlinkVO> getDeptMachineAll(String startTime, Integer dpId, String ckName) {
        if (startTime.length() > 10) {
            startTime = startTime.substring(0, 9);
        }
        /*?????????????????????????????????????????????*/
        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList = ordlinkMapper.getDeptMachineAll(startTime, dpId, ckName);
        if (!workbatchOrdlinkVOList.isEmpty()) {
            for (WorkbatchOrdlinkVO workbatchOrdlinkVO : workbatchOrdlinkVOList) {
                /*????????????????????????*/
                BaseDeptinfo baseDeptinfo = baseDeptinfoMapper.selectById(workbatchOrdlinkVO.getDpId());
                workbatchOrdlinkVO.setDpName(baseDeptinfo.getDpName());//????????????
                /*????????????oee??????*/
                WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.selectOne(new QueryWrapper<WorkbatchOrdoee>().eq("wk_id", workbatchOrdlinkVO.getId()));
                if (workbatchOrdoee != null) {
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("wk_oee_id", workbatchOrdoee.getId());
                    /*????????????oee??????????????????*/
                    List workbatchordoeeMaintainList = workbatchordoeeMaintainMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMaintainList(workbatchordoeeMaintainList);
                    /*????????????oee??????????????????*/
                    List workbatchordoeeMealList = workbatchordoeeMealMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMealList(workbatchordoeeMealList);
                    /*????????????oee????????????*/
                    List workbatchordoeeMouldList = workbatchordoeeMouldMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMouldList(workbatchordoeeMouldList);
                    /*????????????oee??????*/
                    workbatchOrdlinkVO.setWorkbatchOrdoee(workbatchOrdoee);
                }
            }
        }

        return workbatchOrdlinkVOList;
    }

    @Override
    public List<WorkbatchOrdlinkVO> getUserRunOrder(Integer usId) {
        WorkbatchOrdlink runOrder = WorkbatchOrdlink.getInstanceProduction();
        runOrder.setUsId(usId);

        return ordlinkMapper.getUserRunOrder(runOrder);
    }

    @Override
    public List<WorkbatchOrdlinkVO> getUserBrieferOrder(Integer usId) {
        List<SuperviseExecute> superviseExecuteList =
                executeMapper.selectList(new QueryWrapper<SuperviseExecute>().eq("operator", usId));
        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList = ordlinkMapper.getUserBrieferOrder(usId);
        if (!workbatchOrdlinkVOList.isEmpty()) {
            for (WorkbatchOrdlinkVO ordlink : workbatchOrdlinkVOList) {
                for (SuperviseExecute SuperviseExecute : superviseExecuteList) {
                    Integer sdId = ordlink.getId();
                    if (sdId.equals(SuperviseExecute.getSdId())) {
                        Integer maId = SuperviseExecute.getMaId();
                        Double faultTime = stateMapper.selectFaultTimeById(maId, sdId);
                        if (faultTime == null) {
                            faultTime = 0.0;
                        }
                        Double proTime = stateMapper.selectProTimeById(maId, sdId);
                        if (proTime == null) {
                            proTime = 0.0;
                        }
                        Integer wasteNum = wasteMapper.selectWasteBySdId(sdId);
                        if (wasteNum == null) {
                            wasteNum = 0;
                        }
                        ordlink.setWasteNum(wasteNum);
                        ordlink.setProTime(proTime);
                        ordlink.setFaltTime(faultTime);
                    }
                }
            }
        }

        return workbatchOrdlinkVOList;
    }

    @Override
    public List<WorkbatchOrdlink> getSdOrderList(Integer maId) {
        List<WorkbatchOrdlink> workbatchOrdlink1 = ordlinkMapper.getMachineSdOrderList(maId);
        List<WorkbatchOrdlink> ordlink = ordlinkMapper.getNoMachineSdOrderList(maId);
        if (workbatchOrdlink1.isEmpty()) {
            workbatchOrdlink1 = new ArrayList<>();
        }
        if (!ordlink.isEmpty()) {
            workbatchOrdlink1.addAll(ordlink);
        }
        return workbatchOrdlink1;

    }

    @Override
    public List<WorkbatchOrdlink> getSortUP(Integer id) {
        return ordlinkMapper.getSortUP(id);
    }

    @Override
    public List<WorkbatchOrdlink> getSortDown(Integer id) {
        return ordlinkMapper.getSortDown(id);
    }

    @Override
    public String getSdSort() {
        return ordlinkMapper.getSdSort();
    }

    /*?????????????????????oee??????*/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insertOrdOEEOrdlink(WorkbatchOrdoeeVo workbatchOrdoeeVO) {
        Date closeTime = null;
        WorkbatchOrdlinkVO workbatchOrdlink = workbatchOrdoeeVO.getWorkbatchOrdlink();
        String odNo = workbatchOrdlink.getOdNo();
        OrderOrdinfo orderOrdinfo =
                orderOrdinfoMapper.selectOne(new QueryWrapper<OrderOrdinfo>().eq("od_no", odNo));
        String cmName = null;
        if (orderOrdinfo != null) {
            cmName = orderOrdinfo.getCmName();
        }
        workbatchOrdlink.setCmName(cmName);//????????????
        Integer ptId = workbatchOrdlink.getPtId();//??????id
        /*??????????????????*/
        List<MaterProdlink> materProdlinkList =
                materProdlinkMapper.selectList(new QueryWrapper<MaterProdlink>().eq("pt_id", ptId));
        if (!materProdlinkList.isEmpty()) {
            MaterProdlink materProdlink = materProdlinkList.get(0);//???????????????,????????????
            workbatchOrdlink.setPtSize(materProdlink.getSize());//?????????????????????????????????
        }
        SimpleDateFormat sdfSdDate = new SimpleDateFormat("yyyy-MM-dd");
        workbatchOrdlink.setSdDate(sdfSdDate.format(workbatchOrdlink.getStartTime()));//????????????
        /*??????????????????*/
        ProdPdinfoVO prodPdinfoVOByWbId = prodPdinfoMapper.getProdPdinfoVOByWbId(workbatchOrdlink.getWbId());
        /*????????????*/
        workbatchOrdlink.setPdCode(prodPdinfoVOByWbId.getPdNo());
        /*????????????*/
        workbatchOrdlink.setPdImageurl(prodPdinfoVOByWbId.getImageUrl());
        /*????????????*/
        workbatchOrdlink.setPdName(prodPdinfoVOByWbId.getPdName());
        /*????????????*/
        workbatchOrdlink.setPdType(prodPdinfoVOByWbId.getClName());
        /*????????????????????????*/
        Integer prId = workbatchOrdlink.getPrId();
        ProcessWorkinfo processWorkinfo = processWorkinfoMapper.selectById(prId);
        OrderWorkbatch orderWorkbatch = orderWorkbatchMapper.selectById(workbatchOrdlink.getWbId());
        workbatchOrdlink.setWbNo(orderWorkbatch.getBatchNo());//????????????
        /*?????????????????????????????????*/
        List<ProdProcelinkVO> prodProcelinkVOList = prodProcelinkMapper.select(ptId);
        if (!prodProcelinkVOList.isEmpty()) {
            for (int i = 0; i < prodProcelinkVOList.size(); i++) {
                ProdProcelinkVO prodProcelinkVO = prodProcelinkVOList.get(i);
                if (prId.equals(prodProcelinkVO.getId())) {
                    if (i > 0) {
                        ProdProcelinkVO procelinkVO = prodProcelinkVOList.get(i - 1);
                        String upPorcess = procelinkVO.getPrName();
//                        Integer sortNum = procelinkVO.getSortNum();
                        workbatchOrdlink.setUpPorcess(upPorcess);//???????????????
//                        workbatchOrdlink.setUpprocessSort(sortNum);//???????????????
                    }
                    if (i < prodProcelinkVOList.size() - 1) {
                        String downPorcess = prodProcelinkVOList.get(i + 1).getPrName();
                        workbatchOrdlink.setDownPorcess(downPorcess);//???????????????
                    }
                }
            }
        }
        /*????????????*/
        workbatchOrdlink.setSort(processWorkinfo.getSort());
        /*????????????????????????*/
        ProdPartsinfo prodPartsinfo = prodPartsinfoMapper.selectById(workbatchOrdlink.getPtId());
        /*????????????*/
        workbatchOrdlink.setPtNo(prodPartsinfo.getPtNo());
        /*????????????*/
        workbatchOrdlink.setPartName(prodPartsinfo.getPtName());
        /*????????????*/
        Integer planNum = workbatchOrdlink.getPlanNumber() + workbatchOrdlink.getExtraNum();
        workbatchOrdlink.setPlanNum(planNum);
        /*????????????*/
        workbatchOrdlink.setIncompleteNum(planNum);
        Date startTime = workbatchOrdlink.getStartTime();

      /*  Integer sdSort = ordlinkMapper.getSdSort();
        if (sdSort == null) {
            sdSort = 1;
        } else {
            sdSort++;
        }*/
//        workbatchOrdlink.setSdSort(String.valueOf(sdSort));
        if (workbatchOrdlink.getCloseTime() != null) {  //??????????????????????????????????????????????????????????????????
            closeTime = workbatchOrdlink.getCloseTime();
            Long diff = closeTime.getTime() - startTime.getTime();  //????????????????????????????????????
            Long minutes = diff / (1000 * 60);  //???
            /*????????????*/
            workbatchOrdlink.setPlanTime(minutes.intValue());
        } else {//????????????????????????,??????????????????????????????????????????????????????,??????????????????
            Integer speed = 3500;//??????????????????
            if (workbatchOrdlink.getMaId() != null) {
                //MachineClassify machineClassify = machineClassifyMapper.selectspeedByMaId(workbatchOrdlink.getMaId());
                ProcessMachlink processMachlink = processMachlinkMapper.selectOne(new QueryWrapper<ProcessMachlink>()
                        .eq("pr_id", workbatchOrdlink.getPrId()).eq("ma_id", workbatchOrdlink.getMaId()));
                speed = processMachlink.getSpeed();//??????:???/h
            }

            long planTime = (workbatchOrdlink.getPlanNum() * 60) / speed + workbatchOrdoeeVO.getProducePreTime();//??????
            /*??????????????????*/
            workbatchOrdlink.setPlanTime(Long.valueOf(planTime).intValue());
            long planHS = planTime * 1000 * 60;//??????
            long closeho = startTime.getTime() + planHS;//??????????????????+????????????
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s = sdf.format(closeho);
            try {
                closeTime = sdf.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        workbatchOrdlink.setCloseTime(closeTime);
        workbatchOrdlink.setCreateAt(new Date());
        workbatchOrdlink.setRunStatus(0);
        workbatchOrdlink.setStatus("0");
        /*????????????*/
        WorkbatchShift workbatchShift = new WorkbatchShift();
        List<WorkbatchShiftset> workbatchShiftsets = workbatchShiftsetMapper.selectList(new QueryWrapper<>());
        for (WorkbatchShiftset workbatchShiftset : workbatchShiftsets) {
            if (workbatchOrdlink.getCkName().equals(workbatchShiftset.getCkName())) {//????????????????????????????????????????????????????????????????????????????????????
                workbatchShift.setWsId(workbatchShiftset.getId());
                workbatchShift.setStayTime(workbatchShiftset.getStayTime());//??????????????????
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //??????????????????????????????
                Date shiftsetStartTime = workbatchShiftset.getStartTime();
                //??????????????????????????????
                Date shiftsetEndTime = workbatchShiftset.getEndTime();
                //??????????????????
//                Date ordlinkDate = workbatchOrdlink.getStartTime();
                String sdDate = workbatchOrdlink.getSdDate();
                String ckStartTime = sdf.format(shiftsetStartTime);
                String ckEndTime = sdf.format(shiftsetEndTime);
                //????????????????????????
                ckStartTime = sdDate + ckStartTime;
                ckEndTime = shiftsetStartTime + ckEndTime;
                try {
                    Date ckStartDate = sdf.parse(ckStartTime);
                    Date ckEndDate = sdf.parse(ckEndTime);
                    if (ckStartDate.getTime() > ckEndDate.getTime()) {
                        long h = ckEndDate.getTime() + 24 * 60 * 60 * 1000;//?????????
                        ckEndDate = new Date(h);
                    }
                    //??????????????????????????????
                    workbatchShift.setStartTime(ckStartDate);
                    //??????????????????????????????
                    workbatchShift.setEndTime(ckEndDate);
                    break;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        /*????????????*/
        workbatchShift.setCkName(workbatchOrdlink.getCkName());
        workbatchShift.setCreateAt(new Date());
        /*????????????????????????*/
        int Shift = workbatchShiftMapper.insert(workbatchShift);

        /*??????id*/
        workbatchOrdlink.setWsId(workbatchShift.getId());
        /*??????????????????*/
        int ordlink = ordlinkMapper.insert(workbatchOrdlink);
        /*??????oee??????*/
        WorkbatchOrdoee workbatchOrdoee = new WorkbatchOrdoee();

        Integer speed = workbatchOrdoeeVO.getSpeed();
        if (speed == null || speed == 0) {
            if (workbatchOrdlink.getMaId() != null) {
                ProcessMachlink processMachlink = processMachlinkMapper.selectOne(new QueryWrapper<ProcessMachlink>()
                        .eq("pr_id", workbatchOrdlink.getPrId()).eq("ma_id", workbatchOrdlink.getMaId()));
                speed = processMachlink.getSpeed();
                if (speed == null || speed == 0) {
                    speed = 1;
                }
            } else {
                speed = 1;
            }
        }
        workbatchOrdoee.setSpeed(speed);//????????????
        workbatchOrdoee.setWkId(workbatchOrdlink.getId());//??????id
        workbatchOrdoee.setBeforePtid(workbatchOrdlink.getPtId());
        workbatchOrdoee.setBeforePtno(prodPartsinfo.getPtNo());//????????????
        workbatchOrdoee.setBeforePtname(prodPartsinfo.getPtName());//????????????
        workbatchOrdoee.setDifficultNum(workbatchOrdoeeVO.getDifficultNum());//????????????
//        workbatchOrdoee.setMaintainNum(workbatchOrdoeeVO.getMaintainNum());//????????????
//        workbatchOrdoee.setMaintain(workbatchOrdoeeVO.getMaintain());//?????????????????????1??????2??????3??????4??????5?????????6??????
//        workbatchOrdoee.setMaintainStay(workbatchOrdoeeVO.getMaintainStay());//?????????????????????
//        workbatchOrdoee.setMealNum(workbatchOrdoeeVO.getMealNum());//????????????
//        workbatchOrdoee.setMealStay(workbatchOrdoeeVO.getMealStay());//????????????
        workbatchOrdoee.setMouldStay(workbatchOrdoeeVO.getMouldStay());//??????????????????????????????????????????
        workbatchOrdoee.setProducePreTime(workbatchOrdoeeVO.getProducePreTime());//??????????????????
        workbatchOrdoee.setMouldNum(workbatchOrdoeeVO.getMouldNum());//????????????
//        workbatchOrdoee.setQualityNum(workbatchOrdoeeVO.getQualityNum());//????????????
        /*????????????oee??????*/
        int Ordoee = workbatchOrdoeeMapper.insert(workbatchOrdoee);
        /*Integer oeeId = workbatchOrdoee.getId();
        List<WorkbatchordoeeMaintain> workbatchordoeeMaintains = workbatchOrdoeeVO.getWorkbatchordoeeMaintainList();
        List<WorkbatchordoeeMeal> workbatchordoeeMeals = workbatchOrdoeeVO.getWorkbatchordoeeMealList();
        List<WorkbatchordoeeMould> workbatchordoeeMoulds = workbatchOrdoeeVO.getWorkbatchordoeeMouldList();
        if (!workbatchordoeeMaintains.isEmpty()) {
            for (WorkbatchordoeeMaintain workbatchordoeeMaintain : workbatchordoeeMaintains) {
                workbatchordoeeMaintain.setWkOeeId(oeeId);
                *//*??????????????????OEE??????????????????*//*
                workbatchordoeeMaintainMapper.insert(workbatchordoeeMaintain);

            }
        }
        if (!workbatchordoeeMeals.isEmpty()) {
            for (WorkbatchordoeeMeal workbatchordoeeMeal : workbatchordoeeMeals) {
                workbatchordoeeMeal.setWkOeeId(oeeId);
                *//*??????????????????OEE??????????????????*//*
                workbatchordoeeMealMapper.insert(workbatchordoeeMeal);
            }
        }
        if (!workbatchordoeeMeals.isEmpty()) {
            for (WorkbatchordoeeMould workbatchordoeeMould : workbatchordoeeMoulds) {
                workbatchordoeeMould.setWkOeeId(oeeId);
                *//*????????????OEE??????????????????*//*
                workbatchordoeeMouldMapper.insert(workbatchordoeeMould);
            }
        }*/
        return workbatchOrdlink.getId();
    }

    /**
     * ??????????????????????????????????????????????????????
     */
    @Override
    public List<WorkbatchOeeShiftinVO> getWorkbatchOrdlinkVOList(String ckName) {
        List<WorkbatchOeeShiftinVO> WorkbatchOrdlinkVOs = ordlinkMapper.getWorkbatchOrdlinkVOList(ckName);
        for (WorkbatchOeeShiftinVO WorkbatchOeeShiftinVO : WorkbatchOrdlinkVOs) {
            if (WorkbatchOeeShiftinVO != null) {

                MachineMainfo machineMainfo = machineMainfoMapper.selectById(WorkbatchOeeShiftinVO.getMaId());
                if (machineMainfo != null) {
                    WorkbatchOeeShiftinVO.setMaName(machineMainfo.getName());
                }
            }
        }
        return WorkbatchOrdlinkVOs;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updataOrdOEEOrdlink(WorkbatchOrdoeeVo workbatchOrdoeeVO) {
        Boolean sta = false;
        WorkbatchOrdlinkVO workbatchOrdlink = workbatchOrdoeeVO.getWorkbatchOrdlink();
        /*??????????????????*/
        workbatchOrdlink.setUpdateAt(new Date());
//        Integer PlanNumber = workbatchOrdlink.getPlanNum() - workbatchOrdlink.getExtraNum();
//        workbatchOrdlink.setPlanNumber(PlanNumber);//??????????????????
        Integer planNum = workbatchOrdlink.getPlanNumber() + workbatchOrdlink.getExtraNum();
        workbatchOrdlink.setPlanNum(planNum);
        workbatchOrdlink.setIncompleteNum(planNum);
        workbatchOrdlink.setStatus("0");
        int updateOrdlink = ordlinkMapper.updateById(workbatchOrdlink);
        /*????????????oee??????*/
        int updateOee = workbatchOrdoeeMapper.updateById(workbatchOrdoeeVO);

        List<WorkbatchordoeeMaintain> workbatchordoeeMaintains = workbatchOrdoeeVO.getWorkbatchordoeeMaintainList();
        List<WorkbatchordoeeMeal> workbatchordoeeMeals = workbatchOrdoeeVO.getWorkbatchordoeeMealList();
        List<WorkbatchordoeeMould> workbatchordoeeMoulds = workbatchOrdoeeVO.getWorkbatchordoeeMouldList();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("wk_oee_id", workbatchOrdoeeVO.getId());
        /*????????????oee??????????????????*/
        workbatchordoeeMaintainMapper.delete(queryWrapper);
        /*????????????oee??????????????????*/
        workbatchordoeeMealMapper.delete(queryWrapper);
        /*????????????oee????????????*/
        workbatchordoeeMouldMapper.delete(queryWrapper);
        for (WorkbatchordoeeMaintain workbatchordoeeMaintain : workbatchordoeeMaintains) {
            workbatchordoeeMaintain.setWkOeeId(workbatchOrdoeeVO.getId());
            /*?????????????????????*/
            workbatchordoeeMaintainMapper.insert(workbatchordoeeMaintain);

        }

        for (WorkbatchordoeeMeal workbatchordoeeMeal : workbatchordoeeMeals) {
               /* if(workbatchordoeeMeal.getId() == null){
                    workbatchordoeeMeal.setWkOeeId(workbatchOrdoeeVO.getId());
                    workbatchordoeeMealMapper.insert(workbatchordoeeMeal);
                    continue;
                }
                *//*??????????????????OEE??????????????????*//*
                workbatchordoeeMealMapper.updateById(workbatchordoeeMeal);*/
            workbatchordoeeMeal.setWkOeeId(workbatchOrdoeeVO.getId());
            workbatchordoeeMealMapper.insert(workbatchordoeeMeal);
        }

        for (WorkbatchordoeeMould workbatchordoeeMould : workbatchordoeeMoulds) {
               /* if(workbatchordoeeMould.getId() == null){
                    workbatchordoeeMould.setWkOeeId(workbatchOrdoeeVO.getId());
                    workbatchordoeeMouldMapper.insert(workbatchordoeeMould);
                    continue;
                }
                *//*????????????OEE??????????????????*//*
                workbatchordoeeMouldMapper.updateById(workbatchordoeeMould);*/
            workbatchordoeeMould.setWkOeeId(workbatchOrdoeeVO.getId());
            workbatchordoeeMouldMapper.insert(workbatchordoeeMould);
        }

        WorkbatchOrdlinkShiftVO ordlink = ordlinkMapper.getOrdshiftById(workbatchOrdlink.getId());
        //????????????????????????
        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(ordlink.getWsId());
//        WorkbatchShift workbatchShift = workbatchShiftsetMapper.selectByMaid(ordlink.getWsId(),ordlink.getMaId());
        if (!workbatchShift.getCkName().equals(workbatchOrdlink.getCkName())) {//???????????????????????????????????????????????????????????????,?????????????????????
            workbatchShift.setCkName(workbatchOrdlink.getCkName());
            /*????????????????????????*/
            List<WorkbatchShiftset> workbatchShiftsets = workbatchShiftsetMapper.selectList(new QueryWrapper<>());
            for (WorkbatchShiftset workbatchShiftset : workbatchShiftsets) {
                if (workbatchOrdlink.getCkName().equals(workbatchShiftset.getCkName())) {//??????????????????????????????,??????????????????????????????????????????
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //??????????????????
                    Date ckStartTime = workbatchShiftset.getStartTime();
                    //??????????????????
                    Date ckEndTime = workbatchShiftset.getEndTime();
                    //????????????
                    Date ordlinkTime = ordlink.getStartTime();
                    String ckStart = sdf.format(ckStartTime);
                    String ckEnd = sdf.format(ckEndTime);
                    String ordlinkTimeD = sdf.format(ordlinkTime).substring(0, 10);
                    if (ckStart.length() > 10) {
                        ckStart = ckStart.substring(10);
                    }
                    if (ckEnd.length() > 10) {
                        ckEnd = ckEnd.substring(10);
                    }
                    ckStart = ordlinkTimeD + ckStart;
                    ckEnd = ordlinkTimeD + ckEnd;
                    try {
                        Date ckStartDate = sdf.parse(ckStart);
                        Date ckEndDate = sdf.parse(ckEnd);
                        //??????????????????????????????
                        workbatchShift.setStartTime(ckStartDate);
                        //????????????????????????
                        workbatchShift.setEndTime(ckEndDate);
                        break;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            //????????????????????????
            workbatchShiftMapper.updateById(workbatchShift);
        }

        if (updateOrdlink != 0 || updateOee != 0) {
            sta = true;
        }
        return sta;
    }

    @Override
    public WorkbatchOrdlinkVO getWorkBatchInfoBySdId(Integer sdId) {
        return workbatchOrdlinkMapper.getWorkBatchInfoBySdId(sdId);
    }

    @Override
    public WorkbatchOrdoeeVo getWorkbatchOEEById(Integer id) {
        Map<String, Object> map = new HashMap<>();
        map.put("wk_id", id);
        WorkbatchOrdoeeVo batchOrdoee =
                WorkbatchOrdoeeWrapper.build().entityVO(
                        workbatchOrdoeeMapper.selectByMap(map).get(0));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("wk_oee_id", batchOrdoee.getId());
        /*????????????oee??????????????????*/
        List workbatchordoeeMaintainList = workbatchordoeeMaintainMapper.selectList(queryWrapper);
        /*????????????oee??????????????????*/
        List workbatchordoeeMealList = workbatchordoeeMealMapper.selectList(queryWrapper);
        /*????????????oee????????????*/
        List workbatchordoeeMouldList = workbatchordoeeMouldMapper.selectList(queryWrapper);
        batchOrdoee.setWorkbatchordoeeMaintainList(workbatchordoeeMaintainList);
        batchOrdoee.setWorkbatchordoeeMouldList(workbatchordoeeMouldList);
        batchOrdoee.setWorkbatchordoeeMealList(workbatchordoeeMealList);
        return batchOrdoee;
    }

    @Override
    public IPage<WorkbatchOrdlinkVO> WorkbatchOrdlinkoeeExcel(WorkbatchOrdlinkVO
                                                                      workbatchOrdlinkVO, IPage<WorkbatchOrdlinkVO> page) {

        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList = workbatchOrdlinkMapper.workbatchOrdlinkoeeExcel(page, workbatchOrdlinkVO);
        return page.setRecords(workbatchOrdlinkVOList);
    }

    @Override
    public void WorkbatchOrdlinkExcelExport(List<Integer> ids) {
//        String path = "/excelExport/images/";
        /*String filePath = "C:/Users/Administrator/Desktop/excelExport/demo/";
        String fileXLS = filePath + "?????????.xls";*/
       /* File imagesPath = new File(path);
        if (!imagesPath.exists()) {//??????????????????????????????
            imagesPath.mkdirs();//???????????????????????????
        }*/
        /*File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }*/
        // ????????????????????????
        //
        String templatePath = "model/WorkbatchOrdlinkModel.xls";
        //      String templatePath =this.getClass().getClassLoader().getResource("model/WorkbatchOrdlinkModel.xls").getPath();
        //       String templatePath = ClassUtils.getDefaultClassLoader().getResource("model/WorkbatchOrdlinkModel.xls").getPath();
//        OutputStream os = null;
        InputStream imageInputStream = null;//?????????
        BufferedOutputStream bos = null;//???????????????
        List<Integer> indexList = new ArrayList<>();
        List<WorkbatchOrdlinkShiftVO> workbatchOrdlinkList = workbatchOrdlinkMapper.selectBatchIds(ids);
        List<WorkbatchOrdlinkExcelVO> workbatchOrdlinkExcelVOList = new ArrayList<>();
        WorkbatchOrdlinkWrapper workbatchOrdlinkWrapper = new WorkbatchOrdlinkWrapper();
        for (int i = 0; i < workbatchOrdlinkList.size(); i++) {//????????????
            int count = 0;
            if (!indexList.isEmpty()) {
                Boolean whether = false;
                for (Integer index : indexList) {
                    whether = index.equals(i);
                    if (whether) {
                        break;
                    }
                }
                if (whether) {//????????????
                    continue;
                }
            }
            WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkList.get(i);
            WorkbatchOrdlinkExcelVO workbatchOrdlinkExcelVO = new WorkbatchOrdlinkExcelVO();
            Integer prId = workbatchOrdlink.getPrId();
            workbatchOrdlinkExcelVO.setPrId(prId);//??????id
            workbatchOrdlinkExcelVO.setPrName(workbatchOrdlink.getPrName());//????????????
            workbatchOrdlinkExcelVO.setSdDate(workbatchOrdlink.getSdDate());//????????????
            /*????????????*/
            BaseDeptinfo baseDeptinfo = baseDeptinfoMapper.selectById(workbatchOrdlink.getDpId());
            workbatchOrdlinkExcelVO.setDpName(baseDeptinfo.getDpName());//????????????
            /*????????????*/
            List<MachineMainfoVO> machineMainfoVOList = processMachlinkMapper.machineListByPrIdDeptId(prId, workbatchOrdlink.getDpId());
            workbatchOrdlinkExcelVO.setMachineMainfoVOList(machineMainfoVOList);//????????????

            List<WorkbatchOrdlinkVO> workbatchOrdlinkVOS = new ArrayList<>();//????????????
            for (int j = 0; j < workbatchOrdlinkList.size(); j++) {
                WorkbatchOrdlinkShiftVO ordlink = workbatchOrdlinkList.get(j);
                if (workbatchOrdlink.getPrId().equals(ordlink.getPrId())
                        && workbatchOrdlink.getSdDate().equals(ordlink.getSdDate())
                        && workbatchOrdlink.getDpId().equals(ordlink.getDpId())) {
                    count++;
                    WorkbatchOrdlinkVO workbatchOrdlinkVO = workbatchOrdlinkWrapper.entityVO(ordlink);
                    /*????????????*/
//                    WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(ordlink.getWsId());
                    WorkbatchShiftset workbatchShift = workbatchShiftsetMapper.selectByMaid(ordlink.getWsId(), ordlink.getMaId());
                    if (workbatchShift != null) {//????????????????????????,
                        //WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(workbatchShift.getWsId());
                        if (count != 1) {
                            workbatchOrdlinkVO.setMealStay("X");//????????????
                        } else {
                            workbatchOrdlinkVO.setCkName(workbatchShift.getCkName());//????????????
                            workbatchOrdlinkVO.setMealStay(workbatchShift.getMealStay() + "/???");//????????????
                        }
                        if (j != 0) {
                            Integer wsId = workbatchOrdlinkList.get(j - 1).getWsId();
                            //WorkbatchShift Shift = workbatchShiftMapper.selectById(wsId);//????????????????????????
                            WorkbatchShiftset Shift = workbatchShiftsetMapper.selectByMaid(wsId, workbatchOrdlinkList.get(j - 1).getMaId());
                            if (!Shift.getCkName().equals(workbatchShift.getCkName())) {
                                workbatchOrdlinkVO.setMealStay(workbatchShift.getMealStay() + "/???");//????????????
                                workbatchOrdlinkVO.setCkName(workbatchShift.getCkName());//????????????
                            }
                        }
                    }

//                    String pathlast = ordlink.getId() + ".png";
//                    barCodeUtil.getBarCode(workbatchOrdlinkVO.getId().toString(), path + pathlast);//???????????????
                    byte[] result = barCodeUtil.generate(workbatchOrdlinkVO.getId().toString());

                    // ??????????????????????????????
                    try {
                        //    imageInputStream = new FileInputStream(path + pathlast);
                        // ??????????????????????????????byte??????
                        byte[] imageBytes = result;
                        workbatchOrdlinkVO.setImg(imageBytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (imageInputStream != null) {
                                imageInputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    workbatchOrdlinkVOS.add(workbatchOrdlinkVO);
                    if (count != 1) {
                        indexList.add(j);
                    }
                }
            }
            workbatchOrdlinkExcelVO.setWorkbatchOrdlinkVOList(workbatchOrdlinkVOS);
            workbatchOrdlinkExcelVOList.add(workbatchOrdlinkExcelVO);
        }
        List<Page> page = individual(workbatchOrdlinkExcelVOList); // ???????????????????????????
        Map<String, Object> model = new HashMap<String, Object>();

        model.put("pages", page);
        model.put("sheetNames", getSheetName(page));
        model.put("slName", getSheetName(page));
        try {
//            os = new FileOutputStream(fileXLS);
            bos = ExportlUtil.getBufferedOutputStream("?????????.xls", response);//??????????????????
//            JxlsUtils.exportExcel(templatePath, os, model);//???????????????;
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//???????????????
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
               /* if(os != null){
                    os.close();
                }*/
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public PlanProduceModelVO planProduceDetail(WorkbatchOrdlinkVO workbatchOrdlink) {
        return workbatchOrdlinkMapper.planProduceDetail(workbatchOrdlink);
    }


    /**
     * ???????????????????????????????????????????????????List
     */
    public static List<Page> individual(List<WorkbatchOrdlinkExcelVO> list) {
        List<Page> pages = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Page p = new Page();
            p.setOnlyOne(list.get(i));
//            p.setSheetName(list.get(i).getDpName()+"-"+list.get(i).getSfName()+"-"+(i+1));
            //??????sheet??????
            p.setSheetName(list.get(i).getSdDate() + list.get(i).getPrName());
            pages.add(p);
        }

        return pages;
    }

    /**
     * Excel ?????????????????????????????????
     * ????????????????????????????????????????????????????????????????????????????????????
     */
    public static ArrayList<String> getSheetName(List<Page> page) {
        ArrayList<String> al = new ArrayList<>();
        for (int i = 0; i < page.size(); i++) {
            al.add(page.get(i).getSheetName());
        }
        return al;
    }


    /**
     * ??????????????????id ??????????????????????????????id
     */
    private SuperviseExecute getExecuteOrder(Integer maId) {
        return executeMapper.getExecuteOrder(maId);
    }

    private Integer getExecuteOrderByother(Integer maId) {
        return executeMapper.getExecuteOrderByother(maId);
    }


    @Override
    public String saveOrdlinkYS(List<WorkbatchOrdlinkYS> workbatchOrdlinkYSList) {
        Iterator<WorkbatchOrdlinkYS> iterator = workbatchOrdlinkYSList.iterator();
        WorkbatchOrdlink workbatchOrdlink;//????????????
        WorkbatchOrdoee workbatchOrdoee;//??????oee??????
        WorkbatchShift workbatchShift;//??????????????????
        Date date = new Date();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ckTime = simpleDateFormat2.format(date);
        String sdDate = simpleDateFormat1.format(date);
        /*-----------???????????????---------*/
        List<WorkbatchShiftset> workbatchShiftsetList = workbatchShiftsetMapper.selectList(new QueryWrapper<WorkbatchShiftset>()
                .le("start_date", sdDate).ge("end_date", sdDate));
        /*??????????????????*/
//        List<ProcessWorkinfo> processWorkinfoList = processWorkinfoMapper.selectList(new QueryWrapper<>());
        /*????????????????????????*/
        List<ProdClassify> prodClassifyList = prodClassifyMapper.selectList(new QueryWrapper<>());
        /*??????????????????*/
        //List<MachineMainfo> machineMainfoList = machineMainfoMapper.selectList(new QueryWrapper<>());
        if (!workbatchOrdlinkYSList.isEmpty()) {
            while (iterator.hasNext()) {//????????????????????????
                WorkbatchOrdlinkYS workbatchOrdlinkYS = iterator.next();
                /*????????????????????????*/
                String specification = workbatchOrdlinkYS.getSpecification();//????????????
                if (StringUtil.isEmpty(specification)) {
                    return "????????????????????????";
                }
                Integer maId = workbatchOrdlinkYS.getMaId();//??????id
                if (maId == null || maId == 0) {
                    return "??????id???????????????0";
                }

                Map ruleMap = new HashMap<>();
                ruleMap.put("ma_id", maId);
                Integer material = workbatchOrdlinkYS.getMaterial();
                Integer size = workbatchOrdlinkYS.getSize();
                Integer planNum = workbatchOrdlinkYS.getPlanNum();//?????????(?????????)
                /*??????????????????*/
                RuleProdoee ruleProdoee = ruleProdoeeMapper.selectRuleProdoee(maId, material, planNum, size);
               /* if (!ruleProdoeeList.isEmpty()) {
                    for (RuleProdoee prodoee : ruleProdoeeList) {
                        if (specification.equals(prodoee.getPdSize())) {
                            ruleProdoee = prodoee;
                            break;
                        }
                    }
                }*/
                if (ruleProdoee == null) {
                    return "??????????????????????????????";
                }
                Integer pcId = ruleProdoee.getPcId();//????????????id
                if (pcId == null || pcId == 0) {
                    return "???????????????????????????id?????????";
                }
                ProdClassify prodClassify = null;
                for (ProdClassify classify : prodClassifyList) {
                    if (classify.getId().equals(pcId)) {
                        prodClassify = classify;
                    }
                }
                if (prodClassify == null) {
                    return "?????????????????????";
                }
//               Integer prId = ruleProdoee.getPrId();//??????id

                /*ProcessWorkinfo processWorkinfo = null;
                for (ProcessWorkinfo processWorkin : processWorkinfoList) {
                    if (processWorkin.getId().equals(prId)) {
                        processWorkinfo = processWorkin;
                    }
                }
                if (processWorkinfo == null) {
                    return "???????????????";
                }*/
                Integer wbId = workbatchOrdlinkYS.getWbId();//??????id
                Integer ptId = workbatchOrdlinkYS.getPtId();//??????id
                String ptName = workbatchOrdlinkYS.getPtName();//????????????
                Integer planTime = workbatchOrdlinkYS.getPlanTime();//????????????(???)
//                Integer planNumber = workbatchOrdlinkYS.getPlanNumber();//?????????

//                Integer extraNum = workbatchOrdlinkYS.getExtraNum();//?????????
//                Integer completeNum = workbatchOrdlinkYS.getCompleteNum();//????????????
                Date closeTime = workbatchOrdlinkYS.getCloseTime();//????????????
//                String pdType = workbatchOrdlinkYS.getPdType();//????????????
                String pdNo = workbatchOrdlinkYS.getPdNo();//????????????
                String pdName = workbatchOrdlinkYS.getPdName();//????????????
                String odNo = workbatchOrdlinkYS.getOdNo();//????????????

                Integer dpId = workbatchOrdlinkYS.getDpId();//??????id
                if (dpId == null || dpId == 0) {
                    return "??????id???????????????0";
                }
                if (wbId == null || wbId == 0) {
                    return "??????id???????????????0";
                }
                /*if(StringUtil.isEmpty(ptNo)){
                    return "???????????????????????????0";
                }*/
                if (ptId == null || ptId == 0) {
                    return "??????id???????????????0";
                }
                if (StringUtil.isEmpty(ptName)) {
                    return "????????????????????????";
                }
                if (planTime == null) {
                    return "????????????????????????";
                }

                /*if(StringUtil.isEmpty(closeTime)){
                    return "????????????????????????";
                }*/
                if (StringUtil.isEmpty(pdNo)) {
                    return "????????????????????????";
                }
                if (StringUtil.isEmpty(pdName)) {
                    return "????????????????????????";
                }
                if (StringUtil.isEmpty(odNo)) {
                    return "????????????????????????";
                }

            }

        } else {
            return "????????????????????????";
        }
        /*??????????????????????????????*/
        for (WorkbatchOrdlinkYS workbatchOrdlinkYS : workbatchOrdlinkYSList) {
            /*RuleProdoee ruleProdoee = null;
            if (!ruleProdoeeList.isEmpty()) {
                for (RuleProdoee prodoee : ruleProdoeeList) {
                    if (workbatchOrdlinkYS.getSpecification().equals(prodoee.getPdSize())) {
                        ruleProdoee = prodoee;
                        break;
                    }
                }
            }*/
            Integer planNum = workbatchOrdlinkYS.getPlanNum();//?????????(?????????)
            Integer material = workbatchOrdlinkYS.getMaterial();
            Integer maId = workbatchOrdlinkYS.getMaId();//??????id
            Integer size = workbatchOrdlinkYS.getSize();
            /*??????????????????*/
            RuleProdoee ruleProdoee = ruleProdoeeMapper.selectRuleProdoee(maId, material, planNum, size);
            ProdClassify prodClassify = null;
            for (ProdClassify classify : prodClassifyList) {
                if (classify.getId().equals(ruleProdoee.getPcId())) {
                    prodClassify = classify;
                }
            }
            Integer prId = ruleProdoee.getPrId();
           /* ProcessWorkinfo processWorkinfo = null;
            for (ProcessWorkinfo processWorkin : processWorkinfoList) {
                if (processWorkin.getId().equals(prId)) {
                    processWorkinfo = processWorkin;
                }
            }*/
            workbatchOrdlink = new WorkbatchOrdlink();
            workbatchOrdoee = new WorkbatchOrdoee();
            workbatchShift = new WorkbatchShift();
            Integer wbId = workbatchOrdlinkYS.getWbId();//??????id
            String remarks = workbatchOrdlinkYS.getRemarks();//??????
            String ptNo = workbatchOrdlinkYS.getPtNo();//????????????
            Integer ptId = workbatchOrdlinkYS.getPtId();//??????id
            String ptName = workbatchOrdlinkYS.getPtName();//????????????
            Integer planTime = workbatchOrdlinkYS.getPlanTime();//????????????(???)
            Integer planNumber = workbatchOrdlinkYS.getPlanNumber();//?????????
            Integer extraNum = workbatchOrdlinkYS.getExtraNum();//?????????
            Integer completeNum = workbatchOrdlinkYS.getCompleteNum();//????????????
            Date closeTime = workbatchOrdlinkYS.getCloseTime();//????????????
//          String pdType = workbatchOrdlinkYS.getPdType();//????????????
            String pdNo = workbatchOrdlinkYS.getPdNo();//????????????
            String pdName = workbatchOrdlinkYS.getPdName();//????????????
            String pdImageurl = workbatchOrdlinkYS.getPdImageurl();//??????????????????
            String odNo = workbatchOrdlinkYS.getOdNo();//????????????
            Integer dpId = workbatchOrdlinkYS.getDpId();//??????id

            if (planNumber == null) {
                planNumber = 0;
            }
            if (planNum == null || planNum == 0) {
                planNum = 0;
            }

            if (extraNum == null) {
                extraNum = 0;
            }
            if (completeNum == null) {
                completeNum = 0;
            }


            Iterator<WorkbatchShiftset> shiftsetIterator = workbatchShiftsetList.iterator();
            while (shiftsetIterator.hasNext()) {
                WorkbatchShiftset workbatchShiftset = shiftsetIterator.next();
                Date startTime = workbatchShiftset.getStartTime();
                Date endTime = workbatchShiftset.getEndTime();
                String startTimeFormat = simpleDateFormat2.format(startTime);
                String endTimeFormat = simpleDateFormat2.format(endTime);
                String start = sdDate + " " + startTimeFormat;
                String end = sdDate + " " + endTimeFormat;
                Date startParse = null;
                Date endParse = null;
                try {
                    startParse = simpleDateFormat3.parse(start);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    endParse = simpleDateFormat3.parse(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date ckTimeDate = null;
                try {
                    ckTimeDate = simpleDateFormat2.parse(ckTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long ckTimeDateTime = ckTimeDate.getTime();
                if (startTime.getTime() > endTime.getTime()) {
                    if (ckTimeDateTime >= startTime.getTime() || ckTimeDateTime <= endTime.getTime()) {
                        workbatchShift.setWsId(workbatchShiftset.getId());
                        workbatchShift.setStartTime(startParse);
                        workbatchShift.setEndTime(endParse);
                        workbatchShift.setCkName(workbatchShiftset.getCkName());
                        workbatchShift.setStayTime(workbatchShiftset.getStayTime());
                        workbatchShift.setWsId(workbatchShiftset.getId());
                        break;
                    }
                } else {
                    if (ckTimeDateTime >= startTime.getTime() && ckTimeDateTime <= endTime.getTime()) {
                        workbatchShift.setWsId(workbatchShiftset.getId());
                        workbatchShift.setStartTime(startParse);
                        workbatchShift.setEndTime(endParse);
                        workbatchShift.setStayTime(workbatchShiftset.getStayTime());
                        workbatchShift.setCkName(workbatchShiftset.getCkName());
                        workbatchShift.setWsId(workbatchShiftset.getId());
                        break;
                    }
                }
                if (workbatchShift == null) {
                    WorkbatchShiftset shiftset = workbatchShiftsetList.get(0);
                    String format = simpleDateFormat2.format(shiftset.getStartTime());
                    String imeFormat = simpleDateFormat2.format(shiftset.getEndTime());
                    String start1 = sdDate + " " + format;
                    String end2 = sdDate + " " + imeFormat;
                    Date startParse1 = null;
                    Date endParse2 = null;
                    try {
                        startParse1 = simpleDateFormat3.parse(start1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        endParse2 = simpleDateFormat3.parse(end2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    workbatchShift.setWsId(shiftset.getId());
                    workbatchShift.setStartTime(startParse1);
                    workbatchShift.setEndTime(endParse2);
                    workbatchShift.setStayTime(shiftset.getStayTime());
                    workbatchShift.setCkName(shiftset.getCkName());
                    workbatchShift.setWsId(shiftset.getId());
                }
            }
            workbatchShift.setCreateAt(date);
            int workbatchShiftinsert = workbatchShiftMapper.insert(workbatchShift);
            /*---------????????????-------------*/
            workbatchOrdlink.setPlanNum(planNum);//?????????
            workbatchOrdlink.setPlanTime(planTime);//????????????
            workbatchOrdlink.setPlanNumber(planNumber);//?????????
            workbatchOrdlink.setCompleteNum(completeNum);//?????????
            workbatchOrdlink.setIncompleteNum(planNum - completeNum);//????????????
            workbatchOrdlink.setCloseTime(closeTime);//????????????
            workbatchOrdlink.setExtraNum(extraNum);//?????????
            workbatchOrdlink.setStartTime(date);//??????????????????
            workbatchOrdlink.setSdDate(sdDate);//????????????
            workbatchOrdlink.setMaId(maId);//??????id
            workbatchOrdlink.setOdNo(odNo);//????????????
            workbatchOrdlink.setPdName(pdName);//????????????
            workbatchOrdlink.setPdImageurl(pdImageurl);//??????????????????
            workbatchOrdlink.setPdCode(pdNo);//????????????
            workbatchOrdlink.setWbId(wbId);//??????id
            workbatchOrdlink.setPtId(ptId);//??????id
            workbatchOrdlink.setPtNo(ptNo);//????????????
            workbatchOrdlink.setPartName(ptName);//????????????
            workbatchOrdlink.setRemarks(remarks);//??????
            workbatchOrdlink.setDpId(dpId);//??????id
            workbatchOrdlink.setStatus("0");
            workbatchOrdlink.setRunStatus(0);
            workbatchOrdlink.setPdType(prodClassify.getClName());//????????????
            workbatchOrdlink.setPrName(workbatchOrdlinkYS.getPrName());
            workbatchOrdlink.setPrId(prId);
            int workbatchOrdlinkInsert = ordlinkMapper.insert(workbatchOrdlink);

            /*----------oee--------------*/
            Integer speed = ruleProdoee.getSpeed() == null ? 1 : ruleProdoee.getSpeed();
            workbatchOrdoee.setSpeed(speed);//??????
            workbatchOrdoee.setMouldStay(ruleProdoee.getPrepareTime());
            workbatchOrdoee.setMouldNum(1);
//            workbatchOrdoee.setQualityNum(0);
            workbatchOrdoee.setProducePreTime(0);
            workbatchOrdoee.setBeforePtid(ptId);
            workbatchOrdoee.setBeforePtname(ptName);
            workbatchOrdoee.setBeforePtno(ptNo);
            workbatchOrdoee.setWkId(workbatchOrdlink.getId());
            workbatchOrdoee.setDifficultNum(1.0);
            workbatchOrdoee.setPlanTotalTime(1);//TODO:???????????????,?????????
            int workbatchOrdoeeInsert = workbatchOrdoeeMapper.insert(workbatchOrdoee);
        }
        return "????????????";
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param maId
     * @return
     */
    @Override
    public WorkbatchOrdlinkVO getReceivedOrder(Integer maId) {
//        return workbatchOrdlinkMapper.getReceivedOrder(maId);
        return workbatchOrdlinkMapper.getReceivedOrderByMaInfo(maId);
    }

    @Override
    public WorkbatchOrdlinkVO getReceivedOrderBywfId(Integer wfId) {
        return workbatchOrdlinkMapper.getReceivedOrderBywfId(wfId);
    }


    @Override
    public List<WorkbatchOrdlinkVO> getwaitWorkBatchOrd(WorkbatchOrdlinkVO workbatchOrdlinkVO) {
        if (workbatchOrdlinkVO != null && workbatchOrdlinkVO.getSdDate() == null) {
            String sdDate = DateUtil.refNowDay();
            workbatchOrdlinkVO.setSdDate(sdDate);//?????????????????????sdDate???????????????
        }
        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOS = workbatchOrdlinkMapper.getwaitWorkBatchOrd(workbatchOrdlinkVO);
        //????????????????????????????????????????????????
        for (WorkbatchOrdlinkVO o : workbatchOrdlinkVOS) {
            WorkbatchOrdlinkVO workbatchOrdlinkVO1 = new WorkbatchOrdlinkVO();
            workbatchOrdlinkVO1.setSdId(o.getId());
            List<MaterMtinfoVO> materBatchlinks = materMtinfoMapper.findBySdId(workbatchOrdlinkVO1);
            if (!materBatchlinks.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                materBatchlinks.forEach(m -> {
                    if (m != null) {
                        if (m.getMcId() != null && m.getMold() == 1) {
                            if (StringUtils.isNotBlank(m.getMlName())) {
                                o.setMaterialName(m.getMlName());
                            }
                            o.setMainInTime(m.getInTime());
                            o.setMainIngredientTime(m.getInstorageTime());
                        }
                        if (m.getMcId() != null && m.getMold() == 2) {
                            if (StringUtils.isNotBlank(m.getMlName())) {
                                builder.append(m.getMlName() + ",");
                            }
                        }
                        o.setInTime(m.getInTime());
                        o.setIngredientTime(m.getInstorageTime());
                        o.setWarehouseStatus(m.getStatus());
                    }
                });
                if (builder.length() > 0) {
                    o.setIngredientName(builder.substring(0, builder.toString().length() - 1));
                }
            }
        }
        List<WorkbatchOrdlinkVO> list = new ArrayList<>();
        workbatchOrdlinkVOS = workbatchOrdlinkVOS.stream().filter(o -> {
            if (StringUtils.isNotBlank(o.getSdSort())) {
                return true;
            } else {
                list.add(o);
                return false;
            }
        }).collect(toList());
        workbatchOrdlinkVOS.addAll(list);
        return workbatchOrdlinkVOS;
    }

    @Override
    public Map<String, Integer> getDayList(Integer maId, Integer dpId, Integer wsId) {

        WorkbatchOrdlink batchOrdlink = new WorkbatchOrdlink();
        batchOrdlink.setMaId(maId);
        batchOrdlink.setMaType(null);
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
        if (machineMainfo != null && machineMainfo.getIsRecepro() == 1) {
            Integer maType = (machineMainfo.getMaType() != null) ? Integer.parseInt(machineMainfo.getMaType()) : 0;
            batchOrdlink.setMaType(maType);
            batchOrdlink.setMaId(null);
        }
        //batchOrdlink.setWsId(wsId);
//        if (dpId == null) {
//            MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
//            if (machineMainfo == null) {
//                throw new CommonException(HttpStatus.NOT_FOUND.value(), "????????????????????????????????????????????????");
//            }
//            batchOrdlink.setDpId(machineMainfo.getDpId());
//        }
        Date now = new Date();
        Date date = DateUtil.addDayForDate(now, -1);
        Date date1 = DateUtil.addDayForDate(now, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String nowDate = simpleDateFormat.format(now);
        String startDate = simpleDateFormat.format(date);
        String endDate = simpleDateFormat.format(date1);


        List<Map> maps = ordlinkMapper.findWorkbatchOrdlinkCount(batchOrdlink, startDate, endDate);
        Map<String, Integer> remap = new LinkedHashMap<>();
        if (maps != null && maps.size() > 0) {
            for (Map count : maps) {
                int cunm = 0;
                if (count.get("cnum") != null) {
                    cunm = ((Long) count.get("cnum")).intValue();
                }
                remap.put((String) count.get("sd_date"), cunm);
            }
        }
        return remap;
    }

    @Override
    public WorkbatchOrdlinkVO getPudectOrder(Integer maId) {

        WorkbatchOrdlink runOrder = new WorkbatchOrdlink();
        runOrder.setMaId(maId);
        runOrder.setStatus("1");
        runOrder.setRunStatus(1);
        return ordlinkMapper.getRunOrder(runOrder);
    }


    @Override
    public IPage<WorkbatchOrdlinkVO> getyetWorkBatchOrd(IPage<WorkbatchOrdlinkVO> page, WorkbatchOrdlinkVO
            workbatchOrdlinkVO) {
        List<Integer> prIds = new ArrayList<>();
        if (null != workbatchOrdlinkVO.getPrId()) {
            // ???????????????????????????????????????????????????????????????id
            Integer clId = processClassifyMapper.getClassifyByPrId(workbatchOrdlinkVO.getPrId());
            prIds = processClassifyMapper.getPrIdsByClaId(clId);
            workbatchOrdlinkVO.setMaId(null);
        }
        String by = "";
        boolean materialNameSort = false;
        boolean ingredientNameSort = false;

        workbatchOrdlinkVO.setOrderBy("create_at DESC");
        if (workbatchOrdlinkVO.getOrderBy() != null && workbatchOrdlinkVO.getOrderBy().length() > 0) {
            String[] ord = Func.toStrArray(workbatchOrdlinkVO.getOrderBy());
            for (String od : ord) {
                if (by.equals("")) {
                    by += "ORDER BY " + od;
                } else {
                    by += ", " + od;
                }
                if (od.equals("ingredient_name")) {
                    ingredientNameSort = true;
                }
                if (od.equals("material_name")) {
                    materialNameSort = true;
                }
            }
        }
        workbatchOrdlinkVO.setOrderBy(by);
        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOS = workbatchOrdlinkMapper.getyetWorkBatchOrd(page, workbatchOrdlinkVO, prIds);
        if (workbatchOrdlinkVOS == null) {
            return page.setRecords(new ArrayList<>());
        }
        for (int i = 0; i < workbatchOrdlinkVOS.size(); i++) {
            WorkbatchOrdlinkVO o = workbatchOrdlinkVOS.get(i);
            Integer worksNum = worksNum(o.getId(), workbatchOrdlinkVO.getMaId());
            o.setNoSchedulingNum(worksNum);
            o.setSdId(o.getId());
            workbatchOrdlinkVO.setSdId(o.getId());
            if (o.getPtId() == null) {
                continue;
            }
            if (StringUtils.isNotBlank(workbatchOrdlinkVO.getMaterialName())) {
                o.setMaterialName(workbatchOrdlinkVO.getMaterialName());
            }
            if (StringUtils.isNotBlank(workbatchOrdlinkVO.getIngredientName())) {
                o.setIngredientName(workbatchOrdlinkVO.getIngredientName());
            }
            List<MaterMtinfoVO> materMtinfoVOS = materMtinfoMapper.findBySdId(workbatchOrdlinkVO);
            if (!materMtinfoVOS.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                materMtinfoVOS.forEach(m -> {
                    if (m != null) {
                        if (m.getMcId() != null && m.getMold() == 1) {
                            if (StringUtils.isNotBlank(m.getMlName())) {
                                o.setMaterialName(m.getMlName());
                                o.setMainInTime(m.getInTime());
                                o.setMainIngredientTime(m.getInstorageTime());
                            }
                        }
                        if (m.getMcId() != null && m.getMold() == 2) {
                            if (StringUtils.isNotBlank(m.getMlName())) {
                                builder.append(m.getMlName() + ",");
                            }
                        }
                        o.setInTime(m.getInTime());
                        o.setIngredientTime(m.getInstorageTime());
                        o.setWarehouseStatus(m.getStatus());
                    }
                });
                if (builder.length() > 0) {
                    o.setIngredientName(builder.substring(0, builder.toString().length() - 1));
                }
                o.setMaterMtinfo(materMtinfoVOS);
            }
        }
        //???????????????????????????????????????????????????
        //List<MaterBatchlink> materbatchLs =  getMaterDate(vos);

        //????????????
        if (materialNameSort) {
            workbatchOrdlinkVOS = workbatchOrdlinkVOS.stream().sorted(Comparator.comparing(WorkbatchOrdlinkVO::getMaterialName)).collect(toList());
        }
        //????????????
        if (ingredientNameSort) {
            workbatchOrdlinkVOS = workbatchOrdlinkVOS.stream().sorted(Comparator.comparing(WorkbatchOrdlinkVO::getIngredientName)).collect(toList());
        }
        return page.setRecords(workbatchOrdlinkVOS);
    }


    /****
     * ???????????????????????????
     * @param workBatechLs
     * @return
     */
    private List<MaterBatchlink> getMaterDate(List<WorkbatchOrdlinkVO> workBatechLs) {
        List<Integer> mtSdids = new ArrayList<>();
        ;
        if (workBatechLs != null && !workBatechLs.isEmpty()) {
            workBatechLs.forEach(wbatch -> {
                mtSdids.add(wbatch.getSdId());
            });
            //??????sdids????????????????????????
            if (mtSdids != null && mtSdids.size() > 0) {
                return materBatchlinkMapper.findBySdIds(mtSdids);
            }
        }
        return null;
    }


    @Override
    public List<WorkbatchOrdlink> getMaIdList(String sdDate, Integer wsId) {
        return workbatchOrdlinkMapper.getMaIdList(sdDate, wsId);
    }


    @Override
    public List<Integer> getBySdDate(String targetDay, Integer wsId) {
        return workbatchOrdlinkMapper.getBySdDate(targetDay, wsId);
    }

    @Override
    public List<WorkbatchOrdlinkShiftVO> getOrdLinkBysdate(String sdDate, Integer maId, Integer wsId) {
        WorkbatchOrdlinkShiftVO worklink = new WorkbatchOrdlinkShiftVO();
        worklink.setSdDate(sdDate);
        worklink.setMaId(maId);
        //worklink.setWsId(wsId); //???????????????????????????????????????

        return workbatchOrdlinkMapper.getOrdLinkBysdate(worklink);
    }

    @Override
    public WorkbatchOrdlinkShiftVO getById(Integer id) {
        return workbatchOrdlinkMapper.getOrdshiftById(id);
    }

    @Override
    public WorkbatchOrdlink getOrdById(Integer sdId) {
        return workbatchOrdlinkMapper.getOrdById(sdId);
    }

    @Override
    public void XYWorkbatchExcelExport(String sdDate, Integer maId, Integer wsId) {

        /*??????????????????????????????*/
        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList = workbatchOrdlinkMapper.selectBySdDateMaIdWsId(sdDate, maId, wsId);
        String templatePath = "model/WorkbatchOrdlinkXY.xls";
//        InputStream imageInputStream = null;//?????????
        BufferedOutputStream bos = null;//???????????????
        Integer index = 0;
        for (WorkbatchOrdlinkVO workbatchOrdlinkVO : workbatchOrdlinkVOList) {
            index++;
            workbatchOrdlinkVO.setIndex(index);
            Integer sdCoutNum = workbatchOrdlinkVO.getSdCoutNum();
            Integer wasteNum = workbatchOrdlinkVO.getWasteNum();
            sdCoutNum = sdCoutNum == null ? 0 : sdCoutNum;
            wasteNum = wasteNum == null ? 0 : wasteNum;
            workbatchOrdlinkVO.setWsPlanNumber(sdCoutNum - wasteNum);
            String ptSize = workbatchOrdlinkVO.getPtSize();
            Integer maType = workbatchOrdlinkVO.getMaType();
            switch (maType) {
                case 1://??????
                case 2://??????
                case 3://????????????
                case 4://??????
                case 5://??????
                case 9: {//????????????
                    ptSize = workbatchOrdlinkVO.getOperateSize();//????????????
                    break;
                }
                case 6://??????
                case 8: {//??????
                    ptSize = workbatchOrdlinkVO.getPdSize();//????????????
                    break;
                }

            }
            workbatchOrdlinkVO.setPtSize(ptSize);
//            System.out.println(workbatchOrdlinkVO);
//            byte[] result = BarcodeUtil.generate(workbatchOrdlinkVO.getId().toString());
            byte[] result = BarcodeUtil.generateBarCode(workbatchOrdlinkVO.getId().toString());
            // ??????????????????????????????
            try {
                // ??????????????????????????????byte??????
                byte[] imageBytes = result;
                workbatchOrdlinkVO.setImg(imageBytes);
            } catch (Exception e) {
                e.printStackTrace();
            } //finally {
//                try {
//                    if (imageInputStream != null) {
//                        imageInputStream.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        //List<Page> page = XYindividual(workbatchOrdlinkVOList); // ???????????????????????????
        List<Page> page = DataByPage.byPage(workbatchOrdlinkVOList);
        Map<String, Object> model = new HashMap<>();

        model.put("pages", page);
        model.put("sheetNames", getSheetName(page));
        model.put("slName", getSheetName(page));
        try {
            bos = ExportlUtil.getBufferedOutputStream("?????????.xls", response);//??????????????????
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//???????????????
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * ???????????????????????????????????????????????????List
     */
    public static List<Page> XYindividual(List<WorkbatchOrdlinkVO> list) {
        List<Page> pages = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Page p = new Page();
            p.setOnlyOne(list.get(i));
//            p.setSheetName(list.get(i).getDpName()+"-"+list.get(i).getSfName()+"-"+(i+1));
            //??????sheet??????
            p.setSheetName(list.get(i).getSdDate() + list.get(i).getPrName());
            pages.add(p);
        }

        return pages;
    }


    /******
     * ????????????????????????
     * @param workbatchOrdlinkVO
     * @return
     */
    @Override
    public int andProductionScheduling(WorkbatchOrdlinkVO workbatchOrdlinkVO) {
        WorkbatchShift workbatchShift = new WorkbatchShift();
        Integer sdId = workbatchOrdlinkVO.getId();//??????id
        String sdDate = workbatchOrdlinkVO.getSdDate();//????????????
        Integer planNum = workbatchOrdlinkVO.getPlanNum();//????????????
        Integer mouldStay = workbatchOrdlinkVO.getMouldStay();//????????????
        Integer wsId = workbatchOrdlinkVO.getWsId();//??????id

//        WorkbatchOrdlink ordlink = workbatchOrdlinkMapper.selectById(sdId);
        Integer maId = workbatchOrdlinkVO.getMaId();//???????????????id?????????????????????id??????
        List<WorkbatchShift> workbatchShiftList = workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>()
                .eq("sd_date", sdDate).eq("ws_id", wsId).eq("sd_id", sdId).eq("ma_id", workbatchOrdlinkVO.getMaId()));
        if (!workbatchShiftList.isEmpty()) {
            return 0;
        }
        workbatchShift.setSdId(sdId);//??????ID
        workbatchShift.setSdDate(sdDate);//????????????
        workbatchShift.setPlanNum(planNum);//?????????
        workbatchShift.setSpeed(workbatchOrdlinkVO.getSpeed());//????????????
        workbatchShift.setCreateAt(new Date());
        workbatchShift.setWasteNum(0);//?????????
        workbatchShift.setMouldStay(mouldStay);
        workbatchShift.setMaId(workbatchOrdlinkVO.getMaId());
        workbatchShift.setWsId(wsId);//??????id
        workbatchShift.setShiftStatus(-1);//???????????????
        workbatchShift.setStatus("0");//1,0?????????????????????????????????????????????1
        workbatchShift.setMaId(maId);//???????????????????????????id
        workbatchShift.setPlanType("1");//??????????????????
        /*??????????????????*/
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId, maId);
        if (workbatchShiftset != null) {
            workbatchShift.setCkName(workbatchShiftset.getCkName());//????????????
            Date startTime = workbatchShiftset.getStartTime();
            Date endTime = workbatchShiftset.getEndTime();
            Date classStartTime = DateUtil.toDate(sdDate + " " + DateUtil.format(startTime, "HH:mm:ss"), null);
            Date classEndTime = DateUtil.toDate(sdDate + " " + DateUtil.format(endTime, "HH:mm:ss"), null);
            workbatchShift.setStartTime(classStartTime);
            workbatchShift.setEndTime(classEndTime);
        }
        int insert = workbatchShiftMapper.insert(workbatchShift);

        //???????????????????????????????????????----start Jenny
        iWorkbatchOrdlinkNewService.setWorksNum(workbatchShift.getSdId(), workbatchShift.getMaId(), workbatchOrdlinkVO.getStatus());
        //???????????????????????????????????????----end Jenny
        return workbatchShift.getId();
    }


    @Override
    public int updateOrdStatusBySdsort(WorkbatchOrdlink ordlink) {
        return workbatchOrdlinkMapper.updateOrdStatusBySdsort(ordlink.getId(), ordlink.getSdDate(), ordlink.getSdSort(), ordlink.getStatus());
    }

    @Override
    public int updateOrdStatus(WorkbatchOrdlink ordlink) {
        return workbatchOrdlinkMapper.updateOrdStatus(ordlink.getId(), ordlink.getStatus(), ordlink.getMaId());
    }

    @Override
    public int updateBindByMaId(WorkbatchOrdlink ordlink) {
        return workbatchOrdlinkMapper.updateBindByMaId(ordlink.getId(), ordlink.getMaId(), ordlink.getStatus());
    }

    @Override
    public int updatePlannumById(WorkbatchOrdlink ordlink) {
        return workbatchOrdlinkMapper.updatePlannumById(ordlink.getId(), ordlink.getPlanNum());
    }

    @Override
    public WorkbatchOrdlinkOeeVO getOrdlinkOeeBySdId(Integer sdId) {
        return workbatchOrdlinkMapper.getOrdlinkOeeBySdId(sdId);
    }

    @Override
    public int updateOtherInfo(WorkbatchOrdlink ordlink) {
        return workbatchOrdlinkMapper.updateOtherInfo(ordlink.getId(), ordlink.getFinalTime(), ordlink.getIngredientTime(), ordlink.getRemarks(), ordlink.getSecondRemark());
    }

    @Override
    public List<ArticlesCmNameOdNoVO> getArticlesCmNameOdNoVOList(ArticlesBeingProcessedParmVO articlesBeingProcessedParmVO) {
        List<ArticlesOrderVO> articlesOrderVOList =
                workbatchOrdlinkMapper.getArticlesCmNameOdNoVOList(articlesBeingProcessedParmVO);
        List<ArticlesCmNameOdNoVO> cmNameOdNoVOList = new ArrayList<>();
        ArticlesCmNameOdNoVO cmNameOdNoVO;
        List<ArticlesOrderVO> articlesOrderVOS;
        Map<String, List<ArticlesOrderVO>> listMap = new LinkedHashMap<>();
        for (ArticlesOrderVO articlesOrderVO : articlesOrderVOList) {//??????????????????
            String cmName = articlesOrderVO.getCmName();
            if (listMap.containsKey(cmName)) {
                listMap.get(cmName).add(articlesOrderVO);
            } else {
                articlesOrderVOS = new ArrayList<>();
                articlesOrderVOS.add(articlesOrderVO);
                listMap.put(cmName, articlesOrderVOS);
            }
        }
        for (String cmName : listMap.keySet()) {
            cmNameOdNoVO = new ArticlesCmNameOdNoVO();
            cmNameOdNoVO.setCmName(cmName);
            cmNameOdNoVO.setArticlesOrderVOList(listMap.get(cmName));
            cmNameOdNoVOList.add(cmNameOdNoVO);
        }
        return cmNameOdNoVOList;
    }

    @Override
    public List<ArticlesWbProcessVO> getArticlesWbProcessVOList(ArticlesBeingProcessedParmVO articlesBeingProcessedParmVO, String odNo) {
        List<ArticlesWbProcessVO> articlesWbProcessVOList = new ArrayList<>();
        ArticlesWbProcessVO articlesWbProcessVO;
        List<ArticlesProcessVO> articlesProcessVOList =
                workbatchOrdlinkMapper.getArticlesWbProcessVOList(articlesBeingProcessedParmVO, odNo);
        Map<String, List<ArticlesProcessVO>> map = new LinkedHashMap<>();
        List<ArticlesProcessVO> articlesProcessVOS;
        for (ArticlesProcessVO articlesProcessVO : articlesProcessVOList) {
            String wbNo = articlesProcessVO.getWbNo();
            if (map.containsKey(wbNo)) {
                map.get(wbNo).add(articlesProcessVO);
            } else {
                articlesProcessVOS = new ArrayList<>();
                articlesProcessVOS.add(articlesProcessVO);
                map.put(wbNo, articlesProcessVOS);
            }
        }
        for (String wbNo : map.keySet()) {
            articlesWbProcessVO = new ArticlesWbProcessVO();
            List<ArticlesProcessVO> processVOS = map.get(wbNo);
            articlesWbProcessVO.setWbNo(wbNo);
            articlesWbProcessVO.setWbExtraNum(processVOS.get(0).getExtraNum());
            articlesWbProcessVO.setWbPlanCount(processVOS.get(0).getPlanNumber());
            articlesWbProcessVO.setArticlesProcessVOList(processVOS);
            articlesWbProcessVOList.add(articlesWbProcessVO);
        }
        return articlesWbProcessVOList;
    }

    @Override
    public List<ArticlesShiftVO> getArticlesShiftList(Integer sdId) {
        return workbatchOrdlinkMapper.getArticlesShiftList(sdId);
    }

    @Override
    public List<Integer> getWfIdList(String targetDay, Integer wsId) {
        if (targetDay == null) {
            targetDay = DateUtil.refNowDay();
        }
        return workbatchOrdlinkMapper.getWfIdList(targetDay, wsId);
    }


    @Override
    public IPage<WorkbatchOrdlink> getByMachineId(Integer machineId, IPage<WorkbatchOrdlink> page) {
        List<WorkbatchOrdlink> workbatchOrdlinks = workbatchOrdlinkMapper.selectList(Wrappers.<WorkbatchOrdlink>lambdaQuery().eq(WorkbatchOrdlink::getMaId, machineId));
        page.setTotal(workbatchOrdlinks.size());
        return page.setRecords(workbatchOrdlinks);
    }

    @Override
    public IPage<WorkbatchOrdlink> getByProcessId(Integer processId, IPage<WorkbatchOrdlink> page) {
        List<WorkbatchOrdlink> workbatchOrdlinks = workbatchOrdlinkMapper.selectList(Wrappers.<WorkbatchOrdlink>lambdaQuery().eq(WorkbatchOrdlink::getPrId, processId));
        page.setTotal(workbatchOrdlinks.size());
        return page.setRecords(workbatchOrdlinks);
    }


    //    @Override
    public Integer worksNum1(Integer sdId) {
        WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectOne(Wrappers.<WorkbatchOrdlink>lambdaQuery().eq(WorkbatchOrdlink::getId, sdId).select(WorkbatchOrdlink::getPlanNumber));
        AtomicReference<Integer> planNumber = new AtomicReference<>(workbatchOrdlink.getPlanNumber());
        // status== -1:????????? 0:????????????1???????????????2???????????????  3?????????????????????????????? 4????????????(?????????)
        List<SlideShiftDetailsVO> slideShiftDetails = workbatchShiftMapper.findSlideShiftDetails(sdId);
        if (slideShiftDetails.size() == 0) {
            return planNumber.get();
        }
        List<SlideShiftDetailsVO> collect = slideShiftDetails.stream().filter(s -> null != s.getStatus()).collect(toList());
        Map<String, List<SlideShiftDetailsVO>> map = collect.stream().collect(Collectors.groupingBy(SlideShiftDetailsVO::getStatus));
        if (map.containsKey("0")) {
            for (SlideShiftDetailsVO s : map.get("0")) {
                LocalDate sdDate = LocalDate.parse(s.getSdDate());
                if (sdDate.compareTo(LocalDate.now()) >= 0) {
                    planNumber.updateAndGet(v -> v - s.getPlanNum());
                }
            }
        }
        if (map.containsKey("1")) {
            Map<Integer, List<SlideShiftDetailsVO>> m = map.get("1").stream().collect(Collectors.groupingBy(SlideShiftDetailsVO::getSdId));
            m.forEach((k, v) -> {
                planNumber.updateAndGet(v1 -> v1 - v.get(0).getPlanNum());
            });
        }
        if (map.containsKey("2")) {
            for (SlideShiftDetailsVO s : map.get("2")) {
                planNumber.updateAndGet(v -> v - s.getCountNum());
            }
        }
        if (map.containsKey("3")) {
            for (SlideShiftDetailsVO s : map.get("3")) {
                planNumber.updateAndGet(v -> v - s.getPlanNum());
            }
        }
        if (map.containsKey("4")) {
            for (SlideShiftDetailsVO s : map.get("4")) {
                planNumber.updateAndGet(v -> v - s.getCountNum());
            }
        }
        if (map.containsKey("-1")) {
            for (SlideShiftDetailsVO s : map.get("-1")) {
                planNumber.updateAndGet(v -> v - s.getPlanNum());
            }
        }
        return planNumber.get();
    }

    /***
     * ?????????????????????
     * @param sdId
     * @param maId
     * @return
     */
    @Override
    public Integer worksNum(Integer sdId, Integer maId) {
        WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectOne(Wrappers.<WorkbatchOrdlink>lambdaQuery().eq(WorkbatchOrdlink::getId, sdId).select(WorkbatchOrdlink::getPlanNumber));
        if (workbatchOrdlink != null) {
            Integer planNumber = workbatchOrdlink.getPlanNumber();
            String shiftStratTime = workbatchShiftsetMapper.getShiftStartTime(maId);
            // ?????????
            Integer notReportedNum = workbatchShiftMapper.getNotReportedNum(sdId, shiftStratTime);
            // ?????????
            Integer reportedNum = workbatchShiftMapper.getReportedNum(sdId);
            return planNumber - notReportedNum - reportedNum;
        } else {
            return 0;
        }
    }

    @Override
    public IPage<WorksTempoVO> worksTempo(IPage<WorksTempoVO> page) {
        IPage<WorksTempoVO> result = workbatchOrdlinkMapper.selectWorsTempo(page);
        for (WorksTempoVO worksTempo : result.getRecords()) {
            List<WorkbatchOrdlink> workbatchOrdlinks = workbatchOrdlinkMapper.selectList(Wrappers.<WorkbatchOrdlink>lambdaQuery()
                    .eq(WorkbatchOrdlink::getOdNo, worksTempo.getOdNo())
                    .orderByAsc(WorkbatchOrdlink::getSort)
                    .select(WorkbatchOrdlink::getPlanNum, WorkbatchOrdlink::getCompleteNum, WorkbatchOrdlink::getPrName));
            worksTempo.setWorkbatchOrdlinkList(workbatchOrdlinks);
        }
        return result;
    }

    @Override
    public Integer updateByworksNum(Integer sdId, Integer wknum, String status) {
        return workbatchOrdlinkMapper.updateByworksNum(sdId, wknum, status);
    }

    @Override
    public Integer setCompleteNum(Integer sdId, Integer completeNum, Integer incompleteNum) {
        return workbatchOrdlinkMapper.setCompleteNum(sdId, completeNum, incompleteNum);
    }


    @Override
    public Integer hasShiftThreeDay(Integer maId) {
        //???????????????????????????????????????
        WorkbatchOrdlink batchOrdlink = new WorkbatchOrdlink();
        batchOrdlink.setMaId(maId);
        batchOrdlink.setMaType(null);
        //?????????????????????????????????????????????????????????????????????
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
        if (machineMainfo != null && machineMainfo.getIsRecepro() == 1) {
            Integer maType = (machineMainfo.getMaType() != null) ? Integer.parseInt(machineMainfo.getMaType()) : 0;
            batchOrdlink.setMaType(maType);
            batchOrdlink.setMaId(null);
        }
        Date now = new Date();
        Date date = DateUtil.addDayForDate(now, -1);
        Date date1 = DateUtil.addDayForDate(now, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = simpleDateFormat.format(date);
        String endDate = simpleDateFormat.format(date1);


        List<Map> maps = ordlinkMapper.findWorkbatchOrdlinkCount(batchOrdlink, startDate, endDate);
        Map<String, Integer> remap = new LinkedHashMap<>();
        Integer totalnum = 0;
        if (maps != null && maps.size() > 0) {
            for (Map count : maps) {
                int cunm = 0;
                if (count.get("cnum") != null) {
                    cunm = ((Long) count.get("cnum")).intValue();
                    totalnum += cunm;
                }
                remap.put((String) count.get("sd_date"), cunm);
            }
        }
        return totalnum;
    }

    @Override
    public List<ProductionSchedulingDetailsVO> productionSchedulingDetails(ProductionSchedulingDetailsParam detailsParam) {

        String starTime = detailsParam.getStarTime();
        if (StringUtil.isEmpty(starTime)) {
            detailsParam.setStarTime(DateUtil.refNowDay());
        }
        List<Integer> wsIdList = detailsParam.getWsIdList();
        List<Integer> maIdList = detailsParam.getMaIdList();
        if (maIdList.isEmpty()) {
            return null;
        }
        List<ProductionSchedulingDetailsVO> productionSchedulingDetailsVOList = new ArrayList<>();
        List<WorkbatchShiftDetailVO> workbatchShiftDetailVOList = workbatchOrdlinkMapper.productionSchedulingDetails(detailsParam);
        maIdList.forEach(maId -> {
            ProductionSchedulingDetailsVO productionSchedulingDetailsVO = new ProductionSchedulingDetailsVO();
            List<MealOneTimeVO> mealOneTimeVOList = new ArrayList();
            wsIdList.forEach(e -> {
                MealOneTimeVO mealOneTimeVO = null;
                WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.getWorkbatchShiftset(e, maId);
                String mealOnetime = workbatchShiftset.getMealOnetime();
                if (!StringUtil.isEmpty(mealOnetime)) {
                    mealOneTimeVO = new MealOneTimeVO();
                    String[] split = mealOnetime.split("~");
                    mealOneTimeVO.setStarTime(split[0]);
                    mealOneTimeVO.setEndTime(split[1]);
                    mealOneTimeVOList.add(mealOneTimeVO);
                }
                String mealSecondtime = workbatchShiftset.getMealSecondtime();
                if (!StringUtil.isEmpty(mealSecondtime)) {
                    mealOneTimeVO = new MealOneTimeVO();
                    String[] split = mealSecondtime.split("~");
                    mealOneTimeVO.setStarTime(split[0]);
                    mealOneTimeVO.setEndTime(split[1]);
                    mealOneTimeVOList.add(mealOneTimeVO);
                }
                String mealThirdtime = workbatchShiftset.getMealThirdtime();
                if (!StringUtil.isEmpty(mealThirdtime)) {
                    mealOneTimeVO = new MealOneTimeVO();
                    String[] split = mealThirdtime.split("~");
                    mealOneTimeVO.setStarTime(split[0]);
                    mealOneTimeVO.setEndTime(split[1]);
                    mealOneTimeVOList.add(mealOneTimeVO);
                }
            });
            productionSchedulingDetailsVO.setMealOneTimeVOList(mealOneTimeVOList);
            productionSchedulingDetailsVO.setMaId(maId);
            MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
            if (machineMainfo != null) {
                productionSchedulingDetailsVO.setMaName(machineMainfo.getName());
            }
            Stream<WorkbatchShiftDetailVO> workbatchShiftDetailVOStream =
                    workbatchShiftDetailVOList.stream().filter(e -> maId.equals(e.getMaId()));
            productionSchedulingDetailsVO.setWorkbatchShiftDetailVOList(workbatchShiftDetailVOStream.collect(toList()));
            productionSchedulingDetailsVOList.add(productionSchedulingDetailsVO);
        });
        return productionSchedulingDetailsVOList;
    }
}
