package com.wang.think.initmethod;

import org.springframework.beans.factory.InitializingBean;

/**
 * @description: 自定义 Bean 初始化方法（初始化方法在 bean 实例创建好之后、属性值填充完成后 才调用）
 * @author: wei·man cui
 * @date: 2020/12/30 13:50
 */
public class Car implements InitializingBean {

    private String brand;

    private Integer price;

    private String color;

    public void init() {
        System.out.println("<bean>标签的 init-method 属性");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setColor("五彩斑斓黑");
        System.out.println("实现 InitializingBean 接口的 afterPropertiesSet() 方法");
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
