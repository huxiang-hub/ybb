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
package com.yb.actset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.actset.entity.ActsetCheckLog;
import com.yb.actset.entity.ActsetCkflow;
import com.yb.actset.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author
 * @since 2020-03-10
 */
@Mapper
public interface ActsetCheckLogMapper extends BaseMapper<ActsetCheckLog> {


    /**
     * 订单编号 查找当前产品或者订单有几条审核工序记录
     * @param orderId
     * @return
     */
    Integer getLogCount(Integer orderId);

    /***
     *查找某个指定的审核记录
     */
   ActsetCheckLogVO getActsetCheckLog(CheckModelVO modelVO);

    /***
     *查找的审核记录 根据 awType查出所有的待审核记录
     */
    List<OrderCheckModelVO> getOrderCheckLogs(CheckModelVO modelVO, IPage<OrderCheckModelVO> page);

    /***
     *查找的审核记录 根据 awType查出所有的待审核记录
     */
    List<ProductCheckModelVO> getProdCheckLogs(CheckModelVO modelVO, IPage page);
    /***
     *查找的审核记录 根据 awType查出所有的待审核记录
     */
    List<StoregeCheckModelVO> getStoregeCheckLogs(CheckModelVO modelVO, IPage page);
    /**
     * 通过具体操作环节找到审核流程表的主键
     * @param asType
     * @param awType
     * @return
     */
    ActsetCkflow getAwIdByType(String asType, String awType);

    /**
     * 返回最新的一天审核记录
     * @return
     */
    ActsetCheckLog getMoreNewCheckLog(Integer orderId);

    /***
     *查找的审核记录 根据 awType查出所有的待审核记录(订单，包含产品名称)
     */
    List<OrderCheckModelVO> getOrderCheckLogsAndPdName(CheckModelVO modelVO, IPage<OrderCheckModelVO> page);
}


