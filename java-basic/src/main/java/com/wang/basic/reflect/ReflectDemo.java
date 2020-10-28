package com.wang.basic.reflect;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;


/**
 * @description: 小案例
 * @author: wei·man cui
 * @date: 2020/10/28 15:07
 */
public class ReflectDemo {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException, InstantiationException {
        ClassLoader loader = ReflectDemo.class.getClassLoader();
        InputStream in = loader.getResourceAsStream("pro.properties");

        Properties pro = new Properties();
        pro.load(in);

        String className = pro.getProperty("className");
        String methodName = pro.getProperty("methodName");

        Class<?> clazz = Class.forName(className);
        Object o = clazz.getDeclaredConstructor().newInstance();

        Method method = clazz.getMethod(methodName);
        method.invoke(o);

    }


}
