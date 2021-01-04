package com.wang.alipay.enums;

import lombok.Getter;

/**
 * @description: 币种
 * @author: wei·man cui
 * @date: 2021/1/4 15:42
 */
@Getter
public enum CurrencyEnum {

    /**
     * 英镑
     */
    POUND("GBP", "£"),
    /**
     * 美元
     */
    DOLLAR("USD", "$"),
    /**
     * 欧元
     */
    EURO("EUR", "€"),
    /**
     * 人民币
     */
    RMB("CNY", "¥"),

    ;

    private String currency;

    private String symbol;

    CurrencyEnum(String currency, String symbol) {
        this.currency = currency;
        this.symbol = symbol;
    }
}
