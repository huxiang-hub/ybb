package com.yb.supervise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.supervise.entity.SuperviseIntervalalg;
import com.yb.supervise.vo.SuperviseIntervalalgEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperviseIntervalalgMapper extends BaseMapper<SuperviseIntervalalg> {

    /**
     * 创建事件
     *
     * @param superviseIntervalalgEventVO
     */
    @SqlParser(filter = true)
    void createEvent(@Param("superviseIntervalalgEventVO") SuperviseIntervalalgEventVO superviseIntervalalgEventVO);

    /**
     * 转移所需数据
     */
    void copyData();

    /**
     * 修改表名
     */
    @SqlParser(filter = true)
    void renameTable();

    /**
     * 清除表数据
     */
    @SqlParser(filter = true)
    void truncateTable();

}
