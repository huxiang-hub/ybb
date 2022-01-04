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
import com.yb.base.entity.BaseStaffclass;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.mapper.BaseStaffclassMapper;
import com.yb.base.mapper.BaseStaffinfoMapper;
import com.yb.base.service.IBaseStaffclassService;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.base.vo.BaseStaffclassVO;
import com.yb.base.vo.BaseStaffinfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 人员班组临时调班_yb_base_staffclass 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class BaseStaffclassServiceImpl extends ServiceImpl<BaseStaffclassMapper, BaseStaffclass> implements IBaseStaffclassService {
    /**
     * 自定义分页
     *
     * @param page
     * @param baseStaffclassVO
     * @return
     */
    @Override
    public IPage<BaseStaffclassVO> selectBaseStaffclassPage(IPage<BaseStaffclassVO> page, BaseStaffclassVO baseStaffclassVO) {
        return  page.setRecords(baseMapper.selectBaseStaffclassPage(page, baseStaffclassVO));
    }
    /**
     * 查询调出去的人员信息
     */
    @Override
    public BaseStaffclassVO getGoOutUser(Integer userId) {
        return baseMapper.getGoOutUser(userId);
    }

    @Override
    public boolean updateIsUsedByIds(List<Integer> ids) {
        Integer result = baseMapper.updateIsUsedByIds(ids);
        return result != null && result >= 1;
    }

    @Override
    public boolean updataByisused() {
        Integer result = baseMapper.updataByisused();
        return result != null && result >= 1;
    }

    @Override
    public boolean updateIsUsedBybcIds(List<Integer> bcids) {
        Integer result = baseMapper.updateIsUsedBybcIds(bcids);
        return result != null && result >= 1;
    }

}
