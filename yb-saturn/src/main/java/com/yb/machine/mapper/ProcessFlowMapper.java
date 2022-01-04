package com.yb.machine.mapper;

import com.yb.machine.request.ProcessFlowPageRequest;
import com.yb.machine.response.ProcessFlowVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessFlowMapper extends BaseMapper<ProcessFlowVO> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param request
	 * @return
	 */
	List<ProcessFlowVO> page(@Param("page") IPage page, @Param("request") ProcessFlowPageRequest request);

}
