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
package com.yb.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.vo.BaseStaffinfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人员表_yb_base_staffinfo Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface BaseStaffinfoMapper extends BaseMapper<BaseStaffinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param baseStaffinfo
     * @return
     */
    List<BaseStaffinfoVO> selectBaseStaffinfoPage(IPage page, BaseStaffinfoVO baseStaffinfo);
    /**
     * 获取机台操作人的部门ID（添加同部门人的助手做准备）
     * */
    public BaseStaffinfoVO getLeaderByLeaderId(String leadrId);
    /**
     * 获取人员信息 通过us_id
     *
     */
    public BaseStaffinfoVO getBaseStaffinfoByUsId(Integer usId);

    /**
     * 获取所有员工信息
     * @return
     */
    List<BaseStaffinfoVO> selectBaseStaffInfoAndBaseUser(IPage page, @Param("baseStaffinfoVO")BaseStaffinfoVO baseStaffinfoVO);

    /**
     *获取单个用户信息
     */
    public BaseStaffinfoVO getOneStaffInfo(Integer id);

    /**
     * 修改为停用
     */
    public Integer updateStaffInfoIsUsedById(Integer id);

    /**
     * 查询工号是否重复
     */
    public BaseStaffinfoVO getBaseStaffinfoVOByjobnum(@Param("jobnum") String jobnum);
    /**
     * 查询工号是否重复
     */
    public Integer getJobnumIsExit(@Param("jobnum") String jobnum);

    /**
     * 查询每个工序总共的人数
     * @return
     */
    Integer getProcessesSum(String prId);
    /**
     * 车间查询人员
     */
    List<BaseStaffinfoVO> getUserListByPdId(@Param("baseStaffinfoVO") BaseStaffinfoVO baseStaffinfoVO);
    /**
     * 修改班组
     */
    public Integer updateStaffInfoBcIdById(Integer id, Integer bcId);
    /**
     * 查询班组内人员names
     */
    String getNamesBybcId(Integer bcId);
    /**
     * 查询班组内人员ids
     */
    String getIdsBybcId(Integer bcId);
    /**
     * 查询人员信息（所属班组，当前所在班组）
     */
    List<BaseStaffinfoVO> getAllByBcId(@Param("bcId") Integer bcId);
    /**
     * 查询调来的人员信息（所属班组）
     */
    List<BaseStaffinfoVO> getInUserByBcId(@Param("bcId") Integer bcId);

    /**
     * #查询使用该班次的班组集合（查询所有处理调班人员以外的人员）
     * @param wsId
     * @return
     */
    List<BaseStaffinfoVO> getAllByWsId(@Param("wsId") Integer wsId);

    /**
     * 查询换组来执行这个班次的人员
     * @param wsId
     * @return
     */
    List<BaseStaffinfoVO> getStaffClassAllByWsId(@Param("wsId") Integer wsId);

    Integer updateStaffInfoBcIdBybcId(@Param("list") List<Integer> bcIds, @Param("bcId") Integer bcId);

    /**
     * 根据电话修改钉钉id
     * @param userid
     * @param mobile
     */
    void updateStaffinfoDDid(@Param("userid") String userid, @Param("mobile")String mobile);

    /**
     * 根据电话修改飞书id
     * @param openId
     * @param mobile
     */
    void updateFeiShuIdByPhone(@Param("openId") String openId, @Param("mobile") String mobile);
}
