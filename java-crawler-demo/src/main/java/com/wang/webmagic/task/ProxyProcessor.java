package com.wang.webmagic.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/**
 * @description: 使用代理
 * @author: wei·man cui
 * @date: 2020/11/16 14:59
 */
@Component
public class ProxyProcessor implements PageProcessor {

    private Site site = Site.me();

    @Scheduled(fixedDelay = 1000)
    public void process() {
        // 创建下载器，设置代理服务器
        HttpClientDownloader downloader = new HttpClientDownloader();
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("61.160.210.234", 808)));
        Spider.create(new ProxyProcessor())
                .addUrl("https://www.51job.com/")
                .setDownloader(downloader)
                .run();
    }

    @Override
    public void process(Page page) {
        System.out.println(page.getHtml().toString());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
