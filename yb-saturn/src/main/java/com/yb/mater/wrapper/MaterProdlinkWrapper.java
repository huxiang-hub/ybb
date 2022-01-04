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
package com.yb.mater.wrapper;

import com.yb.mater.entity.MaterProdlink;
import com.yb.mater.vo.MaterProdlinkVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 产品物料关系（materiel）包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-03-10
 */
public class MaterProdlinkWrapper extends BaseEntityWrapper<MaterProdlink, MaterProdlinkVO> {

    public static MaterProdlinkWrapper build() {
        return new MaterProdlinkWrapper();
    }

    @Override
    public MaterProdlinkVO entityVO(MaterProdlink materProdlink) {
        MaterProdlinkVO materProdlinkVO = BeanUtil.copy(materProdlink, MaterProdlinkVO.class);

        return materProdlinkVO;
    }

}
