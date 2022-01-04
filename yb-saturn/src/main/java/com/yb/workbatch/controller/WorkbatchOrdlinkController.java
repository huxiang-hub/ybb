package com.yb.workbatch.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.actset.common.SaveCheckLogUnit;
import com.yb.common.DateUtil;
import com.yb.common.entity.WorkbatchOrdlinkYS;
import com.yb.customer.service.CrmCustomerService;
import com.yb.customer.vo.CrmCustomerVO;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.service.ExecuteTraycardService;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.service.IOrderOrdinfoService;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.order.vo.OrderOrdinfoVO;
import com.yb.order.vo.OrderWorkbatchVO;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.service.IProcessMachlinkService;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.prod.entity.ProdPartsinfo;
import com.yb.prod.service.IProdPartsinfoService;
import com.yb.prod.service.IProdPdinfoService;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdPdinfoVO;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.*;
import com.yb.workbatch.excelUtils.ExcelUtil;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.service.IWorkbatchModulusService;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.IWorkbatchOrdoeeService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.task.WorkbatchTask;
import com.yb.workbatch.vo.*;
import com.yb.workbatch.wrapper.WorkbatchOrdlinkWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 生产排产表yb_workbatch_ordlink 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/workbatchordlink")
@Api(value = "生产排产表yb_workbatch_ordlink", tags = "生产排产表yb_workbatch_ordlink接口")
public class WorkbatchOrdlinkController extends BladeController {
    private IWorkbatchOrdlinkService workbatchOrdlinkService;
    @Autowired
    private IOrderOrdinfoService orderOrdinfoService;
    private IProdPdinfoService prodPdinfoService;
    private IOrderWorkbatchService orderWorkbatchService;
    private IWorkbatchOrdoeeService iWorkbatchOrdoeeService;
    private IWorkbatchModulusService iWorkbatchModulusService;
    private IMachineMainfoService iMachineMainfoService;
    private IProdPartsinfoService iProdPartsinfoService;
    private CrmCustomerService crmCustomerService;
    private IProcessWorkinfoService iProcessWorkinfoService;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private StatisOrdreachMapper statisOrdreachMapper;
    @Autowired
    private IWorkbatchShiftsetService IWorkbatchShiftsetService;
    @Autowired
    private IProcessMachlinkService processMachlinkService;
    @Autowired
    private ExecuteTraycardService executeTraycardService;


    @RequestMapping("/getDayList")
    public R getDayList(Integer maId, Integer dpId, Integer wsId) {
        Map<String, Integer> map = workbatchOrdlinkService.getDayList(maId, dpId, wsId);

        return R.data(map);
    }

    /**
     * 导出选择日期,设备,班次下的排产单
     *
     * @param sdDate
     * @param maId
     * @param wsId
     * @return
     */
    @RequestMapping("/XYWorkbatchExcelExport")
    @ApiOperation(value = "导出", notes = "传入ids")
    public R XYWorkbatchExcelExport(String sdDate, Integer maId, Integer wsId) {
        workbatchOrdlinkService.XYWorkbatchExcelExport(sdDate, maId, wsId);
        return null;
    }

    /**
     * 根据设备id查询当前设备的任务数
     */
    @RequestMapping("/saveOrdlinkYS")
    public R saveOrdlinkYS(@RequestBody List<WorkbatchOrdlinkYS> workbatchOrdlinkYSList) {
//        String s = workbatchOrdlinkService.saveOrdlinkYS(saveOrdlinkYS());//测试假数据
        String s = workbatchOrdlinkService.saveOrdlinkYS(workbatchOrdlinkYSList);
        if (!"操作成功".equals(s)) {
            return R.fail(s);
        }
        return R.success(s);
    }


    /**
     * 根据设备id查询当前设备的任务数
     */
    @RequestMapping("/getmaassignment")
    public R getMaAssignment(String startTime, Integer maId, String ckName) {
        return R.data(workbatchOrdlinkService.getMaAssignment(startTime, maId, ckName));
    }

    /**
     * 查询部门下全部设备上的排产记录
     */
    @RequestMapping("/getDeptMachineAll")
    public R getDeptMachineAll(String startTime, Integer dpId, String ckName) {
        return R.data(workbatchOrdlinkService.getDeptMachineAll(startTime, dpId, ckName));
    }

    /**
     * 新增排产数据
     *
     * @param workbatchOrdoee
     * @return
     */
    @RequestMapping("/saveWorkbatchOrdlink")
    public R saveWorkbatchOrdlinkVO(@Valid @RequestBody WorkbatchOrdoeeVo workbatchOrdoee) {
        Boolean aBoolean = false;
        WorkbatchOrdlinkVO workbatchOrdlinkVO = workbatchOrdoee.getWorkbatchOrdlink();
        if (workbatchOrdoee.getId() != null || workbatchOrdlinkVO.getId() != null) {
            aBoolean = workbatchOrdlinkNumber(workbatchOrdlinkVO);//修改时已排产数量是否超过批次总数量
            if (aBoolean) {
                return R.fail("已排产数量超过批次总数量");
            }
            return R.status(workbatchOrdlinkService.updataOrdOEEOrdlink(workbatchOrdoee));//修改排产与相关信息
        }
        aBoolean = workbatchOrdlinkNumber(workbatchOrdlinkVO);//新增时已排产数量是否超过批次总数量
        if (aBoolean) {
            return R.fail("已排产数量超过批次总数量");
        }
        //存储达成率信息
        Integer data = workbatchOrdlinkService.insertOrdOEEOrdlink(workbatchOrdoee);
        StatisOrdreach statisOrdreach = new StatisOrdreach();
        statisOrdreach.setTargetMin(new Date().getMinutes());
        //时间转化
        String targetDay = DateTimeUtil.format(workbatchOrdlinkVO.getStartTime(), DateTimeUtil.DEFAULT_DATE_FORMATTER);
        statisOrdreach.setTargetHour(workbatchOrdlinkVO.getStartTime().getHours());
        statisOrdreach.setTargetDay(targetDay);
        statisOrdreach.setSdId(workbatchOrdlinkVO.getSdId().toString());
        //获取设备信息
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(workbatchOrdlinkVO.getMaId());
        statisOrdreach.setMaId(workbatchOrdlinkVO.getMaId());
        statisOrdreach.setMaName(machineMainfo.getName());
        statisOrdreach.setPdName(workbatchOrdlinkVO.getPdName());
        statisOrdreach.setWbNo(workbatchOrdlinkVO.getOdNo());
        statisOrdreach.setWsId(workbatchOrdlinkVO.getId());
        statisOrdreach.setWsName(workbatchOrdlinkVO.getCkName());
        statisOrdreach.setCreateAt(new Date());
        //

        statisOrdreachMapper.insert(statisOrdreach);


        return R.data(data);//新增排产与相关信息
    }

