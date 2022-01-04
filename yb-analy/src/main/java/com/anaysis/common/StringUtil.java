package com.anaysis.common;

public class StringUtil {

    public static String cleanBlank(String str) {
        String dd = str;
        if (dd != null && dd.length() > 0) {
            dd = dd.replaceAll(" ", "");
            dd = dd.replaceAll("\n", "");
            dd = dd.replaceAll("\t", "");
            dd = dd.replaceAll("\n", "");
        }
        return dd;
    }

    /****
     * 判断字符串中包含多少个str字符串
     * @param resStr
     * @param str
     * @return
     */
    public static int includeBystr(String resStr, String str) {
        String res = resStr;
        String b = str;
        int count = 0;
        if (res == null || b == null)
            return count;
        while (res.contains(b)) {
            res = res.substring(res.indexOf(b) + 1);
            ++count;
        }
        return count;
    }
}