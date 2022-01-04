package com.yb.yilong.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.common.DateUtil;
import com.yb.execute.mapper.ExecuteInfoMapper;
import org.springblade.system.feign.XunYueClient;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.mapper.ExecuteBrieferMapper;
import com.yb.execute.mapper.ExecuteFaultMapper;
import com.yb.execute.service.IExecuteInfoService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.mapper.SuperviseBoxinfoMapper;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.xunyue.mapper.ExecuteFormalcMapper;
import com.yb.xunyue.mapper.ExecutePreparebMapper;
import com.yb.xunyue.request.XueYueOpenShiftRequest;
import com.yb.xunyue.entity.ExecuteFormalc;
import com.yb.xunyue.entity.ExecutePrepareb;
import com.yb.yilong.request.*;
import com.yb.yilong.response.BoxInfoNumberVO;
import com.yb.yilong.response.BoxInfoVO;
import com.yb.yilong.response.MachineDownPageVO;
import com.yb.yilong.response.WbNoInfoVO;
import com.yb.yilong.service.PicapiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.common.exception.CommonException;
import org.springblade.common.modelMapper.ModelMapperUtil;
import org.springblade.core.tool.api.R;
import org.springblade.common.tool.UUIDUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2021/1/6 14:17
 */
@Service
@Slf4j
public class PicapiServiceImpl implements PicapiService {

