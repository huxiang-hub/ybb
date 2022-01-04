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
package com.yb.prod.wrapper;

import com.yb.prod.entity.ProdPdmodel;
import com.yb.prod.vo.ProdPdmodelVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 产品模版信息（productmodel）包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-03-10
 */
public class ProdPdmodelWrapper extends BaseEntityWrapper<ProdPdmodel, ProdPdmodelVO> {

    public static ProdPdmodelWrapper build() {
        return new ProdPdmodelWrapper();
    }

    @Override
    public ProdPdmodelVO entityVO(ProdPdmodel prodPdmodel) {
        ProdPdmodelVO prodPdmodelVO = BeanUtil.copy(prodPdmodel, ProdPdmodelVO.class);

        return prodPdmodelVO;
    }

}
