package com.yb.workbatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.common.constant.LocalEnum;
import com.yb.common.constant.OrderStatusConstant;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.mapper.ExecuteBrieferMapper;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.mater.entity.MaterBatchlink;
import com.yb.mater.mapper.MaterBatchlinkMapper;
import com.yb.mater.mapper.MaterMtinfoMapper;
import com.yb.mater.vo.MaterMtinfoVO;
import com.yb.prod.mapper.ProdProcelinkMapper;
import com.yb.rule.service.RuleProdoeeService;
import com.yb.statis.dto.WorkBatchSortUpdateDTO;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.statis.service.StatisOrdreachService;
import com.yb.statis.service.impl.StatisOrdreachServiceImpl;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.*;
import com.yb.workbatch.mapper.*;
import com.yb.workbatch.request.*;
import com.yb.workbatch.service.IWorkbatchOrdlinkNewService;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springblade.common.exception.CommonException;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 新排产
 * @Author my
 * @Date Created in 2020/7/25 10:55
 */
@Service
@Slf4j
public class WorkbatchOrdlinkNewServiceImpl extends ServiceImpl<WorkbatchOrdlinkMapper, WorkbatchOrdlink> implements IWorkbatchOrdlinkNewService {

    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private WorkbatchOrdoeeMapper workbatchOrdoeeMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private MaterBatchlinkMapper materBatchlinkMapper;
    @Autowired
    private StatisOrdreachMapper statisOrdreachMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private MaterMtinfoMapper materMtinfoMapper;
    @Autowired
    private RuleProdoeeService ruleProdoeeService;
    @Autowired
    private StatisOrdreachService statisOrdreachService;
    @Autowired
    private IWorkbatchOrdlinkService workbatchOrdlinkService;
    @Autowired
    private WorkbatchProgressMapper workbatchProgressMapper;
    @Autowired
    private ProdProcelinkMapper prodProcelinkMapper;
    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;
    @Autowired
    private ExecuteBrieferMapper executeBrieferMapper;
    @Autowired
    private IExecuteBrieferService executeBrieferService;
    @Autowired
    private WorkbatchShiftService workbatchShiftService;

    static int rate = 60;

    /*****
     *
     * @param request
     * @return
     * @throws ParseException
     */


    /****
     * 修改状态的接口方法  下发/挂起/废弃/驳回/排产
     * @param request
     * @return
     * @throws ParseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateStatus(WorkbatchOrdlinkStatusUpdateRequest request) throws ParseException {
        StringBuilder str = new StringBuilder();
        //表示历史条件不能进行状态的更改   先暂时屏蔽，要不就修改为1天前排产时间的限制。
//        if (isHistoryData(request)) {
//            str.append("往期数据无法进行状态修改");
//            return str.toString();
//        }
        //判断在做什么操作 【排产】操作
        if (request.getStatus() == LocalEnum.WorkbatchOrdlinkStatus.SCHEDULING) {
            str.append(updateStatusBySchedul(request));
            //直接生成对应的小时达成数，计划产能计划
            statisOrdreachService.setOrdreach(request.getSdDate(), request.getWsId(), request.getMaId(), DBIdentifier.getProjectCode());//更新状态后更新小时达成率
        }
        //判断在做什么操作  【发布】操作下发
        if (request.getStatus() == LocalEnum.WorkbatchOrdlinkStatus.ISSUED) {
            str.append(updateStatusByIssued(request));
        }
        //判断在做什么操作 【驳回】操作
        if (request.getStatus() == LocalEnum.WorkbatchOrdlinkStatus.DOWN) {
            str.append(updateStatusByDown(request));
            //直接生成对应的小时达成数，计划产能计划  需要有wsId
            statisOrdreachService.setOrdreach(request.getSdDate(), request.getWsId(), request.getMaId(), DBIdentifier.getProjectCode());//更新状态后更新小时达成率
        }

        //判断在做什么操作 【废弃】操作
        if (request.getStatus() == LocalEnum.WorkbatchOrdlinkStatus.DISCARD) {
            str.append(updateStatusByDiscard(request));
        }

        //判断在做什么操作 【挂起】操作 --暂停
        if (request.getStatus() == LocalEnum.WorkbatchOrdlinkStatus.HANG) {
            str.append(updateStatusByHang(request));
        }
        //更新待排数据和状态=-------start-----wyn修改
        //Map<Integer, Integer> sdIdNumMap = new HashMap<>();//只有排产才会用到该数据
        List<Integer> sdIds = new ArrayList<>();
        if (request.getStatus() == LocalEnum.WorkbatchOrdlinkStatus.SCHEDULING) {
            List<SdIdNumberVo> sdIdNumberVos = request.getSdIdNumberVos();
            if (null != sdIdNumberVos && sdIdNumberVos.size() > 0) {
                //sdIdNumMap = sdIdNumberVos.stream().collect(Collectors.toMap(SdIdNumberVo::getSdId, SdIdNumberVo::getNumber, (k1, k2) -> k1));
                sdIds = sdIdNumberVos.stream().map(SdIdNumberVo::getSdId).collect(Collectors.toList());
            }
            for (Integer sdId : sdIds) {
                setWorksNum(sdId, request.getMaId(), request.getStatus().getType());
            }
        }
        //为排产的时候request.getSdIds() 为空------------------------------不为排产状态的数据信息
        if (request.getSdIds() != null && request.getSdIds().size() > 0 && request.getStatus() != LocalEnum.WorkbatchOrdlinkStatus.SCHEDULING) {
            for (Integer sdId : request.getSdIds()) {
                setWorksNum(sdId, request.getMaId(), request.getStatus().getType());
            }
        }
        //更新待排数据和状态=-------end

        return str.toString();
    }

    /****
     * 不能修改状态
     * @param ordshift
     * @return
     */
    private Boolean notStatus(WorkbatchOrdlinkShiftVO ordshift) {
        //生产状态中不能驳回
        if (ordshift != null && ordshift.getRunStatus() == 1) {
            //str.append(o.getWbNo() + "处于生产状态，无法修改。");
            return true;
        }
        return false;
    }

