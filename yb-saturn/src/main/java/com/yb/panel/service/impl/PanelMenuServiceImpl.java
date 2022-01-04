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
package com.yb.panel.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.panel.entity.PanelMenu;
import com.yb.panel.mapper.PanelMenuMapper;
import com.yb.panel.service.PanelMenuService;
import com.yb.panel.vo.PanelMenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工序分类表_yb_process_classify 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class PanelMenuServiceImpl extends ServiceImpl<PanelMenuMapper, PanelMenu> implements PanelMenuService {

	@Autowired
	private PanelMenuMapper panelMenuMapper;

	@Override
	public List<String> getMuName(List<Integer> muIds) {
		return panelMenuMapper.getMuName(muIds);
	}

	@Override
	public List<PanelMenuVO> getPanelMenuAll() {
		List<PanelMenuVO> panelMenuList=null;
		panelMenuList=panelMenuMapper.getPanelMenuAll();
		return panelMenuList;
	}
}
