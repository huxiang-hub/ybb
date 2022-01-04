
package com.vim.chatapi.staff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vim.chatapi.staff.entity.StaffUscheck;
import com.vim.chatapi.staff.vo.DateModelVO;
import com.vim.chatapi.staff.vo.StaffUscheckVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
 public interface StaffUscheckMapper extends BaseMapper<StaffUscheck> {

 /**
  * 获取所有的考情记录
  * @return
  */
 List<StaffUscheck> getStaffUschecks(Integer usId, String date,Integer model);
 /**
  * 打卡
  * @param staffUscheck
  * @return
  */
 boolean saveStaffUscheck(StaffUscheck staffUscheck);

 /**
  * 下班打卡
  * @param staffUscheck
  * @return
  */
 boolean updateStaffUscheck(StaffUscheckVO staffUscheck);

 /**
  * 更新打卡记录表
  * @param staffUscheck
  * @return
  */
 boolean updateSUscheck(StaffUscheck staffUscheck);

 /**
  * 查询某个时间段的的考勤
  * @param dateModelVO
  * @return
  */
 List<StaffUscheck> getStaffUscheckByDate(DateModelVO dateModelVO);

 /**
  * 查看考勤记录
  * @param userId
  * @return
  */
 StaffUscheck getStaffUscheck(Integer userId);


 /**
  * 获取当前的考勤状态
  * @param userId
  * @param date
  * @return
  */
 StaffUscheck getStaffUscheckInfo(Integer userId,String date,Integer model);



}
