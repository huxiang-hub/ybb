package com.yb.stroe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.mapper.ExecuteTraycardMapper;
import com.yb.execute.service.ExecuteTraycardService;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.service.IExecuteInfoService;
import com.yb.execute.vo.PhoneTrayCardVO;
import com.yb.statis.excelUtils.JxlsUtils;
import com.yb.stroe.entity.*;
import com.yb.stroe.mapper.StoreInventoryMapper;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.service.IStoreInventoryhisService;
import com.yb.stroe.service.StoreSeatService;
import com.yb.stroe.vo.*;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.excelUtils.ExportlUtil;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lzb
 * @since 2020-09-19
 */
@Slf4j
@Service
public class StoreInventoryServiceImpl extends ServiceImpl<StoreInventoryMapper, StoreInventory> implements IStoreInventoryService {

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private StoreSeatService seatService;
    @Autowired
    private StoreInventoryMapper inventoryMapper;
    @Autowired
    private ExecuteTraycardService executeTraycardService;
    @Autowired
    private StoreInlogServiceImpl storeInlogService;
    @Autowired
    private StoreOutlogServiceImpl outlogService;
    @Autowired
    private StoreMorelogServiceImpl morelogService;
    @Autowired
    private StoreRemovelogServiceImpl removelogService;
    @Autowired
    private StoreDislocatlogServiceImpl dislocatlogService;
    @Autowired
    private StoreChecklogServiceImpl checklogService;
    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;
    @Autowired
    private ExecuteTraycardMapper executeTraycardMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private IStoreInventoryhisService storeInventoryhisService;
    @Autowired
    private IExecuteInfoService executeInfoService;
    @Autowired
    private IExecuteBrieferService executeBrieferService;

