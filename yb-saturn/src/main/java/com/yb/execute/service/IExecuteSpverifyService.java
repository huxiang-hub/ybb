package com.yb.execute.service;/*
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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.execute.entity.ExecuteSpverify;
import com.yb.execute.vo.ExecuteSpverifyVO;

/**
 * 审核记录_yb_execute_spverify 服务类
 *
 * @author BladeX
 * @since 2021-03-07
 */
public interface IExecuteSpverifyService extends IService<ExecuteSpverify> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param executeSpverify
	 * @return
	 */
	IPage<ExecuteSpverifyVO> selectExecuteSpverifyPage(IPage<ExecuteSpverifyVO> page, ExecuteSpverifyVO executeSpverify);

	/**
	 * 获取stModel
	 * @param id
	 * @return
	 */
    Integer getSpMoldByProinstanceId(String id);
}
