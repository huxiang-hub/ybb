package com.sso.panelapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.panelapi.entity.MachineMainfo;
import com.sso.panelapi.vo.CompanyVO;
import com.sso.panelapi.vo.MachineMainfoVO;

import java.util.List;

/**
 * 设备_yb_mach_mainfo 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IMachineMainfoService extends IService<MachineMainfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param
     * @return
     */
    IPage<MachineMainfoVO> selectMachineMainfoPage(IPage<MachineMainfoVO> page,
                                                   Integer dpId, Integer prId, Integer status);

    /**
     * 获取设备基本信息
     * remark   yb_machine_mainfo
     *  预留传参 以防以后更新
     */
    public MachineMainfoVO getMachineMainfo(Integer maId);
    /**
     * 获取设备基本信息
     * remark   yb_machine_mainfo
     *  预留传参 以防以后更新
     */
    public MachineMainfoVO getMachineMainfoById(Integer id);
    /***
     *  修改设备信息
     * remark   yb_machine_mainfo
     *  预留传参 以防以后更新
     */
    public boolean updateMachineMaInfo(MachineMainfo info);
    List<MachineMainfo> getMachins(String name);

    /**
     * 更具部门Id 工序Id  查询对应的设备
     * @param dpId
     * @param prId
     * @param status
     * @return
     */
    List<MachineMainfoVO> getMachinsByDpIdPrId(Integer maId,Integer dpId,Integer prId,Integer status);



    IPage<MachineMainfoVO> getAllMachinePages(IPage page, MachineMainfoVO mainfoVO);

    /**
     * 根据设备名称和设备no 查询是否有这个设备
     * @param machineName
     * @param machineNo
     * @return
     */
    MachineMainfoVO getMachineInfoByCondition(String machineName, String machineNo);

    /**
     * 通过设备id 拿到设备的工序和转速
     * @param maId
     * @return
     */
    List<MachineMainfoVO> getRateByMachineId(Integer maId,String prName);

    /*通过工序id查询设备名称*/
    List<MachineMainfoVO> selectMaNames(Integer prId);
    /**
     * 根据部门ID 查询机器vo
     * @param dpId
     * @return
     */
    List<MachineMainfoVO> getMachineByDpId(Integer dpId);

    /**
     * 通过设备查询当前公司信息
     * @param maId
     * @return
     */

   CompanyVO getCompanyInfoByMaId(Integer maId);

    /**
     * 根据部门id和工序id查询所有设备信息
     * @param prId
     * @param dpId
     * @return
     */
    List<MachineMainfo> machineListByDeptId(Integer prId, Integer dpId);
}
