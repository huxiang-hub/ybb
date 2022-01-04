package com.yb.panelapi.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.mapper.BaseStaffinfoMapper;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.vo.ExecuteBrieferOrdlinkVO;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.order.vo.OrderWorkbatchVO;
import com.yb.panelapi.common.UpdateStateUtils;
import com.yb.panelapi.order.entity.FinishAndAccept;
import com.yb.panelapi.request.OrderAcceptRequest;
import com.yb.panelapi.user.utils.R;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.service.ISuperviseExecuteService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import com.yb.workbatch.vo.WorkbatchShiftDataVO;
import com.yb.workbatch.vo.WorkbatchShiftListVO;
import com.yb.workbatch.wrapper.WorkbatchOrdlinkWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.common.resubmit.annotion.ReSubmit;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@RestController
@RequestMapping("/plapi/info")
@Api(tags = "机台订单接受/结束相关接口")
public class PApiWorkbatchOrdlinkController {

    @Autowired
    private IWorkbatchOrdlinkService ordlinkService;
    @Autowired
    private ISuperviseExecuteService executeService;
    @Autowired
    private IOrderWorkbatchService workbatchService;
    @Autowired
    private IOrderWorkbatchService orderWorkbatchService;
    @Autowired
    private IMachineMainfoService machineMainfoService;
    @Autowired
    private ISuperviseBoxinfoService superviseBoxinfoService;
    @Autowired
    private BaseStaffinfoMapper baseStaffinfoMapper;
    @Autowired
    private WorkbatchShiftService workbatchShiftService;
    @Autowired
    private IExecuteBrieferService executeBrieferService;


