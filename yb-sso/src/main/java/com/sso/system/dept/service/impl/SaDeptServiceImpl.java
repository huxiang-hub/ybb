package com.sso.system.dept.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.base.entity.BaseDeptinfo;
import com.sso.mapper.SaDeptMapper;
import com.sso.system.dept.service.SaIDeptService;
import com.sso.system.dept.vo.SaDeptVO;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class SaDeptServiceImpl extends ServiceImpl<SaDeptMapper, BaseDeptinfo> implements SaIDeptService {

	@Override
	public IPage<SaDeptVO> selectDeptPage(IPage<SaDeptVO> page, SaDeptVO dept) {
		return page.setRecords(baseMapper.selectDeptPage(page, dept));
	}

	@Override
	public List<SaDeptVO> tree(String tenantId) {
		return ForestNodeMerger.merge(baseMapper.tree(tenantId));
	}

	@Override
	public IPage<BaseDeptinfo> selectDeptPages(IPage<BaseDeptinfo> page, BaseDeptinfo baseDeptinfo) {
		return page.setRecords(baseMapper.selectDeptPages(page, baseDeptinfo));
	}

}
