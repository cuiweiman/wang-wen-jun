
> [TOC]


[学习地址](https://coding.imooc.com/class/chapter/434.html);
[练习代码](https://github.com/cuiweiman/wang-wen-jun)

[官网](http://kafka.apache.org/);
[kafka调研信息](https://blog.csdn.net/steve_frank/article/details/109782212);
[组件概念](https://blog.csdn.net/lrxcmwy2/article/details/82853300);


# Kafka基础概念
> [Kafka](http://kafka.apache.org/)：分布式事件流平台，用于高性能数据管道、流式分析、数据集成和任务关键型应用程序。

- Topic是Kafka数据写入操作的基本单元，可以指定副本;
- 一个Topic包含一个或多个Partition，建Topic的时候可以手动指定Partition个数，个数与服务器个数相当
每条消息属于且仅属于一个Topic;
- Producer发布数据、Consumer订阅消息时，必须指定发布或订阅的那个Topic。

|功能|命令|
|:--|:--|
|启动kafka|bin/kafka-server-start.sh config/server.properties &|
|停止kafka|bin/kafka-server-stop.sh|
|创建Topic|bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test-topic-name|
|查看Topic信息|bin/kafka-topics.sh --list --zookeeper localhost:2181|
|发送消息到Topic|	bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic-name|
|从Topic中消费|bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic-name --from-beginning|

## Topic
> 虚拟概念，由1到多个 Partitions 组成，建Topic的时候可以手动指定Partition个数，个数与服务器(broker)个数相当。



**Topic管理命令**
```bash
# 查询Topic
bin/kafka-topics.sh --zookeeper localhost:2181 --list

# 创建 Topic
## --topic: 定义topic名
## --replication-factor: 定义副本数,与 brokers 个数有关
## --partitions: 定义分区数
## --zookeeper: 把 broker 注册信息存储到 zookeeper 集群
## --bootstrap-server: 把 broker 注册信息存储到 kafka 集群
bin/kafka-topics.sh --create --zookeeper localhost:2181 \
--replication-factor 1 --partitions 1 --topic first

bin/kafka-topics.sh --create --bootstrap-server localhost:9092 \
--replication-factor 1 --partitions 1 --topic second

# 查看 Topic 详情
bin/kafka-topics.sh --zookeeper localhost:2181  --describe --topic first
bin/kafka-topics.sh --describe --bootstrap-server localhost:9092

# 删除 Topic
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic first

```


## Partitions
> 实际消息存储单位

- 每个Partition只会在一个Broker上，物理上每个Partition对应的是一个文件夹
- Kafka默认使用的是hash进行分区，所以会出现不同的分区数据不一样的情况，但是partitioner是可以override的
Partition包含多个Segment，每个Segment对应一个文件，Segment可以手动指定大小，当Segment达到阈值时，将不再写数据，每个Segm- ent都是大小相同的
- Segment由多个不可变的记录组成，记录只会被append到Segment中，不会被单独删除或者修改，每个Segment中的Message数量不一定相等


## Message




## Kafka配置全解析
> [Kafka配置全解析](https://www.cnblogs.com/gxc2015/p/9835837.html)



# Kafka客户端
- **Producer客户端**：生产者API，发布消息到1至多个Topics。
- **Consumer客户端**：消费者API，订阅1至多个Topics并处理消息。
- **Connector客户端**：Kafka与数据库连接的API，从源系统或应用程序中直接拉取数据到Kafka。
- **StreamProcessors客户端**：流处理的API，高效地将输入流转换到输出流。
- **Admin客户端**：管理Kafka的客户端，允许管理和检测Topic、broker以及其他Kafka对象。

## Admin Client
> 管理Kafka的Topic和配置信息等。

```java
// 创建 Kafka Admin Client
AdminClient#create(properties)

// 查询所有的 Topics
AdminClient#listTopics();

// 创建 Topics
AdminClient#createTopics();

// 删除 Topics
AdminClient#deleteTopics();

// 获取 Topics 详情
AdminClient#describeTopics();

// 获取 Topics 配置详情
AdminClient#describeConfigs();
```


## Producer Client
**Producer 消息发送模式**
- **异步发送**：
- **同步发送**：实际上是“异步阻塞发送”，Producer发送信息后返回的是Future，当执行future.get()时就会进入阻塞，发送成功后才会继续执行程序。
- **异步回调发送**：异步发送成功后，异步回调（每个发送都包含一个Future）


**Producer 发送：**
> 源码：org.apache.kafka.clients.producer.KafkaProducer。

1. 先构造 KafkaProducer 对象：org.apache.kafka.clients.producer.KafkaProducer#KafkaProducer();
    > 1. 构建 ClientID，用于 Metric 的 kafka 监控；
    > 2. 初始化 消息Key、Value的 Serializer；
    > 3. 加载 分区的负载均衡器 Partitioner；
    > 4. 初始化 RecordAccumulator 计数器；
    > 5. 创建 Sender 对象：每构造一次 KafkaProducer，都会创建一次(new Sender)，可以说明 KafkaProducer 是线程安全的。


2. 消息发送：KafkaProducer#send(ProducerRecord<K,V>)
    > 1. 验证 Topic 存在；
    > 2. 负载均衡，决定消息发送到的分区 partition；
    > 3. 创建 Callback 回调对象，以及校验事务对象；
    > 4. RecordAccumulator 计算消息批次，消息条数大于批次后会放入新的批次。
    > 5. this.sender.wakeup(); 唤醒 Sender守护线程，进行消息发送。

    - 消息不是来一条发一条，而是批量发送的。 不停地创建批次，向批次中追加消息，唤醒线程进行消息发送。
    - ProducerConfig#LINGER_MS_CONFIG: 设置发送的间隔时间；
    - ProducerConfig#BATCH_SIZE_CONFIG：设置发送批次的消息条数。


![ProducerClient](https://cuiweiman.github.io/images/kafka/imooc/Producer.png)

![ProducerClient](https://cuiweiman.github.io/images/kafka/imooc/ProducerProcess.png)

![ProducerClient](https://cuiweiman.github.io/images/kafka/imooc/ProducerClient.png)
> retry 重试次数由 properties.put(ProducerConfig.RETRIES_CONFIG, "1"); 配置




## 三种消息传递保障机制
> 依赖于 Producer 和 Consumer，主要依赖于Producer：ProducerConfig.ACKS_CONFIG

- **最多一次**：消息最多发送一次（0~1次），消息只发送一次，并且不等待回调响应。
- **最少一次**：消息发送至少一次（1~多次），消息发送后，若没有回调响应，则消息重发（可能是网络延迟等）。
- **正好一次**：消息仅发送一次（只1次）。Producer 携带 ID 发送消息到 broker，若没有收到回调响应，则携带相同的ID再次发送，broker 接收消息时根据 ID 去重。

> Properties.put(ProducerConfig.ACKS_CONFIG,"all")：
> - 0：表示不需要等待server的回复；
> - 1：表示至少要等leader节点将数据写入本地log，不必等待follower写入成功。（若此时leader挂掉消息会丢失）；
> - all：leader要等待所有的节点都写入成功才回复确认信号，只要有一个备份存活就不会丢失数据，是最强保证。



## Consumer Client
- 单个 partition 的消息只能由 ConsumerGroup 中的某一个 Consumer 消费；
- Consumer 从 partition 中消费消息是顺序的，默认从头开始；
- 单个 ConsumerGroup 会消费多个 Partition 中的消息；

**Consumer 客户端每重新启动一次，都会从 offset = 0 开始消费 broker 中的消息**

> 一个 Consumer 可以消费一个/多个 Partition；同一个 Partition 不能被 同一个ConsumerGroup 下的多个 Consumer 消费。

**Consumer Group 的 Consumer 与 Topic 的 Partition 一对一**
![ConsumerClient](https://cuiweiman.github.io/images/kafka/imooc/ConsumerClient.png)

**Consumer Group 的 Consumer 监听多个 Partition 并将消息发给事件处理器（类似于Netty）**
![ConsumerClient2](https://cuiweiman.github.io/images/kafka/imooc/ConsumerClient2.png)


## Consumer 消费 令牌桶限流
> Kafka 是一个吞吐量很高的消息队列，除了spark、flink会使用较好的内存、CPU资源外，大部分业务系统都不会给kafka消费者提供较多的服务器资源。当出现消息峰值等其他情景时，Consumer可能会崩溃，因此要限流。

```java
// 根据 限流策略，达到阈值时通过以下方法，放弃指定 分区 的消息消费
org.apache.kafka.clients.consumer.KafkaConsumer#pause(Collection<TopicPartition>)

// 根据限流策略，当低于阈值时，通过以下方法继续消费指定的分区
org.apache.kafka.clients.consumer.KafkaConsumer#resume(Collection<TopicPartition>)
```


## Kafka Consumer 客户端限流
1. 接收到 record 信息后，从令牌桶中获取令牌；
2. 如果拿到令牌，则继续业务处理；
3. 若拿不到令牌，则 KafkaConsumer#pause 等待令牌；
4. 等令牌同中的令牌足够后，则 KafkaConsumer#resume 继续消费。


> **令牌桶算法限流**：
> - com.google.common.util.concurrent.RateLimiter。
> - Redis + LUA 脚本 实现令牌桶限流。


## Kafka消费者 加入 消费者组
![ConsumerJoinGroup](https://cuiweiman.github.io/images/kafka/imooc/ConsumerJoinGroup.png)



## Kafka消费者 异常退出 消费者组
![ConsumerExistError](https://cuiweiman.github.io/images/kafka/imooc/ConsumerExistError.png)


## Kafka消费者 正常退出 消费者组
![ConsumerExistNormal](https://cuiweiman.github.io/images/kafka/imooc/ConsumerExistNormal.png)

## Kafka消费者 加/离消费组时提交偏移量
![ConsumerCommitOffset](https://cuiweiman.github.io/images/kafka/imooc/ConsumerCommitOffset.png)
