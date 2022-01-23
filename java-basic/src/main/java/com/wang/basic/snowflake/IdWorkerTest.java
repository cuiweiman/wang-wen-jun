package com.wang.basic.snowflake;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2022/1/23 23:20
 */
public class IdWorkerTest {
    public static void main(String[] args) {
        for (int i = 0; i < 30000; i++) {
            System.out.println(IdWorker.nextId());
        }
    }
}
