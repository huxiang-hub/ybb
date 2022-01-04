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
import com.yb.mater.entity.MaterProdlink;
import com.yb.mater.vo.MaterProdlinkVO;

import java.util.List;

/**
 * 产品物料关系（materiel） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IMaterProdlinkService extends IService<MaterProdlink> {

    /**
     * 自定义分页
     *
     * @param page
     * @param materProdlink
     * @return
     */
    IPage<MaterProdlinkVO> selectMaterProdlinkPage(IPage<MaterProdlinkVO> page, MaterProdlinkVO materProdlink);

    /**
     * 产品id（pdid）查询对应物料
     */
    List<MaterProdlinkVO> selectMaterProdlinkVOListByPdId(Integer pdId, Integer pdType);
    /**
     * 部件id（id）查询对应物料
     */
    public List<MaterProdlinkVO> selectMaterProdlinkVOListById(Integer Id);
    /**
     * 删除产品对应部件对应的物料
     */
    boolean removeByPdIdAndPdType(Integer pdId, Integer pdType);
}
