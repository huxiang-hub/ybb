package com.sso.supervise.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.base.entity.BaseStaffinfo;
import com.sso.base.vo.BaseStaffinfoVO;
import com.sso.base.vo.StaffInfoVO;

import java.util.List;


/**
 * 人员表_yb_base_staffinfo 服务类
 */
public interface IBaseStaffinfoService extends IService<BaseStaffinfo> {
    /**
     * 获取人员信息 通过us_id
     */
     BaseStaffinfoVO getBaseStaffinfoByUsId(Integer usId);

    /**
     * 获取机台操作人的部门ID（添加同部门人的助手做准备）
     * */
     BaseStaffinfoVO getLeaderByLeaderId(String leadrId);

    /**
     * 机组人员信息
     * @param id
     * @return
     */
    List<StaffInfoVO> faceClassInfo(Integer id);
}
