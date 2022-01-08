package com.wang.kafka.clients.adminclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.ConfigResource;
import org.assertj.core.util.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @description: admin client demo
 * @author: cuiweiman
 * @date: 2021/10/27 17:17
 * @see AdminSample#createAdminClient() 创建 kafka adminClient
 * @see AdminSample#listTopics() 查询所有的 Topics
 * @see AdminSample#createTopic() 创建 Topics
 * @see AdminSample#deleteTopic() 删除 Topics
 * @see AdminSample#describeTopics() 获取 Topics 详情
 * @see AdminSample#describeConfig() 获取 Topics 配置详情
 */
@Slf4j
public class AdminSample {

    public static final String TOPIC_NAME = "admin_topic";

    public static final String INPUT_TOPIC = "stream_in";
    public static final String OUTPUT_TOPIC = "stream_out";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        createTopic();
        // deleteTopic();
        // listTopics();
        describeTopics();
        // describeConfig();
    }

    /**
     * 查看配置详情
     * key = ConfigResource(type=TOPIC, name='admin_topic'),
     * value = Config(entries=[
     * ConfigEntry(name=compression.type, value=producer, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=STRING, documentation=null),
     * ConfigEntry(name=leader.replication.throttled.replicas, value=, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LIST, documentation=null),
     * ConfigEntry(name=message.downconversion.enable, value=true, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=BOOLEAN, documentation=null), ConfigEntry(name=min.insync.replicas, value=1, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=INT, documentation=null),
     * ConfigEntry(name=segment.jitter.ms, value=0, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null), ConfigEntry(name=cleanup.policy, value=delete, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LIST, documentation=null),
     * ConfigEntry(name=flush.ms, value=9223372036854775807, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null), ConfigEntry(name=follower.replication.throttled.replicas, value=, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LIST, documentation=null),
     * ConfigEntry(name=segment.bytes, value=1073741824, source=STATIC_BROKER_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=INT, documentation=null), ConfigEntry(name=retention.ms, value=604800000, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null),
     * ConfigEntry(name=flush.messages, value=9223372036854775807, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null), ConfigEntry(name=message.format.version, value=2.7-IV2, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=STRING, documentation=null),
     * ConfigEntry(name=file.delete.delay.ms, value=60000, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null), ConfigEntry(name=max.compaction.lag.ms, value=9223372036854775807, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null),
     * ConfigEntry(name=max.message.bytes, value=1048588, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=INT, documentation=null), ConfigEntry(name=min.compaction.lag.ms, value=0, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null),
     * ConfigEntry(name=message.timestamp.type, value=CreateTime, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=STRING, documentation=null),
     * ConfigEntry(name=preallocate, value=false, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=BOOLEAN, documentation=null), ConfigEntry(name=min.cleanable.dirty.ratio, value=0.5, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=DOUBLE, documentation=null),
     * ConfigEntry(name=index.interval.bytes, value=4096, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=INT, documentation=null), ConfigEntry(name=unclean.leader.election.enable, value=false, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=BOOLEAN, documentation=null),
     * ConfigEntry(name=retention.bytes, value=-1, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null), ConfigEntry(name=delete.retention.ms, value=86400000, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null),
     * ConfigEntry(name=segment.ms, value=604800000, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null), ConfigEntry(name=message.timestamp.difference.max.ms, value=9223372036854775807, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=LONG, documentation=null),
     * ConfigEntry(name=segment.index.bytes, value=10485760, source=DEFAULT_CONFIG, isSensitive=false, isReadOnly=false, synonyms=[], type=INT, documentation=null)
     * ])
     */
    public static void describeConfig() throws ExecutionException, InterruptedException {
        final AdminClient adminClient = createAdminClient();
        ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
        final DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Lists.newArrayList(resource));
        final Map<ConfigResource, Config> resourceConfigMap = describeConfigsResult.all().get();
        resourceConfigMap.forEach((k, v) -> {
            log.info("key = {}, value = {}", k, v);
        });
    }

    /**
     * topic 详情：
     * <span class="code">
     * (name=admin_topic,
     * internal=false,
     * partitions=(partition=0,leader=172.18.66.98:9092 (id: 0 rack: null),replicas=172.18.66.98:9092 (id: 0 rack: null),isr=172.18.66.98:9092 (id: 0 rack: null)),
     * authorizedOperations=null)
     * </span>
     * partitions 是 分区集合，包含partition,leader,replicas副本集,isr
     */
    public static void describeTopics() throws ExecutionException, InterruptedException {
        final AdminClient adminClient = createAdminClient();
        final DescribeTopicsResult describeTopics = adminClient.describeTopics(Lists.newArrayList(TOPIC_NAME));

        final Map<String, KafkaFuture<TopicDescription>> futureMap = describeTopics.values();
        log.info("describe: {}", futureMap.get(TOPIC_NAME).get());
    }

    /**
     * 删除 Topics
     */
    public static void deleteTopic() {
        final AdminClient adminClient = createAdminClient();
        final List<String> list = Lists.newArrayList("test-topic-name", "first");
        final DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(list);
        log.info("delete topic result: {}", deleteTopicsResult.topicNameValues().entrySet());
    }

    /**
     * 创建 Topics
     */
    public static void createTopic() {
        final AdminClient adminClient = createAdminClient();
        // 副本集数量
        short rs = 1;
        final NewTopic newTopic = new NewTopic(OUTPUT_TOPIC, 1, rs);
        final CreateTopicsResult topics = adminClient.createTopics(Lists.newArrayList(newTopic));
        log.info("create topics result: {}", topics);
    }

    /**
     * 查询所有的 Topics
     */
    public static void listTopics() throws ExecutionException, InterruptedException {
        final AdminClient adminClient = createAdminClient();
        // 查询 Topics
        final ListTopicsResult listTopicsResult = adminClient.listTopics();
        log.info("all topics: {}", listTopicsResult.names().get());

        // 查询所有的 Topics
        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(true);
        final ListTopicsResult listTopicsInternal = adminClient.listTopics(options);
        log.info("list topics name contain internal: {}", listTopicsInternal.names().get());
        final Collection<TopicListing> listings = listTopicsInternal.listings().get();
        log.info("list topics internal: {}", listings);
    }

    /**
     * 创建 AdminClient
     */
    public static AdminClient createAdminClient() {
        final Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return AdminClient.create(properties);
    }
}
