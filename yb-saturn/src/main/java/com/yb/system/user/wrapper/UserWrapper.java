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
package com.yb.system.user.wrapper;

import com.yb.system.user.entity.SaUser;
import com.yb.system.user.service.SaIUserService;
import com.yb.system.user.vo.SaUserVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Jenny wang
 */
public class UserWrapper extends BaseEntityWrapper<SaUser, SaUserVO> {

    private static SaIUserService SaIUserService;

//    private static IDictClient dictClient;

    static {
        SaIUserService = SpringUtil.getBean(SaIUserService.class);
//        dictClient = SpringUtil.getBean(IDictClient.class);
    }

    public static UserWrapper build() {
        return new UserWrapper();
    }

    @Override
    public SaUserVO entityVO(SaUser saUser) {
        SaUserVO userVO = BeanUtil.copy(saUser, SaUserVO.class);
        List<String> roleName = SaIUserService.getRoleName(saUser.getRoleId());
        List<String> deptName = null;
        if (saUser.getDeptId() != null && !saUser.getDeptId().equals("")) {
            deptName = SaIUserService.getDeptName(saUser.getDeptId());
            userVO.setDeptName(Func.join(deptName));
        }
        userVO.setRoleName(Func.join(roleName));
//        R<String> dict = dictClient.getValue("sex", Func.toInt(user.getSex()));
//        if (dict.isSuccess()) {
//            userVO.setSexName(dict.getData());
//        }
        return userVO;
    }

}