    /**
     * 入库
     *
     * @param stId   库位id
     * @param trayId 托板id
     * @param stType 存储类型1、半成品2、成品 3、原料4、辅料5、备品备件
     * @param etIds  标识卡ids，逗号隔开
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean putStore(Integer stId, Integer trayId, Integer stType, String etIds) {
        // 如果标识卡etIds不为空代表重新打印标识卡，需要清除之前台账及入库日志
        if (!StringUtils.isEmpty(etIds)) {
            List<Integer> integers = Func.toIntList(etIds);
            this.deleteBatchByetIds(integers);
            storeInlogService.deleteBatchByEtIds(etIds);
        }
        // 标识卡位空直接入库
        StoreSeat seat = seatService.getById(stId);
        Integer allTrayNum = seat.getTrayNum();
        QueryWrapper<StoreInventory> wrapper = new QueryWrapper<>();
        wrapper.eq("st_id", stId);
        List<StoreInventory> list = this.list(wrapper);
        int useLayNum = 0;
        for (StoreInventory inventory : list) {
            useLayNum += inventory.getLayNum();
        }
        ExecuteTraycard traycard = executeTraycardService.getById(trayId);
        Integer sdId = traycard.getSdId();
        WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectById(sdId);
        String operateSize = null;
        if (workbatchOrdlink != null) {
            operateSize = workbatchOrdlink.getOperateSize();
        }
        Integer layNum = traycard.getLayNum();
        if ((allTrayNum - useLayNum) >= layNum) {
            StoreInventory inventory = new StoreInventory();
            inventory.setEtPdnum(traycard.getTrayNum());
            inventory.setStType(stType);
            inventory.setStId(stId);
            inventory.setStNo(seat.getStNo());
            inventory.setStSize(operateSize);
            inventory.setEtId(trayId);
            inventory.setLayNum(traycard.getLayNum());
            inventory.setCreateAt(LocalDateTime.now());
            inventory.setStatus(2);
            inventory.setAreaId(seat.getSrId());
            inventory.setStModel(1);
            inventory.setStWay(3);
            inventory.setDbId(trayId);
            inventoryMapper.insert(inventory);
            StoreInlog storeInlog = new StoreInlog();
            storeInlog.setStType(stType);
            storeInlog.setStId(stId);
            storeInlog.setStNo(seat.getStNo());
            storeInlog.setStSize(operateSize);
            storeInlog.setEtId(trayId);
            storeInlog.setEtPdnum(traycard.getTrayNum());
            storeInlog.setUsId(traycard.getUsId());
            storeInlog.setLayNum(traycard.getLayNum());
            storeInlog.setCreateAt(LocalDateTime.now());
            storeInlog.setDbId(inventory.getDbId());
            storeInlog.setStModel(inventory.getStModel());
            storeInlogService.save(storeInlog);
            return true;
        }
        return false;
    }

    /**
     * 出库
     *
     * @param trayIds 要出库的托板id，用,（多个用逗号隔开）
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean outStore(String trayIds, Integer usId) {
        QueryWrapper<StoreInventory> wrapper = new QueryWrapper<>();
        wrapper.in("et_id", Arrays.asList(trayIds.split(",")));
        List<StoreInventory> list = this.list(wrapper);
        List<ExecuteTraycard> executeTraycardList = executeTraycardMapper.selectList(
                new QueryWrapper<ExecuteTraycard>().in("id", Arrays.asList(trayIds.split(","))));

        Integer operator = null;
        if (!executeTraycardList.isEmpty()) {
            Integer maId = executeTraycardList.get(0).getMaId();
            if (maId != null) {
                SuperviseExecute superviseExecute = superviseExecuteMapper.selectOne(
                        new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
                operator = superviseExecute.getOperator();//操作人
            }
        }

        for (StoreInventory inventory : list) {
            StoreOutlog storeOutlog = new StoreOutlog();
            storeOutlog.setUsId(usId);
            storeOutlog.setStType(inventory.getStType());
            storeOutlog.setStId(inventory.getStId());
            storeOutlog.setStNo(inventory.getStNo());
            storeOutlog.setStSize(inventory.getStSize());
            storeOutlog.setMlId(inventory.getMlId());
            storeOutlog.setEtId(inventory.getEtId());
            storeOutlog.setEtPdnum(inventory.getEtPdnum());
            storeOutlog.setLayNum(inventory.getLayNum());
            storeOutlog.setCreateAt(LocalDateTime.now());
            storeOutlog.setDbId(inventory.getDbId());
            storeOutlog.setStModel(inventory.getStModel());
            outlogService.save(storeOutlog);
            StoreInventoryhis storeInventoryhis = new StoreInventoryhis();
            storeInventoryhis.setSiId(inventory.getId());
            storeInventoryhis.setStType(inventory.getStType());
            storeInventoryhis.setStId(inventory.getStId());
            storeInventoryhis.setStNo(inventory.getStNo());
            storeInventoryhis.setStSize(inventory.getStSize());
            storeInventoryhis.setMlId(inventory.getMlId());
            storeInventoryhis.setEtPdnum(inventory.getEtPdnum());
            storeInventoryhis.setEtId(inventory.getEtId());
            storeInventoryhis.setLayNum(inventory.getLayNum());
            storeInventoryhis.setStatus(inventory.getStatus());
            storeInventoryhis.setCreateTime(LocalDateTime.now());
            storeInventoryhisService.save(storeInventoryhis);
        }
        return this.remove(wrapper);
    }

    /**
     * @param trayId        托板标识id
     * @param currentNum    当前数量
     * @param currentSeatId 当前库位id
     * @param currentLocal  当前库位编码
     * @description 盘点
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void check(Integer trayId, Integer currentNum, Integer currentSeatId, String currentLocal) {
        QueryWrapper<StoreInventory> wrapper = new QueryWrapper<>();
        wrapper.eq("et_id", trayId);
        StoreInventory inventory = this.getOne(wrapper);
        StoreChecklog storeChecklog = new StoreChecklog();
        if (!inventory.getStId().equals(currentSeatId)) {
            // 错位
            StoreDislocatlog storeDislocatlog = new StoreDislocatlog();
            storeDislocatlog.setEtId(trayId);
            storeDislocatlog.setLayNum(inventory.getLayNum());
            storeDislocatlog.setCurrLocal(currentLocal);
            storeDislocatlog.setBefLocal(inventory.getStNo());
            storeDislocatlog.setEtPdnum(currentNum);
            storeDislocatlog.setBefPdnum(inventory.getEtPdnum());
            storeDislocatlog.setDiffNum(currentNum - inventory.getEtPdnum());
            dislocatlogService.save(storeDislocatlog);
            storeChecklog.setStatus(3);
            inventory.setStId(currentSeatId);
        } else if (inventory.getEtPdnum() > currentNum) {
            // 盘缺
            StoreRemovelog storeRemovelog = new StoreRemovelog();
            storeRemovelog.setEtId(trayId);
            storeRemovelog.setLayNum(inventory.getLayNum());
            storeRemovelog.setEtPdnum(currentNum);
            storeRemovelog.setBefPdnum(inventory.getEtPdnum());
            storeRemovelog.setDiffNum(inventory.getEtPdnum() - currentNum);
            removelogService.save(storeRemovelog);
            storeChecklog.setStatus(2);
            inventory.setEtPdnum(currentNum);
        } else if (inventory.getEtPdnum() < currentNum) {
            // 盘盈
            StoreMorelog storeMorelog = new StoreMorelog();
            storeMorelog.setEtId(trayId);
            storeMorelog.setLayNum(inventory.getLayNum());
            storeMorelog.setEtPdnum(currentNum);
            storeMorelog.setBefPdnum(inventory.getEtPdnum());
            storeMorelog.setDiffNum(currentNum - inventory.getEtPdnum());
            morelogService.save(storeMorelog);
            storeChecklog.setStatus(1);
            inventory.setEtPdnum(currentNum);
        } else {
            // 正常
            storeChecklog.setStatus(4);
        }
        storeChecklog.setStType(inventory.getStType());
        storeChecklog.setSkType(0);
        storeChecklog.setStId(currentSeatId);
        storeChecklog.setStNo(currentLocal);
//        storeChecklog.setUsId(0);
//        storeChecklog.setUsName("");
        // 是否复核
        storeChecklog.setIsReview(0);
//        storeChecklog.setRwUsid(0);
//        storeChecklog.setRwUsname("");
        checklogService.save(storeChecklog);
        this.updateById(inventory);
    }

    /**
     * 根据库区id获取库位的台账已经放置拖板数量及剩余托板位置数量
     *
     * @param areaId 库区id，如果为空则查所有
     * @return
     */
    @Override
    public List<SeatInventoryVO> seatInventoryInfo(Integer areaId) {
        List<StoreSeat> storeSeats = seatService.getByAreaId(areaId);
        List<SeatInventoryVO> seatInventoryVOS = inventoryMapper.seatInventoryInfo(areaId);
        HashMap<Integer, Integer> map = new HashMap<>();
        for (SeatInventoryVO seatInventoryVO1 : seatInventoryVOS) {
            map.put(seatInventoryVO1.getSeatId(), seatInventoryVO1.getUseNum());
        }
        List<SeatInventoryVO> list = new ArrayList<>();
        for (StoreSeat storeSeat : storeSeats) {
            SeatInventoryVO seatInventoryVO = new SeatInventoryVO();
            seatInventoryVO.setSeatId(storeSeat.getId());
            seatInventoryVO.setSrId(areaId);
            seatInventoryVO.setSrNo(storeSeat.getSrNo());
            seatInventoryVO.setStType(storeSeat.getStType());
            seatInventoryVO.setStNo(storeSeat.getStNo());
            seatInventoryVO.setSize(storeSeat.getSize());
            seatInventoryVO.setLayer(storeSeat.getLayer());
            seatInventoryVO.setTrayNum(storeSeat.getTrayNum());
            seatInventoryVO.setCapacity(storeSeat.getCapacity());
            seatInventoryVO.setSort(storeSeat.getSort());
            seatInventoryVO.setXNum(storeSeat.getXNum());
            seatInventoryVO.setYNum(storeSeat.getYNum());
            // 已经存放托盘
            if (map.containsKey(storeSeat.getId())) {
                Integer useNum = map.get(storeSeat.getId());
                seatInventoryVO.setUseNum(useNum);
                seatInventoryVO.setUsableNum(storeSeat.getTrayNum() - useNum);
                int lockNum = 0;
                QueryWrapper<StoreInventory> wrapper = new QueryWrapper<>();
                wrapper.eq("st_id", storeSeat.getId());
                List<StoreInventory> storeInventories = inventoryMapper.selectList(wrapper);
                for (StoreInventory storeInventory : storeInventories) {
                    if (2 == storeInventory.getStatus()) {
                        int laynum = (storeInventory.getLayNum() != null) ? storeInventory.getLayNum() : 0;
                        lockNum += laynum;
                    }
                }
                seatInventoryVO.setLockNum(lockNum);
            } else {
                seatInventoryVO.setStatus(3);
                seatInventoryVO.setUseNum(0);
                seatInventoryVO.setUsableNum(storeSeat.getTrayNum());
            }
            if (seatInventoryVO.getUsableNum() != null && seatInventoryVO.getUsableNum() > 0) {
                list.add(seatInventoryVO);
            }
        }
        return list;
    }

