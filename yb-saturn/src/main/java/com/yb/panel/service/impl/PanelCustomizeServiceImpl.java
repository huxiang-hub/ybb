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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.panel.entity.PanelCustomize;
import com.yb.panel.mapper.PanelCustomizeMapper;
import com.yb.panel.request.PanelCustomizeRequest;
import com.yb.panel.service.PanelCustomizeService;
import com.yb.panel.vo.PanelCustomizeVO;
import com.yb.process.entity.ProcessClassify;
import com.yb.process.mapper.ProcessClassifyMapper;
import com.yb.process.service.IProcessClassifyService;
import com.yb.process.vo.ProcessClassifyVO;
import com.yb.process.vo.PyModelVO;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * 工序分类表_yb_process_classify 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class PanelCustomizeServiceImpl extends ServiceImpl<PanelCustomizeMapper, PanelCustomize> implements PanelCustomizeService {

	@Autowired
	private PanelCustomizeMapper panelCustomizeMapper;

	@Autowired
	private IMachineMainfoService iMachineMainfoService;

	@Override
	public List<Integer> getMuId(Integer maId) {
		return panelCustomizeMapper.getMuId(maId);
	}

	@Override
	public boolean panelMenuAdd(List<PanelCustomize> panelCustomizes) {
		int n=panelCustomizeMapper.panelMenuAdd(panelCustomizes);
		boolean flag=false;
		if(n>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public boolean deteleMenu(List<PanelCustomize> panelCustomizes) {
		boolean flag=false;
		if(panelCustomizes!=null&&panelCustomizes.size()>0){
			panelCustomizeMapper.deleteMenu(panelCustomizes);
			flag=true;
		}
		return flag;
	}

	@Override
	public boolean batchEdit(PanelCustomizeRequest request) {
		List<PanelCustomize> panelCustomizeS=new ArrayList<>();
		boolean flag=false;
		if(request.getCheckList()!=null && request.getCheckList().size()>0){
			for(int i=0;i<request.getMaNameList().size();i++) {
				String maName = null;
				if (request.getMaNameList().get(i).contains("(")) {
					maName = request.getMaNameList().get(i).substring(0, request.getMaNameList().get(i).indexOf("("));
				} else {
					maName = request.getMaNameList().get(i);
				}
				int maId = iMachineMainfoService.getMachins(maName).get(0).getId();
				panelCustomizeMapper.batchDeletion(maId);
				for (int j = 0; j < request.getCheckList().size(); j++) {
					PanelCustomize panelCustomize = new PanelCustomize();
					panelCustomize.setMaId(maId);
					panelCustomize.setMuId(request.getCheckList().get(j));
					panelCustomize.setStatus(1);
					panelCustomizeS.add(panelCustomize);
				}
			}
			flag = panelMenuAdd(panelCustomizeS);
		}else{
			for(int i=0;i<request.getMaNameList().size();i++) {
				String maName = null;
				if (request.getMaNameList().get(i).contains("(")) {
					maName = request.getMaNameList().get(i).substring(0, request.getMaNameList().get(i).indexOf("("));
				} else {
					maName = request.getMaNameList().get(i);
				}
				int maId = iMachineMainfoService.getMachins(maName).get(0).getId();
				panelCustomizeMapper.batchDeletion(maId);
			}
			flag=true;
		}
		return flag;
	}
}
