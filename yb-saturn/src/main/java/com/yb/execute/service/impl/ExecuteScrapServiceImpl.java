package com.yb.execute.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BasePicture;
import com.yb.base.mapper.BasePictureMapper;
import com.yb.dingding.entity.DingProcessinstanceCreate;
import com.yb.dingding.entity.ProcessinstanceCreateParam;
import com.yb.dingding.service.DingProcessService;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteExamine;
import com.yb.execute.entity.ExecuteScrap;
import com.yb.execute.entity.ExecuteSpverify;
import com.yb.execute.mapper.ExecuteScrapMapper;
import com.yb.execute.mapper.ExecuteSpverifyMapper;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.service.IExecuteExamineService;
import com.yb.execute.service.IExecuteScrapService;
import com.yb.execute.service.IExecuteSpverifyService;
import com.yb.execute.vo.ExecuteScrapVO;
import com.yb.execute.vo.SubmitAuditRequest;
import com.yb.statis.utils.FileUtils;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.WorkbatchShiftService;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 审核清单_yb_execute_scrap 服务实现类
 *
 * @author BladeX
 * @since 2021-03-08
 */
@Service
public class ExecuteScrapServiceImpl extends ServiceImpl<ExecuteScrapMapper, ExecuteScrap> implements IExecuteScrapService {

	@Autowired
	private IExecuteScrapService executeScrapService;
	@Autowired
	private IExecuteBrieferService executeBrieferService;
	@Autowired
	private WorkbatchShiftService workbatchShiftService;
	@Autowired
	private IWorkbatchOrdlinkService workbatchOrdlinkService;
	@Autowired
	private IExecuteExamineService executeExamineService;
	@Autowired
	private IExecuteSpverifyService executeSpverifyService;
	@Autowired
	private ExecuteScrapMapper executeScrapMapper;
	@Autowired
	private DingProcessService dingProcessService;
	@Autowired
	private ExecuteSpverifyMapper executeSpverifyMapper;
	@Autowired
	BasePictureMapper basePictureMapper;
	@Autowired
	private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
	@Autowired
	private HttpServletRequest req;

	@Override
	public IPage<ExecuteScrapVO> selectExecuteScrapPage(IPage<ExecuteScrapVO> page, ExecuteScrapVO executeScrap) {
		return page.setRecords(baseMapper.selectExecuteScrapPage(page, executeScrap));
	}

