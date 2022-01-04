package com.yb.workbatch.service.impl;/*
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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.workbatch.entity.Ordexecute;
import com.yb.workbatch.mapper.OrdexecuteMapper;
import com.yb.workbatch.service.IOrdexecuteService;
import com.yb.workbatch.vo.OrdexecuteVO;
import org.springframework.stereotype.Service;

/**
 * VIEW 服务实现类
 *
 * @author BladeX
 * @since 2021-01-15
 */
@Service
public class OrdexecuteServiceImpl extends ServiceImpl<OrdexecuteMapper, Ordexecute> implements IOrdexecuteService {

    @Override
    public IPage<OrdexecuteVO> selectOrdexecutePage(IPage<OrdexecuteVO> page, OrdexecuteVO ordexecute) {
        return page.setRecords(baseMapper.selectOrdexecutePage(page, ordexecute));
    }

}
