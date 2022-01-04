package com.yb.sysShow.mapper;

import com.yb.sysShow.entity.BoxCleanLogEntity;
import com.yb.sysShow.entity.BoxinfoViewEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author by summer
 * @date 2020/5/27.
 */
@Mapper
public interface BoxinfoViewMapper {
    List<BoxinfoViewEntity> getlist();

    BoxinfoViewEntity getUuid(String uuid);

    int cleanByuuid(String uuid);

    int cleanLog(BoxCleanLogEntity boxlog);
}
