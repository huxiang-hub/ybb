package com.yb.execute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.vo.*;
import com.yb.workbatch.vo.ArticlesTraycardVO;
import com.yb.workbatch.vo.ConstructionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExecuteTraycardMapper extends BaseMapper<ExecuteTraycard> {
    /**
     *导出标识卡
     * @param etIdList
     * @return
     */
    List<ConstructionVO> construction(@Param("etIdList") List<Integer> etIdList);

    /**
     * 删除未变红的
     * @param wfId
     * @param etIdList
     */
    void deleteListById(@Param("wfId") Integer wfId,
                        @Param("etIdList") List<Integer> etIdList);

    /**
     * 批量性
     * @param mpId
     * @param remark
     * @param storePlace
     * @param etIdList
     * @return
     */
    Integer updateTraycardList(Integer mpId, String remark, String storePlace, Integer trayNum, Integer usId, @Param("etIdList") List<Integer> etIdList);

    /**
     * 查询被删除的托盘ID集合
     * @param etIdList
     * @param wfId
     * @return
     */
    List<Integer> selectNeEtIdList(@Param("etIdList")List<Integer> etIdList,
                                   @Param("wfId") Integer wfId);

    /**
     * 修改托盘的总台数
     * @param etIdList
     * @param totalNum
     */
    void updateTraycardTotalNumList(@Param("etIdList") List<Integer> etIdList,@Param("totalNum") int totalNum);

    /**
     * 批量修改打印次数
     * @param etIdList
     */
    void updatePrintNumList(@Param("etIdList") List<Integer> etIdList,
                            @Param("maId")Integer maId,
                            @Param("exId")Integer exId);

    /**
     * 查询打印标识卡所需数据
     * @param etIdList
     * @return
     */
    List<TraycardDataVO> getTraycardData(@Param("etIdList") List<Integer> etIdList,
                                         @Param("maId")Integer maId);

    /**
     * 查询最大id
     * @return
     */
    Integer getMaxId();

    /**
     * 查询改wfId下未打印过的标识卡
     * @param wfId
     * @return
     */
    List<Integer> getNoPrintEtIdList(@Param("wfId") Integer wfId);


    /**
     * 根据wfId查询托盘信息
     * @param wfId
     * @return
     */
    List<ExecuteTraycardVO> getExecuteTraycardByWfId(Integer wfId);

    /**
     * 查询托盘信息
     * @param barCode
     * @return
     */
    @Select("SELECT a.*, c.pr_name FROM yb_execute_traycard a LEFT JOIN yb_workbatch_shift b ON a.wf_id = b.id AND a.td_no = #{barCode} JOIN yb_workbatch_ordlink c ON b.sd_id = c.id")
    ExecuteTraycard selectByTdNo(String barCode);

    @Select("SELECT a.*, c.pr_name FROM yb_execute_traycard a LEFT JOIN yb_workbatch_shift b ON a.wf_id = b.id AND a.wf_id = #{wfId} JOIN yb_workbatch_ordlink c ON b.sd_id = c.id")
    List<ExecuteTraycard> getByWfId(Integer wfId);

    /**
     * 根据wfId查询托盘及上料相关信息
     * @param wfId 排产工单id
     * @return
     */
    List<TraycardMaterialsVO> getTraycardMaterials(@Param("wfId") Integer wfId);

    @Select("select MAX(td_no) from yb_execute_traycard order by create_at desc limit 1")
    String getMaxTdNo();


    List<ExecuteTraycardVO> getTraycardByTrayParam(IPage<ExecuteTraycardVO> page,@Param("trayParam") ExecuteTraycardStoreVO trayParam);
    List<ExecuteTraycardVO> getTraycardByStoreinfo(IPage<ExecuteTraycardVO> page,@Param("trayParam") ExecuteTraycardStoreVO trayParam);
    /**
     * 查询该产品所有的工序流程托盘相关信息
     * @param wbNo 工单编号
     * @param pdId 产品id
     * @return
     */
    List<TrayCardSumVO> processFlow(@Param("wbNo") String wbNo, @Param("pdId") Integer pdId);

    List<PhoneTrayCardVO> getByBfId(Integer bfId);

    List<PhoneSeatTrayInfoRecordVO> getPhoneInfoBySeatId(Integer seatId);
}
