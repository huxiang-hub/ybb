/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.supervise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.panelapi.vo.MachineStopVO;
import com.yb.statis.request.DeviceCapacityProgressRequest;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.vo.SuperviseExecuteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

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

    /**
     * @param request
     * @param maIds
     * @return
     */
    Integer getNum(@Param("request") DeviceCapacityProgressRequest request, @Param("maIds") List<Integer> maIds);

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

    SuperviseExecute getExecuteOrder(@Param("maId") Integer maId);

    Integer getExecuteOrderByother(@Param("maId") Integer maId);

    boolean updateStateToSupervise(@PathParam("maId") Integer maId, @PathParam("esId") Integer esId);

    boolean updateSuperviseExecuteBymMaId(@PathParam("maId") Integer maId, @PathParam("usIds") String usIds);

    List<SuperviseExecuteVO> findExecuteOrderStatus(Integer current, Integer size, String odName, String userName, String equipmentName);

    Integer executeOrderCount(String odName, String userName, String equipmentName);

    SuperviseExecuteVO getBeanByUUID(String uuid);

    /**
     * 删除对应设备id（ma_id）集合的数据
     */
    Integer removerListByMaid(@Param("list") List<Integer> maIds);

    /**
     * 获取处于执行状态的设备
     *
     * @return
     */
    List<SuperviseExecute> findByExeStatus();

    Map<String, Object> findMainfoBywfId(Integer wfId);

    SuperviseExecute getByMaIdAndSdId(@Param("maId") Integer maId, @Param("sdId") Integer sdId);


    /**
     * 根据设备id获取当前设备的工单客户名称和工单编号
     *
     * @param maId
     * @return
     */
    MachineStopVO getByMaId(@Param("maId") Integer maId);


    Map<String, Integer> getStartNum(@Param("maId") Integer maId);

    void update(SuperviseExecute currExecute);

}
