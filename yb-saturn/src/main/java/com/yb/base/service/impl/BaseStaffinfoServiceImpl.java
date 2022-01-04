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
package com.yb.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.mapper.BaseStaffinfoMapper;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.base.vo.BaseStaffinfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public IPage<BaseStaffinfoVO> selectBaseStaffinfoPage(IPage<BaseStaffinfoVO> page, BaseStaffinfoVO baseStaffinfo) {
        return page.setRecords(baseMapper.selectBaseStaffinfoPage(page, baseStaffinfo));
    }
    /*获取机长的部门
     * -1 biao shi mei you zhao dao
     */
    @Override
    public BaseStaffinfoVO getLeaderByLeaderId(String leaderId) {
        /*leader  dp_id*/

        return  staffinfoMapper.getLeaderByLeaderId(leaderId);
    }

    @Override
    public BaseStaffinfoVO getBaseStaffinfoByUsId(Integer usId) {
        return staffinfoMapper.getBaseStaffinfoByUsId(usId);
    }

    @Override
    public IPage<BaseStaffinfoVO> selectBaseStaffPage(IPage<BaseStaffinfoVO> page, BaseStaffinfoVO baseStaffinfoVO) {
        return page.setRecords(baseMapper.selectBaseStaffInfoAndBaseUser(page, baseStaffinfoVO));
    }

    @Override
    public BaseStaffinfoVO getOneStaffInfo(Integer id) {
        return staffinfoMapper.getOneStaffInfo(id);
    }

    @Override
    @Transactional
    public boolean updateStaffInfoIsUsedById(Integer[] ids) {
        int num = 0;
        try{
            for (int id : ids){
                int a = staffinfoMapper.updateStaffInfoIsUsedById(id);
                num += a;
            }
        }catch(RuntimeException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        if (num == ids.length){
            return true;
        }
        return false;
    }

    @Override
    public BaseStaffinfoVO getBaseStaffinfoVOByjobnum(String jobnum) {
        return staffinfoMapper.getBaseStaffinfoVOByjobnum(jobnum);
    }

    @Override
    public Integer getJobnumIsExit(String jobnum) {

        return staffinfoMapper.getJobnumIsExit(jobnum);
    }

    @Override
    public Integer getProcessesSum(String prId) {
        return staffinfoMapper.getProcessesSum(prId);
    }

    @Override
    public List<BaseStaffinfoVO> getUserListByPdId(BaseStaffinfoVO baseStaffinfoVO) {
        return staffinfoMapper.getUserListByPdId(baseStaffinfoVO);
    }

    @Override
    public Integer updateStaffInfoBcIdById(List<Integer> ids, Integer bcId) {
        Integer updateNum = 0;
        for (Integer id: ids) {
            updateNum += staffinfoMapper.updateStaffInfoBcIdById(id, bcId);
        }
        return updateNum;
    }

    @Override
    public String getNamesBybcId(Integer bcId){
        return staffinfoMapper.getNamesBybcId(bcId);
    }

    @Override
    public String getIdsBybcId(Integer bcId){
        return staffinfoMapper.getIdsBybcId(bcId);
    }

    @Override
    public List<BaseStaffinfoVO> getAllByBcId(Integer bcId) {
        return staffinfoMapper.getAllByBcId(bcId);
    }

    @Override
    public List<BaseStaffinfoVO> getInUserByBcId(Integer bcId) {
        return staffinfoMapper.getInUserByBcId(bcId);
    }

    @Override
    public List<BaseStaffinfoVO> getAllByWsId(Integer wsId) {
        return staffinfoMapper.getAllByWsId(wsId);
    }

    @Override
    public List<BaseStaffinfoVO> getStaffClassAllByWsId(Integer wsId) {
        return staffinfoMapper.getStaffClassAllByWsId(wsId);
    }

    @Override
    public boolean updateStaffInfoBcIdBybcId(List<Integer> bcids, Integer bcId) {
        Integer result = staffinfoMapper.updateStaffInfoBcIdBybcId(bcids, bcId);
        return result != null && result >= 1;
    }

    @Override
    public void updateStaffinfoDDid(String userid, String mobile) {
        staffinfoMapper.updateStaffinfoDDid(userid, mobile);
    }

    @Override
    public void updateFeiShuIdByPhone(String openId, String mobile) {
        staffinfoMapper.updateFeiShuIdByPhone(openId, mobile);
    }
}
