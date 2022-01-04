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
import com.yb.prod.entity.ProdPartsinfo;
import com.yb.prod.vo.ProdPartsinfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 产品信息（product） Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ProdPartsinfoMapper extends BaseMapper<ProdPartsinfo> {
    List<ProdPartsinfoVo> selectPtNames(Integer wbId);


    List<ProdPartsinfoVo> listByPdId(@Param("pdId") Integer pdId, @Param("pdType") Integer pdType);

    Integer removeByPdIdAndPdType(@Param("pdId") Integer pdId, @Param("pdType") Integer pdType);
}
