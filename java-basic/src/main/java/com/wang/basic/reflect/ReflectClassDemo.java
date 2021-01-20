package com.wang.basic.reflect;

/**
 * <p>
 * 1. 通过 包名+类名，使用Class静态方法forName() 获取
 * 2. 通过 Object.class 直接获取
 * 3. 通过 类的实例获取，同 2，Object.class
 * </p>
 *
 * @description: 反射获取 Class对象
 * @author: wei·man cui
 * @date: 2020/10/28 14:34
 */
public class ReflectClassDemo {

    public static void main(String[] args) throws ClassNotFoundException {
        Class clazz = Class.forName("com.wang.basic.reflect.Person");
        System.out.println(clazz);

        System.out.println(Person.class);

        System.out.println(new Person().getClass());
    }

}
