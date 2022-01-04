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

import com.anaysis.executSupervise.entity.SuperviseInterval;
import com.anaysis.executSupervise.entity.SuperviseShiftcount;
import com.anaysis.executSupervise.mapper.SuperviseShiftcountMapper;
import com.anaysis.executSupervise.service.SuperviseIntervalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备状态间隔表interval-视图 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseIntervalServiceImpl implements SuperviseIntervalService {

    @Autowired
    private SuperviseShiftcountMapper intervalMapper;

    @Override
    public SuperviseInterval getId(Integer id) {
        return intervalMapper.getId(id);
    }

    @Override
    public SuperviseInterval getLastState(String uuid){
        return intervalMapper.getLastState(uuid);
    }

    @Override
    public int save(SuperviseInterval intervalEntity){
          intervalMapper.save(intervalEntity);
          return intervalEntity.getId();
    }

    @Override
    public int update(SuperviseInterval intervalEntity){
        return intervalMapper.update(intervalEntity);
    }

    /***
     * 即时数据进行处理
     *
     * @param superviseBoxinfo
     */
    @Override
    public int setStatusLog (SuperviseInterval superviseBoxinfo){
        SuperviseInterval intervalEntity = new SuperviseInterval();
        return intervalMapper.update(intervalEntity);
    }

    @Override
    public int updateLast(SuperviseInterval interval){
        return intervalMapper.updateLast(interval);
    }

    @Override
    public int saveAlg(SuperviseShiftcount algintval){
        return intervalMapper.saveAlg(algintval);
    }

    @Override
    public SuperviseShiftcount getAlgid(Integer id){
        return intervalMapper.getIdAlg(id);
    }
    @Override
    public SuperviseShiftcount getLastStateLag(String uuid){
        return intervalMapper.getLastStateLag(uuid);
    }

    @Override
    public int updateLastAlg(SuperviseShiftcount algintval){
        return intervalMapper.updateLastAlg(algintval);
    }
}
