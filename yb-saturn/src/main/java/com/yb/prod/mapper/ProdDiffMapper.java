package com.yb.prod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.process.vo.ProcessClassifyVO;
import com.yb.prod.entity.ProdClassify;
import com.yb.prod.entity.ProdDiff;
import com.yb.prod.vo.ProdDiffVO;

import java.util.List;

/**
 * 产品分类信息
 */
public interface ProdDiffMapper extends BaseMapper<ProdDiff> {

    /**
     * 自定义分页
     *
     * @param page
     * @param prodDiffVO
     * @return
     */
    List<ProdDiffVO> getAllProcdiff(IPage page, ProdDiffVO prodDiffVO);

    /**
     * 查询详情
     * @return
     */
    ProdDiffVO detail(Integer id);
}
