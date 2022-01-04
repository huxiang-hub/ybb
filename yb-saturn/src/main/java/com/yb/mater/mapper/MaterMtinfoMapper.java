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
import com.yb.mater.entity.MaterMtinfo;
import com.yb.mater.vo.MaterMtinfoVO;
import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料列表_yb_mater_matinfo Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface MaterMtinfoMapper extends BaseMapper<MaterMtinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param materMtinfo
     * @return
     */
    List<MaterMtinfoVO> selectMaterMtinfoPage(IPage page, @Param("materMtinfo") MaterMtinfoVO materMtinfo);

    /*查询对应订单所需物料*/
    List<MaterMtinfo> selectMaterMtinfos(Integer pdId);

    /**
     * 修改为已删除
     * @param id
     * @return
     */
    Integer updatemtinfoIsdelById(Integer id);
    /**
     * 查询属于该类型的物料
     */
    List<MaterMtinfoVO> mtinfoByType(Integer mcId);

    List<MaterMtinfoVO> findBySdId(WorkbatchOrdlinkVO workbatchOrdlinkVO);

    List<MaterMtinfoVO> findBySdIds(WorkbatchOrdlinkVO workbatchOrdlinkVO);
}
