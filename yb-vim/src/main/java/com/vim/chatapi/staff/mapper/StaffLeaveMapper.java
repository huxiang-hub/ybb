
package com.vim.chatapi.staff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vim.chatapi.staff.entity.StaffLeave;
import com.vim.chatapi.staff.vo.StaffLeaveVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
 public interface StaffLeaveMapper extends BaseMapper<StaffLeave> {

 /***
  *
  *审核人审批申请
  */
 List<StaffLeaveVO> checkApplyLeave(Integer userId, Integer status);

 /**
  * 保存补卡申请记录
  * @param staffLeave
  * @return
  */
 boolean saveStaffLeave(StaffLeave staffLeave);

 /**
  * 修改补卡的 申请状态
  */
 boolean updateStaffLeave(StaffLeave staffLeave);
 /**
  * 获取所有的补卡记录
  * @param userId
  * @return
  */
 List<StaffLeave> getStaffLeaves(Integer userId,Integer status);

 /**
  * 补卡详情
  * @param id
  * @return
  */
 StaffLeaveVO getStaffLeaveById(Integer id);




}
