package com.sso.panelapi.service.impl;

import com.sso.base.vo.BaseStaffinfoVO;
import com.sso.mapper.UserLoginMapper;
import com.sso.panelapi.service.UserLoginService;
import com.sso.panelapi.vo.CompanyInfoVO;
import com.sso.supervise.entity.ExecuteState;
import com.sso.supervise.entity.SuperviseBoxinfo;
import com.sso.system.entity.SaUser;
import com.sso.utils.MD5Utils;
import org.springblade.core.tool.utils.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {


    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public BaseStaffinfoVO loginByJobNum(SaUser saUser) {
            /*
            * 逻辑处理，判断是工号密码等是否不符合
            md5加密算法加密在比较
            * */

        if (!"".equals(saUser.getPassword())) {
            //MD5加密密码
            //saUser.setPassword(MD5Utils.encodeByMD5(saUser.getPassword()));
            saUser.setPassword(DigestUtil.encrypt(saUser.getPassword()));
            return userLoginMapper.loginByJobNum(saUser);
        }
        return null;
    }

    @Override
    public BaseStaffinfoVO loginByPrintChat(Integer id) {
        return userLoginMapper.loginByPrintChat(id);
    }

    @Override
    public SuperviseBoxinfo getBoxInfoByMaId(Integer maId) {
        return userLoginMapper.getBoxInfoByMaId(maId);
    }


    @Override
    public boolean upDataUsIds(String usIds, Integer maId) {
        userLoginMapper.upDataUsIds(usIds, maId);
        return true;
    }

    @Override
    public boolean saveUserEvent(ExecuteState state) {
        return userLoginMapper.saveUserEvent(state);
    }


    @Override
    public String getFactoryTenantId(String tenantId) {

        return userLoginMapper.getFactoryTenantId(tenantId);
    }

}
