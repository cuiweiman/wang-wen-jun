package com.wang.concurrent.deadlock;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 缩小锁的范围
 *
 * @author weiman cui
 * @date 2020/5/18 21:46
 */
public class ReduceLock {

    private Map<String, String> matchMap = new HashMap<>();

    /**
     * 锁的范围 缩小了
     *
     * @param name
     * @param regexp
     * @return
     */
    public boolean isMatchReduce(String name, String regexp) {
        String key = "user." + name;
        String job;
        synchronized (this) {
            job = matchMap.get(key);
        }
        if (job == null) {
            return false;
        } else {
            return Pattern.matches(regexp, job);
        }
    }


    /**
     * 为了保证线程安全，这里使用了synchronized关键字，但实际上
     * 线程不安全的方法只有Map.get()方法而已。因此锁的范围可缩小。
     *
     * @param name
     * @param regexp
     * @return
     */
    public synchronized boolean isMatch(String name, String regexp) {
        String key = "user." + name;
        String job = matchMap.get(key);
        if (job == null) {
            return false;
        } else {
            return Pattern.matches(regexp, job);
        }
    }

}
