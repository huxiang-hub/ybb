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

import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.mapper.SaTenantMapper;
import com.anaysis.executSupervise.mapper.SuperviseBoxinfoMapper;
import com.anaysis.executSupervise.service.SuperviseBoxinfoService;
import com.anaysis.executSupervise.vo.SuperviseBoxinfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备当前状态表boxinfo-视图 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseBoxinfoServiceImpl implements SuperviseBoxinfoService {

    @Autowired
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;
    @Autowired
    private SaTenantMapper saTenantMapper;

    @Override
    public SuperviseBoxinfo getBoxInfoByBno(String uuid) {
        return superviseBoxinfoMapper.getBoxInfoByBno(uuid);
    }

    @Override
    public int saveOrUpdate(SuperviseBoxinfo boxInfoEntity) {
        if (boxInfoEntity.getId() == null) {
            return superviseBoxinfoMapper.save(boxInfoEntity);
        } else {
            //获取历史数据后进行速度计算信息
            return superviseBoxinfoMapper.update(boxInfoEntity);
        }
    }

    @Override
    public List<SuperviseBoxinfo> getList() {
        return superviseBoxinfoMapper.getList();
    }

    @Override
    public List<SuperviseBoxinfoVo> getListByrun() {
        return superviseBoxinfoMapper.getListByrun();
    }

    @Override
    public SuperviseBoxinfoVo getBoxVoByuuid(String uuid) {
        return superviseBoxinfoMapper.getBoxVoByuuid(uuid);
    }

    @Override
    public String getTenantIdByUuid(String uuid) {
        return saTenantMapper.getTenantIdByUuid(uuid);
    }
}
