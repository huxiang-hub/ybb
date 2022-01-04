package com.yb.workbatch.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.workbatch.vo.WorkbymaVO;

import java.util.List;

/**
 * VIEW 服务类
 *
 * @author BladeX
 * @since 2021-01-11
 */
public interface IWorkbymaService extends IService<WorkbymaVO> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param workbyma
	 * @return
	 */
	IPage<WorkbymaVO> selectWorkbymaPage(IPage<WorkbymaVO> page, WorkbymaVO workbyma);

	List<WorkbymaVO> getAllList(WorkbymaVO workbymaVO);
}
