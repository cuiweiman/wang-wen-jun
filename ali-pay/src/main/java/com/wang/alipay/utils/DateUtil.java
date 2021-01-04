package com.wang.alipay.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @description: 日期工具类
 * @author: wei·man cui
 * @date: 2021/1/4 10:48
 */
public class DateUtil {

    public static final String YMDHMS = "yyyyMMddHHmmss";

    public static LocalDateTime getNow() {
        return LocalDateTime.now();
    }

    public static String format(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static void main(String[] args) {
        System.out.println(format(getNow(), YMDHMS));
    }

}
