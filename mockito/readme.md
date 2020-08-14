[汪文君Mockito实战视频](https://www.bilibili.com/video/BV1jJ411A7Sv)

> [toc]

***单元测试应具有以下特性：***
- 自动化，执行速度快。
- 可以独立执行。不依赖其他测试结果，不需要按顺序执行。
- 不依赖数据库连接、文件访问，或其他长时间运行任务。
- 时间和空间是透明的。任何时间任何时候/环境，执行结果都一直。
- 单源测试应该有意义。
- 尽量简单，同源码一样的态度对待。
- 单元测试修改过的数据或文件，要复原。

***单元测试原则：***
- 单元测试是可靠的。
- 自动化执行。
- 可以快速执行，并迅速获得反馈。
- 无障碍安装启动和执行。

***Mockito特点：***
- 安全的自动化测试。
- 不需要 获取数据库连接，获取或修改数据。
- 不需要连接互联网下载文件。
- 不需要连接SMTP服务器发送邮件。
- 不需要查找java目录接口、调用web服务器。
- 不需要操作IO流，如打印报告。  
**即，不依赖外部资源，不改变任何数据（单元测试前后，数据或文件不会有变动）。**


```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-all</artifactId>
</dependency>
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
</dependency>
```


### 简单示例
只能测试当前方法的逻辑正确性？  
**行为模拟：** 不会真正执行方法，只是模拟该行为。
- @RunWith(MockitoJUnitRunner.class)：指定运行器是Mockito单测运行器。
- Mockito.when(request.getParameter("userName")).thenReturn("Sun"); 当执行XXX方法时，模拟返回YYY结果。
- Mockito.when(accountDao.findAccount(anyString(), anyString())).thenThrow(UnsupportedOperationException.class); 但执行XXX方法时，模拟返回YYY结果。

```java
@Test
public void login() {
    Account account = new Account();
    Mockito.when(request.getParameter("userName")).thenReturn("Sun");
    Mockito.when(request.getParameter("password")).thenReturn("123456");
    // Mockito.when(accountDao.findAccount(anyString(), anyString())).thenReturn(account);
    // assertThat(accountLoginController.login(request), equalTo("index"));

    Mockito.when(accountDao.findAccount(anyString(), anyString())).thenThrow(UnsupportedOperationException.class);
    assertThat(accountLoginController.login(request), equalTo("505"));
}
```

### 多种Mock方式及深度Mock
**Mock的三种方式：**
1. 使用Mockito启动器：@RunWith(MockitoJUnitRunner.class)。
```java
@RunWith(MockitoJUnitRunner.class)
public class MockitoByRunnerTest {
    @Test
    public void testMock() {
        // AccountDao accountDao = Mockito.mock(AccountDao.class);
        AccountDao accountDao = Mockito.mock(AccountDao.class, Mockito.RETURNS_SMART_NULLS);
        // 不会报错，但返回null。不会真正执行 accountDao.findAccount()方法，只是模拟该行为。
        Account account = accountDao.findAccount("x", "y");
        System.out.println(account);
    }
}
```
2. 单测执行前，执行 MockitoAnnotations.initMocks(this); 和并使用@Mock注解
```java
public class MockByAnnotationTest {
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private AccountDao accountDao;

    @Test
    public void testMock() {
        Account account = accountDao.findAccount("x", "y");
        System.out.println(account);
    }
}
```

3. 使用 MockitoJUnit.rule(); 和@Mock注解（也可以使用Mockito.mock()出来）。
```java
public class MockByRuleTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private AccountDao accountDao;

    @Test
    public void tetMock() {
        // AccountDao accountDao = Mockito.mock(AccountDao.class, Mockito.CALLS_REAL_METHODS);
        Account account = accountDao.findAccount("x", "y");
        System.out.println(account);
    }
}
```

**DeepMock：深层Mock**  
获取Lesson03对象的lesson03Service.get()方法会抛出异常，Mock后返回null，而null执行foo()方法抛出空指针异常。因此使用深层Mock，模拟了两种行为。
```java
public class DeepMockTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Lesson03Service lesson03Service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeepMockByAnswers() {
        Lesson03 l03 = lesson03Service.get();
        l03.foo();
    }
}
```

### Mockito Stubbing语法 — 模拟返回值
```java
@RunWith(MockitoJUnitRunner.class)
public class StubbingTest {

    private List<String> list;

    @Before
    public void init() { // Mock测试数据
        this.list = Mockito.mock(ArrayList.class);
    }

    @After
    public void destory() {
        Mockito.reset(this.list);
    }

    @Test
    public void howToUseStubbing() {
        Mockito.when(list.get(0)).thenReturn("first");
        assertThat(list.get(0), equalTo("first"));

        Mockito.when(list.get(anyInt())).thenThrow(new RuntimeException());
        try {
            list.get(0);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(RuntimeException.class));
        }
    }

    // Stubbing 测试 模拟 void 无返回值 方法
    @Test
    public void howToStubbingVoidMethod() {
        Mockito.doNothing().when(list).clear();
        list.clear();
        Mockito.verify(list, Mockito.times(1)).clear();

        Mockito.doThrow(RuntimeException.class).when(list).clear();
        try {
            list.clear();
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(RuntimeException.class));
        }
    }

    @Test
    public void stubbingDoReturn() {
        // 等价
        Mockito.when(list.get(0)).thenReturn("first");
        Mockito.doReturn("second").when(list).get(1);

        assertThat(list.get(0), equalTo("first"));
        assertThat(list.get(1), equalTo("second"));
    }

    // 迭代.调用次序不同，返回内容不同
    @Test
    public void iterateStubbing() {
        Mockito.when(list.size()).thenReturn(1, 2, 3, 4);
        // Mockito.when(list.size()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4);
        assertThat(list.size(), equalTo(1));
        assertThat(list.size(), equalTo(2));
        assertThat(list.size(), equalTo(3));
        assertThat(list.size(), equalTo(4));
    }

    @Test
    public void stubbingWithAnswer() {
        Mockito.when(list.get(anyInt())).thenAnswer(invocationOnMock -> {
            Integer index = invocationOnMock.getArgumentAt(0, Integer.class);
            return String.valueOf(index * 10);
        });
        assertThat(list.get(0), equalTo("0"));
        assertThat(list.get(99), equalTo("990"));
    }

    @Test
    public void stubbingWithRealCall() {
        StubbingService stubbingService = Mockito.mock(StubbingService.class);
        Mockito.when(stubbingService.getS()).thenReturn("String");
        Mockito.when(stubbingService.getI()).thenCallRealMethod();

        assertThat(stubbingService.getS(), equalTo("String"));
        assertThat(stubbingService.getI(), equalTo(10));
    }
}
```

### Mockito Spying
Mockito.spy 作用于真实对象，执行方法会被真实执行，但是可以对部分方法进行Mock。Stubbing所有的方法都是Mock的（虚假的）。  
```java
public class SpyingAnnotationTest {

    @Spy
    private List<String> list = new ArrayList<>();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(SpyingAnnotationTest.class);
    }

    @Test
    public void testSpy() {
        list.add("Mockito");
        list.add("PowerMock");
        assertThat(list.get(0), equalTo("Mockito"));
        assertThat(list.get(1), equalTo("PowerMock"));
        assertThat(list.isEmpty(), equalTo(false));

        // Stubbing 语法 模拟 list 的返回内容
        Mockito.when(list.isEmpty()).thenReturn(true);
        Mockito.when(list.size()).thenReturn(0);

        assertThat(list.get(0), equalTo("Mockito"));
        assertThat(list.get(1), equalTo("PowerMock"));
        assertThat(list.isEmpty(), equalTo(true));
        assertThat(list.size(), equalTo(0));
    }
}
```

### Mockito Argument Matchers
主要在Stubbing语法中使用。
```java
@RunWith(MockitoJUnitRunner.class)
public class ArgumentsMatcherTest {
    @Test
    public void basicTest() {
        List<Integer> list = Mockito.mock(ArrayList.class);
        Mockito.when(list.get(0)).thenReturn(100);
        assertThat(list.get(0), equalTo(100));
        assertThat(list.get(1), CoreMatchers.nullValue());
    }

    /* isA, any 的不同 */
    @Test
    public void testComplex() {
        Foo foo = Mockito.mock(Foo.class);
        // Mockito.when(foo.function(Mockito.isA(Parent.class))).thenReturn(100);
        Mockito.when(foo.function(Mockito.isA(Child1.class))).thenReturn(100);
        int result = foo.function(new Child1());
        assertThat(result, equalTo(100));

        result = foo.function(new Child2());
        assertThat(result, equalTo(0));

        Mockito.reset(foo);

        Mockito.when(foo.function(Mockito.any(Child1.class))).thenReturn(100);
        result = foo.function(new Child2());
        assertThat(result, equalTo(100));
    }

    static class Foo {
        int function(Parent p) {return p.work();}
    }

    interface Parent {
        int work();
    }

    class Child1 implements Parent {
        @Override
        public int work() {throw new RuntimeException();}
    }

    class Child2 implements Parent {
        @Override
        public int work() {throw new RuntimeException();}
    }
}
```

### Mockito Wildcard Arg
***协助在Stubbing语法中，方法中对不同的入参 配置不同的返回值。【顺序：统配方法最前，特殊返回值方法在后】***。就是做一些预设值之类的工作。

```java
public class SimpleService {
    public int method1(int i, String s, Collection<?> c, Serializable serializable) {
        throw new RuntimeException();
    }
    public void method2(int i, String s, Collection<?> c, Serializable serializable) {
        throw new RuntimeException();
    }
}
```
```java
@RunWith(MockitoJUnitRunner.class)
public class WildcardArgumentMatcherTest {

    @Mock
    private SimpleService simpleService;

    @After
    public void destory() {
        Mockito.reset(simpleService);
    }

    @Test
    public void wildCardMethod1() {
        Mockito.when(simpleService.method1(anyInt(), anyString(), anyCollection(), isA(Serializable.class)))
                .thenReturn(100);
        int result = simpleService.method1(1, "Sun", Collections.emptySet(), "Mockito");
        assertThat(result, equalTo(100));
    }

    // 对 Mockito.when() 参数进行特殊化 处理。入参不同，返回结果不同。
    @Test
    public void wildCardMethodWithSpecial() {
        Mockito.when(simpleService.method1(anyInt(), anyString(), anyCollection(), isA(Serializable.class))).thenReturn(-1);
        Mockito.when(simpleService.method1(anyInt(), eq("Jack"), anyCollection(), isA(Serializable.class))).thenReturn(100);
        Mockito.when(simpleService.method1(anyInt(), eq("Rose"), anyCollection(), isA(Serializable.class))).thenReturn(200);
        // 注意顺序，统配的 在最前，特殊的 在后面
        // Mockito.when(simpleService.method1(anyInt(), anyString(), anyCollection(), isA(Serializable.class))).thenReturn(-1);
        int result = simpleService.method1(1, "Jack", Collections.emptySet(), "Mockito");
        assertThat(result, equalTo(100));
        result = simpleService.method1(1, "Rose", Collections.emptySet(), "Mockito");
        assertThat(result, equalTo(200));
        result = simpleService.method1(1, "Sun", Collections.emptySet(), "Mockito");
        assertThat(result, equalTo(-1));
    }

    @Test
    public void wildCardMethod2() {
        Mockito.doNothing().when(simpleService).method2(anyInt(), anyString(), anyCollection(), isA(Serializable.class));

        simpleService.method2(2, "Sun", Collections.emptyList(), "Mockito");
        Mockito.verify(simpleService, Mockito.times(1))
                .method2(anyInt(), eq("Sun"), anyCollection(), isA(Serializable.class));
    }
}
```

### Hamcrest Matcher 断言
```java
public class AssertMatcherTest {
    @Test
    public void test() {
        int i = 10;
        assertThat(i, equalTo(10));
        assertThat(i, is(10));
        assertThat(i, not(20));
        double price = 23.45;
        assertThat(price, either(equalTo(23.45)).or(equalTo(23.54)));
    }

    @Test
    public void test2() {
        double price = 23.45;
        // assertThat(price, both(equalTo(23.45)).and(equalTo(23.54)));
        assertThat(price, both(equalTo(23.45)).and(not(equalTo(23.54))));
        assertThat(price, anyOf(is(23.45), is(15.65), not(10.01)));

        assertThat(Stream.of(1, 2, 3).anyMatch(i -> i > 2), equalTo(true));
        assertThat(Stream.of(1, 2, 3).allMatch(i -> i > 0), equalTo(true));
    }

    // java.lang.NoSuchMethodError: org.hamcrest.Matcher.describeMismatch
    @Test
    public void test3() {
        double price = 23.45;
        assertThat("NotMatch", price, equalTo(23.4));
    }
}
```

### 自定义Matcher实现gt、lt函数
