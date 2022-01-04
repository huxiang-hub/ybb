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
package com.anaysis.mysqlmapper;


import com.anaysis.entity.WorkbatchOrdlink;
import com.anaysis.entity.det;
import com.anaysis.entity.tmp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 生产排产表yb_workbatch_ordlink Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface WorkbatchOrdlinkMapper extends BaseMapper<WorkbatchOrdlink> {

    /**
     * 查询排序
     */
    Integer getSdSort();

    Integer allDelete();

    Integer setStatus(@Param("list") List<String> list);

    List<tmp> selectByStatus(String status);

    List<det> selectByStatusReturnErpId(String status);

    List<Integer> selectByEroId(@Param("erpId") String erpId);

    @Select("select erp_id from yb_workbatch_ordlink where erp_id is not null and status != 10")
    List<String> getErpId();

    void deleteByErpIds(List<String> delIds);

    @Select("select erp_id from yb_workbatch_ordlink where ma_id = #{maId} and status != 10")
    List<String> getErpIdByMaId(String maId);

    List<Integer> selectByErpIds(List<String> delIds);

    @Select("select id from yb_workbatch_shift where sd_id = #{sdId}")
    List<Integer> selectWorbatchShift(Integer sdId);

    @Select("select * from yb_workbatch_ordlink where erp_id = #{erpId}")
    List<WorkbatchOrdlink> selectByErpId(String erpId);
}