    /*判断排产的数量是否超出*/
    private Boolean workbatchOrdlinkNumber(WorkbatchOrdlinkVO workbatchOrdlinkVO) {
        int workbatchOrdlinkCount = 0;//同批次同工序下已排产数量
        Integer wbId = workbatchOrdlinkVO.getWbId();//批次id
        /*查询批次信息*/
        OrderWorkbatch orderWorkbatch = orderWorkbatchService.getById(wbId);
        List<WorkbatchOrdlink> workbatchOrdlinkList = workbatchOrdlinkService.getBaseMapper().selectList(new QueryWrapper<WorkbatchOrdlink>()
                .eq("pr_id", workbatchOrdlinkVO.getPrId()).eq("wb_id", wbId).eq("pt_id", workbatchOrdlinkVO.getPtId()));
        if (!workbatchOrdlinkList.isEmpty()) {
            for (WorkbatchOrdlink WorkbatchOrdlink : workbatchOrdlinkList) {
                workbatchOrdlinkCount += WorkbatchOrdlink.getPlanNumber();//同批次同工序下已排产数量
            }
        }
        if (workbatchOrdlinkVO.getId() != null) {//修改时进入,新增时不进入
            WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkService.getById(workbatchOrdlinkVO.getId());
            workbatchOrdlinkCount = workbatchOrdlinkCount - workbatchOrdlink.getPlanNumber();
        }
        int countNum = workbatchOrdlinkCount + workbatchOrdlinkVO.getPlanNumber();//总的应交数;
        if (countNum > orderWorkbatch.getPlanNum()) {
            return true;
        }
        return false;


    }

