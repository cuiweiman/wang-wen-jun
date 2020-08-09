package com.wang.guava.io;

import com.google.common.base.Preconditions;

/**
 * final修饰，不可继承;
 * private 构造函数，不允许实现。
 *
 * @description: 自定义Base64 编码与反编码
 * @date: 2020/8/8 23:08
 * @author: wei·man cui
 */
public final class Base64 {

    // Base64 字典
    private final static String CODE_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^!";
    private final static char[] CODE_DICT = CODE_STRING.toCharArray();

    private Base64() {
    }

    public static String encode(String plain) {
        Preconditions.checkNotNull(plain);
        StringBuilder result = new StringBuilder();
        String binaryString = toBinary(plain);

        // Base64规则是将8位二进制数，从左到右6位一组划分，最右边不足6位补0；
        int delta = 6 - binaryString.length() % 6;
        for (int i = 0; i < delta && delta != 6; i++) {
            binaryString += "0";
        }

        // 6位一组划分,在转10进制
        for (int i = 0; i < binaryString.length() / 6; i++) {
            int begin = i * 6;
            String encodeString = binaryString.substring(begin, begin + 6);
            char encodeChar = CODE_DICT[Integer.parseInt(encodeString, 2)];
            result.append(encodeChar);
        }
        // 补了n个0，末尾添加 n/2个=
        if (delta != 6) {
            for (int i = 0; i < delta / 2; i++) {
                result.append("=");
            }
        }
        return result.toString();
    }

    /**
     * 解码
     *
     * @param encode
     * @return
     */
    public static String decode(final String encrypt) {
        StringBuilder result = new StringBuilder();
        String temp = encrypt;
        int equalIndex = temp.indexOf("=");
        if (equalIndex > 0) {
            temp = temp.substring(0, equalIndex);
        }
        String base64Binary = toBase64Binary(temp);
        // 每8位一组
        for (int i = 0; i < base64Binary.length() / 8; i++) {
            int begin = i * 8;
            String binary = base64Binary.substring(begin, begin + 8);
            char c = Character.toChars(Integer.parseInt(binary, 2))[0];
            result.append(c);
        }
        return result.toString();
    }

    /**
     * 按照Base64编码表，找到字母对应的10进制数，转换成二进制，再补齐6位；
     * Base64二进制是6位一组，正常二进制是8位一组。
     *
     * @param source 入参
     * @return 结果
     */
    private static String toBase64Binary(final String source) {
        final StringBuilder binary = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            int index = CODE_STRING.indexOf(source.charAt(i));
            String charBin = Integer.toBinaryString(index);
            int delta = 6 - charBin.length();
            for (int j = 0; j < delta; j++) {
                charBin = "0" + charBin;
            }
            binary.append(charBin);
        }
        return binary.toString();
    }


    /**
     * 获取 字符对应的ASCII 数字的二进制数，并处理成 8位。
     *
     * @param source 入参
     * @return 结果
     */
    public static String toBinary(final String source) {
        final StringBuilder binary = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            String charBin = Integer.toBinaryString(source.charAt(i));
            int delta = 8 - charBin.length();
            for (int d = 0; d < delta; d++) {
                charBin = "0" + charBin;
            }
            binary.append(charBin);
        }
        return binary.toString();
    }
}
