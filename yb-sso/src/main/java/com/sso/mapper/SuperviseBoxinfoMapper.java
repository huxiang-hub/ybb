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
package com.sso.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.supervise.entity.SuperviseBoxinfo;
import com.sso.supervise.vo.SuperviseBoxinfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备当前状态表boxinfo-视图 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface SuperviseBoxinfoMapper extends BaseMapper<SuperviseBoxinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param superviseBoxinfo
     * @return
     */
    List<SuperviseBoxinfoVO> selectSuperviseBoxinfoPage(IPage page, SuperviseBoxinfoVO superviseBoxinfo);

    int getBoxNum(String mId);

    String getMacUser(String mId);

    SuperviseBoxinfo getBoxInfoByBno(String uuid_s);

    SuperviseBoxinfo getBoxInfoByMid(Integer maId);


    List<SuperviseBoxinfoVO> selectSuperviseBoxinfoVO();

    Integer pageCount(String status);

    List<SuperviseBoxinfoVO> getBoxListNotStop();
}
