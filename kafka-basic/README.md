
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
  Partition包含多个Segment，每个Segment对应一个文件，Segment可以手动指定大小，当Segment达到阈值时，将不再写数据，每个Segment都是大小相同的
- Segment由多个不可变的记录组成，记录只会被append到Segment中，不会被单独删除或者修改，每个Segment中的Message数量不一定相等


## Message

```bash
# Producer 向指定 Topic：stream_in 中发送消息
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic stream_in

# Consumer 消费指定 Topic：stream_out 中的消息，并按照语法对key和value进行解码
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
--topic stream_out \
--property print.key=true \
--property print.value=true \
--property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
--property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer \
--from-beginning
```



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




# Kafka Stream流计算

> 1. Kafka Stream 是处理分析存储在 Kafka 数据 的 客户端 程序库。
> 2. Kafka Stream 通过 state store 可以实现高效状态操作。
> 3. 支持原语 Processor 和 高层抽象 DSL (Storm/Spark SQL)。


**Kafka Stream 关键词**
- 流及流处理器；
- 流处理拓扑；
- 源处理器及Sink处理器。



**个人理解(Kafka完整高层架构图)**
1. Producer 发送的消息到一个Topic中；
2. 定义好流计算过程(类似于编辑好数据处理的规则引擎)，并启动。
3. Consumer 接收到 Topic 的消息，并经过 KafkaStream 流计算处理，由SinkProcessor处理器将处理好的消息发到Producer中
4. Producer再将处理好的消息发送到指定的另一个Topic中，由Consumer消费。
5. [可参考官网 Demo](https://kafka.apache.org/10/documentation/streams/quickstart)


![Kafka高层架构图](https://cuiweiman.github.io/images/kafka/imooc/KafkaStructure-1.png)


![Kafka拓扑图](https://cuiweiman.github.io/images/kafka/imooc/KafkaTopology.png)


![Kafka完整高层架构图](https://cuiweiman.github.io/images/kafka/imooc/KafkaStructure-2.png)


# Kafka Connect API

- Kafka Connect 是 Kafka 流式计算的一部分。
- Kafka Connect 主要用于与其它中间件建立流式通道。
- Kafka Connect 支持流式和批量处理集成。

> **Kafka Connect 在实际应用中不是很常用，类似于ELK中的Loglash，进行数据的集成、处理、流转等操作，但实际效果并不是很好。因此一般不会很常用。**

![KafkaConnect](https://cuiweiman.github.io/images/kafka/imooc/KafkaConnect-1.png)


## Sql与KafkaConnect集成Demo
1. 准备数据来源和数据目的地
```sql
CREATE DATABASE `kafka_study` DEFAULT CHARACTER SET utf8mb4;
-- 数据来源
CREATE TABLE `users` (
  `uuid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4

-- 数据输出
CREATE TABLE `users_bak` (
  `uuid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4

```
2. 配置kafka-connect并启动

略.

[https://www.confluent.io/hub/](www.confluent.io/hub)


**kafka-connect启动命令**
```bash
# kafka-connect 启动命令
## 普通启动-调试环境
bin/connect-distributed.sh config/connect-distributed.properties
## 后台运行-生产环境
bin/connect-distributed.sh -daemon config/connect-distributed.properties

# kafka-connect的端口号：8083，打开以下url可以查看是否启动成功
http://localhost:8083/connector-plugins
```
3. 添加 kafka-connect-source，支持动态添加。
4. 添加 kafka-connect-sink，支持动态添加。

```bash
查看 kafka-connect 中动态添加好的 connector：http://localhost:8083/connectors
```



# Kafka集群
1. Kafka天然支持集群
2. Kafka集群依赖于Zookeeper协调
3. Kafka集群通过brokerId区分不同节点

- **broker**: Kafka的部署节点，一个进程
- **Leader**: 处理消息的接收和消费等请求
- **Follower**: 主要用于备份消息数据。



## server.properties 配置
```bash
############################# Server Basics #############################
# broker id 整型，在集群中必须是唯一的
broker.id=0
# Kafka Server使用的协议、主机名以及端口，用于本地服务器监听
listeners=PLAINTEXT://localhost:9092
# 将 Broker 的 Listener 信息发布到 Zookeeper 供客户端使用监听，是真正的对外代理地址。
# 如果没有设置，会用 listeners。
advertised.listeners=PLAINTEXT://localhost:9092

# 接收/响应请求的线程数
num.network.threads=3
# 执行请求/（可能包括磁盘I/O）的线程数
num.io.threads=8

# 在介绍下面两个缓冲区设置之前，先来介绍下相关肯景知识:
# 每个TCP socket在内核中都有一个发送缓冲区(SO_SNDBUE )和一个接收缓冲区(SO_ RCVBUF)。接收缓冲区把数据缓存入内
# 核，应用进程一直没有调用read进行读取的话，此数据会一直缓存在相应socket的接收缓冲区内。需要注意，不管进程是否读取
# socket，对端发来的数据都会经由内核接收并且缓存到socket的内核接收缓冲区之中。read所做的工作，就是把内核缓冲区中
# 的数据复制到应用层用户的buffer里面，仅此而已。进程调用send发送的数据的时候，一般情况下，将数据复制进人socket的
# 内核发送缓冲区之中，然后send便会在上层返回。换句话说，send返回之时，数据不一定会发送到对端去，send仅仅是把应用
# buffer的数据复制进socket的内核发送buffer中

# 套接字服务器发送缓存区
socket.send.buffer.bytes=102400
# 套接字服务器接收缓存区
socket.receive.buffer.bytes=102400
# 请求的最大长度（针对OOM的保护）
socket.request.max.bytes=104857600

############################# Log Basics #############################
# 日志文件目录，多个逗号分隔形成目录列表。同一时间只能有一个进程访问
log.dirs=/tmp/kafka-logs
# 每个Topic默认的partition数量，默认值是1。
num.partitions=1
# 用来恢复log文件以及关闭时将log数据刷新到磁盘的线程数量，每个目录对应的线程数
num.recovery.threads.per.data.dir=1

############################# Log Flush Policy #############################
# 每隔多少个【消息】触发次flush操作，将内存中的消息刷新到硬盘上
log.flush.interval.messages=10000
# 每隔多少【毫秒】触发一次flush操作，将内存中的消息刷新到硬盘上
log.flush.interval.ms=1000
# 上面这两个配置是全局的，可以在Topic中重新设置，并覆盖这两个配置

############################# Log Retention Policy #############################
# 注意:下面有两种配置，一种是基于时间的策略，另种是基于日志文件大小的策略，两种策略同是配置的话，只要满足其中种策略，则触发Log删除的操作。删除操作总是先删除最旧的日志
# 消息在Kafka中保存的时间，168小时之前的1og， 可以被删除掉
log.retention.hours=168
# 当剩余空间低于log.retention.bytes字节，则开始删除1og
log.retention.bytes=1073741824
# segment日志文件大小的上限值。当超过这个值时，会创建新的segment日志文件，segment文件的相关信息在后面介绍log.segment.bytes=1073741824
# 每隔300000ms, logcleaner线程将检查一次，看是否符合上述保留策略的消息可以被删除
log.retention.check.interval.ms=300000

############################# Internal Topic Settings  #############################
##### 集群里几个节点设置为几个 #####
# kafka的内部topic consumer_offsets副本数，用于配置offset记录的topic的partition的副本个数
offsets.topic.replication.factor=1
# 事务主题的复制因子（设置更高以确保可用性）。 内部主题创建将失败，直到群集大小满足此复制因素要求。
transaction.state.log.replication.factor=1
# 覆盖事务主题的min.insync.replicas配置
transaction.state.log.min.isr=1

############################# Zookeeper #############################
# zookeeper 连接信息,多个逗号隔开
zookeeper.connect=localhost:2181,localhost:2182,localhost:2183
# zookeeper 连接超时时间
zookeeper.connection.timeout.ms=18000

############################# Group Coordinator Settings #############################
# coordinator协调器在空消费组接收到成员加入请求时，rebalance的延迟时间
group.initial.rebalance.delay.ms=0
```

## 参数必要性说明

**服务端必要参数：**
- zookeeper.connect: 必配参数，所有节点都应配置所有zk
- broker.id: 必配参数，集群节点标识ID，不得重复，0~n
- log.dirs：不使用默认的 /tmp/kafka-logs

**服务端推荐参数：**
- advertiesd.host.name: 注册到zk 供用户使用的主机名，内网无需配置
- advertiesd.port: 注册到zk供用户使用的服务端口
- num.partitions: 创建topic时默认的partition数量，默认1
- default.replication.factor:自动创建topic的默认副本数量，至少修改为2
- min.insync.replicasISR: 提交生成者请求的最小副本数，建议至少2~3个
- unclean.leader.election.enable:是否允许不具备ISR资格的replicas被选举为leader，建议false。
- controlled.shutdown.enbale: 在kafka收到stop命令或异常终止时，允许自动同步数据，建议开启。

**动态调整参数**
- unclean.leader.election.enable:不严格的leader选举，有助于集群健壮，但是存在数据丢失风险。
- min.insync.replicas: 若同步状态副本节点数小于该值，服务器不再接收 request.required.acks为-1或all的写入请求。
- max.message.bytes: 单条消息的最大长度。若修改了该值，那么 replica.fetch.max.bytes和消费者的fetch.message.max.bytes也要同步修改。
- cleanup.policy: 生命周期终结数据的处理，默认删除
- flush.messages: 强制刷新写入的最大缓存消息数。
- flush.ms: 强制刷新写入的最大等待时长。

## 服务器最佳配置
**JVM参数建议**
使用JVM的G1垃圾回收器
```bash
# 24GB内存的4核Intel 至强处理器，8*7200转的SATA盘
-Xmx6g -Xms6g -XX:MetaspaceSize=96m -XX:+UseG1GC -XX:MaxGCPauseMillis=29
-XX:InitiatingHeapOccupancyPercent=35 -X:G1HeapRegionSize=16m
-XX:MinMetaspaceFreeRatio=50 -XX:MaxMetaspaceFreeRatio=80
```

**操作系统调优**
- 内存：建议64G
- CPU：尽量多核，发挥多核更好的并发处理性能
- 磁盘：RAID优先推荐，SSD也可以考虑
- 网络：最好是万兆网络，千兆也可以
- 文件系统：ext4 最佳选择
- 操作系统：任何Unix系统均可运行良好

**服务器核心参数调整建议**
- 文件描述符数量调整：(number_of_partitions)*(partition_size/segment_size)  建议10,0000 以上。
- 最大套接字缓冲区大小：大一点
- pagecache：尽量分配与大多数日志的激活日志段大小一直。
- 禁用swap
- 设计broker的数量：单broker上的分区数<2000；分区大小 不超过 25G
- 设计partition的数量：至少和最大的消费者组中的consumer数量一致。分区小于25G。


## Kafka 副本集
- Kafka副本集是指将日志复制多份
- Kafka可以为每个Topic设置副本集
- Kafka可以通过配置，设置默认副本集数量


![KafkaBroker](https://cuiweiman.github.io/images/kafka/imooc/KafkaBroker.png)


![Kafka架构图](https://cuiweiman.github.io/images/kafka/imooc/KafkaTopology2.png)
> - Producer 表示两个生产者进程。
> - Kafka 的元数据信息都需要通过zookeeper获取。
> - Consumer Group 消费者组，Consumer组可以添加或移出Consumer。
> - brokers 集群中，同一个topic的partition，只能有一个leader，其他的作为follower备存存在，一起组成副本集。leader和follower尽可能保持数据一致，具有软一致性。
> - Producer 发送数据时，只会发送到 leader 中；Consumer 消费消息时，只会消费 leader 中的数据。


# Kafka运维

## 节点故障与选举
> 即 broker 故障

1. Kafka与zookeeper心跳未保持 视为节点故障；
2. follower消息落后leader太多 视为节点故障；


**Kafka对节点故障的处理**
- Kafka基本不会因节点故障而丢失数据；
- Kafka的语义担保(最多一次、最少一次、all)很大程度上避免数据丢失；
- Kafka会对消息进行集群平衡(主节点与备份节点不在同一broker)，减少消息在某节点热度过高；

### 集群Leader选举
- **Kafka没有采用『多数投票』来选举 Leader**
    - kafka中存在某个follower和leader消息数据量不一致；
    - 可能平票、弃票，造成多轮选举，浪费时间，kafka不适合。
- Kafka动态维护一组 Leader 数据的副本 ISR；
- Kafka会在ISR中选择一个速度比较快的设为 Leader。

### ISR副本全部宕机
**unclean leader 脏选举**
- 等待 ISR 中的节点恢复(牺牲时间)
- 在 ISR 以外的节点选举 Leader(可能丢失数据)


**生产环境中一般都会 禁用 unclean leader 选举**

**可以指定最小ISR，当ISR中存在节点宕机时，直到正常的节点数小于最小ISR时，数据无法写入**


## Kafka集群监控
- bin/Kafka-run-class.sh 等命令进行集群管理
- 图形工具 [CMAK(Cluster Manager for Apache Kafka)](https://github.com/yahoo/CMAK) 2.0.2版本
- 图形工具 [offset Explore2](https://www.kafkatool.com/download.html)

## Kafka的安全措施
- Kafka提供了 SSL 或 SASL 机制
- Kafka提供了 Broker 到 Zookeeper 链接的安全机制
- Kafka 支持Client的读写验证。

**内网自建证书实现SSL**
```bash
# 创建密钥仓库，存储证书文件
keytool -keystore server.keystore.jks -alias imoockafka -validity 3650 -genkey

# 创建 CA
openssl req -new -x509 -keyout ca-key -out ca-cert -days 3650

# 将CA证书添加到客户信任库
keytool -keystore client.truststore.jks -alias CARoot -import -file ca-cert

# 为broker提供信任库以及所有客户端签名了密钥的CA证书 
keytool -keystore server.truststore.jks -alias CARoot -import -file ca-cert

# 签名证书，用自己生成的CA来签名之前生成的证书
## 从密钥仓库导出证书
keytool -keystore server.keystore.jks -alias imoockafka -certreq -file cert-file
## 用CA签名
openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed -days 3650 -CAcreateserial -passin pass:cuiwei
## 导入CA证书和已签名的证书到密钥仓库
keytool -keystore server.keystore.jks -alias CARoot -import -file ca-cert
keytool -keystore server.keystore.jks -alias imoockafka -import -file cert-signed

```

**kafka配置SSL**
```bash
# kafka server.properties:
listeners=PLAINTEXT://127.0.0.1:9092,SSL://127.0.0.1:8989
advertiesd.listeners=PLAINTEXT://127.0.0.1:9092,SSL://127.0.0.1:8989
ssl.keystore.location=/ca-tmp/server.keystore.jkd
ssl.keystore.password=cuiwei
ssl.key.password=cuiwei
ssl.truststore.location=/ca-tmp/server.truststore.jkd
ssl.truststore.password=cuiwei
```

**测试SSL是否成功**
```bash
openssl s_client -debug -connect 127.0.0.0:8989 -tls1
```

**客户端配置**
```bash
security.protocol=SSL
ssl.endpoint.identification.algorithm=
ssl.truststore.location=/ca-tmp2/client.truststore.jks
ssl.truststore.password=cuiwei
```


# Kafka重要的底层原理
> kafka概念：分布式流处理平台
> kafka特性一：提供发布订阅及topic支持
> kafka特性二：吞吐量高，但并不保证消息有序(保证topic某个partition的消息有序)

1. 使用场景：是怎么使用kafka的？
```bash
1. 日志收集(elk在filebeat-logstash之间加一层kafka)或流式系统
2. 作为消息系统使用(订单管理-无序消息)，不保证消息有序
3. 用户活动跟踪或运营指标监控(例如调查问卷分析、分析用户等)
```
2. 与其他消息中间件的异同？
```bash
1. Kafka遵循一般的MQ结构(传统的遵从AMQP)，Consumer、Product、Broker。
2. 吞吐量大，主要用于处理活跃的流式数据,大数据量的数据处理。
3.1 Kafka采用zookeeper对集群中的broker、consumer进行管理，可以注册topic到zookeeper上；
3.2 通过zookeeper的协调机制，producer保存对应topic的broker信息，可以随机或者轮询发送到broker上；
3.3 并且producer可以基于语义指定分片，消息发送到broker的某分片上。
```

3. Kafka吞吐量大/速度快的原因？
```bash
1. 日志顺序读写和快速检索
2. partition机制
3. 批量发送、接收及数据压缩机制
4. 通过 sendfile 实现 零拷贝 原则
```

4. Kafka日志检索底层原理？

5. Kafka的sendfile零拷贝原理？
```bash
#  普通 数据传输
1. 磁盘文件读取到内核读取缓冲区（内核空间上下文）
2. 用户缓冲区（应用程序上下文）从内核读取缓冲区读取数据
3. Socket缓冲区（内核空间上下文） 从用户缓冲区 读取数据
4. 通过网卡接口，Socket发送到消费者进程

# 领拷贝 数据传输
1. 磁盘文件读取到内核读取缓冲区
2. 通过网卡接口，内核读取缓冲区 发送到消费者进程。
```
>   减少了系统的应用上下文切换，不再经过应用程序上下文。

6. 消费者组和消费者
```bash
- kafka消费者组是kafka消费的单位
- 单个partition只能由消费者组中的某个消费者消费
- 消费者组中的单个消费者可以消费多个Partition
```

7. Producer客户端

8. Kafka如何保证消息有序性？
```bash
Kafka 只支持 topic 中 单 Partition 有序，但是并行会失效，且也只能由一个 Consumer 消费。
使用 Kafka key+offset 做到业务上的全局有序。
```

9. Kafka Topic 删除的原理？
   建议 auto.create.topics.enable=false
   建议 delete.topic.enable=true
   建议 停掉 kafka 的数据流再删除 topic
   ![KafkaTopicDelete](https://cuiweiman.github.io/images/kafka/imooc/KafkaTopicDelete.png)