    /**
     * 查询未提交自审核或审核失败的排产数据
     */
    @RequestMapping("/getWorkbatchOrdlinkVOList")
    @ApiOperation(value = "班次名称", notes = "传入班次名称ckName")
    public R getWorkbatchOrdlinkVOList(String ckName) {
        return R.data(workbatchOrdlinkService.getWorkbatchOrdlinkVOList(ckName));
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入workbatchOrdlink")
    public R<WorkbatchOrdlinkVO> detail(WorkbatchOrdlink workbatchOrdlink) {
        WorkbatchOrdlink detail = workbatchOrdlinkService.getOne(Condition.getQueryWrapper(workbatchOrdlink));
        return R.data(WorkbatchOrdlinkWrapper.build().entityVO(detail));
    }

    /**
     * 分页 生产排产表yb_workbatch_ordlink
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入workbatchOrdlink")
    public R<IPage<WorkbatchOrdlinkVO>> list(WorkbatchOrdlinkVO workbatchOrdlinkVO, Query query) {
        IPage<WorkbatchOrdlinkVO> pages = workbatchOrdlinkService.selectWorkbatchOrdlinkPage(Condition.getPage(query), workbatchOrdlinkVO);
        return R.data(pages);
    }

    /**
     * 自定义分页 生产排产表yb_workbatch_ordlink
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入workbatchOrdlink")
    public R<IPage<WorkbatchOrdlinkVO>> page(WorkbatchOrdlinkVO workbatchOrdlink, Query query) {
        IPage<WorkbatchOrdlinkVO> pages = workbatchOrdlinkService.selectWorkbatchOrdlinkPage(Condition.getPage(query), workbatchOrdlink);
        return R.data(pages);
    }

    /**
     * 新增 生产排产表yb_workbatch_ordlink
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入workbatchOrdlink")
    public R save(@Valid @RequestBody WorkbatchOrdlink workbatchOrdlink) {
        return R.status(workbatchOrdlinkService.save(workbatchOrdlink));
    }

    /**
     * 修改 生产排产表yb_workbatch_ordlink
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入workbatchOrdlink")
    public R update(@Valid @RequestBody WorkbatchOrdlink workbatchOrdlink) {
        return R.status(workbatchOrdlinkService.updateOrdStatus(workbatchOrdlink) > 0 ? true : false);
    }

    /**
     * 新增或修改 生产排产表yb_workbatch_ordlink
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入workbatchOrdlink")
    public R submit(@Valid @RequestBody WorkbatchOrdlink workbatchOrdlink) {
        return R.status(workbatchOrdlinkService.saveOrUpdate(workbatchOrdlink));
    }


    /**
     * 删除 生产排产表yb_workbatch_ordlink
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        List<Integer> list = Func.toIntList(ids);
        for (Integer sdId : list) {
            List<WorkbatchShift> workbatchShifts = workbatchShiftMapper.selectList(Wrappers.<WorkbatchShift>lambdaQuery().eq(WorkbatchShift::getSdId, sdId).select(WorkbatchShift::getId));
            if (!(workbatchShifts.size() > 0)) {
                workbatchOrdlinkService.removeById(sdId);
            }
        }
        return R.success("删除成功");
    }

    /**
     * 导入简单排产信息
     */
    @PostMapping("/simpleExcelInc")
    @ApiOperation(value = "导入简单排产信息", notes = "传入的excel文件")
    @Transactional(rollbackFor = Exception.class)
    public R simpleExcelInc(@RequestParam(value = "file") MultipartFile file) throws Exception {

        Map<String, String> maps = new HashMap<>();
        maps.put("排产时间", "sdDate");
//        maps.put("班次", "ckName");
        maps.put("设备名称", "machineName");
        maps.put("工序名称", "prName");
        maps.put("排产计划数", "planNum");
        maps.put("应交数", "planNumber");
        maps.put("工序放数", "extraNum");
        maps.put("备注说明", "remarks");
        maps.put("订货厂家（可选）", "cmName");
        maps.put("产品名称", "pdName");
        maps.put("产品编号", "pdNo");
        maps.put("订单编号", "odNo");
        maps.put("生产单号", "wbNo");
//        maps.put("应交数", "odCount");

        maps.put("设备标准时速（可不填，默认标准）", "speed");
        maps.put("换模时长（可不填，默认标准）", "mouldStay");

        XSSFWorkbook xssfWorkbook;
        try {
            xssfWorkbook = new XSSFWorkbook(file.getInputStream());
        } catch (Exception e) {
            return R.fail("Excel 文件有问题");
        }
        Set<String> errorList = new HashSet<>();
        List<Map<String, String>> mapList;
        JSONArray jsonData;
        try {
            ExcelErrorVo excelErrorVo = ExcelUtil.readExcel(xssfWorkbook, 0, maps, errorList);//声明错误信息
            errorList = excelErrorVo.getErrorMessageList();
            mapList = excelErrorVo.getResultList();
            mapList.remove(0);
            jsonData = JSONArray.fromObject(mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("Excel模板不正确");
        }
        List<WorkbatchOrdlinkVO> workBatchOrdlinkList = (List<WorkbatchOrdlinkVO>) JSONArray.toList(JSONArray.fromObject(mapList), WorkbatchOrdlinkVO.class);
        List<OrderOrdinfoVO> orderInfoList = (List<OrderOrdinfoVO>) JSONArray.toList(JSONArray.fromObject(mapList), OrderOrdinfoVO.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int count = 2;
        for (WorkbatchOrdlinkVO work : workBatchOrdlinkList) {
            count = count + 1;
            //对时间进行判断
            if (!DateUtil.isValidDate(work.getSdDate())) {
                errorList.add("第" + count + "行的[排产时间]（A）列有问题");
            }
            //对工序名称非null 进行判断
            if (StringUtil.isEmpty(work.getPrName())) {
                errorList.add("第" + count + "行的[工序名称]（E）列有问题");
            }
            //这里对通过工序名称对工序进行判断
            ProcessWorkinfoVO processWorkinfoVO = iProcessWorkinfoService.getProcessWorkInByName(work.getPrName());
            if (processWorkinfoVO == null) {
                return R.fail("第" + count + "行（E）列工序不存在");
            }

            //对计划数量进行判断
            if (work.getPlanNum() == null || work.getPlanNum() == 0) {
                errorList.add("第" + count + "行的[计划数量]（G）列有问题");
            }
            if (StringUtil.isEmpty(work.getWbNo())) {
                errorList.add("第" + count + "行的[生产单号]（I）列有问题");
            }
            //对应交数量进行判断
            if (work.getPlanNumber() == null || work.getPlanNumber() == 0) {
                errorList.add("第" + count + "行的[应交数量]（J）列有问题");
            }

            /*String release = work.getRelease();
            //对冗余数进行判断
            String trim = release.trim();
            if (trim == null || !trim.matches("^[0-9]*$")) {
                errorList.add("第" + count + "行的[工序放数]（I）列有问题");
            }*/
            if (work.getExtraNum() == null) {
                errorList.add("第" + count + "行的[工序放数]（H）列有问题");
            }
            //对产品名进行判断
            String pdName = work.getPdName();
            if (StringUtil.isEmpty(pdName)) {
                errorList.add("第" + count + "行的[产品名称]（C）列有问题");
            }
            //对产品编号进行判断
            if (StringUtil.isEmpty(work.getPdNo())) {
                errorList.add("第" + count + "行的[产品编号]（D）列有问题");
            }
            //对备注进行判断
            /*if (work.getRemarks() == null) {
                errorList.add("第" + count + "行的[备注]（K）列有问题");
            }*/
            //对设备名称进行判断
            if (StringUtil.isEmpty(work.getMachineName())) {
                errorList.add("第" + count + "行的[设备名称]（B）列有问题");
            }
            MachineMainfo machineMainfo =
                    iMachineMainfoService.getOne(new QueryWrapper<MachineMainfo>().eq("name", work.getMachineName()));
            if (machineMainfo == null) {
                return R.fail("第" + count + "行（C）列,设备不存在");
            }
            ProcessMachlink processMachlink =
                    processMachlinkService.getOne(new QueryWrapper<ProcessMachlink>().eq("pr_id", processWorkinfoVO.getId()).eq("ma_id", machineMainfo.getId()));
            if (processMachlink == null) {
                errorList.add("第" + count + "行,工序设备未绑定");
            }
        }

        count = 2;
        for (OrderOrdinfoVO orders : orderInfoList) {
            count = count + 1;
            //订单编号
            if (StringUtil.isEmpty(orders.getOdNo())) {
                errorList.add("第" + count + "行的[订单编号]（G）列有问题");
            }
        }
        // 当没有错误的情况下 进行插入
        int row = 2;
        if (errorList == null || errorList.size() == 0) {
            for (int i = 0; i < jsonData.size(); i++) {
                row++;
                JSONObject jason = jsonData.getJSONObject(i);
                // 首先处理最上层的product
                ProdPdinfoVO product = (ProdPdinfoVO) JSONObject.toBean(jason, ProdPdinfoVO.class);//产品信息
                OrderOrdinfoVO orderInfo = (OrderOrdinfoVO) JSONObject.toBean(jason, OrderOrdinfoVO.class);//订单信息
                WorkbatchOrdoeeVo workbatchOrdoee = (WorkbatchOrdoeeVo) JSONObject.toBean(jason, WorkbatchOrdoeeVo.class);//排产oee
                WorkbatchOrdlinkVO workbatchOrdlink = (WorkbatchOrdlinkVO) JSONObject.toBean(jason, WorkbatchOrdlinkVO.class);//排产信息
                ProdPdinfoVO pro = prodPdinfoService.getProdPdinfoVoBypdNo(product.getPdNo());//查询产品是否存在
                //对订单的订单编号 如果是纯数字进行修改
                if (isDouble(orderInfo.getOdNo())) {
                    Double odDouble = Double.parseDouble(orderInfo.getOdNo());
                    Integer odInter = odDouble.intValue();
                    orderInfo.setOdNo(odInter.toString());
                }
                if (pro == null) {
                    prodPdinfoService.saveOrUpdate(product);
                    pro = product;
                }
                OrderOrdinfoVO orderVo = orderOrdinfoService.getOrderinfoByObno(orderInfo.getOdNo());//判断订单是否存在
                if (orderVo == null) {
                    orderInfo.setPdId(pro.getId());
                    Date date = new Date();
                    orderInfo.setOdName(product.getPdName());
                    orderInfo.setLimitDate(date);//截止日期
                    orderInfo.setCreateAt(date);
                    CrmCustomerVO crmCustomerVO = crmCustomerService.getOneByCmName(orderInfo.getCmName());
                    if (crmCustomerVO != null) {
                        orderInfo.setCmId(crmCustomerVO.getId());
                        orderInfo.setCmShortname(crmCustomerVO.getCmShortname());
                    }
                    orderInfo.setAuditStatus(-1);//审核状态 -1草稿  0提交至技术中心  1 提交至工艺审核  2 审核中  3审核通过  ',
                    orderInfo.setProductionState(0);//生产状态   0 未执行  1 正在执行 2 已完成',
                    orderOrdinfoService.saveOrUpdate(orderInfo);
                    orderVo = orderInfo;
                }
                //当ordworkBatch 的odNo 为null 的时候我们就默认没有这个批次那么这个批次就是一个订单
                OrderWorkbatch orderWorkbatchVo =
                        orderWorkbatchService.getOne(new QueryWrapper<OrderWorkbatch>().eq("od_no", orderVo.getOdNo()));
                Date date = new Date();
                if (orderWorkbatchVo == null) {
                    OrderWorkbatchVO orderWorkbatch = new OrderWorkbatchVO();
                    orderWorkbatch.setOdId(orderVo.getId());
                    orderWorkbatch.setOdNo(orderVo.getOdNo());//订单编号
                    //批次的编号，数量，消耗数都会来自于订单表
                    orderWorkbatch.setBatchNo(orderVo.getOdNo() + "_1");//批次编号
                    orderWorkbatch.setPlanNum(orderVo.getOdCount() == null ? 0 : orderVo.getOdCount());// '计划数量',
                    orderWorkbatch.setCreateAt(date);
                    orderWorkbatch.setCloseTime(date);
                    orderWorkbatch.setStatus(1);
                    orderWorkbatchService.saveOrUpdate(orderWorkbatch);
                    orderWorkbatchVo = orderWorkbatch;
                }
                // 获取设备的id
                MachineMainfo machineMainfo =
                        iMachineMainfoService.getOne(new QueryWrapper<MachineMainfo>().eq("name", workbatchOrdlink.getMachineName()));
                /*查询工序信息*/
                ProcessWorkinfoVO processWorkinfoVO =
                        iProcessWorkinfoService.getProcessWorkInByName(workbatchOrdlink.getPrName());
                //排产表
                Integer maId = machineMainfo.getId();//设备id
                Integer prId = processWorkinfoVO.getId();//工序id
                workbatchOrdlink.setOdNo(orderVo.getOdNo());
                workbatchOrdlink.setBatchNo(orderWorkbatchVo.getBatchNo());
                workbatchOrdlink.setMaId(maId);
                workbatchOrdlink.setWbId(orderWorkbatchVo.getId());
                workbatchOrdlink.setPartName(product.getPdName());
                //Integer extraNum = Integer.valueOf(workbatchOrdlink.getRelease());
                // workbatchOrdlink.setExtraNum(extraNum);
                workbatchOrdlink.setPdCode(product.getPdNo());
                workbatchOrdlink.setCloseTime(date);
                workbatchOrdlink.setPlanTime(0);
                workbatchOrdlink.setDpId(machineMainfo.getDpId());
                /*int number = workbatchOrdlink.getPlanNum() - workbatchOrdlink.getExtraNum();
                workbatchOrdlink.setPlanNumber(number);*/
                workbatchOrdlink.setIncompleteNum(workbatchOrdlink.getPlanNumber());
                workbatchOrdlink.setCompleteNum(0);
                workbatchOrdlink.setDutyNum(0);
                workbatchOrdlink.setStartTime(date);
                //这里对工序的缺少的字段进行添加
                workbatchOrdlink.setPrId(processWorkinfoVO.getId());
                workbatchOrdlink.setSort(processWorkinfoVO.getSort());
                workbatchOrdlink.setRunStatus(0);
                workbatchOrdlink.setStatus("0");
//                workbatchOrdlink.setSdSort(String.valueOf(sdSort));
                workbatchOrdlinkService.saveOrUpdate(workbatchOrdlink);

                /*排产oee*/
                Integer mouldStay = workbatchOrdlink.getMouldStay();
                Integer speed = workbatchOrdlink.getSpeed();
                if (mouldStay == null || speed == null) {
                    ProcessMachlink processMachlink =
                            processMachlinkService.getOne(new QueryWrapper<ProcessMachlink>().eq("pr_id", prId).eq("ma_id", maId));
                    if (processMachlink == null) {
                        return R.fail("第" + row + "行,工序设备未绑定");
                    }
                    if (mouldStay == null) {
                        mouldStay = processMachlink.getPrepareTime();
                    }
                    if (speed == null) {
                        speed = processMachlink.getSpeed();
                    }
                }
                if (speed == 0) {
                    speed = 1;
                }
                Integer ptId = workbatchOrdlink.getPtId();
                if (ptId != null) {
                    workbatchOrdoee.setBeforePtid(ptId);
                }
                workbatchOrdoee.setDifficultNum(0.0);//难易程度

                workbatchOrdoee.setProducePreTime(mouldStay);//生产准备时间

//                workbatchOrdoee.setQualityNum(0);//质检次数
                workbatchOrdoee.setWkId(workbatchOrdlink.getId());
                workbatchOrdoee.setMouldNum(1);
                workbatchOrdoee.setBeforePtname(workbatchOrdlink.getPartName());//部件名称
                workbatchOrdoee.setMouldStay(mouldStay);//设置换膜时长
                workbatchOrdoee.setSpeed(speed);//设置设备对应工序速度
                iWorkbatchOrdoeeService.saveOrUpdate(workbatchOrdoee);
            }
            return R.data("success");
        } else {
            return R.fail(errorList.toString());
        }

    }


    @PostMapping("/readExcel")
    @ApiOperation(value = "传入数据批量操作", notes = "传入的excel文件")
    public R readExcel(@RequestParam(value = "file") MultipartFile file) throws ParseException, FileNotFoundException {
        Map<String, String> maps = new HashMap<>();
        maps.put("排产时间", "execelOrderLinkTime");
        maps.put("班次", "ckName");
        maps.put("计划人数", "dutyNum");
        maps.put("设备名称", "machineName");
        maps.put("设备编号", "machineNo");
        maps.put("工序名称", "prName");
        maps.put("计划数量", "planNum");
        maps.put("已完成数量", "completeNum");
        maps.put("损耗数", "wasteCount");
        maps.put("应交时间", "excelCloseTime");
        maps.put("难易程度", "difficultNum");
        maps.put("订单编号", "odNo");
        maps.put("订单名称", "odName");
        maps.put("订货厂家", "cmName");
        maps.put("合同编号", "contractNum");
        maps.put("合同名称", "contractName");
        maps.put("截止时间", "excellimitDate");
        maps.put("订单数量", "odCount");
        maps.put("产品名称", "pdName");
        maps.put("产品编号", "pdNo");
        maps.put("产品类型", "pdType");
        maps.put("产品损耗率", "nbwaste");
        maps.put("部件名称", "ptName");
        maps.put("部件编号", "ptNo");
        maps.put("转后部件名称", "afterPtName");
        maps.put("转后部件编号", "afterPtNo");
        maps.put("转化率", "excelModulus");
        maps.put("批次编号", "batchNo");
        maps.put("批次数量", "pcPlanNumber");
        maps.put("批次损耗", "wasteTotal");
        maps.put("批次截止时间", "excelCloseTime");

        maps.put("保养次数", "maintainNum");
        maps.put("保养类型", "maintain");
        maps.put("保养时长", "maintainStay");
        maps.put("换模次数", "mouldNum");
        maps.put("每次换模时长", "mouldStay");
        maps.put("生产准备时间", "producePreTime");
        maps.put("吃饭次数", "mealNum");
        maps.put("每次吃饭时间", "mealStay");
        maps.put("质检次数", "qualityNum");

        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(file.getInputStream());
        } catch (Exception e) {
            return R.fail("Excel 文件有问题");
        }
        Set<String> errorList = new HashSet<>();
        ExcelErrorVo excelErrorVo = ExcelUtil.readExcel(xssfWorkbook, 0, maps, errorList);//声明错误信息
        errorList = excelErrorVo.getErrorMessageList();
        List<Map<String, String>> mapList = excelErrorVo.getResultList();
        mapList.remove(0);
        JSONArray jsonData = JSONArray.fromObject(mapList);
        List<WorkbatchOrdlinkVO> workBatchOrdlinkList = (List<WorkbatchOrdlinkVO>) JSONArray.toList(JSONArray.fromObject(mapList), WorkbatchOrdlinkVO.class);
        List<OrderOrdinfoVO> orderInfoList = (List<OrderOrdinfoVO>) JSONArray.toList(JSONArray.fromObject(mapList), OrderOrdinfoVO.class);
        List<WorkbatchOrdoeeVo> workbatchOrdoeeVoList = (List<WorkbatchOrdoeeVo>) JSONArray.toList(JSONArray.fromObject(mapList), WorkbatchOrdoeeVo.class);
        List<WorkbatchModulusVO> workbatchModulusVOList = (List<WorkbatchModulusVO>) JSONArray.toList(JSONArray.fromObject(mapList), WorkbatchModulusVO.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //对OrderWorkbatch定制化错误的判断 可以进行添加
        int count = 2;
        for (WorkbatchOrdlinkVO work : workBatchOrdlinkList) {
            count = count + 1;
            //对时间进行判断
            if (!DateUtil.isValidDate(work.getExecelOrderLinkTime())) {
                errorList.add("第" + count + "行的[排产时间]（A）列有问题");
            }
            //对班次进行判断白班/夜班/晚班/
            Map<String, String> classMap = new HashMap<>();
            classMap.put("白班", "1");
            classMap.put("夜班", "2");
            classMap.put("晚班", "3");
            String result = classMap.get(work.getCkName());
            if (result == null) {
                errorList.add("第" + count + "行的[班次]（B）列有问题");
            }
            //TODO 先简单的验证后面在认真验证 白班验证
//            Integer countProcess = 0;
//            MachineMainfoVO machineMainfoVO = null;
//            //首先判断我们要验证的process的字体和非空
//            if (classMap.get(work.getCkName())==null){
//                errorList.add("第" + count + "列的班次有问题");
//                countProcess=1;
//            }
//            //model 4 需要的条件的非空验证
//            if (countProcess==0) {
//                if (work.getMachineName() != null && work.getMachineNo() != null) {
//                    errorList.add("第" + count + "列的设备名字和设备编号有问题");
//                    countProcess=1;
//                }
//            }
//            //首先是设备 model =4
//            if (countProcess==0){
//                 machineMainfoVO = iMachineMainfoService.getMachineInfoByCondition(work.getMachineName(), work.getMachineNo());
//            }
//            //第四步不等于null 的判断
//            if (machineMainfoVO!=null && countProcess==0){
//                Integer model =4;
//                List<WorkbatchShiftsetVO> workBathchList = workbatchShiftsetService.getWorkbatchByCondition(model, machineMainfoVO.getId());
//                if (workBathchList.size()>0){
//                   if (workBathchList.get(0).getClassify().equals(classMap.get(work.getCkName()))){
//                       countProcess=1;
//                   }
//                }
//            }
//            //其次是工艺工序的条件判断
//            if (countProcess==0){
//                if (work.getPrName()!=null) {
//                    errorList.add("第" + count + "列的工序名称有问题");
//                    countProcess=1;
//                }
//            }
//            //第三步骤 工序的判断
//             if (countProcess==0){
//                 Integer model=3;
//                 ProcessWorkinfoVO workinfoVO = iProcessWorkinfoService.getProcessWorkInByName(work.getPartName());
//                 List<WorkbatchShiftsetVO> workBathchList = workbatchShiftsetService.getWorkbatchByCondition(model, workinfoVO.getId());
//                 if (workBathchList.size()>0){
//                     if (workBathchList.get(0).getClassify().equals(classMap.get(work.getCkName()))){
//                         countProcess=1;
//                     }
//                 }
//             }
            //对工序名称非null 进行判断
            if (work.getPrName() == null) {
                errorList.add("第" + count + "行的[工序名称]（E）列有问题");
            } else {
                //这里对通过工序名称对工序进行判断
                ProcessWorkinfoVO processWorkinfoVO = iProcessWorkinfoService.getProcessWorkInByName(work.getPrName());
                if (processWorkinfoVO == null) {
                    errorList.add("第" + count + "行的[工序名称]（E）列有问题");
                }
            }
            if (work.getDutyNum() == null) {
                errorList.add("第" + count + "行的[计划人数]（M）列有问题");
            }
            //对计划数量进行判断
            if (work.getPlanNum() == null) {
                errorList.add("第" + count + "行的[计划数量]（F）列有问题");
            }
            if (work.getCompleteNum() == null) {
                errorList.add("第" + count + "行的[完成数量]（G）列有问题");
            }
            //应交时间
            if (work.getExcelCloseTime() == null) {
                errorList.add("第" + count + "行的[应交时间]（I）列有问题");
            } else {
                if (!DateUtil.isValidDate(work.getExcelCloseTime())) {
                    errorList.add("第" + count + "行的[应交时间]（I）列有问题");
                }
            }
            MachineMainfo machineMainfo = iMachineMainfoService.getMachineInfoByCondition(work.getMachineName(), work.getMachineNo());
            if (machineMainfo == null) {
                errorList.add("第" + count + "行的[设备名称](C)列或者[设备编号](D)有问题");
            }
        }
        count = 2;
        for (OrderOrdinfoVO orders : orderInfoList) {
            count = count + 1;
            //订单编号
            if (orders.getOdNo() == null) {
                errorList.add("第" + count + "行的[订单编号]（K）列有问题");
            }
            //订单名称
            if (orders.getOdName() == null) {
                errorList.add("第" + count + "行的[订单名称]（L）列有问题");
            }
            //订货厂家
            if (orders.getCmName() == null) {
                errorList.add("第" + count + "行的[订货厂家](N)列有问题");
            } else {
//                查询是否存在此订货厂家
                CrmCustomerVO crmCustomerVO = crmCustomerService.getOneByCmName(orders.getCmName());
                if (Func.isEmpty(crmCustomerVO)) {
                    errorList.add("第" + count + "行的[订货厂家](M)列有问题");
                }
            }
            //截止时间
            if (orders.getExcellimitDate() == null) {
                errorList.add("第" + count + "行的[截止时间](Q)列有问题");
            } else {
                if (!DateUtil.isValidDate(orders.getExcellimitDate())) {
                    errorList.add("第" + count + "行的[截止时间](Q)列有问题");
                }
            }
            //订单数量
            if (orders.getOdCount() == null) {
                errorList.add("第" + count + "行的[订单数量](R)列有问题");
            }
            if (orders.getWasteCount() == null) {
                errorList.add("第" + count + "行的[损耗数]（H）列有问题");
            }
        }
        count = 2;
        for (WorkbatchOrdoeeVo oee : workbatchOrdoeeVoList) {
            count = count + 1;
            //换膜次数
            if (oee.getMouldNum() == null) {
                errorList.add("第" + count + "行的[换膜次数](AI)列有问题");
            }
            //每次换膜时长
            if (oee.getMouldStay() == null) {
                errorList.add("第" + count + "行的[换膜次数](AJ)列有问题");
            }
        }
        count = 2;
        for (WorkbatchModulusVO workbatchModulusVO : workbatchModulusVOList) {
            count = count + 1;
            //部件名称
            if (workbatchModulusVO.getPtName() == null) {
                errorList.add("第" + count + "行的[部件名称](V)列有问题");
            }
            //部件编号
            if (workbatchModulusVO.getPtNo() == null) {
                errorList.add("第" + count + "行的[部件编号](W)列有问题");
            }
            //转化后部件名称
            if (workbatchModulusVO.getAfterPtName() == null) {
                errorList.add("第" + count + "行的[转后部件名称](X)列有问题");
            }
            //转化后部件编号
            if (workbatchModulusVO.getAfterPtNo() == null) {
                errorList.add("第" + count + "行的[转后部件编号](Y)列有问题");
            }
        }
        // 当没有错误的情况下 进行插入
        if (errorList == null || errorList.size() == 0) {
            for (int i = 0; i < jsonData.size(); i++) {
                JSONObject jason = jsonData.getJSONObject(i);
                // 首先处理最上层的product
                ProdPdinfoVO product = (ProdPdinfoVO) JSONObject.toBean(jason, ProdPdinfoVO.class);
                OrderOrdinfoVO orderInfo = (OrderOrdinfoVO) JSONObject.toBean(jason, OrderOrdinfoVO.class);
                OrderWorkbatchVO orderWorkbatch = (OrderWorkbatchVO) JSONObject.toBean(jason, OrderWorkbatchVO.class);
                WorkbatchModulusVO workbatchModulus = (WorkbatchModulusVO) JSONObject.toBean(jason, WorkbatchModulusVO.class);
                WorkbatchOrdoeeVo workbatchOrdoee = (WorkbatchOrdoeeVo) JSONObject.toBean(jason, WorkbatchOrdoeeVo.class);
                ProdPartsinfoVo prodPartsinfoVo = (ProdPartsinfoVo) JSONObject.toBean(jason, ProdPartsinfoVo.class);
                WorkbatchOrdlinkVO workbatchOrdlink = (WorkbatchOrdlinkVO) JSONObject.toBean(jason, WorkbatchOrdlinkVO.class);
                ProdPdinfoVO pro = prodPdinfoService.getProdPdinfoVoBypdNo(product.getPdNo());//查询产品是否存在
                //对订单的订单编号 如果是纯数字进行修改
                if (isDouble(orderInfo.getOdNo())) {
                    Double odDouble = Double.parseDouble(orderInfo.getOdNo());
                    Integer odInter = odDouble.intValue();
                    orderInfo.setOdNo(odInter.toString());
                }
                if (pro == null) {
                    prodPdinfoService.saveOrUpdate(product);
                    pro = product;
                }
                OrderOrdinfoVO orderVo = orderOrdinfoService.getOrderinfoByObno(orderInfo.getOdNo());//判断订单是否存在
                if (orderVo == null) {
                    orderInfo.setPdId(pro.getId());
                    Date data = simpleDateFormat.parse(orderInfo.getExcellimitDate());
                    orderInfo.setLimitDate(data);
                    CrmCustomerVO crmCustomerVO = crmCustomerService.getOneByCmName(orderInfo.getCmName());
                    orderInfo.setCmId(crmCustomerVO.getId());
                    orderInfo.setCmShortname(crmCustomerVO.getCmShortname());
                    orderInfo.setAuditStatus(-1);
                    orderInfo.setProductionState(0);
                    orderOrdinfoService.saveOrUpdate(orderInfo);
                    orderVo = orderInfo;
                }
                OrderWorkbatchVO orderWorkbatchVo = null;
                //当ordworkBatch 的odNo 为null 的时候我们就默认没有这个批次那么这个批次就是一个订单
                if (orderWorkbatch.getBatchNo() == null || orderWorkbatch.getBatchNo() == "") {
                    orderWorkbatch.setOdId(orderVo.getId());
                    orderWorkbatch.setOdNo(orderVo.getOdNo());
                    //批次的编号，数量，消耗数都会来自于订单表
                    orderWorkbatch.setBatchNo(orderVo.getOdNo());
                    orderWorkbatch.setPlanNum(orderVo.getOdCount());
                    orderWorkbatch.setWaste(orderVo.getWasteCount());
                    Date data = simpleDateFormat.parse(orderWorkbatch.getExcelCloseTime());
                    orderWorkbatch.setCloseTime(data);
                    orderWorkbatch.setWaste(orderInfo.getWasteCount());
                    orderWorkbatch.setStatus(1);
                    orderWorkbatchService.saveOrUpdate(orderWorkbatch);
                    orderWorkbatchVo = orderWorkbatch;
                }
                //判断批次是存在
                orderWorkbatchVo = orderWorkbatchService.findObjectBybatchNo(orderWorkbatch.getBatchNo());//判断批次是否存在；
                //批次有信息 一个订单分为多个批次的情况
                if (orderWorkbatchVo == null) {
                    orderWorkbatch.setOdId(orderVo.getId());
                    orderWorkbatch.setOdNo(orderVo.getOdNo());
                    Date data = simpleDateFormat.parse(orderWorkbatch.getExcelCloseTime());
                    orderWorkbatch.setCloseTime(data);
                    orderWorkbatch.setWaste(orderWorkbatch.getWasteTotal());
                    orderWorkbatch.setStatus(1);
                    orderWorkbatchService.saveOrUpdate(orderWorkbatch);
                    OrderWorkbatchVO orderWorkbatchVos = orderWorkbatchService.findObjectBybatchNo(orderWorkbatch.getBatchNo());
                    orderWorkbatchVo = orderWorkbatchVos;
                }
                // 获取设备的id
                MachineMainfo machineMainfo = iMachineMainfoService.getMachineInfoByCondition(workbatchOrdlink.getMachineName(), workbatchOrdlink.getMachineNo());
                //排产表
                workbatchOrdlink.setOdNo(orderVo.getOdNo());
                workbatchOrdlink.setMaId(machineMainfo.getId());
                workbatchOrdlink.setWbId(orderWorkbatchVo.getId());
                workbatchOrdlink.setPartName(workbatchModulus.getPtName());
                workbatchOrdlink.setPdCode(product.getPdNo());
                Date data = simpleDateFormat.parse(orderWorkbatch.getExcelCloseTime());
                workbatchOrdlink.setCloseTime(data);
                Date orderLinkTime = simpleDateFormat.parse(orderWorkbatch.getExcelCloseTime());
                //TODO:报错workbatchOrdlink.setSdDate(orderLinkTime);
                workbatchOrdlink.setExtraNum(orderVo.getWasteCount());
                workbatchOrdlink.setPlanNumber(workbatchOrdlink.getPlanNum() + workbatchOrdlink.getExtraNum());
                workbatchOrdlink.setIncompleteNum(workbatchOrdlink.getPlanNum() - workbatchOrdlink.getCompleteNum());
                //这里对工序的缺少的字段进行添加
                ProcessWorkinfoVO processWorkinfoVO = iProcessWorkinfoService.getProcessWorkInByName(workbatchOrdlink.getPrName());
                workbatchOrdlink.setPrId(processWorkinfoVO.getId());
                workbatchOrdlink.setSort(processWorkinfoVO.getSort());
                workbatchOrdlink.setRunStatus(0);
                workbatchOrdlink.setStatus("1");
                workbatchOrdlinkService.saveOrUpdate(workbatchOrdlink);

                //在这里获取我们刚刚插入的排产数据然后使用id做外键
                String[] pdarrs = workbatchModulus.getAfterPtNo().split("#");
                String[] pnarrs = workbatchModulus.getAfterPtName().split("#");
                String[] ratearr = workbatchModulus.getExcelModulus().split("#");

                Map<String, Object> moudulesMap = new HashMap<>();
                moudulesMap.put("pt_no", workbatchModulus.getPtNo());
                List<WorkbatchModulus> moudleList = iWorkbatchModulusService.getBaseMapper().selectByMap(moudulesMap);
                //我们在这里需要通过产品id去查询产品部件表有着进行绑定没有则进行创建
                List<ProdPartsinfo> prodPartsinfos = iProdPartsinfoService.getBaseMapper().selectByMap(moudulesMap);
                if (prodPartsinfos.size() == 0) {
                    prodPartsinfoVo.setPdId(pro.getId());
                    iProdPartsinfoService.saveOrUpdate(prodPartsinfoVo);
                    //这里将orderee 的部件id update上去
                    workbatchOrdoee.setBeforePtid(prodPartsinfoVo.getId());
                    workbatchOrdlink.setPtId(prodPartsinfoVo.getId());
                    workbatchOrdlinkService.updateById(workbatchOrdlink);//导入新增
                } else {
                    workbatchOrdoee.setBeforePtid(prodPartsinfos.get(0).getId());
                    workbatchOrdlink.setPtId(prodPartsinfos.get(0).getId());
                    workbatchOrdlinkService.updateById(workbatchOrdlink);//导入新增
                }
                if (moudleList.size() == 0) {
                    workbatchModulus.setWeId(workbatchOrdoee.getId());
                    workbatchModulus.setModulus((int) Double.parseDouble(ratearr[0]));
                    iWorkbatchModulusService.saveOrUpdate(workbatchModulus);
                }
                //因为排产表和我们的orderee 的表一一对应的所以我们直接插入这2张表
                workbatchOrdoee.setWkId(workbatchOrdlink.getId());
                workbatchOrdoee.setBeforePtname(pnarrs[0]);
                iWorkbatchOrdoeeService.saveOrUpdate(workbatchOrdoee);

                //现在存储转换后的对象
                if (pdarrs.length != 0) {
                    for (int x = 0; x < pdarrs.length; x++) {
                        Map<String, Object> mapConditon = new HashMap<>();
                        mapConditon.put("pt_no", pdarrs[x]);
                        List<WorkbatchModulus> ListResult = iWorkbatchModulusService.getBaseMapper().selectByMap(mapConditon);
                        if (ListResult.size() == 0) {
                            WorkbatchModulus workbatchModuluss = new WorkbatchModulus();
                            double dou = Double.parseDouble(ratearr[x]);
                            workbatchModuluss.setModulus((int) dou);
                            workbatchModuluss.setWeId(workbatchOrdoee.getId());
                            workbatchModuluss.setPtName(pnarrs[x]);
                            workbatchModuluss.setPtNo(pdarrs[x]);
                            workbatchModuluss.setPtId(workbatchModulus.getId());
                            iWorkbatchModulusService.saveOrUpdate(workbatchModuluss);
                        }
                        List<ProdPartsinfo> ListRes = iProdPartsinfoService.getBaseMapper().selectByMap(mapConditon);
                        if (ListRes.size() == 0) {
                            double dou = Double.parseDouble(ratearr[x]);
                            ProdPartsinfo partsinfo = new ProdPartsinfo();
                            partsinfo.setPdId(workbatchModulus.getPtId());
                            partsinfo.setPid(prodPartsinfoVo.getId());
                            partsinfo.setPtName(pnarrs[x]);
                            partsinfo.setPtNo(pdarrs[x]);
                            iProdPartsinfoService.saveOrUpdate(partsinfo);
                        }
                    }
                }

            }
            return R.data("success");
        } else {
            return R.fail(errorList.toString());
        }
    }

    public static boolean isDouble(String str) {
        boolean bCheckResult = true;
        try {
            Double dCheckValue = Double.parseDouble(str);
            if (dCheckValue instanceof Double == false) {
                bCheckResult = false;
            }
        } catch (Exception e) {
            bCheckResult = false;
        }
        return bCheckResult;
    }

    /**
     * 查询某一生产排产表yb_workbatch_ordlink
     */
    @GetMapping("/planProduceDetail")
    @ApiOperation(value = "分页", notes = "传入workbatchOrdlink")
    public R<PlanProduceModelVO> planProduceDetail(WorkbatchOrdlinkVO workbatchOrdlink) {
        return R.data(workbatchOrdlinkService.planProduceDetail(workbatchOrdlink));
    }

    /**
     * 自定义分页 生产排产表yb_workbatch_ordlink
     */
    @GetMapping("/planProducePage")
    @ApiOperation(value = "分页", notes = "传入workbatchOrdlink")
    public R<IPage<PlanProduceModelVO>> planProducePage(WorkbatchOrdlinkVO workbatchOrdlink, Query query) {
        IPage<PlanProduceModelVO> pages = workbatchOrdlinkService.planProducePage(Condition.getPage(query), workbatchOrdlink);
        return R.data(pages);
    }

    /**
     * 自定义分页 生产排产表yb_workbatch_ordlink
     */
    @GetMapping("/getWorkbatchOEEById")
    @ApiOperation(value = "分页", notes = "id")
    public R getWorkbatchOEEById(Integer id) {
        /*
         * 前端那个表格傲娇的很，不是List渲染不出数据
         * */
        List<WorkbatchOrdoeeVo> batchOrdoee = new ArrayList<>();
        batchOrdoee.add(workbatchOrdlinkService.getWorkbatchOEEById(id));
        return R.data(batchOrdoee);
    }

    /**
     * 自定义分页 生产排产表yb_workbatch_ordlink
     */
    @GetMapping("/planProduceToActSetPage")
    @ApiOperation(value = "分页", notes = "id")
    public R planProduceToActSetPage(WorkbatchOrdlinkVO workbatchOrdlink, Query query) {
        IPage<PlanProduceModelVO> pages =
                workbatchOrdlinkService.planProduceToActSetPage(Condition.getPage(query), workbatchOrdlink);
        return R.data(pages);
    }

    /**
     * 提交待审核
     */
    @GetMapping("/saveCheckLogForPlanPro")
    @ApiOperation(value = "分页", notes = "id")
    public R saveCheckLogForPlanPro(String asType, String awType, Integer orderId) {
        if (SaveCheckLogUnit.saveCheckLog(asType, awType, orderId)) { //提交审核信息成功
            WorkbatchOrdlink ordlink = workbatchOrdlinkService.getById(orderId);
            ordlink.setStatus("6");//已提交到审核
            workbatchOrdlinkService.updateOrdStatus(ordlink);
        }
        return R.data("提交审核成功!");
    }

    /**
     * 驳回到排产
     */
    @PostMapping("/returnWorkbatch")
    public R ReturnWorkbatch(@RequestBody WorkbatchOrdlinkVO ordlinklink) {
        WorkbatchOrdlinkShiftVO ordlink = workbatchOrdlinkService.getById(ordlinklink.getId());
//        ordlink.setStatus("7");//驳回排产
//        ordlink.setRemarks(ordlinklink.getRemarks());//驳回排产的备注
//        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(ordlink.getWsId());
        WorkbatchShiftset workbatchShift = IWorkbatchShiftsetService.selectByMaid(ordlink.getWsId(), ordlink.getMaId());
        workbatchShiftMapper.deleteById(workbatchShift);
        WorkbatchOrdoee workbatchOrdoee = iWorkbatchOrdoeeService.getOne(new QueryWrapper<WorkbatchOrdoee>().eq("wk_id", ordlinklink.getId()));
        iWorkbatchOrdoeeService.removeById(workbatchOrdoee.getId());
        workbatchOrdlinkService.removeById(ordlink);
        return R.data("提交审核成功!");
    }

    @RequestMapping("/WorkbatchOrdlinkoeeExcel")
    @ApiOperation(value = "分页", notes = "id")
    public R<IPage<WorkbatchOrdlinkVO>> WorkbatchOrdlinkoeeExcel(WorkbatchOrdlinkVO workbatchOrdlinkVO, Query query) {
        IPage<WorkbatchOrdlinkVO> ordlink = workbatchOrdlinkService.WorkbatchOrdlinkoeeExcel(workbatchOrdlinkVO, Condition.getPage(query));
        return R.data(ordlink);
    }

    @RequestMapping("/WorkbatchOrdlinkExcelExport")
    @ApiOperation(value = "导出", notes = "传入ids")
    public R WorkbatchOrdlinkExcelExport(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        workbatchOrdlinkService.WorkbatchOrdlinkExcelExport(Func.toIntList(ids));
        return null;
    }

    @RequestMapping("/workbatchOrdlinkById")
    @ApiOperation(value = "详情", notes = "传入id")
    public R workbatchOrdlinkById(Integer id) {
        WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkService.getById(id);
        OrderOrdinfo orderOrdinfo =
                orderOrdinfoService.getOne(new QueryWrapper<OrderOrdinfo>().eq("od_no", workbatchOrdlink.getOdNo()));
//        WorkbatchOrdlinkVO workbatchOrdlinkVO = WorkbatchOrdlinkWrapper.build().entityVO(workbatchOrdlink);
//        workbatchOrdlinkVO.setIndentor(orderOrdinfo.getCmName());//客户名称
        Map<String, Object> map = new HashMap();
        map.put("orderLink", workbatchOrdlink);
        map.put("workBatch", orderOrdinfo);
        return R.data(map);
    }

    /**
     * 更改排序
     *
     * @param id
     * @param type(0上移,1下移)
     * @return
     */
    @GetMapping("/setSdSort")
    @ApiOperation(value = "分页", notes = "id and type")
    public R setSdSort(Integer id, Integer type) {
//        查询此id的排产信息
        WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkService.getById(id);
        String sdSort = workbatchOrdlink.getSdSort();
        if (type == 0) {
//        查询此id的上一条排产信息
            List<WorkbatchOrdlink> upList = workbatchOrdlinkService.getSortUP(id);
            if (Func.isEmpty(upList)) {
                return R.fail("此排产单已在首位");
            } else {
                workbatchOrdlink.setSdSort(upList.get(0).getSdSort());
                workbatchOrdlinkService.updateOrdStatusBySdsort(workbatchOrdlink);
                upList.stream().forEach(element -> {
                    element.setSdSort(sdSort);
                    workbatchOrdlinkService.updateOrdStatusBySdsort(element);
                });
                return R.success("排序已修改为" + workbatchOrdlink.getSdSort());
            }
        } else {
//        查询此id的下一条排产信息
            List<WorkbatchOrdlink> downList = workbatchOrdlinkService.getSortDown(id);
            if (Func.isEmpty(downList)) {
                return R.fail("此排产单已在末位");
            } else {
                workbatchOrdlink.setSdSort(downList.get(0).getSdSort());
                workbatchOrdlinkService.updateOrdStatusBySdsort(workbatchOrdlink);
                downList.stream().forEach(element -> {
                    element.setSdSort(sdSort);
                    workbatchOrdlinkService.updateOrdStatusBySdsort(element);
                });
                return R.success("排序已修改为" + workbatchOrdlink.getSdSort());
            }
        }
    }

    @GetMapping("/getCmNameOdNoVOList")
    @ApiOperation(value = "查询在制品的客户名称和订单信息", notes = "传入articlesBeingProcessedParmVO对象")
    public R<List<ArticlesCmNameOdNoVO>> getArticlesCmNameOdNoVOList(ArticlesBeingProcessedParmVO articlesBeingProcessedParmVO) {
        List<ArticlesCmNameOdNoVO> articlesCmNameOdNoVOList =
                workbatchOrdlinkService.getArticlesCmNameOdNoVOList(articlesBeingProcessedParmVO);
        return R.data(articlesCmNameOdNoVOList);
    }

    @GetMapping("/getArticlesWbProcessVOList")
    @ApiOperation(value = "查询在制品的批次和工序信息", notes = "传入articlesBeingProcessedParmVO对象")
    public R<List<ArticlesWbProcessVO>> getArticlesWbProcessVOList(ArticlesBeingProcessedParmVO articlesBeingProcessedParmVO, @RequestParam("odNo") String odNo) {
        List<ArticlesWbProcessVO> articlesWbProcessVOList =
                workbatchOrdlinkService.getArticlesWbProcessVOList(articlesBeingProcessedParmVO, odNo);
        return R.data(articlesWbProcessVOList);
    }

    @GetMapping("/getArticlesShiftList")
    @ApiOperation(value = "查询在制品班次排产信息", notes = "传入sdId")
    public R<List<ArticlesShiftVO>> getArticlesShiftList(@RequestParam("sdId") Integer sdId) {
        List<ArticlesShiftVO> articlesShiftVOList = workbatchOrdlinkService.getArticlesShiftList(sdId);
        return R.data(articlesShiftVOList);
    }

    @GetMapping("/getArticlesTraycardList")
    @ApiOperation(value = "查询在制品托盘信息", notes = "传入wfId")
    public R<List<ExecuteTraycard>> getArticlesTraycardList(@RequestParam("wfId") Integer wfId) {
        List<ExecuteTraycard> executeTraycardList =
                executeTraycardService.list(new QueryWrapper<ExecuteTraycard>().eq("wf_id", wfId));
        return R.data(executeTraycardList);
    }

    @GetMapping("/getWfIdList")
    @ApiOperation(value = "获取当前日期的所有wfId", notes = "targetDay")
    public R getWfIdList(@RequestParam("targetDay") String targetDay, @RequestParam("wsId") Integer wsId) {
        List<Integer> wfIdList = workbatchOrdlinkService.getWfIdList(targetDay, wsId);
        return R.data(wfIdList);
    }

    @ApiOperation("批量删除")
    @PostMapping("delBatch")
    public R delBatch(@RequestParam("ids") String ids) {
        List<Integer> list = Func.toIntList(ids);
        workbatchOrdlinkService.removeByIds(list);
        return R.success("删除成功");
    }

    @Autowired
    WorkbatchTask workbatchTask;

    @ApiOperation("执行定时方法的调用测试类")
    @GetMapping("/workconfigtask")
    public R workconfigtask() {
        workbatchTask.workConfigTask();
        return R.success("已经执行完毕。");
    }


}