    /**
     * 根据库位id获取当前库位后面的连续库位已经放置拖板数量及剩余托板位置数量
     *
     * @param seatId 库位id
     * @return
     */
    @Override
    public List<SeatInventoryVO> seatInventoryInfoBySort(Integer seatId) {
        StoreSeat seat = seatService.getById(seatId);
        Integer srId = seat.getSrId();
        Integer sort = seat.getSort();
        List<SeatInventoryVO> seatInventoryVOS = this.seatInventoryInfo(srId);
        List<SeatInventoryVO> collect = seatInventoryVOS.stream().filter(s -> s.getSort() >= sort).sorted(Comparator.comparing(SeatInventoryVO::getSort)).collect(Collectors.toList());
        return collect;
    }

    /**
     * 根据标识卡id批量删除台账
     *
     * @param etIds 标识卡ids
     */
    @Override
    public void deleteBatchByetIds(List<Integer> etIds) {
        if (!etIds.isEmpty()) {
            QueryWrapper<StoreInventory> wrapper = new QueryWrapper<>();
            wrapper.in("et_id", etIds);
            inventoryMapper.delete(wrapper);
        }
    }

    @Override
    public void updateStatus(List<Integer> etIdList, Integer status) {
        inventoryMapper.updateStatus(etIdList, status);
    }

