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
package com.yb.panel.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.panel.entity.PanelMenu;
import com.yb.panel.vo.PanelMenuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机台菜单_yb_panel_menu服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface PanelMenuService extends IService<PanelMenu> {

	/**
	 * 通过菜单id查出对应的菜单名称
	 * */
	List<String> getMuName(@Param("muIds") List<Integer> muIds);

	/**
	 * 返回设备菜单所有信息
	 * */
	List<PanelMenuVO> getPanelMenuAll();
}
