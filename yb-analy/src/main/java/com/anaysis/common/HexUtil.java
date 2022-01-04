package com.anaysis.common;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class HexUtil {


    /**
     * 16进制数字字符集
     */
    private static String hexString = "0123456789ABCDEF";

    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String HexStringToString(String bytes) throws UnsupportedEncodingException {
        bytes = bytes.replace(" ", "");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        //将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2) {
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        }
        return new String(baos.toByteArray(), "GBK");
    }

    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            unicode.append(Integer.toHexString(c) + " ");
        }

        return unicode.toString();
    }

    public static String unicode2String(String unicode) {  //unicode转字符串
        if (unicode != null && !"".equals(unicode)) {
            StringBuffer string = new StringBuffer();
            String[] hex = unicode.split(" ");
            for (int i = 0; i < hex.length; i++) {
                int data = Integer.parseInt(hex[i], 16);
                string.append((char) data);
            }
            return string.toString();
        } else {
            return null;
        }
    }

    public static byte[] hexStringToBytes(String hexString) { //16进制字符串转字节数组
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * @param b 字节数组
     * @return 16进制字符串
     * @throws
     * @Title:bytes2HexString
     * @Description:字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

//    public static void main(String[] args) {
//        String DI1="0110000100010200016641";
//        byte[] bytes = hexString2Bytes(DI1);
//        System.out.println();
//    }
    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @throws
     * @Title:string2HexString
     * @Description:字符串转16进制字符串
     */
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            //hexString.append(strHex+" ");
            hexString.append(strHex);
        }
        return hexString.toString().substring(0, hexString.toString().length() - 1);
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     */
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp
                    + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),
                    16).byteValue();
        }
        return temp;
    }

    /**
     * @param src
     * @return
     * @throws
     * @Title:char2Byte
     * @Description:字符转成字节数据char-->integer-->byte
     */
    public static Byte char2Byte(Character src) {
        return Integer.valueOf((int) src).byteValue();
    }

    /**
     * @param a   转化数据
     * @param len 占用字节数
     * @return
     * @throws
     * @Title:intToHexString
     * @Description:10进制数字转成16进制
     */
    private static String intToHexString(int a, int len) {
        len <<= 1;
        String hexString = Integer.toHexString(a);
        int b = len - hexString.length();
        if (b > 0) {
            for (int i = 0; i < b; i++) {
                hexString = "0" + hexString;
            }
        }
        return hexString;
    }

    /****
     * 把16进制转换成字符串
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String byteToHex(byte[] bytes){
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }
//    public static void main(String[] args) {
//        String s = "7B0A0922436F6C6C65637444617461223A097B0A0909226D6964223A09302C0A09092275696E646578223A0931342C0A0909226D6174223A093134302C0A0909226D6274223A09302C0A0909226D7374223A093134302C0A0909226D6674223A093134302C0A0909226D6273223A09302C0A0909226D66223A09312C0A0909226D63223A09302C0A0909226D7462223A0922323031392D31302D3032202031373A31373A3234222C0A0909226D7463223A0922323031392D31302D3032202031373A32343A3435220A097D0A7D";
//        s = HexUtil.hexStringToString(s);
//        CollectData collect = (CollectData) JSONUtils.jsonToBean(s, CollectData.class, "CollectData");
//        System.out.println("======collect============collect-id：" + collect.getMid());
//        //System.out.println("s:"+s);
//    }
}
