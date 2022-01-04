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
package com.yb.mater.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.mater.entity.MaterProdlink;
import com.yb.mater.vo.MaterProdlinkVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品物料关系（materiel） Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface MaterProdlinkMapper extends BaseMapper<MaterProdlink> {

    /**
     * 自定义分页
     *
     * @param page
     * @param materProdlink
     * @return
     */
    List<MaterProdlinkVO> selectMaterProdlinkPage(IPage page, MaterProdlinkVO materProdlink);

    /**
     * 产品id（pdid）查询对应物料
     */
    List<MaterProdlinkVO> selectMaterProdlinkVOListByPdId(Integer pdId, Integer pdType);

    /**
     * 部件id（id）查询对应物料
     */
    List<MaterProdlinkVO> selectMaterProdlinkVOListById(@Param("Id") Integer Id);
    /**
     * 删除产品对应部件对应的物料
     */
    Integer removeByPdIdAndPdType(@Param("pdId") Integer pdId, @Param("pdType") Integer pdType);
}
