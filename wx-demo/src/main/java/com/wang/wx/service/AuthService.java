package com.wang.wx.service;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wang.wx.constants.WechatConstant;
import com.wang.wx.enums.ResponseStatus;
import com.wang.wx.exception.GlobalException;
import com.wang.wx.model.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Desc wechat user service
 * @Author cui·weiman
 * @Since 2021/10/7 17:28
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {

    @Value("${wechat.app-id}")
    private String appId;

    @Value("${wechat.app-secret}")
    private String appSecret;

    private final RestTemplate restTemplate;

    Cache<String, JsApiTicketResponse> ticketCache = Caffeine.newBuilder()
            .expireAfterWrite(7200, TimeUnit.SECONDS)
            .maximumSize(10)
            .build();

    public AccessTokenResponse getAccessToken(String code) {
        String accessTokenUrl = String.format(WechatConstant.ACCESS_TOKEN, appId, appSecret, code);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(accessTokenUrl, String.class);
        AccessTokenResponse accessToken =
                Optional.ofNullable(JSONObject.parseObject(responseEntity.getBody(), AccessTokenResponse.class))
                        .orElse(AccessTokenResponse.empty());
        if (Objects.nonNull(accessToken.getErrcode())) {
            log.error("获取accessToken失败.errCode={},errMsg={}", accessToken.getErrcode(), accessToken.getErrmsg());
            throw new GlobalException(accessToken.getErrcode(), accessToken.getErrmsg());
        }
        return accessToken;
    }


    public AccessTokenResponse refreshAccessToken(String refreshToken) {
        String refreshTokenUrl = String.format(WechatConstant.REFRESH_TOKEN, appId, refreshToken);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(refreshTokenUrl, String.class);
        AccessTokenResponse accessToken =
                Optional.ofNullable(JSONObject.parseObject(responseEntity.getBody(), AccessTokenResponse.class))
                        .orElse(AccessTokenResponse.empty());
        if (Objects.nonNull(accessToken.getErrcode())) {
            log.error("更新accessToken失败.errCode={},errMsg={}", accessToken.getErrcode(), accessToken.getErrmsg());
            throw new GlobalException(accessToken.getErrcode(), accessToken.getErrmsg());
        }
        return accessToken;
    }


    public JsApiTicketResponse getJsApiTicket() {
        String jsAccessTokenUrl = String.format(WechatConstant.JS_API_ACCESS_TOKEN, appId, appSecret);
        ResponseEntity<String> tokenResponseEntity = restTemplate.getForEntity(jsAccessTokenUrl, String.class);

        JsApiTicketTokenResponse tokenResponse = Optional.ofNullable(
                JSONObject.parseObject(tokenResponseEntity.getBody(), JsApiTicketTokenResponse.class))
                .orElse(JsApiTicketTokenResponse.empty());
        if (Objects.nonNull(tokenResponse.getErrcode())) {
            log.error("获取jsApiAccessToken失败.errCode={},errMsg={}",
                    tokenResponse.getErrcode(), tokenResponse.getErrmsg());
            throw new GlobalException(tokenResponse.getErrcode(), tokenResponse.getErrmsg());
        }

        String jsApiTicketUrl = String.format(WechatConstant.JS_API_TICKET, tokenResponse.getAccessToken());
        ResponseEntity<String> ticketResponse = restTemplate.getForEntity(jsApiTicketUrl, String.class);
        JsApiTicketResponse jsApiTicket =
                Optional.ofNullable(JSONObject.parseObject(ticketResponse.getBody(), JsApiTicketResponse.class))
                        .orElse(new JsApiTicketResponse());
        if (0 != jsApiTicket.getErrcode()) {
            log.error("获取jsApiTicket失败.errCode={},errMsg={}", jsApiTicket.getErrcode(), jsApiTicket.getErrmsg());
            throw new GlobalException(jsApiTicket.getErrcode(), jsApiTicket.getErrmsg());
        }
        return jsApiTicket;
    }


    public WechatUserInfoResponse getUserInfo(String openid, String accessToken) {
        String getUserInfoUrl = String.format(WechatConstant.USER_INFO, openid, accessToken);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getUserInfoUrl, String.class);
        WechatUserInfoResponse infoResponse =
                Optional.ofNullable(JSONObject.parseObject(responseEntity.getBody(), WechatUserInfoResponse.class))
                        .orElse(new WechatUserInfoResponse());
        if (0 != infoResponse.getErrcode()) {
            log.error("获取用户信息失败.errCode={},errMsg={}", infoResponse.getErrcode(), infoResponse.getErrmsg());
            throw new GlobalException(infoResponse.getErrcode(), infoResponse.getErrmsg());
        }
        return infoResponse;
    }


    public CheckAccessTokenResponse checkAccessToken(String openid, String accessToken) {
        String getUserInfoUrl = String.format(WechatConstant.CHECK_ACCESS_TOKEN, openid, accessToken);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getUserInfoUrl, String.class);
        return Optional.ofNullable(JSONObject.parseObject(responseEntity.getBody(), CheckAccessTokenResponse.class))
                .orElse(new CheckAccessTokenResponse());
    }

    public JsSdkConfigResponse getJsSdkConfig(String openId, String url) {
        try {
            JsApiTicketResponse jsApiTicket = ticketCache.getIfPresent(openId);
            if (Objects.isNull(jsApiTicket)) {
                jsApiTicket = this.getJsApiTicket();
                ticketCache.put(openId, jsApiTicket);
            }
            String nonceStr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            long timestamp = System.currentTimeMillis() / 1000;
            url = URLDecoder.decode(url, StandardCharsets.UTF_8.name());

            String signature = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s",
                    jsApiTicket.getTicket(), nonceStr, timestamp, url);
            signature = DigestUtils.sha1Hex(signature);
            return new JsSdkConfigResponse(timestamp, nonceStr, signature);
        } catch (UnsupportedEncodingException e) {
            log.error(e.toString());
            throw new GlobalException(ResponseStatus.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }

}
