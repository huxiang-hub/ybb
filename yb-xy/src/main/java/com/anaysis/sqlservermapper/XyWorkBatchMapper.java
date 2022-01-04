package com.anaysis.sqlservermapper;

import com.anaysis.entity.MachineMainfo;
import com.anaysis.entity.WorkbatchOrdlink;
import com.anaysis.entity.WorkbatchOrdlinkVO;
import com.anaysis.entity.tmp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface XyWorkBatchMapper {
//    查询工作中心
    List<Integer> listIGzzxId();
//    查询排产信息
    List<WorkbatchOrdlinkVO> listByIGzzxId(@Param("list") List<String> list);
//    设备查询排产
    WorkbatchOrdlink selectById(@Param("Id") Integer Id);
    //    查询排产信息
    List<WorkbatchOrdlinkVO> listByerpId(@Param("erpId") String erpId);

    //    查询排产信息
    List<String> erpAll(@Param("list") List<MachineMainfo> list);

    //创建临时表
    Integer creatTMP();
    //删除临时表
    Integer dropTMP();

    //批量插入数据
    Integer insterTMP(@Param("list") List<tmp> list);
    //查询状态修改
    List<String> selectStatusListw();

    //查询状态修改
    List<String> selectStatusListy();

    String selectByErpId(String erpId);

    // 未导入数据
    List<String> getIdsByMaIds(List<String> maIds);

    List<WorkbatchOrdlinkVO> getByIds(List<String> addIds);

    WorkbatchOrdlinkVO selectOneByErpId(String erpId);

    @Select("select SCZT from SC_WWKHDDList_SCJH where ID = #{erpId}")
    String selectStatusById(String erpId);

    List<String> getAllIdsByMaIds(List<String> maIds);
}
