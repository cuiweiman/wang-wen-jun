package com.wang.basic.crawler.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;

/**
 * @description: Jsoup Html解析器
 * @date: 2020/11/1 22:29
 * @author: wei·man cui
 */
public class JsoupTest {
    static String path = "E:\\news.html";

    /*public JsoupTest() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://news.qq.com/");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity());
            File file = new File(path);
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
            writer.close();
        }
    }*/

    public static void main(String[] args) throws Exception {
        JsoupTest instance = new JsoupTest();
        // instance.jsoupNet();
        instance.jsoupDocument();

    }

    public void jsoupNet() throws Exception {
        Document docNet = Jsoup.parse(new URL("https://news.qq.com/"), 1000);
        String title = docNet.getElementsByTag("title").first().text();
        System.out.println("网站title：" + title);
    }

    public void jsoupDocument() throws Exception {
        Document doc = Jsoup.parse(new File(path), "utf8");
        System.out.println(doc.getElementsByTag("title").first().text());

        Element topNav = doc.getElementById("TopNav");
        System.out.println(topNav.text());

        Element qqChannel = doc.getElementsByClass("qq_channel").first();

        Elements href = doc.getElementsByAttributeValue("href", "/upgrade.htm");

        System.out.println(href.text());

    }


}
