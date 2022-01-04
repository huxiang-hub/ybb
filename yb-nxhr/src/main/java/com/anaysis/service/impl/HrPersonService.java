package com.anaysis.service.impl;

import com.anaysis.entity.BaseStaffinfo;
import com.anaysis.entity.BladeUser;
import com.anaysis.entity.HrPerson;
import com.anaysis.entity.ShiftMachine;
import com.anaysis.sqlservermapper.HrPersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 10:23
 **/
@Service
public class HrPersonService {
    @Autowired
    private HrPersonMapper personMapper;

    public List<BaseStaffinfo> list() {
        return personMapper.list();
    }

    public List<String> getAllErpId() {
        return personMapper.getAllErpId();
    }

    public List<HrPerson> getByIds(List<String> addErpIds) {
        return personMapper.getByIds(addErpIds);
    }

    public List<BladeUser> getDeptStaff() {
        return personMapper.getDeptStaff();
    }

    public List<ShiftMachine> getClassInfoMachine() {
        return personMapper.getClassInfoMachine();
    }

    public List<BladeUser> getWorkStaffNotInHrperson() {
        return personMapper.getWorkStaffNotInHrperson();
    }

    public List<BladeUser> getWorkStaffInHrperson() {
        return personMapper.getWorkStaffInHrperson();
    }
}
