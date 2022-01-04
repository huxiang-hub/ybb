package com.anaysis.dynamicData.dbmgr;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目数据库管理。提供根据项目编码查询数据库名称和IP的接口。
 *
 * @author elon
 * @version 2018年2月25日
 */

public class ProjectDBMgr {

    /**
     * 保存项目编码与数据名称的映射关系。这里是硬编码，实际开发中这个关系数据可以保存到redis缓存中；
     * 新增一个项目或者删除一个项目只需要更新缓存。到时这个类的接口只需要修改为从缓存拿数据。
     */
    private static Map<String, String> dbNameMap = new HashMap<String, String>();

    /**
     * 保存项目编码与数据库IP的映射关系。
     */
    private Map<String, String> dbIPMap = new HashMap<String, String>();

    private ProjectDBMgr() {
        dbNameMap.put("fuli", "3378/yb_fuli_pro");
        dbNameMap.put("baofeng", "3378/yb_baofeng_pro");
        dbNameMap.put("xingyi", "3378/yb_xingyi_pro");
        dbNameMap.put("hbhr", "3378/yb_hbhr_pro");
        dbNameMap.put("nxjsj", "3378/yb_nxjsj_pro");
        dbNameMap.put("000000", "3378/yb_test");
        dbNameMap.put("demo", "3378/yb_demo_pro");
        dbNameMap.put("nxhr", "3378/yb_nxhr_dev");
        dbNameMap.put("yilong", "3378/yb_yilong_pro");
        dbNameMap.put("yintong", "3378/yb_yintong_pro");

        dbIPMap.put("hbhr", "39.98.57.236");
        dbIPMap.put("baofeng", "39.98.57.236");
        dbIPMap.put("xingyi", "39.98.57.236");
        dbIPMap.put("fuli", "39.98.57.236");
        dbIPMap.put("nxjsj", "39.98.57.236");
        dbIPMap.put("000000", "39.98.57.236");
        dbIPMap.put("demo", "39.98.57.236");
        dbIPMap.put("nxhr", "39.98.57.236");
        dbIPMap.put("yilong", "39.98.57.236");
        dbIPMap.put("yintong", "39.98.57.236");
    }

    public static ProjectDBMgr instance() {
        return ProjectDBMgrBuilder.instance;
    }

    // 实际开发中改为从缓存获取
    public String getDBName(String projectCode) {
        if (dbNameMap.containsKey(projectCode)) {
            return dbNameMap.get(projectCode);
        }
        return "";
    }

    //实际开发中改为从缓存中获取
    public String getDBIP(String projectCode) {
        if (dbIPMap.containsKey(projectCode)) {
            return dbIPMap.get(projectCode);
        }
        return "";
    }

    private static class ProjectDBMgrBuilder {
        private static ProjectDBMgr instance = new ProjectDBMgr();
    }

    public static Map<String, String> getDbNameMap() {
        return dbNameMap;
    }


}
