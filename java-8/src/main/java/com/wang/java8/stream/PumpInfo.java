package com.wang.java8.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: cuiweiman
 * @Since: 2021/6/11 下午5:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PumpInfo {

    private String houseName;

    private String groupName;

    private String pumpName;

    private String param;

    public PumpInfo(String houseName, String groupName, String pumpName) {
        this.houseName = houseName;
        this.groupName = groupName;
        this.pumpName = pumpName;
    }
}
