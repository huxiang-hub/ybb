package com.yb.execute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.execute.entity.ExecuteScrap;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.vo.ExecuteScrapVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审核清单_yb_execute_scrap Mapper 接口
 *
 * @author BladeX
 * @since 2021-03-08
 */
public interface ExecuteScrapMapper extends BaseMapper<ExecuteScrap> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param executeScrap
	 * @return
	 */
	List<ExecuteScrapVO> selectExecuteScrapPage(IPage page, ExecuteScrapVO executeScrap);

    List<ExecuteScrapVO> getPhoneList(Integer usId, String exStatus);

	/**
	 * 查询审核详情
	 * @param id
	 * @return
	 */
	ExecuteScrapVO getPhoneDetail(@Param("id") Integer id);
}
