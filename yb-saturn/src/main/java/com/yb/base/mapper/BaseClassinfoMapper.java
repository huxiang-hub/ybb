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
package com.yb.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.base.entity.BaseClassinfo;
import com.yb.base.vo.BaseClassinfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人班组信息_yb_base_classinfo Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface BaseClassinfoMapper extends BaseMapper<BaseClassinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param baseClassinfoVO
     * @return
     */
    List<BaseClassinfoVO> selectBaseClassinfoPage(IPage page, @Param("baseClassinfoVO") BaseClassinfoVO baseClassinfoVO);
    /**
     * 班次下的班组
     */
    String getBcIdsBywsId(@Param("wsId") Integer wsId);

    Integer setWsIdByids(@Param("wsId") Integer wsId, @Param("bcIds") Integer[] bcIds);

    Integer setWsIdIsNull(@Param("id") Integer id);

    List<BaseClassinfoVO> getAllLits();
}
