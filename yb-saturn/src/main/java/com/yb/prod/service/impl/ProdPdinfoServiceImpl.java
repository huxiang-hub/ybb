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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.prod.entity.ProdClassify;
import com.yb.prod.entity.ProdPdinfo;
import com.yb.prod.mapper.ProdClassifyMapper;
import com.yb.prod.mapper.ProdPdinfoMapper;
import com.yb.prod.service.IProdPdinfoService;
import com.yb.prod.vo.ProdPdinfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品信息（product） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ProdPdinfoServiceImpl extends ServiceImpl<ProdPdinfoMapper, ProdPdinfo> implements IProdPdinfoService {
    @Autowired
    private ProdPdinfoMapper prodPdinfoMapper;
    @Autowired
    private ProdClassifyMapper prodClassifyMapper;

    @Override
    public IPage<ProdPdinfoVO> selectProdPdinfoPage(IPage<ProdPdinfoVO> page, ProdPdinfoVO prodPdinfo) {
        return page.setRecords(baseMapper.selectProdPdinfoPage(page, prodPdinfo));
    }

    @Override
    public ProdPdinfoVO getProdPdinfoVoBypdNo(String pbNo) {
        return prodPdinfoMapper.getProdPdinfoVoBypdNo(pbNo);
    }

    @Override
    public String addPridPdinfoByPcId(Integer pcId) {
        List<ProdPdinfo> prodPdinfos = prodPdinfoMapper.selectPridPdinfoByPcId(pcId);
        /*如果该类产品已有产品*/
        if(!prodPdinfos.isEmpty()){
            ProdPdinfo prodPdinfo = prodPdinfos.get(0);
            String pdNo = prodPdinfo.getPdNo();
            String firstSubstring = pdNo.substring(0, 2);
            String subPdNo = pdNo.substring(2, 6);
            /*String lastSubstring = pdNo.substring(6);*/
            Integer intPdNo = Integer.valueOf(subPdNo)+1;
            subPdNo = String.valueOf(intPdNo);
            pdNo = firstSubstring + subPdNo + "00" + "00" + "00" + "00";
            return pdNo;

        }
        /*如果该类还没有产品信息*/
        ProdClassify prodClassify = prodClassifyMapper.selectById(pcId);
        /*分类编号*/
        String classify = prodClassify.getClassify();
        String pdNo = classify + "0000" + "00" + "00" + "00" + "00";
        return pdNo;
    }

    @Override
    public String updatePridPdinfoById(String[] updateNames, Integer pdId) {
        ProdPdinfo prodPdin = prodPdinfoMapper.selectById(pdId);
        String pdNo = prodPdin.getPdNo();
        for(String updateName : updateNames){

            if(updateName.equals("结构")){
                pdNo = substring(pdNo, 6, 8);
            }
            if(updateName.equals("材料")){
                pdNo = substring(pdNo, 8, 10);
            }
            if(updateName.equals("工艺")){
                pdNo = substring(pdNo, 10, 12);
            }
            if(updateName.equals("文字图")){
                pdNo = substring(pdNo, 12, 14);
            }
        }

        return pdNo;
    }

    @Override
    public String substring(String pdNo, int first, int last) {
        String firstSubstring = pdNo.substring(0, first);
        String subPdNo = pdNo.substring(first, last);
        String lastSubstring = pdNo.substring(last);
        Integer intPdNo = Integer.valueOf(subPdNo)+1;
        subPdNo = String.valueOf(intPdNo);
        pdNo = firstSubstring + subPdNo + lastSubstring;
        return pdNo;
    }

    @Override
    public ProdPdinfoVO getProdPdinfoVoBypdId(Integer pdId) {
        return prodPdinfoMapper.getProdPdinfoVoBypdId(pdId);
    }


    @Override
    public ProdPdinfoVO getProdPdinfoByPdNo(String pdNo) {
        return prodPdinfoMapper.getProdPdinfoByPdNo(pdNo);
    }

    @Override
    public IPage<ProdPdinfoVO> selectePdinfoList(ProdPdinfoVO prodPdinfo, Integer current, Integer size) {
        List<ProdPdinfoVO> prodPdinfoVOS = prodPdinfoMapper.selectePdinfoList(prodPdinfo.getPdName()
                , prodPdinfo.getPdNo(), current, size);
        Integer total = prodPdinfoMapper.selectePdinfoCount(prodPdinfo.getPdName(), prodPdinfo.getPdNo());
        IPage page = new Page();
        page.setRecords(prodPdinfoVOS);
        page.setTotal(total);
        Integer pages = total / size;
        if(total % size != 0){
            pages++;
        }
        page.setPages(pages);
        return page;
    }

    @Override
    public ProdPdinfoVO selectPdInFoOne(Integer id) {
        ProdPdinfoVO prodPdinfoVO = prodPdinfoMapper.selectPdInFoOne(id);
        return prodPdinfoVO;
    }

    @Override
    public List<ProdClassify> selectClName() {

        return prodClassifyMapper.selectList(new QueryWrapper<ProdClassify>());
    }

    @Override
    public String getPdNo(String pd) {
        String headerNo = prodPdinfoMapper.getPdNo(pd);
        if(headerNo == null){
            return pd.substring(0,2)+"000100000000";
        }else {
            String hNo = headerNo.substring(0,2);
            String numNo = (Integer.parseInt(headerNo.substring(2,6))+1)+"";
            switch (numNo.trim().length()){
                case 1:
                    numNo = "000"+numNo;
                    break;
                case 2:
                    numNo = "00"+numNo;
                    break;
                case 3:
                    numNo = "0"+numNo;
                    break;
                case 4:
                    break;
            }
            return hNo+numNo+"00000000";
        }
    }
}
