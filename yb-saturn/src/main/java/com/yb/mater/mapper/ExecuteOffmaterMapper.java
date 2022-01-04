/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yb.mater.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.vo.TraycardDataVO;
import com.yb.mater.entity.ExecuteOffmater;
import com.yb.mater.vo.ExecuteOffmaterVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 物料退料管理_yb_execute_offmater Mapper 接口
 *
 * @author BladeX
 * @since 2021-01-18
 */
public interface ExecuteOffmaterMapper extends BaseMapper<ExecuteOffmater> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param executeOffmater
	 * @return
	 */
	List<ExecuteOffmaterVO> selectExecuteOffmaterPage(IPage page, ExecuteOffmaterVO executeOffmater);

	List<ExecuteOffmater> getListByWfId(Integer wfId);

	List<ExecuteOffmaterVO> getListcsByWfId(Integer wfId);
	/**
	 * 物料获取打印数据
	 * @param idList
	 * @return
	 */
	List<TraycardDataVO> getExecuteMaterialsPrint(@Param("idList") List<Integer> idList);

	/**
	 * 根据退料查询标识卡id
	 * @param idList
	 * @return
	 */
    List<Integer> executeOffmaterMapper(@Param("idList") List<Integer> idList);

    @Select("select bar_code from yb_execute_offmater where bar_code like '%NXWL-%' order by create_at DESC limit 1")
    String getNxwlBarCode();

	/**
	 * 半成品,托盘里找数据
	 * @param idList
	 * @return
	 */
	List<TraycardDataVO> getMaterialsPrint(@Param("idList") List<Integer> idList);

}
