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
import com.yb.prod.entity.ProdPdinfo;
import com.yb.prod.vo.ProdPdinfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品信息（product） Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ProdPdinfoMapper extends BaseMapper<ProdPdinfo> {



    /**
     * 自定义分页
     *
     * @param page
     * @param prodPdinfo
     * @return
     */
    List<ProdPdinfoVO> selectProdPdinfoPage(IPage page, ProdPdinfoVO prodPdinfo);

    /**
     * 根据id 去查询vo
     * @param pbNo
     * @return
     */
    ProdPdinfoVO getProdPdinfoVoBypdNo(String pbNo);

    /**
     * 根据id 去查询vo
     * @param pdId
     * @return
     */
    ProdPdinfoVO getProdPdinfoVoBypdId(Integer pdId);


    List<ProdPdinfo> selectPridPdinfoByPcId(Integer pcId);

    ProdPdinfoVO getProdPdinfoByPdNo(String pdNo);

    /**
     * 根据订单批次id查询产品
     * @param wbId
     * @return
     */
    ProdPdinfoVO getProdPdinfoVOByWbId(Integer wbId);

    /**
     * 查询所有的产品数
     * @return
     */
    List<ProdPdinfoVO> selectePdinfoList(String pdName, String pdNo, Integer current, Integer size);

    Integer selectePdinfoCount(String pdName, String pdNo);
    /**
     * 查询产品和产品分类名
     */
    ProdPdinfoVO selectPdInFoOne(Integer id);
    /**
     * 获取产品编号
     */
    String getPdNo(@Param("pd") String pd);
}
