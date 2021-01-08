package com.wang.alipay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.wang.alipay.component.AliPayComponent;
import com.wang.alipay.enums.CurrencyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 支付成功的异步通知 {@link #notifyUrl(HttpServletRequest)} 中，trade_status有两种状态： TRADE_SUCCESS、TRADE_FINISHED、TRADE_CLOSE。
 * TRADE_SUCCESS：顾客支付后，商家还可以进行退款操作，即该交易并没有彻底关闭，卖家还能进一步操作。
 * TRADE_FINISHED：交易结束，不可退款。支付宝维持 TRADE_SUCCESS 状态会维持三个月（建议联系客服检查签约的时间设置），退款有效期超时后，支付宝会再次发送 异步通知。
 * TRADE_CLOSE： 未付款交易超时关闭，或支付完成后全额退款。
 *
 * @description: 支付宝通用接口
 * @author: wei·man cui
 * @date: 2020/11/26 11:00
 */
@Slf4j
@RestController
@RequestMapping("/aliPay")
public class AliPayCommonController {

    @Resource
    private AliPayComponent aliPayComponent;

    @Resource
    private AlipayClient alipayClient;

    /**
     * 支付异步通知
     * <p>
     * 接收到异步通知并验签通过后，一定要检查通知内容，
     * 包括通知中的app_id、out_trade_no、total_amount是否与请求中的一致，
     * 并根据trade_status进行后续业务处理。
     * <p>
     * https://docs.open.alipay.com/194/103296
     */
    @RequestMapping("/notifyUrl")
    public String notifyUrl(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder notifyBuild = new StringBuilder("/****************************** alipay notify ******************************/\n");
        parameterMap.forEach((key, value) -> notifyBuild.append(key).append("=").append(value[0]).append("\n"));
        log.info(notifyBuild.toString());

        boolean flag = aliPayComponent.rsaCheckV1(request);
        if (flag) {
            /**
             * TODO 需要严格按照如下描述校验通知数据的正确性
             *
             * 商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
             * 并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
             * 同时需要校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
             *
             * 上述有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
             * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
             * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
             */

            //交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            // TRADE_FINISHED (表示交易已经成功结束，并不能再对该交易做后续操作);
            // TRADE_SUCCESS (表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
            if (tradeStatus.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，
                // 并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。

            } else if (tradeStatus.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，
                // 并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            }
            return "success";
        }
        return "fail";
    }


    @RequestMapping("/queryOrderStatus/{outTradeNo}")
    public AlipayTradeQueryResponse query(@PathVariable String outTradeNo) throws AlipayApiException {
        return aliPayComponent.queryTradeStatus(outTradeNo);
    }

    @RequestMapping("/refund/{outTradeNo}")
    public Object refund(@PathVariable String outTradeNo) throws AlipayApiException {
        final AlipayTradeQueryResponse query = aliPayComponent.queryTradeStatus(outTradeNo);

        final AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
        refundModel.setOutTradeNo(query.getOutTradeNo());
        refundModel.setRefundAmount(query.getTotalAmount());
        refundModel.setTradeNo(query.getTradeNo());
        refundModel.setRefundReason("不想要了");
        refundModel.setRefundCurrency(CurrencyEnum.RMB.getCurrency());

        final AlipayTradeRefundRequest refundRequest = new AlipayTradeRefundRequest();
        refundRequest.setBizModel(refundModel);

        return alipayClient.execute(refundRequest);
    }

    @RequestMapping("/refundQuery/{outTradeNo}")
    public Object refundQuery(@PathVariable String outTradeNo) throws AlipayApiException {
        final AlipayTradeFastpayRefundQueryModel refundQueryModel = new AlipayTradeFastpayRefundQueryModel();
        refundQueryModel.setOutTradeNo(outTradeNo);
        refundQueryModel.setOutRequestNo(outTradeNo);

        final AlipayTradeFastpayRefundQueryRequest refundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();
        refundQueryRequest.setBizModel(refundQueryModel);

        return alipayClient.execute(refundQueryRequest).getBody();
    }

    /**
     * 关闭交易
     * 用于交易创建后，用户在一定时间内未进行支付，可调用该接口直接将未付款的交易进行关闭。
     *
     * @param outTradeNo 商户订单号
     * @return 结果
     * @throws AlipayApiException 异常
     */
    @RequestMapping("/close/{outTradeNo}")
    public Object close(@PathVariable String outTradeNo) throws AlipayApiException {
        final AlipayTradeCloseModel closeModel = new AlipayTradeCloseModel();
        closeModel.setOutTradeNo(outTradeNo);

        final AlipayTradeCloseRequest closeRequest = new AlipayTradeCloseRequest();
        closeRequest.setBizModel(closeModel);

        return alipayClient.execute(closeRequest);
    }

}
