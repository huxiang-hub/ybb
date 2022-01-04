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
package com.yb.prod.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.mater.service.IMaterProdlinkService;
import com.yb.prod.entity.ProdPdmodel;
import com.yb.prod.mapper.ProdPdmodelMapper;
import com.yb.prod.service.IProdPartsinfoService;
import com.yb.prod.service.IProdPdmodelService;
import com.yb.prod.service.IProdProcelinkService;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdPdmodelVO;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品模版信息（productmodel） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ProdPdmodelServiceImpl extends ServiceImpl<ProdPdmodelMapper, ProdPdmodel> implements IProdPdmodelService {
    @Autowired
    private ProdPdmodelMapper prodPdmodelMapper;
    @Autowired
    private IProdProcelinkService prodProcelinkService;
    @Autowired
    private IProdPartsinfoService prodPartsinfoService;
    @Autowired
    private IMaterProdlinkService materProdlinkService;
    @Override
    public IPage<ProdPdmodelVO> selectProdPdmodelPage(IPage<ProdPdmodelVO> page, ProdPdmodelVO prodPdmodel) {
        return page.setRecords(baseMapper.selectProdPdmodelPage(page, prodPdmodel));
    }

    @Override
    public List<ProdPdmodelVO> tree() {
        return baseMapper.tree();
    }

    @Override
    public ProdPdmodelVO getOneProdPdmodelById(Integer id) {
        return baseMapper.getOneProdPdmodelById(id);
    }

    @Override
    public ProdPdmodelVO getOneProdPdmodelByPdNo(String pdNo) {
        return baseMapper.getOneProdPdmodelByPdNo(pdNo);
    }

    @Override
    public IPage<ProdPdmodelVO> selectePdinfoModelList(IPage<ProdPdmodelVO> page, ProdPdmodelVO prodPdmodel) {
        return page.setRecords(prodPdmodelMapper.selectePdinfoModelList(page, prodPdmodel));
    }

    @Override
    public ProdPdmodelVO selectePdinfoModel(Integer id) {
        return prodPdmodelMapper.selectePdinfoModel(id);
    }

    //    传入模板id
    @Override
    public ProdPdmodelVO getOneByIdAll(Integer id, Integer dpType) {
        //        查询部件集合
        List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(id, dpType);
        return null;
    }

}
