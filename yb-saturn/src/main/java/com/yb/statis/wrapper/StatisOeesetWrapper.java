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
package com.yb.statis.wrapper;

import com.yb.statis.entity.StatisOeeset;
import com.yb.statis.vo.StatisOeesetVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-04-17
 */
public class StatisOeesetWrapper extends BaseEntityWrapper<StatisOeeset, StatisOeesetVO>  {

    public static StatisOeesetWrapper build() {
        return new StatisOeesetWrapper();
    }

	@Override
	public StatisOeesetVO entityVO(StatisOeeset statisOeeset) {
		StatisOeesetVO statisOeesetVO = BeanUtil.copy(statisOeeset, StatisOeesetVO.class);

		return statisOeesetVO;
	}

}
