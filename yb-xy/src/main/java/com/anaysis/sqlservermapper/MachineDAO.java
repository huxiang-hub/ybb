package com.anaysis.sqlservermapper;

import com.anaysis.entity.MMSMachineEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MachineDAO {
    int save(MMSMachineEntity mmsMachineEntity);
}
