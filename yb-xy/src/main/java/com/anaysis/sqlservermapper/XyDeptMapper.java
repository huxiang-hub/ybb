package com.anaysis.sqlservermapper;

import com.anaysis.entity.BaseDeptInfo;
import com.anaysis.entity.BaseDeptInfoVo;
import com.anaysis.entity.SaUser;
import org.springblade.saturn.entity.BaseDeptinfo;

import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/6/14 10:46
 */
public interface XyDeptMapper {
    List<BaseDeptInfoVo> list();
}
