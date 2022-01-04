package com.yb.execute.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BasePicture;
import com.yb.base.mapper.BasePictureMapper;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteScrap;
import com.yb.execute.mapper.ExecuteBrieferMapper;
import com.yb.execute.mapper.ExecuteScrapMapper;
import com.yb.execute.mapper.ExecuteSpverifyMapper;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.vo.*;
import com.yb.prod.vo.ProdPartsinfoVo;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 生产执行上报信息_yb_execute_briefer 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExecuteBrieferServiceImpl extends ServiceImpl<ExecuteBrieferMapper, ExecuteBriefer> implements IExecuteBrieferService {

    @Autowired
    private ExecuteBrieferMapper brieferMapper;
    @Autowired
    private BasePictureMapper basePictureMapper;
    @Autowired
    private ExecuteSpverifyMapper executeSpverifyMapper;

    @Override
    public IPage<ExecuteBrieferVO> selectExecuteBrieferPage(IPage<ExecuteBrieferVO> page, ExecuteBrieferVO executeBriefer) {
        return page.setRecords(baseMapper.selectExecuteBrieferPage(page, executeBriefer));
    }

    @Override
    public IPage<ExecuteBrieferVO> pageFindList(Integer current, Integer size, ExecuteBrieferVO executeBrieferVO) {
        List<ExecuteBrieferVO> listExecuteBrieferVO = brieferMapper.pageFindList(current, size, executeBrieferVO.getOdId(), executeBrieferVO.getOdName());
        Integer count = brieferMapper.executeBrieferCount(executeBrieferVO.getOdId(), executeBrieferVO.getOdName());

        IPage<ExecuteBrieferVO> page = new Page();
        page.setTotal(count);
        return page.setRecords(listExecuteBrieferVO);
    }

    @Override
    public ExecuteBriefer getStartTimeByCondition(Integer exId) {
        return brieferMapper.getStartTimeByCondition(exId);
    }

    /**
     * 查询对应作业批次的对应部件生产状况
     */
    @Override
    public List<ExecuteBriefer> detailBatchNo(ProdPartsinfoVo partsinfoVo) {
        return brieferMapper.detailBatchNo(partsinfoVo);
    }

    @Override
    public List<ExecuteBriefer> getNumberByExId(List<Integer> exIds) {

        return brieferMapper.getNumberByExId(exIds);
    }

    @Override
    public int updateCheck(ExecuteBriefer briefer) {
        return brieferMapper.updateCheck(briefer);
    }

    @Override
    public Integer getTotalByWfid(Integer wfid) {
        return brieferMapper.getTotalByWfid(wfid);
    }

    @Override
    public Integer getTotalBySdid(Integer sdid) {
        return brieferMapper.getTotalBySdid(sdid);
    }

    @Override
    public List<ReportedVO> getExecuteBrieferList(String targetDay, Integer maId, Integer wsId) {
        List<ReportedVO> reportedVOList = brieferMapper.getExecuteBrieferList(targetDay, maId, wsId);
        for (ReportedVO re : reportedVOList) {
            Integer exId = re.getExId();
            if (exId != null) {
                List<ProcessInstanceVO> processInstanceList = executeSpverifyMapper.getByExId(exId);
                re.setProcessInstanceIdList(processInstanceList);
            }
        }
        return reportedVOList;
    }

    @Override
    public ExecuteBrieferDetailVO getExecuteBrieferDetail(Integer id) {
        ExecuteBrieferDetailVO executeBrieferDetail = brieferMapper.getExecuteBrieferDetail(id);
        /*查询上报历史数据的托盘清单*/
        List<TraycardDetailedVO> traycardDetailedVOList = brieferMapper.traycardDetailedVOList(id);
        traycardDetailedVOList.forEach(
                e -> {
                    if (Func.isNotBlank(e.getExPics())) {
                        String exPics = e.getExPics();
                        String picsIds = exPics.replace("|", ",");
                        List<BasePicture> basePictureList = basePictureMapper.selectBatchIds(Func.toIntList(picsIds));
                        StringBuffer stringBuffer = new StringBuffer();
                        basePictureList.forEach(basePicture -> {
                            String picUrl = basePicture.getPicUrl();
                            stringBuffer.append(picUrl).append(",");
                        });
                        e.setExPics(stringBuffer.substring(0, stringBuffer.length() - 1));
                    }
                });
        executeBrieferDetail.setTraycardDetailedVOList(traycardDetailedVOList);
        return executeBrieferDetail;
    }

    @Override
    public IPage<ExecuteExamineVO> notExecuteBrieferList(NotExecuteBrieferRequest notExecuteBrieferRequest, IPage<ExecuteExamineVO> page) {
        String startTime = notExecuteBrieferRequest.getStartTime();
        if (StringUtil.isEmpty(startTime)) {
            notExecuteBrieferRequest.setStartTime(DateUtil.refNowDay());
        }
        List<ExecuteExamineVO> executeExamineVOList = brieferMapper.notExecuteBrieferList(notExecuteBrieferRequest, page);
        return page.setRecords(executeExamineVOList);
    }
}
