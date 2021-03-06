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
package com.yb.system.menu.wrapper;

import com.yb.system.dict.service.SaIDictService;
import com.yb.system.menu.entity.Menu;
import com.yb.system.menu.service.SaIMenuService;
import com.yb.system.menu.vo.MenuVO;
import org.springblade.common.constant.CommonConstant;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class MenuWrapper extends BaseEntityWrapper<Menu, MenuVO> {

    private static SaIMenuService menuService;

    //private static IDictClient dictClient;
    private static SaIDictService dictClient;

    static {
        menuService = SpringUtil.getBean(SaIMenuService.class);
        //dictClient = SpringUtil.getBean(IDictClient.class);
    }

    public static MenuWrapper build() {
        return new MenuWrapper();
    }

    @Override
    public MenuVO entityVO(Menu menu) {
        MenuVO menuVO = BeanUtil.copy(menu, MenuVO.class);
        if (Func.equals(menu.getParentId(), CommonConstant.TOP_PARENT_ID)) {
            menuVO.setParentName(CommonConstant.TOP_PARENT_NAME);
        } else {
            Menu parent = menuService.getById(menu.getParentId());
            menuVO.setParentName(parent.getName());
        }
        R<String> d1 = dictClient.getDictVal("menu_category", Func.toInt(menuVO.getCategory()));
        R<String> d2 = dictClient.getDictVal("button_func", Func.toInt(menuVO.getAction()));
        R<String> d3 = dictClient.getDictVal("yes_no", Func.toInt(menuVO.getIsOpen()));
        if (d1.isSuccess()) {
            menuVO.setCategoryName(d1.getData());
        }
        if (d2.isSuccess()) {
            menuVO.setActionName(d2.getData());
        }
        if (d3.isSuccess()) {
            menuVO.setIsOpenName(d3.getData());
        }
        return menuVO;
    }


    public List<MenuVO> listNodeVO(List<Menu> list) {
        List<MenuVO> collect = list.stream().map(menu -> BeanUtil.copy(menu, MenuVO.class)).collect(Collectors.toList());
        return ForestNodeMerger.merge(collect);
    }

}
