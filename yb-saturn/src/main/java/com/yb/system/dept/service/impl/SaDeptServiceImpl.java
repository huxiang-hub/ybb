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
package com.yb.system.dept.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.system.dept.mapper.SaDeptMapper;
import com.yb.system.dept.service.SaIDeptService;
import com.yb.system.dept.vo.SaDeptVO;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class SaDeptServiceImpl extends ServiceImpl<SaDeptMapper, BaseDeptinfo> implements SaIDeptService {

	@Override
	public IPage<SaDeptVO> selectDeptPage(IPage<SaDeptVO> page, SaDeptVO dept) {
		return page.setRecords(baseMapper.selectDeptPage(page, dept));
	}

	@Override
	public List<SaDeptVO> tree(String tenantId) {
        List<SaDeptVO> merge = ForestNodeMerger.merge(baseMapper.tree(tenantId));
        return merge;
	}

	@Override
	public IPage<BaseDeptinfo> selectDeptPages(IPage<BaseDeptinfo> page, BaseDeptinfo baseDeptinfo) {
		return page.setRecords(baseMapper.selectDeptPages(page, baseDeptinfo));
	}

	@Override
	public boolean updateIsdelete(List<Integer> ids) {
		Integer result = baseMapper.updateIsdelete(ids);
		return result != null && result>= 1;
	}

}
