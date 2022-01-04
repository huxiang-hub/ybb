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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.prod.entity.ProdClassify;
import com.yb.prod.entity.ProdPdinfo;
import com.yb.prod.vo.ProdPdinfoVO;

import java.util.List;


/**
 * 产品信息（product） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IProdPdinfoService extends IService<ProdPdinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param prodPdinfo
     * @return
     */
    IPage<ProdPdinfoVO> selectProdPdinfoPage(IPage<ProdPdinfoVO> page, ProdPdinfoVO prodPdinfo);
    /**
     * 通过pd_no去获得yb_prod_pdinfo对象
     */
    ProdPdinfoVO getProdPdinfoVoBypdNo(String pbNo);

    /**
     * 新增产品生成产品编号
     * @param pcId
     * @return
     */
    String addPridPdinfoByPcId(Integer pcId);

    /**
     * 修改产品的四个维度时修改产品编号
     * @param updateName
     * @return
     */
    String updatePridPdinfoById(String[] updateName, Integer pdId);
    /**
     * 通过pd_id获取yb_prod_pdinfo对象
     */
    ProdPdinfoVO getProdPdinfoVoBypdId(Integer pdId);

    /**
     * 分割拼接字符串
     * @param pdNo
     * @param first
     * @param last
     * @return
     */
    String substring(String pdNo, int first, int last);
    /**
     * 产品编号查询产品
     */
    ProdPdinfoVO getProdPdinfoByPdNo(String pdNo);
    /**
     * 查询所有的产品数
     * @return
     */
    IPage<ProdPdinfoVO> selectePdinfoList(ProdPdinfoVO prodPdinfo, Integer current, Integer size);

    ProdPdinfoVO selectPdInFoOne(Integer id);

    /**
     * 查询产品分类名
     * @return
     */
    List<ProdClassify> selectClName();
    /**
     * 获取产品编号
     */
    String getPdNo(String pd);
}
