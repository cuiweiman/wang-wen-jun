package com.wang.basic.throwable;

/**
 * @description: ClassNotFoundException 可检查性异常,发生在编译期
 * @author: wei·man cui
 * @date: 2021/2/2 15:48
 */
public class ClassNotFoundExceptionDemo {
    public static void main(String[] args) {
        try {
            // final Object myObject = Class.forName("myObject");
            final Object myObject2 = ClassLoader.getSystemClassLoader().loadClass("myObject");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
