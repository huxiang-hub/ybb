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
package com.yb.execute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecutePutStore;
import com.yb.execute.vo.ExecuteBrieferVO;
import com.yb.execute.vo.ExecuteFaultVO;
import com.yb.execute.vo.ExecutePutStoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生产执行上报信息_yb_execute_briefer Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExecutePutstoreMapper extends BaseMapper<ExecutePutStore> {

    /**
     * 自定义分页
     *
     * @param
     * @param
     * @return
     */
    /**
     * 自定义分页
     *
     * @param page
     * @param
     * @return
     */
    List<ExecutePutStoreVO> selectExecutePutStorePage(IPage page,ExecutePutStoreVO executePutStoreVO);

    /*分页查询如入库上报*/
    List<ExecuteBrieferVO> pagePutstoreList(Integer current, Integer size, Integer orderId, String orderName);
    /*入库上报总数*/
    Integer executePutstorECount(Integer orderId, String orderName);

}
