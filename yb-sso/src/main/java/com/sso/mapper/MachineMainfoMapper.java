package com.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.panelapi.entity.MachineMainfo;
import com.sso.panelapi.vo.CompanyVO;
import com.sso.panelapi.vo.MachineMainfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    List<MachineMainfo> getMachins(@Param("name") String name);

    /**1
     * 更具部门Id 工序Id  查询对应的设备
     * @param dpId
     * @param prId
     * @param status
     * @return
     */
    List<MachineMainfoVO> getMachinsByDpIdPrId(Integer maId,Integer dpId,Integer prId,Integer status);


    List<MachineMainfoVO> getAllMachinePages(IPage page, @Param("mainfoVO") MachineMainfoVO mainfoVO);


    /**
     * 根据machineName 和machineID查询是否有这个设备
     * @param machineName
     * @param machineNo
     * @return
     */
    MachineMainfoVO getMachineInfoByCondition(String machineName, String machineNo);

    /**
     * 根据机器id 查询机器vo
     * @param maId
     * @return
     */
    List<MachineMainfoVO> getRateByMachineId(Integer maId,String prName);

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
