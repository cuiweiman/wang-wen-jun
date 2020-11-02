package com.wang.crawlerdemo.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.crawlerdemo.entity.JdItem;
import com.wang.crawlerdemo.service.IJdItemService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @description: 定时抓取数据
 * @author: wei·man cui
 * @date: 2020/11/2 10:34
 */
@Component
public class JdItemTask {

    @Resource
    private HttpClientComponent clientComponent;

    @Resource
    private IJdItemService jdItemService;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 每1分钟执行一次
     */
    @Scheduled(fixedDelay = 1000 * 60)
    public void process() throws Exception {
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&cid2=653&cid3=655&s=5760&click=0&page=";
        //遍历执行，获取所有的数据
        for (int i = 1; i < 10; i = i + 2) {
            //发起请求进行访问，获取页面数据,先访问第一页
            String html = this.clientComponent.getHtml(url + i);
            //解析页面数据，保存数据到数据库中
            this.parseHtml(html);
        }
        System.out.println("执行完成");
    }

    /**
     * 解析页面，并把数据保存到数据库中
     */
    private void parseHtml(String html) throws Exception {
        //使用jsoup解析页面
        Document document = Jsoup.parse(html);
        //获取商品数据
        Elements spus = document.select("div#J_goodsList > ul > li");

        //遍历商品spu数据
        for (Element spuEle : spus) {
            //获取商品spu
            Long spuId = Long.parseLong(spuEle.attr("data-spu"));
            //获取商品sku数据
            Elements skus = spuEle.select("li.ps-item img");
            for (Element skuEle : skus) {
                //获取商品sku
                Long skuId = Long.parseLong(skuEle.attr("data-sku"));

                //判断商品是否被抓取过，可以根据sku判断
                JdItem param = new JdItem();
                param.setSku(skuId);
                List<JdItem> list = this.jdItemService.findAll(param);
                //判断是否查询到结果
                if (!CollectionUtils.isEmpty(list)) {
                    //如果有结果，表示商品已下载，进行下一次遍历
                    continue;
                }

                //保存商品数据，声明商品对象
                JdItem item = new JdItem();

                //商品spu
                item.setSpu(spuId);
                //商品sku
                item.setSku(skuId);
                //商品url地址
                item.setUrl("https://item.jd.com/" + skuId + ".html");
                //创建时间
                item.setCreated(new Date());
                //修改时间
                item.setUpdated(item.getCreated());


                //获取商品标题
                String itemHtml = this.clientComponent.getHtml(item.getUrl());
                String title = Jsoup.parse(itemHtml).select("div.sku-name").text();
                item.setTitle(title);

                //获取商品价格
                String priceUrl = "https://p.3.cn/prices/mgets?skuIds=J_" + skuId;
                String priceJson = this.clientComponent.getHtml(priceUrl);
                //解析json数据获取商品价格
                double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                item.setPrice(price);

                //获取图片地址
                String pic = "https:" + skuEle.attr("data-lazy-img").replace("/n9/", "/n1/");
                System.out.println(pic);
                //下载图片
                String picName = this.clientComponent.getImage(pic);
                item.setPic(picName);

                //保存商品数据
                this.jdItemService.save(item);
            }
        }
    }


}
