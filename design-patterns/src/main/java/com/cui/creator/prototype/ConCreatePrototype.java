package com.cui.creator.prototype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 模型模式 具体原型类
 * @author: wei·man cui
 * @date: 2020/7/9 13:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConCreatePrototype implements Cloneable {

    private String attr;

    @Override
    public ConCreatePrototype clone() {
        ConCreatePrototype prototype = null;
        try {
            prototype = (ConCreatePrototype) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return prototype;
    }

    public static void main(String[] args) {
        ConCreatePrototype obj1 = new ConCreatePrototype("原型模式");
        ConCreatePrototype obj2 = obj1.clone();
        System.out.println(obj2.getAttr());

    }

}
