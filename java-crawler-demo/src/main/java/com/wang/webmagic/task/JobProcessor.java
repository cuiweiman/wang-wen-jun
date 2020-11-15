package com.wang.webmagic.task;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * @description: 爬虫工具
 * @date: 2020/11/15 20:09
 * @author: wei·man cui
 */
@Component
public class JobProcessor implements PageProcessor {

    private static final String url = "https://search.51job.com/list/170200,000000,0000,00,9,99,Java,2,1.html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";

    @SneakyThrows
    @Override
    public void process(Page page) {
        // 1. 解析页面，获取招聘信息详情的url地址。结果在js中，无法通过选择器获取到
        String html = page.getHtml().toString();



    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 编码、超时时间、重试间隔、重试次数
     */
    private Site site = Site.me()
            .setCharset("gbk")
            .setTimeOut(10 * 1000)
            .setRetrySleepTime(3000)
            .setRetryTimes(3);

    /**
     * 项目启动1s后开始执行任务
     * 定时任务 每隔100s执行一次
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
    public void process() {
        Spider.create(new JobProcessor())
                .addUrl(url)
                // 设置布隆过滤器
                .setScheduler(new QueueScheduler().setDuplicateRemover(
                        new BloomFilterDuplicateRemover(100000)
                ))
                .thread(10)
                .run();
    }


}
