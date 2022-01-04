package com.yb.machine.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.machine.entity.MachineStafflink;
import com.yb.machine.mapper.MachineStafflinkMapper;
import com.yb.machine.request.MachineAuthorizationSelectUserRequest;
import com.yb.machine.response.EquipmentUserPageVo;
import com.yb.machine.response.getEquipmentVo;
import com.yb.machine.service.MachineStafflinkService;
import com.yb.machine.vo.UserFaceVO;
import com.yb.system.role.entity.Role;
import com.yb.system.role.mapper.SaRoleMapper;
import com.yb.system.user.response.*;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MachineStafflinkServiceImpl extends ServiceImpl<MachineStafflinkMapper, MachineStafflink> implements MachineStafflinkService {
    @Autowired
    private MachineStafflinkMapper machineStafflinkMapper;

    @Autowired
    private SaRoleMapper saRoleMapper;


    @Override
    public IPage<UserPageVo> page(IPage<UserPageVo> page, MachineAuthorizationSelectUserRequest request) {
        return machineStafflinkMapper.page(page,request);
    }

    @Override
    public IPage<EquipmentUserPageVo> getEquimentUserList(IPage<EquipmentUserPageVo> page, getEquipmentVo equipmentVO) {
        List<EquipmentUserPageVo> vo = machineStafflinkMapper.getEquimentUserList(page,equipmentVO);
        if (vo.isEmpty()) {
            return page.setRecords(new ArrayList<>());
        }
        List<Role> role = saRoleMapper.selectList(null);
        vo.forEach(o -> {
            String roleIds = o.getRoleIds();
            if (Func.isNotBlank(roleIds)) {
                List<String> ids = Arrays.asList(o.getRoleIds().split(","));
                List<Role> roles = role.stream().filter(b -> {
                    if (ids.contains(String.valueOf(b.getId()))) {
                        return true;
                    } else {
                    }
                    return false;
                }).collect(Collectors.toList());
                o.setRole(roles);
            }
        });
        return page.setRecords(vo);
    }

    @Override
    public void deleteEquipmentUser(getEquipmentVo equipmentVO) {
        if(equipmentVO.getUsIdList().size()>0&& equipmentVO.getUsIdList()!=null && equipmentVO.getMaNameList()!=null && equipmentVO.getMaNameList().size()!=0){
            machineStafflinkMapper.deleteEquipmentUser(equipmentVO);
        }
    }

    @Override
    public void equipmentAuthor(@Param("roleAuthors") List<RoleAuthor> roleAuthors) {
        if(roleAuthors.size()>0){
            machineStafflinkMapper.equipmentAuthor(roleAuthors);
        }
    }

    @Override
    public List<TeamVo> teamInformation() {
        return machineStafflinkMapper.teamInformation();
    }

    /********************勿删，机台接口代码******************************/

    @Override
    public List<UserFaceVO> getUsersByMaId(Integer maId) {
        return machineStafflinkMapper.getUsersByMaId(maId);
    }
}