    /*****
     *【排产】操作 --排产操作：从待产区移动到已排产区域
     * [最新排产] 待排产区指定排产数量版本
     * [最新排产] 之前排产入参为sdIds(工单id)，现在改为sdIdNumberVos(工单id、对应排产数量)
     * @param request
     * @return
     */
    private String updateStatusBySchedul(WorkbatchOrdlinkStatusUpdateRequest request) {

        //限制同班次同批次多订单===========================================TODO WYN
        request.getSdIdNumberVos().forEach(o -> {
            WorkbatchShiftVO repeat = workbatchShiftMapper.getRepeat(o.getSdId(), request.getMaId(), request.getWsId(), request.getSdDate());
            if (repeat != null) {
                log.error("工单编号:" + repeat.getWbNo() + "当天当班次已存在该工单，请取消勾选后重试");
                throw new CommonException(20003, "工单编号:" + repeat.getWbNo() + "当天当班次已存在该工单，请取消勾选后重试");
            }
        });
        StringBuilder str = new StringBuilder();
        Map<Integer, Integer> sdIdNumMap = new HashMap<>();
        List<SdIdNumberVo> sdIdNumberVos = request.getSdIdNumberVos();
        if (null != sdIdNumberVos && sdIdNumberVos.size() > 0) {
            sdIdNumMap = sdIdNumberVos.stream().collect(Collectors.toMap(SdIdNumberVo::getSdId, SdIdNumberVo::getNumber, (k1, k2) -> k1));
        }
        List<Integer> sdIds = sdIdNumberVos.stream().map(SdIdNumberVo::getSdId).collect(Collectors.toList());
        List<WorkbatchOrdlinkShiftVO> workbatchOrdlinkList = workbatchOrdlinkMapper.selectBatchIdsAndWfIds(sdIds, request.getWfIds());
        if (!workbatchOrdlinkList.isEmpty()) {
            //查询发布状态内容信息
            for (WorkbatchOrdlinkShiftVO o : workbatchOrdlinkList) {
                //正在生产的状态不能够改变
//                if ("2".equalsIgnoreCase(o.getStatus()) || "3".equalsIgnoreCase(o.getStatus()) || "4".equalsIgnoreCase(o.getStatus())) {
//                    str.append(o.getWbNo() + "状态为生产状态，暂时无法改变其状态。");
//                    continue;
//                }
                //shift
                //状态：-1未下发，0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）
                //状态（0起草1发布2正在生产3已完成4已挂起5废弃） -1待排产ERP接入 6驳回7已排产8部分完成9未排完
                // 前端传过来的排产数
                Integer plan = sdIdNumMap.get(o.getId());
//                o.setStatus(request.getStatus().getType());
                o.setSdDate(request.getSdDate());
                //为下发是更新排产班次状态信息
                WorkbatchShift wbshift = workbatchShiftMapper.getWsid(request.getSdDate(), o.getSdId(), request.getWsId(), request.getMaId());
                if (wbshift == null) {
                    wbshift = new WorkbatchShift();
                    wbshift.setSdId(o.getId());
                    wbshift.setId(o.getId());
                    wbshift.setWsId(request.getWsId());
                    wbshift.setSdDate(request.getSdDate());
//                     wbshift.setPlanNum(o.getPlanNum());
                    // [最新排产]计划数,之前为排产单总数，现在改为前端传过来的排产数
                    wbshift.setPlanNum(plan);
                    wbshift.setMaId(request.getMaId());
                    wbshift.setPlanType("1");//类型为1表示生产2表示保养3表示计划停机
                    wbshift.setCreateAt(new Date());
                    workbatchShiftMapper.insert(wbshift);
                }
                String status = "0";//修改已排产状态 发布状态
                wbshift.setShiftStatus(-1);//已排产 待下发
                wbshift.setStatus(status);//下发后状态为1已下发的状态
                wbshift.setUpdateAt(new Date());
                workbatchShiftMapper.updateById(wbshift);
//                Integer worksNum = workbatchOrdlinkService.worksNum(o.getId(), o.getMaId());
//                //数量是否超过   与排产外的数据重复了。直接调用外部的排产状态数据即可。
//                Integer shiftPlanNum = workbatchShiftMapper.getPlanNumBySdId(o.getId());
//                shiftPlanNum = (shiftPlanNum != null) ? shiftPlanNum : 0;
//                Integer completeNum = (o.getCompleteNum() != null) ? o.getCompleteNum() : 0;
//                Integer planNum = (o.getPlanNum() != null) ? o.getPlanNum() : 0;
//                if (worksNum - sdIdNumMap.get(o.getId()) > 0) {
//                    o.setStatus("9");//设定没有排产完成"未排完"
//                }
                workbatchOrdlinkService.updateOrdStatus(o);
                //更新update的OEE的规则信息内容并且创建或者更新shift表内容
                //循环为每一个排产单增加对应的换型时间和速度
                boolean a = ruleProdoeeService.setRuleOrdoee(o.getId(), request.getSdDate(), request.getWsId(), request.getMaId(), status);
                if (!a) {
                    str.append(o.getWbNo() + "数据异常");
                }
                //如果是排产操作，需要设置主计划的排产计划时间
                Integer sdId = o.getId();
                Integer maId = o.getMaId();
                setMainplan(sdId, maId);//修改主计划内容，新增排产信息的状态
            }
        }
        return str.toString();
    }


    /****
     * 更新状态；按钮后进行状态变化 修改【发布】状态
     * @param request
     * @return
     * @throws ParseException
     */
    public String updateStatusByIssued(WorkbatchOrdlinkStatusUpdateRequest request) throws ParseException {
        StringBuilder str = new StringBuilder();
        List<WorkbatchOrdlinkShiftVO> workbatchOrdlinkList = workbatchOrdlinkMapper.selectBatchIdsAndWfIds(request.getSdIds(), request.getWfIds());
        if (!workbatchOrdlinkList.isEmpty()) {
            //查询发布状态内容信息
            for (WorkbatchOrdlinkShiftVO o : workbatchOrdlinkList) {
                //班次状态信息
                WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(o.getWfId());
                if (workbatchShift != null && (workbatchShift.getShiftStatus() == -1 || workbatchShift.getShiftStatus() == 0)) {
                    //shift
                    //状态：-1未下发，0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）
                    //状态（0起草1发布2正在生产3已完成4已挂起5废弃） -1待排产ERP接入 6驳回7已排产8部分完成9未排完
                    //为下发是更新排产班次状态信息
//                WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(o.getWfId());
                    String status = "1"; //表示发布状态
                    workbatchOrdlinkService.updateOrdStatusBySdsort(o); //更新状态信息
                    workbatchShift.setShiftStatus(0);//下发的把排产单状态修改成待接单状态
                    workbatchShift.setStatus(status);//下发后状态为1已下发的状态
                    workbatchShift.setUpdateAt(new Date());
                    workbatchShiftMapper.updateById(workbatchShift);
                }

                //判断数量是否未排完，进行状态更新
                //数量是否超过
                Integer yetNum = workbatchShiftMapper.getYetNum(o.getSdId());
                yetNum = (yetNum != null) ? yetNum : 0;
                Integer completeNum = (o.getPlanNumber() != null) ? o.getPlanNumber() : 0;
                Integer planNum = (o.getPlanNumber() != null) ? o.getPlanNumber() : 0;
                if (planNum - completeNum - yetNum > 0) {
                    o.setStatus("9");//设定没有排产完成"未排完"
                    o.setSdDate(request.getSdDate());
                    workbatchOrdlinkService.updateOrdStatus(o);//若没有排完，就更新状态
                }
            }
        }
        return str.toString();
    }

