package com.wang.basic.crawler.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @description: Java爬虫
 * @author: wei·man cui
 * @date: 2020/11/30 10:26
 */
public class JsoupDemo {

    public static void main(String[] args) throws IOException {
        String keyWord = "特朗普";
        crawler(keyWord);
    }

    public static void crawler(String keyWord) throws IOException {
        String url = "https://www.baidu.com/s?wd=" + URLEncoder.encode(keyWord, StandardCharsets.UTF_8.toString());
        Document document = Jsoup.parse(new URL(url), 3000);
        String title = document.getElementsByTag("title").first().text();
        System.out.println("网站title：" + title);
        System.out.println(document.html());
    }


}
