package com.anaysis.sqlservermapper;

import com.anaysis.entity.*;
import com.anaysis.sqlservermapper.domain.XyMachineDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface XyMachineDao {
    List<XyMachineDO> list(Map<String, Object> map);

   List<MachineMainfoVO> getMachineList();

   List<MachineClassify> getMachineClassify();

    List<ProcessMachlinkVO> getProcessMachlink(String maId);
}