    /***
     * 设定状态驳回状态
     * @param request
     * @return
     */
    private String updateStatusByDown(WorkbatchOrdlinkStatusUpdateRequest request) {
        StringBuilder str = new StringBuilder();
        List<WorkbatchOrdlinkShiftVO> workbatchOrdlinkList = workbatchOrdlinkMapper.selectBatchIdsAndWfIds(request.getSdIds(), request.getWfIds());
        if (!workbatchOrdlinkList.isEmpty()) {
            //查询发布状态内容信息
            for (WorkbatchOrdlinkShiftVO o : workbatchOrdlinkList) {
                //正在生产的状态不能够改变
                WorkbatchShift wbshift = workbatchShiftMapper.selectById(o.getWfId());
                //正在生产的状态不能够改变
                if ("4".equalsIgnoreCase(wbshift.getStatus())) {
                    str.append(o.getWbNo() + "状态为暂停状态，无法进行驳回操作。");
                    continue;
                }
                //shift
                //状态：-1未下发，0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）
                //状态（0起草1发布2正在生产3已完成4已挂起5废弃） -1待排产ERP接入 6驳回7已排产8部分完成9未排完
                //为下发是更新排产班次状态信息
                if (wbshift != null && wbshift.getShiftStatus() != null && (wbshift.getShiftStatus() == -1 || wbshift.getShiftStatus() == 0)) {
                    String status = "6";//修改驳回的状态
                    o.setStatus(status);//设定驳回
                    workbatchOrdlinkService.updateOrdStatusBySdsort(o); //更新状态信息
                    //删除该排产数据信息对象
                    if (o.getWfId() != null) {
                        workbatchShiftMapper.deleteById(o.getWfId());
                    }
                }
            }
        }
        return str.toString();
    }

    /***
     * 设定状态 【废弃】操作
     * @param request
     * @return
     */
    private String updateStatusByDiscard(WorkbatchOrdlinkStatusUpdateRequest request) {
        StringBuilder str = new StringBuilder();
        List<WorkbatchOrdlinkShiftVO> workbatchOrdlinkList = workbatchOrdlinkMapper.selectBatchIdsAndWfIds(request.getSdIds(), request.getWfIds());
        if (!workbatchOrdlinkList.isEmpty()) {
            //查询发布状态内容信息
            for (WorkbatchOrdlinkShiftVO o : workbatchOrdlinkList) {
                //正在生产的状态不能够改变
//                if ("2".equalsIgnoreCase(o.getStatus()) || "3".equalsIgnoreCase(o.getStatus()) || "4".equalsIgnoreCase(o.getStatus())) {
//                    str.append(o.getWbNo() + "状态为生产状态，暂时无法改变其状态。");
//                    continue;
//                }
                //shift
                //状态：-1未下发，0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）
                //状态（0起草1发布2正在生产3已完成4已挂起5废弃） -1待排产ERP接入 6驳回7已排产8部分完成9未排完
                //为下发是更新排产班次状态信息
                //数量是否超过
                String status = "5";
                o.setStatus(status);//设定废弃状态信息
                workbatchOrdlinkService.updateOrdStatusBySdsort(o); //更新状态信息
                WorkbatchShift wbshift = workbatchShiftMapper.selectById(o.getWfId());
                if (wbshift != null) {
                    wbshift.setStatus(status);//班次的信息
                    wbshift.setUpdateAt(new Date());
                    workbatchShiftMapper.updateById(wbshift);//更改废弃状态
                }

            }
        }
        return str.toString();
    }

    /*****
     *【挂起】操作 --暂停
     * @param request
     * @return
     */
    private String updateStatusByHang(WorkbatchOrdlinkStatusUpdateRequest request) {
        StringBuilder str = new StringBuilder();
        List<WorkbatchOrdlinkShiftVO> workbatchOrdlinkList = workbatchOrdlinkMapper.selectBatchIdsAndWfIds(request.getSdIds(), request.getWfIds());
        if (!workbatchOrdlinkList.isEmpty()) {
            //查询发布状态内容信息
            for (WorkbatchOrdlinkShiftVO o : workbatchOrdlinkList) {
                //正在生产的状态不能够改变
                //shift
                //状态：-1未下发，0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）
                //状态（0起草1发布2正在生产3已完成4已挂起5废弃） -1待排产ERP接入 6驳回7已排产8部分完成9未排完
                String status = "4";//修改为暂停的状态
                //为下发是更新排产班次状态信息
                WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(o.getWfId());
                if (workbatchShift != null) {
                    workbatchShift.setShiftStatus(-1);//修改未待排产
                    workbatchShift.setStatus(status);//下发后状态为1已下发的状态
                    workbatchShift.setUpdateAt(new Date());
                    workbatchShiftMapper.updateById(workbatchShift);
                }
                //数量是否超过
                //o.setStatus(status);
                //workbatchOrdlinkService.updateOrdStatusBySdsort(o); //更新状态信息
            }
        }
        return str.toString();
    }


    /****
     * 是否为历史数据变不做更新
     * @param request
     * @return
     */
    private boolean isHistoryData(WorkbatchOrdlinkStatusUpdateRequest request) {
        //是否查看的为往期数据
        String targetDay = DateUtil.refNowDay();
        Date nowDate = DateUtil.toDate(targetDay, "yyyy-MM-dd");
        if (StringUtils.isNotBlank(request.getSdDate())) {
            Date sdDate = DateUtil.toDate(request.getSdDate(), "yyyy-MM-dd");
            if (nowDate.getTime() > sdDate.getTime()) {
                //throw new CommonException(HttpStatus.NOT_FOUND.value(), "往期数据无法进行状态修改");
                return true;
            }
        }
        return false;
    }

