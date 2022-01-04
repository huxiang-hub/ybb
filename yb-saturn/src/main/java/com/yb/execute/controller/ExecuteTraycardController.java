package com.yb.execute.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.service.ExecuteTraycardService;
import com.yb.execute.vo.*;
import com.yb.panelapi.user.entity.BaseFactory;
import com.yb.panelapi.user.mapper.BaseFactoryMapper;
import com.yb.stroe.entity.StoreArea;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.service.IStoreInlogService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.service.StoreAreaService;
import com.yb.stroe.vo.SeatInventoryVO;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.service.ISuperviseExecuteService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ExecuteTraycard")
@Api(tags = "标识卡")
public class ExecuteTraycardController {

    @Autowired
    private ExecuteTraycardService executeTraycardService;
    @Autowired
    private IWorkbatchOrdlinkService workbatchOrdlinkService;
    @Autowired
    private IStoreInventoryService storeInventoryService;
    @Autowired
    private StoreAreaService storeAreaService;
    @Autowired
    private ISuperviseExecuteService superviseExecuteService;
    @Autowired
    private IStoreInlogService storeInlogService;
    @Autowired
    private BaseFactoryMapper baseFactoryMapper;

    /*单个托盘位置长度*/
    private final Integer SIZE = 1300;
    private final int DIVISOR = 999999;//托盘编号的最大值


