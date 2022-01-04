package com.vim.hdverify.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.vim.hdverify.entity.HdverifyMach;
import com.vim.hdverify.mapper.HdverifyMachMapper;
import com.vim.hdverify.service.IHdverifyMachService;
import com.vim.hdverify.vo.HdverifyMachVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HdverifyMachServiceImpl extends ServiceImpl<HdverifyMachMapper, HdverifyMach> implements IHdverifyMachService {


    @Override
    public List<HdverifyMachVO> getHdverifyMachList(HdverifyMachVO machVO) {

        return baseMapper.getHdverifyMachList(machVO);
    }
}
