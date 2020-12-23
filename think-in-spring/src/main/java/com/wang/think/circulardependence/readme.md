
## 循环依赖
### 定义
  循环依赖 即循环引用，就是两个或多个bean相互之间持有对方，比如 CircleA 引用 CircleB，CircleB应用 CircleC，CircleC引用CircleA，最终形成一个环。循环调用是无法解决的，除非有终结条件，否则就是死循环，最终导致内存溢出错误。

### Spring如何解决循环依赖
Spring 容器循环依赖包括 构造器循环依赖、Setter循环依赖。首先我们定义一个循环引用：TestA-TestB-TestC。

#### Spring中将循环依赖的处理分成3种情况
- 构造器循环依赖：com.wang.think.circulardependence.CircularConstructorTest
> 通过构造器注入构成的循环依赖，此依赖是无法解决的，只能抛出 BeanCurrentlyInCreationException 异常表示循环依赖。如：创建TestA时，构造器需要TestB，然后先构造TestB，发现TestB的构造器需要TestC，又先去构造TestC，发现构造器需要TestA，形成闭环，无法构建。

1. Spring 容器创建 testA，首先去 当前正在创建bean缓存池（singletonsCurrentlyInCreation）中查找是否当前bean正在创建，如果没发现，则继续准备其需要的构造参数 TestB，并将 TestA 标识符放到 当前正在创建bean池 中。
2. Spring 容器创建 testB，首先去 当前正在创建bean缓存池（singletonsCurrentlyInCreation）中查找是否当前bean正在创建，如果没发现，则继续准备其需要的构造参数 TestC，并将 TestB 标识符放到 当前正在创建bean池 中。
3. Spring 容器创建 testC，首先去 当前正在创建bean缓存池（singletonsCurrentlyInCreation）中查找是否当前bean正在创建，如果没发现，则继续准备其需要的构造参数 TestA，并将 TestC 标识符放到 当前正在创建bean池 中。
4. Spring 容器创建 testA，首先去 当前正在创建bean缓存池（singletonsCurrentlyInCreation）中查找是否当前bean正在创建，当前正在创建Bean池 中存在 TestA标识符，表示存在循环依赖，抛出 BeanCurrentlyInCreationException 异常。

Spring 容器将每一个正在创建的 bean 标识符 放在 “当前创建的bean池”中 ({@link org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#singletonsCurrentlyInCreation})，在创建 bean 过程中若发现 bean 已经在该缓存池中了，将抛出 BeanCurrentlyInCreationException 异常 表示循环依赖；而对于创建完毕的bean，将会从该容器池中移除掉。

- setter循环依赖



- prototype 范围的依赖处理 




