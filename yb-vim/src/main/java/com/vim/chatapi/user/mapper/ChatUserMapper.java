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
package com.vim.chatapi.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vim.chatapi.user.entity.BaseStaffinfo;
import com.vim.chatapi.user.vo.BaseStaffextVO;
import com.vim.chatapi.user.vo.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springblade.saturn.entity.BaseStaffext;
import org.springblade.system.user.entity.User;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Mapper 接口
 *
 * @author qinbo
 */
@Mapper
public interface ChatUserMapper extends BaseMapper<User> {

    /**
     * 自定义分页
     *
     * @param page
     * @param user
     * @return
     */
    List<User> selectUserPage(IPage page, User user);

    /**
     * 获取用户
     *
     * @param tenantId
     * @param account
     * @param password
     * @return
     */
    User getUser(String tenantId, String account, String password);

    /**
     * 获取角色名
     *
     * @param ids
     * @return
     */
    List<String> getRoleName(String[] ids);

    /**
     * 获取角色别名
     *
     * @param ids
     * @return
     */
    List<String> getRoleAlias(String[] ids);

    /**
     * 获取部门名
     *
     * @param ids
     * @return
     */
    List<String> getDeptName(String[] ids);

    /**
     * 注册用户
     * author  : qinbo
     *
     * @param user
     * @return
     */
    boolean saveBladeUser(User user);

    /**
     * 获取最大的chatNo编号
     *
     * @return
     */
    Integer getMaxChatNo();

    /**
     * date:2020/3/24
     * author: qinbo
     *
     * @param newPhone
     * @return 用户修改手机
     */
    boolean updataUserPhone(String newPhone, Integer id);

    /**
     * ate:2020/3/24
     * author qinbo
     *
     * @param id
     * @return 获取用户信息
     */
    User getUser(Integer id);

    /**
     * date 2020/3/24
     * author qinbo
     *
     * @param phoneNum
     * @return 通过手机知道用户
     */
    User getUserByPhone(String phoneNum);

    /**
     * date 2020/3/25
     * author qinbo
     *
     * @param newPassword
     * @return 修改密码通过id
     */
    boolean updatePasswordById(String newPassword, Integer id);


    UserDto getUserInfo(@PathParam("userId") Integer userId,
                        @PathParam("tenantId") String tenantId);

    /**
     * 登录
     *
     * @param user
     * @return
     */
    User selectUser(User user);

    /**
     * 获取用户的其他信息
     * @return
     */
   BaseStaffextVO getBaseStaffextVO(Integer userId);



    User selectUserInfo(Integer userId);

    /**
     * 修改住址
     * @param
     * @return
     */
    int updateStaffinfo(BaseStaffext baseStaffext);

    BaseStaffinfo getbaseStaffInfoByJobnNum(String jobNum);

    int  updateBaseSatffinfo(String jobNum,Integer userId);


}
