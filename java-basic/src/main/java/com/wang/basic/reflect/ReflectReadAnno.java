package com.wang.basic.reflect;

import com.wang.basic.annotation.Pro;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description: 反射 读取 注解
 * @author: wei·man cui
 * @date: 2020/10/28 15:51
 */
@Pro(className = "com.wang.basic.reflect.Person", methodName = "say")
public class ReflectReadAnno {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<ReflectReadAnno> clazz = ReflectReadAnno.class;
        Pro pro = clazz.getAnnotation(Pro.class);

        String className = pro.className();
        String methodName = pro.methodName();

        Class<?> aClass = Class.forName(className);
        Object o = aClass.getDeclaredConstructor().newInstance();
        Method method = aClass.getMethod(methodName);
        method.invoke(o);
    }
}
