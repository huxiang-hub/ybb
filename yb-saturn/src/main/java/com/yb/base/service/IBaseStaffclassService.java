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
package com.yb.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.base.entity.BaseStaffclass;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.vo.BaseStaffclassVO;
import com.yb.base.vo.BaseStaffinfoVO;

import java.util.List;

/**
 * 人员班组临时调班_yb_base_staffclass 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IBaseStaffclassService extends IService<BaseStaffclass> {

    /**
     * 自定义分页
     *
     * @param page
     * @param baseStaffclassVO
     * @return
     */
    IPage<BaseStaffclassVO> selectBaseStaffclassPage(IPage<BaseStaffclassVO> page, BaseStaffclassVO baseStaffclassVO);
    /**
     * 查询调出去的人员信息
     */
    BaseStaffclassVO getGoOutUser(Integer userId);
    /**
     * 将ids的失效
     */
    boolean updateIsUsedByIds(List<Integer> ids);

    /**
     * 将到时间的调换修改为不可用
     */
    boolean updataByisused();
    /**
     * 将bcids的失效
     */
    boolean updateIsUsedBybcIds(List<Integer> bcids);
}
