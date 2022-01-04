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


import com.yb.supervise.entity.SuperviseExerun;
import com.yb.supervise.mapper.SuperviseExerunMapper;
import com.yb.supervise.service.SuperviseExerunService;
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
public class SuperviseExerunServiceImpl implements SuperviseExerunService {

    @Autowired
    private SuperviseExerunMapper exerunMapper;


    @Override
    public SuperviseExerun getByuuid(String uuid) {
      return  exerunMapper.getByuuid(uuid);
    }

    /***
     * 返回清理以后的已经记录数据信息，进行数据跟进管理
     * @return
     */
    @Override
    public List<SuperviseExerun> getHisexerun() {
        return  exerunMapper.getHisexerun();
    }

    @Override
    public int clearByalreadylog(){ return exerunMapper.clearByalreadylog();}

    @Override
    public int clearByuuid(String uuid){
        return  exerunMapper.clearByuuid(uuid);
    }
    @Override
    public int insertByuuid(SuperviseExerun exerun){
        return exerunMapper.insertByuuid(exerun);
    }
    @Override
    public int updateByuuid(SuperviseExerun exerun){
        return exerunMapper.updateByuuid(exerun);
    }
}
