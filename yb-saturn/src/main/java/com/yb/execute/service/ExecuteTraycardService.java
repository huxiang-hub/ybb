package com.yb.execute.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.vo.*;

import java.util.List;

public interface ExecuteTraycardService extends IService<ExecuteTraycard> {


    /**
     * 导出标识卡
     * @param etIdList
     */
    void construction(List<Integer> etIdList);

    /**
     * 删除未变红的
     * @param wfId
     * @param etIdList
     */
    void deleteListById(Integer wfId, List<Integer> etIdList);

    /**
     * 批量修改
     * @param traycardDeleteListVO
     * @return
     */
    Boolean updateTraycardList(TraycardDeleteListVO traycardDeleteListVO);

    /**
     * 查询被删除的托盘ID集合
     * @param etIdList
     * @param wfId
     * @return
     */

    List<Integer> selectNeEtIdList(List<Integer> etIdList, Integer wfId);

    /**
     * 根据当前排产单查询上工序托盘信息
     * @param wfIdList
     * @return
     */
    List<UpPrcessTraycardVO> getUpPrcessTraycardList(List<Integer> wfIdList);

    /**
     * 修改托盘的总台数
     * @param etIdList
     * @param totalNum
     */
    void updateTraycardTotalNumList(List<Integer> etIdList, int totalNum);

    /**
     * 查询打印标识卡所需数据
     * @param toIntList
     * @return
     */
    List<TraycardTextVO> getTraycardData(List<Integer> toIntList, Integer maId);

    /**
     * 修改打印次数
     * @param toIntList
     * @param maId
     * @param exId
     */
    void updatePrintNumList(List<Integer> toIntList, Integer maId, Integer exId);

    /**
     * 查询最大的id
     * @return
     */
    Integer getMaxId();

    /**
     * 查询改wfId下未打印过的标识卡
     * @param wfId
     * @return
     */
    List<Integer> getNoPrintEtIdList(Integer wfId);

    /**
     * 根据wfId查询托盘信息
     * @param wfId
     * @return
     */
    List<ExecuteTraycardVO> getExecuteTraycardByWfId(Integer wfId);

    IPage<ExecuteTraycardVO> getTraycardByTrayParam(IPage<ExecuteTraycardVO> page,ExecuteTraycardStoreVO trayParam);
    IPage<ExecuteTraycardVO> getTraycardByStoreinfo(IPage<ExecuteTraycardVO> page,ExecuteTraycardStoreVO trayParam);

    ExecuteTraycard selectByTdNo(String barCode);

    List<ExecuteTraycard> getByWfId(Integer sfId);

    String getTdNo();

    String getTdNo2();

//    void audits(String operation, Integer etId, Integer number, Integer seatId);

    /**
     * 扫码获取托盘流程单信息
     * @param tdNo
     * @return
     */
    FlowCardVO getFlowCard(String tdNo);

    String audits(Integer etId, Integer number, Integer seatId, Integer bfId, Integer exUserid);

    List<PhoneTrayCardVO> getTrayByBfId(Integer bfId);

    /**
     * 根据库位id查询库位托盘占用情况
     * @param seatId
     * @return
     */
    PhoneSeatTrayInfoVO getPhoneInfoBySeatId(Integer seatId);

    /**
     * 根据执行单exId判断是否存在托盘
     * @param exId 执行单id
     * @return
     */
    boolean hasTray(Integer exId);
}
