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
import com.yb.base.entity.BaseClassinfo;
import com.yb.base.mapper.BaseClassinfoMapper;
import com.yb.base.service.IBaseClassinfoService;
import com.yb.base.vo.BaseClassinfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 人员班组临时调班_yb_base_staffclass 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class BaseClassinfoServiceImpl extends ServiceImpl<BaseClassinfoMapper, BaseClassinfo> implements IBaseClassinfoService {
    /**
     * 自定义分页
     *
     * @param page
     * @param baseClassinfoVO
     * @return
     */
    @Override
    public IPage<BaseClassinfoVO> selectBaseClassinfoPage(IPage<BaseClassinfoVO> page, BaseClassinfoVO baseClassinfoVO) {
        return page.setRecords(baseMapper.selectBaseClassinfoPage(page, baseClassinfoVO));
    }

    @Override
    public String getBcIdsBywsId(Integer wsId) {
        return baseMapper.getBcIdsBywsId(wsId);
    }
    /**
     * 更改班组班次信息
     */
    @Override
    public Integer setWsIdByids(Integer wsId, Integer[] bcIds) {
        return baseMapper.setWsIdByids(wsId, bcIds);
    }

    @Override
    public boolean setWsIdIsNull(Integer id) {
        return baseMapper.setWsIdIsNull(id) > 0;
    }

    @Override
    public List<BaseClassinfoVO> getAllLits() {
        return baseMapper.getAllLits();
    }

}
