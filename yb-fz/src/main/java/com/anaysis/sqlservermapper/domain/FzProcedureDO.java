package com.anaysis.sqlservermapper.domain;

import lombok.Data;

/****
 * 工序实体对象，从方正的SQLserver数据
 */
@Data
public class FzProcedureDO {
    Integer id;
    String cName;
    String cContent;
    Integer iPlan_Job;
    Integer iPlan_InTime;
    String cGzzxIDStr;
    Integer iicon;
    Integer iisprinting;
}
