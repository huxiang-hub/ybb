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
package com.yb.system.dept.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.system.dept.vo.SaDeptVO;

import java.util.List;

/**
 * 服务类
 *
 * @author Chill
 */
public interface SaIDeptService extends IService<BaseDeptinfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param dept
	 * @return
	 */
	IPage<SaDeptVO> selectDeptPage(IPage<SaDeptVO> page, SaDeptVO dept);

	/**
	 * 树形结构
	 *
	 * @param tenantId
	 * @return
	 */
	List<SaDeptVO> tree(String tenantId);

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @return
	 */
	IPage<BaseDeptinfo> selectDeptPages(IPage<BaseDeptinfo> page, BaseDeptinfo baseDeptinfo);

	/**
	 *
	 */
	boolean updateIsdelete(List<Integer> ids);
}
