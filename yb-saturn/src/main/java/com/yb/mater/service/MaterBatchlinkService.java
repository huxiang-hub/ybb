package com.yb.mater.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.mater.entity.MaterBatchlink;
import com.yb.mater.vo.MaterBatchlinkVO;

import java.util.List;

public interface MaterBatchlinkService extends IService<MaterBatchlink> {
    /**
     * 分页查询
     * @param materBatchlinkVO
     * @param page
     * @return
     */
    IPage<MaterBatchlinkVO> getpage(MaterBatchlinkVO materBatchlinkVO, IPage<MaterBatchlinkVO> page);

    /**
     * 查询所有状态信息
     * 0发起请求1部分入库2全部入库3关闭（自动关闭5天）4 手工关闭
     * @return
     */
    List<Integer> getStatus();

    /**
     * 查询未回料数据
     * @param materBatchlinkVO
     * @param page
     * @return
     */
    IPage<MaterBatchlinkVO> waitPage(MaterBatchlinkVO materBatchlinkVO, IPage<MaterBatchlinkVO> page);
}
