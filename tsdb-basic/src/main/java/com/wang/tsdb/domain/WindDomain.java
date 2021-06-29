package com.wang.tsdb.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * @Description: wind 风
 * @Author: cuiweiman
 * @Since: 2021/6/29 下午5:00
 */
@Data
public class WindDomain {

    @CsvBindByName(column = "time")
    private Long timeStamp;

    @CsvBindByName(column = "speed")
    private Double speed;

    @CsvBindByName(column = "direction")
    private Double direction;

    @CsvBindByName(column = "value")
    private String value;

}
