package com.anaysis.sqlservermapper;

import com.anaysis.entity.BaseStaffinfo;
import com.anaysis.entity.BladeUser;
import com.anaysis.entity.HrPerson;
import com.anaysis.entity.ShiftMachine;
import com.anaysis.service.impl.BladeUserServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 10:14
 **/
@Mapper
public interface HrPersonMapper {
    /** 查询人事档案 */
    List<BaseStaffinfo> list();

    @Select("SELECT ObjID FROM HRPersonProf where Status = '在职'")
    List<String> getAllErpId();

    List<HrPerson> getByIds(List<String> erpIds);

    List<BladeUser> getDeptStaff();

    @Select("SELECT ShiftID AS shift, m.ObjID AS maErpId FROM WorkShiftID a LEFT JOIN MACHINES m ON a.Machine = m.MachCode WHERE m.ObjID is not null")
    List<ShiftMachine> getClassInfoMachine();

    @Select("SELECT StaffName AS realName,ShiftID AS shift FROM WorkStaff WHERE StaffName NOT IN (SELECT PersonName FROM HRPersonProf)")
    List<BladeUser> getWorkStaffNotInHrperson();

    @Select("SELECT StaffName AS realName,ShiftID AS shift FROM WorkStaff WHERE StaffName IN (SELECT PersonName FROM HRPersonProf)")
    List<BladeUser> getWorkStaffInHrperson();
}
