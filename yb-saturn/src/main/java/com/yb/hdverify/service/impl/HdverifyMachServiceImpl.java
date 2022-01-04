package com.yb.hdverify.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.hdverify.entity.HdverifyMach;
import com.yb.hdverify.mapper.HdverifyMachMapper;
import com.yb.hdverify.service.IHdverifyMachService;
import com.yb.hdverify.vo.HdverifyMachVO;
import com.yb.supervise.vo.SuperviseBoxinfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
@Service
public class HdverifyMachServiceImpl extends ServiceImpl<HdverifyMachMapper, HdverifyMach> implements IHdverifyMachService {

    @Autowired
    private HdverifyMachMapper hdverifyMachMapper;
    @Override
    public List<HdverifyMachVO> getHdverifyMachList(HdverifyMachVO machVO) {

        return baseMapper.getHdverifyMachList(machVO);
    }

    @Override
    public List<SuperviseBoxinfoVO> getMachList() {
        List<SuperviseBoxinfoVO> superviseBoxinfoVOList = hdverifyMachMapper.getMachList();//查询所有已绑定盒子的设备
        //查询正在验证的设备
        List<HdverifyMach> hdverifyMachList = hdverifyMachMapper.selectList(new QueryWrapper<HdverifyMach>().eq("status", 2));
        Iterator<SuperviseBoxinfoVO> iterator = superviseBoxinfoVOList.iterator();
        if(!hdverifyMachList.isEmpty()){
            while (iterator.hasNext()){
                SuperviseBoxinfoVO superviseBoxinfoVO = iterator.next();
                for(HdverifyMach HdverifyMach : hdverifyMachList){
                    if(HdverifyMach.getMaId().equals(superviseBoxinfoVO.getMaId())){
                        iterator.remove();//如果正在验证则删除
                        continue;
                    }
                }
            }
        }
        return superviseBoxinfoVOList;
    }
}
