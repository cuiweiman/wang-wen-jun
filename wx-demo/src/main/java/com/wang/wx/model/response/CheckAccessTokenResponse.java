package com.wang.wx.model.response;

import lombok.Data;

/**
 * @Desc check access token response
 * @Author cui·weiman
 * @Since 2021/10/7 20:44
 */
@Data
public class CheckAccessTokenResponse {

    private Integer errcode;

    private String errmsg;

}
