package com.yb.supervise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.vo.SuperviseExecuteVO;

import java.util.List;
import java.util.Map;

/**
 * @author by SUMMER
 * @date 2020/3/21.
 */
public interface ISuperviseExecuteService extends IService<SuperviseExecute> {

    IPage<SuperviseExecuteVO> selectSuperviseExecutePage(IPage<SuperviseExecuteVO> page, SuperviseExecuteVO superviseExecute);

    Integer getCurrNum(Integer maId);

    Integer getCurrNumByOderId(Integer maId, Integer sdId);

    /***
     * 更新生产实施表 把山生产准备改为生产保养 or 换膜准备
     * @param
     * @return
     */

    boolean updateStateToSuperviseExecute(SuperviseExecute execute);



    /**
     * @return
     */
    SuperviseExecute getExecuteStateByOdId(Integer sdId);

    SuperviseExecute getExecuteOrder(Integer maId);

    boolean updateSuperviseExecuteBymMaId(Integer maId, String usIds);

    IPage<SuperviseExecuteVO> findExecuteOrderStatus(Integer current, Integer size, SuperviseExecuteVO superviseExecuteVO);

    SuperviseExecuteVO getBeanByUUID(String uuid);

    /**
     * 删除对应设备id（ma_id）集合的数据
     */
    boolean removerListByMaid(List<Integer> maIds);

    Map<String, Integer> getStartNum(Integer maId);

}