    @Override
    public Integer getUsableNum(Integer mpId) {
        return inventoryMapper.getUsableNum(mpId);
    }

    @Override
    public void deleteByEtIdList(List<Integer> etIdList) {
        inventoryMapper.deleteByEtIdList(etIdList);
    }

    @Override
    public IPage<StoreInventorySemiVO> pageStoreFindList(Integer current, Integer size, StoreInventoryVO storeInventoryVO) {
        List<StoreInventorySemiVO> storeInventoryVOlist = inventoryMapper.pageStoreFindList(current, size, storeInventoryVO);
        List<Integer> countmap = inventoryMapper.pageStoreFindListCount(storeInventoryVO);
        int count = (countmap != null && countmap.size() > 0) ? countmap.size() : 0;
        IPage<StoreInventorySemiVO> page = new Page<StoreInventorySemiVO>();
        page.setTotal(count);
        return page.setRecords(storeInventoryVOlist);
    }

    @Override
    public void updateStatusBymlIdList(List<String> mlIdList, Integer status) {
        inventoryMapper.updateStatusBymlIdList(mlIdList, status);
    }

    @Override
    public void putStoreByNumber(ExecuteBriefer executeBriefer) {
        if (executeBriefer.getId() == null) {
            StoreInventory inventory = new StoreInventory();
            inventory.setStType(2);
            inventory.setStatus(1);
            inventory.setEtPdnum(executeBriefer.getBoxNum());
            inventory.setStModel(2);
            inventory.setDbId(executeBriefer.getId());
            inventory.setStWay(2);
            inventoryMapper.insert(inventory);
        } else {
            List<ExecuteTraycard> executeTraycards = executeTraycardService.list(Wrappers.<ExecuteTraycard>lambdaQuery().eq(ExecuteTraycard::getExId, executeBriefer.getExId()));
            // 判断是否存在托盘入库，不存在则直接根据数量入库
            StoreInventory inventory = null;
            if (executeTraycards.size() == 0) {
                inventory = inventoryMapper.selectOne(Wrappers.<StoreInventory>lambdaQuery().eq(StoreInventory::getDbId, executeBriefer.getId()).eq(StoreInventory::getStModel, "2"));
                if (null == inventory) {
                    inventory = new StoreInventory();
                }
                inventory.setStType(2);
                inventory.setStatus(1);
                inventory.setEtPdnum(executeBriefer.getCountNum());
                inventory.setStModel(2);
                inventory.setDbId(executeBriefer.getId());
                inventory.setStWay(2);
                this.saveOrUpdate(inventory);
            }
        }
    }


