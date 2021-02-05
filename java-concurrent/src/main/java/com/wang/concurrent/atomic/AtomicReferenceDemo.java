package com.wang.concurrent.atomic;

import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 引用类型的原子操作类的使用
 * AtomicReference
 */
public class AtomicReferenceDemo {

    static AtomicReference<UserInfo> reference = new AtomicReference<>();

    public static void main(String[] args) {
        UserInfo user = new UserInfo("Jack", 18);
        System.out.println(user.getName() + " " + user.getAge());

        // 原子类 包装UserInfo，并修改原引用对象。
        reference.set(user);
        UserInfo updateUser = new UserInfo("Rose", 25);
        reference.compareAndSet(user, updateUser);
        System.out.println(reference.get().toString());

        // 只修改了对象的引用，并未修改对象本身。
        System.out.println(user.getName() + " " + user.getAge());
    }

    // 定义实体类
    @Data
    static class UserInfo {
        private String name;
        private int age;

        public UserInfo(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

}
