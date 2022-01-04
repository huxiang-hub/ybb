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
package com.yb.machine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.machine.entity.MachineMixbox;
import com.yb.machine.vo.MachineMixboxVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 印联盒（本租户的盒子），由总表分发出去 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface MachineMixboxMapper extends BaseMapper<MachineMixbox> {

    /**
     * 自定义分页
     *
     * @param page
     * @param machineMixbox
     * @return
     */
    List<MachineMixboxVO> selectMachineMixboxPage(IPage page, MachineMixboxVO machineMixbox);

    /**
     * 通过盒子编号UUID查询
     */

    MachineMixbox selectBoxByBno(String uuid);
    /**
     * 修改盒子绑定状态
     * */
    public int setMixboxByMaId(String uuid);
    /**
     * 修改盒子绑定状态
     *
     * */
    public int addMixboxByMaId(String uuid,Integer maId);
    /**
     *
     * 获取可以active盒状态为1的子
     *
     * */
    public List<MachineMixbox> getBlindBox();

    /**
     * 將删除设备的盒子解绑
     */
    Integer setMixboxByListMaId(@Param("list") List<Integer> ids);

    /****blade x 搬运项目***/
    /**
     * 自定义分页
     *
     * @param page
     * @param
     * @return
     */

    List<MachineMixboxVO> getMachineMixboxPage(IPage page, MachineMixboxVO mixboxVO);

    /**
     * 获取部门
     * @return
     */
    List<BaseDeptinfo> getDeptInfo();


    /**
     * 通过盒子uuid 找盒子
     * @param uuid
     * @return
     */
    MachineMixboxVO findMixboxIsExit(String uuid);
}