    @PostMapping("saveExecuteTraycard")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "生成标识卡", notes = "传入traycardVO")
    public R saveExecuteTraycard(@RequestBody ExecuteTraycardVO traycardVO) {
        synchronized(this){
            Integer wfId = traycardVO.getWfId();
            Integer sdId = traycardVO.getSdId();
            Integer mpId = traycardVO.getMpId();//库位id
            Integer trayNum = traycardVO.getTrayNum();
            Integer maId = traycardVO.getMaId();//设备id
            if(maId == null){
                return R.fail("传入的设备id不能为空");
            }
//        Integer exId = traycardVO.getExId();
//        if(exId == null){
//            return R.fail("传入的执行id不能为空");
//        }
            List<Integer> etIdList = traycardVO.getEtIdList();//不删除的托盘id
            SuperviseExecute superviseExecute =
                    superviseExecuteService.getOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
            Date startTime = null;
            if(superviseExecute != null){
                startTime = superviseExecute.getStartTime();
            }
            /*查询最大id*/
            Integer maxId = executeTraycardService.getMaxId();
            /*查询排产单信息*/
            WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkService.getBaseMapper().selectById(sdId);
            /*托盘占用库位个数*/
            Integer layNum = getLayNum(workbatchOrdlink);
            String operateSize = workbatchOrdlink.getOperateSize();//上机尺寸
            int totalNum = traycardVO.getTotalNum() == null ? 0 : traycardVO.getTotalNum();
            Integer planNum = traycardVO.getPlanNum() == null ? 0 : traycardVO.getPlanNum();
            Integer redNum = 1;
            /*删除托盘标识卡*/
            ExecuteTraycardRoveVO executeTraycardRoveVO = deleteExecuteTraycard(traycardVO);
            if(executeTraycardRoveVO != null){
                planNum = executeTraycardRoveVO.getPlanNum();//剩余计划数量
                redNum = executeTraycardRoveVO.getRedNum();//已变红托盘数+1
                totalNum = executeTraycardRoveVO.getTotalNum();//总台数
            }
            if(etIdList != null && !etIdList.isEmpty()){
                /*修改总台数数量*/
                executeTraycardService.updateTraycardTotalNumList(etIdList, totalNum);
            }
            boolean save = false;
            Date date = new Date();
            ExecuteTraycard traycard = new ExecuteTraycard();
            traycard.setTrayNum(trayNum);//本台计数
            traycard.setTyStatus(0);
            traycard.setExStatus(0);
//        traycard.setMaId(maId);
            traycard.setExId(traycardVO.getExId());
            traycard.setPlanNum(traycardVO.getPlanNum());//计划总数
            traycard.setUsId(traycardVO.getUsId());
            traycard.setRemark(traycardVO.getRemark());//备注
            traycard.setLayNum(layNum);//托板占用数量
            traycard.setTotalNum(totalNum);//总台数
            traycard.setStartTime(startTime);
            traycard.setSdId(sdId);
            traycard.setWfId(wfId);
            List<SeatInventoryVO> seatInventoryVOS = storeInventoryService.seatInventoryInfoBySort(mpId);
            if(redNum > totalNum){//变红的数量大于台数,则剩余的数量只需要一个托盘
                if(!seatInventoryVOS.isEmpty()){
                    maxId++;
                    SeatInventoryVO seatInventoryVO = seatInventoryVOS.get(0);
                    traycard.setTrayNum(planNum);//本台计数
                    traycard.setUpdateAt(date);
                    traycard.setCreateAt(date);
                    traycard.setTrayNo(redNum);
                    String tdNo = getTdNo(maxId);
                    traycard.setTdNo(tdNo);
                    traycard.setMpId(seatInventoryVO.getSeatId());//库位id
                    traycard.setStorePlace(seatInventoryVO.getStNo());//库位名称
                    save = executeTraycardService.save(traycard);
                    /*锁定库位*/
                    lockStore(traycard, operateSize);
                }else {
                    return R.fail("库位不足");
                }
            }
            Integer usableNum;
            Integer stNum;
            int j = 0;
            int etNum = 1;
            for(int i = redNum; i <= totalNum; i++){
                /*查询可用托盘*/
                if(!seatInventoryVOS.isEmpty()){
                    if(seatInventoryVOS.size() > j){
                        SeatInventoryVO seatInventoryVO = seatInventoryVOS.get(j);
                        Integer seatId = seatInventoryVO.getSeatId();
                        String seatNo = seatInventoryVO.getStNo();
                        usableNum = seatInventoryVO.getUsableNum();//第一个库位剩余托盘数
                        if(i == redNum){//防止第一个库位剩余位置不够
                            stNum  = etNum * layNum;//托盘所需位置数
                            if(stNum > usableNum){
                                j++;
                                if(seatInventoryVOS.size() > j){
                                    seatInventoryVO = seatInventoryVOS.get(j);
                                    seatId = seatInventoryVO.getSeatId();
                                    seatNo = seatInventoryVO.getStNo();
                                    usableNum = seatInventoryVO.getUsableNum();//库位剩余托盘数
                                }else {
                                    return R.fail("库位不足");
                                }
                            }
                        }
                        traycard.setMpId(seatId);//库位id
                        traycard.setStorePlace(seatNo);//库位名称
                        etNum++;
                        stNum  = etNum * layNum;//托盘所需位置数
                        if(stNum > usableNum){//如果所需托盘位大于剩余的,则换下个库位
                            j++;
                            etNum = 1;
                        }
                    }else {
                        return R.fail("库位不足");
                    }
                }
                if(i == totalNum){//如果是最后一个托盘
                    Integer trayNumber = planNum - (i - redNum) * trayNum;
                    traycard.setTrayNum(trayNumber);
                }
                traycard.setUpdateAt(date);
                traycard.setCreateAt(date);
                traycard.setTrayNo(i);
                maxId++;
                String tdNo = getTdNo(maxId);
                traycard.setTdNo(tdNo);
                save = executeTraycardService.save(traycard);
                /*锁定库位*/
                boolean lockStore = lockStore(traycard, operateSize);
                if(!lockStore){//如果库位锁定失败,抛异常
                    throw new RuntimeException();
                }
            }
            return R.status(save);
        }
    }

    /**
     * 生成编号(NXHR-XXXXXX)
     * @param maxId 编号数
     * @return
     */
    public String getTdNo(Integer maxId){
        BaseFactory baseFactory = baseFactoryMapper.selectOne(new QueryWrapper<>());
        String tenantId = baseFactory.getTenantId();
        String tdNo;
        Integer no = maxId % DIVISOR;
        if(no == 0){
            no = DIVISOR;
        }
        if(no < 10){
            tdNo = "00000" + no;
        }else if(10 <= no && no < 100){
            tdNo = "0000" + no;
        }else if(100 <= no && no < 1000){
            tdNo = "000" + no;
        }else if(1000 <= no && no < 10000){
            tdNo = "00" + no;
        }else if (10000 <= no && no < 100000){
            tdNo = "0" + no;
        }else {
            tdNo = no.toString();
        }
        return tenantId.toUpperCase() + "-" + tdNo;
    }

    /**
     * 托盘占用库位个数
     * @return
     */
    private Integer getLayNum(WorkbatchOrdlink workbatchOrdlink){
        Integer layNum = 1;//托盘占位数
        String operateSize = workbatchOrdlink.getOperateSize();//上机尺寸
        if(operateSize != null){
            String[] s = operateSize.split("\\*");
            Integer size = Integer.valueOf(s[0]) > Integer.valueOf(s[1]) ? Integer.valueOf(s[0]) : Integer.valueOf(s[1]);
            layNum = SIZE > size ? 1 : 2;
        }
        return layNum;
    }

    /**
     * 删除托盘标识卡
     * @param traycardVO
     * @return
     */
    private ExecuteTraycardRoveVO deleteExecuteTraycard(ExecuteTraycardVO traycardVO){
        Integer wfId = traycardVO.getWfId();
        Integer trayNum = traycardVO.getTrayNum();
        ExecuteTraycardRoveVO executeTraycardRoveVO = null;
        Integer redNum;//变红的台数的下一台序号
        int totalNum;
        Integer planNum = traycardVO.getPlanNum() == null ? 0 : traycardVO.getPlanNum();
        Integer etStatus = traycardVO.getEtStatus();//是否覆盖 0 不覆盖, 1 覆盖
        Integer contNum;//已变红数量
        if(etStatus == 0){//0 不覆盖
            executeTraycardRoveVO = new ExecuteTraycardRoveVO();
            List<Integer> etIdList = traycardVO.getEtIdList();
            /*查询被删除的托盘ID集合*/
            List<Integer> noEtIdList = executeTraycardService.selectNeEtIdList(etIdList, wfId);
            /*删除台账信息*/
            if(noEtIdList != null && !noEtIdList.isEmpty()){
                storeInventoryService.deleteBatchByetIds(noEtIdList);
                storeInlogService.deleteNoEtIdList(noEtIdList);
            }
            contNum = traycardVO.getContNum() == null ? 0 : traycardVO.getContNum();
            /*变红的不删除*/
            executeTraycardService.deleteListById(wfId, etIdList);
            redNum = traycardVO.getRedNum() + 1;//已变红的台数
            planNum = planNum - contNum;//变红剩余的
            int ceil = (int)Math.ceil(planNum / (double)trayNum);//剩余数量所需台数
            totalNum = traycardVO.getRedNum() + ceil;//总台数
            executeTraycardRoveVO.setTotalNum(totalNum);
            executeTraycardRoveVO.setPlanNum(planNum);
            executeTraycardRoveVO.setRedNum(redNum);
        }else if (etStatus == 1){//1 覆盖
            /*查询被删的托盘id集合*/
            List<ExecuteTraycard> traycardList =
                    executeTraycardService.list(new QueryWrapper<ExecuteTraycard>().eq("wf_id", wfId));
            List<Integer> etIdList = new ArrayList<>();
            for(ExecuteTraycard traycard : traycardList){
                etIdList.add(traycard.getId());
            }
            /*删除台账信息*/
            storeInventoryService.deleteBatchByetIds(etIdList);
            storeInlogService.deleteNoEtIdList(etIdList);
            /*先删除后新增*/
            executeTraycardService.remove(new QueryWrapper<ExecuteTraycard>().eq("wf_id", wfId));
        }
        return executeTraycardRoveVO;
    }


    /**
     * 锁定库位
     * @param traycard
     * @param stSize
     * @return
     */
    private boolean lockStore(ExecuteTraycard traycard, String stSize){
        StoreInventory storeInventory = new StoreInventory();
        Integer mpId = traycard.getMpId();//库位id
        /*根据库位id查询所在库区信息*/
        StoreArea storeArea = storeAreaService.selectStoreAreaByStId(mpId);
        if(storeArea != null){
            storeInventory.setAreaId(storeArea.getId());//库区id
        }
        storeInventory.setEtPdnum(traycard.getTrayNum());//托板产品数量
        storeInventory.setStId(mpId);//库位id
        storeInventory.setEtId(traycard.getId());//托盘标识卡ID
        storeInventory.setLayNum(traycard.getLayNum());//托盘占用位置数量
        storeInventory.setStatus(2);
        storeInventory.setStNo(traycard.getStorePlace());//库位编号
        storeInventory.setStSize(stSize);//入库尺寸
        storeInventory.setStType(1);//存储类型（数据字典1、半成品2、成品 3、原料4、辅料5、备品备件）
        return storeInventoryService.save(storeInventory);
    }
    @PostMapping("updateExecuteTraycard")
    @ApiOperation(value = "修改", notes = "传入executeTraycard")
    public R updateExecuteTraycard(@RequestBody ExecuteTraycard executeTraycard) {
        return R.status(executeTraycardService.updateById(executeTraycard));
    }

    @GetMapping("updateTraycardTrayNum")
    @ApiOperation(value = "修改托盘本台计数", notes = "托盘id, 数量trayNum")
    @Transactional(rollbackFor = Exception.class)
    public R updateTraycardTrayNum(@RequestParam("id") Integer id,
                                   @RequestParam("trayNum") Integer trayNum,
                                   @RequestParam("maId") Integer maId) {
        if(id == null){
            return R.fail("传入id不能为空");
        }
        if(maId == null){
            return R.fail("传入设备id不能为空");
        }
        ExecuteTraycard traycard = executeTraycardService.getById(id);
        if(traycard == null){
            return R.fail("传入的托盘id不正确");
        }
        if (traycard.getMaId() != null && !maId.equals(traycard.getMaId())) {
            return R.fail("当前设备与打印标识卡设备不一致,禁止修改操作");
        }
        ExecuteTraycard executeTraycard = new ExecuteTraycard();
        executeTraycard.setId(id);
        executeTraycard.setTrayNum(trayNum);
        boolean traycardBoolean = executeTraycardService.updateById(executeTraycard);

        boolean storeBoolean;
        boolean outStoreBoolean = true;
        Integer printNum = traycard.getPrintNum();
        if(printNum == null || printNum == 0){//打印前
            StoreInventory storeInventory = new StoreInventory();
            storeInventory.setEtPdnum(trayNum);
            storeBoolean = storeInventoryService.update(storeInventory, new QueryWrapper<StoreInventory>().eq("et_id", id));
        }else {//打印后

            StoreInventory storeInventory = new StoreInventory();
            storeInventory.setEtPdnum(trayNum);
            storeBoolean = storeInventoryService.update(storeInventory, new QueryWrapper<StoreInventory>().eq("et_id", id));
            StoreInlog storeInlog = new StoreInlog();
            storeInlog.setEtPdnum(trayNum);
            outStoreBoolean = storeInlogService.update(storeInlog, new QueryWrapper<StoreInlog>().eq("et_id", id));
        }
        if(trayNum == null){
            return R.fail("传入trayNum不能为空");
        }
        return R.status(traycardBoolean || outStoreBoolean || storeBoolean);
    }

    @GetMapping("updateTraycardStorePlace")
    @ApiOperation(value = "修改托盘库位", notes = "托盘id, 库位id: mpId, 库位名称: storePlace")
    @Transactional(rollbackFor = Exception.class)
    public R updateTraycardStorePlace(@RequestParam("id") Integer id,
                                      @RequestParam("mpId") Integer mpId,
                                      @RequestParam("storePlace") String storePlace,
                                      @RequestParam("maId") Integer maId) {
        synchronized (this){
            if(id == null){
                return R.fail("传入id不能为空");
            }
            if(maId == null){
                return R.fail("传入设备id不能为空");
            }
            if(mpId == null){
                return R.fail("传入mpId不能为空");
            }
            ExecuteTraycard traycard = executeTraycardService.getById(id);
            if(traycard == null){
                return R.fail("传入的托盘id不正确");
            }
            if (traycard.getMaId() != null && !maId.equals(traycard.getMaId())) {
                return R.fail("当前设备与打印标识卡设备不一致,禁止修改操作");
            }
            Integer printNum = traycard.getPrintNum();
            /*先查询该库位是否有剩余*/
            Integer usableNum = storeInventoryService.getUsableNum(mpId);
            if(usableNum <= 0){
                return R.fail("当前库位已满,请选择其他库位");
            }
            StoreInventory storeInventory = new StoreInventory();
            storeInventory.setStNo(storePlace);
            storeInventory.setStId(mpId);
            List<Integer> etIdList = new ArrayList<>();
            etIdList.add(id);
            if(printNum == null || printNum == 0){//打印前
                storeInventoryService.update(storeInventory, new QueryWrapper<StoreInventory>().eq("et_id", id));
            }else {//打印后
                /*删除日志*/
                storeInlogService.deleteNoEtIdList(etIdList);
                /*再入库*/
                storeInventoryService.deleteBatchByetIds(etIdList);
                storeInventoryService.putStore(mpId, id, 1, null);
            }

            if(StringUtil.isEmpty(storePlace)){
                return R.fail("传入storePlace不能为空");
            }
            ExecuteTraycard executeTraycard = new ExecuteTraycard();
            executeTraycard.setId(id);
            executeTraycard.setMpId(mpId);
            executeTraycard.setStorePlace(storePlace);
            return R.status(executeTraycardService.updateById(executeTraycard));
        }
    }

    /**
     * 批量修改
     * @param traycardDeleteListVO
     * @return
     */
    @PostMapping("updateTraycardList")
    @ApiOperation(value = "批量修改", notes = "传入traycardDeleteListVO")
    public R updateTraycardList(@RequestBody TraycardDeleteListVO traycardDeleteListVO) {
        return R.status(executeTraycardService.updateTraycardList(traycardDeleteListVO));
    }

    @GetMapping("getExecuteTraycardByWfId")
    @ApiOperation(value = "查询详情", notes = "传入wfId")
    public R<List<ExecuteTraycardVO>> getExecuteTraycardByWfId(@RequestParam(value = "wfId") Integer wfId) {
        if(wfId == null){
            return R.fail("wfId不能为null");
        }
        return R.data(executeTraycardService.getExecuteTraycardByWfId(wfId));
    }

    @GetMapping("getUpPrcessTraycardList")
    @ApiOperation(value = "根据当前排产单查询上工序托盘信息", notes = "传入wfId")
    public R<List<UpPrcessTraycardVO>> getUpPrcessTraycardList(@ApiParam(value = "主键集合", required = true) @RequestParam String wfIds){
        return R.data(executeTraycardService.getUpPrcessTraycardList(Func.toIntList(wfIds)));
    }

    /**
     * 导出标识卡pdf
     * @param etIds
     * @return
     */
    @GetMapping("/construction")
    @ApiOperation(value = "导出标识卡pdf", notes = "传入etIds")
    public R construction(String etIds){
        if(StringUtil.isEmpty(etIds)){
            return R.fail("etId不能为空");
        }
        executeTraycardService.construction(Func.toIntList(etIds));
        return null;
    }
    @GetMapping("/getTraycardData")
    @ApiOperation(value = "打印标识卡数据", notes = "传入etIds,批量时etId以逗号隔开")
    public R<List<TraycardTextVO>> getTraycardData(@RequestParam("etIds")String etIds,
                                                   @RequestParam("maId")Integer maId){

        if(StringUtil.isEmpty(etIds)){
            return R.fail("etIds不能为空");
        }
        if(maId == null){
            return R.fail("设备id不能为空");
        }
        List<TraycardTextVO> traycardData = executeTraycardService.getTraycardData(Func.toIntList(etIds), maId);
        if(traycardData.isEmpty()){
            return R.fail("标识卡绑定的设备与当前不符");
        }
        return R.data(traycardData);
    }
    @GetMapping("/updatePrintNumList")
    @ApiOperation(value = "修改打印次数", notes = "传入etIds")
    @Transactional(rollbackFor = Exception.class)
    public R updatePrintNumList(@RequestParam("etIds") String etIds,
                                @RequestParam("maId")Integer maId,
                                @RequestParam("exId")Integer exId){
        if(StringUtil.isEmpty(etIds)){
            return R.fail("etIds不能为空");
        }
        if(maId == null){
            return R.fail("设备id不能为空");
        }
        if(exId == null){
            return R.fail("执行id不能为空");
        }
        List<Integer> toIntList = Func.toIntList(etIds);
        try {
            /*修改打印次数*/
            executeTraycardService.updatePrintNumList(toIntList, maId, exId);
            /*修改库位占用状态*/
            storeInventoryService.updateStatus(toIntList, 1);
            /*工单实时执行表*/
            SuperviseExecute superviseExecute =
                    superviseExecuteService.getOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
            Integer operator = superviseExecute.getOperator();//机台操作人员
            Integer sdId = superviseExecute.getSdId();//批次ID
            /*查询排产单信息*/
            WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkService.getBaseMapper().selectById(sdId);
            String operateSize = workbatchOrdlink.getOperateSize();//上机尺寸
            /*台账写入日志*/
            List<StoreInlog> storeInlogList = new ArrayList<>();
            StoreInlog storeInlog;
            for (Integer etId : toIntList) {
                storeInlog = new StoreInlog();
                ExecuteTraycard executeTraycard = executeTraycardService.getById(etId);
                Integer printNum = executeTraycard.getPrintNum();
                if(1 == printNum){
                    storeInlog.setEtPdnum(executeTraycard.getTrayNum());
                    storeInlog.setEtId(etId);
                    storeInlog.setLayNum(executeTraycard.getLayNum());
                    storeInlog.setStId(executeTraycard.getMpId());
                    storeInlog.setStNo(executeTraycard.getStorePlace());
                    storeInlog.setStSize(operateSize);
                    storeInlog.setUsId(operator);
                    storeInlog.setStType(1);
//                storeInlog.setMlId();
                    storeInlog.setOperateType(1);
                    storeInlog.setCreateAt(LocalDateTime.now());
                    storeInlogList.add(storeInlog);
                }
            }
            storeInlogService.saveBatch(storeInlogList);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("修改失败");
        }
        return R.success("修改成功");
    }

    @PostMapping("/saveTraycardOne")
    @ApiOperation(value = "新增一个托盘数据")
    @Transactional(rollbackFor = Exception.class)
    public R saveTraycardOne(@RequestBody ExecuteTraycard executeTraycard){
        synchronized (this){
            Integer mpId = executeTraycard.getMpId();
            /*可使用的库位*/
            List<SeatInventoryVO> seatInventoryVOS = storeInventoryService.seatInventoryInfoBySort(mpId);
            if(seatInventoryVOS.isEmpty()){
                return R.fail("库位不足");
            }
            SeatInventoryVO seatInventoryVO = seatInventoryVOS.get(0);
            Integer seatId = seatInventoryVO.getSeatId();
            String stNo = seatInventoryVO.getStNo();
            Integer wfId = executeTraycard.getWfId();
            List<ExecuteTraycardVO> executeTraycardVOS = executeTraycardService.getExecuteTraycardByWfId(wfId);
            List<ExecuteTraycard> executeTraycardList = new ArrayList<>();
            ExecuteTraycard traycard;
            Integer totalNum = 1;
            for(ExecuteTraycardVO executeTraycardVO : executeTraycardVOS){
                totalNum = executeTraycardVO.getTotalNum() + 1;
                traycard = new ExecuteTraycard();
                traycard.setId(executeTraycardVO.getId());
                traycard.setTotalNum(totalNum);
                executeTraycardList.add(traycard);
            }
            Integer sdId = executeTraycard.getSdId();
            /*查询最大id*/
            Integer maxId = executeTraycardService.getMaxId();
            /*查询排产单信息*/
            WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkService.getBaseMapper().selectById(sdId);
            String operateSize = workbatchOrdlink.getOperateSize();
            Integer layNum = getLayNum(workbatchOrdlink);
            Integer maId = executeTraycard.getMaId();
            SuperviseExecute superviseExecute =
                    superviseExecuteService.getOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
            executeTraycard.setStartTime(superviseExecute.getStartTime());
            executeTraycard.setMaId(null);
            executeTraycard.setExId(null);
            /*获取tdNo*/
            String tdNo = getTdNo(maxId);
            executeTraycard.setTdNo(tdNo);
            executeTraycard.setLayNum(layNum);
            executeTraycard.setTotalNum(totalNum);
            executeTraycard.setTrayNo(totalNum);
            executeTraycard.setMpId(seatId);
            executeTraycard.setStorePlace(stNo);
            executeTraycard.setTyStatus(0);
            executeTraycard.setExStatus(0);
            executeTraycard.setCreateAt(new Date());
            executeTraycard.setUpdateAt(new Date());
            executeTraycardService.save(executeTraycard);
            executeTraycardService.updateBatchById(executeTraycardList);
            boolean lockStore = lockStore(executeTraycard, operateSize);
            return R.status(lockStore);
        }
    }


    @GetMapping("getTraycardByTrayParam")
    @ApiOperation(value = "托盘的具体详情数据", notes = "传入托盘对应ids集合数据")
    public R<IPage<ExecuteTraycardVO>> getTraycardByTrayParam(Query query,ExecuteTraycardStoreVO trayParam) {
        if(trayParam.getTyIds() == null){
            if(trayParam.getSdId() !=null) {
                IPage<ExecuteTraycardVO> pages = executeTraycardService.getTraycardByStoreinfo(Condition.getPage(query), trayParam);
                return R.data(pages);
            }
        }else {
            IPage<ExecuteTraycardVO> pages = executeTraycardService.getTraycardByTrayParam(Condition.getPage(query), trayParam);
            return R.data(pages);
        }
        return R.fail("未传入有效参数；托盘id：tyId或者工单id：sdId");
    }

    @GetMapping("getByBfId")
    @ApiOperation("手机盘点-根据上报id查询托盘")
    public R<List<PhoneTrayCardVO>> getTrayByBfId(Integer bfId) {
        List<PhoneTrayCardVO> list = executeTraycardService.getTrayByBfId(bfId);
        return R.data(list);
    }

    @GetMapping("audits")
    @ApiOperation("手机盘点-手机确定审核托盘")
    public R audits(
            @ApiParam(value = "托盘id") @RequestParam(required = false) Integer etId,
            @ApiParam(value = "更正数量") @RequestParam(required = false) Integer number,
            @ApiParam(value = "库位id") @RequestParam(required = false) Integer seatId,
            @ApiParam(value = "上报表id") @RequestParam Integer bfId,
            @ApiParam(value = "审核人id") @RequestParam(required = false) Integer exUserid) {
        String msg = executeTraycardService.audits(etId, number, seatId, bfId, exUserid);
        return R.success(msg);
    }

    @GetMapping("/getFlowCard")
    @ApiOperation(value = "扫码获取托盘流程单信息")
    public R<FlowCardVO> getFlowCard(@ApiParam(value = "托盘id")@RequestParam(value = "tdNo") String tdNo){

        return R.data(executeTraycardService.getFlowCard(tdNo));
    }

    @GetMapping("getPhoneInfoBySeatId")
    @ApiOperation("手机盘点-根据库位id获取库位存放的托盘列表信息")
    public R<PhoneSeatTrayInfoVO> getPhoneInfoBySeatId(@ApiParam(value = "库位id") @RequestParam Integer seatId) {
        PhoneSeatTrayInfoVO phoneSeatTrayInfoVO = executeTraycardService.getPhoneInfoBySeatId(seatId);
        return R.data(phoneSeatTrayInfoVO);
    }


}
