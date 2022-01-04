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
package com.sso.supervise.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.base.entity.BaseStaffinfo;
import com.sso.base.vo.BaseStaffinfoVO;
import com.sso.base.vo.StaffInfoVO;
import com.sso.mapper.BaseStaffinfoMapper;
import com.sso.mapper.UserLoginMapper;
import com.sso.supervise.service.IBaseStaffinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 人员表_yb_base_staffinfo 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class BaseStaffinfoServiceImpl extends ServiceImpl<BaseStaffinfoMapper, BaseStaffinfo> implements IBaseStaffinfoService {

    @Autowired
    private BaseStaffinfoMapper staffinfoMapper;

    @Autowired
    private UserLoginMapper userLoginMapper;


    @Override
    public BaseStaffinfoVO getBaseStaffinfoByUsId(Integer usId) {
        return staffinfoMapper.getBaseStaffinfoByUsId(usId);
    }

    /*获取机长的部门
     * -1 biao shi mei you zhao dao
     */
    @Override
    public BaseStaffinfoVO getLeaderByLeaderId(String leaderId) {

        return staffinfoMapper.getLeaderByLeaderId(leaderId);
    }

    @Override
    public List<StaffInfoVO> faceClassInfo(Integer id) {
        StaffInfoVO staffInfo = userLoginMapper.getStaffInfo(id);
        //获取班组人员信息
        List<StaffInfoVO> staffInfoVOS = staffinfoMapper.factManInfo(staffInfo);
        staffInfoVOS.add(staffInfo);
        return staffInfoVOS;

    }
}
