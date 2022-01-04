package com.anaysis.sqlservermapper;

import com.alibaba.fastjson.JSONObject;
import com.anaysis.entity.HrMaterial;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author lzb
 * @Date 2021/1/10 09:18
 **/
@Mapper
public interface HrMaterialMapper {
    /**
     * 根据条码获取物料库存信息
     * @param barCode 条码号
     * @return
     */
    HrMaterial selectByBarCode(String barCode);

    List<JSONObject> getPaperRoll(String a,String f, String startDate, String endDate);
}
