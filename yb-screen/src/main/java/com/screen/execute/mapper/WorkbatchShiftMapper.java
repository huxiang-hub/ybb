package com.screen.execute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.screen.execute.vo.WorkbatchOrdlinkVO;
import com.screen.execute.vo.WorkbatchShiftProcessVO;
import com.screen.execute.vo.WorkbatchShiftVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkbatchShiftMapper extends BaseMapper<WorkbatchShiftVO> {

    /****
     * 根据设备id信息和开始时间和结束时间返回排程对应数据信息
     * @param maId
     * @param startDate
     * @param endDate
     * @return
     */
    List<WorkbatchShiftVO> getWorkShiftList(Integer maId,String startDate,String endDate);

    /*****
     * 根据排程wfId获取对应的工艺详情路线内容
     * @param wfId
     * @return
     */
    WorkbatchShiftProcessVO getProcessDetail(Integer wfId);

    /*****
     * 根据排产单获取上道工序的的orlink对象信息
     * @param wfId
     * @return
     */
    WorkbatchOrdlinkVO getUpProcess(Integer wfId);
}