    /**
     * DONE点击接受订单
     */
    @PostMapping(value = "/acceptOrder", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public R acceptOrder(@RequestBody WorkbatchOrdlinkVO ordlinkVO) {
//        return R.ok(ordlinkService.acceptOrder(ordlinkVO));
        Integer maId = ordlinkVO.getMaId();
        Integer wfId = ordlinkVO.getWfId();
        Integer wsId = ordlinkVO.getWsId();
        Integer usId = ordlinkVO.getUsId();

        //结束当前设备上已经接单的数据内容。
        R rt = ordlinkService.finishOrderNew(maId);
//        if(rt.get("code")=="200"){
//
//        }
        R rc = ordlinkService.acceptOrderNew(ordlinkVO);
        //调整新的接单方法；设备按照
        return R.ok(rc);
    }

    /**
     * DONE点击接受订单
     */
    @PostMapping("/acceptOrderNew")
    @ApiModelProperty("新的接单的方法，需要获取设备maId、排产班次ID、用户id")
    public R acceptOrderNew(@RequestBody OrderAcceptRequest orderAccept) {        //结束当前设备上已经接单的数据内容。

        if (orderAccept == null || orderAccept.getMaId() == null) {
            R.error("未收到设备唯一标识");
        }
        if (orderAccept.getWfId() == null)
            R.error("接单信息为空。请核对后重新请求。");

        R rt = ordlinkService.finishOrderNew(orderAccept.getMaId());
//        if(rt.get("code")=="200"){
//
//        }
        R rc = ordlinkService.acceptOrderBywfid(orderAccept);
        //调整新的接单方法；设备按照
        return R.ok(rc);
    }


    /**
     * Done 正式生产订单
     */
    @PostMapping("/proOrder")
    @ResponseBody
    public R proOrder(@RequestBody WorkbatchOrdlinkVO ordlinkVO) {
        Map<Object, Object> map = new HashMap<>();
        R r = ordlinkService.proOrder(ordlinkVO);
        map.put("orderLink", r.get("data"));
        return R.ok(map);
    }

    /**
     * Done 正式生产订单
     */
    @GetMapping("/proOrderNew")
    @ApiModelProperty("生产准备过程进度正式生产；新方法")
    public R proOrderNew(Integer maId) {
        System.out.println("--------------------->1111:proOrderNew:::maId:" + maId + ":::::::proOrderNew:" );
        R rct = ordlinkService.proOrderNew(maId);
        return R.ok(rct);
    }

    /**
     * DONE 结束生产订单
     */
    @GetMapping("/finishOrder")
    @ResponseBody
    @ReSubmit
    public R finishOrder(Integer maId, Integer sdId, Integer usId, Integer infoId, Integer wfId) {
        R r = R.ok(ordlinkService.finishOrder(maId, sdId, usId, infoId, wfId, 3));
        return r;
    }


    /**
     * DONE 结束生产订单
     */
    @GetMapping("/finishOrderNew")
    @ApiOperation(value = "正式生产后结束生产；新方法")
    public R finishOrderNew(Integer maId) {
        R r = R.ok(ordlinkService.finishOrderNew(maId));
        return r;
    }


    @PostMapping("/finishAndAccept")
    @ApiOperation(value = "先结束订单再接受订单")
    public R finishAndAccept(@RequestBody FinishAndAccept FinishAndAccept) {
        Integer maId = FinishAndAccept.getMaId();
        Integer sdId = FinishAndAccept.getSdId();
        Integer usId = FinishAndAccept.getUsId();
        Integer wfId = FinishAndAccept.getWfId();//接受的订单
        SuperviseExecute superviseExecute =
                executeService.getOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
        if (superviseExecute != null) {
            Integer finishSdId = superviseExecute.getSdId();
            Integer finishWfId = superviseExecute.getWfId();
            Integer exId = superviseExecute.getExId();
            Integer operator = superviseExecute.getOperator();
            /*结束订单*/
            ordlinkService.finishOrder(maId, finishSdId, operator, exId, finishWfId, 3);
        }

        WorkbatchOrdlink ordlink = ordlinkService.getById(sdId);

        WorkbatchShift workbatchShift = workbatchShiftService.getById(wfId);
        WorkbatchOrdlinkVO ordlinkVO = WorkbatchOrdlinkWrapper.build().entityVO(ordlink);
        if (workbatchShift != null) {
            ordlinkVO.setWsId(workbatchShift.getWsId());
            ordlinkVO.setSdDate(workbatchShift.getSdDate());
        }
        ordlinkVO.setMaId(maId);
        ordlinkVO.setWfId(wfId);
        ordlinkVO.setUsId(usId);
        /*接受订单*/
        return R.ok(ordlinkService.acceptOrderNew(ordlinkVO));
    }

    /**
     * 获取生产结束 没有上报的订单
     */
    @GetMapping("/getNoReportOrder")
    @ResponseBody
    public R getNoReportOrder(Integer maId) {
        List<WorkbatchOrdlinkVO> noReportOrder = ordlinkService.getNoReportOrder(maId);
        return R.ok(noReportOrder);
    }
    /**
     * 获取生产结束 没有上报的订单
     */
    @GetMapping("/getNoReportOrderNew")
    @ResponseBody
    public R getNoReportOrderNew(Integer maId) {
        List<ExecuteBrieferOrdlinkVO> noReportOrder = ordlinkService.getNoReportOrderNew(maId);
        return R.ok(noReportOrder);
    }

    /**
     * DONE 暂停当前订单
     */
    @GetMapping("/pauseOrder")
    @ResponseBody
    public R pauseOrder(Integer maId, Integer sdId, Integer usId) {
        Date currTime = new Date();
        SuperviseExecute execute = executeService.getExecuteOrder(maId);
        ExecuteState state = new ExecuteState();
        Integer orderId = null;
        OrderWorkbatch orderWorkbatch = orderWorkbatchService.getById(sdId);
        if (orderWorkbatch != null) {
            orderId = orderWorkbatch.getOdId();
        }
        state.setMaId(maId);
        state.setSdId(sdId);
        state.setOdId(orderId);
        state.setUsId(usId);
        state.setWbId(execute.getWbId());
        state.setStartAt(currTime);
        state.setTeamId(execute.getUsIds());
        state.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());
        state.setEvent(GlobalConstant.ProType.PAUSE_EVENT.getType());
        state.setCreateAt(currTime);
        UpdateStateUtils.updateSupervise(state, null);//暂停无需修改执行单主键信息
        return R.ok();
    }