    /**
     * 根据库区id获取库位的台账已经放置拖板数量及剩余托板位置数量(空位及有托盘的都查出来)
     *
     * @param areaId
     * @return
     */
    @Override
    public List<SeatInventoryVO> seatInventoryInfo1(Integer areaId) {
        List<StoreSeat> storeSeats = seatService.getByAreaId(areaId);
        List<SeatInventoryVO> seatInventoryVOS = inventoryMapper.seatInventoryInfo(areaId);
        HashMap<Integer, Integer> map = new HashMap<>();
        for (SeatInventoryVO seatInventoryVO1 : seatInventoryVOS) {
            map.put(seatInventoryVO1.getSeatId(), seatInventoryVO1.getUseNum());
        }
        List<SeatInventoryVO> list = new ArrayList<>();
        for (StoreSeat storeSeat : storeSeats) {
            SeatInventoryVO seatInventoryVO = new SeatInventoryVO();
            seatInventoryVO.setSeatId(storeSeat.getId());
            seatInventoryVO.setSrId(areaId);
            seatInventoryVO.setSrNo(storeSeat.getSrNo());
            seatInventoryVO.setStType(storeSeat.getStType());
            seatInventoryVO.setStNo(storeSeat.getStNo());
            seatInventoryVO.setSize(storeSeat.getSize());
            seatInventoryVO.setLayer(storeSeat.getLayer());
            seatInventoryVO.setTrayNum(storeSeat.getTrayNum());
            seatInventoryVO.setCapacity(storeSeat.getCapacity());
            seatInventoryVO.setSort(storeSeat.getSort());
            seatInventoryVO.setXNum(storeSeat.getXNum());
            seatInventoryVO.setYNum(storeSeat.getYNum());
            // 已经存放托盘
            if (map.containsKey(storeSeat.getId())) {
                Integer useNum = map.get(storeSeat.getId());
                seatInventoryVO.setUseNum(useNum);
                seatInventoryVO.setUsableNum(storeSeat.getTrayNum() - useNum);
                int lockNum = 0;
                QueryWrapper<StoreInventory> wrapper = new QueryWrapper<>();
                wrapper.eq("st_id", storeSeat.getId());
                List<StoreInventory> storeInventories = inventoryMapper.selectList(wrapper);
                for (StoreInventory storeInventory : storeInventories) {
                    if (2 == storeInventory.getStatus()) {
                        seatInventoryVO.setStatus(2);
                        int laynum = (storeInventory.getLayNum() != null) ? storeInventory.getLayNum() : 0;
                        lockNum += laynum;
                    } else {
                        seatInventoryVO.setStatus(1);
                    }
                }
                seatInventoryVO.setLockNum(lockNum);
            } else {
                seatInventoryVO.setStatus(3);
                seatInventoryVO.setUseNum(0);
                seatInventoryVO.setUsableNum(storeSeat.getTrayNum());
            }
            list.add(seatInventoryVO);
        }
        return list;
    }

