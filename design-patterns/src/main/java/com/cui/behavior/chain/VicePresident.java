package com.cui.behavior.chain;

/**
 * @description: 副董事长类：具体处理者
 * @date: 2020/7/15 21:58
 * @author: weiman cui
 */
public class VicePresident extends Approver {
    public VicePresident(String name) {
        super(name);
    }

    @Override
    public void processRequest(PurchaseRequest request) {
        if (request.getAmount() < 100000) {
            System.out.println("副董事长" + this.name + "审批采购单：" + request.getNumber() + "，金额：" + request.getAmount() + "元，采购目的：" + request.getPurpose() + "。");
        } else {
            this.approver.processRequest(request);
        }
    }
}
