package com.sso.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.base.entity.BaseDeptinfo;
import com.sso.system.dept.vo.SaDeptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author Chill
 */
@Mapper
public interface SaDeptMapper extends BaseMapper<BaseDeptinfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param dept
	 * @return
	 */
	List<SaDeptVO> selectDeptPage(IPage page, SaDeptVO dept);

	/**
	 * 获取树形节点
	 *
	 * @param tenantId
	 * @return
	 */
	List<SaDeptVO> tree(String tenantId);

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param baseDeptinfo
	 * @return
	 */
	List<BaseDeptinfo> selectDeptPages(IPage page, @Param("baseDeptinfo")BaseDeptinfo baseDeptinfo);
}
