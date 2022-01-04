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
package com.yb.hdverify.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.base.vo.BaseClassinfoVO;
import com.yb.hdverify.entity.HdverifyMach;
import com.yb.hdverify.vo.HdverifyMachVO;
import com.yb.supervise.vo.SuperviseBoxinfoVO;

import java.util.List;


/**
 * 部门结构_yb_ba_dept 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IHdverifyMachService extends IService<HdverifyMach> {
    /**
     * 获取列表
     */
    List<HdverifyMachVO> getHdverifyMachList(HdverifyMachVO machVO);

    /**
     * 获取待认证的设备信息
     * @return
     */
    List<SuperviseBoxinfoVO> getMachList();
}
