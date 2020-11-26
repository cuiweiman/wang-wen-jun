package com.wang.es.utils;

import com.wang.es.entity.JdGoods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: Html页面解析 工具
 * @date: 2020/11/26 22:55
 * @author: wei·man cui
 */
@Component
public class HtmlParseUtil {

    public List<JdGoods> parseJdGood(String keyWord) throws IOException {
        String url = "https://search.jd.com/Search?keyword=" + keyWord;
        Document document = Jsoup.parse(new URL(url), 3000);
        Element jdGoods = document.getElementById("J_goodsList");
        Elements liElements = jdGoods.getElementsByTag("li");
        List<JdGoods> list = new ArrayList<>();
        for (Element element : liElements) {
            String goodId = element.attr("data-sku");
            String goodTitle = element.getElementsByClass("p-name").eq(0).text();
            String goodPrice = element.getElementsByClass("p-price").eq(0).text();
            // 图片特别多，通常是懒加载的，先加载内容，后加载图片。
            String goodImg = element.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String publishingHouse = element.getElementsByClass("p-shopnum").eq(0).text();
            JdGoods goods = new JdGoods(goodId, goodTitle, goodPrice, goodImg, publishingHouse);
            list.add(goods);
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        HtmlParseUtil util = new HtmlParseUtil();
        List<JdGoods> list = util.parseJdGood("心理学");
        System.out.println(list.size());
        list.forEach(System.out::println);
    }


}