    @Resource
    @Qualifier("redisTemp")
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;
    @Autowired
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;
    @Autowired
    private IExecuteInfoService executeInfoService;
    @Autowired
    private ExecuteBrieferMapper brieferMapper;
    @Autowired
    private IWorkbatchShiftsetService iWorkbatchShiftsetService;
    @Autowired
    private IStoreInventoryService storeInventoryService;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private ExecuteFaultMapper executeFaultMapper;
    @Autowired
    private ExecuteFormalcMapper executeFormalcMapper;
    @Autowired
    private ExecutePreparebMapper executePreparebMapper;
    @Autowired
    private XunYueClient xunYueClient;
    @Autowired
    private ExecuteInfoMapper executeInfoMapper;
    //表示接单
    private final String ACCEPT = "B";
    //表示正式生产
    private final String PRO = "C";
    //表示结束订单
    private final String FINISH = "D";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R opShift(OpenShiftRequest request) {

        String status = (request != null && request.getStatus() != null) ? request.getStatus() : null;//如果状态不存在就不执行操作
        if (status == null) {
            return R.fail("请设定状态信息");
        }

        //做数据类型转化
        if ("1".equalsIgnoreCase(status)) {
            request.setStatus("D");
        } else if ("0".equalsIgnoreCase(status)) {
            //标识0的时候是接单操作
            request.setStatus("B");
        }
        SuperviseExecute superviseExecute = superviseExecuteMapper.getExecuteOrder(request.getMaId());
        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(request.getMaId());

        if (superviseExecute == null || superviseBoxinfo == null) {
            log.error("接单失败，实时表不存在:[request:{}]", request);
            return R.fail("系统接单请求失败，请联系管理员，设备参数或配置异常。");
            //throw new CommonException(20001, "接单请求失败请联系管理员");
        }

        //当前状态是否能够执行：判断状态不为B、C才能进行接单操作；
        String currStatus = superviseExecute.getExeStatus();//获取本身设备的状态


        //接单执行状态操作
        if (ACCEPT.equals(request.getStatus())) {
            //判断设备状态不为B或者并且不为D，才可以执行接单->
            if (!ACCEPT.equalsIgnoreCase(currStatus) && !PRO.equalsIgnoreCase(currStatus)) {
                return accept(request, superviseExecute, superviseBoxinfo);
            } else {
                return R.fail("当前设备已经接单，请重新返回");
            }
        } else if (PRO.equals(request.getStatus())) {
            //正式生产进行生产状态操作
            return pro(request, superviseExecute, superviseBoxinfo);
        } else if (FINISH.equals(request.getStatus())) {
            //表示结束订单
            return finish(superviseExecute, superviseBoxinfo);
        }
        return R.fail("请求参数错误");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R xunYueOpShift(XueYueOpenShiftRequest request) {
        SuperviseExecute superviseExecute = superviseExecuteMapper.getExecuteOrder(request.getMaId());
        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(request.getMaId());
        Integer wsId = null;
        if (superviseExecute == null || superviseBoxinfo == null) {
            log.error("讯越接单失败，实时表不存在:[request:{}]", request);
            return R.fail("系统接单请求失败，请联系管理员，设备参数或配置异常。");
        }
        ExecuteInfo execute = executeInfoMapper.selectById(superviseExecute.getExId());
        log.debug("开始接单操作:[ request:{}]", UUIDUtil.randomUUID(10), request);
        //订单号更改对上个订单结束操作
        boolean a = execute != null && execute.getWsId() != request.getClasses();
        if (a && superviseBoxinfo.getWbNo() != null && !request.getWbNo().equals(superviseBoxinfo.getWbNo())) {
            opStatusBymaId(request.getMaId(), "D");
        }

        //接单操作
        Date currTime = new Date();
        ExecuteInfo executeInfo = new ExecuteInfo();
        executeInfo.setStartTime(currTime);
        executeInfo.setCreateAt(currTime);
        executeInfo.setMaId(request.getMaId());
        executeInfo.setWbNo(request.getWbNo());
        //标记为接单0、接单1、执行中2、执行完成3、执行结束未上报
        executeInfo.setStatus(0);

        //获取当前设备的班次信息内容。
        WorkbatchShiftset workbatchShiftset = null;
        if (request.getClasses() == null) {
            workbatchShiftset = workbatchShiftsetMapper.getNowWsTime(request.getMaId());
            if (workbatchShiftset == null) {
                workbatchShiftset = workbatchShiftsetMapper.getNowWsDate(request.getMaId());
            }
            //获取当前时间的班次id信息
            if (workbatchShiftset != null) {
                wsId = workbatchShiftset.getWsId();
            }
        } else {
            wsId = request.getClasses();
        }

        Date startTime = (workbatchShiftset != null) ? workbatchShiftset.getStartTime() : null;
        String targeDay = DateUtil.refNowDay();
        if (startTime != null) {
            targeDay = DateUtil.toDatestr(startTime, "yyyy-MM-dd");
        }
        executeInfo.setTargetDay(targeDay);
        String usId = (request.getOperator() != null) ? request.getOperator().toString() : "";
        usId = (usId == null || usId.length() <= 0) ? ((superviseExecute.getOperator() != null) ? superviseExecute.getOperator().

                toString() : "") : usId;
        //当前操作人员信息
        executeInfo.setUsId(usId);
        executeInfo.setWsId(wsId);
        executeInfoService.save(executeInfo);

        //插入准备数据
        ExecutePrepareb executePrepareb = new ExecutePrepareb();
        executePrepareb.setCreateAt(currTime);
        executePrepareb.setEvent("B1");
        executePrepareb.setWsId(wsId == null ? null : wsId.toString());
        executePrepareb.setExId(executeInfo.getId());
        executePrepareb.setMaId(request.getMaId());
        executePrepareb.setStartAt(currTime);
        executePrepareb.setStartNum(0);
        executePrepareb.setUsIds(usId);
        executePrepareb.setUsId(usId == null ? null : Integer.valueOf(usId));
        executePreparebMapper.insert(executePrepareb);
        //更新实时表信息
        superviseExecute.setExeStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());
        superviseExecute.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());
        superviseExecute.setStartNum(superviseBoxinfo.getNumberOfDay());
        superviseExecute.setEndNum(0);
        superviseExecute.setCurrNum(0);
        superviseExecute.setStartTime(currTime);
        superviseExecute.setExId(executeInfo.getId());
        superviseExecute.setUsIds(usId);
        superviseExecute.setEndTime(null);
        superviseExecute.setWbNo(request.getWbNo());
        superviseExecute.setEsId(executePrepareb.getId());
        superviseExecute.setWbNo(request.getWbNo());
        superviseExecuteMapper.updateById(superviseExecute);
        superviseBoxinfo.setWbNo(request.getWbNo());
        //标识接单的状态，为1接单状态
        superviseBoxinfo.setBlnAccept(1);
        superviseBoxinfoMapper.updateById(superviseBoxinfo);
        //erp数据插入
        xunYueClient.open(request.getMaId(), request.getWbNo(), wsId);

