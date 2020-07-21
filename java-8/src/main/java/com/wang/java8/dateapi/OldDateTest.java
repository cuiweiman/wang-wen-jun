package com.wang.java8.dateapi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: 旧 Date API
 * @author: wei·man cui
 * @date: 2020/7/2 11:37
 */
public class OldDateTest {

    public static void main(String[] args) throws ParseException {
        // 问题1： Year = 1900 + 116, Month = 1 + 2
        Date date = new Date(116, 2, 18);
        System.out.println(date);
        // 问题2：非线程安全的
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    Date parse = null;
                    try {
                        parse = sdf.parse("20160505");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(parse);
                }
            }).start();
        }

    }


}
