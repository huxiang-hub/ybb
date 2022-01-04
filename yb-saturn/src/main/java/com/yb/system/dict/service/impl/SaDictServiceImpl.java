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
package com.yb.system.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.system.dict.entity.Dict;
import com.yb.system.dict.mapper.SaDictMapper;
import com.yb.system.dict.service.SaIDictService;
import com.yb.system.dict.vo.DictVO;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springblade.common.cache.CacheNames.DICT_LIST;
import static org.springblade.common.cache.CacheNames.DICT_VALUE;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class SaDictServiceImpl extends ServiceImpl<SaDictMapper, Dict> implements SaIDictService {

    @Override
    public IPage<DictVO> selectDictPage(IPage<DictVO> page, DictVO dict) {
        return page.setRecords(baseMapper.selectDictPage(page, dict));
    }

    @Override
    public List<DictVO> tree() {
        return ForestNodeMerger.merge(baseMapper.tree());
    }

    @Override
    @Cacheable(cacheNames = DICT_VALUE, key = "#code+'_'+#dictKey")
    public String getValue(String code, Integer dictKey) {
        return Func.toStr(baseMapper.getValue(code, dictKey), StringPool.EMPTY);
    }

    @Override
    @Cacheable(cacheNames = DICT_LIST, key = "#code")
    public List<Dict> getList(String code) {
        return baseMapper.getList(code);//增加排序功能
    }

    @Override
    @CacheEvict(cacheNames = {DICT_LIST, DICT_VALUE}, allEntries = true)
    public boolean submit(Dict dict) {
        LambdaQueryWrapper<Dict> lqw = Wrappers.<Dict>query().lambda().eq(Dict::getCode, dict.getCode()).eq(Dict::getDictKey, dict.getDictKey());
        Integer cnt = baseMapper.selectCount((Func.isEmpty(dict.getId())) ? lqw : lqw.notIn(Dict::getId, dict.getId()));
        if (cnt > 0) {
            throw new ApiException("当前字典键值已存在!");
        }
        return saveOrUpdate(dict);
    }
    @Override
    public R<String> getDictVal(String code, Integer dictKey){
        return R.data(Func.toStr(baseMapper.getValue(code, dictKey), StringPool.EMPTY));
    }
}
