package com.yb.quality.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.quality.entity.QualityItemset;
import com.yb.quality.entity.QualityTacitly;
import com.yb.quality.request.QualityRequest;
import com.yb.quality.vo.ProcessClassifyVo;
import com.yb.quality.vo.QualityBaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface QualityItemsetMapper extends BaseMapper<QualityItemset> {
    /**
     * 先删除表
     * @param tableName
     */
    Integer dropTable(@Param("tableName") String tableName);

    /**
     * 创建表
     *
     * @param tableName
     * @param split
     */
    void creatTable(@Param("tableName")String tableName, @Param("split") String[] split);

    /**
     * 根基工序分类和检查类型查询表名
     * @param pyId
     * @param checkType
     * @return
     */
    String getInsertTableName(@Param("pyId") Integer pyId, @Param("checkType") String checkType);

    List<String> getInsertTableNames(List<Integer> pyIds, @Param("checkType") String checkType);

    /**
     * 新增表记录
     * @param tableName
     * @param fieldNameList
     * @param valList
     * @return
     */
    Integer insetTable(@Param("tableName")String tableName,
                       @Param("fieldNameList") List<String> fieldNameList,
                       @Param("valList") List<Object> valList);

    /**
     * 查询表记录
     * @param tableName
     * @return
     */
    List<Map> getTableList(@Param("tableName") String tableName,
                           @Param("qualityRequest") QualityRequest qualityRequest,
                           @Param("page")IPage<Map> page);


    List<QualityBaseVO> getTableListByTables(@Param("tableNames") List<String> tableNames,
                                             @Param("qualityRequest") QualityRequest qualityRequest,
                                             @Param("page")IPage<QualityBaseVO> page);
    /**
     * 增加表字段
     * @param tableName
     * @param colName
     * @param colShow
     * @return
     */
    Integer addTableCols(@Param("tableName") String tableName, @Param("colName") String colName, @Param("colShow") String colShow);

    /**
     * 添加默认字段相关信息
     * @param qualityItemset
     * @return
     */
    Integer insertQualityItemset(QualityItemset qualityItemset);

    /**
     * 根据检查类型和工序分类查询所需的检查项
     * @param pyId 工序分类id
     * @param checkType 检查类型
     * @return
     */
    List<QualityItemset> getQualityItemsetList(@Param("pyId") Integer pyId, @Param("checkType") String checkType);

    /**
     * 验证存入的字段在表中是否存在
     * @param tableName
     * @param colName
     * @return
     */
    Integer verifyTheField(@Param("tableName") String tableName, @Param("colName") String colName);

    /**
     * 修改自定义质检表数据
     * @param tableName
     * @param insertTableMap
     * @param id
     * @return
     */
    Integer updateTable(@Param("tableName") String tableName,
                        @Param("insertTableMap") Map<String, Object> insertTableMap,
                        @Param("id") Integer id);

    /**
     * 查询工序类型表获取信息及关联设备的ID
     * */
    List<ProcessClassifyVo> processList();

    /**
     * 通过py_id查询设备集合
     * */
    List<MachineMainfoVO> getMachins(@Param("pyId") Integer pyId);

    /**
     * 查询自定义新增表数据详情
     * @param tabName
     * @param id
     * @return
     */
    Map selectTableDetail(@Param("tabName") String tabName, @Param("id") Integer id);
}
