package com.cui.behavior.chain;

/**
 * @description: 客户端测试类
 * @date: 2020/7/15 22:01
 * @author: wei·man cui
 */
public class Client {
    public static void main(String[] args) {
        Approver wjZhang, gYang, jGuo, meeting;
        wjZhang = new Director("张无忌主任");
        gYang = new VicePresident("杨过副董事长");
        jGuo = new President("郭靖董事长");
        meeting = new Congress("董事会");

        // 创建 职责链 张无忌->杨过->郭靖->董事会
        wjZhang.setApprover(gYang);
        gYang.setApprover(jGuo);
        jGuo.setApprover(meeting);

        // 创建采购单
        PurchaseRequest pr1 = new PurchaseRequest(45000D, 10001, "购买倚天剑");
        wjZhang.processRequest(pr1);

        PurchaseRequest pr2 = new PurchaseRequest(60000D, 10002, "购买《葵花宝典》");
        wjZhang.processRequest(pr2);

        PurchaseRequest pr3 = new PurchaseRequest(160000D, 10003, "购买《金刚经》");
        wjZhang.processRequest(pr3);

        PurchaseRequest pr4 = new PurchaseRequest(800000D, 10004, "购买桃花岛");
        wjZhang.processRequest(pr4);
    }


}
