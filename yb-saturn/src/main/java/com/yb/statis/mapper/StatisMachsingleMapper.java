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
package com.yb.statis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.statis.entity.StatisMachsingle;
import com.yb.statis.vo.StatisMachsingleVO;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author Blade
 * @since 2020-04-17
 */
public interface StatisMachsingleMapper extends BaseMapper<StatisMachsingle> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param statisMachsingle
	 * @return
	 */
	List<StatisMachsingleVO> selectStatisMachsinglePage(IPage page, StatisMachsingleVO statisMachsingle);

	/**
	 * 分页怕重复
	 * @param page
	 * @param statisMachsingle
	 * @return
	 */
	List<StatisMachsingleVO> selectStatisMachsingleoEEPage(IPage page, StatisMachsingleVO statisMachsingle);

	/**
	 * 兴艺
	 * @param page
	 * @param statisMachsingle
	 * @return
	 */
	List<StatisMachsingleVO> PageOfMachsinglexy(IPage page, StatisMachsingleVO statisMachsingle);
}
