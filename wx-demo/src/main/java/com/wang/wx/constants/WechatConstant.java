package com.wang.wx.constants;

/**
 * @Desc wechat constant
 * @Author cui·weiman
 * @Since 2021/10/7 17:31
 */
public class WechatConstant {

    public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
            "grant_type=authorization_code&appid=%s&secret=%s&code=%s";

    public static final String REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
            "grant_type=refresh_token&appid=%s&refresh_token=%s";


    public static final String USER_INFO = "https://api.weixin.qq.com/sns/userinfo?" +
            "lang=zh_CN&openid=%s&access_token=%s";


    public static final String CHECK_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/auth?" +
            "openid=%s&access_token=%s";


    public static final String JS_API_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?" +
            "grant_type=client_credential&appid=%s&secret=%s";

    public static final String JS_API_TICKET =
            "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=%s";

}