    public String updateStatus_old(WorkbatchOrdlinkStatusUpdateRequest request) throws ParseException {
        StringBuilder str = new StringBuilder();
        List<SdIdNumberVo> sdIdNumberVos = request.getSdIdNumberVos();
        List<Integer> collect = sdIdNumberVos.stream().map(SdIdNumberVo::getSdId).collect(Collectors.toList());
        List<WorkbatchOrdlinkShiftVO> workbatchOrdlinkList = workbatchOrdlinkMapper.selectBatchIdsAndWfIds(collect, request.getWfIds());
        if (!workbatchOrdlinkList.isEmpty()) {
            for (WorkbatchOrdlinkShiftVO o : workbatchOrdlinkList) {
                String status = request.getStatus().getType();
                //生产状态中不能驳回
                if (o.getRunStatus() == 1 && request.getStatus() == LocalEnum.WorkbatchOrdlinkStatus.DOWN) {
                    str.append(o.getWbNo() + "处于生产状态，无法修改  ");
                    continue;
                }
                //生产状态中不能废弃
                if (o.getRunStatus() == 1 && request.getStatus() == LocalEnum.WorkbatchOrdlinkStatus.DISCARD) {
                    str.append(o.getWbNo() + "处于生产状态，无法废弃");
                    continue;
                }
                //状态为已生产
                if (o.getStatus().equals("2") || o.getStatus().equals("3")) {
                    if (!status.equals("4")) {
                        str.append(o.getWbNo() + "处于生产状态，无法修改  ");
                        continue;
                    }
                }


                //当为驳回的时候需要删除该班次的计划信息
                if (request.getStatus() == LocalEnum.WorkbatchOrdlinkStatus.DOWN) {
                    //删除驳回的班次信息内容
//                    WorkbatchShift wshift = workbatchShiftMapper.getWsid(request.getSdDate(), o.getId(), request.getWsId(), request.getMaId());
                    if (o.getWfId() != null) {
                        workbatchShiftMapper.deleteById(o.getWfId());
                    }
                    //workbatchShiftMapper.deleBySdDateWsid(request.getSdDate(),o.getId(),request.getWsId(),request.getMaId());
                }
                //更新状态，和排产日期
                o.setSdDate(request.getSdDate());
                o.setStatus(request.getStatus().getType());
                workbatchOrdlinkService.updateOrdStatusBySdsort(o);
            }
        }

        return str.toString();
    }

    /***
     * 排产时候修改主计划内容信息
     * @param sdId
     */
    private void setMainplan(Integer sdId, Integer maId) {
        /*修改主计划相关数据*/
        List<WorkbatchShift> workbatchShiftList =
                workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>().eq("sd_id", sdId).orderByDesc("sd_date"));
        WorkbatchShift workbatchShift = null;
        if (!workbatchShiftList.isEmpty()) {
            workbatchShift = workbatchShiftList.get(0);
        }

