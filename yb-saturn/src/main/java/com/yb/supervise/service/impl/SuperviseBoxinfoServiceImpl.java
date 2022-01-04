package com.yb.supervise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.mapper.BaseDeptinfoMapper;
import com.yb.base.mapper.BaseStaffinfoMapper;
import com.yb.base.vo.BaseDeptinfoVO;
import com.yb.execute.mapper.SuperviseRegularMapper;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.mapper.SuperviseBoxinfoMapper;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.vo.DeptProductNumberVO;
import com.yb.supervise.vo.MaStatusNumberVO;
import com.yb.supervise.vo.MachineAtPresentStatusVO;
import com.yb.supervise.vo.SuperviseBoxinfoVO;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 设备当前状态表boxinfo-视图 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseBoxinfoServiceImpl extends ServiceImpl<SuperviseBoxinfoMapper, SuperviseBoxinfo> implements ISuperviseBoxinfoService {

    @Autowired
    private SuperviseBoxinfoMapper boxinfoMapper;
    @Autowired
    private BaseDeptinfoMapper baseDeptinfoMapper;
    @Autowired
    private BaseStaffinfoMapper baseStaffinfoMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private SuperviseRegularMapper superviseRegularMapper;

    @Override
    public IPage<SuperviseBoxinfoVO> selectSuperviseBoxinfoPage(IPage<SuperviseBoxinfoVO> page, SuperviseBoxinfoVO superviseBoxinfo) {
        return page.setRecords(baseMapper.selectSuperviseBoxinfoPage(page, superviseBoxinfo));
    }

    @Override
    public int getBoxNum(String mId) {
        return boxinfoMapper.getBoxNum(mId);
    }

    @Override
    public String getMacUser(String mId) {
        return boxinfoMapper.getMacUser(mId);
    }

    @Override
    public SuperviseBoxinfo getBoxInfoByBno(String uuid_s) {

        return boxinfoMapper.getBoxInfoByBno(uuid_s);
    }

    @Override
    public SuperviseBoxinfo getBoxInfoByMid(Integer maId) {
        return boxinfoMapper.getBoxInfoByMid(maId);
    }

    /**
     * 设备实时状态查询
     */
    @Override
    public List<SuperviseBoxinfoVO> selectSuperviseBoxinfoVO(Integer dpId, Integer prId) {
        /*查询所有人员信息*/
        List<BaseStaffinfo> baseStaffinfoList = baseStaffinfoMapper.selectList(new QueryWrapper<>());
        /*查询所有的设备生产状态*/
        List<SuperviseBoxinfoVO> superviseBoxinfoVOList = boxinfoMapper.selectBoxinfoList(dpId, prId);


        if (!superviseBoxinfoVOList.isEmpty()) {
            List<String> usNames;
            Iterator<SuperviseBoxinfoVO> superviseBoxinfoVOIt = superviseBoxinfoVOList.iterator();
            while (superviseBoxinfoVOIt.hasNext()) {
                SuperviseBoxinfoVO superviseBoxinfoVO = superviseBoxinfoVOIt.next();
                String status = superviseBoxinfoVO.getStatus();//状态1运行2停机3故障4离线
                Integer blnAccept = superviseBoxinfoVO.getBlnAccept();//是否接单
                if (StringUtil.isEmpty(status) || "3".equals(status)) {//没状态和和故障的干掉
                    superviseBoxinfoVOIt.remove();
                    continue;
                }
                if(("4").equals(status)){
                    superviseBoxinfoVO.setMaStatus("4");
                }else if (("1").equals(status)){
                    if(blnAccept == null || blnAccept == 0){
                        superviseBoxinfoVO.setMaStatus("2");
                    }else {
                        superviseBoxinfoVO.setMaStatus("1");
                    }
                }else if(("2").equals(status)){
                    if(blnAccept == null || blnAccept == 0){
                        superviseBoxinfoVO.setMaStatus("0");
                    }else {
                        superviseBoxinfoVO.setMaStatus("3");
                    }
                }
                if (!baseStaffinfoList.isEmpty()) {
                    String usIds = superviseBoxinfoVO.getUsIds();//生产人员ids
                    if (!StringUtil.isEmpty(usIds)) {
                        usNames = new ArrayList<>();
                        String[] split = usIds.split("\\|");
                        for (String usId : split) {
                            if (!StringUtil.isEmpty(usId)) {
                                for (BaseStaffinfo baseStaffinfo : baseStaffinfoList) {
                                    if (baseStaffinfo.getUserId() == null) {
                                        continue;
                                    }
                                    if (usId.equals(baseStaffinfo.getUserId().toString())) {
                                        usNames.add(baseStaffinfo.getName());
                                    }
                                }
                            }
                        }
                        //人员名称
                        superviseBoxinfoVO.setUsNames(usNames);
                    }
                }
            }
        }

        return superviseBoxinfoVOList;
    }

    @Override
    public Integer equipmentNum(String status) {
        return boxinfoMapper.pageCount(status);
    }

    @Override
    public List<SuperviseBoxinfoVO> getBoxListNotStop() {
        return boxinfoMapper.getBoxListNotStop();
    }

    @Override
    public List<BaseDeptinfoVO> selectDeptBoxinfoStatusList() {
        /*查询所有生产部门*/
        List<BaseDeptinfo> deptinfos = baseDeptinfoMapper.selectList(new QueryWrapper<BaseDeptinfo>()
                .eq("classify", 2).orderByAsc("sort"));
        /*查询所有当天排产单*/
        List<WorkbatchOrdlink> workbatchOrdlinks = boxinfoMapper.selectNumberList(null);
        List<BaseDeptinfoVO> baseDeptinfoVOS = new ArrayList<>();
        if (!deptinfos.isEmpty()) {
            List<WorkbatchOrdlink> workbatchOrdlinkList;
            Iterator<BaseDeptinfo> iterator = deptinfos.iterator();
            while (iterator.hasNext()) {
                BaseDeptinfo baseDeptinfo = iterator.next();
                BaseDeptinfoVO baseDeptinfoVO = new BaseDeptinfoVO();
                /*每个部门下不同状态的设备的数量*/
                List<MaStatusNumberVO> superviseBoxinfoVOS = boxinfoMapper.selectDeptBoxinfoStatusList(baseDeptinfo.getId());
                Integer maCount = 0;
                for (MaStatusNumberVO maStatusNumberVO : superviseBoxinfoVOS) {
                    if (maStatusNumberVO.getStatus() != null) {
                        maCount += maStatusNumberVO.getStatusNumber();//干掉没有绑定盒子的设备
                        Integer blnAccept = maStatusNumberVO.getBlnAccept();
                        String status = maStatusNumberVO.getStatus();
                        if(("4").equals(status)){
                            maStatusNumberVO.setMaStatus("4");
                        }else if (("1").equals(status)){
                            if(blnAccept == null || blnAccept == 0){
                                maStatusNumberVO.setMaStatus("2");
                            }else {
                                maStatusNumberVO.setMaStatus("1");
                            }
                        }else if(("2").equals(status)){
                            if(blnAccept == null || blnAccept == 0){
                                maStatusNumberVO.setMaStatus("0");
                            }else {
                                maStatusNumberVO.setMaStatus("3");
                            }
                        }

                    }
                }
                if (maCount == 0) {//如果该车间下没有设备,则干掉这个车间
                    iterator.remove();
                    continue;
                }
                workbatchOrdlinkList = new ArrayList<>();
                if (!workbatchOrdlinks.isEmpty()) {
                    for (WorkbatchOrdlink WorkbatchOrdlink : workbatchOrdlinks) {
                        if (WorkbatchOrdlink.getDpId().equals(baseDeptinfo.getId())) {
                            workbatchOrdlinkList.add(WorkbatchOrdlink);
                        }
                    }
                }
                Integer sdNumber = 0;//已生产完成排产单数
                Integer planNums = 0;//生产总数量
                Integer completeNums = 0;//已生产完成数
                Integer incompleteNums = 0;//未生产完成数
                /*查询工厂生产数量*/
                //List<WorkbatchOrdlink> workbatchOrdlinkList = boxinfoMapper.selectNumberList(baseDeptinfo.getId());
                DeptProductNumberVO deptProductNumberVO = new DeptProductNumberVO();
                if (!workbatchOrdlinkList.isEmpty()) {
                    Iterator<WorkbatchOrdlink> workbatchOrdlinkIt = workbatchOrdlinkList.iterator();
                    while (workbatchOrdlinkIt.hasNext()) {
                        WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkIt.next();
                        String status = workbatchOrdlink.getStatus();
                        /*if("0".equals(status) || "6".equals(status) || "7".equals(status)){
                            workbatchOrdlinkIt.remove();
                            continue;
                        }*/
                        if (StringUtil.isEmpty(workbatchOrdlink.getIncompleteNum())) {
                            workbatchOrdlink.setIncompleteNum(0);
                        }
                        planNums += workbatchOrdlink.getPlanNum();
                        completeNums += workbatchOrdlink.getCompleteNum();
//                        incompleteNums += workbatchOrdlink.getIncompleteNum();
                        if ("3".equals(status)) {
                            sdNumber++;
                        }
                    }
                }
                incompleteNums = planNums - completeNums;
                Integer sdNum = workbatchOrdlinkList.size();
                Integer unSdNumber = sdNum - sdNumber;
                if (unSdNumber < 0) {
                    unSdNumber = 0;
                }
                if (incompleteNums < 0) {
                    incompleteNums = 0;
                }
                deptProductNumberVO.setSdNum(sdNum);//排产单总数量
                deptProductNumberVO.setSdNumber(sdNumber);//已生产完成的排产单数量
                deptProductNumberVO.setUnSdNumber(unSdNumber);//未生产完成的排产单数量
                deptProductNumberVO.setPlanNums(planNums);//生产总数量
                deptProductNumberVO.setCompleteNums(completeNums);//已生产完成数
                deptProductNumberVO.setIncompleteNums(incompleteNums);//未生产完成数

                baseDeptinfoVO.setDeptProductNumberVO(deptProductNumberVO);//排产单信息
                baseDeptinfoVO.setMaCount(maCount);//设备总数
                baseDeptinfoVO.setId(baseDeptinfo.getId());//部门id
                baseDeptinfoVO.setDpName(baseDeptinfo.getDpName());//部门名
                baseDeptinfoVO.setPId(baseDeptinfo.getPId());
                baseDeptinfoVO.setTenantId(baseDeptinfo.getTenantId());
                baseDeptinfoVO.setClassify(baseDeptinfo.getClassify());
                baseDeptinfoVO.setDpNum(baseDeptinfo.getDpNum());
                baseDeptinfoVO.setFullName(baseDeptinfo.getFullName());
                baseDeptinfoVO.setSort(baseDeptinfo.getSort());
                baseDeptinfoVO.setStatusNumber(superviseBoxinfoVOS);//不同状态设备数
                baseDeptinfoVOS.add(baseDeptinfoVO);
            }
        }
        return baseDeptinfoVOS;
    }

    @Override
    public List<ProcessWorkinfoVO> selectDeptBoxinfoSdList(Integer dpId) {
        /*查询车间下所有工序*/
        List<ProcessWorkinfoVO> processWorkinfoVOS = boxinfoMapper.selectDpProcess(dpId);
        /*查询所有人员信息*/
        List<BaseStaffinfo> baseStaffinfoList = baseStaffinfoMapper.selectList(new QueryWrapper<>());
        /*查询所有的设备生产状态*/
        List<SuperviseBoxinfoVO> superviseBoxinfoVOList = boxinfoMapper.selectDeptBoxinfoSdList(null, null);
        if (!processWorkinfoVOS.isEmpty()) {
            Iterator<ProcessWorkinfoVO> iterator = processWorkinfoVOS.iterator();
            while (iterator.hasNext()) {
                ProcessWorkinfoVO processWorkinfoVO = iterator.next();
                List<SuperviseBoxinfoVO> superviseBoxinfoVOS = new ArrayList<>();
                for (SuperviseBoxinfoVO superviseBoxinfoVO : superviseBoxinfoVOList) {
                    if (processWorkinfoVO.getId().equals(superviseBoxinfoVO.getPrId()) && dpId.equals(superviseBoxinfoVO.getDpId())) {
                        superviseBoxinfoVOS.add(superviseBoxinfoVO);
                    }
                }
                /*查询每个工序下对应的设备生产状态*/
                //superviseBoxinfoVOS = boxinfoMapper.selectDeptBoxinfoSdList(processWorkinfoVO.getId(), dpId);
                if (!superviseBoxinfoVOS.isEmpty()) {
                    Iterator<SuperviseBoxinfoVO> it = superviseBoxinfoVOS.iterator();
                    while (it.hasNext()) {
                        SuperviseBoxinfoVO superviseBoxinfoVO = it.next();
                        if (StringUtil.isEmpty(superviseBoxinfoVO.getStatus())) {
                            it.remove();
                            continue;
                        }
                        String usIds = superviseBoxinfoVO.getUsIds();
                        if (!StringUtil.isEmpty(usIds)) {
                            List<String> usNames = new ArrayList<>();
                            String[] split = usIds.split("\\|");
                            for (String usId : split) {
                                if (!StringUtil.isEmpty(usId)) {
                                    /*查询该工单的生产人员*/
                                   /* BaseStaffinfo user = baseStaffinfoMapper.selectOne(new QueryWrapper<BaseStaffinfo>().eq("user_id", Integer.valueOf(usId)));
                                    if(user != null){
                                        usNames.add(user.getName());
                                    }*/
                                    for (BaseStaffinfo baseStaffinfo : baseStaffinfoList) {
                                        if (baseStaffinfo.getUserId() == null) {
                                            continue;
                                        }
                                        if (usId.equals(baseStaffinfo.getUserId().toString())) {
                                            usNames.add(baseStaffinfo.getName());
                                        }
                                    }
                                }
                            }
                            //人员名称
                            superviseBoxinfoVO.setUsNames(usNames);
                        }
                    }
                    if (superviseBoxinfoVOS.isEmpty()) {
                        iterator.remove();
                        continue;
                    }
                    processWorkinfoVO.setSuperviseBoxinfoVOS(superviseBoxinfoVOS);
                } else {
                    iterator.remove();
                }
            }
        }
        return processWorkinfoVOS;
    }

    @Override
    public boolean removerListByMaid(List<Integer> maIds) {
        Integer result = boxinfoMapper.removerListByMaid(maIds);
        return result != null && result >= 1;
    }

    @Override
    public MachineAtPresentStatusVO getBoxinfoStatusByMaId(Integer maId) {
        MachineAtPresentStatusVO presentStatusVO = boxinfoMapper.getBoxinfoStatusByMaId(maId);
        /*查询所有人员信息*/
        List<BaseStaffinfo> baseStaffinfoList = baseStaffinfoMapper.selectList(new QueryWrapper<>());
        if (presentStatusVO != null) {
            Integer wsId = presentStatusVO.getWsId();
            String sdDate = presentStatusVO.getSdDate();
            String sdNum = presentStatusVO.getSdNum();
            /*查询当班所有排产单的已完成和正在运行总数*/
            Integer Num = workbatchShiftMapper.getCompleteNum(maId, wsId, sdDate);
            presentStatusVO.setSdNum(Num + "/" + sdNum);
            String usIds = presentStatusVO.getUsIds();
            if (usIds != null) {
                List<String> usNames = new ArrayList<>();
                String[] split = usIds.split("\\|");
                for (String usId : split) {
                    if (!StringUtil.isEmpty(usId)) {
                        for (BaseStaffinfo baseStaffinfo : baseStaffinfoList) {
                            if (baseStaffinfo.getUserId() == null) {
                                continue;
                            }
                            if (usId.equals(baseStaffinfo.getUserId().toString())) {
                                usNames.add(baseStaffinfo.getName());
                            }
                        }
                    }
                }
                presentStatusVO.setUsNames(usNames);
            }

        }
        return presentStatusVO;
    }


    private WorkbatchShiftset getWorkbatchShiftset(Integer wsId, Integer maId) {
        WorkbatchShiftset workbatchShiftset;
        workbatchShiftset = workbatchShiftsetMapper.getByWsIdAndMaId(wsId, maId);
        try {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formatTime = sdf.format(date);
            String staTime = simpleDateFormat.format(workbatchShiftset.getStartTime());
            Date startTime = simpleDate.parse(formatTime + (" ") + staTime);
            String eTime = simpleDateFormat.format(workbatchShiftset.getEndTime());
            Date endTime = simpleDate.parse(formatTime + (" ") + eTime);
            //跨天
            if (endTime.getTime() < startTime.getTime()) {
                endTime = DateTimeUtil.getNextDay(endTime, 1);
            }
            //当前处于跨天了
            if (date.getHours() < endTime.getHours() && date.getHours() < startTime.getHours()) {
                startTime = DateTimeUtil.getFrontDay(startTime, 1);
                endTime = simpleDate.parse(formatTime + (" ") + eTime);
            }
            workbatchShiftset.setStartTime(startTime);
            workbatchShiftset.setEndTime(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return workbatchShiftset;
    }

}
