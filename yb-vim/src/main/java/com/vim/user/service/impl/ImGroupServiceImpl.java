package com.vim.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vim.user.entity.ImGroup;
import com.vim.user.mapper.ImGroupMapper;
import com.vim.user.service.IImGroupService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 乐天
 * @since 2018-10-23
 */
@Service
@Qualifier("imGroupService")
public class ImGroupServiceImpl extends ServiceImpl<ImGroupMapper, ImGroup> implements IImGroupService {

}
