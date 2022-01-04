package com.anaysis.sqlservermapper;

import com.anaysis.entity.MachineMainfo;
import com.anaysis.entity.ProcessMachlink;
import com.anaysis.sqlservermapper.domain.FzMachineDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FzMachineDao {
    List<FzMachineDO> list(Map<String, Object> map);

   List<MachineMainfo> getMachineList();

    List<ProcessMachlink> getProcessMachlink(Integer maId);
}
