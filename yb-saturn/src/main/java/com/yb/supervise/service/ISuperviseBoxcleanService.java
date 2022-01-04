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
package com.yb.supervise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.supervise.entity.SuperviseBoxclean;
import com.yb.supervise.vo.SuperviseBoxcleanVO;

/**
 * 设备清零日志表 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface ISuperviseBoxcleanService extends IService<SuperviseBoxclean> {

    /**
     * 自定义分页
     *
     * @param page
     * @param superviseBoxclean
     * @return
     */
    IPage<SuperviseBoxcleanVO> selectSuperviseBoxcleanPage(IPage<SuperviseBoxcleanVO> page, SuperviseBoxcleanVO superviseBoxclean);

}
