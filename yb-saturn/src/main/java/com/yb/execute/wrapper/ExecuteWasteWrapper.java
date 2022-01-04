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

import com.yb.execute.entity.ExecuteWaste;
import com.yb.execute.vo.ExecuteWasteVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 质量检查废品表_yb_execute_waste包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-03-10
 */
public class ExecuteWasteWrapper extends BaseEntityWrapper<ExecuteWaste, ExecuteWasteVO> {

    public static ExecuteWasteWrapper build() {
        return new ExecuteWasteWrapper();
    }

    @Override
    public ExecuteWasteVO entityVO(ExecuteWaste executeWaste) {
        ExecuteWasteVO executeWasteVO = BeanUtil.copy(executeWaste, ExecuteWasteVO.class);

        return executeWasteVO;
    }

}
