package com.anaysis.sqlservermapper;


import com.anaysis.entity.SaUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FzSaUserMapper {
    List<SaUser> list();

    List<SaUser> getUserList();
}
