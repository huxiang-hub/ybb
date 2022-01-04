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
package com.yb.exeset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.exeset.entity.ExesetFault;
import com.yb.exeset.vo.ExesetFaultVO;

/**
 * 故障停机设置_yb_exeset_fault 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IExesetFaultService extends IService<ExesetFault> {

    /**
     * 自定义分页
     *
     * @param page
     * @param exesetFault
     * @return
     */
    IPage<ExesetFaultVO> selectExesetFaultPage(IPage<ExesetFaultVO> page, ExesetFaultVO exesetFault);

    ExesetFault selectExesetFault(Integer mId);
    /***故停机窗口设置 设置弹出窗口 ，窗口小时时间 等
     * remark    yb_exeset_fault;
     */
    public boolean updateFaultPoP(ExesetFault fault);

}

