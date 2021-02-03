package com.wang.basic.strings;

/**
 * @description: 字符串对象与常量池
 * @author: wei·man cui
 * @date: 2021/2/3 15:20
 */
public class StringDemo {

    public static void main(String[] args) {
        String s1 = "abc";
        String s2 = "abc";
        System.out.println(s1 == s2);

        String s3 = new String("abc");
        String s4 = new String("abc");
        System.out.println(s3 == s4);
        System.out.println(s3.equals(s4));

        // String#intern() 方法直接从字符串常量池中获取字符串常量。
        final String intern = s3.intern();
        System.out.println(s1 == intern);
    }

}
