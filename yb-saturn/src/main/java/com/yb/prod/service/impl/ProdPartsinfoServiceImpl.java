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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.mapper.ProcessWorkinfoMapper;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.prod.entity.ProdPartsinfo;
import com.yb.prod.mapper.ProdPartsinfoMapper;
import com.yb.prod.mapper.ProdProcelinkMapper;
import com.yb.prod.service.IProdPartsinfoService;
import com.yb.prod.util.PartsinfoUtil;
import com.yb.prod.vo.ProdPartsinfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 产品信息（product） 服务实现类
 *ProdPartsinfoServiceImpl
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ProdPartsinfoServiceImpl extends ServiceImpl<ProdPartsinfoMapper, ProdPartsinfo> implements IProdPartsinfoService {
    @Autowired
    private  ProdPartsinfoMapper prodPartsinfoMapper;
    @Autowired
    private ProcessWorkinfoMapper processWorkinfoMapper;
    @Override
    public List<ProdPartsinfoVo> selectPtNames(Integer wbId) {
        /*根据批次id查询所有部件*/
        List<ProdPartsinfoVo> prodPartsinfoVos = prodPartsinfoMapper.selectPtNames(wbId);
        if(prodPartsinfoVos != null){
            List<Integer> reSize = new ArrayList();
            for (int i = 0; i < prodPartsinfoVos.size(); i++){
                int count = 0;
                for (int j = 0; j < prodPartsinfoVos.size(); j++){
                    if(!prodPartsinfoVos.get(i).getId().equals(prodPartsinfoVos.get(j).getPid())){
                        count += 1;
                    }
                }
                if(count != prodPartsinfoVos.size()){
                    reSize.add(i);//找到所有存在子部件的部件的下标
                }
            }
            for(int i = reSize.size() - 1; i >= 0; i-- ){
                int index = reSize.get(i);
                prodPartsinfoVos.remove(index);//干掉所有存在子部件的部件
            }
        }
        if(prodPartsinfoVos != null){
            Iterator<ProdPartsinfoVo> it = prodPartsinfoVos.iterator();
            while (it.hasNext()){//查询最子部件对应的工序
                ProdPartsinfoVo prodPartsinfoVo = it.next();
                List<ProcessWorkinfoVO> processWorkinfos = processWorkinfoMapper.selectByPtId(prodPartsinfoVo.getId());
                if(processWorkinfos.isEmpty()){//如果该部件下没有工序,那么就干掉这个部件(不展示)
                    it.remove();
                    continue;
                }
                prodPartsinfoVo.setPrNames(processWorkinfos);//设置工序信息
            }
        }
        return prodPartsinfoVos;
    }
    @Override
    public List<ProdPartsinfoVo> listByPdId(Integer pdId, Integer pdType) {
//        查询部件id
        return baseMapper.listByPdId(pdId, pdType);
    }

    @Override
    public boolean removeByPdIdAndPdType(Integer pdId, Integer pdType) {
        Integer result = baseMapper.removeByPdIdAndPdType(pdId, pdType);
        return result != null && result >= 1;
    }
}
