package com.anaysis.common;

import com.anaysis.socket1.CollectData;

import java.util.List;

public class TestJSON {
    private static Object setJson(){
        String rs = "{\n" +
                "        \"CollectData\":  {\n" +
                "                \"mid\":  \"170043000D504E3755333720\",\n" +
                "                \"uindex\":       8420,\n" +
                "                \"mat\":  8419,\n" +
                "                \"mbt\":  6341,\n" +
                "                \"mst\":  2078,\n" +
                "                \"mft\":  0,\n" +
                "                \"mbs\":  1,\n" +
                "                \"mf\":   0,\n" +
                "                \"mc\":   4539,\n" +
                "                \"mtb\":  \"2020-08-04 06:00:00\",\n" +
                "                \"mtc\":  \"2020-08-04 08:20:18\"\n" +
                "        }\n" +
                "}{\n" +
                "        \"CollectData\":  {\n" +
                "                \"mid\":  \"170043000D504E3755333720\",\n" +
                "                \"uindex\":       8421,\n" +
                "                \"mat\":  8420,\n" +
                "                \"mbt\":  6342,\n" +
                "                \"mst\":  2078,\n" +
                "                \"mft\":  0,\n" +
                "                \"mbs\":  1,\n" +
                "                \"mf\":   0,\n" +
                "                \"mc\":   4540,\n" +
                "                \"mtb\":  \"2020-08-04 06:00:00\",\n" +
                "                \"mtc\":  \"2020-08-04 08:20:19\"\n" +
                "        }\n" +
                "}{\n" +
                "        \"CollectData\":  {\n" +
                "                \"mid\":  \"170043000D504E3755333720\",\n" +
                "                \"uindex\":       8422,\n" +
                "                \"mat\":  8421,\n" +
                "                \"mbt\":  6343,\n" +
                "                \"mst\":  2078,\n" +
                "                \"mft\":  0,\n" +
                "                \"mbs\":  1,\n" +
                "                \"mf\":   0,\n" +
                "                \"mc\":   4541,\n" +
                "                \"mtb\":  \"2020-08-04 06:00:00\",\n" +
                "                \"mtc\":  \"2020-08-04 08:20:20\"\n" +
                "        }\n" +
                "}{\n" +
                "        \"CollectData\":  {\n" +
                "                \"mid\":  \"170043000D504E3755333720\",\n" +
                "                \"uindex\":       8423,\n" +
                "                \"mat\":  8422,\n" +
                "                \"mbt\":  6344,\n" +
                "                \"mst\":  2078,\n" +
                "                \"mft\":  0,\n" +
                "                \"mbs\":  1,\n" +
                "                \"mf\":   0,\n" +
                "                \"mc\":   4542,\n" +
                "                \"mtb\":  \"2020-08-04 06:00:00\",\n" +
                "                \"mtc\":  \"2020-08-04 08:20:21\"\n" +
                "        }\n" +
                "}";
        String lastStr = "{\"CollectData\":"+rs.substring(rs.lastIndexOf("{"),rs.length());
        System.out.println("lastStr:::::"+lastStr);
        //判定接收到定时数据信息
        CollectData coldata = JSONUtils.jsonToBean(lastStr, CollectData.class, "CollectData");
    return coldata;
    }

//    public static void main(String[] args) {
//       System.out.println(setJson());
//    }
}
