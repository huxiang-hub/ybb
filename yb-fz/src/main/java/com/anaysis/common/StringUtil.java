package com.anaysis.common;

import org.springblade.core.tool.utils.Charsets;
import org.springblade.core.tool.utils.Exceptions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    //去除所有空格
    public static String replaceAllBlank(String str) {
        String s = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	/*\n 回车(\u000a)
	\t 水平制表符(\u0009)
	\s 空格(\u0008)
	\r 换行(\u000d)*/
            Matcher m = p.matcher(str);
            s = m.replaceAll("");
        }
        return s;
    }

    //去除所有空格，留下一个
    public static String replaceBlankLeaveOne(String str) {
        String s = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s{2,}|\t|\r|\n");
            Matcher m = p.matcher(str);
            s = m.replaceAll(" ");
        }
        return s;
    }

//    public static void main(String[] args) {
//        System.out.println(StringUtil.replaceAllBlank("just    do     it!"));
//        System.out.println(StringUtil.replaceBlankLeaveOne("just    do     it!"));
//        Pattern pattern = Pattern.compile("[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}[:][0-9]{1,2}");
//        Matcher matcher = pattern.matcher("2、开标时间：2018-08-02 14:00:00。2、开标时间：2018-08-02 16:00:00。2、开标时间：2018-08-02 15:00:00。");
//        while (matcher.find()) {
//            System.out.println(matcher.group());
//        }
//
//        System.out.println(isValidDate("2007-09-31"));
//    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验e799bee5baa6e997aee7ad94e78988e69d8331333361313836证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

}
