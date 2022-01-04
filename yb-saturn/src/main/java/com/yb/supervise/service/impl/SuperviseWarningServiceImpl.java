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
package com.yb.supervise.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.supervise.entity.SuperviseWarning;
import com.yb.supervise.mapper.SuperviseWarningMapper;
import com.yb.supervise.service.ISuperviseWarningService;
import com.yb.supervise.vo.SuperviseWarningVO;
import org.springframework.stereotype.Service;

/**
 * 预警定义_yb_supervise_warning 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseWarningServiceImpl extends ServiceImpl<SuperviseWarningMapper, SuperviseWarning> implements ISuperviseWarningService {

    @Override
    public IPage<SuperviseWarningVO> selectSuperviseWarningPage(IPage<SuperviseWarningVO> page, SuperviseWarningVO superviseWarning) {
        return page.setRecords(baseMapper.selectSuperviseWarningPage(page, superviseWarning));
    }

}
