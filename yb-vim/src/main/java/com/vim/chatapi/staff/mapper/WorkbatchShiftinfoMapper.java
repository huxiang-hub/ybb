
package com.vim.chatapi.staff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vim.chatapi.staff.entity.WorkbatchShiftinfo;
import com.vim.chatapi.staff.vo.WorkbatchShiftinfoVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
 public interface WorkbatchShiftinfoMapper extends BaseMapper<WorkbatchShiftinfo> {
 /**
  * 查看班次信息
  * @param userId
  * @return
  */
 WorkbatchShiftinfoVO getWorkbatchShiftinfoVO(Integer userId, String date);

 /**
  * 保存打卡记录
  * @return
  */
 int saveWorkbatchShiftinfo(WorkbatchShiftinfo workInfo);
 /**
  * 打卡记录
  * @return
  */
 int updateWorkbatchShiftinfo(WorkbatchShiftinfo workInfo);

}
