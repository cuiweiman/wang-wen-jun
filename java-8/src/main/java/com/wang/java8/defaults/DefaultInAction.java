package com.wang.java8.defaults;

/**
 * @description: Default 示例2
 * @date: 2020/6/30 22:41
 * @author: weiman cui
 */
public class DefaultInAction {

    public static void main(String[] args) {
        C c = new C();
        c.hello();
    }

    private interface A {
        default void hello() {
            System.out.println("== A.hello ==");
        }
    }

    /**
     * B接口 实现了A接口，并对A的方法进行了重写
     */
    private interface B extends A {
        @Override
        default void hello() {
            System.out.println("== B.hello ==");
        }
    }

    private static class C implements A, B {

    }

}
