package com.anaysis.sqlservermapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.nxhr.param.GoodProdInsertParam;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface HrProcedureMapper {

    /**
     * 调用正品上报储存过程
     * @param param
     * @return
     */
    Map<String, String> goodProdInsert(@Param("param") GoodProdInsertParam param);

    /**
     * 新增修改存储过程调用 (注:输出参数只有ReturnMsg可用)
     * @param buildParam 调用参数(需注意顺序:按所需参数的顺序)
     * @param procedureName 存储过程名称
     * @return
     */
    Map<String, String> execProcedure(@Param("buildParam") Map<String, String> buildParam,
                                      @Param("procedureName") String  procedureName);

    /**
     * 成品入库存储过程调用
     * @param buildParam 调用参数(需注意顺序:按所需参数的顺序)
     * @param procedureName 存储过程名称
     * @return
     */
    Map<String, String> storeFinishedProdInsert(@Param("param") Map<String, String> buildParam,
                                                @Param("procedureName") String procedureName);
}
