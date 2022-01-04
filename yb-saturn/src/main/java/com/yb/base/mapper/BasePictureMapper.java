package com.yb.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.base.entity.BasePicture;
import com.yb.fastdfs.FileSystem;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 图片管理表_yb_base_picture Mapper 接口
 *
 * @author BladeX
 * @since 2021-03-10
 */
public interface BasePictureMapper extends BaseMapper<BasePicture> {


    List<BasePicture> getByBtIdAndBtType(@Param("btId") Integer btId, @Param("btType") String btType);

    List<FileSystem> findByUuid(@Param("code") String code);
}
