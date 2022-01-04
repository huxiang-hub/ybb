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
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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

    @Select("select erp_id from yb_workbatch_ordlink where erp_id is not null and status != 10")
    List<String> getErpId();

    @Select("select erp_id from yb_workbatch_ordlink")
    List<String> getAllErpIds();

    @Select("select a.erp_id from yb_workbatch_ordlink a left join yb_workbatch_shift b ON a.id = b.sd_id WHERE b.id IS NOT NULL")
    List<String> getAllNoYieldErpIds();
}


