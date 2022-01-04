package com.yb.machine.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.machine.entity.MachineStafflink;
import com.yb.machine.request.MachineAuthorizationSelectUserRequest;
import com.yb.machine.response.EquipmentUserPageVo;
import com.yb.machine.response.getEquipmentVo;
import com.yb.machine.vo.UserFaceVO;
import com.yb.system.user.response.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MachineStafflinkService extends IService<MachineStafflink> {
    /**
     *设备授权用户集合
     **/
    IPage<UserPageVo> page(IPage<UserPageVo> page, @Param("request") MachineAuthorizationSelectUserRequest request);

    /**
     *设备已授权用户
     **/
    IPage<EquipmentUserPageVo> getEquimentUserList(IPage<EquipmentUserPageVo> page, @Param("equipmentVO") getEquipmentVo equipmentVO);

    /**
     *删除设备已授权用户
     **/
    void deleteEquipmentUser(@Param("equipmentVO") getEquipmentVo equipmentVO);

    /**
     *授权
     **/
    void equipmentAuthor(@Param("roleAuthors") List<RoleAuthor> roleAuthors);

    /**
     *获得班组信息
     * */
    List<TeamVo> teamInformation();

    /********************勿删，机台接口代码******************************/
    /**
     * 根据设备id查询人员信息
     * @param maId
     * @return
     */
    List<UserFaceVO> getUsersByMaId(Integer maId);
}
