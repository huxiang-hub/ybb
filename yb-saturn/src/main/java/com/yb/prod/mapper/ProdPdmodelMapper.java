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
package com.yb.prod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.prod.entity.ProdPdmodel;
import com.yb.prod.vo.ProdPdmodelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品模版信息（productmodel） Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ProdPdmodelMapper extends BaseMapper<ProdPdmodel> {

    /**
     * 自定义分页
     *
     * @param page
     * @param prodPdmodel
     * @return
     */
    List<ProdPdmodelVO> selectProdPdmodelPage(IPage page, ProdPdmodelVO prodPdmodel);
    /**
     * 树形结构
     */
    List<ProdPdmodelVO> tree();
    /**
     * 查询单个模板详情
     */
    ProdPdmodelVO getOneProdPdmodelById(@Param("id")Integer id);
    /**
     * 用产品编号查询
     */
    ProdPdmodelVO getOneProdPdmodelByPdNo(@Param("pdNo") String pdNo);

    /**
     * 查询所有产品模板信息
     * @param page
     * @param prodPdmodel
     * @return
     */
    List<ProdPdmodelVO> selectePdinfoModelList(IPage<ProdPdmodelVO> page, ProdPdmodelVO prodPdmodel);

    /**根据ID查询产品模板信息
     *
     * @param id
     * @return
     */
    ProdPdmodelVO selectePdinfoModel(Integer id);
}
