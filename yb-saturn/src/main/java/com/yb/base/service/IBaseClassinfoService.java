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
package com.yb.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.base.entity.BaseClassinfo;
import com.yb.base.entity.BaseStaffclass;
import com.yb.base.vo.BaseClassinfoVO;
import com.yb.base.vo.BaseStaffclassVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人班组信息_yb_base_classinfo 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IBaseClassinfoService extends IService<BaseClassinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param baseClassinfoVO
     * @return
     */
    IPage<BaseClassinfoVO> selectBaseClassinfoPage(IPage<BaseClassinfoVO> page, BaseClassinfoVO baseClassinfoVO);

    /**
     * 班次下的班组
     * @param wsId
     * @return
     */
    String getBcIdsBywsId(Integer wsId);

    /**
     * 更改班组班次信息
     */
    Integer setWsIdByids(Integer wsId, Integer[] bcIds);

    /**
     * 修改班组班次为空
     * @param id
     * @return
     */
    boolean setWsIdIsNull(Integer id);
    /**
     * 获取所有班组
     */
    List<BaseClassinfoVO> getAllLits();
}
