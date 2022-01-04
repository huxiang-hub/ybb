package com.screen.execute.service;

import com.screen.execute.vo.WorkbatchShiftProcessVO;
import com.screen.execute.vo.WorkbatchShiftVO;

import java.util.List;

public interface WorkbatchShiftService {

    List<WorkbatchShiftVO> getWorkShiftList(Integer maId);

    WorkbatchShiftProcessVO getShiftDetail(Integer wfId);

}
