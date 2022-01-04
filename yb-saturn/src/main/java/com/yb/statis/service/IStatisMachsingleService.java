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
package com.yb.statis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.base.vo.BaseStaffinfoVO;
import com.yb.statis.entity.StatisMachsingle;
import com.yb.statis.vo.StatisMachsingleVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.text.ParseException;
import java.util.Date;

/**
 *  服务类
 *
 * @author Blade
 * @since 2020-04-17
 */
public interface IStatisMachsingleService extends IService<StatisMachsingle> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param statisMachsingle
	 * @return
	 */
	IPage<StatisMachsingleVO> selectStatisMachsinglePage(IPage<StatisMachsingleVO> page, StatisMachsingleVO statisMachsingle);

	IPage<StatisMachsingleVO> selectStatisMachsingleoEEPage(IPage<StatisMachsingleVO> page,StatisMachsingleVO statisMachsingle);

	void generateMachineOeeReport(Integer userId, Integer mallId, Date systemDate, Integer machOeeID,Integer exId) throws ParseException;

	IPage<StatisMachsingleVO> PageOfMachsinglexy(IPage<StatisMachsingleVO> page, StatisMachsingleVO statisMachsingle);
}
