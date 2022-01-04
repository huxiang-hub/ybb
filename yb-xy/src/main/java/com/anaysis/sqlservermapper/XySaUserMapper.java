package com.anaysis.sqlservermapper;


import com.anaysis.entity.SaUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XySaUserMapper {
    List<SaUser> list();

    List<SaUser> getUserList();
}
