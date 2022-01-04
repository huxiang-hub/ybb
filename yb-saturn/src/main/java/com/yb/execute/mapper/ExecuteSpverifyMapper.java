package com.yb.execute.mapper;/*
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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.execute.entity.ExecuteSpverify;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.vo.ExecuteSpverifyVO;
import com.yb.execute.vo.ProcessInstanceVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审核记录_yb_execute_spverify Mapper 接口
 *
 * @author BladeX
 * @since 2021-03-07
 */
public interface ExecuteSpverifyMapper extends BaseMapper<ExecuteSpverify> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param executeSpverify
	 * @return
	 */
	List<ExecuteSpverifyVO> selectExecuteSpverifyPage(IPage page, ExecuteSpverifyVO executeSpverify);

    List<ProcessInstanceVO> getByExId(Integer exId);

    @Select("SELECT b.sp_mold FROM yb_execute_spverify a LEFT JOIN yb_execute_scrap b ON a.sp_id = b.id WHERE a.process_instance_id = #{id}")
    Integer getSpMoldByProinstanceId(String id);
}
