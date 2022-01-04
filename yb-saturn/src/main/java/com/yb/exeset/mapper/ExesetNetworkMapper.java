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
package com.yb.exeset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.exeset.entity.ExesetNetwork;
import com.yb.exeset.vo.ExesetNetworkVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 网络设置管理_yb_exeset_network Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExesetNetworkMapper extends BaseMapper<ExesetNetwork> {

    /**
     * 自定义分页
     *
     * @param page
     * @param exesetNetwork
     * @return
     */
    List<ExesetNetworkVO> selectExesetNetworkPage(IPage page, ExesetNetworkVO exesetNetwork);
    /**
    *网络设置管理_yb_exeset_network
    */
    public ExesetNetwork getNetwork( Integer maId);
    /**
     * 网络设置(修改)管理_yb_exeset_network
     * */
    public boolean setNetwork(ExesetNetwork exesetNetwork);
    /**
     * 设置是否通信
     *
     * */
   public  boolean setIsChart(Integer maId,Integer isChart);



}
