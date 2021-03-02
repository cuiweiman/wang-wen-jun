package com.wang.basic.passencode.unicode;

import java.util.Arrays;
import java.util.Collections;

/**
 * @description: unicode 编解码
 * @author: wei·man cui
 * @date: 2021/3/2 14:52
 */
public class UnicodeUtils {
    /**
     * 解码
     *
     * @param unicode unicode串
     * @return 解码
     */
    public static String unicodeToString(String unicode) {
        if (unicode == null || "".equals(unicode)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        return sb.toString();
    }

    /**
     * 编码
     *
     * @param string 字符串
     * @return 结果
     */
    public static String stringToUnicode(String string) {
        if (string == null || "".equals(string)) {
            return null;
        }
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }


    private static String hex2Binary(byte[] hex) {
        String[] binaryArray = {"0000", "0001", "0010", "0011",
                "0100", "0101", "0110", "0111",
                "1000", "1001", "1010", "1011",
                "1100", "1101", "1110", "1111"};

        StringBuilder outStr = new StringBuilder();
        int pos = 0;
        for (byte b : hex) {
            //低四位
            pos = b & 0x0F;
            outStr.append(binaryArray[pos]);
        }
        return outStr.toString();
    }


    public static void main(String[] args) {
        // String s = stringToUnicode("测试");
        // System.out.println("编码：" + s);
        // String s1 = unicodeToString(s);
        // System.out.println("解码：" + s1);

        final String result = hex2Binary("0020".getBytes());
        System.out.println(result);
    }

}
