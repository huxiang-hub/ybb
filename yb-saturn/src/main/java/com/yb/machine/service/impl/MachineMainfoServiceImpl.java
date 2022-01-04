package com.yb.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.entity.SuperviseRegular;
import com.yb.execute.mapper.SuperviseRegularMapper;
import com.yb.machine.entity.MachineClassify;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineClassifyMapper;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.machine.request.MonitorRequest;
import com.yb.machine.response.MachineMonitorVO;
import com.yb.machine.response.MonitorCapacityAnalyVO;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.machine.vo.*;
import com.yb.statis.mapper.StatisMachreachMapper;
import com.yb.statis.vo.DeviceOrderNumProgressListVO;
import com.yb.supervise.mapper.SuperviseIntervalMapper;
import com.yb.system.user.mapper.SaUserMapper;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备_yb_mach_mainfo 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class MachineMainfoServiceImpl extends ServiceImpl<MachineMainfoMapper, MachineMainfo> implements IMachineMainfoService {
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private MachineClassifyMapper machineClassifyMapper;
    @Autowired
    private SuperviseIntervalMapper superviseIntervalMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private SaUserMapper saUserMapper;
    @Autowired
    private StatisMachreachMapper statisMachreachMapper;
    @Autowired
    private SuperviseRegularMapper superviseRegularMapper;

    @Override
    public IPage<MachineMainfoVO> selectMachineMainfoPage(IPage<MachineMainfoVO> page,
                                                          Integer dpId, Integer prId, Integer status) {
        return page.setRecords(baseMapper.selectMachineMainfoPage(page, dpId, prId, status));
    }

    @Override
    public MachineMainfoVO getMachineMainfo(Integer maId) {
        return machineMainfoMapper.getMachineMainfo(maId);
    }

    @Override
    public MachineMainfoVO getMachineMainfoById(Integer id) {
        MachineMainfoVO machineMainfoById = machineMainfoMapper.getMachineMainfoById(id);
        return machineMainfoById;
    }

    @Override
    public List<MachineMainfoVO> getMachineMainfoByType(Integer id) {
        return machineMainfoMapper.getMachineMainfoByType(id);
    }

    @Override
    public boolean updateMachineMaInfo(MachineMainfo info) {

        return machineMainfoMapper.updateMachineMaInfo(info);
    }

    @Override
    public List<MachineMainfo> getMachins(String name) {

        return machineMainfoMapper.getMachins(name);
    }

    @Override
    public List<MachineMainfoVO> getMachinsByDpIdPrId(Integer maId, Integer dpId, Integer prId, Integer status) {
        List<MachineMainfoVO> oldList = machineMainfoMapper.getMachinsByDpIdPrId(maId, dpId, prId, status);
        List<MachineMainfoVO> newList = new ArrayList<>();
        for (MachineMainfoVO mv : oldList) {
            mv.setId(mv.getMaId());//让组件id 显是MaId
            mv.setLabel(mv.getMaName());//让组件Label 显是MaName
            mv.setValue(mv.getMaId());//让组件Value 显是MaId
            newList.add(mv);
        }
        return newList;
    }

    @Override

    public IPage<MachineMainfoVO> getAllMachinePages(IPage page, MachineMainfoVO mainfoVO) {
        return page.setRecords(baseMapper.getAllMachinePages(page, mainfoVO));
    }

    @Override
    public MachineMainfoVO getMachineInfoByCondition(String machineName, String machineNo) {
        return machineMainfoMapper.getMachineInfoByCondition(machineName, machineNo);

    }

    @Override
    public List<MachineMainfoVO> getRateByMachineId(Integer maId, String prName) {
        return machineMainfoMapper.getRateByMachineId(maId, prName);
    }

    @Override
    public List<MachineMainfoVO> selectMaNames(Integer prId) {
        return machineMainfoMapper.getMachinsByDpIdPrId(null, null, prId, null);
    }

    @Override
    public List<MachineMainfoVO> getMachineByDpId(Integer dpId) {
        List<MachineMainfoVO> oldList = machineMainfoMapper.getMachineByDpId(dpId);
        List<MachineMainfoVO> newList = new ArrayList<>();
        for (MachineMainfoVO mv : oldList) {
            mv.setId(mv.getMaId());//让组件id 显是MaId
            mv.setLabel(mv.getMaName() + "【" + (mv.getPrName() == null ? "暂无" : mv.getPrName()) + "】");//让组件Label 显是MaName
            mv.setValue(mv.getMaId());//让组件Value 显是MaId
            newList.add(mv);
        }
        return newList;
    }

    @Override
    public CompanyVO getCompanyInfoByMaId(Integer maId) {

        return baseMapper.getCompanyInfoByMaId(maId);
    }

    @Override
    public List<MachineMainfo> machineListByDeptId(Integer prId, Integer dpId) {
        return baseMapper.machineListByDeptId(prId, dpId);
    }

    @Override
    public List<MachineClassTree> MainchineClassiflyList() {
        /*查询所有设备分类信息*/
        List<MachineClassify> machineClassifyVOList = machineClassifyMapper.selectList(new QueryWrapper<>());
        /*查询所设备*/
        List<MachineMainfo> machineMainfoList = baseMapper.selectList(new QueryWrapper<>());
        List<MachineClassTree> machineClassTreeList = new ArrayList<>();
        List<MainfoVO> MainfoVOList = null;
        MachineClassTree machineClassTree;
        MainfoVO mainfoVO;
        Iterator<MachineClassify> classifyIterator = machineClassifyVOList.iterator();
        while (classifyIterator.hasNext()) {
            MachineClassify machineClassify = classifyIterator.next();
            machineClassTree = new MachineClassTree();
            Integer mtId = machineClassify.getId();
            machineClassTree.setValue(mtId);//分类id
            machineClassTree.setLabel(machineClassify.getModel());//品牌信息
            MainfoVOList = new ArrayList<>();
            for (MachineMainfo MachineMainfo : machineMainfoList) {
                if (mtId.equals(MachineMainfo.getMtId())) {
                    mainfoVO = new MainfoVO();
                    Integer maId = MachineMainfo.getId();
                    mainfoVO.setLabel(MachineMainfo.getName());//设备名称
                    mainfoVO.setValue(maId);//设备id
                    mainfoVO.setMaId(maId);
                    MainfoVOList.add(mainfoVO);
                }
            }
            machineClassTree.setChildren(MainfoVOList);
            if (machineClassTree.getChildren().isEmpty()) {//如果分类下没有设备则干点该分类
                classifyIterator.remove();
                continue;
            }
            machineClassTreeList.add(machineClassTree);
        }
        return machineClassTreeList;
    }

    @Override
    public List<MachineMonitorVO> monitor(MonitorRequest request) {
        String targetDay = DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER);

        List<MachineMonitorVO> monitor = machineMainfoMapper.monitor(request);
        if (monitor.isEmpty()) {
            return new ArrayList<>();
        }
        monitor.forEach(o -> {
            //开机人员
            String userIds = o.getUserIds();
            if (Func.isNotBlank(userIds)) {
                List<String> ids = Arrays.asList(userIds.split("\\|"));
                o.setOpenUserName(saUserMapper.getNameByIds(ids));
            }
            WorkbatchShiftset workbatchShiftset = null;
            if (request.getWsId() == null) {
                workbatchShiftset = workbatchShiftsetMapper.getNowWsTime(o.getMaId());
                if (workbatchShiftset == null) {
                    workbatchShiftset = workbatchShiftsetMapper.getNowWsDate(o.getMaId());
                }
            } else {
                workbatchShiftset = getWorkbatchShiftset(request.getWsId(), o.getMaId());
            }
            DeviceOrderNumProgressListVO orderNum = statisMachreachMapper.getOrderNum(targetDay, workbatchShiftset.getWsId(), o.getMaId());
            if (orderNum != null) {
                o.setFinishOrderNum(orderNum.getFinishNum());
                o.setAllOrderNum(orderNum.getWfNum());
            }
            //当班开机时间获取 暂时只能查interval
            Integer openTme = superviseIntervalMapper.getNowWsOpenTime(workbatchShiftset.getStartTime(), workbatchShiftset.getEndTime(), o.getMaId());
            o.setOpenTime(openTme);
            Integer wsYield = superviseRegularMapper.getWsYield(workbatchShiftset.getStartTime(), workbatchShiftset.getEndTime(), o.getMaId());
            o.setNowCapacity(wsYield);
            //todo 当班能耗暂无
            });

        return monitor;
    }

    @Override
    public List<MonitorCapacityAnalyVO> monitorCapacityAnaly(Integer wsId) {
        List<MonitorCapacityAnalyVO> vos = machineMainfoMapper.monitorCapacityAnaly(wsId);
        vos.forEach(o -> {
            WorkbatchShiftset workbatchShiftset = null;
            if (wsId == null) {
                workbatchShiftset = workbatchShiftsetMapper.getNowWsTime(o.getMaId());
                if (workbatchShiftset == null) {
                    workbatchShiftset = workbatchShiftsetMapper.getNowWsDate(o.getMaId());
                }
            } else {
                workbatchShiftset = getWorkbatchShiftset(wsId, o.getMaId());
            }
            Integer wsYield = superviseRegularMapper.getWsYield(workbatchShiftset.getStartTime(), workbatchShiftset.getEndTime(), o.getMaId());
            o.setCapacity(wsYield);
        });
        return vos;
    }

    @Override
    public List<MachineMainfoVO> getMachineTree(Integer dictKey) {
        return machineMainfoMapper.getMachineTree(dictKey);
    }

    @Override
    public List<TodayCapacityVO> todayCapacity() {
        List<TodayCapacityVO> todayCapacityVOS = superviseRegularMapper.todayCapacity();
        return todayCapacityVOS;
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

    @Override
    public  List<MachineMainfo> getListAll(){
        return machineMainfoMapper.getListAll();
    }
}
