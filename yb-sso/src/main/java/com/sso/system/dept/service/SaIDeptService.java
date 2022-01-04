package com.sso.system.dept.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.base.entity.BaseDeptinfo;
import com.sso.system.dept.vo.SaDeptVO;

import java.util.List;

/**
 * 服务类
 *
 * @author Chill
 */
public interface SaIDeptService extends IService<BaseDeptinfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param dept
	 * @return
	 */
	IPage<SaDeptVO> selectDeptPage(IPage<SaDeptVO> page, SaDeptVO dept);

	/**
	 * 树形结构
	 *
	 * @param tenantId
	 * @return
	 */
	List<SaDeptVO> tree(String tenantId);

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @return
	 */
	IPage<BaseDeptinfo> selectDeptPages(IPage<BaseDeptinfo> page, BaseDeptinfo baseDeptinfo);

}
