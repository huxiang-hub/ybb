package com.yb.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.mapper.ProcessMachlinkMapper;
import com.yb.rule.entity.RuleCondition;
import com.yb.rule.entity.RuleProdoee;
import com.yb.rule.mapper.RuleProdoeeMapper;
import com.yb.rule.service.RuleProdoeeService;
import com.yb.rule.vo.WorkbatchOeePowerVO;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.IWorkbatchOrdoeeService;
import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.vo.WorkbatchOrdlinkShiftVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.digester3.internal.cglib.core.$ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RuleProdoeeServiceImpl extends ServiceImpl<RuleProdoeeMapper, RuleProdoee> implements RuleProdoeeService {
    @Autowired
    private RuleProdoeeMapper ruleProdoeeMapper;

    @Autowired
    private IWorkbatchOrdlinkService iWorkbatchOrdlinkService;

    @Autowired
    private IWorkbatchOrdoeeService iWorkbatchOrdoeeService;

    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private ProcessMachlinkMapper processMachlinkMapper;
    @Autowired
    private WorkbatchShiftService workbatchShiftService;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;

    public Integer GIRTHMODEL = 1;//尺寸按照周长来处理
    public Integer AREAMODEL = 2;//尺寸按照面积来处理
    public int MACHESURFACE = 3;//表面处理为3的value值信息

    static int rate = 60;

    @Override
    public RuleProdoee selectRuleProdoee(RuleCondition ruleCondition) {
        Integer maId = ruleCondition.getMaId();
        Integer material = (ruleCondition.getMaterial() != null && ruleCondition.getMaterial() > 0) ? ruleCondition.getMaterial() : null;
        Integer planNum = (ruleCondition.getPlanNum() != null && ruleCondition.getPlanNum() > 0) ? ruleCondition.getPlanNum() : null;
        Integer size = (ruleCondition.getSize() != null && ruleCondition.getSize() > 0) ? ruleCondition.getSize() : null;
        Integer model = (ruleCondition.getModel() != null && ruleCondition.getModel() > 0) ? ruleCondition.getModel() : null;
        RuleProdoee ruleProdoee = ruleProdoeeMapper.selectRuleProdoee(maId, material, planNum, size, model);
        return ruleProdoee;
    }

    @Override
    public IPage<RuleProdoee> selectRuleProdoeeByMaId(IPage<RuleProdoee> page, Integer maId) {
        List<RuleProdoee> ruleProdoeeList =
                ruleProdoeeMapper.selectRuleProdoeeByMaId(page, maId);
        return page.setRecords(ruleProdoeeList);
    }

    /****
     * * 获取换版速度及换模时间（再进行排产的时候进行oee的换型时间和速度，以及增加shift的换型和速度）
     * @param sdId
     * @param sdDate
     * @param wsId
     * @return
     */
    @Override
    public boolean setRuleOrdoee(Integer sdId, String sdDate, Integer wsId, Integer maId, String requestStatus) {
        Date nowtime = new Date();
        WorkbatchOrdlink ordlink = workbatchOrdlinkMapper.getBySdDateAndSdIdAndWsId(sdId);
        MachineMainfo mainfo = machineMainfoMapper.getMachineMainfo(maId);
        boolean issuccess = false;
        //Integer totaltime = 0;//设定生产总时间
        try {
            if (ordlink != null) { //如果没有该排产单将不进行oee追加
                String size = ordlink.getOperateSize();
                Integer[] pdsize = setSizeByptsize(size);
                RuleCondition ruleCondition = new RuleCondition();
                ruleCondition.setModel(GIRTHMODEL);//设定类型为产品的长度，周长 否则就是产品的面积计算
                ruleCondition.setMaId(ordlink.getMaId());

                //ruleCondition.setPlanNum(ordlink.getPlanNum());//增加产能的判断数据  先暂时屏蔽掉按照数量内容信息
                //判断类型就选择不同的尺寸判断模式
                if (GIRTHMODEL.equals(ruleCondition.getModel())) {
                    ruleCondition.setSize(pdsize[0] + pdsize[1]);
                } else if (AREAMODEL.equals(ruleCondition.getModel())) {
                    ruleCondition.setSize(pdsize[0] * pdsize[1]);
                }

                //先查询是否已有排产单的oee的数据
                WorkbatchOrdoee ordoee = iWorkbatchOrdoeeService.getOrdlinkOeeBySdId(sdId);
                if (ordoee == null || ordoee.getId() == null) {
                    ordoee = new WorkbatchOrdoee();
                    ordoee.setWkId(sdId);
                    ordoee.setCreateAt(nowtime);
                }
                //查询出对应的换版和性能规则信息内容 新增OEE数据
                WorkbatchOeePowerVO ordoeePower = setPower(ruleCondition, ordlink, mainfo, pdsize);
                if (ordoeePower != null && ordoeePower.getSdId() != null) {
                    ordoee.setSpeed(ordoeePower.getSpeed());
                    ordoee.setMouldStay(ordoeePower.getMouldStay());
                    ordoee.setWkId(ordoeePower.getSdId());
                    ordoee.setPlanTotalTime(ordoeePower.getPlanTotalTime());
                    ordoee.setUpdateAt(nowtime);//更新时间
                }

                //统计盒子是空或者零的话，就采用ERP导入过来的数据信息
                if (ordoee.getSpeed() == null || ordoee.getSpeed() <= 0) {
                    //速度为空，就要用ERP的速度和时间进行--ERP导入数据
                    if (ordoee.getSpeed() == null && ordoee.getErpSpeed() != null) {
                        ordoee.setSpeed(ordoee.getErpSpeed());
                        Integer totimes = ordoee.getPlanTime();//总用时时间
                        if (ordoee.getErpSpeed() > 0) {
                            Integer erptimes = ordlink.getPlanNum() / ordoee.getErpSpeed() * 60;
                            if (totimes != null && totimes > erptimes) {
                                ordoee.setMouldStay(totimes - erptimes);
                                ordoee.setPlanTotalTime(totimes);
                            }
                        }
                    }
                }
                //当速度都没有的时候采用默认3002标识
//                if (ordoee.getSpeed() == null || ordoee.getSpeed() <= 0) {
//                    ordoee.setSpeed(3002);//默认速速
//                    ordoee.setMouldStay(2);//默认时间分钟
//                }
                // 保存OEE数据参数表
                iWorkbatchOrdoeeService.saveOrUpdate(ordoee);

                //查询插入shift的参数变量信息内容[最新排产修改]
                setWorkbatchShift(sdDate, wsId, ordlink, ordoee, nowtime, requestStatus, maId);
                issuccess = true;
            }
        } catch (Exception e) {
            log.info("报错了！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！" + e.getMessage() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return false;
        }
        return issuccess;
    }

    //返回还剩余的计划数量信息
    private Integer getStaycountByPlannum(WorkbatchOrdlink ordlink) {
        return workbatchShiftMapper.getStaycount(ordlink.getPlanNum(), ordlink.getId());
    }


    /****
     * 通过规则条件查询规则表，如果规则表中没有符合条件【过胶机类型的特别的算法，根据传入的尺寸进行计算速度的算法】，获取设备的工序规则条件内容，
     * @param ruleCondition
     * @param ordlink
     * @param mainfo
     * @param pdsize
     * @return
     */
    private WorkbatchOeePowerVO setPower(RuleCondition ruleCondition, WorkbatchOrdlink ordlink, MachineMainfo mainfo, Integer[] pdsize) {
        WorkbatchOeePowerVO ordOeePower = new WorkbatchOeePowerVO();
        ordOeePower.setSdId(ordlink.getId());
        RuleProdoee ruleProdoee = selectRuleProdoee(ruleCondition);
        if (ruleProdoee != null && ruleProdoee.getId() != null) {
            //如果已经有oee初始值，就用oee的配置信息
            Integer plannum = (ordlink.getPlanNum() != null) ? ordlink.getPlanNum() : 0;//生产总量为计划生产总数
            Integer mould = (ruleProdoee.getPrepareTime() != null) ? ruleProdoee.getPrepareTime() : 0;//换版时间
            Integer speed = (ruleProdoee.getSpeed() != null) ? ruleProdoee.getSpeed() : 0;

            ordOeePower.setMouldStay(mould);
            ordOeePower.setSpeed(speed);
            //根据规则的速度以及设备类型为表面处理的管理，依据size大小转化speed的计算张的速度信息进行设定 设备类型过胶机算法
            if (mainfo != null && String.valueOf(MACHESURFACE).equals(mainfo.getMaType()) && speed <= 120) {
                speed = speed * 60 / (pdsize[0] - 3) * 1000;
                ordOeePower.setSpeed(speed);
            } else {
                ordOeePower.setSpeed(speed);//设定标准生产速度
            }

            if (speed != null && speed > 0) {
                Integer totaltime = (int) Math.round(((double) plannum / speed) * rate) + mould;
                ordOeePower.setPlanTotalTime(totaltime);//总和时间分钟
            }

        } else {
            //如果没有规则，就获取工序对应的设备换模时间--取标准工序的换型时间和速度
            ProcessMachlink processMachlink = processMachlinkMapper.getEntityByPrMt(ordlink.getMaId(), ordlink.getPrId());
            if (processMachlink != null && processMachlink.getSpeed() != null) {
                ordOeePower.setSpeed(processMachlink.getSpeed());
                //生产换型准备时间
                Integer mould = (ordOeePower.getMouldStay() != null) ? ordOeePower.getMouldStay() : 0;
                ordOeePower.setMouldStay(mould);//换型准备时间
                ordOeePower.setProducePreTime(mould); //生产准备时间
                //统计生产计数总的时间
                if (ordOeePower.getSpeed() != null) {
                    Integer totaltime = (ordlink != null && ordlink.getPlanNum() > 0) ? (int) Math.round(((double) ordlink.getPlanNum() / ordOeePower.getSpeed()) * rate) + mould : 0;
                    ordOeePower.setPlanTotalTime(totaltime);
                }
            }
        }
        return ordOeePower;
    }

    /****
     * 根据参数传入对应的班次对象信息内容
     * [最新排产]指定排产数之前入参没有 currentMaid，之前用的工单设备id
     * @param sdDate
     * @param wsId
     * @param ordlink
     * @param ordoee
     * @param nowtime
     * @param currentMaid 当前排产的设备id
     */
    private void setWorkbatchShift(String sdDate, Integer wsId, WorkbatchOrdlink ordlink, WorkbatchOrdoee ordoee, Date nowtime, String requestStatus, Integer currentMaid) {
        Integer sdId = ordlink.getId();
        String sdSort = ordlink.getSdSort();
        Integer maId = ordlink.getMaId();
        Integer mould = ordoee.getMouldStay();
        Integer speed = ordoee.getSpeed();
        Integer wastenum = ordlink.getWaste();
        Integer plannum = ordlink.getPlanNum();
        Integer planTime = ordoee.getPlanTotalTime();
        WorkbatchShift workbatchShift = workbatchShiftMapper.getBySdDateAndSdId(sdDate, sdId, wsId, currentMaid);
        if (workbatchShift == null) {
            workbatchShift = new WorkbatchShift();
        }
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId,currentMaid);
        workbatchShift.setWsId(wsId);
        workbatchShift.setSdSort(sdSort);//设定之前默认顺序
        workbatchShift.setCreateAt(nowtime);
        workbatchShift.setMouldStay(mould);
        workbatchShift.setSpeed(speed);
        workbatchShift.setSdDate(sdDate);
        workbatchShift.setWasteNum(wastenum);//设置计划放数信息
        if (requestStatus.equalsIgnoreCase("7"))
            workbatchShift.setShiftStatus(-1);//待接单  未下发状态
        if (requestStatus.equalsIgnoreCase("1"))
            workbatchShift.setShiftStatus(0);//待接单  未下发状态
            // [最新排产]待排产区指定数量排产，数据不对，之前没有注释下面判断
//        Integer plnum = getStaycountByPlannum(ordlink);
//        if (plnum != null && plnum > 0) {
//            workbatchShift.setPlanNum(plnum);
//        } else {
//            workbatchShift.setPlanNum(plannum);
//        }

        workbatchShift.setCkName(workbatchShiftset.getCkName());
        workbatchShift.setPlanTotalTime(planTime);
        //获取班次设定开始时间
        Date shiftsetStartTime = workbatchShiftset.getStartTime();
        //获取班次设定结束时间
        Date shiftsetEndTime = workbatchShiftset.getEndTime();
        //获取排产日期
        String ckStartTime = DateUtil.toDatestr(shiftsetStartTime, "HH:mm:ss");
        String ckEndTime = DateUtil.toDatestr(shiftsetEndTime, "HH:mm:ss");
        ckStartTime = sdDate + " " + ckStartTime;
        ckEndTime = sdDate + " " + ckEndTime;
        Date ckStartDate = DateUtil.toDate(ckStartTime, null);
        Date ckEndDate = DateUtil.toDate(ckEndTime, null);
        if (ckStartDate.getTime() > ckEndDate.getTime()) {
            ckEndDate = DateUtil.addDayForDate(ckEndDate, 1);
        }
        workbatchShift.setStartTime(ckStartDate);
        workbatchShift.setEndTime(ckEndDate);
        workbatchShift.setSdId(sdId);
        workbatchShift.setMaId(currentMaid);
        workbatchShiftService.saveOrUpdate(workbatchShift);
    }

    /***
     * 返回尺寸取值类型
     * @param size
     * @return
     */
    private Integer[] setSizeByptsize(String size) {
        String split[] = {"+", "*"};
        Integer length = 0;
        Integer width = 0;
        for (String lit : split) {
            if (size != null && size.indexOf(lit) >= 0) {
                lit = ("*".equalsIgnoreCase(lit)) ? "\\*" : lit;//特殊字符的分隔
                try {
                    length = (int) Double.parseDouble(size.split(lit)[0]);
                    width = (int) Double.parseDouble(size.split(lit)[1]);
                    break;
                } catch (Exception e) {
                    break;
                }
            }
        }
        return new Integer[]{length, width};
    }
}
