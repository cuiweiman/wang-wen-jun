package com.cui.behavior.strategy;

/**
 * @description: 电影票类：环境类
 * @date: 2020/7/20 23:01
 * @author: wei·man cui
 */
public class MovieTicket {

    private double price;

    private Discount discount;

    public double getPrice() {
        return discount.calculate(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}


/**
 * @description: 折扣类：抽象策略类
 * @author: wei·man cui
 * @date: 2020/7/20 23:02
 */
interface Discount {
    /**
     * 票价计算
     *
     * @param price 票价
     * @return 结果
     */
    double calculate(double price);
}

/**
 * @description: 学生票折扣类：具体策略类
 * @author: wei·man cui
 * @date: 2020/7/20 23:03
 */
class StudentDiscount implements Discount {
    @Override
    public double calculate(double price) {
        System.out.println("学生票：");
        return price * 0.8;
    }
}

/**
 * @description: 儿童票折扣类：具体策略类
 * @author: wei·man cui
 * @date: 2020/7/20 23:03
 */
class ChildrenDiscount implements Discount {
    @Override
    public double calculate(double price) {
        System.out.println("儿童票：");
        return price - 10;
    }
}

/**
 * @description: VIP会员票折扣类：具体策略类
 * @author: wei·man cui
 * @date: 2020/7/20 23:04
 */
class VipDiscount implements Discount {
    @Override
    public double calculate(double price) {
        System.out.println("VIP票：");
        System.out.println("增加积分！");
        return price * 0.5;
    }
}

