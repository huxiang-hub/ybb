package com.yb.machine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.machine.entity.MachineStafflink;
import com.yb.machine.request.MachineAuthorizationSelectUserRequest;
import com.yb.machine.response.EquipmentUserPageVo;
import com.yb.machine.response.getEquipmentVo;
import com.yb.machine.vo.UserFaceVO;
import com.yb.system.user.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MachineStafflinkMapper extends BaseMapper<MachineStafflink> {
    /**
     *设备授权用户集合
     **/
    IPage<UserPageVo> page(IPage<UserPageVo> page, @Param("request") MachineAuthorizationSelectUserRequest request);

    /**
     *设备已授权用户
     **/
    List<EquipmentUserPageVo> getEquimentUserList(IPage<EquipmentUserPageVo> page, @Param("equipmentVO") getEquipmentVo equipmentVO);

    /**
     *删除设备已授权用户
     **/
    void deleteEquipmentUser(@Param("equipment") getEquipmentVo equipment);

    /**
     *授权
     **/
    void equipmentAuthor( @Param("roleAuthors") List<RoleAuthor> roleAuthors);

    /**
     *获得班组信息
     * */
    List<TeamVo> teamInformation();

    /**
     * 通过maId查询UsId
     * */
    int selectUsId(int usId,int maId);

    /********************勿删，机台接口代码******************************/
    /**
     * 根据设备id查询人员信息
     * @param maId
     * @return
     */
    List<UserFaceVO> getUsersByMaId(@Param("maId") Integer maId);

}
