package com.wang.basic.throwable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/10/28 16:26
 */
public class Demo {

    public static void main(String[] args) {
        // compileException();
        // runtimeException();
        // runtimeException2();
        error();
    }

    /**
     * 编译异常
     */
    public static void compileException() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-mm-DD");
        try {
            Date date = sdf.parse("1999/12/10");
        } catch (ParseException e) {
            System.out.println("编译异常");
            e.printStackTrace();
        }
    }

    public static void runtimeException() {
        int a = 0;
        int b = 10 / a;
        System.out.println(b);
    }

    public static void runtimeException2() {
        int[] arr = {1, 2, 3};
        System.out.println(arr[5]);
    }

    public static void error() {
        int[] arr = new int[1024 * 1024 * 1024];
        System.out.println(arr.length);
    }

}
