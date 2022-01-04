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
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.prod.entity.ProdProcelink;
import com.yb.prod.vo.ProdProcelinkVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 产品对应工序关联表 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ProdProcelinkMapper extends BaseMapper<ProdProcelink> {

    /**
     * 自定义分页
     *
     * @param page
     * @param prodProcelink
     * @return
     */
    List<ProdProcelinkVO> selectProdProcelinkPage(IPage page, ProdProcelinkVO prodProcelink);

    /*查询部件所需工序*/
    List<ProdProcelinkVO> selectOrderProcelink(Integer pdId);

    /*查询部件所需工序*/
    List<ProdProcelinkVO> select(Integer ptId);

    /**
     * 查询分类工序
     *
     * @param pdId
     * @return
     */
    List<ProdProcelinkVO> rowSelectPd(Integer pdId, Integer pdType);

    /**
     * 删除产品对应部件对应的工序
     */
    Integer removeByPdIdAndPdType(@Param("pdId") Integer pdId, @Param("pdType") Integer pdType);

    ProdProcelink getByPtIdAndPrId(@Param("ptId") Integer ptId, @Param("prId") Integer prId);

    ProdProcelink getUpProcess(@Param("ptId") Integer ptId, @Param("prId") Integer prId, @Param("sortNum") Integer sortNum);

}
