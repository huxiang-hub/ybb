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
package com.yb.mater.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.mater.entity.MaterMtinfo;
import com.yb.mater.vo.MaterMtinfoVO;

import java.util.List;

/**
 * 物料列表_yb_mater_matinfo 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IMaterMtinfoService extends IService<MaterMtinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param materMtinfo
     * @return
     */
    IPage<MaterMtinfoVO> selectMaterMtinfoPage(IPage<MaterMtinfoVO> page, MaterMtinfoVO materMtinfo);

    /**
     * 更改刪除状态为已删除
     */
    boolean updatemtinfoIsdelById(Integer[] ids);
    /**
     * 查询属于该类型的物料
     */
    List<MaterMtinfoVO> mtinfoByType(Integer mcId);
}