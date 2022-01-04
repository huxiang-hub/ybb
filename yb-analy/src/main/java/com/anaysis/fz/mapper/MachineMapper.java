package com.anaysis.fz.mapper;

import com.anaysis.entity.MMSMachineEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MachineMapper {
    //erp的存储过程内容
    int save(MMSMachineEntity mmsMachineEntity);
}
