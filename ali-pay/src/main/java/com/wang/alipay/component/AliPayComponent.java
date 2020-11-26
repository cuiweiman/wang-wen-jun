package com.wang.alipay.component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.wang.alipay.properties.AliPayProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @description: 阿里支付 通用方法 组件
 * @author: wei·man cui
 * @date: 2020/11/26 11:07
 */
@Component
public class AliPayComponent {

    @Resource
    private AliPayProperties aliPayProperties;

    /**
     * 校验签名
     *
     * @param request 请求
     * @return 结果
     * @throws AlipayApiException 异常
     */
    public boolean rsaCheckV1(HttpServletRequest request) throws AlipayApiException {
        Iterator<Map.Entry<String, String[]>> iterator = request.getParameterMap().entrySet().iterator();
        Map<String, String> params = new HashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> next = iterator.next();
            String[] values = next.getValue();
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(next.getKey(), valueStr);
        }
        return AlipaySignature.rsaCheckV1(params,
                aliPayProperties.getPublicKey(),
                aliPayProperties.getCharset(),
                aliPayProperties.getSignType());
    }


}
