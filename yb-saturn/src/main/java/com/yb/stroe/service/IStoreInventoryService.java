package com.yb.stroe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.vo.PhoneTrayCardVO;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.vo.InInventoryByNumberVO;
import com.yb.stroe.vo.SeatInventoryVO;
import com.yb.stroe.vo.StoreInventorySemiVO;
import com.yb.stroe.vo.StoreInventoryVO;

import java.util.List;
import java.util.Map;

/**
* @author lzb
* @date 2020-09-19
*/


public interface IStoreInventoryService extends IService<StoreInventory> {

    boolean putStore(Integer stId, Integer trayId, Integer stType, String etIds);

    boolean outStore(String trayIds, Integer usId);

    void check(Integer trayId, Integer currentNum, Integer currentSeatId, String currentLocal);

    List<SeatInventoryVO> seatInventoryInfo(Integer areaId);

    List<SeatInventoryVO> seatInventoryInfo1(Integer areaId);

    List<SeatInventoryVO> seatInventoryInfoBySort(Integer seatId);

    void deleteBatchByetIds(List<Integer> etIds);

    /**
     * 修改库位占用状态
     * @param etIdList 库位id集合
     * @param status 状态值
     */
    void updateStatus(List<Integer> etIdList, Integer status);

    /**
     * 查询该库位剩余位置数
     * @param mpId 库位id
     * @return
     */
    Integer getUsableNum(Integer mpId);

    /**
     * 删除为使用的台账信息
     * @param etIdList
     */
    void deleteByEtIdList(List<Integer> etIdList);

    IPage<StoreInventorySemiVO> pageStoreFindList(Integer current, Integer size, StoreInventoryVO storeInventoryVO);

    /**
     * 修改物料相关的台账状态
     * @param mlIdList 物料id集合
     * @param status 状态
     */
    void updateStatusBymlIdList(List<String> mlIdList, Integer status);

    /**
     * 根据数量直接上报入库
     * @param executeBriefer 上报实体对象
     */
    void putStoreByNumber(ExecuteBriefer executeBriefer);

    /**
     * 根据传入数量或库位修改库存和出入库
     * @param storeInventory
     * @param modifyNumber
     * @param modifyStNo
     */
    void inOrOut(StoreInventory storeInventory, Integer modifyNumber, String modifyStNo, Integer usId);

    Integer storeExcelExport();

    List<PhoneTrayCardVO> getByBfId(Integer bfId);

    /**
     * 根据工单按时间顺序出库
     * @param sdId 工单id
     * @param number 出库数量
     */
    void outInventory(Integer sdId, Integer number);

    /**
     * 根据仓库台账集合全部出库
     * @param outStoreList 全部出库的台账记录
     */
    void outStore(List<StoreInventory> outStoreList);

    /**
     * 根据仓库台账部分出库
     * @param inventory 待出库记录
     * @param outNumber 出库数量
     */
    void outStoreSection(StoreInventory inventory, Integer outNumber);

    /**
     * 根据数量入库
     */
    void inInventoryByNumber(InInventoryByNumberVO parames);

    /**
     * 根据工单id获取库存记录
     * @param sdId 工单id
     * @return
     */
    List<StoreInventory> getBySdId(Integer sdId);
}
