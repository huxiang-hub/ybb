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
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.prod.entity.ProdProcelink;
import com.yb.prod.mapper.ProdPartsinfoMapper;
import com.yb.prod.mapper.ProdProcelinkMapper;
import com.yb.prod.service.IProdProcelinkService;
import com.yb.prod.util.PartsinfoUtil;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdProcelinkVO;
import com.yb.system.dict.mapper.SaDictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 产品对应工序关联表 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ProdProcelinkServiceImpl extends ServiceImpl<ProdProcelinkMapper, ProdProcelink> implements IProdProcelinkService {

    @Autowired
    private ProdProcelinkMapper prodProcelinkMapper;
    @Autowired
    private ProdPartsinfoMapper prodPartsinfoMapper;
    @Autowired
    private SaDictMapper saDictMapper;
    @Override
    public IPage<ProdProcelinkVO> selectProdProcelinkPage(IPage<ProdProcelinkVO> page, ProdProcelinkVO prodProcelink) {
        return page.setRecords(baseMapper.selectProdProcelinkPage(page, prodProcelink));
    }

    @Override
    public List<ProdProcelinkVO> list(Integer ptId) {
        return baseMapper.select(ptId);
    }
    /*查询部件所需工序*/
    @Override
    public List<ProdPartsinfoVo> rowSelectPr(Integer pdId, Integer pdType) {
        List<ProdPartsinfoVo> prodPart = prodPartsinfoMapper.listByPdId(pdId, pdType);//查询所有部件
        List<ProdPartsinfoVo> prodPartsinfoVos = PartsinfoUtil.listGetStree(prodPart);//转换为树
        for(ProdPartsinfoVo prodPartsinfoVo : prodPartsinfoVos){
            String ptTy = saDictMapper.getValue("part_type", prodPartsinfoVo.getPtType());
            List<ProdProcelinkVO> ProdProcelinkVOs = prodProcelinkMapper.select(prodPartsinfoVo.getId());
            if(!prodPartsinfoVo.getChildren().isEmpty()){
                List<ProdProcelinkVO> ProdProcelinkVOList = addProdProcelinkVO(prodPartsinfoVo, ProdProcelinkVOs);
                ProdProcelinkVOs.addAll(ProdProcelinkVOList);
            }
            prodPartsinfoVo.setProdProcelinkVOList(ProdProcelinkVOs);
            prodPartsinfoVo.setPtTy(ptTy);
        }
        return prodPartsinfoVos;
    }

    private List<ProdProcelinkVO> addProdProcelinkVO(ProdPartsinfoVo prodPartsinfoVo, List<ProdProcelinkVO> ProdProcelinkVOs) {
        Iterator<ProdPartsinfoVo> it = prodPartsinfoVo.getChildren().iterator();
        List<ProdPartsinfoVo> listChildren = new ArrayList<>();
        while (it.hasNext()){
            ProdPartsinfoVo partsinfoVo = it.next();
            List<ProdProcelinkVO> ProdProcelinkVOList = prodProcelinkMapper.select(partsinfoVo.getId());
            ProdProcelinkVOs.addAll(ProdProcelinkVOList);
            List<ProdPartsinfoVo> children = partsinfoVo.getChildren();
            if(!children.isEmpty() || !listChildren.isEmpty()){
                if(it.hasNext()){
                    listChildren.addAll(children);
                    continue;
                } else {
                    for(ProdPartsinfoVo partsinfo : listChildren){
                        addProdProcelinkVO(partsinfo, ProdProcelinkVOs);
                    }
                }
            }
        }
        return ProdProcelinkVOs;
    }

    /* 查询分类工序*/
    @Override
    public List<ProdProcelinkVO> rowSelectPd(Integer pdId, Integer pdType) {

        return prodProcelinkMapper.rowSelectPd(pdId, pdType);
    }

    @Override
    public boolean removeByPdIdAndPdType(Integer pdId, Integer pdType) {
        Integer result = prodProcelinkMapper.removeByPdIdAndPdType(pdId, pdType);
        return result != null && result >= 1;
    }

}
