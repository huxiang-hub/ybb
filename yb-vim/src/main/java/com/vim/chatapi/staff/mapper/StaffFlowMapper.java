
package com.vim.chatapi.staff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vim.chatapi.staff.entity.StaffFlow;
import org.apache.ibatis.annotations.Mapper;

@Mapper
 public interface StaffFlowMapper extends BaseMapper<StaffFlow> {

 /***
  * 申请提交后插入 dayflow表的数据  考勤通用
  * @param staffFlow
  * @return
  */
 Integer saveStaffFlow(StaffFlow staffFlow);
 /**
  * 修改是审核的状态
  */
 boolean updateStaffFlow(StaffFlow staffFlow);




}
