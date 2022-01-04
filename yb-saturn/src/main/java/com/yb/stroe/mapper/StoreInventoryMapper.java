package com.yb.stroe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.execute.vo.ExecuteBrieferVO;
import com.yb.execute.vo.PhoneTrayCardVO;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.vo.SeatInventoryVO;
import com.yb.stroe.vo.StoreExcelExportVO;
import com.yb.stroe.vo.StoreInventorySemiVO;
import com.yb.stroe.vo.StoreInventoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author lzb
 * @date 2020-09-19
 */

@Mapper
public interface StoreInventoryMapper extends BaseMapper<StoreInventory> {

    List<SeatInventoryVO> seatInventoryInfo(Integer areaId);

    List<SeatInventoryVO> seatInventoryInfoBySort(Integer seatId);

    /**
     * 修改库位占用状态
     * @param etIdList 库位id集合
     * @param status 状态值
     */
    void updateStatus(@Param("etIdList") List<Integer> etIdList, @Param("status")Integer status);

    /**
     * 查询该库位剩余位置数
     * @param mpId 库位id
     * @return
     */
    Integer getUsableNum(@Param("mpId") Integer mpId);

    /**
     * 删除为使用的台账信息
     * @param etIdList
     */
    void deleteByEtIdList(@Param("etIdList") List<Integer> etIdList);

    /*分页查询生产订单数*/
    List<StoreInventorySemiVO> pageStoreFindList(Integer current, Integer size, @Param("request")StoreInventoryVO request);

    List<Integer> pageStoreFindListCount(@Param("request")StoreInventoryVO request);

    /**
     * 修改物料相关的台账状态
     * @param mlIdList 物料id集合
     * @param status 状态
     */
    void updateStatusBymlIdList(@Param("mlIdList") List<String> mlIdList,@Param("status") Integer status);

    List<StoreExcelExportVO> getExcelExportData();

    List<PhoneTrayCardVO> getByBfId(Integer bfId);

    List<StoreInventory> getByBrieferIds(List<Integer> exBrieferIds);

    List<StoreInventory> getByEtIds(List<Integer> etIds);
}
