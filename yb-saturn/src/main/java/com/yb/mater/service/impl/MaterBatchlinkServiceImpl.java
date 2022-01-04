package com.yb.mater.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.mater.entity.MaterBatchlink;
import com.yb.mater.mapper.MaterBatchlinkMapper;
import com.yb.mater.service.MaterBatchlinkService;
import com.yb.mater.vo.MaterBatchlinkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterBatchlinkServiceImpl extends ServiceImpl<MaterBatchlinkMapper, MaterBatchlink> implements MaterBatchlinkService {
    @Autowired
    private MaterBatchlinkMapper materBatchlinkMapper;
    @Override
    public IPage<MaterBatchlinkVO> getpage(MaterBatchlinkVO materBatchlinkVO, IPage<MaterBatchlinkVO> page) {
        List<MaterBatchlinkVO> materBatchlinkList = materBatchlinkMapper.getpage(materBatchlinkVO, page);
        return page.setRecords(materBatchlinkList);
    }

    @Override
    public List<Integer> getStatus() {
        return materBatchlinkMapper.getStatus();
    }

    @Override
    public IPage<MaterBatchlinkVO> waitPage(MaterBatchlinkVO materBatchlinkVO, IPage<MaterBatchlinkVO> ipage) {
        List<MaterBatchlinkVO> materBatchlinkList = materBatchlinkMapper.waitPage(materBatchlinkVO, ipage);
        return ipage.setRecords(materBatchlinkList);
    }
}
