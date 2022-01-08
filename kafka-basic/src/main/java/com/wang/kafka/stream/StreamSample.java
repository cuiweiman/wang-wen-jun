package com.wang.kafka.stream;

import com.wang.kafka.clients.adminclient.AdminSample;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * Kafka流计算：类似于 数据处理的 规则引擎：
 * 可参考官网 Demo：https://kafka.apache.org/10/documentation/streams/quickstart
 *
 * 1. Producer 发送的消息到一个Topic中；
 * 2. 定义好流计算过程(类似于编辑好数据处理的规则引擎)，并启动。
 * 3. Consumer 接收到 Topic 的消息，并经过 KafkaStream 流计算处理，由SinkProcessor处理器将处理好的消息发到Producer中
 * 4. Producer再将处理好的消息发送到指定的另一个Topic中，由Consumer消费。
 * Kafka完整高层架构图：https://cuiweiman.github.io/images/kafka/imooc/KafkaStructure-2.png
 * <p>
 * 定义好流式处理器后，分别执行以下命令行：
 *
 * <pre class="code">
 * 生产者放数据：bin/kafka-console-producer.sh --broker-list localhost:9092 --topic stream_in
 * > Hello World C
 * > Hello Wold S
 * > Hello CS
 *
 * 消费者看结果（包括反序列化）：
 * bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
 * --topic stream_out \
 * --property print.key=true \
 * --property print.value=true \
 * --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
 * --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer \
 * --from-beginning
 * </pre>
 *
 * @description: 流计算Demo
 * @author: cuiweiman
 * @date: 2022/1/8 21:50
 */
public class StreamSample {

    /**
     * {@link AdminSample#createTopic()} 中先创建好 Topic
     */
    public static final String INPUT_TOPIC = "stream_in";
    public static final String OUTPUT_TOPIC = "stream_out";

    public static void main(String[] args) {
        // 配置 连接信息
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // 构建 Stream
        final StreamsBuilder builder = new StreamsBuilder();
        wordCountStream(builder);

        // 创建 KafkaStreams 并启动
        final KafkaStreams streams = new KafkaStreams(builder.build(), properties);

        final CountDownLatch latch = new CountDownLatch(1);

        // 在jvm中增加一个关闭的钩子，当jvm关闭的时候，会执行系统中已经设置的所有通过方法addShutdownHook添加的钩子。
        // 当系统执行完这些钩子后，jvm才会关闭。所以这些钩子可以在jvm关闭的时候进行内存清理、对象销毁等操作。
        Runtime.getRuntime().addShutdownHook(new Thread("streams-word-count-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        try {
            streams.start();
            latch.await();
        } catch (InterruptedException e) {
            System.exit(1);
        }
        System.exit(0);
    }

    /**
     * 构建 Word Count Stream 的 StreamBuilder 流计算
     */
    public static void wordCountStream(final StreamsBuilder builder) {
        // 不断从 INPUT_TOPIC 中获取数据，并追加到流上的一个抽象对象
        KStream<String, String> source = builder.stream(INPUT_TOPIC);
        // KTable 是数据集合的抽象对象
        // flatMapValues -> 将一行数据拆分为多行数据
        /*
            key 1 , value Hello   -> Hello 1  World 2
            key 2 , value World
            key 3 , value World
         */
        KTable<String, Long> count = source
                .flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" ")))
                // 合并 -> 按value值合并，并统计 value 出现的次数
                .groupBy((key, value) -> value).count();

        count.toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
    }

}


