package com.wang.kafka.wechat.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wang.kafka.wechat.config.WeChatTemplateProperties;
import com.wang.kafka.wechat.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * @description: WeChatTemplate service
 * @author: cuiweiman
 * @date: 2021/11/8 15:47
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeChatTemplateService {

    private final WeChatTemplateProperties properties;

    private final Producer<String, Object> producer;

    private static final String TOPIC_NAME = "admin_topic";

    /**
     * 获取目前active为true的调查问卷模板
     */
    public WeChatTemplateProperties.WeChatTemplate getWeChatTemplate() {
        List<WeChatTemplateProperties.WeChatTemplate> templates = properties.getTemplates();
        Optional<WeChatTemplateProperties.WeChatTemplate> weChatTemplate
                = templates.stream().filter(WeChatTemplateProperties.WeChatTemplate::isActive).findFirst();
        return weChatTemplate.orElse(null);
    }

    public void templateReported(JSONObject reportInfo) {
        // kafka producer将数据推送至Kafka Topic
        log.info("templateReported : [{}]", reportInfo);
        // 发送Kafka数据
        String templateId = reportInfo.getString("templateId");
        JSONArray reportData = reportInfo.getJSONArray("result");
        // 如果 templateId 相同，后续在统计分析时，可以考虑将相同的id的内容放入同一个partition，便于分析使用
        ProducerRecord<String, Object> record =
                new ProducerRecord<>(TOPIC_NAME, templateId, reportData);
        /*
            1、Kafka Producer是线程安全的，建议多线程复用，如果每个线程都创建，出现大量的上下文切换或争抢的情况，影响Kafka效率
            2、Kafka Producer的key是一个很重要的内容：
                2.1 我们可以根据Key完成Partition的负载均衡
                2.2 合理的Key设计，可以让Flink、Spark Streaming之类的实时分析工具做更快速处理
            3、ack - all， kafka层面上就已经有了只有一次的消息投递保障，但是如果想真的不丢数据，最好自行处理异常
         */
        try {
            producer.send(record, (metadata, e) -> {
                log.info(" partition={}, offset={}", metadata.partition(), metadata.offset());
            });
        } catch (Exception e) {
            // 将数据加入重发队列， redis，es，...
        }
    }

    /**
     * 获取 统计结果
     */
    public JSONObject templateStatistics(String templateId) {
        // 判断数据结果获取类型:0-文件获取
        if (properties.getTemplateResultType() == 0) {
            return FileUtils.readFile2JsonObject(properties.getResultFilePath()).orElse(null);
        }
        return null;
    }
}
