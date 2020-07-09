> [toc]

[设计模式参考视频](https://www.bilibili.com/video/BV1jt41137zK)  
[设计模式最强专栏](https://blog.csdn.net/LoveLion/article/details/17517213)

[练习Demo：design-patterns](https://github.com/cuiweiman/wang-wen-jun.git)

## 1. 引入
设计模式用于在特定的条件下为一些重复出现的软件设计问题提供合理的、有效的解决方案。
### 设计模式分类
- **创建型模式：** 描述怎样创建对象，主要特点是将对象的创建与使用分离。包含：简单工厂模式、工厂方法模式、抽象工厂模式、单例模式、原型模式、建造者模式。
- **结构型模式：** 用于描述如何将类或对象按某种布局组成更大的结构。包括：适配器模式、桥接模式、组合模式、装饰模式、外观模式、享元模式、代理模式。
- **行为型模式：** 包括：责任链模式、命令模式、解释器模式、迭代器模式、中介者模式、备忘录模式、观察者模式、状态模式、策略模式、模板方法模式、访问者模式。

### 六大原则
- **[总原则——开闭原则：](https://blog.csdn.net/LoveLion/article/details/7537584)** 软件实体应当对扩展开放，对修改关闭。尽量在不修改原代码的情况下进行扩展。（抽象化是开闭原则的关键）
- **[单一职责原则：](https://blog.csdn.net/lovelion/article/details/7536542)** 一个类只负责一个功能领域中的相应职责。是实现高内聚、低耦合的指导方针。
- **[里氏替换原则：](https://blog.csdn.net/lovelion/article/details/7540445)** 所有引用基类的地方，必须能透明地使用其子类对象。（代码中子类可以替换父类，程序不会出错）（子类可以扩展父类的功能，但不改变父类原有的功能。）
- **[依赖倒转原则：](https://blog.csdn.net/LoveLion/article/details/7562783)** 面向接口编程，而不是具体。(尽量引用层次搞得抽象层类，不要用具体类)
- **[接口隔离原则：](https://blog.csdn.net/LoveLion/article/details/7562842)** 使用多个专门的接口，而不是一个总接口。
- **[迪米特法则（最少知道原则）：](https://blog.csdn.net/LoveLion/article/details/7563445)** 一个类应当尽可能少地与其他实体发生相互作用。减少对象之间的交互或通信，降低系统类与类之间的耦合度。
- **[合成复用原则：](https://blog.csdn.net/LoveLion/article/details/7563441)** 尽量使用对象组合，而不是继承来达到复用的目的。

## 2. 创建型模式
### 2.1 简单工厂模式
不属于GoF 23中设计i模式，但是使用频繁。  
- **定义：** 创建一个工厂类，它可以根据参数的不同返回不同类的实例，被创建的实例通常都具有共同的父类。因为在简单工厂模式中用于创建实例的方法是静态(static)方法，因此简单工厂模式又被称为静态工厂方法(Static Factory Method)模式，它属于类创建型模式。
- **优点：** 
	1. 工厂类包含必要的判断逻辑；
	2. 客户端无需知道创建的产品类的类名，只需要知道类所对应的参数即可。
	3. 通过引入配置文件，可以在不修改客户端代码的情况下更换或增加新的具体产品类。
-  **缺点：** 
	1. 工厂类职责过重，一旦发生异常，整个系统都会受到影响。
	2. 简单工厂模式增加新的类，增加了系统的复杂度和理解难度。
	3. 系统扩展困难，一旦添加新类就需要修改工厂逻辑。
	4. 简单工厂模式由于使用了静态工厂方法，工厂角色无法形成基于继承的等级结构。
- **适用场景：**
	1. 工厂类负责创建的对象较少；
	2. 客户端只知道传入工厂类的参数，对如何创建对象并不关心。

```
graph BT
ConCreateProductA--> Product
ConCreateProductB --> Product
Factory --> ConCreateProductA
Factory --> ConCreateProductB
```
### 2.2 工厂方法模式
简单工厂模式的弊端：工厂类职责较重，引入新类时需要修改工厂类代码，违背了“开闭原则”。为解决这个问题，工厂方法模式应运而生。
- **定义：** 定义一个用于创建对象的接口，让子类决定将哪一个类实例化。即提供一个抽象工厂接口并声明抽象工厂方法，其子类具体实现工厂方法，创建产品对象。
- **优点：** 
	1. 工厂方法模式隐藏了类实例化的细节，无需关心类的创建细节与类名。
	2. 抽象工厂类与工厂实现类 可以使工厂自主确定创建类对象。
	3. 扩展添加新类时无需修改原代码，只需添加一个工厂实现类即可。符合“开闭原则”。

- **缺点：** 
	1. 扩展新类时，需要添加新的抽象工厂实现类。
	2. 实现时可能要用到反射等技术，增加了系统的实现难度。
	
- **适用场景：** 不需要知道类名，只需要知道对应的工厂即可。具体的类由具体的工厂创建；抽象工厂类通过子类来指定创建的对象，利用了面向对象的多态性和里氏替换原则。共容易扩展。

### 2.3 抽象工厂模式
抽象工厂模式为创建一组对象提供了解决方案。与工厂方法模式相比，抽象工厂模式的具体工厂不只是创建一种产品，而是负责创建一个系列的产品。
- **定义：** 提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。
- **优点：** 
	1. 抽象工厂模式隔离了具体类的生成，使得客户并不需要知道什么被创建。由于这种隔离，更换一个具体工厂就变得相对容易，所有的具体工厂都实现了抽象工厂中定义的那些公共接口，因此只需改变具体工厂的实例，就可以在某种程度上改变整个软件系统的行为。
	2. 当一个产品族中的多个对象被设计成一起工作时，它能够保证客户端始终只使用同一个产品族中的对象。
	3. 增加新的产品族很方便，无须修改已有系统，符合“开闭原则”。

- **缺点：** 增加新的产品等级结构麻烦，需要对原有系统进行较大的修改，甚至需要修改抽象层代码，这显然会带来较大的不便，违背了“开闭原则”。

### 2.4 单例模式
- **定义：** 确保某一个类只有一个实例，而且自行实例化并向整个系统提供这个实例，这个类称为单例类，它提供全局访问的方法。三个要点：
	1. 该类只有一个实例；
	2. 自行创建实例；
	3. 向整个系统提供实例。

- **优点：**
	1. 提供了对唯一实例的受控访问。
	2. 系统内存只存在一个对象，节约了系统资源。
	3. 允许可变数目的实例。通过单例控制相似的方法获得指定个数的对象实例，既节省系统资源，又避免了单例对象共享过多 有损性能的问题。

- **缺点：**
	1. 没有抽象层，不便于扩展；
	2. 单例类职责过重，违背 “单一职责原则”；
	3. 实例化的共享对象长时间没被使用，会被垃圾回收器认定成垃圾而销毁并回收。下次利用时有需要重新实例化，将导致共享的单例对象状态的丢失。

#### **饿汉式**
```java
public class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();
    /**
     * 需要将 构造方法私有化，不允许外部调用构造方法创建本类
     */
    private EagerSingleton() {
    }
    public static EagerSingleton getInstance() {
        return instance;
    }
}
```

#### **懒汉式 / 延迟加载**
实例：为了避免多线程调用getInstance()方法，在方法上使用了 synchronized 关键字。但是此时会降低系统性能。
```java
public class LazySingleton {
    private static LazySingleton instance = null;
    private LazySingleton() { }
    public synchronized static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
```
改进1：synchronized关键字用在方法中。但仍存在问题：多线程同时判断 instance=null，那么线程A获取锁，线程B等待，等线程A创建实例后，线程B获取锁再次创建了实例，就不符合单例了。
```java
public class LazySingleton {
    private static LazySingleton instance = null;
    private LazySingleton() { }
    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                instance = new LazySingleton();
            }
        }
        return instance;
    }
}
```
改进2：***双重检查锁定（Double-Check Locking）***
```java
public class LazySingleton {
    private static LazySingleton instance = null;
    private LazySingleton() { }
    public static LazySingleton getInstance() {
        // 第一重 判断
        if (instance == null) {
            // 锁定代码块
            synchronized (LazySingleton.class) {
                // 第二重 判断
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}
```

#### 饿汉式与懒汉式单例类的比较
- ***饿汉式*** 在类加载时自行实例化，无需考虑多线程问题，可以确保实例唯一性。调用速度和响应时间会优于懒汉式。但是从资源利用效率分析，系统加载时就需要创建饿汉式单例对象，加载时间会比较长。
- ***饿汉式*** 在首次使用时才实例化，无需一直占用系统资源，实现了延迟加载，但是必须处理好多线程同时访问的问题。而双重检测机制会使系统性能受到一定影响。

#### Initialization Demand Holder（IoDH）单例模式
可以实现对象的延迟加载，并在保证线程安全的情况下使系统性能不受到影响。
> 静态单例对象  instance 没有作为 Singleton 的成员变量而直接实例化，因此类加载时不会实例化。在首次调用getInstance方法时，内部类HolderClass中定义了static类型的变量instance，此时会首先初始化这个成员变量，并且由Java虚拟机来保证其线程安全性，确保该成员变量只能初始化一次。由于没有任何线程锁定，因此不会对性能造成任何影响。
```java
public class Singleton {
    private Singleton() {}
    private static class HolderClass {
        private final static Singleton instance = new Singleton();
    }
    // 静态内部类 具备外部类的特性
    public static Singleton getInstance() {
        return HolderClass.instance;
    }
    public static void main(String[] args) {
        Singleton s1, s2;
        s1 = Singleton.getInstance();
        s2 = Singleton.getInstance();
        System.out.println(s1 == s2);
    }
}
```

### 2.5 原型模式
***通过一个原型对象，克隆出多个一模一样的对象。***
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConCreatePrototype implements Cloneable {
    private String attr;
    @Override
    public ConCreatePrototype clone() {
        ConCreatePrototype prototype = null;
        try {
            prototype = (ConCreatePrototype) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return prototype;
    }
    public static void main(String[] args) {
        ConCreatePrototype obj1 = new ConCreatePrototype("原型模式");
        ConCreatePrototype obj2 = obj1.clone();
        System.out.println(obj2.getAttr());

    }
}
```
#### 浅拷贝
如果原型对象的成员变量是值类型，将复制一份给克隆对象；如果原型对象的成员变量是引用类型，则将引用对象的地址复制一份给克隆对象，也就是说原型对象和克隆对象的成员变量指向相同的内存地址。
```java
@Data
class Address {
    private String add;
    private String[] arr;
}

public class ShallowCopy implements Cloneable {
    private Address address = new Address();

    public void setAdd(String add) {
        this.address.setAdd(add);
    }

    public String getAdd() {
        return this.address.getAdd();
    }

    public void setArr(String[] arr) {
        this.address.setArr(arr);
    }

    public String[] getArr() {
        return this.address.getArr();
    }

    @Override
    protected ShallowCopy clone() {
        ShallowCopy shallow = null;
        try {
            shallow = (ShallowCopy) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return shallow;
    }

    public static void main(String[] args) {
        ShallowCopy a = new ShallowCopy();
        a.setAdd("北京市朝阳区");
        a.setArr(new String[]{"Hello", "Java", "World"});
        System.out.println(a.getAdd());
        System.out.println(Arrays.asList(a.getArr()));

        ShallowCopy b = a.clone();
        System.out.println(b.getAdd());
        System.out.println(Arrays.asList(b.getArr()));

        System.out.println("======= 浅拷贝 拷贝的是 引用对象的地址 =======");
        b.setArr(new String[]{"Php", "Guava", "Python"});
        System.out.println(Arrays.asList(a.getArr()));
        System.out.println(Arrays.asList(b.getArr()));
    }
}
```
#### 深拷贝
无论原型对象的成员变量是值类型还是引用类型，都将复制一份给克隆对象，即深克隆将原型对象的引用对象也复制一份给克隆对象。
```java
@Data
@AllArgsConstructor
class Info implements Cloneable {
    private String add;
    private String[] arr;
    @Override
    protected Info clone() throws CloneNotSupportedException {
        return (Info) super.clone();
    }
}
@Data
public class DeepCopy implements Cloneable {
    public Info info;
    @Override
    protected DeepCopy clone() throws CloneNotSupportedException {
        DeepCopy deepCopy = (DeepCopy) super.clone();
        deepCopy.info = info.clone();
        return deepCopy;
    }
    public static void main(String[] args) throws CloneNotSupportedException {
        DeepCopy a = new DeepCopy();
        a.setInfo(new Info("上海市静安区", new String[]{"CSDN", "Byte", "BiliBili"}));
        DeepCopy b = a.clone();
        System.out.println(JSONObject.toJSONString(b));
        b.getInfo().setAdd("天津市和平区");
        b.getInfo().setArr(new String[]{"五大道", "小白楼"});

        System.out.println("a= " + JSONObject.toJSONString(a));
        System.out.println("b= " + JSONObject.toJSONString(b));
    }
}
```

### 2.6 建造者模式
- **定义：** 将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。














