package com.sso.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.base.entity.BaseStaffinfo;
import com.sso.base.vo.BaseStaffinfoVO;
import com.sso.base.vo.StaffInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人员表_yb_base_staffinfo Mapper 接口
 *
 * @author my
 * @since 2020-05-28
 */
@Mapper
public interface BaseStaffinfoMapper extends BaseMapper<BaseStaffinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param baseStaffinfo
     * @return
     */
    List<BaseStaffinfoVO> selectBaseStaffinfoPage(IPage page, BaseStaffinfoVO baseStaffinfo);

    /**
     * 获取机台操作人的部门ID（添加同部门人的助手做准备）
     */
    public BaseStaffinfoVO getLeaderByLeaderId(String leadrId);

    /**
     * 获取人员信息 通过us_id
     */
    public BaseStaffinfoVO getBaseStaffinfoByUsId(Integer usId);

    /**
     * 获取所有员工信息
     *
     * @return
     */
    List<BaseStaffinfoVO> selectBaseStaffInfoAndBaseUser(IPage page, @Param("baseStaffinfoVO") BaseStaffinfoVO baseStaffinfoVO);

    /**
     * 获取单个用户信息
     */
    public BaseStaffinfoVO getOneStaffInfo(Integer id);

    /**
     * 修改为停用
     */
    public Integer updateStaffInfoIsUsedById(Integer id);

    /**
     * 查询工号是否重复
     */
    public BaseStaffinfoVO getBaseStaffinfoVOByjobnum(@Param("jobnum") String jobnum);

    /**
     * 查询工号是否重复
     */
    public Integer getJobnumIsExit(@Param("jobnum") String jobnum);

    /**
     * 查询每个工序总共的人数
     *
     * @return
     */
    Integer getProcessesSum(String prId);

    /**
     * 车间查询人员
     */
    List<BaseStaffinfoVO> getUserListByPdId(@Param("baseStaffinfoVO") BaseStaffinfoVO baseStaffinfoVO);

    /**
     * 修改班组
     */
    public Integer updateStaffInfoBcIdById(Integer id, Integer bcId);

    /**
     * 查询班组内人员
     */
    String getNamesBybcId(Integer bcId);

    /**
     * 查询班组内人员
     */
    String getIdsBybcId(Integer bcId);


    List<StaffInfoVO> factManInfo(@Param("staffInfo") StaffInfoVO staffInfo);
}
