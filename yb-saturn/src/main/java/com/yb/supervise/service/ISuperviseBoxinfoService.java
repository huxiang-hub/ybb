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
package com.yb.supervise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.base.vo.BaseDeptinfoVO;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.vo.MachineAtPresentStatusVO;
import com.yb.supervise.vo.SuperviseBoxinfoVO;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 设备当前状态表boxinfo-视图 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface ISuperviseBoxinfoService extends IService<SuperviseBoxinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param superviseBoxinfo
     * @return
     */
    IPage<SuperviseBoxinfoVO> selectSuperviseBoxinfoPage(IPage<SuperviseBoxinfoVO> page, SuperviseBoxinfoVO superviseBoxinfo);

    int getBoxNum(String mId);

    String getMacUser(String mId);

    SuperviseBoxinfo getBoxInfoByBno(String uuid_s);

    SuperviseBoxinfo getBoxInfoByMid(Integer maId);

    List<SuperviseBoxinfoVO> selectSuperviseBoxinfoVO(Integer dpId, Integer prId);

    Integer equipmentNum(String status);

    List<SuperviseBoxinfoVO> getBoxListNotStop();

    /**
     * 查询每个车间下不同状态的设备数量
     * @return
     */
    List<BaseDeptinfoVO> selectDeptBoxinfoStatusList();

    /**
     * 查询车间下每个设备的生产状态
     * @param dpId
     * @return
     */
    List<ProcessWorkinfoVO> selectDeptBoxinfoSdList(Integer dpId);
    /**
     * 删除对应设备id（ma_id）集合的数据
     */
    boolean removerListByMaid(List<Integer>maIds);

    /**
     * 根据设备id查询设备当前状态
     * @param maId
     * @return
     */
    MachineAtPresentStatusVO getBoxinfoStatusByMaId(Integer maId);
}
