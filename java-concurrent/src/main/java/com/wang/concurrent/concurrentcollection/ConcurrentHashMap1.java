package com.wang.concurrent.concurrentcollection;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author weiman cui
 * @date 2020/5/17 17:20
 */
public class ConcurrentHashMap1 {

    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("1", "1-value");
        map.put("2", "2-value");
        map.put("3", "3-value");

        System.out.println("--------------");
        System.out.println("2=" + map.get("2"));
        map.put("2", "2_value_2nd");
        System.out.println("2=" + map.get("2"));

        // 100 & 110 = 100
        System.out.println("4 & 6 = " + Integer.toBinaryString(4 & 6));
        System.out.println("4 | 6 = " + Integer.toBinaryString(4 | 6));
        System.out.println("~4 = " + Integer.toBinaryString(~4));
        System.out.println("4 ^ 6 = " + Integer.toBinaryString(4 ^ 6));

        System.out.println(1 << 4);

    }

}


