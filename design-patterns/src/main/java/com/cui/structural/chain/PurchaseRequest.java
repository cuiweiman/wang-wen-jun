package com.cui.structural.chain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 根据采购金额的不同由不同层次的主管人员来审批，
 * 主任可以审批5万元以下（不包括5万元）的采购单，
 * 副董事长可以审批5万元至10万元（不包括10万元）的采购单，
 * 董事长可以审批10万元至50万元（不包括50万元）的采购单，
 * 50万元及以上的采购单就需要开董事会讨论决定
 * </p>
 *
 * @description: 采购单 请求类
 * @date: 2020/7/15 21:49
 * @author: weiman cui
 */
@Data
@AllArgsConstructor
public class PurchaseRequest {

    // 采购金额
    private Double amount;

    // 采购数量
    private Integer number;

    // 采购目的
    private String purpose;
}
