package com.wang.basic.reflect;

import lombok.Data;


/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/10/28 14:35
 */
@Data
public class Person {

    public final static String ADDRESS = "BEIJING";

    private String name;

    private Integer age;

    public void say() {
        System.out.println(this.name + " say: I am in love with you");
    }

}