    @Override
    public void inOrOut(StoreInventory storeInventory, Integer modifyNumber, String modifyStNo, Integer usId) {
        if (null == storeInventory) {
            throw new ServiceException("库存不存在");
        }
        Integer originalNumber = storeInventory.getEtPdnum();
        StoreSeat seat = seatService.getOne(Wrappers.<StoreSeat>lambdaQuery().eq(StoreSeat::getStNo, modifyStNo));
        if (null != seat) {
            storeInventory.setStId(seat.getId());
            storeInventory.setStNo(seat.getStNo());
        }
        storeInventory.setEtPdnum(modifyNumber);

        this.saveOrUpdate(storeInventory);

        if (originalNumber > modifyNumber) {
            // 原始数量大于修改后数量，新增出库日志
            StoreOutlog storeOutlog = new StoreOutlog();
            storeOutlog.setStType(1);
            if (null != seat) {
                storeOutlog.setStId(seat.getId());
                storeOutlog.setStNo(seat.getStNo());
            }
            storeOutlog.setOperateType(3);
            storeOutlog.setCreateAt(LocalDateTime.now());
            storeOutlog.setEtPdnum(originalNumber - modifyNumber);
            storeOutlog.setEtId(storeInventory.getEtId());
            storeOutlog.setUsId(usId);
            storeOutlog.setStSize(storeInventory.getStSize());
            storeOutlog.setLayNum(storeInventory.getLayNum());
            storeOutlog.setDbId(storeInventory.getDbId());
            storeOutlog.setStModel(storeInventory.getStModel());
            outlogService.save(storeOutlog);
        } else if (originalNumber < modifyNumber) {
            // 原始数量小于于修改后数量，新增入库日志
            StoreInlog storeInlog = new StoreInlog();
            storeInlog.setStType(1);
            if (null != seat) {
                storeInlog.setStId(seat.getId());
                storeInlog.setStNo(seat.getStNo());
            }
            storeInlog.setOperateType(3);
            storeInlog.setCreateAt(LocalDateTime.now());
            storeInlog.setEtPdnum(modifyNumber - originalNumber);
            storeInlog.setEtId(storeInventory.getEtId());
            storeInlog.setUsId(usId);
            storeInlog.setStSize(storeInventory.getStSize());
            storeInlog.setLayNum(storeInventory.getLayNum());
            storeInlog.setDbId(storeInventory.getDbId());
            storeInlog.setStModel(storeInlog.getStModel());
            storeInlogService.save(storeInlog);
        }
    }