    /**
     * DONE 继续当前订单
     */
    @GetMapping("/continueOrder")
    @ResponseBody
    @ReSubmit
    public R continueOrder(Integer sdId, Integer usId) {
        SuperviseExecute execute = executeService.getExecuteStateByOdId(sdId);
        Date currTime = new Date();
        ExecuteState state = new ExecuteState();
        Integer orderId = null;
        OrderWorkbatch orderWorkbatch = orderWorkbatchService.getById(sdId);
        if (orderWorkbatch != null) {
            orderId = orderWorkbatch.getOdId();
        }
        state.setWbId(execute.getWbId());
        state.setSdId(sdId);
        state.setUsId(usId);
        state.setOdId(orderId);
        state.setMaId(execute.getMaId());
        state.setTeamId(execute.getUsIds());
        state.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());//设定当前状态正式生产
        state.setEvent(GlobalConstant.ProType.PRODUCT_EVENT.getType());//正式生产
        state.setStartAt(currTime);
        state.setCreateAt(currTime);
        UpdateStateUtils.updateSupervise(state, null);//无需修改执行单主键信息
        return R.ok();
    }
    /*@GetMapping("/getPudectOrder")
    @ResponseBody
    @ReSubmit
    public R getPudectOrder(Integer maId) {
        SuperviseExecute executeOrder = executeService.getExecuteOrder(maId);
        WorkbatchOrdlinkVO runOrder = ordlinkService.getPudectOrder(maId);
        Map<Object, Object> map = new HashMap<>();
        if (runOrder == null) {
            return R.error("没有生产准备的订单");
        }
        Integer currNum = executeOrder.getCurrNum();
        Integer incompleteNum = runOrder.getIncompleteNum();
        if(incompleteNum == null){
            incompleteNum = 0;
        }
        Integer num = incompleteNum - currNum;
        if(num < 0) num = 0;
        runOrder.setIncompleteNum(num);
        map.put("orderLink", runOrder);
//        map.put("workBatch", workbatchVO);
        map.put("status", executeOrder);
        return R.ok(map);
    }*/

    @GetMapping("/getOrderListNew")
    @ApiModelProperty("返回对应的接单管理界面，并且展示工单列表信息")
    public R getOrderListNew(Integer maId) {
        SuperviseExecute supexecute = executeService.getExecuteOrder(maId);
        if (supexecute == null || "B".equalsIgnoreCase(supexecute.getEvent()) || "C".equalsIgnoreCase(supexecute.getEvent())) {
            return R.error("设备正在生产，请跳转到运行界面");
        }
        //判断设备是否按照工单或者按照工序进行数据接单管理
        MachineMainfo mainfo = machineMainfoService.getById(maId);
        boolean isrecepro = (mainfo != null && mainfo.getIsRecepro() == 1) ? true : false;//当为1的时候就表示按照工序接单  //是否按照工序接单
        //Integer receproMaType = (mainfo.getMaType() != null) ? Integer.parseInt(mainfo.getMaType()) : 0;//获取设备分类
        List<WorkbatchShiftListVO> list = new ArrayList<>();
        //设定开始时间和结束时间：默认为
        String endDate = DateUtil.refNowDay();//当天的数据信息
        String startDate = DateUtil.refNowDay(DateUtil.addDayForDate(new Date(), -2));//当天的数据信息内容


        //判断当不是按照工序进行排产，按照设备排产，直接查询设备对应排产信息
        if (!isrecepro) {
            //获取机器的安排的数据信息内容
            list = ordlinkService.getOrderListNew(maId, startDate, endDate);
        } else {
            //按照工序进行排产工单处理
            list = ordlinkService.getOrderListByPrids(maId, startDate, endDate);
        }


        //返回结果内容信息
        Map<String, Object> result = new HashMap<>();
        Integer isAuto = 0;
        //判断过滤出来自动接单功能
        for (WorkbatchShiftListVO wbShiftvo : list) {
            if (wbShiftvo != null && wbShiftvo.getIsAuto() == 1) {
                isAuto = 1;
            }
        }
        result.put("isAuto", isAuto);
        result.put("wblist", list);
        return R.ok(result);
    }


    @GetMapping("/getShiftData")
    @ApiModelProperty("返回对应的接单管理界面，并且展示工单列表信息")
    public R getShiftData(Integer wfId) {
        WorkbatchShiftDataVO shdata = workbatchShiftService.getShiftData(wfId);
        if (shdata != null && shdata.getWfId() != null)
            return R.ok(shdata);
        else
            return R.error("未查到该排程单的信息:" + wfId);
    }

    /**
     * 获取订单列表
     * 根据设备id 和 生产状态
     */
    @PostMapping("/getOrderList")
    @ResponseBody
    public R getOrderList(@RequestBody WorkbatchOrdlink workbatchOrdlink) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        HttpServletRequest request = requestAttributes.getRequest();
        String url = request.getRequestURL().toString();
        List<Map<String, Object>> result = new ArrayList();
        boolean isrecepro = false;//是否按照工序接单，false是按设备接单 true按照工序接单；
        Integer receproMaType = 0;//获取设备分类
        //判断是否按照工序状态显示订单数据
        if (workbatchOrdlink != null && workbatchOrdlink.getMaId() != null) {
            MachineMainfo mainfo = machineMainfoService.getById(workbatchOrdlink.getMaId());
            isrecepro = (mainfo != null && mainfo.getIsRecepro() == 1) ? true : false;//当为1的时候就表示按照工序接单
            receproMaType = (mainfo.getMaType() != null) ? Integer.parseInt(mainfo.getMaType()) : 0;
        }
        List<WorkbatchOrdlinkVO> list = new ArrayList<>();
        if (!isrecepro) {
            list = ordlinkService.getOrderList(workbatchOrdlink);
        } else {
            //按照工序进行排产工单处理
//            workbatchOrdlink.setMaId(null);
            workbatchOrdlink.setMaType(receproMaType);
            list = ordlinkService.getOrderListByMatype(workbatchOrdlink);
        }

        for (WorkbatchOrdlinkVO workbatchOrdlinkVO : list) {
            String ptSize = workbatchOrdlinkVO.getPtSize();
            Integer maType = workbatchOrdlinkVO.getMaType();
            //设定为兴艺的账号进行关联URL操作类型
            if (url != null && url.toUpperCase().indexOf("xingyi.".toUpperCase()) != -1) {
                maType = (maType != null) ? maType : 0;
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
            }
            workbatchOrdlinkVO.setPtSize(ptSize);
            workbatchOrdlinkVO.setPtName(workbatchOrdlinkVO.getPartName());//部件名称
            Integer wfId = workbatchOrdlinkVO.getWfId();
            WorkbatchShift workbatchShift =
                    //workbatchShiftService.getOne(new QueryWrapper<WorkbatchShift>().eq("sd_id", ordlink.getId()).eq("sd_date", workbatchOrdlink.getSdDate()).eq("ws_id", workbatchOrdlink.getWsId()));
                    workbatchShiftService.getOne(new QueryWrapper<WorkbatchShift>().eq("id", wfId));
            Map<String, Object> map = new HashMap();
            if (workbatchShift != null) {
                List<ExecuteBriefer> executeBrieferList =
                        executeBrieferService.getBaseMapper().selectList(new QueryWrapper<ExecuteBriefer>().eq("wf_id", wfId));
                Integer completeNum = 0;
                for (ExecuteBriefer executeBriefer : executeBrieferList) {
                    Integer productNum = executeBriefer.getProductNum();//作业数
                    productNum = productNum == null ? 0 : productNum;
                    completeNum += productNum;
                }
                workbatchOrdlinkVO.setCompleteNum(workbatchShift.getFinishNum());//上报表查询已完成数
                Date proBeginTime = workbatchShift.getProBeginTime();
                workbatchOrdlinkVO.setStartTime(proBeginTime);
                Date proFinishTime = workbatchShift.getProFinishTime();
                workbatchOrdlinkVO.setCloseTime(proFinishTime);
                workbatchOrdlinkVO.setPlanNum(workbatchShift.getPlanNum());
                workbatchOrdlinkVO.setExtraNum(workbatchShift.getWasteNum());
                Integer plannum = workbatchShift.getPlanNum();
                plannum = (plannum != null) ? plannum : 0;
                Integer finishnum = workbatchShift.getFinishNum();
                finishnum = (finishnum != null) ? finishnum : 0;
                workbatchOrdlinkVO.setIncompleteNum(plannum - finishnum);
                Integer isAuto = workbatchOrdlinkVO.getIsAuto();
//                if(isAuto != null && isAuto == 1){//特定情况下自动接单
//                    R r = ordlinkService.acceptOrder(workbatchOrdlinkVO);//接单
//                    Integer infoId = (int)r.get("data");
//                    workbatchOrdlinkVO.setInfoId(infoId);
//                    ordlinkService.proOrder(workbatchOrdlinkVO);//正式生产
//                }
            }

//            Integer wbId = ordlink.getWbId();
//            OrderWorkbatchVO workbatchVO = workbatchService.getWorkBatchByWbId(wbId);
            map.put("orderLink", workbatchOrdlinkVO);
//            map.put("workBatch", workbatchVO);
            result.add(map);
        }
        return R.ok(result);
    }

    /**
     * 获取正在运行的订单
     */
    @GetMapping("/getRunOrder")
    @ResponseBody
    public R getRunOrder(Integer maId) {
        SuperviseExecute executeOrder = executeService.getExecuteOrder(maId);
        WorkbatchOrdlinkVO runOrder = ordlinkService.getRunOrder(maId);
        if (runOrder == null) {
            return R.error("没有运行中的订单");
        }
        WorkbatchShift workbatchShift =
                workbatchShiftService.getOne(new QueryWrapper<WorkbatchShift>().eq("sd_id", runOrder.getId()));
        if (workbatchShift != null) {
            Date proBeginTime = workbatchShift.getProBeginTime();
            if (proBeginTime != null) {
                runOrder.setStartTime(proBeginTime);
            }
            Date proFinishTime = workbatchShift.getProFinishTime();
            if (proFinishTime != null) {
                runOrder.setCloseTime(proFinishTime);
            }
        }
        Integer currNum = executeOrder.getCurrNum();
        Integer incompleteNum = runOrder.getIncompleteNum();
        if (incompleteNum == null) {
            incompleteNum = 0;
        }
        Integer num = incompleteNum - currNum;
        if (num < 0) num = 0;
        runOrder.setIncompleteNum(num);
//        OrderWorkbatchVO workbatchVO = workbatchService.getWorkBatchByWbId(runOrder.getWbId());
        Map<Object, Object> map = new HashMap<>();
        map.put("orderLink", runOrder);
//        map.put("workBatch", workbatchVO);
        map.put("status", executeOrder);
        return R.ok(map);
    }

    /**
     * 获取正在运行的订单
     */
    @RequestMapping("/getUserRunOrders")
    @ResponseBody
    public R getUserRunOrder(Integer usId) {
        List<SuperviseExecute> superviseExecuteList =
                executeService.list(new QueryWrapper<SuperviseExecute>().eq("operator", usId));
        List<WorkbatchOrdlinkVO> userRunOrderList = ordlinkService.getUserRunOrder(usId);
        /*查询所有人员信息*/
        List<BaseStaffinfo> baseStaffinfoList = baseStaffinfoMapper.selectList(new QueryWrapper<>());
        List<MachineMainfo> machineMainfoList = machineMainfoService.list();
        List<SuperviseBoxinfo> superviseBoxinfoList =
                superviseBoxinfoService.list(new QueryWrapper<>());

        for (WorkbatchOrdlinkVO workbatchOrdlinkVO : userRunOrderList) {
            for (SuperviseExecute superviseExecute : superviseExecuteList) {
                if (workbatchOrdlinkVO.getId().equals(superviseExecute.getSdId())) {
                    String usIds = superviseExecute.getUsIds();
                    if (!StringUtil.isEmpty(usIds)) {
                        List<String> usNames = new ArrayList<>();
                        String[] split = usIds.split("\\|");
                        for (String userId : split) {
                            if (!StringUtil.isEmpty(userId)) {
                                for (BaseStaffinfo baseStaffinfo : baseStaffinfoList) {
                                    if (userId.equals(baseStaffinfo.getUserId().toString())) {
                                        usNames.add(baseStaffinfo.getName());
                                    }
                                }
                            }
                        }
                        workbatchOrdlinkVO.setUserList(usNames);//操作人员姓名
                    }
                    Integer maId = superviseExecute.getMaId();
                    for (MachineMainfo machineMainfo : machineMainfoList) {
                        if (maId.equals(machineMainfo.getId())) {
                            workbatchOrdlinkVO.setMachineName(machineMainfo.getName());//设备名称
                            break;
                        }
                    }
                    Double dspeed = null;
                    for (SuperviseBoxinfo superviseBoxinfo : superviseBoxinfoList) {
                        if (maId.equals(superviseBoxinfo.getMaId())) {
                            dspeed = superviseBoxinfo.getDspeed();//设备名称
                            break;
                        }
                    }
                    //MachineMainfo machineMainfo = machineMainfoService.getById(maId);
                    //SuperviseBoxinfo superviseBoxinfo =
//                    superviseBoxinfoService.getOne(new QueryWrapper<SuperviseBoxinfo>().eq("ma_id", maId));

                    double speed = dspeed == null ? 0 : dspeed;
                    workbatchOrdlinkVO.setSpeed((int) speed);//设备当前时速

                    //workbatchOrdlinkVO.setCompleteNum(superviseExecute.getCurrNum());//已完成数
                    workbatchOrdlinkVO.setStartTime(superviseExecute.getStartTime());//开始时间
                    workbatchOrdlinkVO.setExeStatus(superviseExecute.getExeStatus());//生产状态
                    break;

                }
            }
        }
        if (userRunOrderList.isEmpty()) {
            return R.error("没有运行中的订单");
        }
        return R.ok(userRunOrderList);
    }

    /**
     * 获取用户待上报的工单
     */
    @RequestMapping("/getUserBrieferOrder")
    @ResponseBody
    public R getUserBrieferOrder(Integer usId) {
        List<WorkbatchOrdlinkVO> userBrieferOrderList = ordlinkService.getUserBrieferOrder(usId);
        List<SuperviseExecute> superviseExecuteList =
                executeService.list(new QueryWrapper<SuperviseExecute>().eq("operator", usId));
        for (WorkbatchOrdlinkVO workbatchOrdlinkVO : userBrieferOrderList) {
            for (SuperviseExecute superviseExecute : superviseExecuteList) {
                if (workbatchOrdlinkVO.getId().equals(superviseExecute.getSdId())) {
                    Integer maId = superviseExecute.getMaId();
                    MachineMainfo machineMainfo = machineMainfoService.getById(maId);
//                    SuperviseBoxinfo superviseBoxinfo =
//                            superviseBoxinfoService.getOne(new QueryWrapper<SuperviseBoxinfo>().eq("ma_id", maId));
                    Integer number = superviseExecute.getCurrNum() == null ? 0 : superviseExecute.getCurrNum();
                    double speed = number / workbatchOrdlinkVO.getProTime() / 3600;
                    workbatchOrdlinkVO.setSpeed((int) speed);//设备当前时速
                    workbatchOrdlinkVO.setMachineName(machineMainfo.getName());//设备名称
                    workbatchOrdlinkVO.setStartTime(superviseExecute.getStartTime());//开始时间
                    workbatchOrdlinkVO.setExeStatus(superviseExecute.getExeStatus());//生产状态
                    workbatchOrdlinkVO.setMachineNumber(number);
                    break;

                }
            }
        }
        return R.ok(userBrieferOrderList);
    }

    /**
     * 扫码获取可接工单
     */
    @RequestMapping("/getSdOrderList")
    @ResponseBody
    public R getSdOrderList(Integer maId) {
//        MachineMainfo machineMainfo = machineMainfoService.getById(maId);
//        Integer dpId = machineMainfo.getDpId();
        List<Map<String, Object>> result = new ArrayList();
        List<WorkbatchOrdlink> list = ordlinkService.getSdOrderList(maId);
        for (WorkbatchOrdlink ordlink : list) {
            Map<String, Object> map = new HashMap();
            Integer wbId = ordlink.getWbId();
            OrderWorkbatchVO workbatchVO = workbatchService.getWorkBatchByWbId(wbId);
            Integer completeNum = ordlink.getCompleteNum() == null ? 0 : ordlink.getCompleteNum();
            Double scheduling = completeNum / (double) ordlink.getPlanNum();
            map.put("orderLink", ordlink);
            map.put("workBatch", workbatchVO);
            map.put("scheduling", scheduling);
            result.add(map);
        }

        return R.ok(result);
    }

    /**
     * 获取订单的实时计数  前台定时请求
     */
    @GetMapping("/getCurrNum")
    @ResponseBody
    public R getOrderNum(Integer maId) {
        return R.ok(ordlinkService.getCurrNum(maId));
    }


    /**
     * 获取已接单但未点正式生产的订单
     *
     * @deprecated
     */
    @GetMapping("/getReceivedOrder")
    @ResponseBody
    public R getReceivedOrder(Integer maId) {
        SuperviseExecute executeOrder = executeService.getExecuteOrder(maId);
        Integer wfId = null;
        //需要判断实施状态表中的状态为B和C即可 wyn
        if ("B".equalsIgnoreCase(executeOrder.getExeStatus()) || "C".equalsIgnoreCase(executeOrder.getExeStatus())) {
            if (executeOrder.getWfId() != null) {
                wfId = executeOrder.getWfId();
                //根据排产单查询得到待接单数据信息
                WorkbatchOrdlinkVO reOrder = ordlinkService.getReceivedOrderBywfId(wfId);
                if (reOrder == null) {
                    return R.error("没有生产准备的订单");
                }

                /***
                 * 增加有效数据 star
                 */
                reOrder.setMaId(maId);
                String ptSize = reOrder.getPtSize();
                Integer maType = reOrder.getMaType();
                maType = maType != null ? maType : 0;
                switch (maType) {
                    case 1://分切
                    case 2://印刷
                    case 3://表面处理
                    case 4://裱纸
                    case 5://模切
                    case 9: {//手工班组
                        ptSize = reOrder.getOperateSize();//上机尺寸
                        break;
                    }
                    case 6://订装
                    case 8: {//机粘
                        ptSize = reOrder.getPdSize();//产品尺寸
                        break;
                    }
                }
                reOrder.setPtSize(ptSize);
                reOrder.setPtName(reOrder.getPartName());
                WorkbatchShift workbatchShift =
                        workbatchShiftService.getOne(new QueryWrapper<WorkbatchShift>().eq("id", reOrder.getWfId()));
                Integer completeNum = 0;
                Integer planNum = 0;
                if (workbatchShift != null) {
                    //Integer wfId = workbatchShift.getId();
                    List<ExecuteBriefer> executeBrieferList =
                            executeBrieferService.getBaseMapper().selectList(new QueryWrapper<ExecuteBriefer>().eq("wf_id", wfId));

                    for (ExecuteBriefer executeBriefer : executeBrieferList) {
                        Integer productNum = executeBriefer.getProductNum();//作业数
                        productNum = productNum == null ? 0 : productNum;
                        completeNum += productNum;
                    }
                    reOrder.setCompleteNum(workbatchShift.getFinishNum());//上报表查询已完成数
                    planNum = workbatchShift.getPlanNum();
                    planNum = planNum == null ? 0 : planNum;
                    reOrder.setPlanNum(planNum);
                    Date proBeginTime = workbatchShift.getProBeginTime();
                    if (proBeginTime != null) {
                        reOrder.setStartTime(proBeginTime);
                    }
                    Date proFinishTime = workbatchShift.getProFinishTime();
                    if (proFinishTime != null) {
                        reOrder.setCloseTime(proFinishTime);
                    }
                }
                Integer currNum = executeOrder.getCurrNum();
                //计划数减上报数之和，得到未完成数
                // [最新排产]
                //        Integer incompleteNum = planNum - completeNum;
                Integer finishnum = workbatchShift.getFinishNum();
                finishnum = (finishnum != null) ? finishnum : 0;
                Integer incompleteNum = planNum - finishnum;
                if (incompleteNum == null) {
                    incompleteNum = 0;
                }
                //未完成数减去盒子实时记录数，得到该工单在这台设备上的未完成数
                Integer num = incompleteNum - currNum;
                if (num < 0) num = 0;
                reOrder.setIncompleteNum(num);//未完成数量TODO
                reOrder.setInfoId(executeOrder.getExId());
                /****
                 * 增加有效数据  end
                 */
                Map<Object, Object> map = new HashMap<>();
                map.put("orderLink", reOrder);
                map.put("status", executeOrder);
                return R.ok(map);
            }
        }
        //状态标识为待接单状态，不是B\C
        return R.error("没有生产准备的订单");
    }

    /**
     * 获取已接单但未点正式生产的订单
     * 历史版本：屏幕进行清单列表展示
     *
     * @deprecated
     */
    @GetMapping("/getReceivedOrderTest")
    @ResponseBody
    public R getReceivedOrderTest(Integer maId) {
        SuperviseExecute executeOrder = executeService.getExecuteOrder(maId);
        //TODO 需要判断实施状态表中的状态为B和C即可
//        WorkbatchOrdlinkVO receivedOrder = new WorkbatchOrdlinkVO();
//        if("B".equalsIgnoreCase(executeOrder.getExeStatus())||"C".equalsIgnoreCase(executeOrder.getExeStatus())){
//            receivedOrder =  ordlinkService.getById(executeOrder.getWfId());
//        }
        WorkbatchOrdlinkVO receivedOrder = ordlinkService.getReceivedOrder(maId);
        Map<Object, Object> map = new HashMap<>();
        if (receivedOrder == null) {
            return R.error("没有生产准备的订单");
        }
        receivedOrder.setMaId(maId);
        String ptSize = receivedOrder.getPtSize();
        Integer maType = receivedOrder.getMaType();
        maType = maType != null ? maType : 0;
        switch (maType) {
            case 1://分切
            case 2://印刷
            case 3://表面处理
            case 4://裱纸
            case 5://模切
            case 9: {//手工班组
                ptSize = receivedOrder.getOperateSize();//上机尺寸
                break;
            }
            case 6://订装
            case 8: {//机粘
                ptSize = receivedOrder.getPdSize();//产品尺寸
                break;
            }
        }
        receivedOrder.setPtSize(ptSize);
        receivedOrder.setPtName(receivedOrder.getPartName());
        WorkbatchShift workbatchShift =
                workbatchShiftService.getOne(new QueryWrapper<WorkbatchShift>().eq("id", receivedOrder.getWfId()));
        Integer completeNum = 0;
        Integer planNum = 0;
        if (workbatchShift != null) {
            Integer wfId = workbatchShift.getId();
            List<ExecuteBriefer> executeBrieferList =
                    executeBrieferService.getBaseMapper().selectList(new QueryWrapper<ExecuteBriefer>().eq("wf_id", wfId));

            for (ExecuteBriefer executeBriefer : executeBrieferList) {
                Integer productNum = executeBriefer.getProductNum();//作业数
                productNum = productNum == null ? 0 : productNum;
                completeNum += productNum;
            }
            receivedOrder.setCompleteNum(workbatchShift.getFinishNum());//上报表查询已完成数
            planNum = workbatchShift.getPlanNum();
            planNum = planNum == null ? 0 : planNum;
            receivedOrder.setPlanNum(planNum);
            Date proBeginTime = workbatchShift.getProBeginTime();
            if (proBeginTime != null) {
                receivedOrder.setStartTime(proBeginTime);
            }
            Date proFinishTime = workbatchShift.getProFinishTime();
            if (proFinishTime != null) {
                receivedOrder.setCloseTime(proFinishTime);
            }
        }
        Integer currNum = executeOrder.getCurrNum();
        //计划数减上报数之和，得到未完成数
        // [最新排产]
//        Integer incompleteNum = planNum - completeNum;
        Integer incompleteNum = planNum - workbatchShift.getFinishNum();
        if (incompleteNum == null) {
            incompleteNum = 0;
        }
        //未完成数减去盒子实时记录数，得到该工单在这台设备上的未完成数
        Integer num = incompleteNum - currNum;
        if (num < 0) num = 0;
        receivedOrder.setIncompleteNum(num);//未完成数量TODO
        map.put("orderLink", receivedOrder);
//        map.put("workBatch", workbatchVO);
        map.put("status", executeOrder);
        return R.ok(map);
    }

    /**
     * 返回设备状态信息
     */
    @PostMapping("/getMachStatus")
    @ResponseBody
    public R getMachStatus(Integer maId) {
        SuperviseExecute maExecute = executeService.getExecuteOrder(maId);
        Map<Object, Object> map = new HashMap<>();
        map.put("status", maExecute.getEvent());
        return R.ok(map);
    }


    /**
     * 获取已接单但未点正式生产的订单
     *
     * @deprecated
     */
    @GetMapping("/getHasShift")
    @ResponseBody
    public R getHasShift(Integer maId) {
        SuperviseExecute executeOrder = executeService.getExecuteOrder(maId);
        Map<String, Boolean> map = new HashMap<>();
        Integer wfId = executeOrder.getWfId();
        //需要判断实施状态表中的状态为B和C即可 wyn
        if ("B".equalsIgnoreCase(executeOrder.getExeStatus()) || "C".equalsIgnoreCase(executeOrder.getExeStatus())) {
            map.put("isAccept", true);
        } else {
            map.put("isAccept", false);
        }
        Integer hasShiftnum = ordlinkService.hasShiftThreeDay(maId);
        //当接单信息大于0的时候，就需要提供给
        if (hasShiftnum > 0) {
            map.put("hasShift", true);
        } else {
            map.put("hasShift", false);
        }
        //当设备有单（三天内）并且没有接单发出状态
        if (map.get("hasShift") && !map.get("isAccept")) {
            map.put("isSound", true);
        }
        //状态标识为待接单状态，不是B\C
        return R.ok(map);
    }
}
