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
package com.yb.prod.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.prod.entity.ProdPdmodel;
import com.yb.prod.vo.ProdPdmodelVO;
import com.yb.system.menu.vo.MenuVO;

import java.util.List;

/**
 * 产品模版信息（productmodel） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IProdPdmodelService extends IService<ProdPdmodel> {

    /**
     * 自定义分页
     *
     * @param page
     * @param prodPdmodel
     * @return
     */
    IPage<ProdPdmodelVO> selectProdPdmodelPage(IPage<ProdPdmodelVO> page, ProdPdmodelVO prodPdmodel);
    /**
     * 树形结构
     *
     * @return
     */
    List<ProdPdmodelVO> tree();
    /**
     * 查询单个模板详情通过id
     */
    ProdPdmodelVO getOneProdPdmodelById(Integer id);
    /**
     * 通过产品编号查询模板Id
     */
    ProdPdmodelVO getOneProdPdmodelByPdNo(String pdNo);

    /**
     * 查询所有产品模板信息
     * @param page
     * @param prodPdmodel
     * @return
     */
    IPage<ProdPdmodelVO> selectePdinfoModelList(IPage<ProdPdmodelVO> page, ProdPdmodelVO prodPdmodel);

    ProdPdmodelVO selectePdinfoModel(Integer id);

    ProdPdmodelVO getOneByIdAll(Integer id, Integer dpType);
}
