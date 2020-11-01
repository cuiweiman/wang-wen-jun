package com.wang.basic.crawler.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @description: HttpClient连接池管理
 * @date: 2020/11/1 22:19
 * @author: wei·man cui
 */
public class HttpClientPoolTest {

    PoolingHttpClientConnectionManager cm;

    public HttpClientPoolTest() {
        this.cm = new PoolingHttpClientConnectionManager();
        // 最大连接数
        cm.setMaxTotal(30);
        // 每台主机的最大连接数
        cm.setDefaultMaxPerRoute(10);
    }

    public void doGet(PoolingHttpClientConnectionManager cm) {
        // 从连接池中 获取 HttpClient客户端
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        HttpGet httpGet = new HttpGet("https://news.qq.com/");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HttpClientPoolTest poolTest = new HttpClientPoolTest();
        poolTest.doGet(poolTest.cm);
    }

}
