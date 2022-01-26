> [TOC]

[bilibili视频教程](https://www.bilibili.com/video/BV1to4y1C7gw)

> 另一个使用 Demo：think-in-springboot/src/main/java/com/wang/boot/zookeeper
> - ZK 作为消息队列
> - ZK 分布式锁

# Zookeeper概念

> Zookeeper 是一个开源的分布式的，为 分布式框架提供协调服务 的 Apache 项目——分布式协调系统。

从设计模式角度来理解: Zookeeper 是一个基 于 观察者模式 设计的 分布式服务管理框架，它负责 存储和管理数据，并接受观察者的 注册。一旦数据的状态发生变化，Zookeeper 就通知注册的观察者进行相应的反应。

```math
Zookeeper=文件系统+通知机制
```

## ZK特点

1. Zookeeper: 一个领导者(Leader)，多个跟随者(Follower)组成的集群。
2. 集群中只要有半数以上节点存活，Zookeeper集群就能正常服务。所以Zookeeper适合安装奇数台服务器。
3. 全局数据一致: 每个 Server 保存一份相同的数据副本，Client无论连接到哪个Server，数据都是一致的。
4. 更新请求顺序执行，来自同一个 Client 的更新请求按其发送顺序依次执行。
5. 数据更新原子性，一次数据更新要么成功，要么失败。
6. 实时性，在一定时间范围内，Client 能读到最新数据。
7. ZK的数据模型结构与 Unix 文件系统很类似，整体上可以看作是一棵树，每个 节点称做一个 ZNode。每一个 ZNode 默认能够存储 1MB 的数据，每个 ZNode 都可以通过 其路径唯一标识。
8. **Zookeeper 具有 强一致性，遵循 CP 原则。** zookeeper集群的所有节点进行数据同步时，会暂停客户端读写，不满足 可用性。

> CAP原则，指在一个分布式系统中，一致性（Consistency）、可用性（Availability）、分区容错性（Partition tolerance），这三个要素最多只能同时实现两点，不可能三者兼顾。

## znode节点类型

> 创建 znode 时设置顺序标识，znode 名称 后会附加一个值，顺序号是一个单调递增的计数 器，由父节点维护。 注意: 在分布式系统中，顺序号 可以被用于为所有的事件进行全局排序，这样客户端可以通过顺序号推断事件的顺序。

|节点类型|创建命令|节点含义| |:--|:--|:--| |**持久节点**|create path|客户端和服务器端断开连接后，创建的节点不删除| |**临时/短暂节点**|create -e
path|客户端和服务器端断开连接后，创建的节点自己删除。| |**有序号节点**|create -s path|普通持久节点+带序号|

## 应用场景

统一命名服务、统一配置管理、统一集群管理、服务器节点动态上下线、软负载均衡。

**统一命名服务**
在分布式环境下，经常需要对应用/服务进行统一命名，便于识别。例如: IP不容易记住，而域名容易记住。
![统一命名服务](https://cuiweiman.github.io/images/zookeeper/zk-use-name.png)

**统一配置管理**

1. 分布式环境下，配置文件同步非常常见。
    1. 一般要求一个集群中，所有节点的配置信息是 一致的，比如 Kafka 集群。
    2. 对配置文件修改后，希望能够快速同步到各个 节点上。
2. 配置管理可交由ZooKeeper实现。
    1. 可将配置信息写入ZooKeeper上的一个Znode
    2. 各个客户端服务器监听这个Znode。
    3. 一旦Znode中的数据被修改，ZooKeeper将通知 各个客户端服务器。

![统一配置管理](https://cuiweiman.github.io/images/zookeeper/zk-use-config.png)

**统一集群管理**

1. 分布式环境中，实时掌握每个节点的状态是必要的，zk可根据节点实时状态做出一些调整。
2. ZooKeeper可以实现实时监控节点状态变化
    1. 可将节点信息写入ZooKeeper上的一个ZNode。
    2. 监听这个ZNode可获取它的实时状态变化。

![统一集群管理](https://cuiweiman.github.io/images/zookeeper/zk-use-cluster.png)

**服务器节点动态上下线**
客户端能实时洞察到服务器上下线的变化。

1. 服务端启动时 在zk 中注册临时节点；
2. 客户端获取在线服务器列表，并注册监听；
3. 服务器节点下线时，zk 通知客户端节点下线事件；

**软负载均衡**
在Zookeeper中记录每台服务器的访问数，让访问数最少的服务器去处理最新的客户端请求。

![软负载均衡](https://cuiweiman.github.io/images/zookeeper/zk-use-balance.png)

## 安装与配置

[下载地址](https://www.apache.org/dyn/closer.cgi)

**配置参数**

```bash
# 通信心跳，服务器与客户端心跳时间，单位：ms
tickTime=2000
# Leader和Follower初始连接时容忍的最大心跳数-tickTime数量
initLimit=10
# Leader和Follower同步通信的最大心跳数-tickTime数量
syncLimit=5
# 存储 Zookeeper 数据，默认的tmp容易被linux定期删除
dataDir=/Users/cuiweiman/develop/zookeeper/node1/data
dataLogDir=/Users/cuiweiman/develop/zookeeper/node1/logs
# 客户端连接端口，默认2181
clientPort=2181
# 最大客户端连接数
# maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
# 
# 保留48小时的日志，以及20个快照文件
## 定时清理 dataDir 中 snapshot 的快照数量，默认3个。
# autopurge.snapRetainCount=20
## 定时清理 日志文件，默认1小时。设置为“0”可禁用自动清除功能。
# autopurge.purgeInterval=48

## Metrics Providers
#
# https://prometheus.io Metrics Exporter
#metricsProvider.className=org.apache.zookeeper.metrics.prometheus.PrometheusMetricsProvider
#metricsProvider.httpPort=7000
#metricsProvider.exportJvmInfo=true

# 集群节点配置，server.ID=IP:节点通讯端口:选举端口
server.1=127.0.0.1:2888:3888
server.2=127.0.0.1:2889:3889
server.3=127.0.0.1:2890:3890
```

> 查看主从状态 zkServer.sh status

## 常用命令

**zkCli 客户端常用命令**
|命令|释义| |:--|:--| |ls path|查看当前znode的子节点，-w监听变化，-s附加次级信息| |create|普通创建znode，-s含有序列，-e临时znode(重启或超时消失)| |get
path|获得节点值，-w监听节点内容变化，-s附加次级信息| |set path value|设置节点的具体值| |stat path|查看节点状态| |delete path|删除节点| |deleteall path|递归删除节点|

```bash
[zk: localhost:2181(CONNECTED) 14] ls -s /zookeeper
[config, quota]
cZxid = 0x0
ctime = Thu Jan 01 08:00:00 CST 1970
mZxid = 0x0
mtime = Thu Jan 01 08:00:00 CST 1970
pZxid = 0x0
cversion = -2
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 0
numChildren = 2
```

> - cZxid: 创建节点的事务 zxid。 每次修改 zk 状态都会产生一个事务ID，是zk中所有修改的总次序。若 zxid1 < zxid2，那么 zxid1 在 zxid2 之前发生。
> - ctime: znode被创建的毫秒数（从1970年开始）
> - mZxid: znode 最后更新的事务 zxid
> - mtime: znode最后修改的毫秒数（从1970年开始）
> - pZxid: znode 最后更新的子节点 zxid
> - cversion: znode 子节点变化号，znode 子节点修改次数
> - dataversion: znode 数据变化号
> - aclVersion: znode 访问控制列表的变化号
> - ephemeralOwner: 如果是临时节点，这个是 znode 拥有者的 session id。如果不是 临时节点则是 0。
> - dataLength: znode 的数据长度
> - numChildren: znode 子节点数量

# Zookeeper原理

## 选举机制

### 启动时初次选举

1. 服务器1启动，发起一次选举后投自己一票。此时服务器1票数一票，不够半数以上(3票)，选举无法完成，服务器1状态为 LOOKING。
2. 服务器2启动，发起一次选举后投自己一票。服务器1和2分别投自己一票并交换选票信息:
   此时服务器1发现服务器2的myid比自己大，更改选票为服务器2。此时服务器1票数0票，服务器2票数2票，没有半数以上结果，选举无法完成，服务器1，2状态保持LOOKING。
3. 服务器3启动，发起一次选举后投自己一票。此时服务器1和2都会更改选票为服务器3。此次投票结果:
   服务器1为0票，服务器2为0票，服务器3为3票。此时服务器3的票数超过半数，服务器3当选Leader。服务器1，2更改状态为FOLLOWING，服务器3更改状态为LEADING;
4. 服务器4启动，发起一次选举后投自己一票。此时服务器1，2，3已经不是LOOKING状态，不会更改选票信息。交换选票信息结果:服务器3为3票，服务器4为1票，
5. 服务器5启动，同服务器4一样。

![投票](https://cuiweiman.github.io/images/zookeeper/zk-vote-1.png)

### 宕机时选举

> 服务器运行期间无法和Leader保持连接，心跳超时或宕机。

而当一台机器进入 Leader 选举流程时，当前集群也可能会处于以下两种状态:

- 集群中本来就已经存在一个Leader(Leader没挂)。机器试图去选举 Leader 时，会被告知当前服务器的 Leader 信息，对于该机器来说，仅仅需要和 Leader 机器建立连 接，并进行状态同步即可。
- 集群中不存在 Leader 时， 假设如上图中，ZooKeeper 由 5 台服务器组成，SID 分别为 1、2、3、4、5，ZXID 分别为 8、8、8、7、7，并且此时 SID 为 3 的服务器是 Leader。某一时刻，3 和 5
  服务器出现故障，因此开始进行 Leader 选举。SID为1、2、4的机器信息为:
  |ServerID|Epoch|zxid| |:--|:--|:--| |1|8|1| |2|8|1| |4|7|1 最终 ServerID=2 的服务器选为 Leader。

> **选举规则：**
> **1. EPOCH大的直接胜出；**
> **2. EPOCH相同，事务ID(zxid)大的胜出；**
> **3. 事务ID相同，SID大的胜出；**

## 监听器原理

> **Zookeeper的监听器机制**
> - 客户端注册监听 它关心的 目录节点，当目录节点发生变化(数据改变、节点删除、子目 录节点增加删除)时，ZooKeeper 会通知客户端。
> - 监听机制保证 ZooKeeper 保存的任何的数据的任何改变都能快速的响应到监听了该节点的应用程序。

1. 客户端的 main 线程中 创建 Zookeeper 客户端，此时创建两个线程，一个负责网络通信connect，一个负责监听listener。
2. 通过 connect 线程将注册的监听事件发送给 Zookeeper。
3. Zookeeper 将监听事件 添加到 监听器列表中。
4. Zookeeper 监听到数据或路径变化，将信息通知到 listener线程。
5. listener 线程内部重写 process 方法处理。

> 这只是 客户端 注册监听器 的过程，算啥子原理哦。

```bash
# ls -w path 监听 znode 节点路径变化
# get -w path 监听 znode 节点值变化
[zk: localhost:2181(CONNECTED) 31] get -w /sanguo
luoguanzhong
# 修改 znode 的值
[zk: localhost:2181(CONNECTED) 32] set /sanguo "luoguanzhong&shinaian"
# 监听事件反馈
[zk: localhost:2181(CONNECTED) 33] 
WATCHER::

WatchedEvent state:SyncConnected type:NodeDataChanged path:/sanguo
```

## 客户端写入zk节点

![zk-write-leader](https://cuiweiman.github.io/images/zookeeper/zk-write-leader.png)

![zk-write-follower](https://cuiweiman.github.io/images/zookeeper/zk-write-follower.png)













