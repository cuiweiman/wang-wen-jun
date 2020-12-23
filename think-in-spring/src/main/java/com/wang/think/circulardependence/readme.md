[很好地关于 循环依赖 的博文](https://blog.csdn.net/chen2526264/article/details/80673598)

> 当循环依赖的bean都是通过属性注入依赖的时候，根据 bean 的作用域是 singleton 还是 prototype，会有不同的表现：
> - 如果循环依赖的bean都是singleton，那么无论先获取哪个bean，都能成功。
> - 如果循环依赖的bean都是prototype，那么无论先获取哪个bean，都会失败。
> - 如果循环依赖的bean中有singleton，也有prototype，那么当先获取的那个bean是singleton时，就会成功，否则失败。
(在创建singleton 范围的bean 时，会提前暴露 ObjectFactory，供依赖的bean使用，而prototype范围不会。但是 prototype 可以禁用 循环依赖)


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

- setter循环依赖：com.wang.think.circulardependence.CircularSetterTest
> 通过setter注入方式构成的循环依赖。对于setter注入造成的依赖是通过 Spring 容器提前暴露刚完成构造器注入，但未完成其它步骤（如setter注入）的bean来完成的，而且只能解决单例作用域 bean 的循环依赖。通过提前暴露一个单例工厂方法，从而使其它bean 能引用到该 bean。
> 如：org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#addSingletonFactory

1. Spring 容器创建单例 testA bean，首先根据无参构造器创建 bean，并暴露一个 ObjectFactory 用于返回提前暴露的一个创建中的 bean，并将 testA 标识符放到 当前正在创建bean缓存池 中，然后进行 setter 注入 testB；
2. Spring 容器创建单例 testB bean，首先根据无参构造器创建 bean，并暴露一个 ObjectFactory 用于返回提前暴露的一个创建中的 bean，并将 testB 标识符放到 当前正在创建bean缓存池 中，然后进行 setter 注入 testC；
3. Spring 容器创建单例 testC bean，首先根据无参构造器创建 bean，并暴露一个 ObjectFactory 用于返回提前暴露的一个创建中的 bean，并将 testC 标识符放到 当前正在创建bean缓存池 中，然后进行 setter 注入 testA；
    在注入A时，由于提前暴露了 beanA 的 ObjectFactory，从而使它返回 一个 提前暴露的创建中的 bean，完成了 testC 的创建。
4. 然后在一次注入 testB、testA，完成 setter 注入。（循环依赖由于 testA 的 ObjectFactory 提前暴露，而打破。） 

- prototype 范围的依赖处理：com.wang.think.circulardependence.CircularPrototypeTest 
> prototype 作用域的bean，Spring容器无法完成依赖注入，因为 Spring 容器不缓存 prototype 作用域的 bean ，因此不会提前暴露一个创建中的 ObjectFactory。 

对于 prototype 作用域的bean 的循环依赖，可以通过 “setAllowCircularReferences(false)” 来禁用循环依赖。
org.springframework.context.support.AbstractRefreshableApplicationContext#allowCircularReferences



