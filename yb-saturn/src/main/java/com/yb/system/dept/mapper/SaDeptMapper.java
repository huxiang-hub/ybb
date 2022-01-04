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
package com.yb.system.dept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.system.dept.vo.DeptTreeNode;
import com.yb.system.dept.vo.SaDeptVO;
import com.yb.workbatch.vo.ShiftTypeListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author Chill
 */
@Mapper
public interface SaDeptMapper extends BaseMapper<BaseDeptinfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param dept
	 * @return
	 */
	List<SaDeptVO> selectDeptPage(IPage page, SaDeptVO dept);

	/**
	 * 获取树形节点
	 *
	 * @param tenantId
	 * @return
	 */
	List<SaDeptVO> tree(String tenantId);

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param baseDeptinfo
	 * @return
	 */
	List<BaseDeptinfo> selectDeptPages(IPage page, @Param("baseDeptinfo") BaseDeptinfo baseDeptinfo);

	Integer updateIsdelete(@Param("list") List<Integer> ids);

	List<ShiftTypeListVO> findByClassify(@Param("classify") Integer classify);
}
