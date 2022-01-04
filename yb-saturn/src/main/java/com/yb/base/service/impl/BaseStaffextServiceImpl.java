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
import com.yb.base.entity.BaseStaffext;
import com.yb.base.mapper.BaseStaffextMapper;
import com.yb.base.service.IBaseStaffextService;
import com.yb.base.vo.BaseStaffextVO;
import org.springframework.stereotype.Service;

/**
 * 基础信息表_yb_base_staffext 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class BaseStaffextServiceImpl extends ServiceImpl<BaseStaffextMapper, BaseStaffext> implements IBaseStaffextService {

    @Override
    public IPage<BaseStaffextVO> selectBaseStaffextPage(IPage<BaseStaffextVO> page, BaseStaffextVO baseStaffext) {
        return page.setRecords(baseMapper.selectBaseStaffextPage(page, baseStaffext));
    }

    /**
     * 身份证号是否存在
     */
    @Override
    public Integer getIdcardIsExit(String Idcard) {
        return baseMapper.getIdcardIsExit(Idcard);
    }

}
