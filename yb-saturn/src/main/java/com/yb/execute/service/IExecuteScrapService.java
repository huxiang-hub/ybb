package com.yb.execute.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.execute.entity.ExecuteScrap;
import com.yb.execute.vo.ExecuteScrapVO;
import com.yb.execute.vo.SubmitAuditRequest;
import io.swagger.models.auth.In;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 审核清单_yb_execute_scrap 服务类
 *
 * @author BladeX
 * @since 2021-03-08
 */
public interface IExecuteScrapService extends IService<ExecuteScrap> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param executeScrap
	 * @return
	 */
	IPage<ExecuteScrapVO> selectExecuteScrapPage(IPage<ExecuteScrapVO> page, ExecuteScrapVO executeScrap);

	/**
	 * 提交审核清单
	 * @param submitAuditRequest
	 */
	void submitAudit(SubmitAuditRequest submitAuditRequest);

	/**
	 * 批量审核
	 * @param ids 审核清单ids
	 * @param usId 审核人id
	 */
	void batchAudit(List<Long> ids, Integer usId, Integer exStatus, Integer acceptUsids, String exApprove);

	List<ExecuteScrapVO> getPhoneList(Integer usId, String exStatus);

	/**
	 * 查询审核详情
	 * @param id
	 * @return
	 */
	ExecuteScrapVO getPhoneDetail(Integer id);
}
