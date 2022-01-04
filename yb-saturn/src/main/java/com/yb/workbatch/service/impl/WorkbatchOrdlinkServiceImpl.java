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
 * 生产排产表yb_workbatch_ordlink 服务实现类
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
    private ProcessMachlinkMapper proceMalinkMapper;//设备和工艺分类关联数据信息

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
     * 根据前端页面传递过来的设备ID
     * 获取等待生产的订单
     */
    @Override
    public List<WorkbatchOrdlinkVO> getOrderList(WorkbatchOrdlink workbatchOrdlink) {
        //获取工单属于哪个车间的数据信息
        if (workbatchOrdlink.getDpId() == null) {
            MachineMainfo machineMainfo = machineMainfoMapper.selectById(workbatchOrdlink.getMaId());
            if (machineMainfo == null) {
                throw new CommonException(HttpStatus.NOT_FOUND.value(), "未找到设备信息，获取订单列表失败");
            }
            workbatchOrdlink.setDpId(machineMainfo.getDpId());//写入车间部门id信息内容
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
        //获取工单属于哪个车间的数据信息
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
        if (machineMainfo == null) {
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "未找到设备信息，获取订单列表失败");
        }
        //设置查询条件管理
        WaitOrderRequest wkquery = new WaitOrderRequest();
        wkquery.setMaId(maId);

        //根据设备查询出对应设备任务工单
        List<WorkbatchShiftListVO> wbatchList = ordlinkMapper.findWorkbatchOrdlinkNew(wkquery, startDate, endDate);

        return wbatchList;
    }

    @Override
    public List<WorkbatchOrdlinkVO> getOrderListByMatype(WorkbatchOrdlink workbatchOrdlink) {
        WorkbatchOrdlink wklink = workbatchOrdlink;
        wklink.setMaId(null);//设定设备条件为空
        List<WorkbatchOrdlinkVO> workbatchOrdlink1 = ordlinkMapper.findWorkbatchOrdlinkByMatype(wklink, null, null);
        for (WorkbatchOrdlinkVO workbatchOrdlinkVO : workbatchOrdlink1) {
            workbatchOrdlinkVO.setMaId(workbatchOrdlink.getMaId());
        }
        return workbatchOrdlink1;
    }


    /****
     * 按照工序分类【pyId】进行查询统计
     * @param maId
     * @return
     */
    @Override
    public List<WorkbatchShiftListVO> getOrderListByPrids(Integer maId, String startDate, String endDate) {

        WorkbatchOrdlink wklink = new WorkbatchOrdlink();
        wklink.setMaId(null);//设定设备条件为空
        String prIds = proceMalinkMapper.getPridsBymaId(maId);//设备按照工序接单过程
        List<WorkbatchShiftListVO> wbatchlink = ordlinkMapper.getOrderListByPrids(prIds, startDate, endDate);
        //查出对应数据内容信息，并且把设备信息统一修改为本设备id的内容信息
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
     * 接单的内容信息
     * @param orderAccept
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R acceptOrderBywfid(OrderAcceptRequest orderAccept) {
        Integer wfId = orderAccept.getWfId();
        Integer maId = orderAccept.getMaId();
        Integer usId = orderAccept.getUsId();

        //ordlinkVO 按照设备id进行工序排产，不能够接收该工单；wyn修改判断
        if (wfId != null && maId != null) {
            MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
            Integer isRecepro = (machineMainfo != null) ? machineMainfo.getIsRecepro() : 0;//默认为
            //不是按照工序进行接单设备;按照设备接单
            if (isRecepro != 1) {
                Map<String, Object> exeMainfo = superviseExecuteMapper.findMainfoBywfId(wfId);
                if (exeMainfo != null && exeMainfo.size() > 0) {
                    Integer cnum = ((Long) exeMainfo.get("cnum")).intValue();
                    String maName = (String) exeMainfo.get("maName");
                    if (cnum > 0) {
                        log.info("该工单已经被[cnum:{}]台设备：[maName:{}]", cnum, maName);
                        throw new CommonException(HttpStatus.NOT_FOUND.value(), "已有" + cnum + "台设备正在生产该工单：" + maName + "；请退出重新登录。");
                    }
                }
            }
        }

        //获取设备的当前的状态信息，如果已经是接单状态就返回异常。
        SuperviseExecute superviseExecute = superviseExecuteMapper.getExecuteOrder(maId);
        //SuperviseBoxinfo boxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //该设备是否已有生产中订单
        // List<WorkbatchShift> workbatchShifts = workbatchShiftMapper.getProOrder(ordlinkVO.getMaId());
        if ("B".equals(superviseExecute.getExeStatus()) || "C".equals(superviseExecute.getExeStatus())) {
            log.info("该设备已接单，请勿重复接单:[maId:{}]", maId);
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "该设备已经是生产状态，请重新登录再执行操作");
        }
        //查询这个排产关联管理信息内容
        WorkbatchShiftVO wbatchshift = workbatchShiftMapper.getBywfId(wfId);
        //当前的时间
        Date currDate = new Date();
        //根据当前时间计算出班次信息，并且计算班次的指定时间 得到当前的排产时间
        WorkbatchShiftset shiftset = workbatchShiftsetMapper.getShiftsetBymaId(maId);
        if (shiftset == null || shiftset.getId() == null) {
            shiftset = new WorkbatchShiftset();
            shiftset.setWsId(44);//如果没有查到就放白班id信息；没有班次信息内容
            shiftset.setStartTime(currDate);
        }
        String targetDay = (shiftset.getStartTime() != null) ? DateUtil.refNowDay(shiftset.getStartTime()) : DateUtil.refNowDay(currDate);
        //接单时新增执行表数据
        ExecuteInfo executeInfo = saveExcuteInfoNew(maId, wbatchshift, currDate, wbatchshift.getWbNo(), shiftset.getWsId(), targetDay, usId);

        //修改是否接单状态
        SuperviseBoxinfo boxInfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //调整班次关联信息，并且更新接单的实施订单数据信息
        if (wbatchshift != null) {
            superviseExecute.setOperator(usId);
            superviseExecute.setWfId(wfId);
            superviseExecute.setWbNo(wbatchshift.getWbNo());
            superviseExecute.setSdId(wbatchshift.getSdId());
            superviseExecute.setOperator(usId);
            superviseExecute.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());//设定为B1为接单信息
            superviseExecute.setExeStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());//设定为B的接单信息
            superviseExecute.setExId(executeInfo.getId());
            superviseExecute.setUpdateAt(currDate);
            superviseExecute.setCurrNum(0);//接单清零管理操作
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
        ordlink.setRunStatus(1);//接单执行状态
        ordlink.setStatus("1");//0:待接单，1：生产中，2：生产完成
        updateAcceptByUsId(ordlink);//更新接收人员和状态
        System.out.print("===================================4-1");
        //更新排产班次内容信息，接单管理
        if (wbatchshift != null) {
            wbatchshift.setShiftStatus(1);//0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报） 执行生产中的管理操作
            wbatchshift.setStatus("1");
            workbatchShiftMapper.updateById(wbatchshift);
        }
        System.out.print("===================================6");
        // SendMsgUtils.sendToUser(usIds, JSON.toJSONString(temp), ChatType.MAC_ACCEPTORDER.getType());//机台发通知给登录印聊人员信息
        System.out.print("===================================7");
        //接单状态
        boxInfo.setBlnAccept(1);
        boxInfo.setWbNo(ordlink.getWbNo());
        superviseBoxinfoMapper.updateById(boxInfo);
        return R.ok(executeInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R acceptOrderNew(WorkbatchOrdlinkVO ordlinkVO) {
        Integer maId = ordlinkVO.getMaId();  //设备id
        Integer sdId = ordlinkVO.getId();  //排产id
        Integer wsId = ordlinkVO.getWsId();  //排产班次id
        Integer wfId = ordlinkVO.getWfId();  //排程单id

        //ordlinkVO 按照设备id进行工序排产，不能够接收该工单；wyn修改判断
        if (wfId != null && maId != null) {
            MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
            Integer isRecepro = machineMainfo.getIsRecepro();
            //不是按照工序进行接单设备;按照设备接单
            if (isRecepro != 1) {
                Map<String, Object> exeMainfo = superviseExecuteMapper.findMainfoBywfId(wfId);
                if (exeMainfo != null && exeMainfo.size() > 0) {
                    Integer cnum = ((Long) exeMainfo.get("cnum")).intValue();
                    String maName = (String) exeMainfo.get("maName");
                    if (cnum > 0) {
                        log.info("该工单已经被[cnum:{}]台设备：[maName:{}]", cnum, maName);
                        throw new CommonException(HttpStatus.NOT_FOUND.value(), "已有" + cnum + "台设备正在生产该工单：" + maName + "；请退出重新登录。");
                    }
                }
            }
        }


        SuperviseExecute superviseExecute = superviseExecuteMapper.selectOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
        //TODO:接单修改主计划状态,其他未改;更新主计划状态信息
        WorkbatchProgress workbatchProgress =
                workbatchProgressMapper.selectOne(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("wp_type", 3));
        if (workbatchProgress != null) {
            workbatchProgress.setStatus(2);//开始生产状态
            workbatchProgressMapper.updateById(workbatchProgress);
            System.out.print("===================================4-2");
        }
        //该设备是否已有生产中订单
        // List<WorkbatchShift> workbatchShifts = workbatchShiftMapper.getProOrder(ordlinkVO.getMaId());


        if ("B".equals(superviseExecute.getExeStatus()) || "C".equals(superviseExecute.getExeStatus())) {
            log.info("该设备已接单，请勿重复接单:[maId:{}]", maId);
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "该设备已经是生产状态，请重新登录再执行操作");
        }

        ExecuteState newState = new ExecuteState();
        Date currDate = new Date();
        String sdDate = ordlinkVO.getSdDate();
        //根据设备id查询设备状态表数据
        SuperviseExecute executeOrder = executeMapper.getExecuteOrder(maId);
        String usIds = (executeOrder != null) ? executeOrder.getUsIds() : "all";
        newState.setTeamId(usIds);   //当前所有登录人员id
        newState.setMaId(maId);   //设备id
        newState.setSdId(sdId);   //排产id
        //查询对应的班次信息
        //WorkbatchShift wshift = getWorkbatchShift(currDate, maId, wsId, sdDate, sdId, ordlinkVO.getPlanNum(), ordlinkVO.getWfId());

        newState.setWfId(ordlinkVO.getWfId());//追加排产班次信息wfId的对应信息
        newState.setWbId(ordlinkVO.getWbId());   //批次id
        newState.setUsId(ordlinkVO.getUsId());
        newState.setStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());    //生产状态
        newState.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());   //接单
        newState.setStartAt(currDate);
        newState.setCreateAt(currDate);

        System.out.print("===================================2");
        WorkbatchOrdlink workbatchOrdlink = ordlinkMapper.selectOne(new QueryWrapper<WorkbatchOrdlink>().eq("id", sdId));

        //接单时新增执行表数据
        ExecuteInfo executeInfo = saveExcuteInfo(ordlinkVO, maId, sdId, newState, currDate, workbatchOrdlink);

        System.out.print("===================================1");
        //更新设备实时状态表和状态信息
        UpdateStateUtils.updateSupervise(newState, executeInfo);


        System.out.print("===================================3");
        Integer infoId = executeInfo.getId();
