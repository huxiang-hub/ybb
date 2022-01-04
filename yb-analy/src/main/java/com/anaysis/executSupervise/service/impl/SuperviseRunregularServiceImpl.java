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
package com.anaysis.executSupervise.service.impl;

import com.anaysis.executSupervise.entity.SuperviseRunregular;
import com.anaysis.executSupervise.mapper.SuperviseRunregularMapper;
import com.anaysis.executSupervise.service.SuperviseRunregularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备状态间隔表interval-视图 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseRunregularServiceImpl implements SuperviseRunregularService {

    @Autowired
    private SuperviseRunregularMapper superviseRunregularMapper;

    @Override
    public SuperviseRunregular getByuuid(String uuid) {
        return superviseRunregularMapper.getByuuid(uuid);
    }

    @Override
    public int saveRunregular(SuperviseRunregular runregular){
        return superviseRunregularMapper.saveRunregular(runregular);
    }

    @Override
    public int updateRunregular(SuperviseRunregular runregular){
        return superviseRunregularMapper.updateRunregular(runregular);
    }
}
