
package com.vim.chatapi.staff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vim.chatapi.staff.entity.StaffDayoff;
import com.vim.chatapi.staff.vo.StaffDayoffVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
 public interface StaffDayoffMapper extends BaseMapper<StaffDayoff> {

 /***
  * 向yb_staff_dayoff 插入申请人的信息
  */
 Integer saveStaffDayoff(StaffDayoff staff);
 /***
  *
  *审核人审批申请
  */
 List<StaffDayoffVO> checkApplyDayoff(Integer userId, Integer status);
 /**
  * 修改请假人的 请假申请状态
  */
 boolean updateStaffDayoff(StaffDayoff staffDayoff);

 /**
  * 获取所有的请假记录
  * @param userId
  * @return
  */
 List<StaffDayoff> getStaffDayoff(Integer userId,Integer status);

 /**
  * 请假详情
  * @param id
  * @return
  */
 StaffDayoff getStaffDayoffById(Integer id);

}