        log.debug("讯越接单操作完成:[wbNo:{}, maId:{}]", request.getWbNo(), request.getMaId());
        return R.success("接单操作成功");
    }

    /*****
     * 执行状态更换方法操作
     * @param maId
     * @param status
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R xunYueOpStatus(Integer maId, Integer status) {

        //status 状态为：1调机状态B，2正式生产状态C 3结束操作D
        if (status == null) {
            return R.fail("请设定状态信息");
        }

        String exStatus = "";
        //做数据类型转化
        if (status == 2) {
            exStatus = "C";
        } else if (status == 1) {
            //标识0的时候是接单操作
            exStatus = "B";
        } else if (status == 3) {
            //标识0的时候是接单操作
            exStatus = "D";
        }
        //执行数据操作接口信息，传设备id、状态id
        R rt = opStatusBymaId(maId, exStatus);
        return rt;
    }

    /******
     * 根据设备id执行设备状态，
     * @param maId
     * @return
     */
    private R opStatusBymaId(Integer maId, String exStatus) {

        SuperviseExecute superviseExecute = superviseExecuteMapper.getExecuteOrder(maId);
        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        if (superviseExecute == null || superviseBoxinfo == null) {
            log.error("接单失败，设备还没有绑定盒子，实施表不存在:[maId:{}]", maId);
            return R.fail("系统接单请求失败，请联系管理员，设备参数或配置异常。");
        }
        String oldStatus = superviseExecute.getExeStatus();
        if (oldStatus.equals(GlobalConstant.ProType.AFTERPRO_STATUS.getType())) {
            return R.fail("切换状态失败，设备状态已为结束请重新接单。");
        }
        log.debug("开始更换状态信息操作:[maId:{}]", maId);

        if (exStatus == null || exStatus.equalsIgnoreCase(superviseExecute.getExeStatus())) {
            return R.fail("当前状态为设置状态，暂时无需更换。");
        }
        Date currTime = new Date();
        executeInfoService.upExeTime(superviseExecute.getExId(), currTime);
        //更新实时表信息
        superviseExecute.setUpdateAt(currTime);
        //当前设备状态为C，表示进入正式生产
        if (exStatus.equalsIgnoreCase(GlobalConstant.ProType.INPRO_STATUS.getType())) {
            superviseExecute.setExeStatus(GlobalConstant.ProType.INPRO_STATUS.getType());
            superviseExecute.setEvent(GlobalConstant.ProType.PRODUCT_EVENT.getType());
            //不为同一状态
            if (!oldStatus.equals(exStatus)) {
                //修改状态表
                ExecutePrepareb executePrepareb = executePreparebMapper.selectById(superviseExecute.getEsId());
                if (executePrepareb != null) {
                    executePrepareb.setEndAt(currTime);
                    executePrepareb.setEndNum(superviseExecute.getCurrNum());
                    executePrepareb.setTotalNum(executePrepareb.getEndNum() - executePrepareb.getStartNum());
                    executePreparebMapper.updateById(executePrepareb);
                }
                int bCount = executePreparebMapper.getCount(superviseExecute.getExId());
                //修改erp准备数据
                xunYueClient.update(maId, superviseBoxinfo.getWbNo(), 1, bCount);

                //增加执行状态表
                ExecuteFormalc executeFormalc = new ExecuteFormalc();
                executeFormalc.setExId(superviseExecute.getExId());
                executeFormalc.setEvent(GlobalConstant.ProType.PRODUCT_EVENT.getType());
                executeFormalc.setStartAt(currTime);
                executeFormalc.setCreateAt(currTime);
                executeFormalc.setStartNum(superviseExecute.getCurrNum());
                executeFormalc.setUsId(Integer.valueOf(superviseExecute.getUsIds()));
                executeFormalc.setUsIds(Integer.valueOf(superviseExecute.getUsIds()));

                executeFormalcMapper.insert(executeFormalc);
                superviseExecute.setEsId(executeFormalc.getId());

            }
        } else if (exStatus.equalsIgnoreCase(GlobalConstant.ProType.BEFOREPRO_STATUS.getType())) {
            //当前设备状态为B，表示进入生产准备
            superviseExecute.setExeStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());
            superviseExecute.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());
            //不为同一状态
            if (!oldStatus.equals(exStatus)) {
                //修改状态表
                ExecuteFormalc formalc = executeFormalcMapper.selectById(superviseExecute.getEsId());
                if (formalc != null) {
                    formalc.setEndAt(currTime);
                    formalc.setEndNum(superviseExecute.getCurrNum());
                    formalc.setTotalNum(formalc.getEndNum() - formalc.getStartNum());
                    executeFormalcMapper.updateById(formalc);
                }
                int proCount = executeFormalcMapper.getCount(superviseExecute.getExId());
                //修改erp生产数据
                xunYueClient.update(maId, superviseBoxinfo.getWbNo(), 2, proCount);
                //增加执行状态表
                ExecutePrepareb executePrepareb = new ExecutePrepareb();
                executePrepareb.setMaId(superviseBoxinfo.getMaId());
                executePrepareb.setExId(superviseExecute.getExId());
                executePrepareb.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());
                executePrepareb.setStartAt(currTime);
                executePrepareb.setCreateAt(currTime);
                executePrepareb.setStartNum(superviseExecute.getCurrNum());
                executePreparebMapper.insert(executePrepareb);
                superviseExecute.setEsId(executePrepareb.getId());
            }
        } else if (exStatus.equalsIgnoreCase(GlobalConstant.ProType.AFTERPRO_STATUS.getType())) {

            //结束订单
            if (oldStatus.equals(GlobalConstant.ProType.BEFOREPRO_STATUS.getType())) {
                ExecutePrepareb executePrepareb = executePreparebMapper.selectById(superviseExecute.getEsId());
                if (executePrepareb != null) {
                    executePrepareb.setEndNum(superviseExecute.getCurrNum());
                    executePrepareb.setEndAt(currTime);
                    executePrepareb.setTotalNum(executePrepareb.getEndNum() - executePrepareb.getStartNum());
                    executePreparebMapper.updateById(executePrepareb);
                }
            } else {
                ExecuteFormalc formalc = executeFormalcMapper.selectById(superviseExecute.getEsId());
                if (formalc != null) {
                    formalc.setEndAt(currTime);
                    formalc.setEndNum(superviseExecute.getCurrNum());
                    formalc.setTotalNum(formalc.getEndNum() - formalc.getStartNum());
                    executeFormalcMapper.updateById(formalc);
                }
            }
            //获取计数综合
            int bCount = executePreparebMapper.getCount(superviseExecute.getExId());
            int proCount = executeFormalcMapper.getCount(superviseExecute.getExId());
            //同步修改rep数据
            xunYueClient.finishUpdate(maId, superviseBoxinfo.getWbNo(), bCount, proCount);
            ExecuteInfo executeInfo = executeInfoService.getById(superviseExecute.getExId());
            //设定执行单的唯一主键
            executeInfo.setStatus(3);//设定状态为结束操作
            executeInfo.setEndTime(currTime);
            executeInfoService.upEndTime(executeInfo);//结束的对象直接更新状态为3，

            //增加上报表信息
            ExecuteBriefer briefer = new ExecuteBriefer();
            briefer.setBoxNum(superviseExecute.getCurrNum());
            briefer.setStartTime(executeInfo.getStartTime());
            briefer.setCreateAt(currTime);
            briefer.setHandle(0);
            briefer.setExId(superviseExecute.getExId());
            briefer.setReadyNum(superviseExecute.getReadyNum());
            briefer.setEndTime(currTime);
            brieferMapper.insert(briefer);

            //更新实时表信息
            superviseExecute.setStartNum(null);
            superviseExecute.setCurrNum(null);
            superviseExecute.setWbNo(null);
            superviseExecute.setWfId(null);
            superviseExecute.setExId(null);
            superviseExecute.setEsId(null);
            //当前设备状态为D，表示进入工单结束状态
            superviseExecute.setExeStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());
            superviseExecute.setEvent(GlobalConstant.ProType.FINISH_EVENT.getType());
            //修改设备状态表
            superviseBoxinfo.setWbNo(null);
            superviseBoxinfo.setBlnAccept(0);
            superviseBoxinfoMapper.updateById(superviseBoxinfo);
        }
        superviseExecuteMapper.updateById(superviseExecute);
        log.debug("正式生产操作完成:[maId:{}]", maId);

        return R.success("修改状态成功");
    }

    /*****
     * 设备id，对应班次id信息接口
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R xunYueOpClasses(XueYueOpenShiftRequest request) {
        Integer maId = request.getMaId();
        Integer classes = request.getClasses();
        String operator = request.getOperator();
        SuperviseExecute superviseExecute = superviseExecuteMapper.getExecuteOrder(maId);
//        SuperviseBoxinfo superviseBoxinfo = superviseBoxinfoMapper.getBoxInfoByMid(maId);
        //查询当前的工单信息内容和当前的执行EX_ID关联数据内容
        //首先是结束之前工单内容，然后再重新创建工单信息。

        if (superviseExecute != null && superviseExecute.getExId() != null) {
            ExecuteInfo exeinfo = executeInfoService.getById(superviseExecute.getExId());
            if (exeinfo != null) {
                Integer exClass = exeinfo.getWsId();//班次表内容信息
                if (classes == exClass) {
                    return R.fail("班次相同的，无需进行换班操作。");
                } else {
                    //班次不同
                    //换单之前先结束状态信息；
                    // finish(superviseExecute, superviseBoxinfo);
                    XueYueOpenShiftRequest requestNew = request;
                    requestNew.setWbNo(exeinfo.getWbNo());//把当前执行的工单wbNo设置到接单信息中
                    return xunYueOpShift(requestNew);
                }
            }
            return R.fail("执行单不存在，请重新进行接单。");
        }

        return R.fail("当前设备未接单，不存在换班，请选择接单操作。");
    }

    @Override
    public void opBriefer(OpBrieferRequest request) {
        SuperviseExecute superviseExecute = superviseExecuteMapper.getExecuteOrder(request.getMaId());
        ExecuteBriefer executeBriefer = brieferMapper.getByExId(superviseExecute.getExId());
        String key = request.getMaId().toString() + "-" + request.getWbNo();
        if (executeBriefer == null) {
            log.error("上报异常:[request:{}]", request);
            throw new CommonException(20022, "上报信息不存在,请先进行结束订单操作");
        }
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            redisTemplate.opsForValue().set(key, request.getIndex());
            executeBriefer.setCountNum(request.getGoodsNum());
            executeBriefer.setWasteNum(request.getWasteNum());
            executeBriefer.setProductNum(request.getGoodsNum() + request.getWasteNum());
            executeBriefer.setHandle(1);
            executeBriefer.setHandleTime(new Date());
        } else {
            //相同更新
            if (Integer.valueOf(o.toString()).equals(request.getIndex())) {
                executeBriefer.setCountNum(request.getGoodsNum());
                executeBriefer.setWasteNum(request.getWasteNum());
                executeBriefer.setProductNum(request.getGoodsNum() + request.getWasteNum());
            } else {
                //不同进行累加
                executeBriefer.setCountNum(request.getGoodsNum() + executeBriefer.getCountNum());
                executeBriefer.setWasteNum(request.getWasteNum() + request.getWasteNum());
                executeBriefer.setProductNum(executeBriefer.getCountNum() + executeBriefer.getWasteNum());
                redisTemplate.opsForValue().set(key, request.getIndex());
            }
        }
        brieferMapper.updateById(executeBriefer);
    }

    @Override
    public List<WbNoInfoVO> wbNoInfo(WbNoInfoRequest request) {
        log.debug("获取设备实时工单信息:[maId:{}]", request.getMaId());

        List<WbNoInfoVO> vo = executeInfoService.wbNoInfo(request);

        log.debug("获取设备实时工单信息完成:[maId:{}]", request.getMaId());
        return vo;
    }

    @Override
    public BoxInfoVO boxInfo(BoxInfoRequest request) {

        BoxInfoVO boxInfoVO = superviseBoxinfoMapper.boxInfo(request);

        return boxInfoVO;
    }

    @Override
    public BoxInfoNumberVO boxNumber(Integer maId) {

        BoxInfoNumberVO vo = superviseBoxinfoMapper.boxNumber(maId);

        return vo;
    }

    @Override
    public IPage<MachineDownPageVO> downPage(IPage<MachineDownPageVO> page, MachineDownPageRequest request) {

        if (request == null || request.getMaId() == null) {
            return null;
        }

        if (request.getStartTime() == null) {
            Date startTime = DateTimeUtil.getDayBegin();
            request.setStartTime(startTime);
        }
        if (request.getEndTime() != null) {
            Date endTime = request.getEndTime();
            endTime.setHours(23);
            endTime.setSeconds(59);
            endTime.setMinutes(59);
            request.setEndTime(endTime);
        }

        List<MachineDownPageVO> vos = executeFaultMapper.downPage(page, request);
        if (vos.isEmpty()) {
            return page.setRecords(new ArrayList<>());
        }
        return page.setRecords(vos);
    }


    /****
     *
     * @param superviseExecute
     * @param superviseBoxinfo
     * @return
     */
    private R finish(SuperviseExecute superviseExecute, SuperviseBoxinfo superviseBoxinfo) {

//        log.debug("开始结束生产操作:[request:{}]", request);
        Date currTime = new Date();
        if ("B".equals(superviseExecute.getExeStatus()) || "C".equals(superviseExecute.getExeStatus())) {
            //把执行单的数据进行结束操作。
            ExecuteInfo executeInfo = setfinishExecuteInfo(superviseExecute.getExId(), currTime);
            //设定上报数据信息
            setfinishBrieferInfo(superviseExecute, executeInfo, currTime);
            setfinishSuerviseInfo(superviseExecute, superviseBoxinfo, currTime);
            return R.success("结束生产操作成功");
        } else {
            return R.fail("结束生产失败,工单未生产状态，请进行接单操作。");
        }
    }

    /****
     * 修改执行单状态为结束操作
     * @param exId
     * @param currTime
     * @return
     */
    private ExecuteInfo setfinishExecuteInfo(Integer exId, Date currTime) {
        //状态为B和C都可以执行结束操作，未结束请点击结束后再执行
        ExecuteInfo executeInfo = executeInfoService.getById(exId);
        if (executeInfo == null) {
            //return R.fail("结束生产失败，请核查数据，未查询该接单操作。");
            return null;
        }
//        executeInfo.setEndTime(currTime);
//        executeInfo.setStatus(3);//0、接单1、执行中2、执行完成3、执行结束未上报
//        if (executeInfo.getUsId() == null) {
//            executeInfo.setUsId(request.getOperator());
//        }

        //设定执行单的唯一主键
        executeInfo.setStatus(3);//设定状态为结束操作
        executeInfo.setEndTime(currTime);
        executeInfoService.upEndTime(executeInfo);//结束的对象直接更新状态为3，
        return executeInfo;
    }

    private void setfinishBrieferInfo(SuperviseExecute superviseExecute, ExecuteInfo executeInfo, Date currTime) {
        //增加上报表信息
        ExecuteBriefer briefer = new ExecuteBriefer();
        briefer.setBoxNum(superviseExecute.getCurrNum());
        briefer.setStartTime(executeInfo.getStartTime());
        briefer.setCreateAt(currTime);
        briefer.setHandle(0);
        briefer.setExId(superviseExecute.getExId());
        briefer.setReadyNum(superviseExecute.getReadyNum());
        briefer.setEndTime(currTime);
        brieferMapper.insert(briefer);
    }

    private void setfinishSuerviseInfo(SuperviseExecute superviseExecute, SuperviseBoxinfo superviseBoxinfo, Date currTime) {
        //修改为未接单状态
        superviseBoxinfo.setBlnAccept(0);//设定结束表示未接单
        superviseBoxinfo.setWbNo("");//未接单设置为空
        superviseBoxinfoMapper.updateById(superviseBoxinfo);
        //修改准备计数为0
        superviseExecute.setReadyNum(0);//亿隆的设定没有生产准备的间隔时间
        superviseExecute.setReadyTime(null);
        superviseExecute.setEvent(GlobalConstant.ProType.FINISH_EVENT.getType());
        superviseExecute.setExeStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());
        superviseExecute.setEndTime(currTime);
        superviseExecuteMapper.updateById(superviseExecute);
    }


    /*****
     * 表示进入正式生产状态信息
     * @param request
     * @param superviseExecute
     * @param superviseBoxinfo
     * @return
     */
    private R pro(OpenShiftRequest request, SuperviseExecute superviseExecute, SuperviseBoxinfo superviseBoxinfo) {
        log.debug("开始正式生产操作:[request:{}]", request);

        if (!"B".equals(superviseExecute.getExeStatus())) {
            return R.fail("正式生产失败，工单未处于接单状态");
        }
        Date currTime = new Date();
//        ExecuteInfo executeInfo = executeInfoService.getById(superviseExecute.getExId());
//        executeInfo.setExeTime(currTime);
//        executeInfo.setStatus(1);//0、接单1、执行中2、执行完成3、执行结束未上报
        executeInfoService.upExeTime(superviseExecute.getExId(), currTime);
        //更新实时表信息
        superviseExecute.setReadyTime(currTime);
        superviseExecute.setReadyNum(superviseExecute.getReadyNum());
        superviseExecute.setExeStatus(GlobalConstant.ProType.INPRO_STATUS.getType());
        superviseExecute.setEvent(GlobalConstant.ProType.PRODUCT_EVENT.getType());
        superviseExecuteMapper.updateById(superviseExecute);
        log.debug("正式生产操作完成:[wbNo:{}, maId:{}]", request.getWbNo(), request.getMaId());

        return R.success("正式生产操作成功");

    }

    /******
     * 接单执行操作，获取接单执行
     * @param request
     * @param superviseExecute
     * @param superviseBoxinfo
     * @return
     */
    private R accept(OpenShiftRequest request, SuperviseExecute superviseExecute, SuperviseBoxinfo superviseBoxinfo) {
        log.debug("开始接单操作:[ request:{}]", UUIDUtil.randomUUID(10), request);

        //接单时已接单自动结束当前工单
        if (StringUtils.isNotBlank(superviseExecute.getExeStatus()) && ("B".equals(superviseExecute.getExeStatus())) ||
                "C".equals(superviseExecute.getExeStatus())) {
            OpenShiftRequest finishRequest = ModelMapperUtil.getStrictModelMapper().map(request, OpenShiftRequest.class);
            finishRequest.setStatus("1");
            finish(superviseExecute, superviseBoxinfo);//如果是其他自动结束
        }

        Date currTime = new Date();
        ExecuteInfo executeInfo = new ExecuteInfo();
        executeInfo.setStartTime(currTime);
        executeInfo.setCreateAt(currTime);
        executeInfo.setMaId(request.getMaId());
        executeInfo.setWbNo(request.getWbNo());  //TODO WYN去掉工单编号写入执行表  yilong外部接口需要编写工单信息
        //标记为接单
        executeInfo.setStatus(0);//0、接单1、执行中2、执行完成3、执行结束未上报
        //获取当前设备的班次信息内容。
        WorkbatchShiftset workbatchShiftset = null;
        workbatchShiftset = workbatchShiftsetMapper.getNowWsTime(request.getMaId());
        if (workbatchShiftset == null) {
            workbatchShiftset = workbatchShiftsetMapper.getNowWsDate(request.getMaId());
        }
        Date startTime = (workbatchShiftset != null) ? workbatchShiftset.getStartTime() : null;
        String targeDay = DateUtil.refNowDay();
        if (startTime != null) {
            targeDay = DateUtil.toDatestr(startTime, "yyyy-MM-dd");
        }
        executeInfo.setTargetDay(targeDay);

        executeInfo.setWfId(superviseExecute.getWfId());
        executeInfo.setSdId(superviseExecute.getSdId());
        String usId = (request.getOperator() != null) ? request.getOperator().toString() : "";
        usId = (usId == null || usId.length() <= 0) ? ((superviseExecute.getOperator() != null) ? superviseExecute.getOperator().toString() : "") : usId;
        executeInfo.setUsId(usId);//当前操作人员信息
        executeInfo.setWsId(workbatchShiftset.getWsId());//获取当前时间的班次id信息
        executeInfoService.save(executeInfo);

        //更新实时表信息
        superviseExecute.setExeStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());
        superviseExecute.setEvent(GlobalConstant.ProType.ACCEPT_EVENT.getType());
        superviseExecute.setStartNum(superviseBoxinfo.getNumberOfDay());
        superviseExecute.setEndNum(0);
        superviseExecute.setCurrNum(0);
        superviseExecute.setStartTime(currTime);
        superviseExecute.setExId(executeInfo.getId());
        superviseExecute.setUsIds(usId);
        superviseExecute.setEndTime(null);
        superviseExecuteMapper.updateById(superviseExecute);
        superviseBoxinfo.setWbNo(request.getWbNo());
        superviseBoxinfo.setBlnAccept(1);//标识接单的状态，为1接单状态
        superviseBoxinfoMapper.updateById(superviseBoxinfo);
        log.debug("接单操作完成:[wbNo:{}, maId:{}]", request.getWbNo(), request.getMaId());
        return R.success("接单操作成功");
    }

    public static void main(String[] args) {
        Integer a = 2;
        if (a != null && 1 == 2 || 1 == 1) {
            System.out.println(1);
        }
    }
}
