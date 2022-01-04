package com.yb.execute.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.execute.entity.ExecutePutStore;
import com.yb.execute.vo.ExecuteBrieferVO;
import com.yb.execute.vo.ExecutePutStoreVO;

import java.util.List;

public interface ExecutePutstoreService extends IService<ExecutePutStore>  {

    /*分页查询如入库上报*/
    IPage<ExecuteBrieferVO> pagePutstoreList(Integer current, Integer size, ExecuteBrieferVO executeBrieferVO);

    /**
     *自定义分页
     * @param page
     * @param executePutStoreVO
     * @return
     */
    IPage<ExecutePutStoreVO> selectExecutePutStorePage(IPage page, ExecutePutStoreVO executePutStoreVO);
}
