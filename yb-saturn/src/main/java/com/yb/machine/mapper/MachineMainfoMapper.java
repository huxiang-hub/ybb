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
package com.yb.machine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.request.MonitorRequest;
import com.yb.machine.response.MachineMonitorVO;
import com.yb.machine.response.MonitorCapacityAnalyVO;
import com.yb.machine.vo.CompanyVO;
import com.yb.machine.vo.MachineMainfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.saturn.vo.MixboxVO;

import java.util.List;

/**
 * 设备_yb_mach_mainfo Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface MachineMainfoMapper extends BaseMapper<MachineMainfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param
     * @return
     */
    List<MachineMainfoVO> selectMachineMainfoPage(IPage page, Integer dpId, Integer prId, Integer status);

    /**
     * 获取设备基本信息
     * remark   yb_machine_mainfo
     * 预留传参 以防以后更新
     */
    MachineMainfoVO getMachineMainfo(Integer maId);

    /**
     * 获取设备基本信息
     * remark   yb_machine_mainfo
     * 预留传参 以防以后更新
     */
    MachineMainfoVO getMachineMainfoById(Integer id);

    /***
     *  修改设备信息
     * remark   yb_machine_mainfo
     *  预留传参 以防以后更新
     */
    boolean updateMachineMaInfo(MachineMainfo info);

    List<MachineMainfo> getMachins(@Param("name") String name);

    /**
     * 1
     * 更具部门Id 工序Id  查询对应的设备
     *
     * @param dpId
     * @param prId
     * @param status
     * @return
     */
    List<MachineMainfoVO> getMachinsByDpIdPrId(Integer maId, Integer dpId, Integer prId, Integer status);


    List<MachineMainfoVO> getAllMachinePages(IPage page, @Param("mainfoVO") MachineMainfoVO mainfoVO);


    /**
     * 根据machineName 和machineID查询是否有这个设备
     *
     * @param machineName
     * @param machineNo
     * @return
     */
    MachineMainfoVO getMachineInfoByCondition(String machineName, String machineNo);

    /**
     * 根据机器id 查询机器vo
     *
     * @param maId
     * @return
     */
    List<MachineMainfoVO> getRateByMachineId(Integer maId, String prName);

    /**
     * 根据部门ID 查询机器vo
     *
     * @param dpId
     * @return
     */
    List<MachineMainfoVO> getMachineByDpId(Integer dpId);

    /**
     * 通过设备查询当前公司信息
     *
     * @param maId
     * @return
     */

    CompanyVO getCompanyInfoByMaId(Integer maId);

    /**
     * 根据部门id和工序id查询所有设备信息
     *
     * @param prId
     * @param dpId
     * @return
     */
    List<MachineMainfo> machineListByDeptId(Integer prId, Integer dpId);

    List<MachineMainfo> findByMaType(@Param("maType") String maType);

    List<MachineMainfoVO> getMachineMainfoByType(@Param("maType") Integer maType);

    List<MachineMonitorVO> monitor(@Param("request") MonitorRequest request);

    List<MonitorCapacityAnalyVO> monitorCapacityAnaly(@Param("maId") Integer maId);

    List<MonitorCapacityAnalyVO> monitorCapacityAnalyByWsId(Integer wsId);

    /**
     * 设备管理界面,获取设备分类树
     * @param maType
     * @return
     */
    List<MachineMainfoVO> getMachineTree(@Param("maType") Integer maType);

    List<MachineMainfo> getListAll();
}
