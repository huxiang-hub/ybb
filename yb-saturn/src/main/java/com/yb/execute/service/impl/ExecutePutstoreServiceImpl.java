package com.yb.execute.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.entity.ExecutePutStore;
import com.yb.execute.mapper.ExecutePutstoreMapper;
import com.yb.execute.service.ExecutePutstoreService;
import com.yb.execute.vo.ExecuteBrieferVO;
import com.yb.execute.vo.ExecutePutStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecutePutstoreServiceImpl extends ServiceImpl<ExecutePutstoreMapper, ExecutePutStore> implements ExecutePutstoreService {
    @Autowired
    private ExecutePutstoreMapper executePutstoreMapper;

    @Override
    public IPage<ExecuteBrieferVO> pagePutstoreList(Integer current, Integer size, ExecuteBrieferVO executeBrieferVO) {
        List<ExecuteBrieferVO> executeBrieferVOS = executePutstoreMapper.pagePutstoreList
                (current, size, executeBrieferVO.getOdId(), executeBrieferVO.getOdName());
        Integer total = executePutstoreMapper.executePutstorECount(executeBrieferVO.getOdId(), executeBrieferVO.getOdName());
        IPage<ExecuteBrieferVO> page = new Page();
        page.setTotal(total);
        long pages = total / size;
        if (total % size != 0) {
            pages++;
        }
        page.setPages(pages);
        page.setRecords(executeBrieferVOS);
        return page;
    }

    @Override
    public IPage<ExecutePutStoreVO> selectExecutePutStorePage(IPage page, ExecutePutStoreVO executePutStoreVO) {
        return page.setRecords(baseMapper.selectExecutePutStorePage(page,executePutStoreVO));
    }

}