        /*查询排产的主计划*/
        List<WorkbatchProgress> workbatchProgressList =
                workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("ma_id", maId));
        if (!workbatchProgressList.isEmpty()) {
            WorkbatchProgress workbatchProgress = workbatchProgressList.get(0);
            Integer planCount = workbatchProgress.getPlanCount();
            if (workbatchShift != null && workbatchProgress != null) {
                Integer planTotalTime = workbatchShift.getPlanTotalTime();//分
                if (planTotalTime == null) {
                    WorkbatchOrdoee workbatchOrdoee =
                            workbatchOrdoeeMapper.selectOne(new QueryWrapper<WorkbatchOrdoee>().eq("wk_id", sdId));
                    if (workbatchOrdoee != null) {
                        Integer speed = workbatchOrdoee.getSpeed();//速度
                        planTotalTime = 60 * planCount / speed;//分
                    }
                }
                workbatchProgress.setTotalTime(planTotalTime);//总计划时间(分钟)
                workbatchProgress.setUpdateAt(new Date());
                workbatchProgress.setStayTime(planTotalTime);//工序剩余时间(分钟)
                workbatchProgress.setStatus(1);
                workbatchProgressMapper.updateById(workbatchProgress);//修改排产主计划
            }
        }
    }

    /****
     * 设置功能操作
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLeanSet(WorkBatchOrdlinkUpdateLeanSet request) {
        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(request.getWfId());
        if (workbatchShift != null) {
            //判断数据合计
            WorkbatchOrdlink ordlink = workbatchOrdlinkMapper.selectById(workbatchShift.getSdId());
            Integer plannum = ordlink.getPlanNum();
            Integer completeNum = ordlink.getCompleteNum();
            workbatchShift.setMouldStay(request.getMouldStay());
            workbatchShift.setSpeed(request.getSpeed());
            workbatchShift.setPlanNum(request.getPlanNum());
            workbatchShift.setWasteNum(request.getWasteNum());
            //设定对应的数据信息
            workbatchShiftMapper.updateById(workbatchShift);
            //已完成数量
            //Integer yetNum = workbatchShiftMapper.getPlanNumBySdId(workbatchShift.getSdId());
            //yetNum = (yetNum != null) ? yetNum : 0;
            //Integer staycout = plannum - completeNum - yetNum;//未排产数量
            //查询已经排产的数量信息
//            if (staycout > 0) {
//                ordlink.setStatus(LocalEnum.WorkbatchOrdlinkStatus.NOSCHEDULING.getType());//未排产完成
//                //修改排产顺序和状态信息内容
//                workbatchOrdlinkService.updateOrdStatus(ordlink);
//            } else {
            ordlink.setStatus(LocalEnum.WorkbatchOrdlinkStatus.SCHEDULING.getType());
            //修改排产顺序和状态信息内容
            workbatchOrdlinkService.updateOrdStatus(ordlink);
//            }
//            else {
//                //已经发布就无需修改状态
//                if(!ordlink.getStatus().equalsIgnoreCase("1")) {
//                    ordlink.setStatus(LocalEnum.WorkbatchOrdlinkStatus.SCHEDULING.getType());//已排产完成
//                }
//            }
            //workbatchOrdlinkMapper.updateById(ordlink);
            //ModelMapperUtil.getStrictModelMapper().map(request, workbatchShift);

            //执行待排数据变化的判断操作----start Jenny
            setWorksNum(workbatchShift.getSdId(), workbatchShift.getMaId(), ordlink.getStatus());
            //执行待排数据变化的判断操作----end Jenny
        }
    }

    /*****
     * 修改主料、辅料、入库时间
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialAndRemark(WorkBatchOrdlinkMaterialUpdateRequest request) {
        WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectById(request.getSdId());
        if (workbatchOrdlink == null) {
            log.info("排产单信息不存在:[sdId:{}]", request.getSdId());
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "未找到排产单信息");
        }

        workbatchOrdlink.setRemarks(request.getRemarks());
        workbatchOrdlink.setSecondRemark(request.getSecondRemark());
        workbatchOrdlink.setFinalTime(request.getFinalTime());//最后最终交期
        workbatchOrdlinkService.updateOtherInfo(workbatchOrdlink);


        WorkbatchOrdlinkVO workbatchOrdlinkVO = new WorkbatchOrdlinkVO();
        workbatchOrdlinkVO.setSdId(request.getSdId());
        List<MaterMtinfoVO> materMtinfoVOS = materMtinfoMapper.findBySdId(workbatchOrdlinkVO);
        if (!materMtinfoVOS.isEmpty()) {
            materMtinfoVOS.forEach(m -> {
                if (m != null) {
                    //是否存在不存在就新增
                    //原料
                    if (m.getMaterBatchlinkId() == null) {
                        MaterBatchlink materBatchlink = new MaterBatchlink();
                        if (m.getMcId() != null && m.getMold() == 1 && request.getMainIngredientTime() != null) {
                            Date mainIngredientTime = request.getMainIngredientTime();
                            String format = DateTimeUtil.format(mainIngredientTime, DateTimeUtil.DEFAULT_DATE_FORMATTER);
                            saveMaterBatchlink(request, workbatchOrdlink, m, materBatchlink, format);
                        }
                        //辅料
                        if (m.getMcId() != null && m.getMold() == 2 && request.getIngredientTime() != null) {
                            Date mainIngredientTime = request.getIngredientTime();
                            String format = DateTimeUtil.format(mainIngredientTime, DateTimeUtil.DEFAULT_DATE_FORMATTER);
                            saveMaterBatchlink(request, workbatchOrdlink, m, materBatchlink, format);
                        }
                    }
                    if (m.getMaterBatchlinkId() != null) {
                        MaterBatchlink materBatchlink = materBatchlinkMapper.selectById(m.getMaterBatchlinkId());
                        if (materBatchlink != null) {
                            if (m.getMcId() != null && m.getMold() == 1) {
                                Date mainIngredientTime = request.getMainIngredientTime();
                                if (mainIngredientTime != null) {
                                    String format = DateTimeUtil.format(mainIngredientTime, DateTimeUtil.DEFAULT_DATE_FORMATTER);
                                    materBatchlink.setInstorageTime(format);
                                }
                            } else {
                                Date mainIngredientTime = request.getIngredientTime();
                                if (mainIngredientTime != null) {
                                    String format = DateTimeUtil.format(mainIngredientTime, DateTimeUtil.DEFAULT_DATE_FORMATTER);
                                    materBatchlink.setInstorageTime(format);
                                }
                            }
                            materBatchlinkMapper.updateById(materBatchlink);
                        }
                    }
                }
            });
        }

    }

    private void saveMaterBatchlink(WorkBatchOrdlinkMaterialUpdateRequest request, WorkbatchOrdlink
            workbatchOrdlink, MaterMtinfoVO m, MaterBatchlink materBatchlink, String format) {
        materBatchlink.setInstorageTime(format);
        materBatchlink.setSdId(request.getSdId());
        materBatchlink.setCreateAt(new Date());
        materBatchlink.setStatus(0);
        materBatchlink.setMlId(m.getId());
        materBatchlink.setMcId(m.getMcId());
        materBatchlink.setWkNo(workbatchOrdlink.getWbNo());
        materBatchlink.setProcessNum(m.getMtNum());
        materBatchlinkMapper.insert(materBatchlink);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrdReach(WorkBatchOrdReachUpdateRequest request) {
        StatisOrdreach statisOrdreach = statisOrdreachMapper.selectById(request.getId());
        if (statisOrdreach == null) {
            log.info("修改达成率计划数失败，达成率信息不存在:[id:{}]", request.getId());
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "达成率信息不存在");
        }
        statisOrdreach.setPlanCount(request.getPlanCount());
        statisOrdreachMapper.updateById(statisOrdreach);
    }

    /****
     * 点击保存按钮，保存排产编号，并且绑定下工序内容
     就、kkkkXX * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkBatchSort(WorkBatchSortUpdateRequest request) {
        //判断类型为1的时候进行下工序关联更新
        if (request != null && request.getType() != null && request.getType() == 1) {
            //设定本工序
            setOrdLinkBysdSort(request);
            //设定下道工序
            setDownprocessBysdSort(request);
        } else {
            //设定本工序；为零表示不绑定下工序
            setOrdLinkBysdSort(request);
        }
        //设定当班次排产的小时达成率
        statisOrdreachService.setOrdreach(request.getSdDate(), request.getWsId(), request.getMaId(), DBIdentifier.getProjectCode());//保存按钮保存达成率
    }

    /***
     * 更新下道工序的排产内容信息
     * @param request
     */
    public void setDownprocessBysdSort(WorkBatchSortUpdateRequest request) {
        List<WorkBatchSortUpdateDTO> workBatchSortUpdateDTOS = request.getWorkBatchSortUpdateDTOS();
        workBatchSortUpdateDTOS.forEach(o -> {
            WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.getBySdDateAndSdIdAndWsId(o.getSdId());
            List<WorkbatchOrdlink> watlink = workbatchOrdlinkMapper.workbathByDownOrd(workbatchOrdlink);
            if (watlink != null && !watlink.isEmpty()) {
                for (WorkbatchOrdlink ordlink : watlink) {
                    //ordlink.setSdSort(o.getSdSort());
                    workbatchOrdlinkMapper.updateBysdsort(o.getSdSort(), ordlink.getId());
                }
                log.info("已经更新upProcess下工序数量：watlink:" + ((watlink != null) ? watlink.size() : 0));
            }
        });
    }

    /****
     * 设定本工序的排产顺序
     * @param request
     * @return
     */
    public void setOrdLinkBysdSort(WorkBatchSortUpdateRequest request) {
        List<WorkBatchSortUpdateDTO> workBatchSortUpdateDTOS = request.getWorkBatchSortUpdateDTOS();
        String sdDate = request.getSdDate();
        Date nowtime = new Date();
        //WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(request.getWsId());
        workBatchSortUpdateDTOS.forEach(o -> {
            //根据id获取排产表的信息内容
            WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.getBySdDateAndSdIdAndWsId(o.getSdId());
            if (workbatchOrdlink != null) {
                workbatchOrdlink.setSdSort(o.getSdSort());//设定排产顺序信息
                workbatchOrdlinkMapper.updateBysdsort(o.getSdSort(), o.getSdId());//更新对象
            }
            //拿去排产oee设置表，如果oee没有数据则取工序对应设备的默认时速
            WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.getOeeBySdId(o.getSdId());
            if (workbatchOrdoee != null) {
                //设定班次，调整开始时间、结束时间
                WorkbatchShift workbatchShift = workbatchShiftMapper.getWsid(sdDate, o.getSdId(), request.getWsId(), request.getMaId());
                //查询该班次的信息，如果为空表示没有保存到该班次信息。
                if (workbatchShift == null) {
                    workbatchShift = new WorkbatchShift();
                    Integer plannum = (workbatchOrdlink.getPlanNum() != null) ? workbatchOrdlink.getPlanNum() : 0;//生产总量为计划生产总数
                    Integer mould = (workbatchOrdoee.getMouldStay() != null) ? workbatchOrdoee.getMouldStay() : 0;//换版时间
                    Integer speed = (workbatchOrdoee.getSpeed() != null) ? workbatchOrdoee.getSpeed() : 0;
                    Integer total = (int) Math.round(((double) plannum / speed) * rate) + mould;
//                    Integer total = plannum / speed * 60 + mould;
                    workbatchShift.setSdDate(sdDate);
                    workbatchShift.setSdSort(o.getSdSort());
                    workbatchShift.setSdId(o.getSdId());
                    workbatchShift.setPlanNum(plannum);
                    workbatchShift.setSpeed(speed);
                    workbatchShift.setMouldStay(mould);
                    workbatchShift.setPlanTotalTime(total);
                    workbatchShift.setWsId(request.getWsId());
                    workbatchShift.setCreateAt(nowtime);
                    workbatchShift.setUpdateAt(nowtime);
                    workbatchShift.setMaId(request.getMaId());
                    //日期拼接--需要重新单算
                    workbatchShiftMapper.insert(workbatchShift);
                } else {
                    //更新该班次的相关信息：计划产量、生产小时产能、排产的开始时间和结束时间、以及换模时间
                    Integer plannum = (workbatchShift.getPlanNum() != null) ? workbatchShift.getPlanNum() : 0;//生产总量为计划生产总数
                    Integer mould = (workbatchShift.getMouldStay() != null) ? workbatchShift.getMouldStay() : 0;//换版时间
                    Integer speed = (workbatchShift.getSpeed() != null) ? workbatchShift.getSpeed() : 0;
                    Integer total = (int) Math.round((double) (plannum * 60) / speed) + mould;
                    workbatchShift.setPlanTotalTime(total);
                    //workbatchShift.setPlanNum(plannum);
                    workbatchShift.setSpeed(speed);
                    workbatchShift.setSdDate(sdDate);
                    workbatchShift.setSdSort(o.getSdSort());
                    workbatchShift.setUpdateAt(nowtime);
                    workbatchShiftMapper.updateById(workbatchShift);
                }
            }
        });
        //更新该班次的开始时间和结束时间 probeginTime\profinishTime
        setStartEndTimeBySdDate(request.getSdDate(), request.getWsId(), request.getMaId());
    }

    /****
     * 设定排产的开始时间和结束时间
     * @param sdDate
     * @param wsId
     * @param maId
     */
    private void setStartEndTimeBySdDate(String sdDate, Integer wsId, Integer maId) {
        List<WorkbatchShift> getShift = workbatchShiftMapper.getShiftByMaid(sdDate, wsId, maId);
//        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(wsId);
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId, maId);
        Date begintime = (workbatchShiftset != null && workbatchShiftset.getStartTime() != null) ? DateUtil.toDate(sdDate + " " + DateUtil.format(workbatchShiftset.getStartTime(), "HH:mm:ss"), null) : null;
        Date finishtime = null;
        //班次的开始时间和结束时间
        if (getShift != null && !getShift.isEmpty()) {
            for (WorkbatchShift wshift : getShift) {
                Integer plannum = (wshift.getPlanNum() != null) ? wshift.getPlanNum() : 0;//生产总量为计划生产总数
                Integer mould = (wshift.getMouldStay() != null) ? wshift.getMouldStay() : 0;//换版时间
                Integer speed = (wshift.getSpeed() != null) ? wshift.getSpeed() : 0;
                Integer total = (int) Math.round(((double) plannum / speed) * rate) + mould;
                //Integer total = plannum / speed * 60 + mould; //总合计时间
                wshift.setProBeginTime(begintime);
                finishtime = DateUtil.addMinBystarttime(begintime, total);
                wshift.setProFinishTime(finishtime);
                workbatchShiftMapper.updateById(wshift);
                begintime = finishtime;//第二任务的开始时间就等于前面的结束时间
            }
        }
    }

    @Override
    public List<MachineMainfo> getSimilarDeviceList(String maType) {
        List<MachineMainfo> list = machineMainfoMapper.selectList(new QueryWrapper<MachineMainfo>().eq("ma_type", maType));
        return list;
    }

    /****
     * 移动状态方法
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindWorkBatch(WorkBatchBindRequest request) throws ParseException {
        Date nowtime = new Date();

        Date nowDate = new Date();
        long ctime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String targetDay = sdf.format(nowDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //移动状态方法内容
        request.getWorkBatchBindInfoRequests().forEach(o -> {
            WorkbatchShiftset shift = new WorkbatchShiftset();
            try {
                shift = getShiftStartAndEndTime(targetDay, simpleDateFormat, simpleDate, o);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            WorkbatchOrdlinkOeeVO ordlink = workbatchOrdlinkService.getOrdlinkOeeBySdId(o.getSdId());
            //计划数量
            Integer planNum = (ordlink != null && ordlink.getPlanNum() > 0) ? ordlink.getPlanNum() : 0;
//            WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(o.getWsId());
            WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(o.getWsId(), request.getMaId());

            WorkbatchOrdlink workbatchOrdlink = new WorkbatchOrdlink();//初始化新增迁移对象内容

            WorkbatchOrdoee oee = ordlink.getWorkbatchOrdoee();
            if (oee == null || oee.getSpeed() == null) {
                oee = new WorkbatchOrdoee();
            }

            //如果设备
            if (ordlink != null && ordlink.getMaId() != null && ordlink.getMaId().equals(request.getMaId())) {
                //存储班次信息
                WorkbatchShift workbatchShift = new WorkbatchShift();
                workbatchShift.setSdId(ordlink.getId());
                workbatchShift.setCreateAt(nowtime);
                workbatchShift.setSdDate(o.getSdDate());
                workbatchShift.setMaId(request.getMaId());
                workbatchShift.setPlanNum(o.getNumber());
                workbatchShift.setWsId(o.getWsId());
                workbatchShift.setCkName(workbatchShiftset.getCkName());
                workbatchShift.setPlanType("1");//默认生产状态，其他为排产
                workbatchShift.setStatus("0");
                workbatchShift.setShiftStatus(-1);//班次状态：-1未下发，0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）
                workbatchShift.setSpeed(ordlink.getSpeed());
                workbatchShift.setMouldStay(ordlink.getMouldStay());
                workbatchShift.setStartTime(shift.getStartTime());
                workbatchShift.setEndTime(shift.getEndTime());
                workbatchShiftMapper.insert(workbatchShift);
                return;
            }

            //超过取所有计划数
            if (o.getNumber() >= planNum) {
                ordlink.setMaId(request.getMaId());
                ordlink.setStatus("7");
                ordlink.setRunStatus(OrderStatusConstant.RUN_STATUS_WAITING);
                workbatchOrdlinkService.updateBindByMaId(ordlink);
            } else {
                //分离出来
                planNum = ordlink.getPlanNum() - o.getNumber();
                //修改原排产的数量分出来减去之前的总计划数量
                ordlink.setPlanNum(planNum);
                workbatchOrdlinkService.updatePlannumById(ordlink);//更新老的数据信息
                //迁移需要移动的数量的内容
                workbatchOrdlink.setId(null);
                workbatchOrdlink.setPlanNum(o.getNumber());
                workbatchOrdlink.setMaId(request.getMaId());
                workbatchOrdlink.setSdDate(o.getSdDate());
                workbatchOrdlink.setStatus("0");//状态修改为已排产状态；表示已排产
                workbatchOrdlink.setRunStatus(-1);//已排产未下发状态
                workbatchOrdlink.setCompleteNum(0);//初始化数据未完成数据
                workbatchOrdlink.setIncompleteNum(o.getNumber());//初始化未生产数据
                workbatchOrdlink.setExtraNum(0);//冗余数据
                workbatchOrdlink.setCreateAt(nowtime);
                workbatchOrdlinkMapper.insert(workbatchOrdlink);
            }

            //存储班次信息
            WorkbatchShift workbatchShift = new WorkbatchShift();
            workbatchShift.setSdId(ordlink.getId());
            workbatchShift.setCreateAt(nowtime);
            workbatchShift.setSdDate(o.getSdDate());
            workbatchShift.setMaId(request.getMaId());
            workbatchShift.setPlanNum(o.getNumber());
            workbatchShift.setWsId(o.getWsId());
            workbatchShift.setCkName(workbatchShiftset.getCkName());
            workbatchShift.setPlanType("1");//默认生产状态，其他为排产
            workbatchShift.setStatus("0");
            workbatchShift.setShiftStatus(-1);//班次状态：-1未下发，0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）
            workbatchShift.setSpeed(ordlink.getSpeed());
            workbatchShift.setMouldStay(ordlink.getMouldStay());
            workbatchShift.setStartTime(shift.getStartTime());
            workbatchShift.setEndTime(shift.getEndTime());
            workbatchShiftMapper.insert(workbatchShift);
        });
    }

    private WorkbatchShiftset getShiftStartAndEndTime(String targetDay, SimpleDateFormat simpleDateFormat, SimpleDateFormat simpleDate, WorkBatchBindInfoRequest o) throws ParseException {
//        WorkbatchShiftset shiftset = workbatchShiftsetMapper.selectById(o.getWsId());
        WorkbatchShiftset shiftset = workbatchShiftsetMapper.selectByMaid(o.getWsId(), null);

        Date classStartTime = null;
        Date classEndTime = null;
        String staTime = simpleDateFormat.format(shiftset.getStartTime());
        Date startTime = simpleDate.parse(targetDay + (" ") + staTime);
        String eTime = simpleDateFormat.format(shiftset.getEndTime());
        Date endTime = simpleDate.parse(targetDay + (" ") + eTime);
        classStartTime = startTime;
        //跨天
        if (endTime.getTime() < startTime.getTime()) {
            endTime = DateTimeUtil.getNextDay(endTime);
            classEndTime = endTime;
        } else {
            classEndTime = endTime;
        }
        shiftset.setStartTime(classStartTime);
        shiftset.setEndTime(classEndTime);
        return shiftset;
    }

    @Override
    public List<WorkbatchShift> reschedule(Integer sdId) {
        List<WorkbatchShift> reschedule = workbatchShiftMapper.reschedule(sdId);
        return reschedule;
    }

    @Override
    public Integer lockordreach(Integer wsId, String sdDate, Integer maId, Integer reachIslock) {
        List<StatisOrdreach> ordreachesls = statisOrdreachMapper.statisOrdInitList(sdDate, wsId, maId);
        List<Integer> ordrIds = new ArrayList<>();
        if (ordreachesls != null && ordreachesls.size() > 0) {
            ordreachesls.forEach(ordr -> {
                ordrIds.add(ordr.getId());
            });
            //判断查询出数据，更新锁定操作
            if (ordrIds != null && ordrIds.size() > 0) {
                return statisOrdreachMapper.lockOrdreach(ordrIds, reachIslock);//0不锁定1锁定(手工统计)
            }
        }
        return null;
    }

    /****
     * 是否确认锁定排产的机台顺序信息
     * @param wsId
     * @param sdDate
     * @param maId
     * @param wfsortIslock
     */
    @Override
    public void lockWfsort(Integer wsId, String sdDate, Integer maId, Integer wfsortIslock) {
        List<WorkbatchShift> wshift = workbatchShiftMapper.getShiftByMaid(sdDate, wsId, maId);
        List<Integer> wshiftIds = new ArrayList<>();
        if (wshift != null && wshift.size() > 0) {
            wshift.forEach(wfshif -> {
                wshiftIds.add(wfshif.getId());
            });
            if (wshiftIds != null && wshiftIds.size() > 0) {
                workbatchShiftMapper.lockWfsort(wshiftIds, wfsortIslock);
            }
        }
    }

    /*****
     * 刷新真实的实时数据
     * @param wsId
     * @param sdDate
     * @param maId
     */
    @Override
    public void refreshRealcount(Integer wsId, String sdDate, Integer maId) {
        //获取班次信息管理
//        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(wsId);
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId, maId);
        Date classStartTime = workbatchShiftset.getStartTime();
        Date classEndTime = workbatchShiftset.getEndTime();
        Date[] wsDatetime = StatisOrdreachServiceImpl.setWsDatetime(classStartTime, classEndTime, sdDate);//判断大小后赋值给开始时间和结束时间
        classStartTime = wsDatetime[0];
        classEndTime = wsDatetime[1];
        Map<Integer, Integer> hoursMap = statisOrdreachService.getRealcoutByHour(classStartTime, classEndTime, maId);//若是夜班需要跨时间
        if (hoursMap != null && hoursMap.size() > 0) {
            List<StatisOrdreach> realchLs = statisOrdreachMapper.statisOrdInitList(sdDate, wsId, maId);
            if (realchLs != null && realchLs.size() > 0) {
                for (StatisOrdreach sord : realchLs) {
                    Integer realCount = hoursMap.get(sord.getTargetHour());
                    statisOrdreachMapper.refreshRealcount(wsId, sdDate, maId, sord.getTargetHour(), realCount);//更新小时数的数量
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forcedEnd(Integer sdId) {
        //强制结束
        //WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectOne(new QueryWrapper<WorkbatchOrdlink>().eq("id", sdId));
        WorkbatchOrdlink workbatchOrdlink = new WorkbatchOrdlink();
        workbatchOrdlink.setId(sdId);
        workbatchOrdlink.setStatus("10");
        workbatchOrdlinkService.updateOrdStatus(workbatchOrdlink);
    }

    @Override
    public UpProcessScheduleVO upProcessSchedule(Integer wfId) {
        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
        UpProcessScheduleVO upProcessScheduleVO = new UpProcessScheduleVO();
        if (workbatchShift != null) {
            WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectById(workbatchShift.getSdId());
            if (workbatchOrdlink != null) {
                String prName = workbatchOrdlink.getPrName();
                WorkbatchOrdlink upProcessOrdlink = workbatchOrdlinkMapper.findByWbNoAndPrName(workbatchOrdlink.getWbNo(), prName);
                if (upProcessOrdlink == null) {
                    log.debug("暂无上工序排产单信息:[sdId:{}, ptId:{}, prId:{}]", workbatchShift.getSdId(), workbatchOrdlink.getPtId(), workbatchOrdlink.getPrId());
                    return new UpProcessScheduleVO();
                }

                Integer countNum = 0;
                //获取上报信息
                SuperviseExecute superviseExecute = superviseExecuteMapper.getByMaIdAndSdId(upProcessOrdlink.getMaId(), upProcessOrdlink.getId());
                if (superviseExecute != null) {
                    countNum = executeBrieferMapper.getCountNumBySdId(upProcessOrdlink.getId(), superviseExecute.getExId());
                    countNum = countNum + superviseExecute.getCurrNum();
                } else {
                    countNum = executeBrieferMapper.getCountNumBySdId(upProcessOrdlink.getId(), null);
                }
                upProcessScheduleVO.setId(upProcessOrdlink.getId());
                upProcessScheduleVO.setPlanNum(upProcessOrdlink.getPlanNum());
                upProcessScheduleVO.setCompletNum(countNum);
                upProcessScheduleVO.setStatus(upProcessOrdlink.getStatus());
                return upProcessScheduleVO;
            }
        }
        return upProcessScheduleVO;
    }

    @Override
    public List<String> existingSchedule(Integer maId, String sdDate) throws ParseException {
        //获取选定月的开始结束时间
        Date date = DateTimeUtil.format(sdDate, DateTimeUtil.DEFAULT_DATE_FORMATTER);
        Integer year = Integer.valueOf(DateTimeUtil.format(date, DateTimeUtil.YEAR_FORMATTER));
        int month = DateTimeUtil.getLastDayByDesignationMonth(date);
        String beginDayOfMonth = DateTimeUtil.getFirstDayOfMonth(year, month);
        String endDayOfMonth = DateTimeUtil.format(DateTimeUtil.getEndDayByMonth(month, date), DateTimeUtil.DEFAULT_DATE_FORMATTER);
        List<String> dates = workbatchShiftMapper.existingSchedule(maId, beginDayOfMonth, endDayOfMonth);
        return dates;
    }

    /*****
     *
     * @param sdId
     * @param maId
     * @deprecated 设定待排数据信息，并且设定待排的状态：为9未排完
     */
    @Override
    public void setWorksNum(Integer sdId, Integer maId, String status) {
        //排产数据统计数据
        Integer wknum = workbatchOrdlinkService.worksNum(sdId, maId);
//        //判断状态为排产的；排产的时候因为还没有shift数据；需要单独处理  , Integer schedulingnum  因为有事务回滚操作，无需重复操作
//        if (status == LocalEnum.WorkbatchOrdlinkStatus.SCHEDULING.getType()) {
//            wknum = wknum - schedulingnum;
//        }
        if (wknum > 0) {
            //未排完强制变成状态9的操作
            int cunm = workbatchOrdlinkService.updateByworksNum(sdId, wknum, "9");
            log.info("------------更新数据库，还有待排数据；工单id：" + sdId);
        } else {
            int cunm = workbatchOrdlinkService.updateByworksNum(sdId, 0, status);
        }
    }

    @Override
    public void setOrdShift(Integer wfid) {
        //修改ordlink数据shift的表里面数据
        WorkbatchShift workbatchShift = workbatchShiftService.getById(wfid);
        if (workbatchShift != null) {
            //获取ordlink的表数据信息
            WorkbatchOrdlink ordlinkinfo = workbatchOrdlinkService.getOrdById(workbatchShift.getSdId());
            Integer finishnum = executeBrieferService.getTotalByWfid(wfid);
            Integer completeNum = executeBrieferService.getTotalBySdid(workbatchShift.getSdId());
            if (ordlinkinfo != null && ordlinkinfo.getId() != null) {
                Integer planNum = ordlinkinfo.getPlanNum();
                planNum = (planNum == null) ? 0 : planNum;
                completeNum = (completeNum == null) ? 0 : completeNum;
                Integer incompleteNum = planNum - completeNum;//用总数减去已完成数量
                workbatchShiftService.setFinishNum(wfid, finishnum);
                workbatchOrdlinkService.setCompleteNum(workbatchShift.getSdId(), completeNum, incompleteNum);
            }
        }
    }

    @Override
    public List<SaturabilityVO> getSaturability(ProductionSchedulingDetailsParam detailsParam) {
        String starTime = detailsParam.getStarTime();
        if (StringUtil.isEmpty(starTime)) {
            detailsParam.setStarTime(DateUtil.refNowDay());
        }
        List<Integer> maIdList = detailsParam.getMaIdList();
        if (maIdList.isEmpty()) {
            return null;
        }
        List<Integer> wsIdList = detailsParam.getWsIdList();
        if (wsIdList.isEmpty()) {
            return null;
        }
        List<SaturabilityVO> saturabilityVOList = workbatchOrdlinkMapper.getSaturability(detailsParam);
        Integer totalTime = 0;
        for (Integer wsId : wsIdList) {
            for (Integer maId : maIdList) {
                WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.getWorkbatchShiftset(wsId, maId);
                if (workbatchShiftset != null) {
                    totalTime += workbatchShiftset.getStayTime();
                }
            }
        }
        for (SaturabilityVO saturabilityVO : saturabilityVOList) {
            saturabilityVO.setTotalTime(totalTime);
            Integer planTotalTime = saturabilityVO.getPlanTotalTime();
            if (totalTime != null && totalTime != 0) {
                double f = (double) planTotalTime / totalTime;
                BigDecimal b = new BigDecimal(f);
                saturabilityVO.setSaturability(b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        return saturabilityVOList;
    }
}
