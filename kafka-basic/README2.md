
[TOC]

[消息队列Kafka](https://www.bilibili.com/video/BV1a4411B7V9)

[视频笔记](https://my.oschina.net/jallenkwong/blog/4449224)

# Kafka基础

> Kafka 是一个『分布式』的『基于发布/订阅模式』的消息队列（Message Queue），主要应用于大数据实时处理领域。
> - 解耦：例如短信、邮件发送系统，与主业务程序解耦，当发送系统出现问题，也不会影响主业务程序
> - 异步通信能力
> - 流式处理：大数据消息流式规则进行处理，吞吐量大。

![Kafka架构](https://cuiweiman.github.io/images/kafka/bilibili/KafkaStructure.png)

1. Producer ： 消息生产者，就是向 Kafka ；
2. Consumer ： 消息消费者，向 Kafka broker 取消息的客户端；
3. Consumer Group （CG）： 消费者组，由多个 consumer 组成。 消费者组内每个消费者负责消费不同分区的数据，一个分区只能由一个组内的一个消费者消费；消费者组之间互不影响。 所有的消费者都属于某个消费者组，即消费者组是逻辑上的一个订阅者。
4. Broker ：一台 Kafka 服务器就是一个 broker。一个集群由多个 broker 组成。一个 broker可以容纳多个 topic。
5. Topic ： 主题，可以理解为一个队列， 生产者和消费者面向的都是一个 topic；
6. Partition： 为了实现扩展性，一个非常大的 topic 可以分布到多个 broker（即服务器）上，一个 topic 可以分为多个 partition，每个 partition 是一个有序的队列；
7. Replica： 副本（Replication），为保证集群中的某个节点发生故障时， 该节点上的 partition 数据不丢失，且 Kafka仍然能够继续工作， Kafka 提供了副本机制，一个 topic 的每个分区都有若干个副本，一个 leader 和若干个 follower。
8. Leader： 每个分区多个副本的“主”，生产者发送数据的对象，以及消费者消费数据的对象都是 leader。
9. Follower： 每个分区多个副本中的“从”，实时从 leader 中同步数据，保持和 leader 数据的同步。 leader 发生故障时，某个 Follower 会成为新的 leader。


# Kafka核心
## 工作流程

> Kafka中消息是以 Topic 进行分类的，Producer 生产消息，Consumer 消费消息，都面相 Topic。

![Kafka工作流程](https://cuiweiman.github.io/images/kafka/bilibili/KafkaCluster.png)

Topic是逻辑概念，Partition 是物理概念，每个 partition 对应一个 log 文件，该log文件存储的就是 Producer 产生的数据，（topic <=> N partition，partition <=> log file）。

Producer 生产的数据会被不断追加到该log 文件末端，且每条数据都有自己的 offset。 consumer组中的每个consumer， 都会实时记录自己消费到了哪个 offset，以便出错恢复时，从上次的位置继续消费。（producer -> log with offset -> consumer(s)）


## 数据与日志的文件存储
![Kafka文件划分](https://cuiweiman.github.io/images/kafka/bilibili/KafkaLogs.png)

由于生产者生产的消息会不断追加到 log 文件末尾， 为防止 log 文件过大导致数据定位效率低下， Kafka 采取了分片和索引机制，将每个 partition 分为多个 segment。

每个 segment对应两个文件——“.index”文件和“.log”文件。 这些文件位于一个文件夹下， 该文件夹的命名规则为： topic 名称+分区序号。例如， first 这个 topic 有三个分区，则其对应的文件夹为 first-0,first-1,first-2。


![Kafka文件存储](https://cuiweiman.github.io/images/kafka/bilibili/KafkaFiles.png)
index 和 log 文件以当前 segment 的第一条消息的 offset 命名。上图为 index 文件和 log 文件的结构示意图。

“.index”文件存储大量的索引信息，“.log”文件存储大量的数据，索引文件中的元数据指向对应数据文件中 message 的物理偏移地址。

## 生产者分区策略
1. 分区 方便在集群中扩展，每个 Partition 可以通过调整，适应它所在的机器，而一个 topic又可以有多个 Partition 组成，因此整个集群就可以适应适合的数据了；
2. 分区 可以提高并发，因为可以以 Partition 为单位读写了。（联想到ConcurrentHashMap在高并发环境下读写效率比HashTable的高效）

**Producer发送的数据封装成了一个 ProducerRecord 对象**
```java
public ProducerRecord(String topic, Integer partition, Long timestamp, K key, V value, Iterable<Header> headers)
public ProducerRecord(String topic, Integer partition, Long timestamp, K key, V value) 
public ProducerRecord(String topic, Integer partition, K key, V value, Iterable<Header> headers)
public ProducerRecord(String topic, Integer partition, K key, V value) 
public ProducerRecord(String topic, K key, V value)
public ProducerRecord(String topic, V value)
```
1. 指明 partition 时，直接将指明的值作为partition 值。
2. 没指明 partition 但有 key 时，将key的hash和topic的partition进行区域得到 partition 值。
3. 没有 key 和 partition时，【round-robin算法】第一次调用时随机生成一个整数，之后在这个整数的基础上自增。将这个值与topic可用的partition 总数取余得到 partition 值。

## Leader选举—ISR机制
> 为保证 producer 发送的数据，能可靠的发送到指定的 topic， topic 的每个 partition 收到producer 发送的数据后，都需要向 producer 发送 ack（acknowledgement 确认收到），如果producer 收到 ack， 就会进行下一轮的发送，否则重新发送数据。

- 何时发送 ack？
  确保有 follower 与 leader 同步完成，leader 再发送 ack，保证 leader 挂掉后，能在 follower 中选举新的 leader。
- 多少个 follower 同步完成后 发送 ack？
    1. 半数以上的 follower 同步完成即可发送 ack。
    2. 全部的 follower 同步完成，才可以发送 ack。

**副本数据同步策略**
|名称|方案|优点|缺点|
|:--|:--|:--|:--|
|多数投票|半数以上完成同步就ack|延迟低|选举新leader时，容忍n台故障，需要2n+1个副本|
|存储主节点副本|全部同步完成才ack|选举新leader时，容忍n台故障，需要n+1台副本|延迟高|
> **Kafka 选择第二种方案：**
> 1. 方案一需要 2n+1个副本，且Kafka每个分区都有大量数据，会有大量数据冗余。
> 2. 方案二虽然网络延迟高，但是网络延迟对 Kafka 的影响较小。

**ISR出现的场景**
> 采用方案二之后，设想以下情景： leader 收到数据，所有 follower 都开始同步数据，但有一个 follower，因为某种故障，迟迟不能与 leader 进行同步，那 leader 就要一直等下去，直到它完成同步，才能发送 ack。『死等』这个问题怎么解决呢？

Leader 维护了一个动态的 in-sync replica set (ISR)，意为和 leader 保持同步的 follower 集合。当 ISR 中的 follower 完成数据的同步之后，就会给 leader 发送 ack。如果 follower 长时间未向 leader 同步数据，则该 follower 将被踢出 ISR，该时间阈值由 replica.lag.time.max.ms 参数设定。 Leader 发生故障之后，就会从 ISR 中选举新的 leader。

## 生成者的ACK机制
**ack参数配置：**
- **0：** producer 不等待 broker 的 ack，可以提供最低的延迟， broker 一接收到还没有写入磁盘就返回，当 broker 故障时有可能丢失数据；
- **1：** producer 等待 broker 的 ack， partition 的 leader 落盘成功后返回 ack，如果在 follower 同步成功之前 leader 故障，那么将会丢失数据；
- **-1 即 all**： producer 等待 broker 的 ack， partition 的 leader 和 ISR 的 follower 全部落盘成功后才返回 ack。但是如果在 follower 同步完成后， broker 发送 ack 之前， leader 发生故障，那么会使 producer 重发，造成数据重复。

## ExactlyOnce恰好一次
- **ack = -1或all**：At least once 至少一次;
- **ack = 0**：At most once 至多一次;
- **Exactly Once**：恰好一次，数据不重复，也不丢失。

```math
At Least Once + 幂等性 = Exactly Once
```
要启用『幂等性』，只需要将 Producer 的参数中 enable.idempotence 设置为 true 即可。 Kafka 的幂等性实现是将原来下游需要做的去重放在了数据上游。

开启幂等性的 Producer 在初始化的时候会被分配一个 PID，发往同一 Partition 的消息会附带 Sequence Number。而 Broker 端会对 【PID, Partition, SeqNumber 】 做缓存，当具有相同主键的消息提交时， Broker 只会持久化一条。





## 节点故障时数据一致性机制
![数据一致性](https://cuiweiman.github.io/images/kafka/bilibili/DataConsistency.png)

- **follower 故障**：follower 发生故障后会被临时踢出 ISR，待该 follower 恢复后， follower 会读取本地磁盘记录的上次的 HW，并将 log 文件高于 HW 的部分截取掉，从 HW 开始向 leader 进行同步。等该 follower 的 LEO 大于等于该 Partition 的 HW，即 follower 追上 leader 之后，就可以重新加入 ISR 了。

- **Leader 故障**：leader 发生故障之后，会从 ISR 中选出一个新的 leader，之后，为保证多个副本之间的数据一致性， 其余的 follower 会先将各自的 log 文件高于 HW 的部分截掉，然后从新的 leader同步数据。

> **P.S.这只能保证副本之间的数据一致性，并不能保证数据不丢失或者不重复**


## 消费者分区分配策略
[Kafka再平衡机制详解](https://zhuanlan.zhihu.com/p/86718818)


> 由于 push（推）模式 **很难适应** 消费速率不同的消费者，因为消息发送速率是由 broker 决定的，

**consumer 采用 pull（拉） 模式从 broker 中读取数据。**
> 由于 消费者 pull 模式读取数据，当 kafka 没有数据时，消费者可能会陷入循环中，一直返回空数据。
> 对此，Kafka 消费者 在 pull 数据时会传入一个时长参数 timeout，没数据可消费时会等待 timeout 个时长再返回。


***Kafka 有两种分配策略：round-robin 策略、range 策略，对应配置参数 partition.assignment.strategy***

### Round Robin
主要采用的是一种轮询的方式分配所有的分区。

假设有三个topic：t0、t1和t2，分别拥有的partition数量分别为1、2和3，那么总共有六个分区，这六个分区分别为：t0-0、t1-0、t1-1、t2-0、t2-1和t2-2。这里假设有三个consumer：C0、C1和C2，它们订阅情况为：C0订阅t0，C1订阅t0和t1，C2订阅t0、t1和t2。那么这些分区的分配步骤如下：

1. 首先将所有的 partition 和 consumer 按照**字典顺序**进行排序（所谓的字典序，就是按照其名称的字符串顺序）；
2. 然后依次以按顺序轮询的方式将这六个分区分配给三个consumer，如果当前consumer没有订阅当前分区所在的topic，则轮询的判断下一个consumer：
3. 尝试将t0-0分配给C0，由于C0订阅了t0，因而可以分配成功；
4. 尝试将t1-0分配给C1，由于C1订阅了t1，因而可以分配成功；
5. 尝试将t1-1分配给C2，由于C2订阅了t1，因而可以分配成功；
6. 尝试将t2-0分配给C0，由于C0没有订阅t2，因而会轮询下一个consumer；
7. 尝试将t2-0分配给C1，由于C1没有订阅t2，因而会轮询下一个consumer；
8. 尝试将t2-0分配给C2，由于C2订阅了t2，因而可以分配成功；
9. 同理由于t2-1和t2-2所在的topic都没有被C0和C1所订阅，因而都不会分配成功，最终都会分配给C2。

按照上述的步骤将所有的分区都分配完毕之后，最终分区的订阅情况如下：
|分区|消费者|
|:--|:--|
|t0-0|C0|
|t1-0|C1|
|t1-1,t2-0,t2-1,t2-2|C2|

> 轮询的策略就是简单的将所有的 partition 和 consumer 按照字典序进行排序之后，然后依次将 partition 分配给各个 consumer，如果当前的 consumer 没有订阅当前的 partition，那么就会轮询下一个 consumer，直至最终将所有的分区都分配完毕。但是从上面的分配结果可以看出，轮询的方式会导致每个 consumer 所承载的分区数量不一致，从而导致各个 consumer 压力不均一。

### Range
首先计算各 个consumer 将会承载的分区数量，然后将指定数量的分区分配给该 consumer。假设有两个consumer：C0和C1，两个topic：t0和t1，这两个topic分别都有三个分区，那么总共的分区有六个：t0-0、t0-1、t0-2、t1-0、t1-1和t1-2。那么Range分配策略将会按照如下步骤进行分区的分配：

1. Range策略是按照 topic 依次进行分配的，比如从 t0 开始，其首先会获取t0的所有分区：t0-0、t0-1 和 t0-2，以及所有订阅了该 topic 的 consumer：C0 和 C1，并且会将这些 partition 和 consumer 按照**字典顺序**进行排序；
2. 然后按照平均分配的方式计算每个 consumer 会得到多少个分区，如果没有除尽，则会将多出来的分区依次计算到前面几个 consumer。比如这里是三个 partition 和两个 consumer ，那么每个 consumer 至少会得到 1个 partition，而3除以2后还余1，那么就会将多余的部分依次算到前面几个 consumer，也就是这里的 1 会分配给第一个 consumer。总结来说，C0 将会从第 0 个分区开始，分配 2 个分区，而 C1 将会从第 2 个分区开始，分配 1 个分区；

   |消费者|分区|
   |:--|:--|
   |C0|t0-0,T0-1|
   |C1|t0-2|
> 先计算，后分配

3. 同理，按照上面的步骤依次进行后面的topic的分配。最终上面六个分区的分配情况如下：

|消费者|分区|
|:--|:--|
|C0|t0-0,T0-1;T1-0,T1-1|
|C1|t0-2;T1-2|

### Sticky 分配策略
> 保证再分配时已经分配过的分区尽量保证其能够继续由当前正在消费的consumer继续消费，当然，前提是每个consumer所分配的分区数量都大致相同，这样能够保证每个consumer消费压力比较均衡。分配场景需要分为 **初始状态的分配** 和 **存在 Consumer 宕机时的分配**。

**初试状态分配**
特点：所有的分区都还未分配到任意一个consumer上。
排序方式：不按照字典顺序，而是 Topic 订阅数量的从小到大排序。如下示例：

初始状态分配的特点是，所有的分区都还未分配到任意一个consumer上。这里我们假设有三个consumer：C0、C1和C2，三个topic：t0、t1和t2，这三个topic分别有1、2和3个分区，那么总共的分区为：t0-0、t1-0、t1-1、t2-0、t2-1和t2-2。关于订阅情况，这里C0订阅了t0，C1订阅了t0和1，C2则订阅了t0、t1和t2。这里的分区分配规则如下：

|分区排序结果|订阅topic的Consumer数量|订阅的Consumer|
|:--|:--|:--|
|t2-0; t2-1; t2-2|1|C2|
|t1-0; t1-1|2|C1;C2|
|t0-0|3|C0;C1;C2|

1. 首先将t2-0尝试分配给C0，由于C0没有订阅t2，因而分配不成功，继续轮询下一个consumer；
2. 然后将t2-0尝试分配给C1，由于C1没有订阅t2，因而分配不成功，继续轮询下一个consumer；
3. 接着将t2-0尝试分配给C2，由于C2订阅了t2，因而分配成功；
4. 同理，t2-1、t2-2 也分配给 C2 成功。
5. 首先尝试将其分配给C0，由于C0没有订阅t1，因而分配不成功，继续轮询下一个consumer；
6. 然后尝试将t1-0分配给C1，由于C1订阅了t1，因而分配成功。
7. 分配t1-1，虽然C1和C2都订阅了t1，由于C1排在C2前面，因此该分区分配给C1。
8. 最后，t0-0分配给C0 成功。最终结果为：
   |消费者|分区|
   |:--|:--|
   |C0|T0-0|
   |C1|T1-0;T1-1|
   |C2|T2-0;T2-1;T2-2|

**模拟Consumer宕机**


## 消费者 offset 的存储
consumer 在消费过程中可能会出现断电宕机等故障， consumer 恢复后，需要从故障前的位置的继续消费，所以 **consumer 需要实时记录自己消费到了哪个 offset**，以便故障恢复后继续消费。

![消费者Offset](https://cuiweiman.github.io/images/kafka/bilibili/ConsumerOffset.png)

> Kafka 0.9 版本之前， consumer 默认将 offset 保存在 Zookeeper 中，从 0.9 版本开始，consumer 默认将 offset 保存在 Kafka 一个内置的 topic 中，该 topic 为__consumer_offsets。

1. 修改配置文件 consumer.properties，exclude.internal.topics=false。
2. 读取 offset
    1. 0.11.0.0 之前版本 - bin/kafka-console-consumer.sh --topic __consumer_offsets --zookeeper hadoop102:2181 --formatter "kafka.coordinator.GroupMetadataManager\$OffsetsMessageFormatter" --consumer.config config/consumer.properties --from-beginning
    2. 0.11.0.0 及之后版本 - bin/kafka-console-consumer.sh --topic __consumer_offsets --zookeeper hadoop102:2181 --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter" --consumer.config config/consumer.properties --from-beginning

## Kafka实现高吞吐量
1. 顺序性写磁盘。
   producer 生产数据，要写入到 log 文件中，写的过程是一直追加到文件末端，为顺序写。 官网有数据表明，同样的磁盘，顺序写能到 600M/s，而随机写只有 100K/s。这与磁盘的机械机构有关，顺序写之所以快，是因为其省去了大量磁头寻址的时间。
2. 通过 sendfile 实现 零拷贝。
3. 批量发送、接收及数据压缩机制。

## Zookeeper 在 Kafka 的作用
Kafka 集群中有一个 broker 会被选举为 Controller，负责管理集群 broker 的上下线，所有 topic 的分区副本分配和 leader 选举等工作。
而Controller 的管理工作都是依赖于 Zookeeper 的。


## Kafka的事务
> Kafka 从 0.11 版本开始引入了事务支持。事务可以保证 Kafka 在 Exactly Once 语义的基础上，生产和消费可以跨分区和会话，要么全部成功，要么全部失败。


### Producer 事务
为了实现跨分区跨会话的事务，需要引入一个全局唯一的 Transaction ID，并将 Producer 获得的PID 和Transaction ID 绑定。这样当Producer 重启后就可以通过正在进行的 TransactionID 获得原来的 PID。

为了管理 Transaction， Kafka 引入了一个新的组件 Transaction Coordinator。 Producer 就是通过和 Transaction Coordinator 交互获得 Transaction ID 对应的任务状态。 Transaction Coordinator 还负责将事务所有写入 Kafka 的一个内部 Topic，这样即使整个服务重启，由于事务状态得到保存，进行中的事务状态可以得到恢复，从而继续进行。

### Consumer 事务

上述事务机制主要是从 Producer 方面考虑，对于 Consumer 而言，事务的保证就会相对较弱，尤其时无法保证 Commit 的信息被精确消费。这是由于 Consumer 可以通过 offset 访问任意信息，而且不同的 Segment File 生命周期不同，同一事务的消息可能会出现重启后被删除的情况。

