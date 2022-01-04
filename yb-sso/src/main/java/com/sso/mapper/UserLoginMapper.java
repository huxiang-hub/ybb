package com.sso.mapper;

import com.sso.base.vo.BaseStaffinfoVO;
import com.sso.base.vo.StaffInfoVO;
import com.sso.panelapi.vo.CompanyInfoVO;
import com.sso.supervise.entity.ExecuteState;
import com.sso.supervise.entity.SuperviseBoxinfo;
import com.sso.system.entity.SaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLoginMapper {

    /**
     * 用户登录（工号号密码登录）
     */
    public BaseStaffinfoVO loginByJobNum(SaUser saUser);

    /**
     * 用户登录（手机APP登录）
     */
    public BaseStaffinfoVO loginByPrintChat(Integer id);

    /**
     * 获取盒子的指定盒子的信息
     */
    public SuperviseBoxinfo getBoxInfoByMaId(Integer maId);

    /**
     * 登录人插入指定盒子us_ids*
     */
    public boolean upDataUsIds(String usIds, Integer maId);

    /**
     * 登录人设置为A1上班，A2下班,设置状态为'A' 设置开始时间，创建时间
     */
    public boolean saveUserEvent(ExecuteState state);

    /**
     * 获取厂区的租赁ID
     */
    public String getFactoryTenantId(String tenantId);

    /**
     * 人脸登录
     *
     * @param account 账号
     * @return
     */
    public BaseStaffinfoVO faceLogin(String account);

    StaffInfoVO getStaffInfo(@Param("id") Integer id);
}
