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
package com.yb.execute.wrapper;

import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.vo.ExecuteBrieferVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 生产执行上报信息_yb_execute_briefer包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-03-10
 */
public class ExecuteBrieferWrapper extends BaseEntityWrapper<ExecuteBriefer, ExecuteBrieferVO> {

    public static ExecuteBrieferWrapper build() {
        return new ExecuteBrieferWrapper();
    }

    @Override
    public ExecuteBrieferVO entityVO(ExecuteBriefer executeBriefer) {
        ExecuteBrieferVO executeBrieferVO = BeanUtil.copy(executeBriefer, ExecuteBrieferVO.class);

        return executeBrieferVO;
    }

}
