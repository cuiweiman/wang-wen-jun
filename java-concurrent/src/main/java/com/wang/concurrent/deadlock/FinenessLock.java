package com.wang.concurrent.deadlock;

import java.util.HashSet;
import java.util.Set;

/**
 * 减少锁的粒度。
 * 若需要同时获取 以下案例的两个锁，需要注意出现死锁（银行转账思想）。
 *
 * @author weiman cui
 * @date 2020/5/18 21:52
 */
public class FinenessLock {

    public final Set<String> users = new HashSet<>();
    public final Set<String> queries = new HashSet<>();

    public synchronized void addUser(String u) {
        users.add(u);
    }

    public synchronized void addQuery(String q) {
        queries.add(q);
    }

    /**
     * 减小锁的粒度，从方法上缩小到方法内。
     *
     * @param u
     */
    public void removeUser(String u) {
        synchronized (users) {
            users.remove(u);
        }
    }

    public void removeQuery(String q) {
        synchronized (queries) {
            queries.remove(q);
        }
    }

}
