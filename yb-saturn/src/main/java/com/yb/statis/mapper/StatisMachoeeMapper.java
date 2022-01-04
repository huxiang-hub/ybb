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
import com.yb.statis.entity.StatisMachoee;
import com.yb.statis.vo.MachoeeExcelExportVO;
import com.yb.statis.vo.StatisMachoeeVO;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 *  Mapper 接口
 *
 * @author Blade
 * @since 2020-04-17
 */
public interface StatisMachoeeMapper extends BaseMapper<StatisMachoee> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param statisMachoee
	 * @return
	 */
	List<StatisMachoeeVO> selectStatisMachoeePage(IPage page, StatisMachoeeVO statisMachoee);

	List<StatisMachoeeVO> getOeeRateByDay(String maId, String prId, String dpId, String checkDate, ArrayList<String> conditionArrHour, ArrayList<String> conditionArrMin);

	/**
	 * 查询当周oee统计
	 * @param nowDate
	 * @param maId
	 * @return
	 */
    List<StatisMachoeeVO> getCkMachoee(String nowDate, Integer maId);

	/**
	 * 根据日期.设备id,班次id查询oee
	 * @param oeDate
	 * @param maId
	 * @param wsId
	 * @return
	 */
	Double getMachoeeByWsIdANDMaId(String oeDate, Integer maId, Integer wsId);

	/**
	 * 根据日期,设备类型查询二十四小时oee
	 * @param nowDate
	 * @param maIds
	 * @return
	 */
	List<StatisMachoeeVO> getDayMachoee(String nowDate, @Param("maIds") List<Integer> maIds);

	/**
	 *  查询当周班次类型班次oee
	 * @param nowDate
	 * @param maType
	 * @param wsId
	 * @return
	 */
    List<StatisMachoeeVO> getCkMachTypeOee(String nowDate, Integer maType, Integer wsId);

	/**
	 * 查询导出班次oee当月数据
	 * @return
	 */
    List<MachoeeExcelExportVO> machoeeExcelExport(String targetDay);
}
