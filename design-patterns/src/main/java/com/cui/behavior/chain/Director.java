package com.cui.behavior.chain;

/**
 * @description: 主任 采购单 具体审批处理者
 * @date: 2020/7/15 21:53
 * @author: wei·man cui
 */
public class Director extends Approver {

    public Director(String name) {
        super(name);
    }

    @Override
    public void processRequest(PurchaseRequest request) {
        if (request.getAmount() < 5000) {
            System.out.println("主任" + this.name + "审批采购单：" + request.getNumber() + "，金额：" + request.getAmount() + "元，采购目的：" + request.getPurpose() + "。");  //处理请求
        } else {
            // 转发请求 到 下一个对象
            this.approver.processRequest(request);
        }
    }
}
