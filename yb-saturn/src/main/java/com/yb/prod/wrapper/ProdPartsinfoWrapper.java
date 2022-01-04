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

import com.yb.prod.entity.ProdPartsinfo;
import com.yb.prod.entity.ProdPdinfo;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdPdinfoVO;
import com.yb.system.menu.entity.Menu;
import com.yb.system.menu.vo.MenuVO;
import org.apache.poi.ss.formula.functions.T;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.node.ForestNodeManager;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.node.INode;
import org.springblade.core.tool.utils.BeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品信息（product）包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-03-10
 */
public class ProdPartsinfoWrapper extends BaseEntityWrapper<ProdPartsinfo, ProdPartsinfoVo> {

    public static ProdPartsinfoWrapper build() {
        return new ProdPartsinfoWrapper();
    }

    @Override
    public ProdPartsinfoVo entityVO(ProdPartsinfo entity) {
        ProdPartsinfoVo prodPdinfoVO = BeanUtil.copy(entity, ProdPartsinfoVo.class);
        return prodPdinfoVO;
    }
    public List<ProdPartsinfoVo> listNodeVO(List<ProdPartsinfoVo> list) {
        return null;
    }
}
