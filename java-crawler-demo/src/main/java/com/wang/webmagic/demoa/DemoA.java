package com.wang.webmagic.demoa;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * <p>
 *     页面元素抽取方式：XPath、CSS、正则表达式
 * </p>
 * @description: PageProcessor页面解析器
 * @author: wei·man cui
 * @date: 2020/11/2 11:21
 */
public class DemoA implements PageProcessor {
    @Override
    public void process(Page page) {
        // 解析 Html 页面. 选择的是：div 中 class=dt 标签下的 a标签

        // page.putField("xpathGetNavigator", page.getHtml().xpath("//div[@class=dt]/a/text()"));

        // page.putField("cssGetNavigator", page.getHtml().css("div.dt>a").all().toString());

        // page.putField("regexGetNavigator", page.getHtml().css("div.dt a").regex(".*我的.*").all());

        page.putField("regexGetNavigator", page.getHtml().links().all());

    }

    private Site site = Site.me()
            .setCharset("UTF-8")//编码
            .setSleepTime(1)//抓取间隔时间
            .setTimeOut(1000 * 10)//超时时间
            .setRetrySleepTime(3000)//重试时间
            .setRetryTimes(3);//重试次数


    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new DemoA())
                .addUrl("https://kuaibao.jd.com/")
                .thread(2)
                .addPipeline(new FilePipeline("D:\\result"))
                .run();
    }
}
