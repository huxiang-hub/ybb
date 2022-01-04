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
package com.yb.mater.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.mater.entity.MaterMtinfo;
import com.yb.mater.mapper.MaterMtinfoMapper;
import com.yb.mater.service.IMaterMtinfoService;
import com.yb.mater.vo.MaterMtinfoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 物料列表_yb_mater_matinfo 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class MaterMtinfoServiceImpl extends ServiceImpl<MaterMtinfoMapper, MaterMtinfo> implements IMaterMtinfoService {

    @Override
    public IPage<MaterMtinfoVO> selectMaterMtinfoPage(IPage<MaterMtinfoVO> page, MaterMtinfoVO materMtinfo) {
        return page.setRecords(baseMapper.selectMaterMtinfoPage(page, materMtinfo));
    }

    @Override
    @Transactional
    public boolean updatemtinfoIsdelById(Integer[] ids) {
        int num = 0;
        try{
            for (int id : ids){
                int a = baseMapper.updatemtinfoIsdelById(id);
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
    public List<MaterMtinfoVO> mtinfoByType(Integer mcId) {
        return baseMapper.mtinfoByType(mcId);
    }

}
