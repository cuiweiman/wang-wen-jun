package com.cui.behavior.chain;

/**
 * @description: 审批者类 抽象处理者
 * @date: 2020/7/15 21:51
 * @author: wei·man cui
 */
public abstract class Approver {

    // 审批者姓名
    String name;

    // 后继对象
    Approver approver;

    public Approver(String name) {
        this.name = name;
    }

    // 设置后继者
    public void setApprover(Approver approver) {
        this.approver = approver;
    }

    // 抽象请求 处理方法
    public abstract void processRequest(PurchaseRequest request);

}
