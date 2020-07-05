[CSDN学院：SpringBoot数据本地事务与多数据源事务 电商系统高阶实战](https://edu.csdn.net/course/detail/27429/366708)

## 1. 事务使用案例与定义（引入）
### 1.1 订单系统的事务 
**订单系统表结构：**  订单表——>订单明细表（商品的明细）  
**事务关系：**  下单时，首先写入订单表，再写入订单明细表，即
```sql
insert into orders(); -- 写入订单表
insert into orders_detail(); -- 写入订单明细表
insert into orders_detail(); -- 写入订单明细表（一种商品就需要插入一次明细表）
```
以上的SQL构成了一个事务，使得一系列操作构成一个整体。

### 1.2 交易支付系统中的事务
**支付系统表结构：**  支付系统支付完成——>订单系统修改支付状态——>扣减库存

```sql
update account balance = balance - ? where user_id=?; -- 账户系统扣钱
update orders set status=? where order_id=?; -- 修改订单的支付状态
update stores set balance = balance - ? where goods_id =?; -- 库存表扣库存
```
以上3个SQL构成了一个事务，使得一系列操作构成一个整体，避免因只执行部分，导致重大问题。

### 1.3 金融账户转账系统的事务
**转账表结构：** 转出账户扣钱——>收入账户价钱
```sql
update account balance = balance - ? where user_id = ?; -- 转出
update account balance = balance + ? where user_id = ?; -- 入账
```
以上2个SQL构成了一个事务，使得一系列操作构成一个整体。避免只执行了部分，导致重大问题。

### 1.4 关系型数据库事务的定义
**事务 定义：** 关系型数据库中，由一组SQL组成的执行单元，要么整体执行成功，要么整体执行失败。（不存在只执行了部分的情况。）

### 1.5 事务的四大特性【ACID】
- **原子性：** 执行单元是不可拆分的
- **一致性：** 事务前后，数据库的状态满足所有的完整性约束。（事务执行前后，状态守恒。事务执行前状态=执行后状态）
- **持久性：** 事务完成后，对结果进行持久化。
- **隔离性：** 多个并发事务相互独立、相互隔离、互不影响。

## 2. 数据库事务隔离级别
查看MySQL的事务隔离级别：默认——REPEATABLE-READ（可重复度）
```sql
SELECT @@global.transaction_isolation,@@transaction_isolation;
```
### 2.1 关系型数据库的四中事务隔离级别
**事务隔离的意义：对数据库事务并发时，采取的不同的数据读取策略。** 
1. **Read UnCommitted：** 读未提交，**一个线程 可以读取到 另一个线程 尚未提交 的 数据** 
2. **Read Committed：** 读已提交，**一个线程 只允许读取 另一个线程 提交过的 数据** 
3. **Repeatable Read：** 可重复度，**同一个线程中，多次数据查询都与该事务开始时的数据一致（不会读取到其他线程对该记录的数据修改操作）。** 与其他事务的数据操作隔离了。
4. **Serializable：** 串行化，**多个线程操作数据，串行执行（类似于单线程操作）**

### 2.2 事务并发访问 导致的数据读取问题
1. **脏读 Dirty Read：** 等价于 读未提交，即读到了尚未提交的数据。（若尚未提交的数据回滚了，那么读到的数据是错误的）。
2. **不可重复读 NonRepeatable Read：** 等价于 读已提交，事务A读取数据后，事务B修改了该数据，事务A再次读取时，与初始读到的数据不匹配。
3. **幻读 Phantom Read：** 可重复读 隔离级别导致的。事务A首先根据条件查询到N条数据，然后事务B改变了这N条数据之外的M条或者增添了M条符合事务A搜索条件的数据，导致事务A再次搜索发现有N+M条数据了，就产生了幻读。即第二次读到的数据，比第一次读到的多。
4. 不可重复读与幻读的比较：两者相似，但是前者更针对于 update或delete操作，而幻读更针对于insert操作。

### 2.3 数据库隔离级别面试题
数据库的隔离级别，本质上是解决 读锁和写锁的问题。
| 隔离级别 |  脏读  | 不可重复读 |  幻读  |
| :------: | :----: | :--------: | :----: |
| 读未提交 |  可能  |    可能    |  可能  |
| 读已提交 | 不可能 |    可能    |  可能  |
| 可重复读 | 不可能 |   不可能   |  可能  |
|  串行化  | 不可能 |   不可能   | 不可能 |

### 2.4 常见关系型数据库事务的默认隔离级别
- **SqlServer：** 读已提交 Read Commited  
- **Oracle：** 读已提交 Read Commited  
- **MySQL：** 可重复读 Reapeatable Read  

### 2.5 解决不可重复读的三种方法
1. 将数据库的隔离界别设置为：可重复读（不可重复读对应的隔离级别是：读已提交）。  
2. 将数据库的隔离级别设置为：串行化。  
3. 使用 select * from table for update; （ 独占锁、悲观锁、写锁）。将读锁转化为写锁。

### 2.6 读已提交、可重复读无法保证业务正确
- 读已提交：并发事务执行A有余额500转账100给B，事务1中A转给B100后尚未提交，事务2读到A余额500，转B100，而后两个事务并发提交，导致结果A余额400，正常应该为300。产生严重业务问题。
- 可重复读：同上情境中，并发事务2读到的数据一直都是余额500，数据与事务1隔离了，因此转账100后余额400。同样产生严重的业务问题。  
**因此要使用正确的业务事务编程模型，如：直接在sql中操作**
```sql
update account set balance = balance - 100 where user = A;
```

## 3. 实战——本地事务 订单系统环境搭建
[项目github地址：csdn-transaction-jpa](https://github.com/cuiweiman/wang-wen-jun)
- id自增做物理主键，是mysql聚簇索引必须要有的；
- order_id订单主键，假若分库分表，那么可以全局唯一；自增ID不安全。

## 4. Spring本地事务—— @Trabsactional
### 4.1  rollbackFor指定触发回滚的异常
- @Transactional  默认回滚异常是 RuntimeException（运行时异常）。
```java
@Transactional
public void saveOrder() {
    ......
    ordersRepository.save(orders);
    ......
    ordersDetailRepository.save(detail);
    // 抛出异常,事务回滚
    throw new RuntimeException();
}
```
- 如果抛出了Exception，那么事务是不会回滚的。在这种情况下 需要使用 rollbackFor 对 Transactional注解 配置 触发事务回滚的异常类型。
```java
@Transactional(rollbackFor = Exception.class)
public void saveOrder() throws Exception {
    ......
    ordersRepository.save(orders);
    ......
    ordersDetailRepository.save(detail);
    // 抛出异常,事务回滚
    throw new Exception();
}
```
**Transactional注解 部分源码** 
```java
/**
Transactional注解源码中，配置触发回滚异常 说明
*/
public @interface Transactional {
	// 配置事务回滚的异常类型，可以配置多个
    Class<? extends Throwable>[] rollbackFor() default {};

	// 配置事务回滚的异常类型，可以配置多个，使用 类名的字符串
    String[] rollbackForClassName() default {};

	// 配置事务 不回滚的异常类型
    Class<? extends Throwable>[] noRollbackFor() default {};
	// 配置事务 不回滚的异常类型，使用 类名的字符串
    String[] noRollbackForClassName() default {};
}
```

### 4.2 readOnly 开启只读事务
只读事务：只能进行查询的操作，不能写入。如果只读事务中有增删改操作，会抛出如下异常：  
could not execute statement; nested exception is org.hibernate.exception.GenericJDBCException: could not execute statement。
```java
@Transactional(readOnly = true)
public void saveOrder() throws Exception {
    ......
    ordersRepository.save(orders);
    ......
    ordersDetailRepository.save(detail);
}
```
### 4.3 timeout 事务超时回滚 优化性能
应用网络请求MySQL时，因为如下情景导致耗时长，从而导致事务超时：网络超时、网络抖动、事务本身非常庞大等。  
**设置事务的超时时间，是对数据库的一种保护** ，因为高并发场景下，事务时间过长，会导致MySQL长时间持有事务锁，不释放，严重会导致数据库崩溃。因此添加超时设置，可以对性能调优  
Spring的事务时间 = 事务开始时间 到 最后一个事务执行结束的时间。因此下例中的第二个休眠不会触发事务超时异常。
```java
// timeout = -1：表示永不超时。单位：秒
@Transactional(timeout = 2)
public void saveOrder() throws Exception {
    ......
    ordersRepository.save(orders);
    ......
    // Thread.sleep(2000L);
    ordersDetailRepository.save(detail);
    // 事务已经结束。不会触发 事务超时异常。
    Thread.sleep(2000L);
}
```

### 4.4 @Trabsactional失效的两种情况
- Transactional方法只对**public** 修饰的方法有效，private和protected修饰的方法都会失效。
- 同一个类中，使用其他方法调用被 Transactional 修饰的方法，那么被调用的方法的事务会失效。（若调用方法和被调用方法不在同一个类中，那么被调用的方法的事务仍然有效。）例如：
```java
public void saveOrder1(){
    // 类中内部方法调用，导致被调用方法的 事务失效
    saveOrder(100L);
}
@Transactional(rolbackFor=Exception.class)
public void saveOrder(Long orderId) {
    ......
    ordersRepository.save(orders);
    ......
    ordersDetailRepository.save(detail);
}
```
### 4.5 isolation 设置事务隔离级别
@Transactional(isolation = Isolation.REPEATABLE_READ)  
-  **Isolation.DEFAULT：** 与数据库的事务隔离级别相同；
- **Isolation.READ_UNCOMMITTED：** 读未提交；**性能太低，不要设置**   
-  **Isolation.READ_COMMITTED：** 读已提交；  
- **Isolation.REPEATABLE_READ：** 可重复读；  
-  **Isolation.SERIALIZABLE：** 串行化。**性能太高，不要设置，效率低**  

## 5. Spring本地事务传播机制 propagation
**事务传播机制：** 事务的运行环境。单个事务的运行环境、多个事务（嵌套事务）的运行环境。嵌套事务如：保存订单信息时，先调用订单服务保存订单表，再调用订单详情服务中保存订单详情的方法。
**事务的7个传播级别：** REQUIRED（默认）、SUPPORTS、MANDATORY、REQUIRES_NEW、NOT_SUPPORTED、NEVER、NESTED。

- **Propagation.REQUIRED：** 若当前运行环境没有事务，那么新建一个事务；若有事务，那么加入到该事务中运行（共用一个事务）。若有多个嵌套事务，那么会共用一个事务。**只要事务回滚，那么所有的嵌套事务都会回滚（多个事务合成一个事务）**  

- **Propagation.REQUIRES_NEW：** 每个事务，都会创建一个独立的新事务。
	1. 情景1：事务A使用 Propagation.REQUIRES，事务B使用 Propagation.REQUIRED_NEW，事务A创建一个事务，事务B也创建一个独立的事务，则A发生异常，那么 A回滚，B不回滚。
	2. 情景2：事务A使用 Propagation.REQUIRES_NEW，事务B使用 Propagation.REQUIRED，事务A创建一个事务，事务B会加入到事务A中，则A发生异常，A、B都会回滚。
	3. 情景3与情景4，即都用 REQUIRES 或 REQUIRES_NEW，很容易推导出当事务A异常时， 前者都会回滚，后者事务B不会回滚。
	
- **Propagation.SUPPORTS：** 当前环境有事务，就共用一个事务；若没有事务，那么不使用事务执行。自己不创建新事务。
	1. 情况1：事务A使用REQUIRES，事务B使用SUPPORTS，若A异常那么都回滚。
	2. 情况2：事务A没使用事务，事务B使用SUPPORTS，那么都没事务，都不回滚。

- **Propagation.MANDATORY：** 自己不创建新事务。当前环境有事务，就共用一个事务。若没有事务，那么就报错：IllegalTransactionStateException: No existing transaction found for transaction marked with propagation 'mandatory'。

- **Propagation.NOT_SUPPORTED：** 只能以 **非事务** 的方式运行，若当前环境有事务A，挂起事务A，自己以非事务方式运行结束，事务A再继续执行。（事务A异常时会回滚，但它不会回滚，因为没事务）。

- **Propagation.NEVER：** 以 **非事务** 方式运行，若当前环境有事务，那么就报错：IllegalTransactionStateException: Existing transaction found for transaction marked with propagation 'never'。

- **Propagation.NESTED：** (最难理解的)  若当前环境没有事务，那么创建新事务执行；若当前环境有事务，那么加入当前事务、以 **事务保留点** 的形式执行【嵌套事务】。  
    1. 情景1：事务A REQUIRED 正常提交，事务B NETSTED 正常提交，结果事务都正常提交。
    2. 情景2：事务A REQUIRED 异常后回滚，事务B NETSTED 正常提交，结果事务都进行了回滚。A中调用的B，A是主事务，B是子事务，因此当A回滚，那么B回滚。
    3. 情景3：事务A REQUIRED正常提交，事务B异常后回滚，那么事务A的操作正常提交了，事务B会回滚。

什么是事务保留点？（JPA不支持事务保留点）
> begin transaction; 开始事务  
> do sql1; 进行事务1   
> save savepoint savepoint1; -- 保存 事务保留点1  
> do sql2; 进行事务2  
> save savepoint savepoint2; -- 保存 事务保留点1  
> rollback savepoint1; -- sql2操作异常，回滚到事务保留点1，那么sql2的操作被回滚了，sql1的事务操作仍然生效。  
> commit; -- 提交了 事务1 的执行  

## 6. SpringBoot 多数据源事务管理
[项目github地址：csdn-transaction-jpa-mul](https://github.com/cuiweiman/wang-wen-jun)  
**电商系统多数据源架构部署：** 实际上是对数据库进行垂直拆分等操作，将单数据库拆分成：订单数据库orders、库存数据库stores 等。多数据源的事务管理默认情况下是相互独立的。  
订单库存系统数据库设计：stores.sql

### 6.1 SpringBoot 多数据源配置 事务管理器
- 配置 订单 数据库 事务管理器
```yml
spring:
  datasource:
    order:
      driverClassName: com.mysql.cj.jdbc.Driver
      jdbcUrl: jdbc:mysql://localhost:3306/orders?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
      username: root
      password: zxcvbnm123
      jpa.dialect: org.hibernate.dialect.MySQL5Dialect

    store:
      driverClassName: com.mysql.cj.jdbc.Driver
      jdbcUrl: jdbc:mysql://localhost:3306/orders?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false
      username: root
      password: zxcvbnm123
      jpa.dialect: org.hibernate.dialect.MySQL5Dialect
  jpa:
    show-sql: true
```
```java
@Configuration
@EnableJpaRepositories(basePackages = "com.wang.transaction.mul.stores.repository",entityManagerFactoryRef = "storeEntityManagerFactory",transactionManagerRef = "storeTransactionManager")
public class StoreJpaConfig {
    @Resource
    private Environment env;

    // 库存数据源.注意前缀与application.yml中的一致
    // Primary：标识 默认使用本数据源。否则 有两个相同的 DataSource Bean，SpringBoot启动会报错。
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.store")
    public DataSource storeDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean storeEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(storeDataSource());
        // 实体类 包路径
        factory.setPackagesToScan(new String[]{"com.wang.transaction.mul.stores.entity"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpa = new Properties();
        jpa.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        jpa.put("hibernate.dialect", env.getProperty("spring.datasource.store.jpa.dialect"));
        factory.setJpaProperties(jpa);
        return factory;
    }
    @Bean
    @Primary
    public PlatformTransactionManager storeTransactionManager() {
        EntityManagerFactory factory = storeEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }
}
```
- 配置库存数据库 事务管理器
基本同 订单数据库事务管理器的JpaConfig配置。注意不需要配置Primary注解；注意修改application.yml中的前缀。  

### 6.2 多数据源事务使用 transactionManager
多数据源情况下，使用 Transactional 注解，但是不配置 transactionManager 指定 事务管理器（事务管理器需要提前配置），那么将会在 非事务 的情况下执行（Transactional无效）。
```java
@Transactional(transactionManager = "orderTransactionManager")
```

### 6.3 SpringBoot关闭自动事务 enableDefaultTransactions
（事务默认是 自动开启 的）
在配置事务管理器时，在注解中设置属性 **enableDefaultTransactions = false** 。在读写分离中非常重要。  
**核心作用：** 设置 enableDefaultTransactions 属性为false后，若写/修改方法上不适用@Transactional注解，那么DB操作会抛出异常。

```sql
@Configuration
@EnableJpaRepositories(basePackages = "com.wang.transaction.mul.orders.repository",
        enableDefaultTransactions = false,
        entityManagerFactoryRef = "orderEntityManagerFactory",
        transactionManagerRef = "orderTransactionManager")
public class OrderJpaConfig {}
```