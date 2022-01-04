package com.yb.quality.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.panelapi.waste.vo.QualityBfwasteVO;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.prod.entity.ProdProcelink;
import com.yb.prod.service.IProdProcelinkService;
import com.yb.quality.entity.QualityWastClass;
import com.yb.quality.mapper.QualityWastClassMapper;
import com.yb.quality.service.QualityWastClassService;
import com.yb.quality.vo.ProcessWastClassVO;
import com.yb.quality.vo.QualityWastClassVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QualityWastClassServiceImpl extends ServiceImpl<QualityWastClassMapper, QualityWastClass> implements QualityWastClassService {


    @Autowired
    private IProdProcelinkService prodProcelinkService;
    @Autowired
    private IProcessWorkinfoService processWorkinfoService;

    @Autowired
    private QualityWastClassMapper qualityWastClassMapper;


    @Override
    public IPage<QualityWastClassVO> getQualityWastClassList(IPage<QualityWastClassVO> page, QualityWastClassVO wastClassVO) {

        return page.setRecords(baseMapper.getQualityWastClassList(page, wastClassVO));
    }

    @Override
    public QualityWastClassVO getQualityWastClassById(Integer id) {
        return baseMapper.getQualityWastClassById(id);
    }

    @Override
    public List<ProcessWastClassVO> getPrBeforByPrId(Integer prId, Integer pdId) {
        ProdProcelink procelink = prodProcelinkService.getOne(
                Wrappers.<ProdProcelink>lambdaQuery()
                        .eq(ProdProcelink::getPrId, prId)
                        .eq(ProdProcelink::getPdId, pdId));
        if (null == procelink) {
            throw new RuntimeException("产品工序不存在");
        }
        List<ProdProcelink> list = prodProcelinkService.list(
                Wrappers.<ProdProcelink>lambdaQuery()
                        .eq(ProdProcelink::getPdId, pdId)
                        .le(ProdProcelink::getSortNum, procelink.getSortNum())
                        .orderByDesc(ProdProcelink::getSortNum));
        ArrayList<ProcessWastClassVO> results = new ArrayList<>();
        for (ProdProcelink prodProcelink : list) {
            if (prId == prodProcelink.getId()) {
                continue;//如果是工序相同的工序就跳过。仅仅查询本工序之前的工序对象内容
            }
            ProcessWastClassVO processWastClassVO = new ProcessWastClassVO();
            ProcessWorkinfo processWorkinfo = processWorkinfoService.getById(prodProcelink.getPrId());
            processWastClassVO.setPrId(processWorkinfo.getId());
            processWastClassVO.setPrName(processWorkinfo.getPrName());
            //去掉每个工序的工序废品分类数据信息内容。
            //processWastClassVO.setList(this.list(Wrappers.<QualityWastClass>lambdaQuery().eq(QualityWastClass::getPrId, prId)));
            results.add(processWastClassVO);
        }
        return results;
    }

    @Override
    public List<QualityBfwasteVO> getReportWastByPrid(Integer exPrid) {
        return qualityWastClassMapper.getReportWastByPrid(exPrid);
    }
}
