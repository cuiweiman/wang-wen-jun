package com.wang.basic.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description: Class API
 * @author: wei·man cui
 * @date: 2020/10/28 14:42
 */
public class ReflectClassApi {
    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
        Class<Person> clazz = Person.class;
        Field[] fields = clazz.getFields();
        Arrays.stream(fields).forEach(System.out::println);

        Method[] methods = clazz.getMethods();
        System.out.println("==========Person 的所有方法==========");
        Arrays.stream(methods).forEach(System.out::println);

        System.out.println("==========Person 的所有 declaredMethods 方法==========");
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Arrays.stream(declaredMethods).forEach(System.out::println);

        Method say = clazz.getMethod("say");
        System.out.println("Person的say方法：" + say);

        System.out.println("==========Constructor 的所有方法==========");
        Constructor<?>[] constructors = clazz.getConstructors();
        Arrays.stream(constructors).forEach(System.out::println);

        Package aPackage = clazz.getPackage();
        System.out.println("aPackage "+aPackage);

        String name = clazz.getName();
        System.out.println("className "+name);

        System.out.println("==========所有注解==========");
        Annotation[] annotations = clazz.getAnnotations();
        Arrays.stream(annotations).forEach(System.out::println);
    }


}