	@Override
	public void submitAudit(SubmitAuditRequest submitAuditRequest) {

		Integer bfId = submitAuditRequest.getBfId();
		Integer spMold = submitAuditRequest.getSpMold();
		String spOther = submitAuditRequest.getSpOther();
		String spReason = submitAuditRequest.getSpReason();
		Integer usId = submitAuditRequest.getUsId();
		ExecuteBriefer briefer = executeBrieferService.getById(bfId);
		WorkbatchShift workbatchShift = workbatchShiftService.getById(briefer.getWfId());
		WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectById(briefer.getSdId());

		Map<String, Integer> sumMap = executeExamineService.getSumByBfId(bfId, spMold.toString());
		Integer dataBeforeSum = sumMap.get("dataBeforeSum");
		Integer dataAfterSum = sumMap.get("dataAfterSum");

		List<ExecuteExamine> executeExamines = executeExamineService.list(
				Wrappers.<ExecuteExamine>lambdaQuery()
						.eq(ExecuteExamine::getBfId, bfId)
						.eq(ExecuteExamine::getExMold, spMold)
		);
		String tyIds = executeExamines.stream().map(e -> e.getTyId().toString()).collect(Collectors.joining(","));

		ExecuteScrap executeScrap = new ExecuteScrap();
		executeScrap.setWbNo(workbatchOrdlink.getWbNo());
		executeScrap.setSdId(briefer.getSdId());
		executeScrap.setWfId(briefer.getWfId());
		executeScrap.setExId(briefer.getExId());
		executeScrap.setWsId(workbatchShift.getWsId());
		executeScrap.setSpBefore(dataBeforeSum);
		executeScrap.setSpAfter(dataAfterSum);
		executeScrap.setTyIds(tyIds);
		executeScrap.setImagIds(submitAuditRequest.getImgIds());

		executeScrap.setSpMold(spMold);
		executeScrap.setExDesc("提交");
		executeScrap.setSpNum(Math.abs(dataAfterSum - dataBeforeSum));
		executeScrap.setSpReason(spReason);
		executeScrap.setSpOther(spOther);
		executeScrap.setUsId(usId);
		executeScrap.setCreateAt(LocalDateTime.now());
		executeScrapService.save(executeScrap);
		FileUtils.updatePic(executeScrap.getId(), "2", Func.toIntList(submitAuditRequest.getImgIds()));
//		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);//设置子线程共享
//		new Thread(() -> {
			String templateName = submitAuditRequest.getTemplateName();
			if(StringUtil.isEmpty(templateName)){//设置一个默认值
				templateName = "半成品审核";
			}
			String url = submitAuditRequest.getUrl();
			String rowName = submitAuditRequest.getRowName();
			Map<String, String> map = new HashMap<>();
			if(StringUtil.isEmpty(rowName)){//设置表格默认值:url加清单id
				StringBuffer requestURL = req.getRequestURL();
				String dindUrl = requestURL.substring(0, requestURL.indexOf("satapi"));
				map.put("审核详情路径", dindUrl + "vim/#/pages/error/error_detail?id=" + executeScrap.getId());
			}else {
				map.put(rowName, url + "?id=" + executeScrap.getId());
			}
			String userId = submitAuditRequest.getUserId();//用户钉钉id
			ProcessinstanceCreateParam processinstanceCreateParam = new ProcessinstanceCreateParam();
			processinstanceCreateParam.setMap(map);
			processinstanceCreateParam.setTemplateName(templateName);
			processinstanceCreateParam.setUserId(userId);
			DingProcessinstanceCreate dingProcessinstanceCreate = dingProcessService.processinstanceCreate(processinstanceCreateParam);
			ExecuteSpverify executeSpverify = new ExecuteSpverify();
			executeSpverify.setSpId(executeScrap.getId());
			executeSpverify.setExStatus(1);
			executeSpverify.setProcessInstanceId(dingProcessinstanceCreate.getProcessInstanceId());
			executeSpverify.setCreateAt(LocalDateTime.now());
			executeSpverifyMapper.insert(executeSpverify);
//		}).start();
	}

	@Override
	public void batchAudit(List<Long> ids, Integer usId, Integer exStatus, Integer acceptUsids, String exApprove) {
		for (Long id : ids) {
			ExecuteSpverify executeSpverify = new ExecuteSpverify();
			executeSpverify.setExTime(LocalDateTime.now());
			executeSpverify.setCreateAt(LocalDateTime.now());
			executeSpverify.setAcceptUsids(acceptUsids.toString());
			executeSpverify.setExStatus(exStatus);
			executeSpverify.setSpId(id.intValue());
			executeSpverify.setExApprove(exApprove);
			executeSpverify.setExOperator(usId);
			executeSpverifyService.save(executeSpverify);
		}
	}

	@Override
	public List<ExecuteScrapVO> getPhoneList(Integer usId, String exStatus) {
		List<ExecuteScrapVO> list = executeScrapMapper.getPhoneList(usId, exStatus);
		for (ExecuteScrapVO executeScrapVO : list) {
			List<BasePicture> basePictures = basePictureMapper.selectList(Wrappers.<BasePicture>lambdaQuery().eq(BasePicture::getBtId, executeScrapVO.getId()).eq(BasePicture::getBtType, 2));
			executeScrapVO.setBasePictureList(basePictures);
		}
		return list;
	}

	@Override
	public ExecuteScrapVO getPhoneDetail(Integer id) {
		ExecuteScrapVO executeScrapVO = executeScrapMapper.getPhoneDetail(id);
		if(executeScrapVO != null){
			List<BasePicture> basePictures = basePictureMapper.selectList(Wrappers.<BasePicture>lambdaQuery().eq(BasePicture::getBtId, id).eq(BasePicture::getBtType, 2));
			executeScrapVO.setBasePictureList(basePictures);
		}
		return executeScrapVO;
	}

}
