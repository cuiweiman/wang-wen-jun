package com.cui.creator.prototype;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description: 模型模式 深拷贝
 * @author: weiman cui
 * @date: 2020/7/9 13:48
 */

@Data
@AllArgsConstructor
class Info implements Cloneable {
    private String add;
    private String[] arr;

    @Override
    protected Info clone() throws CloneNotSupportedException {
        return (Info) super.clone();
    }
}

@Data
public class DeepCopy implements Cloneable {
    public Info info;

    @Override
    protected DeepCopy clone() throws CloneNotSupportedException {
        DeepCopy deepCopy = (DeepCopy) super.clone();
        deepCopy.info = info.clone();
        return deepCopy;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        DeepCopy a = new DeepCopy();
        a.setInfo(new Info("上海市静安区", new String[]{"CSDN", "Byte", "BiliBili"}));
        DeepCopy b = a.clone();
        System.out.println(JSONObject.toJSONString(b));
        b.getInfo().setAdd("天津市和平区");
        b.getInfo().setArr(new String[]{"五大道", "小白楼"});

        System.out.println("a= " + JSONObject.toJSONString(a));
        System.out.println("b= " + JSONObject.toJSONString(b));
    }
}