    @Override
    public Integer storeExcelExport() {
        List<StoreExcelExportVO> list = inventoryMapper.getExcelExportData();
        String templatePath = "model/store.xls";
        BufferedOutputStream bos = null;//导出到网页
        Map<String, Object> model = new HashMap<>();
        model.put("storeList", list);
        try {
            bos = ExportlUtil.getBufferedOutputStream("库存报表.xls", response);//返回前端处理
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//返回给前端处理
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }

    @Override
    public List<PhoneTrayCardVO> getByBfId(Integer bfId) {
        return inventoryMapper.getByBfId(bfId);
    }

    @Override
    public void outInventory(Integer sdId, Integer number) {
        log.info("数量出库：sdId:{},number:{}",sdId, number);
        if (number <= 0) {
            log.error("出库数量不能为0");
            return;
        }
        List<StoreInventory> inventoryList = this.getBySdId(sdId);
        if (inventoryList.isEmpty()) {
            log.error("工单id为{}的库存为空",sdId);
            return;
        }
        executeOut(inventoryList, number);
    }

    @Override
    public void inInventoryByNumber(InInventoryByNumberVO param) {
        log.info("数量入库: 参数{}",param.toString());
//        if (param.getNumber() <= 0) {
//            log.error("入库数量不能为0");
//            return;
//        }
        boolean hasTray = executeTraycardService.hasTray(param.getExId());
        if (hasTray) {
            log.info("执行单id{}存在打印托盘信息,不会以数量入库", param.getExId());
            return;
        }
        executeIn(param);
    }



    @Override
    public List<StoreInventory> getBySdId(Integer sdId) {
        List<Integer> etIds = new ArrayList<>();
        List<StoreInventory> result = new ArrayList<>();
        List<StoreInventory> inventoryList1 = new ArrayList<>();
        List<StoreInventory> inventoryList2 = new ArrayList<>();
        List<ExecuteInfo> executeInfoList = executeInfoService.list(Wrappers.<ExecuteInfo>lambdaQuery()
                .eq(ExecuteInfo::getSdId, sdId)
                .select(ExecuteInfo::getId));
        List<ExecuteBriefer> executeBrieferList = executeBrieferService.list(Wrappers.<ExecuteBriefer>lambdaQuery()
                .eq(ExecuteBriefer::getSdId, sdId)
                .select(ExecuteBriefer::getId));
        List<Integer> exInfoIds = executeInfoList.stream().map(ExecuteInfo::getId).collect(Collectors.toList());
        List<Integer> exBrieferIds = executeBrieferList.stream().map(ExecuteBriefer::getId).collect(Collectors.toList());
        if (exInfoIds.size() > 0) {
            List<ExecuteTraycard> traycardList = executeTraycardService.list(Wrappers.<ExecuteTraycard>lambdaQuery()
                    .in(ExecuteTraycard::getExId, exInfoIds).select(ExecuteTraycard::getId));
            etIds = traycardList.stream().map(ExecuteTraycard::getId).collect(Collectors.toList());
        }
        if (exInfoIds.isEmpty() && exBrieferIds.isEmpty()) {
            return new ArrayList<>();
        }
        if (!exBrieferIds.isEmpty()) {
            inventoryList1 = inventoryMapper.getByBrieferIds(exBrieferIds);
        }
        if (!etIds.isEmpty()) {
            inventoryList2 = inventoryMapper.getByEtIds(etIds);
        }
        result.addAll(inventoryList1);
        result.addAll(inventoryList2);
        result = result.stream().sorted(Comparator.comparing(StoreInventory::getCreateAt).reversed()).collect(Collectors.toList());
        return result;
    }

    @Override
    public void outStore(List<StoreInventory> outStoreList) {
        for (StoreInventory inventory : outStoreList) {
            outlogService.saveByInventory(inventory, inventory.getEtPdnum());
            if (inventory.getEtId() != null) {
                ExecuteTraycard executeTraycard = executeTraycardService.getById(inventory.getEtId());
                executeTraycard.setTyStatus(2);
                executeTraycardService.updateById(executeTraycard);
            }

        }
        List<Integer> collect = outStoreList.stream().map(StoreInventory::getId).collect(Collectors.toList());
        this.removeByIds(collect);
    }

    @Override
    public void outStoreSection(StoreInventory inventory, Integer outNumber) {
        outlogService.saveByInventory(inventory, outNumber);
        inventory.setEtPdnum(inventory.getEtPdnum() - outNumber);
        this.updateById(inventory);
    }

    /**
     * 操作库存，执行数量入库操作，存在入库则修改，不存在则新增
     * @param param 数量入库参数
     */
    private void executeIn(InInventoryByNumberVO param) {
        ExecuteBriefer briefer = executeBrieferService.getOne(Wrappers.<ExecuteBriefer>lambdaQuery()
                .eq(ExecuteBriefer::getExId, param.getExId())
                .select(ExecuteBriefer::getId));
        StoreInventory inventory = inventoryMapper.selectOne(Wrappers.<StoreInventory>lambdaQuery().eq(StoreInventory::getDbId, briefer.getId()).eq(StoreInventory::getStModel, 2));
        if (inventory == null) {
            inventory = new StoreInventory();
        }
        inventory.setStType(param.getStType());
        inventory.setLayNum(param.getLayNum());
        inventory.setCreateAt(LocalDateTime.now());
        inventory.setUpdateAt(LocalDateTime.now());
        inventory.setStatus(1);
        inventory.setEtPdnum(param.getNumber());
        inventory.setAreaId(param.getAreaId());
        inventory.setStModel(2);
        inventory.setDbId(briefer.getId());
        inventory.setStWay(2);
        this.saveOrUpdate(inventory);
        storeInlogService.saveByInventory(inventory);
    }

    /**
     * 操作库存表，执行出库操作
     * @param list 库存集合
     * @param number 出库数量
     */
    private void executeOut(List<StoreInventory> list, Integer number) {
        int sum = 0;
        int sectionNumber = 0;
        StoreInventory sectionStore = null;
        List<StoreInventory> outStoreList = new ArrayList<>();
        for (StoreInventory inventory : list) {
            sum += inventory.getEtPdnum();
            if (sum < number) {
                outStoreList.add(inventory);
            } else if (sum == number) {
                outStoreList.add(inventory);
                break;
            } else {
                sectionNumber = sum - number;
                sectionStore = inventory;
                break;
            }
        }
        outStore(outStoreList);
        if (null != sectionStore) {
            int i = sectionStore.getEtPdnum() - sectionNumber;
            outStoreSection(sectionStore, i);
        }
    }

}
