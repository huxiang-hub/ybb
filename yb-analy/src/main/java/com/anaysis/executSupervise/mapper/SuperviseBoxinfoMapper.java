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
package com.anaysis.executSupervise.mapper;

import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.entity.SuperviseExecute;
import com.anaysis.executSupervise.vo.SuperviseBoxinfoVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备当前状态表boxinfo-视图 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface SuperviseBoxinfoMapper extends BaseMapper<SuperviseBoxinfo> {
    SuperviseBoxinfo getBoxInfoByBno(String uuid);

    int update(SuperviseBoxinfo boxInfoEntity);

    int clearZero(@Param("id") Integer id, @Param("clearNum") Integer clearNum);

    int save(SuperviseBoxinfo boxInfoEntity);

    List<SuperviseBoxinfo> getList();

    List<SuperviseBoxinfoVo> getListByrun();

    SuperviseBoxinfoVo getBoxVoByuuid(String uuid);
}
