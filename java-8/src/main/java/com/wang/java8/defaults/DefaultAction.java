package com.wang.java8.defaults;

/**
 * @description: Default接口 示例
 * @date: 2020/6/30 22:16
 * @author: wei·man cui
 */
public class DefaultAction {

    private void confuse(Object o) {
        System.out.println("object");
    }

    private void confuse(int[] i) {
        System.out.println("int[]");
    }

    public static void main(String[] args) {
        A a = () -> 10;
        System.out.println(a.size());
        System.out.println(a.isEmpty());

        // 同名方法会执行哪一个？
        DefaultAction action = new DefaultAction();
        action.confuse(null);

        // 同名方法传参 带类型的 null，会执行哪一个？
        int[] arr = null;
        Object o = null;
        action.confuse(o);
    }

    /**
     * default接口 示例
     */
    private interface A {
        int size();

        default boolean isEmpty() {
            return size() == 0;
        }
    }

}
