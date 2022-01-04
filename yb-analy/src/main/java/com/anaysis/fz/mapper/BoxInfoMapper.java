package com.anaysis.fz.mapper;

import com.anaysis.entity.BoxInfoEntity;
import com.anaysis.entity.ErpMachineEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoxInfoMapper {
    List<BoxInfoEntity> getbymac(@Param("mac") String mac);
    List<BoxInfoEntity> getlist();
    int update(BoxInfoEntity boxInfoEntity);
    int save(BoxInfoEntity boxInfoEntity);

    ErpMachineEntity getByBno(String bno);
}
