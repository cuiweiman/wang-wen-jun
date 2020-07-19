package com.cui.behavior.chain;

/**
 * @description: 董事长类：具体处理者
 * @date: 2020/7/15 22:00
 * @author: weiman cui
 */
public class President extends Approver {

    public President(String name) {
        super(name);
    }

    @Override
    public void processRequest(PurchaseRequest request) {
        if (request.getAmount() < 500000) {
            System.out.println("董事长" + this.name + "审批采购单：" + request.getNumber() + "，金额：" + request.getAmount() + "元，采购目的：" + request.getPurpose() + "。");  //处理请求
        }
        else {
            this.approver.processRequest(request);  //转发请求
        }
    }
}
