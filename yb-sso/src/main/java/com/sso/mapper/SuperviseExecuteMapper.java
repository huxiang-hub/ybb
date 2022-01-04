package com.sso.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.supervise.entity.SuperviseExecute;
import com.sso.supervise.vo.SuperviseExecuteVO;
import org.apache.ibatis.annotations.Mapper;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 设备清零日志表 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface SuperviseExecuteMapper extends BaseMapper<SuperviseExecute> {

    /**
     * 自定义分页
     *
     * @param page
     * @param superviseExecute
     * @return
     */
    List<SuperviseExecuteVO> selectSuperviseExecutePage(IPage page, SuperviseExecuteVO superviseExecute);

    /**
     * @param maId
     * @return
     */
    Integer getCurrNum(Integer maId);

    /***
     *
     * @param maId
     * @param sdId
     * @return
     */
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

    void updateStatusById(String exeStatus, Integer osId);

    SuperviseExecute getExecuteOrder(Integer maId);

    boolean updateStateToSupervise(@PathParam("maId") Integer maId, @PathParam("esId") Integer esId);

    boolean updateSuperviseExecuteBymMaId(@PathParam("maId")Integer maId, @PathParam("usIds")String usIds);

    List<SuperviseExecuteVO> findExecuteOrderStatus(Integer current, Integer size, String odName, String userName, String equipmentName);

    Integer executeOrderCount(String odName, String userName, String equipmentName);

    SuperviseExecuteVO getBeanByUUID(String uuid);
}
