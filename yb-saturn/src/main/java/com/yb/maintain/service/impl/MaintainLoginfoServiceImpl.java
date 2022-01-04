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
package com.yb.maintain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.maintain.entity.MaintainLoginfo;
import com.yb.maintain.mapper.MaintainLoginfoMapper;
import com.yb.maintain.service.IMaintainLoginfoService;
import com.yb.maintain.vo.MaintainLoginfoVO;
import org.springframework.stereotype.Service;

/**
 * 保养日志_yb_maintain_loginfo 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class MaintainLoginfoServiceImpl extends ServiceImpl<MaintainLoginfoMapper, MaintainLoginfo> implements IMaintainLoginfoService {

    @Override
    public IPage<MaintainLoginfoVO> selectMaintainLoginfoPage(IPage<MaintainLoginfoVO> page, MaintainLoginfoVO maintainLoginfo) {
        return page.setRecords(baseMapper.selectMaintainLoginfoPage(page, maintainLoginfo));
    }

}
