package com.wang.basic.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @description: Unsafe类 操作 对象
 * @author: wei·man cui
 * @date: 2021/2/4 14:34
 */
public class UnsafeObject {

    private static Unsafe unsafe;

    static {
        try {
            // 反射机制获取 Unsafe 对象实例
            final Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InstantiationException, NoSuchFieldException {
        /*
         Unsafe.allocateInstance 实例化对象
         */
        EntityDomain entityDomain = (EntityDomain) unsafe.allocateInstance(EntityDomain.class);
        entityDomain.setName("Jack");
        System.out.println(entityDomain.getName());
        /*
        Unsafe.compareAndSwapObject 修改对象属性值
         */
        // 获取属性 在内存中的 偏移量
        long nameOffset = unsafe.objectFieldOffset(EntityDomain.class.getDeclaredField("name"));
        System.out.println(nameOffset);
        // CAS 方法：判断内存中类 EntityDomain 在偏移量 nameOffset 处的内容是不是 “Jack”，若是，则更改成“Rose”，否则不更改。
        final boolean b = unsafe.compareAndSwapObject(entityDomain, nameOffset, "Jack", "Rose");
        System.out.println("b=" + b + "; entityDomain.name=" + entityDomain.getName());

        String[] books = {"Chinese", "Math", "English", "Biology"};
        final long boosOffset = unsafe.arrayBaseOffset(String[].class);
        System.out.println(boosOffset);
        final Object unsafeObject = unsafe.getObject(books, boosOffset);
        System.out.println(unsafeObject);
        final boolean b1 = unsafe.compareAndSwapObject(books, boosOffset, "Chinese", "Chemistry");
        System.out.println(b1);
    }

}
