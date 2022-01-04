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
import com.yb.panel.entity.PanelCustomize;
import com.yb.panel.request.PanelCustomizeRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机台自定义_yb_panel_customize  服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface PanelCustomizeService extends IService<PanelCustomize> {

	/**
	 * 通过设备id查询菜单
	 * */
	List<Integer> getMuId(@Param("maId") Integer maId);

	/**
	 *添加设备菜单
	 * */
	boolean panelMenuAdd(@Param("panelCustomizes") List<PanelCustomize> panelCustomizes);

	/**
	 * 删除菜单
	 * */
	boolean deteleMenu(@Param("panelCustomizes") List<PanelCustomize> panelCustomizes);

	/**
	 * 批量修改
	 * */
	boolean batchEdit(@Param("request") PanelCustomizeRequest request);
}
