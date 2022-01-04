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
import com.yb.mater.entity.MaterProdlink;
import com.yb.mater.mapper.MaterProdlinkMapper;
import com.yb.mater.service.IMaterProdlinkService;
import com.yb.mater.vo.MaterProdlinkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品物料关系（materiel） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class MaterProdlinkServiceImpl extends ServiceImpl<MaterProdlinkMapper, MaterProdlink> implements IMaterProdlinkService {
    @Autowired
    private MaterProdlinkMapper materProdlinkMapper;

    @Override
    public IPage<MaterProdlinkVO> selectMaterProdlinkPage(IPage<MaterProdlinkVO> page, MaterProdlinkVO materProdlink) {
        return page.setRecords(baseMapper.selectMaterProdlinkPage(page, materProdlink));
    }

    @Override
    public List<MaterProdlinkVO> selectMaterProdlinkVOListByPdId(Integer pdId, Integer pdType) {
        return baseMapper.selectMaterProdlinkVOListByPdId(pdId, pdType);
    }

    @Override
    public List<MaterProdlinkVO> selectMaterProdlinkVOListById(Integer Id) {
        return baseMapper.selectMaterProdlinkVOListById(Id);
    }

    @Override
    public boolean removeByPdIdAndPdType(Integer pdId, Integer pdType) {
        Integer result = materProdlinkMapper.removeByPdIdAndPdType(pdId, pdType);
        return result != null && result >= 1;
    }
}
