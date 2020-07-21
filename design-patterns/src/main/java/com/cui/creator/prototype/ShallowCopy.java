package com.cui.creator.prototype;

import lombok.Data;

import java.util.Arrays;

/**
 * @description: 模型模式 浅拷贝
 * @author: wei·man cui
 * @date: 2020/7/9 13:48
 */

@Data
class Address {
    private String add;
    private String[] arr;
}

public class ShallowCopy implements Cloneable {
    private Address address = new Address();

    public void setAdd(String add) {
        this.address.setAdd(add);
    }

    public String getAdd() {
        return this.address.getAdd();
    }

    public void setArr(String[] arr) {
        this.address.setArr(arr);
    }

    public String[] getArr() {
        return this.address.getArr();
    }

    @Override
    protected ShallowCopy clone() {
        ShallowCopy shallow = null;
        try {
            shallow = (ShallowCopy) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return shallow;
    }

    public static void main(String[] args) {
        ShallowCopy a = new ShallowCopy();
        a.setAdd("北京市朝阳区");
        a.setArr(new String[]{"Hello", "Java", "World"});
        System.out.println(a.getAdd());
        System.out.println(Arrays.asList(a.getArr()));

        ShallowCopy b = a.clone();
        System.out.println(b.getAdd());
        System.out.println(Arrays.asList(b.getArr()));

        System.out.println("======= 浅拷贝 拷贝的是 引用对象的地址 =======");
        b.setArr(new String[]{"Php", "Guava", "Python"});
        System.out.println(Arrays.asList(a.getArr()));
        System.out.println(Arrays.asList(b.getArr()));
    }
}
