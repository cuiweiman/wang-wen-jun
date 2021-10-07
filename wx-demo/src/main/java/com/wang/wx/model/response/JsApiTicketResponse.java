package com.wang.wx.model.response;

import lombok.Data;

/**
 * @Desc js api ticket
 * @Author cui·weiman
 * @Since 2021/10/7 18:11
 */
@Data
public class JsApiTicketResponse {

    private Integer errcode;
    private String errmsg;
    private String ticket;
    private Integer expiresIn;


}
