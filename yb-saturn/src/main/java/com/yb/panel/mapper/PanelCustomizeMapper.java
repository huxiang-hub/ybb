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
package com.yb.panel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.panel.entity.PanelCustomize;
import com.yb.panel.request.PanelCustomizeRequest;
import com.yb.panel.vo.PanelCustomizeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序分类表_yb_process_classify Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface PanelCustomizeMapper extends BaseMapper<PanelCustomize> {

	/**
	 * 通过设备id查询菜单
	 * */
	List<Integer> getMuId(@Param("maId") Integer maId);

	/**
	 *添加设备菜单
	 * */
	int panelMenuAdd(@Param("panelCustomizes") List<PanelCustomize> panelCustomizes);

	/**
	 * 删除菜单
	 * */
	void deleteMenu(@Param("panelCustomizes") List<PanelCustomize> panelCustomizes);

	/**
	 * 批量修改
	 * */
	int batchEdit(@Param("request") PanelCustomizeRequest request);

	/**
	 * 批量删除
	 * */
	void batchDeletion(@Param("maId") Integer maId);
}
