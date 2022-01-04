package com.vim.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vim.user.entity.ImChatGroup;
import com.vim.user.mapper.ImChatGroupMapper;
import com.vim.user.service.IImChatGroupService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群 服务实现类
 * </p>
 *
 * @author 乐天
 * @since 2018-10-28
 */
@Service
@Qualifier("imChatGroupServiceImpl")
public class ImChatGroupServiceImpl extends ServiceImpl<ImChatGroupMapper, ImChatGroup> implements IImChatGroupService {

}