//        TempEntity temp = new TempEntity();
//        temp.setStartTime(currDate);
//        temp.setOrderName(ordlinkMapper.getOrderName(sdId));
        //更新排产单状态
        workbatchOrdlink.setRunStatus(1);//接单执行状态
        Integer usId = ordlinkVO.getUsId();
        System.out.print("===================================4");
        if (usId != null) {//登录人id
            workbatchOrdlink.setUsId(usId);
        }
        updateAcceptByUsId(workbatchOrdlink);//更新接收人员和状态
        System.out.print("===================================4-1");
        //更新排产班次内容信息，接单管理
        WorkbatchShift wshift = workbatchShiftMapper.selectById(ordlinkVO.getWfId());
        if (wshift != null) {
            wshift.setShiftStatus(1);//0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报） 执行生产中的管理操作
            wshift.setStatus("1");
            workbatchShiftMapper.updateById(wshift);
        }

        System.out.print("===================================5");

        System.out.print("===================================58");
        //防止设备未绑定工序报错ee
        ProcessMachlink processMachlink = processMachlinkMapper.getEntityByPrMt(maId, workbatchOrdlink.getPrId());
        if (processMachlink == null) {
            log.error("设备工序未关联:maId:{}, prId:{}", maId, workbatchOrdlink.getPrId());
            List<ProcessMachlink> list = processMachlinkMapper.selectList(new QueryWrapper<ProcessMachlink>().eq("ma_id", maId));
            if (!list.isEmpty()) {
                ProcessMachlink processMachlink1 = list.get(0);
                processMachlink1.setPrId(workbatchOrdlink.getPrId());
                processMachlink1.setMaId(maId);
                processMachlink1.setPrepareTime(processMachlink1.getPrepareTime());
                processMachlink1.setSpeed(processMachlink1.getSpeed());
                processMachlinkMapper.insert(processMachlink1);
                processMachlink = processMachlink1;
                log.error("设备未与工序关联,存储设备工序关联信息成功:maId:{}, prId:{}", maId, workbatchOrdlink.getPrId());
            }
        }

        System.out.print("===================================6");
        // SendMsgUtils.sendToUser(usIds, JSON.toJSONString(temp), ChatType.MAC_ACCEPTORDER.getType());//机台发通知给登录印聊人员信息
        System.out.print("===================================7");
        //如获取上工序的信息
        WorkbatchOrdlink ordlink = workbatchOrdlinkMapper.selectById(sdId);
        //修改是否接单状态
        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //接单状态
        superviseBoxinfo.setBlnAccept(1);
        superviseBoxinfo.setWbNo(ordlink.getWbNo());
        superviseBoxinfoMapper.updateById(superviseBoxinfo);
        return R.ok(infoId);
    }


    private void setMainProgress(Integer sdId) {
        //TODO:接单修改主计划状态,其他未改;更新主计划状态信息
        WorkbatchProgress workbatchProgress =
                workbatchProgressMapper.selectOne(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("wp_type", 3));
        if (workbatchProgress != null) {
            workbatchProgress.setStatus(2);//开始生产状态
            workbatchProgressMapper.updateById(workbatchProgress);
            System.out.print("===================================4-2");
        }
    }

    /**
     * 接受订单
     *
     * @param ordlinkVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R acceptOrder(WorkbatchOrdlinkVO ordlinkVO) {
        Integer maId = ordlinkVO.getMaId();  //设备id
        Integer sdId = ordlinkVO.getId();  //排产id
        Integer wsId = ordlinkVO.getWsId();  //排产班次id
        SuperviseExecute superviseExecute = superviseExecuteMapper.selectOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
        //TODO:接单修改主计划状态,其他未改;更新主计划状态信息
        WorkbatchProgress workbatchProgress =
                workbatchProgressMapper.selectOne(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("wp_type", 3));
        if (workbatchProgress != null) {
            workbatchProgress.setStatus(2);//开始生产状态
            workbatchProgressMapper.updateById(workbatchProgress);
            System.out.print("===================================4-2");
        }
        //该设备是否已有生产中订单
        // List<WorkbatchShift> workbatchShifts = workbatchShiftMapper.getProOrder(ordlinkVO.getMaId());


        if ("B".equals(superviseExecute.getExeStatus()) || "C".equals(superviseExecute.getExeStatus())) {
            log.info("该设备已接单，请勿重复接单:[maId:{}]", maId);
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "该设备已接单，请勿重复接单");
        }
//        if (StringUtils.isNotBlank(ordlinkVO.getOdNo())) {
//            OrderOrdinfo orderOrdinfo = orderOrdinfoMapper.getOrderInfoByodNo(ordlinkVO.getOdNo());
//            if (orderOrdinfo != null) {
//                Integer ordId = orderOrdinfo.getId(); //订单id
//                //更改订单状态为进行中
//                if (!orderOrdinfo.getProductionState().equals(OrderStatusConstant.RUN_STATUS_PRODUCTION)) {
//                    orderOrdinfo.setProductionState(OrderStatusConstant.RUN_STATUS_PRODUCTION);
//                    orderOrdinfoMapper.updateById(orderOrdinfo);
//                    newState.setOdId(ordId);  //订单id
//                }
//            }
//        }
        ExecuteState newState = new ExecuteState();
        Date currDate = new Date();
        String sdDate = ordlinkVO.getSdDate();
        //根据设备id查询设备状态表数据
        SuperviseExecute executeOrder = executeMapper.getExecuteOrder(maId);
        String usIds = (executeOrder != null) ? executeOrder.getUsIds() : "all";
        newState.setTeamId(usIds);   //当前所有登录人员id
        newState.setMaId(maId);   //设备id
        newState.setSdId(sdId);   //排产id
        //查询对应的班次信息
        //WorkbatchShift wshift = getWorkbatchShift(currDate, maId, wsId, sdDate, sdId, ordlinkVO.getPlanNum(), ordlinkVO.getWfId());

        newState.setWfId(ordlinkVO.getWfId());//追加排产班次信息wfId的对应信息
        newState.setWbId(ordlinkVO.getWbId());   //批次id
        newState.setUsId(ordlinkVO.getUsId());
        newState.setStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());    //生产状态
        newState.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());   //接单
        newState.setStartAt(currDate);
        newState.setCreateAt(currDate);

        System.out.print("===================================2");
        WorkbatchOrdlink workbatchOrdlink = ordlinkMapper.selectOne(new QueryWrapper<WorkbatchOrdlink>().eq("id", sdId));

        //接单时新增执行表数据
        ExecuteInfo executeInfo = saveExcuteInfo(ordlinkVO, maId, sdId, newState, currDate, workbatchOrdlink);

        System.out.print("===================================1");
        //更新设备实时状态表和状态信息
        UpdateStateUtils.updateSupervise(newState, executeInfo);


        System.out.print("===================================3");
        Integer infoId = executeInfo.getId();
//        TempEntity temp = new TempEntity();
//        temp.setStartTime(currDate);
//        temp.setOrderName(ordlinkMapper.getOrderName(sdId));
        //更新排产单状态
        workbatchOrdlink.setRunStatus(1);//接单执行状态
        Integer usId = ordlinkVO.getUsId();
        System.out.print("===================================4");
        if (usId != null) {//登录人id
            workbatchOrdlink.setUsId(usId);
        }
        updateAcceptByUsId(workbatchOrdlink);//更新接收人员和状态
        System.out.print("===================================4-1");
        //更新排产班次内容信息，接单管理
        WorkbatchShift wshift = workbatchShiftMapper.selectById(ordlinkVO.getWfId());
        if (wshift != null) {
            wshift.setShiftStatus(1);//0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报） 执行生产中的管理操作
            wshift.setStatus("1");
            workbatchShiftMapper.updateById(wshift);
        }

        System.out.print("===================================5");
//        //设置C1缓存信息
//        SuperviseBoxinfo boxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
//        SuperviseExerun exeRunCache = superviseExerunMapper.selectOne(new QueryWrapper<SuperviseExerun>().eq("ma_id", boxinfo.getMaId()));
//        if (exeRunCache != null) {
//            //先删除已存在的缓存
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
//        superviseExerun.setRegular(processMachlink.getKeepRun() == null ? 10 : processMachlink.getKeepRun());//默认规则为正式运行10分钟即为自动正式生产
//        superviseExerunMapper.insert(superviseExerun);//插入执行运行的规则信息内容
        System.out.print("===================================58");
        //防止设备未绑定工序报错ee
        ProcessMachlink processMachlink = processMachlinkMapper.getEntityByPrMt(maId, workbatchOrdlink.getPrId());
        if (processMachlink == null) {
            log.error("设备工序未关联:maId:{}, prId:{}", maId, workbatchOrdlink.getPrId());
            List<ProcessMachlink> list = processMachlinkMapper.selectList(new QueryWrapper<ProcessMachlink>().eq("ma_id", maId));
            if (!list.isEmpty()) {
                ProcessMachlink processMachlink1 = list.get(0);
                processMachlink1.setPrId(workbatchOrdlink.getPrId());
                processMachlink1.setMaId(maId);
                processMachlink1.setPrepareTime(processMachlink1.getPrepareTime());
                processMachlink1.setSpeed(processMachlink1.getSpeed());
                processMachlinkMapper.insert(processMachlink1);
                processMachlink = processMachlink1;
                log.error("设备未与工序关联,存储设备工序关联信息成功:maId:{}, prId:{}", maId, workbatchOrdlink.getPrId());
            }
        }

        System.out.print("===================================6");
        // SendMsgUtils.sendToUser(usIds, JSON.toJSONString(temp), ChatType.MAC_ACCEPTORDER.getType());//机台发通知给登录印聊人员信息
        System.out.print("===================================7");
        //如获取上工序的信息
        WorkbatchOrdlink ordlink = workbatchOrdlinkMapper.selectById(sdId);
        if (ordlink != null) {
            //上工序名称
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
                            //删除入库信息
                            executeTraycardMapper.deleteById(o);
                        });
                    }
                }
            }
        }
        //修改是否接单状态
        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //接单状态
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
        executeInfo.setWfId(newState.getWfId());//设定排产班次id信息
        executeInfo.setStartTime(currDate);
        executeInfo.setCreateAt(currDate);
        executeInfo.setStatus(0);   //设置当前排产的生产状态信息 0、接单1、执行中2、执行完成3、执行结束未上报
        executeInfo.setWsId(ordlinkVO.getWsId());
//        executeInfo.setSfStartTime(ordlinkVO.getSfStartTime());
//        executeInfo.setSfEndTime(ordlinkVO.getSfEndTime());
        executeInfo.setTargetDay(DateUtil.refNowDay());
        executeInfo.setWbNo(workbatchOrdlink.getWbNo()); //TODO WYN去掉工单编号写入执行表 采用关联ordlink表进行查询
        if (ordlinkVO.getUsId() != null)
            executeInfo.setUsId(ordlinkVO.getUsId().toString());
        infoMapper.insert(executeInfo);//插入执行单内容信息
        return executeInfo;
    }

    /****
     * 设定对应的数据信息
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
        executeInfo.setWfId(wbshift.getId());//设定排产班次id信息：wbshift为排产工单信息
        executeInfo.setStartTime(currDate);
        executeInfo.setCreateAt(currDate);
        executeInfo.setStatus(0);   //设置当前排产的生产状态信息 0、接单1、执行中2、执行完成3、执行结束未上报
        executeInfo.setWsId(wsId);//排产班次表信息内容
        executeInfo.setWbNo(wbNo);
//        executeInfo.setSfStartTime(ordlinkVO.getSfStartTime());
//        executeInfo.setSfEndTime(ordlinkVO.getSfEndTime());
        executeInfo.setTargetDay(targetDay);
        executeInfo.setWbNo(wbNo); //TODO WYN去掉工单编号写入执行表 采用关联ordlink表进行查询  关键涉及到yilong的接单用到工单编号标志信息
        if (usId != null)
            executeInfo.setUsId(usId.toString());//修改接单用户信息内容
        infoMapper.insert(executeInfo);//插入执行单内容信息

        return executeInfo;
    }

    private int updateAcceptByUsId(WorkbatchOrdlink ordlink) {
        return workbatchOrdlinkMapper.updateAcceptByUsId(ordlink.getId(), ordlink.getRunStatus());
    }

    /*****
     * 查询班次对应的排产单的内容信息
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
            workbatchShiftMapper.insert(wshift);//如果排产班次表没有生成，就依据排产信息进行排产班次生成数据
        }
        return wshift;
    }


    /**
     * 正式生产订单。更新状态
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
        Integer wsId = ordlinkVO.getWsId();//获取班次表wsId主键信息
        String sdDate = ordlinkVO.getSdDate();//获取排产日期信息
        Integer ordId = null;
        if (StringUtils.isNotBlank(ordlinkVO.getOdNo())) {
            OrderOrdinfo orderOrdinfo = orderOrdinfoMapper.getOrderInfoByodNo(ordlinkVO.getOdNo());
            if (orderOrdinfo != null) {
                //订单id
                ordId = orderOrdinfo.getId();
            }
        }

        //查询订单实时状态
        SuperviseExecute executeOrder = getExecuteOrder(maId);
        Integer exId = executeOrder.getExId();
        ordlinkVO.setInfoId(exId);
        //执行表新增一条状态
        ExecuteState newState = new ExecuteState();
        String usIds = executeOrder.getUsIds();
        newState.setStartAt(currTime);
        newState.setTeamId(usIds);   //当前所有登录人员id
        newState.setMaId(maId);   //设备id
        newState.setSdId(sdId);   //排产id
        newState.setOdId(ordId);  //订单id

        //根据设备及当前时间，判断确认的排产班次表信息
        // WorkbatchShift wshift = getWorkbatchShift(currTime, maId, wsId, sdDate, sdId, ordlinkVO.getPlanNum(), ordlinkVO.getWfId());
        Integer wfId = ordlinkVO.getWfId();
        newState.setWfId(wfId);//增加排产班次id主键
        newState.setWbId(ordlinkVO.getWbId());   //批次id
        newState.setTeamId(executeOrder.getUsIds());
        newState.setUsId(ordlinkVO.getUsId());
        newState.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());   //正式中
        newState.setEvent(GlobalConstant.ProType.PRODUCT_EVENT.getType());    //正式生产状态
        newState.setCreateAt(currTime);
        UpdateStateUtils.updateSupervise(newState, null);//仅仅修改状态无需更新执行单exId信息，置空即可
        //更新排产订单信息
//        ordlinkVO.setStatus(OrderStatusConstant.STATUS_PRODUCTION);
//        ordlinkVO.setRunStatus(OrderStatusConstant.RUN_STATUS_PRODUCTION);
        ordlinkVO.setActuallyStarttime(currTime);
//        List<ExecuteBriefer> executeBrieferList =
//                brieferMapper.selectList(new QueryWrapper<ExecuteBriefer>().eq("wf_id", wfId));
//        Integer completeNum = 0;
//        for (ExecuteBriefer executeBriefer : executeBrieferList) {
//            Integer productNum = executeBriefer.getProductNum();//作业数
//            productNum = productNum == null ? 0 : productNum;
//            completeNum += productNum;
//        }
//        ordlinkVO.setCompleteNum(completeNum);//上报表查询已完成数
        ordlinkVO.setIncompleteNum(ordlinkVO.getPlanNumber());
        ordlinkVO.setPlanNum(ordlinkVO.getPlanNumber());
        ordlinkVO.setStatus("9");
        ordlinkMapper.updateById(ordlinkVO);
        WorkbatchShift wshift = workbatchShiftMapper.selectById(ordlinkVO.getWfId());
        if (wshift != null && wshift.getId() != null) {
            wshift.setShiftStatus(1);//-1:未下发，0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报） 执行生产中的管理操作
            wshift.setStatus("2");//状态（0起草1发布2正在生产3已完成4已挂起5废弃） -1待排产ERP接入 6驳回7已排产8部分完成9未排完
            workbatchShiftMapper.updateById(wshift);
        }

        //实时表新增C1开始时间
        ExecuteInfo executeInfo = infoMapper.selectById(ordlinkVO.getInfoId());
        executeInfo.setStatus(1);//状态：0、接单1、执行中2、执行完成3、执行结束
        executeInfo.setExeTime(currTime);
        infoMapper.updateById(executeInfo);
//        /***
//         * 发送 通知消息
//         */
//        SendMsgUtils.sendToUser(usIds, JSON.toJSONString(temp),
//                ChatType.MAC_PROORDER.getType());
        return R.ok(ordlinkVO);
    }


    /*****
     * 进入正式生产的新的方法
     * @param maId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R proOrderNew(Integer maId) {
        //获取当前设备的状态，如果是生产装备，才能走下面的环节，否则跳出
        SuperviseExecute supexecute = superviseExecuteMapper.getExecuteOrder(maId);

        System.out.println("--------------------->supexecute:::maId:" + maId + ":::::::proOrderNew:");
        if (supexecute != null && supexecute.getExeStatus() != null && !"B".equalsIgnoreCase(supexecute.getExeStatus())) {
            return R.error("设备不是生产准备，请返回.");
        }

        Integer exId = supexecute.getExId();
        Date currTime = new Date();
        //实时表新增C1开始时间
        ExecuteInfo executeInfo = infoMapper.selectById(exId);
        if (executeInfo == null || executeInfo.getId() == null) {
            return R.error("不能查询到当前的执行单信息！");
        }
        executeInfo.setStatus(1);//状态：0、接单1、执行中2、执行完成3、执行结束
        executeInfo.setExeTime(currTime);
        infoMapper.updateById(executeInfo);

        //执行表新增一条状态
        ExecuteState newState = new ExecuteState();
        String usIds = supexecute.getUsIds();
        newState.setStartAt(currTime);
        newState.setTeamId(usIds);   //当前所有登录人员id
        newState.setMaId(maId);   //设备id
        newState.setSdId(executeInfo.getSdId());   //排产id
        newState.setWbId(executeInfo.getWfId());  //订单id

        //根据设备及当前时间，判断确认的排产班次表信息
        // WorkbatchShift wshift = getWorkbatchShift(currTime, maId, wsId, sdDate, sdId, ordlinkVO.getPlanNum(), ordlinkVO.getWfId());
        Integer wfId = executeInfo.getWfId();
        newState.setWfId(wfId);//增加排产班次id主键
//        newState.setWbId(ordlinkVO.getWbId());   //批次id
        newState.setTeamId(usIds);
        String optId = executeInfo.getUsId();
        Integer optIdInt = (optId != null && optId.length() > 0) ? Integer.parseInt(optId) : null;
        newState.setUsId(optIdInt);
        newState.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());   //正式中
        newState.setEvent(GlobalConstant.ProType.PRODUCT_EVENT.getType());    //正式生产状态
        newState.setCreateAt(currTime);
        UpdateStateUtils.updateSupervise(newState, null);//仅仅修改状态无需更新执行单exId信息，置空即可


        WorkbatchShift wshift = workbatchShiftMapper.selectById(wfId);
        if (wshift != null && wshift.getId() != null) {
            wshift.setShiftStatus(1);//-1:未下发，0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报） 执行生产中的管理操作
            wshift.setStatus("2");//状态（0起草1发布2正在生产3已完成4已挂起5废弃） -1待排产ERP接入 6驳回7已排产8部分完成9未排完
            workbatchShiftMapper.updateById(wshift);
        }
        return R.ok("正常执行，进入正式生产环节");
    }

    /**
     * 订单结束生产
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
                exStatus = 3;//默认按流程完成状态
            }
            List<ExecuteBriefer> executeBrieferList =
                    executeBrieferMapper.selectList(new QueryWrapper<ExecuteBriefer>().eq("ex_id", exId));
            if (!executeBrieferList.isEmpty()) {
                return R.ok("该订单已自动结束");
            }
            Date currTime = new Date();
            //获取订单实时状态
            SuperviseExecute executeOrder = getExecuteOrder(maId);
            Integer currNum = executeOrder.getCurrNum();//盒子计数
            //主计划相关
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
            //通过排产id 查询订单id
            ExecuteState newState = getExecuteState(maId, sdId, usId, wfId, currTime, executeOrder, usIds, orderId);
            //更新排产订单信息
            WorkbatchOrdlink ordlink = ordlinkMapper.selectById(sdId);
//        ordlink.setStatus(OrderStatusConstant.STATUS_PRODUCTION);
            if (ordlink != null) {
                ordlink.setRunStatus(OrderStatusConstant.RUN_STATUS_NOREPORT);    //未上报
                ordlinkMapper.updateById(ordlink);
            }

            WorkbatchShift wshift = workbatchShiftMapper.selectById(wfId);
            if (wshift != null) {
                //修改状态的时候需要先判断是否还有别的机台正在生产，如果正在生产就不做状态变化；
                Integer exeByother = getExecuteOrderByother(maId);
                //表示尤其设备还在生产相同的工单，无需修改工单状态
                if (exeByother < 1) {
                    //表示只有它自己需要结束工单状态，
                    wshift.setShiftStatus(OrderStatusConstant.RUN_STATUS_NOREPORT);//未上报3
                    wshift.setStatus(OrderStatusConstant.STATUS_PRODUCTION); //正在生产2
                    workbatchShiftMapper.updateById(wshift);//更新工单的状态信息
                }
            }

            //更新执行单表中的数据
            ExecuteInfo executeInfo = infoMapper.selectById(executeOrder.getExId());
            if (executeInfo != null) {
                executeInfo.setEndTime(currTime);//D1执行完成状态
                executeInfo.setStatus(exStatus);  //当前当次的排产生产完成。//0、接单1、执行中2、执行完成3、执行结束未上报
                infoMapper.updateById(executeInfo);
            }

            /**
             * 执行上报表保存一条数据
             */
            ExecuteBriefer briefer = new ExecuteBriefer();
            briefer.setSdId(sdId);
            briefer.setBoxNum(executeOrder.getCurrNum());
            briefer.setEsId(newState.getId());
            briefer.setWfId(newState.getWfId());//设定排产班次表ID信息
            briefer.setStartTime(executeOrder.getStartTime());  //订单开始时间
            briefer.setEndTime(currTime);
            briefer.setCreateAt(currTime);
            briefer.setHandle(0);   //未处理上报信息
            briefer.setExId(executeInfo.getId());  //插入执行表的主键Id
            briefer.setReadyNum(executeOrder.getReadyNum());
            if (brieferMapper.insert(briefer) <= 0) {
                return R.error("保存执行上报表数据失败");
            }
            try {
                statisMachreachService.createShiftStatisMachreach(briefer.getId(), wshift, ordlink.getMaId(), executeOrder.getCurrNum());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //修改是否接单状态
            SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
            //修改为未接单状态
            superviseBoxinfo.setBlnAccept(0);
            superviseBoxinfo.setWbNo(null);
            superviseBoxinfoMapper.updateById(superviseBoxinfo);
            //修改准备技术为0
            executeOrder.setReadyNum(0);
            executeOrder.setReadyTime(null);
            executeOrder.setEvent(GlobalConstant.ProType.FINISH_EVENT.getType());
            executeOrder.setExeStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());
            executeOrder.setEndTime(currTime);
            superviseExecuteMapper.update(executeOrder);

            if (0 != executeOrder.getCurrNum()) {
                //出库出上工序的
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
     * 订单结束生产
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R finishOrderNew(Integer maId) {
        //, Integer sdId, Integer usId, Integer exId, Integer wfId, Integer exStatus
        //直接进行接单的关键任务，结束该任务操作；
        Integer exStatus = 3; //结束流程完成状态
//        if(exStatus == null){
//            exStatus = 3;//默认按流程完成状态
//        }
        //判断是否该设备已经接单，如果接单之前需要停止前面还未结束的工单。
        SuperviseExecute execute = executeMapper.getExecuteOrder(maId);
        Integer exId = execute.getExId();
        //如果结束在上报表就会有对应的数据信息；
        ExecuteBriefer executeBriefer = executeBrieferMapper.getByExId(exId);
        if (executeBriefer != null && executeBriefer.getId() != null) {
            return R.ok("该订单已自动结束");
        }

        Date currTime = new Date();
        //获取订单实时状态
//        SuperviseExecute executeOrder = getExecuteOrder(maId);

        //更新执行单表中的数据
        ExecuteInfo executeInfo = infoMapper.selectById(exId);
        if (executeInfo == null || executeInfo.getId() == null)
            return R.error("没有改执行单信息");
        Integer sdId = executeInfo.getSdId();
        Integer wfId = executeInfo.getWfId();

//        Integer currNum = execute.getCurrNum();//盒子计数

//        String usIds = execute.getUsIds();
//        Integer orderId = null;

        //通过排产id 查询订单id??  是否需要补充执行表数据信息；
//        ExecuteState newState = getExecuteState(maId, sdId, usId, wfId, currTime, execute, usIds, null);
        //更新排产订单信息
//        WorkbatchOrdlink ordlink = ordlinkMapper.selectById(sdId);
//        ordlink.setStatus(OrderStatusConstant.STATUS_PRODUCTION);
//        if (ordlink != null) {
//            ordlink.setRunStatus(OrderStatusConstant.RUN_STATUS_NOREPORT);    //未上报
//            ordlinkMapper.updateById(ordlink);
//        }


        //更新排产shift表中信息状态
        WorkbatchShift wshift = workbatchShiftMapper.selectById(wfId);
        if (wshift != null) {
            //修改状态的时候需要先判断是否还有别的机台正在生产，如果正在生产就不做状态变化；
            Integer exeByother = getExecuteOrderByother(maId);
            //表示尤其设备还在生产相同的工单，无需修改工单状态
            if (exeByother < 1) {
                //表示只有它自己需要结束工单状态，
                wshift.setShiftStatus(OrderStatusConstant.RUN_STATUS_NOREPORT);//未上报3
                wshift.setStatus(OrderStatusConstant.STATUS_PRODUCTION); //正在生产2
                workbatchShiftMapper.updateById(wshift);//更新工单的状态信息
            }
        }

        //更新执行单的状态信息
        if (executeInfo != null) {
            executeInfo.setEndTime(currTime);//D1执行完成状态
            executeInfo.setStatus(exStatus);  //当前当次的排产生产完成。//0、接单1、执行中2、执行完成3、执行结束未上报
            infoMapper.updateById(executeInfo);
        }

        /**
         * 执行上报表保存一条数据
         */
        ExecuteBriefer briefer = new ExecuteBriefer();
        briefer.setSdId(sdId);
        briefer.setBoxNum(execute.getCurrNum());
//        briefer.setEsId(newState.getId());
        briefer.setWfId(wfId);//设定排产班次表ID信息
        briefer.setStartTime(execute.getStartTime());  //订单开始时间
        briefer.setEndTime(currTime);
        briefer.setCreateAt(currTime);
        briefer.setHandle(0);   //未处理上报信息
        briefer.setExId(exId);  //插入执行表的主键Id
        briefer.setReadyNum(execute.getReadyNum());
        //新增上报表信息
        if (brieferMapper.insert(briefer) <= 0) {
            return R.error("保存执行上报表数据失败");
        }

        try {
            //结束工单的达成率设定
            statisMachreachService.createShiftStatisMachreach(briefer.getId(), wshift, maId, execute.getCurrNum());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //修改是否接单状态
        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //修改为未接单状态
        superviseBoxinfo.setBlnAccept(0);
        superviseBoxinfo.setWbNo(null);
        superviseBoxinfoMapper.updateById(superviseBoxinfo);
        //修改准备技术为0
        execute.setReadyNum(0);
        execute.setReadyTime(null);
        execute.setEvent(GlobalConstant.ProType.FINISH_EVENT.getType());//结束D1结束事件
        execute.setExeStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());//生产结束后D
        execute.setEndTime(currTime);
        //结束清空数据
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
        newState.setTeamId(usIds);   //当前所有登录人员id
        newState.setMaId(maId);   //设备id
        newState.setSdId(sdId);   //排产id
        newState.setWfId(wfId);//增加排产班次id主键

        newState.setOdId(orderId); //订单id
        newState.setUsId(usId);

        newState.setWbId(executeOrder.getWbId());   //批次id
        newState.setStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());   //结束生产状态
        newState.setEvent(GlobalConstant.ProType.FINISH_EVENT.getType());     //结束生产状态
        newState.setStartAt(currTime);
        newState.setCreateAt(currTime);
        //更新订单实时状态表，修改执行表，新增执行表数据
        UpdateStateUtils.updateSupervise(newState, null);//结束生产，无需更新执行单，除非为B1再次接单，才会操作
        return newState;
    }

    private void updateWorkbatchProgress(Integer currNum, List<WorkbatchProgress> gressList) {
        currNum = currNum == null ? 0 : currNum;
        WorkbatchProgress workbatchProgress = gressList.get(0);
        String wbNo = workbatchProgress.getWbNo();//工单编号
        String ptName = workbatchProgress.getPtName();//部件名称
        /*查询部件*/
        List<WorkbatchProgress> workbatchProgressList = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                .eq("wb_no", wbNo).eq("pt_name", ptName).eq("wp_type", 2));
        /*查询编号*/
        List<WorkbatchProgress> progressList =
                workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("wb_no", wbNo).eq("wp_type", 1));

        Integer planCount = workbatchProgress.getPlanCount();//计划总数
        Double rate = 0.0;
        if (planCount != null || planCount != 0) {
            rate = currNum / (double) planCount;
        }
        Integer totalTime = workbatchProgress.getTotalTime();//计划总时长
        totalTime = totalTime == null ? 0 : totalTime;
        Integer time = totalTime - (int) (totalTime * rate);//剩余时长
        workbatchProgress.setStayTime(time);//剩余时长
        workbatchProgress.setStatus(3);
        workbatchProgress.setRealCount(currNum);//完成盒子数
        workbatchProgress.setUpdateAt(new Date());

        if (!workbatchProgressList.isEmpty()) {
            WorkbatchProgress progress = workbatchProgressList.get(0);
            Integer realCount = progress.getRealCount();//已完成数
            realCount = realCount == null ? 0 : realCount;
            realCount += currNum;
            progress.setRealCount(realCount);
            progress.setUpdateAt(new Date());
            workbatchProgressMapper.updateById(progress);
        }
        if (!progressList.isEmpty()) {
            WorkbatchProgress progress = progressList.get(0);
            Integer realCount = progress.getRealCount();//已完成数
            realCount = realCount == null ? 0 : realCount;
            realCount += currNum;
            progress.setRealCount(realCount);
            progress.setUpdateAt(new Date());
            workbatchProgressMapper.updateById(progress);
        }
        workbatchProgressMapper.updateById(workbatchProgress);
    }

    /**
     * 保存实时达成率
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
        int targetHour = calendar.get(Calendar.HOUR_OF_DAY); // 时（24小时制）
        int targetMin = calendar.get(Calendar.MINUTE); // 分
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
        //获取设备信息
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
        //实时达成率
        StatisOrdreach statisOrdreach = new StatisOrdreach();
        statisOrdreach.setTargetDay(targetDay);
        statisOrdreach.setTargetHour(targetHour);
        statisOrdreach.setTargetMin(targetMin);
        statisOrdreach.setMaId(maId);
        statisOrdreach.setMaName(machineMainfo.getName());
        statisOrdreach.setPdName(ordlink.getPdName());
        //statisOrdreach.setSdId(ordlink.getId().toString());
        statisOrdreach.setExId(executeInfo.getId());
        //获取oee信息
        WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.getOeeBySdId(sdId);
        //获取实际生产数
        Integer currNum = executeOrder.getCurrNum();
        //转为每分生产数
        BigDecimal planSpeed = BigDecimal.valueOf(workbatchOrdoee.getSpeed() / 60).setScale(2);
        BigDecimal prePlanTime = BigDecimal.valueOf(workbatchOrdoee.getPlanTotalTime());
        //计算标准数量
        BigDecimal planNum = prePlanTime.multiply(planSpeed);
        //计算达成率
        BigDecimal reachRate = BigDecimal.ZERO;
        try {
            reachRate = BigDecimal.valueOf(currNum).divide(planNum, 2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            log.error("结束订单生成达成率，当前生产数为0:[maId:{}, sdId:{}]", maId, sdId);
        }
        statisOrdreach.setReachRate(reachRate);
        statisOrdreach.setPlanNum(planNum.toString());
        statisOrdreach.setRealCount(currNum);
        statisOrdreach.setWbNo(ordlink.getWbNo());
        //获取班次信息
//        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(ordlink.getWsId());
        WorkbatchShiftset workbatchShift = workbatchShiftsetMapper.selectByMaid(ordlink.getWsId(), ordlink.getMaId());
        statisOrdreach.setWsId(workbatchShift.getId());
        statisOrdreach.setWsName(workbatchShift.getCkName());
        statisOrdreach.setCreateAt(currTime);

        statisOrdreachMapper.insert(statisOrdreach);
    }


    /**
     * 获取订单实时的计数
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
     * 获取订单名称
     *
     * @param sdId
     * @return
     */
    @Override
    public String getOrderName(Integer sdId) {
        return ordlinkMapper.getOrderName(sdId);
    }

    /**
     * 获取未上报的订单
     *
     * @return
     */
    @Override
    public List<WorkbatchOrdlinkVO> getNoReportOrder(Integer maId) {
        List<WorkbatchOrdlinkVO> list = null;
        //判断按照工序进行判断来获取对应数据内容
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
        if (machineMainfo != null && machineMainfo.getIsRecepro() == 1) {
            list = ordlinkMapper.getNoReportOrderByIsRecepro(maId);
        } else {
            /**
             * 查询为上报的工单id
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
                ordlink1.setPtName(ordlink1.getPartName());//部件名称
                ordlink1.setTrayTotalNum(trayTotalNum);
            }
        }

        return list;
    }


    /**
     * 获取未上报的订单【新增】
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
                /*查询排产单的车间*/
                BaseDeptinfo baseDeptinfo = baseDeptinfoMapper.selectById(workbatchOrdlinkVO.getDpId());
                workbatchOrdlinkVO.setDpName(baseDeptinfo.getDpName());//车间名称
                /*查询排产oee信息*/
                WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.selectOne(new QueryWrapper<WorkbatchOrdoee>().eq("wk_id", workbatchOrdlinkVO.getId()));
                if (workbatchOrdoee != null) {
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("wk_oee_id", workbatchOrdoee.getId());
                    /*查询排产oee保养时间区间*/
                    List workbatchordoeeMaintainList = workbatchordoeeMaintainMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMaintainList(workbatchordoeeMaintainList);
                    /*查询排产oee吃饭时间区间*/
                    List workbatchordoeeMealList = workbatchordoeeMealMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMealList(workbatchordoeeMealList);
                    /*查询排产oee换膜区间*/
                    List workbatchordoeeMouldList = workbatchordoeeMouldMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMouldList(workbatchordoeeMouldList);
                    /*查询排产oee数据*/
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
        /*查询部门下全部设备上的排产记录*/
        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList = ordlinkMapper.getDeptMachineAll(startTime, dpId, ckName);
        if (!workbatchOrdlinkVOList.isEmpty()) {
            for (WorkbatchOrdlinkVO workbatchOrdlinkVO : workbatchOrdlinkVOList) {
                /*查询排产单的车间*/
                BaseDeptinfo baseDeptinfo = baseDeptinfoMapper.selectById(workbatchOrdlinkVO.getDpId());
                workbatchOrdlinkVO.setDpName(baseDeptinfo.getDpName());//车间名称
                /*查询排产oee信息*/
                WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.selectOne(new QueryWrapper<WorkbatchOrdoee>().eq("wk_id", workbatchOrdlinkVO.getId()));
                if (workbatchOrdoee != null) {
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("wk_oee_id", workbatchOrdoee.getId());
                    /*查询排产oee保养时间区间*/
                    List workbatchordoeeMaintainList = workbatchordoeeMaintainMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMaintainList(workbatchordoeeMaintainList);
                    /*查询排产oee吃饭时间区间*/
                    List workbatchordoeeMealList = workbatchordoeeMealMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMealList(workbatchordoeeMealList);
                    /*查询排产oee换膜区间*/
                    List workbatchordoeeMouldList = workbatchordoeeMouldMapper.selectList(queryWrapper);
                    workbatchOrdlinkVO.setWorkbatchordoeeMouldList(workbatchordoeeMouldList);
                    /*查询排产oee数据*/
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

    /*新增排产和排产oee数据*/
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
        workbatchOrdlink.setCmName(cmName);//客户信息
        Integer ptId = workbatchOrdlink.getPtId();//部件id
        /*查询部件尺寸*/
        List<MaterProdlink> materProdlinkList =
                materProdlinkMapper.selectList(new QueryWrapper<MaterProdlink>().eq("pt_id", ptId));
        if (!materProdlinkList.isEmpty()) {
            MaterProdlink materProdlink = materProdlinkList.get(0);//不确定关系,取第一个
            workbatchOrdlink.setPtSize(materProdlink.getSize());//设置排产表里的部件尺寸
        }
        SimpleDateFormat sdfSdDate = new SimpleDateFormat("yyyy-MM-dd");
        workbatchOrdlink.setSdDate(sdfSdDate.format(workbatchOrdlink.getStartTime()));//排产日期
        /*查询产品信息*/
        ProdPdinfoVO prodPdinfoVOByWbId = prodPdinfoMapper.getProdPdinfoVOByWbId(workbatchOrdlink.getWbId());
        /*产品编号*/
        workbatchOrdlink.setPdCode(prodPdinfoVOByWbId.getPdNo());
        /*产品图片*/
        workbatchOrdlink.setPdImageurl(prodPdinfoVOByWbId.getImageUrl());
        /*产品名称*/
        workbatchOrdlink.setPdName(prodPdinfoVOByWbId.getPdName());
        /*产品类型*/
        workbatchOrdlink.setPdType(prodPdinfoVOByWbId.getClName());
        /*查询对应工序信息*/
        Integer prId = workbatchOrdlink.getPrId();
        ProcessWorkinfo processWorkinfo = processWorkinfoMapper.selectById(prId);
        OrderWorkbatch orderWorkbatch = orderWorkbatchMapper.selectById(workbatchOrdlink.getWbId());
        workbatchOrdlink.setWbNo(orderWorkbatch.getBatchNo());//批次编号
        /*查询部件对应的所有工序*/
        List<ProdProcelinkVO> prodProcelinkVOList = prodProcelinkMapper.select(ptId);
        if (!prodProcelinkVOList.isEmpty()) {
            for (int i = 0; i < prodProcelinkVOList.size(); i++) {
                ProdProcelinkVO prodProcelinkVO = prodProcelinkVOList.get(i);
                if (prId.equals(prodProcelinkVO.getId())) {
                    if (i > 0) {
                        ProdProcelinkVO procelinkVO = prodProcelinkVOList.get(i - 1);
                        String upPorcess = procelinkVO.getPrName();
//                        Integer sortNum = procelinkVO.getSortNum();
                        workbatchOrdlink.setUpPorcess(upPorcess);//上工序名称
//                        workbatchOrdlink.setUpprocessSort(sortNum);//上工序排序
                    }
                    if (i < prodProcelinkVOList.size() - 1) {
                        String downPorcess = prodProcelinkVOList.get(i + 1).getPrName();
                        workbatchOrdlink.setDownPorcess(downPorcess);//下工序名称
                    }
                }
            }
        }
        /*工序排序*/
        workbatchOrdlink.setSort(processWorkinfo.getSort());
        /*获取对应部件信息*/
        ProdPartsinfo prodPartsinfo = prodPartsinfoMapper.selectById(workbatchOrdlink.getPtId());
        /*部件编号*/
        workbatchOrdlink.setPtNo(prodPartsinfo.getPtNo());
        /*部件名称*/
        workbatchOrdlink.setPartName(prodPartsinfo.getPtName());
        /*应交数量*/
        Integer planNum = workbatchOrdlink.getPlanNumber() + workbatchOrdlink.getExtraNum();
        workbatchOrdlink.setPlanNum(planNum);
        /*未完成数*/
        workbatchOrdlink.setIncompleteNum(planNum);
        Date startTime = workbatchOrdlink.getStartTime();

      /*  Integer sdSort = ordlinkMapper.getSdSort();
        if (sdSort == null) {
            sdSort = 1;
        } else {
            sdSort++;
        }*/
//        workbatchOrdlink.setSdSort(String.valueOf(sdSort));
        if (workbatchOrdlink.getCloseTime() != null) {  //如果截止时间不为空则使用排产员输入的截止时间
            closeTime = workbatchOrdlink.getCloseTime();
            Long diff = closeTime.getTime() - startTime.getTime();  //这样得到的差值是微秒级别
            Long minutes = diff / (1000 * 60);  //分
            /*计划用时*/
            workbatchOrdlink.setPlanTime(minutes.intValue());
        } else {//如果截止时间为空,则根据计划数量和生产速度计算计划用时,算出截止时间
            Integer speed = 3500;//设备默认转速
            if (workbatchOrdlink.getMaId() != null) {
                //MachineClassify machineClassify = machineClassifyMapper.selectspeedByMaId(workbatchOrdlink.getMaId());
                ProcessMachlink processMachlink = processMachlinkMapper.selectOne(new QueryWrapper<ProcessMachlink>()
                        .eq("pr_id", workbatchOrdlink.getPrId()).eq("ma_id", workbatchOrdlink.getMaId()));
                speed = processMachlink.getSpeed();//单位:张/h
            }

            long planTime = (workbatchOrdlink.getPlanNum() * 60) / speed + workbatchOrdoeeVO.getProducePreTime();//分钟
            /*设置计划用时*/
            workbatchOrdlink.setPlanTime(Long.valueOf(planTime).intValue());
            long planHS = planTime * 1000 * 60;//毫秒
            long closeho = startTime.getTime() + planHS;//计划开始时间+计划用时
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
        /*班次信息*/
        WorkbatchShift workbatchShift = new WorkbatchShift();
        List<WorkbatchShiftset> workbatchShiftsets = workbatchShiftsetMapper.selectList(new QueryWrapper<>());
        for (WorkbatchShiftset workbatchShiftset : workbatchShiftsets) {
            if (workbatchOrdlink.getCkName().equals(workbatchShiftset.getCkName())) {//传入班次名称对应数据字典查出的班次名称找到班次的时间区间
                workbatchShift.setWsId(workbatchShiftset.getId());
                workbatchShift.setStayTime(workbatchShiftset.getStayTime());//班次等待时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //获取班次设定开始时间
                Date shiftsetStartTime = workbatchShiftset.getStartTime();
                //获取班次设定结束时间
                Date shiftsetEndTime = workbatchShiftset.getEndTime();
                //获取排产日期
//                Date ordlinkDate = workbatchOrdlink.getStartTime();
                String sdDate = workbatchOrdlink.getSdDate();
                String ckStartTime = sdf.format(shiftsetStartTime);
                String ckEndTime = sdf.format(shiftsetEndTime);
                //排产班次对应日期
                ckStartTime = sdDate + ckStartTime;
                ckEndTime = shiftsetStartTime + ckEndTime;
                try {
                    Date ckStartDate = sdf.parse(ckStartTime);
                    Date ckEndDate = sdf.parse(ckEndTime);
                    if (ckStartDate.getTime() > ckEndDate.getTime()) {
                        long h = ckEndDate.getTime() + 24 * 60 * 60 * 1000;//加一天
                        ckEndDate = new Date(h);
                    }
                    //设置排产班次开始时间
                    workbatchShift.setStartTime(ckStartDate);
                    //设置排产班次结束时间
                    workbatchShift.setEndTime(ckEndDate);
                    break;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        /*班次名称*/
        workbatchShift.setCkName(workbatchOrdlink.getCkName());
        workbatchShift.setCreateAt(new Date());
        /*新增排产班次信息*/
        int Shift = workbatchShiftMapper.insert(workbatchShift);

        /*班次id*/
        workbatchOrdlink.setWsId(workbatchShift.getId());
        /*新增排产信息*/
        int ordlink = ordlinkMapper.insert(workbatchOrdlink);
        /*排产oee数据*/
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
        workbatchOrdoee.setSpeed(speed);//设备转速
        workbatchOrdoee.setWkId(workbatchOrdlink.getId());//排产id
        workbatchOrdoee.setBeforePtid(workbatchOrdlink.getPtId());
        workbatchOrdoee.setBeforePtno(prodPartsinfo.getPtNo());//部件编号
        workbatchOrdoee.setBeforePtname(prodPartsinfo.getPtName());//部件名称
        workbatchOrdoee.setDifficultNum(workbatchOrdoeeVO.getDifficultNum());//难易程度
//        workbatchOrdoee.setMaintainNum(workbatchOrdoeeVO.getMaintainNum());//保养次数
//        workbatchOrdoee.setMaintain(workbatchOrdoeeVO.getMaintain());//保养类型字典：1日保2周保3月保4季保5半年保6年保
//        workbatchOrdoee.setMaintainStay(workbatchOrdoeeVO.getMaintainStay());//保养时长（分）
//        workbatchOrdoee.setMealNum(workbatchOrdoeeVO.getMealNum());//吃饭次数
//        workbatchOrdoee.setMealStay(workbatchOrdoeeVO.getMealStay());//吃饭时长
        workbatchOrdoee.setMouldStay(workbatchOrdoeeVO.getMouldStay());//换模换模时长（分）时长（分）
        workbatchOrdoee.setProducePreTime(workbatchOrdoeeVO.getProducePreTime());//生产准备时间
        workbatchOrdoee.setMouldNum(workbatchOrdoeeVO.getMouldNum());//换模次数
//        workbatchOrdoee.setQualityNum(workbatchOrdoeeVO.getQualityNum());//质检次数
        /*新增排产oee数据*/
        int Ordoee = workbatchOrdoeeMapper.insert(workbatchOrdoee);
        /*Integer oeeId = workbatchOrdoee.getId();
        List<WorkbatchordoeeMaintain> workbatchordoeeMaintains = workbatchOrdoeeVO.getWorkbatchordoeeMaintainList();
        List<WorkbatchordoeeMeal> workbatchordoeeMeals = workbatchOrdoeeVO.getWorkbatchordoeeMealList();
        List<WorkbatchordoeeMould> workbatchordoeeMoulds = workbatchOrdoeeVO.getWorkbatchordoeeMouldList();
        if (!workbatchordoeeMaintains.isEmpty()) {
            for (WorkbatchordoeeMaintain workbatchordoeeMaintain : workbatchordoeeMaintains) {
                workbatchordoeeMaintain.setWkOeeId(oeeId);
                *//*新增生产排产OEE保养时间区间*//*
                workbatchordoeeMaintainMapper.insert(workbatchordoeeMaintain);

            }
        }
        if (!workbatchordoeeMeals.isEmpty()) {
            for (WorkbatchordoeeMeal workbatchordoeeMeal : workbatchordoeeMeals) {
                workbatchordoeeMeal.setWkOeeId(oeeId);
                *//*新增生产排产OEE吃饭时间区间*//*
                workbatchordoeeMealMapper.insert(workbatchordoeeMeal);
            }
        }
        if (!workbatchordoeeMeals.isEmpty()) {
            for (WorkbatchordoeeMould workbatchordoeeMould : workbatchordoeeMoulds) {
                workbatchordoeeMould.setWkOeeId(oeeId);
                *//*生产排产OEE换膜时间区间*//*
                workbatchordoeeMouldMapper.insert(workbatchordoeeMould);
            }
        }*/
        return workbatchOrdlink.getId();
    }

    /**
     * 查询未提交自审核或审核失败的排产数据
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
        /*修改排产数据*/
        workbatchOrdlink.setUpdateAt(new Date());
//        Integer PlanNumber = workbatchOrdlink.getPlanNum() - workbatchOrdlink.getExtraNum();
//        workbatchOrdlink.setPlanNumber(PlanNumber);//修改应交数量
        Integer planNum = workbatchOrdlink.getPlanNumber() + workbatchOrdlink.getExtraNum();
        workbatchOrdlink.setPlanNum(planNum);
        workbatchOrdlink.setIncompleteNum(planNum);
        workbatchOrdlink.setStatus("0");
        int updateOrdlink = ordlinkMapper.updateById(workbatchOrdlink);
        /*修改排产oee数据*/
        int updateOee = workbatchOrdoeeMapper.updateById(workbatchOrdoeeVO);

        List<WorkbatchordoeeMaintain> workbatchordoeeMaintains = workbatchOrdoeeVO.getWorkbatchordoeeMaintainList();
        List<WorkbatchordoeeMeal> workbatchordoeeMeals = workbatchOrdoeeVO.getWorkbatchordoeeMealList();
        List<WorkbatchordoeeMould> workbatchordoeeMoulds = workbatchOrdoeeVO.getWorkbatchordoeeMouldList();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("wk_oee_id", workbatchOrdoeeVO.getId());
        /*删除排产oee保养时间区间*/
        workbatchordoeeMaintainMapper.delete(queryWrapper);
        /*删除排产oee吃饭时间区间*/
        workbatchordoeeMealMapper.delete(queryWrapper);
        /*删除排产oee换膜区间*/
        workbatchordoeeMouldMapper.delete(queryWrapper);
        for (WorkbatchordoeeMaintain workbatchordoeeMaintain : workbatchordoeeMaintains) {
            workbatchordoeeMaintain.setWkOeeId(workbatchOrdoeeVO.getId());
            /*新增保养时间段*/
            workbatchordoeeMaintainMapper.insert(workbatchordoeeMaintain);

        }

        for (WorkbatchordoeeMeal workbatchordoeeMeal : workbatchordoeeMeals) {
               /* if(workbatchordoeeMeal.getId() == null){
                    workbatchordoeeMeal.setWkOeeId(workbatchOrdoeeVO.getId());
                    workbatchordoeeMealMapper.insert(workbatchordoeeMeal);
                    continue;
                }
                *//*修改生产排产OEE吃饭时间区间*//*
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
                *//*生产排产OEE换膜时间区间*//*
                workbatchordoeeMouldMapper.updateById(workbatchordoeeMould);*/
            workbatchordoeeMould.setWkOeeId(workbatchOrdoeeVO.getId());
            workbatchordoeeMouldMapper.insert(workbatchordoeeMould);
        }

        WorkbatchOrdlinkShiftVO ordlink = ordlinkMapper.getOrdshiftById(workbatchOrdlink.getId());
        //查询排产对应班次
        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(ordlink.getWsId());
//        WorkbatchShift workbatchShift = workbatchShiftsetMapper.selectByMaid(ordlink.getWsId(),ordlink.getMaId());
        if (!workbatchShift.getCkName().equals(workbatchOrdlink.getCkName())) {//如果传入的班次名称和查询出的班次名称不一致,则修改班次信息
            workbatchShift.setCkName(workbatchOrdlink.getCkName());
            /*修改排产对应班次*/
            List<WorkbatchShiftset> workbatchShiftsets = workbatchShiftsetMapper.selectList(new QueryWrapper<>());
            for (WorkbatchShiftset workbatchShiftset : workbatchShiftsets) {
                if (workbatchOrdlink.getCkName().equals(workbatchShiftset.getCkName())) {//数据字典查出班次名称,查询班次名称对应的班次时间段
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //班次开始时间
                    Date ckStartTime = workbatchShiftset.getStartTime();
                    //班次结束时间
                    Date ckEndTime = workbatchShiftset.getEndTime();
                    //排产日期
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
                        //设置排产班次开始时间
                        workbatchShift.setStartTime(ckStartDate);
                        //设置排产结束时间
                        workbatchShift.setEndTime(ckEndDate);
                        break;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            //修改排产班次信息
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
        /*查询排产oee保养时间区间*/
        List workbatchordoeeMaintainList = workbatchordoeeMaintainMapper.selectList(queryWrapper);
        /*查询排产oee吃饭时间区间*/
        List workbatchordoeeMealList = workbatchordoeeMealMapper.selectList(queryWrapper);
        /*查询排产oee换膜区间*/
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
        String fileXLS = filePath + "排产单.xls";*/
       /* File imagesPath = new File(path);
        if (!imagesPath.exists()) {//判断文件路径是否存在
            imagesPath.mkdirs();//不存在创建新的文件
        }*/
        /*File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }*/
        // 模板位置，输出流
        //
        String templatePath = "model/WorkbatchOrdlinkModel.xls";
        //      String templatePath =this.getClass().getClassLoader().getResource("model/WorkbatchOrdlinkModel.xls").getPath();
        //       String templatePath = ClassUtils.getDefaultClassLoader().getResource("model/WorkbatchOrdlinkModel.xls").getPath();
//        OutputStream os = null;
        InputStream imageInputStream = null;//条形码
        BufferedOutputStream bos = null;//导出到网页
        List<Integer> indexList = new ArrayList<>();
        List<WorkbatchOrdlinkShiftVO> workbatchOrdlinkList = workbatchOrdlinkMapper.selectBatchIds(ids);
        List<WorkbatchOrdlinkExcelVO> workbatchOrdlinkExcelVOList = new ArrayList<>();
        WorkbatchOrdlinkWrapper workbatchOrdlinkWrapper = new WorkbatchOrdlinkWrapper();
        for (int i = 0; i < workbatchOrdlinkList.size(); i++) {//重复数据
            int count = 0;
            if (!indexList.isEmpty()) {
                Boolean whether = false;
                for (Integer index : indexList) {
                    whether = index.equals(i);
                    if (whether) {
                        break;
                    }
                }
                if (whether) {//重复数据
                    continue;
                }
            }
            WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkList.get(i);
            WorkbatchOrdlinkExcelVO workbatchOrdlinkExcelVO = new WorkbatchOrdlinkExcelVO();
            Integer prId = workbatchOrdlink.getPrId();
            workbatchOrdlinkExcelVO.setPrId(prId);//工序id
            workbatchOrdlinkExcelVO.setPrName(workbatchOrdlink.getPrName());//工序名称
            workbatchOrdlinkExcelVO.setSdDate(workbatchOrdlink.getSdDate());//排产日期
            /*车间信息*/
            BaseDeptinfo baseDeptinfo = baseDeptinfoMapper.selectById(workbatchOrdlink.getDpId());
            workbatchOrdlinkExcelVO.setDpName(baseDeptinfo.getDpName());//车间名称
            /*设备信息*/
            List<MachineMainfoVO> machineMainfoVOList = processMachlinkMapper.machineListByPrIdDeptId(prId, workbatchOrdlink.getDpId());
            workbatchOrdlinkExcelVO.setMachineMainfoVOList(machineMainfoVOList);//设备信息

            List<WorkbatchOrdlinkVO> workbatchOrdlinkVOS = new ArrayList<>();//排产信息
            for (int j = 0; j < workbatchOrdlinkList.size(); j++) {
                WorkbatchOrdlinkShiftVO ordlink = workbatchOrdlinkList.get(j);
                if (workbatchOrdlink.getPrId().equals(ordlink.getPrId())
                        && workbatchOrdlink.getSdDate().equals(ordlink.getSdDate())
                        && workbatchOrdlink.getDpId().equals(ordlink.getDpId())) {
                    count++;
                    WorkbatchOrdlinkVO workbatchOrdlinkVO = workbatchOrdlinkWrapper.entityVO(ordlink);
                    /*班次信息*/
//                    WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(ordlink.getWsId());
                    WorkbatchShiftset workbatchShift = workbatchShiftsetMapper.selectByMaid(ordlink.getWsId(), ordlink.getMaId());
                    if (workbatchShift != null) {//判断班次是否为空,
                        //WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(workbatchShift.getWsId());
                        if (count != 1) {
                            workbatchOrdlinkVO.setMealStay("X");//吃饭时间
                        } else {
                            workbatchOrdlinkVO.setCkName(workbatchShift.getCkName());//班次名称
                            workbatchOrdlinkVO.setMealStay(workbatchShift.getMealStay() + "/班");//吃饭时间
                        }
                        if (j != 0) {
                            Integer wsId = workbatchOrdlinkList.get(j - 1).getWsId();
                            //WorkbatchShift Shift = workbatchShiftMapper.selectById(wsId);//修改班次内容信息
                            WorkbatchShiftset Shift = workbatchShiftsetMapper.selectByMaid(wsId, workbatchOrdlinkList.get(j - 1).getMaId());
                            if (!Shift.getCkName().equals(workbatchShift.getCkName())) {
                                workbatchOrdlinkVO.setMealStay(workbatchShift.getMealStay() + "/班");//吃饭时间
                                workbatchOrdlinkVO.setCkName(workbatchShift.getCkName());//班次名称
                            }
                        }
                    }

//                    String pathlast = ordlink.getId() + ".png";
//                    barCodeUtil.getBarCode(workbatchOrdlinkVO.getId().toString(), path + pathlast);//生成条形码
                    byte[] result = barCodeUtil.generate(workbatchOrdlinkVO.getId().toString());

                    // 文件流，输入一张图片
                    try {
                        //    imageInputStream = new FileInputStream(path + pathlast);
                        // 使用工具方法把流转成byte数组
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
        List<Page> page = individual(workbatchOrdlinkExcelVOList); // 一张表一个对象数据
        Map<String, Object> model = new HashMap<String, Object>();

        model.put("pages", page);
        model.put("sheetNames", getSheetName(page));
        model.put("slName", getSheetName(page));
        try {
//            os = new FileOutputStream(fileXLS);
            bos = ExportlUtil.getBufferedOutputStream("排产单.xls", response);//返回前端处理
//            JxlsUtils.exportExcel(templatePath, os, model);//导入到电脑;
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//返回给前端
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
     * 将数据获取的数据封装成一页一个人的List
     */
    public static List<Page> individual(List<WorkbatchOrdlinkExcelVO> list) {
        List<Page> pages = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Page p = new Page();
            p.setOnlyOne(list.get(i));
//            p.setSheetName(list.get(i).getDpName()+"-"+list.get(i).getSfName()+"-"+(i+1));
            //设置sheet名称
            p.setSheetName(list.get(i).getSdDate() + list.get(i).getPrName());
            pages.add(p);
        }

        return pages;
    }

    /**
     * Excel 的分页名（页码）的封装
     * 此方法用来获取分好页的页名信息，将信息放入一个链表中返回
     */
    public static ArrayList<String> getSheetName(List<Page> page) {
        ArrayList<String> al = new ArrayList<>();
        for (int i = 0; i < page.size(); i++) {
            al.add(page.get(i).getSheetName());
        }
        return al;
    }


    /**
     * 根据排产工单id 获取订单实时状态表的id
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
        WorkbatchOrdlink workbatchOrdlink;//排产对象
        WorkbatchOrdoee workbatchOrdoee;//排产oee对象
        WorkbatchShift workbatchShift;//排产班次对象
        Date date = new Date();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ckTime = simpleDateFormat2.format(date);
        String sdDate = simpleDateFormat1.format(date);
        /*-----------排产班次表---------*/
        List<WorkbatchShiftset> workbatchShiftsetList = workbatchShiftsetMapper.selectList(new QueryWrapper<WorkbatchShiftset>()
                .le("start_date", sdDate).ge("end_date", sdDate));
        /*查询工序信息*/
//        List<ProcessWorkinfo> processWorkinfoList = processWorkinfoMapper.selectList(new QueryWrapper<>());
        /*查询所有产品分类*/
        List<ProdClassify> prodClassifyList = prodClassifyMapper.selectList(new QueryWrapper<>());
        /*查询所有设备*/
        //List<MachineMainfo> machineMainfoList = machineMainfoMapper.selectList(new QueryWrapper<>());
        if (!workbatchOrdlinkYSList.isEmpty()) {
            while (iterator.hasNext()) {//校验传过来的数据
                WorkbatchOrdlinkYS workbatchOrdlinkYS = iterator.next();
                /*获取工序相关信息*/
                String specification = workbatchOrdlinkYS.getSpecification();//上机规格
                if (StringUtil.isEmpty(specification)) {
                    return "产品规格不能为空";
                }
                Integer maId = workbatchOrdlinkYS.getMaId();//设备id
                if (maId == null || maId == 0) {
                    return "设备id不能为空或0";
                }

                Map ruleMap = new HashMap<>();
                ruleMap.put("ma_id", maId);
                Integer material = workbatchOrdlinkYS.getMaterial();
                Integer size = workbatchOrdlinkYS.getSize();
                Integer planNum = workbatchOrdlinkYS.getPlanNum();//计划数(作业数)
                /*查询标准产能*/
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
                    return "产品尺寸表数据不存在";
                }
                Integer pcId = ruleProdoee.getPcId();//工序分类id
                if (pcId == null || pcId == 0) {
                    return "产品规格表产品分类id不存在";
                }
                ProdClassify prodClassify = null;
                for (ProdClassify classify : prodClassifyList) {
                    if (classify.getId().equals(pcId)) {
                        prodClassify = classify;
                    }
                }
                if (prodClassify == null) {
                    return "产品分类不存在";
                }
//               Integer prId = ruleProdoee.getPrId();//工序id

                /*ProcessWorkinfo processWorkinfo = null;
                for (ProcessWorkinfo processWorkin : processWorkinfoList) {
                    if (processWorkin.getId().equals(prId)) {
                        processWorkinfo = processWorkin;
                    }
                }
                if (processWorkinfo == null) {
                    return "工序不存在";
                }*/
                Integer wbId = workbatchOrdlinkYS.getWbId();//批次id
                Integer ptId = workbatchOrdlinkYS.getPtId();//部件id
                String ptName = workbatchOrdlinkYS.getPtName();//部件名称
                Integer planTime = workbatchOrdlinkYS.getPlanTime();//计划用时(分)
//                Integer planNumber = workbatchOrdlinkYS.getPlanNumber();//应交数

//                Integer extraNum = workbatchOrdlinkYS.getExtraNum();//冗余数
//                Integer completeNum = workbatchOrdlinkYS.getCompleteNum();//已完成数
                Date closeTime = workbatchOrdlinkYS.getCloseTime();//截止时间
//                String pdType = workbatchOrdlinkYS.getPdType();//产品类型
                String pdNo = workbatchOrdlinkYS.getPdNo();//产品编号
                String pdName = workbatchOrdlinkYS.getPdName();//产品名称
                String odNo = workbatchOrdlinkYS.getOdNo();//订单编号

                Integer dpId = workbatchOrdlinkYS.getDpId();//部门id
                if (dpId == null || dpId == 0) {
                    return "部门id不能为空或0";
                }
                if (wbId == null || wbId == 0) {
                    return "批次id不能为空或0";
                }
                /*if(StringUtil.isEmpty(ptNo)){
                    return "部件编号不能为空或0";
                }*/
                if (ptId == null || ptId == 0) {
                    return "部件id不能为空或0";
                }
                if (StringUtil.isEmpty(ptName)) {
                    return "部件名称不能为空";
                }
                if (planTime == null) {
                    return "计划用时不能为空";
                }

                /*if(StringUtil.isEmpty(closeTime)){
                    return "截止时间不能为空";
                }*/
                if (StringUtil.isEmpty(pdNo)) {
                    return "产品编号不能为空";
                }
                if (StringUtil.isEmpty(pdName)) {
                    return "产品名称不能为空";
                }
                if (StringUtil.isEmpty(odNo)) {
                    return "订单编号不能为空";
                }

            }

        } else {
            return "传入数据不能为空";
        }
        /*新增排产与关联表数据*/
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
            Integer planNum = workbatchOrdlinkYS.getPlanNum();//计划数(作业数)
            Integer material = workbatchOrdlinkYS.getMaterial();
            Integer maId = workbatchOrdlinkYS.getMaId();//设备id
            Integer size = workbatchOrdlinkYS.getSize();
            /*查询标准产能*/
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
            Integer wbId = workbatchOrdlinkYS.getWbId();//批次id
            String remarks = workbatchOrdlinkYS.getRemarks();//备注
            String ptNo = workbatchOrdlinkYS.getPtNo();//部件编号
            Integer ptId = workbatchOrdlinkYS.getPtId();//部件id
            String ptName = workbatchOrdlinkYS.getPtName();//部件名称
            Integer planTime = workbatchOrdlinkYS.getPlanTime();//计划用时(分)
            Integer planNumber = workbatchOrdlinkYS.getPlanNumber();//应交数
            Integer extraNum = workbatchOrdlinkYS.getExtraNum();//冗余数
            Integer completeNum = workbatchOrdlinkYS.getCompleteNum();//已完成数
            Date closeTime = workbatchOrdlinkYS.getCloseTime();//截止时间
//          String pdType = workbatchOrdlinkYS.getPdType();//产品类型
            String pdNo = workbatchOrdlinkYS.getPdNo();//产品编号
            String pdName = workbatchOrdlinkYS.getPdName();//产品名称
            String pdImageurl = workbatchOrdlinkYS.getPdImageurl();//产品图片地址
            String odNo = workbatchOrdlinkYS.getOdNo();//订单编号
            Integer dpId = workbatchOrdlinkYS.getDpId();//部门id

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
            /*---------排产信息-------------*/
            workbatchOrdlink.setPlanNum(planNum);//计划数
            workbatchOrdlink.setPlanTime(planTime);//计划用时
            workbatchOrdlink.setPlanNumber(planNumber);//应交数
            workbatchOrdlink.setCompleteNum(completeNum);//完成数
            workbatchOrdlink.setIncompleteNum(planNum - completeNum);//未完成数
            workbatchOrdlink.setCloseTime(closeTime);//截止时间
            workbatchOrdlink.setExtraNum(extraNum);//冗余数
            workbatchOrdlink.setStartTime(date);//计划开始时间
            workbatchOrdlink.setSdDate(sdDate);//排产日期
            workbatchOrdlink.setMaId(maId);//设备id
            workbatchOrdlink.setOdNo(odNo);//订单编号
            workbatchOrdlink.setPdName(pdName);//产品名称
            workbatchOrdlink.setPdImageurl(pdImageurl);//产品图片地址
            workbatchOrdlink.setPdCode(pdNo);//产品编号
            workbatchOrdlink.setWbId(wbId);//批次id
            workbatchOrdlink.setPtId(ptId);//部件id
            workbatchOrdlink.setPtNo(ptNo);//部件编号
            workbatchOrdlink.setPartName(ptName);//部件名称
            workbatchOrdlink.setRemarks(remarks);//备注
            workbatchOrdlink.setDpId(dpId);//车间id
            workbatchOrdlink.setStatus("0");
            workbatchOrdlink.setRunStatus(0);
            workbatchOrdlink.setPdType(prodClassify.getClName());//产品类型
            workbatchOrdlink.setPrName(workbatchOrdlinkYS.getPrName());
            workbatchOrdlink.setPrId(prId);
            int workbatchOrdlinkInsert = ordlinkMapper.insert(workbatchOrdlink);

            /*----------oee--------------*/
            Integer speed = ruleProdoee.getSpeed() == null ? 1 : ruleProdoee.getSpeed();
            workbatchOrdoee.setSpeed(speed);//转速
            workbatchOrdoee.setMouldStay(ruleProdoee.getPrepareTime());
            workbatchOrdoee.setMouldNum(1);
//            workbatchOrdoee.setQualityNum(0);
            workbatchOrdoee.setProducePreTime(0);
            workbatchOrdoee.setBeforePtid(ptId);
            workbatchOrdoee.setBeforePtname(ptName);
            workbatchOrdoee.setBeforePtno(ptNo);
            workbatchOrdoee.setWkId(workbatchOrdlink.getId());
            workbatchOrdoee.setDifficultNum(1.0);
            workbatchOrdoee.setPlanTotalTime(1);//TODO:计划总时长,先写死
            int workbatchOrdoeeInsert = workbatchOrdoeeMapper.insert(workbatchOrdoee);
        }
        return "操作成功";
    }

    /**
     * 获取已接单但未点正式生产的订单
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
            workbatchOrdlinkVO.setSdDate(sdDate);//如果为空就传入sdDate的数据信息
        }
        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOS = workbatchOrdlinkMapper.getwaitWorkBatchOrd(workbatchOrdlinkVO);
        //循环排产单内容信息，进行状态转化
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
//                throw new CommonException(HttpStatus.NOT_FOUND.value(), "未找到设备信息，获取订单列表失败");
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
            // 如果工序不为空，查询所属工序分类下所有工序id
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
        //通过集合转化物料信息内容，暂时不用
        //List<MaterBatchlink> materbatchLs =  getMaterDate(vos);

        //物料排序
        if (materialNameSort) {
            workbatchOrdlinkVOS = workbatchOrdlinkVOS.stream().sorted(Comparator.comparing(WorkbatchOrdlinkVO::getMaterialName)).collect(toList());
        }
        //物料排序
        if (ingredientNameSort) {
            workbatchOrdlinkVOS = workbatchOrdlinkVOS.stream().sorted(Comparator.comparing(WorkbatchOrdlinkVO::getIngredientName)).collect(toList());
        }
        return page.setRecords(workbatchOrdlinkVOS);
    }


    /****
     * 获取物料管理信息。
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
            //通过sdids查询对应物料信息
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
        //worklink.setWsId(wsId); //如果需要用次方法需要修改为

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

        /*先查出需要的排产信息*/
        List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList = workbatchOrdlinkMapper.selectBySdDateMaIdWsId(sdDate, maId, wsId);
        String templatePath = "model/WorkbatchOrdlinkXY.xls";
//        InputStream imageInputStream = null;//条形码
        BufferedOutputStream bos = null;//导出到网页
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
                case 1://分切
                case 2://印刷
                case 3://表面处理
                case 4://裱纸
                case 5://模切
                case 9: {//手工班组
                    ptSize = workbatchOrdlinkVO.getOperateSize();//上机尺寸
                    break;
                }
                case 6://订装
                case 8: {//机粘
                    ptSize = workbatchOrdlinkVO.getPdSize();//产品尺寸
                    break;
                }

            }
            workbatchOrdlinkVO.setPtSize(ptSize);
//            System.out.println(workbatchOrdlinkVO);
//            byte[] result = BarcodeUtil.generate(workbatchOrdlinkVO.getId().toString());
            byte[] result = BarcodeUtil.generateBarCode(workbatchOrdlinkVO.getId().toString());
            // 文件流，输入一张图片
            try {
                // 使用工具方法把流转成byte数组
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
        //List<Page> page = XYindividual(workbatchOrdlinkVOList); // 一张表一个对象数据
        List<Page> page = DataByPage.byPage(workbatchOrdlinkVOList);
        Map<String, Object> model = new HashMap<>();

        model.put("pages", page);
        model.put("sheetNames", getSheetName(page));
        model.put("slName", getSheetName(page));
        try {
            bos = ExportlUtil.getBufferedOutputStream("排产单.xls", response);//返回前端处理
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//返回给前端
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
     * 将数据获取的数据封装成一页一个人的List
     */
    public static List<Page> XYindividual(List<WorkbatchOrdlinkVO> list) {
        List<Page> pages = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Page p = new Page();
            p.setOnlyOne(list.get(i));
//            p.setSheetName(list.get(i).getDpName()+"-"+list.get(i).getSfName()+"-"+(i+1));
            //设置sheet名称
            p.setSheetName(list.get(i).getSdDate() + list.get(i).getPrName());
            pages.add(p);
        }

        return pages;
    }


    /******
     * 排产设置内容信息
     * @param workbatchOrdlinkVO
     * @return
     */
    @Override
    public int andProductionScheduling(WorkbatchOrdlinkVO workbatchOrdlinkVO) {
        WorkbatchShift workbatchShift = new WorkbatchShift();
        Integer sdId = workbatchOrdlinkVO.getId();//排产id
        String sdDate = workbatchOrdlinkVO.getSdDate();//排产日期
        Integer planNum = workbatchOrdlinkVO.getPlanNum();//计划数量
        Integer mouldStay = workbatchOrdlinkVO.getMouldStay();//换膜时间
        Integer wsId = workbatchOrdlinkVO.getWsId();//班次id

//        WorkbatchOrdlink ordlink = workbatchOrdlinkMapper.selectById(sdId);
        Integer maId = workbatchOrdlinkVO.getMaId();//判断排产单id信息，返回设备id信息
        List<WorkbatchShift> workbatchShiftList = workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>()
                .eq("sd_date", sdDate).eq("ws_id", wsId).eq("sd_id", sdId).eq("ma_id", workbatchOrdlinkVO.getMaId()));
        if (!workbatchShiftList.isEmpty()) {
            return 0;
        }
        workbatchShift.setSdId(sdId);//排产ID
        workbatchShift.setSdDate(sdDate);//排产日期
        workbatchShift.setPlanNum(planNum);//计划数
        workbatchShift.setSpeed(workbatchOrdlinkVO.getSpeed());//标准产能
        workbatchShift.setCreateAt(new Date());
        workbatchShift.setWasteNum(0);//废品数
        workbatchShift.setMouldStay(mouldStay);
        workbatchShift.setMaId(workbatchOrdlinkVO.getMaId());
        workbatchShift.setWsId(wsId);//班次id
        workbatchShift.setShiftStatus(-1);//未下发状态
        workbatchShift.setStatus("0");//1,0设定为待发布，已发布后再修改为1
        workbatchShift.setMaId(maId);//追加插入数据的设备id
        workbatchShift.setPlanType("1");//设置生产类型
        /*查询班次信息*/
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId, maId);
        if (workbatchShiftset != null) {
            workbatchShift.setCkName(workbatchShiftset.getCkName());//班次名称
            Date startTime = workbatchShiftset.getStartTime();
            Date endTime = workbatchShiftset.getEndTime();
            Date classStartTime = DateUtil.toDate(sdDate + " " + DateUtil.format(startTime, "HH:mm:ss"), null);
            Date classEndTime = DateUtil.toDate(sdDate + " " + DateUtil.format(endTime, "HH:mm:ss"), null);
            workbatchShift.setStartTime(classStartTime);
            workbatchShift.setEndTime(classEndTime);
        }
        int insert = workbatchShiftMapper.insert(workbatchShift);

        //执行待排数据变化的判断操作----start Jenny
        iWorkbatchOrdlinkNewService.setWorksNum(workbatchShift.getSdId(), workbatchShift.getMaId(), workbatchOrdlinkVO.getStatus());
        //执行待排数据变化的判断操作----end Jenny
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
        for (ArticlesOrderVO articlesOrderVO : articlesOrderVOList) {//处理数据格式
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
        // status== -1:未下发 0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成(已上报)
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
     * 待排产数量信息
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
            // 未上报
            Integer notReportedNum = workbatchShiftMapper.getNotReportedNum(sdId, shiftStratTime);
            // 已上报
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
        //获取当前需要的工单信息内容
        WorkbatchOrdlink batchOrdlink = new WorkbatchOrdlink();
        batchOrdlink.setMaId(maId);
        batchOrdlink.setMaType(null);
        //查看关联的设备信息，是否按照工序进行排程操作。
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
