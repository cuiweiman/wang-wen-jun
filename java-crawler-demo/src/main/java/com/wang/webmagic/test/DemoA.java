package com.wang.webmagic.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @description:
 * @author: wei·man cui
 * @date: 2020/11/2 11:21
 */
public class DemoA implements PageProcessor {
    @Override
    public void process(Page page) {
        page.putField("author", page.getHtml().css("div.mt>h1").all());
    }

    private Site site = Site.me();

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new DemoA())
                .addUrl("https://www.jd.com")
                .run();
    }
}
