package com.yb.workbatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.workbatch.mapper.WorkbymaMapper;
import  com.yb.workbatch.vo.WorkbymaVO;
import  com.yb.workbatch.service.IWorkbymaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * VIEW 服务实现类
 *
 * @author BladeX
 * @since 2021-01-11
 */
@Service
public class WorkbymaServiceImpl extends ServiceImpl<WorkbymaMapper, WorkbymaVO> implements IWorkbymaService {

	@Autowired
	WorkbymaMapper workbymaMapper;

	@Override
	public IPage<WorkbymaVO> selectWorkbymaPage(IPage<WorkbymaVO> page, WorkbymaVO workbyma) {
		return page.setRecords(baseMapper.selectWorkbymaPage(page, workbyma));
	}

	@Override
	public List<WorkbymaVO> getAllList(WorkbymaVO workbymaVO) {
		return workbymaMapper.getAllList(workbymaVO);
	}

}
