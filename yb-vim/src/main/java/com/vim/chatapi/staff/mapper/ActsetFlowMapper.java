
package com.vim.chatapi.staff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vim.chatapi.staff.entity.ActsetFlow;
import com.vim.chatapi.staff.vo.ActsetFlowVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
 public interface ActsetFlowMapper extends BaseMapper<ActsetFlow> {
 /***
  *开始申请请假时请求上级审核人信息
  * @return
  */
 ActsetFlowVO startApplyLeave(Integer falg);


}
