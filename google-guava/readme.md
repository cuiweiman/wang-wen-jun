[汪文君Guava视频教程](https://www.bilibili.com/video/BV1kE411b72b)  
[练习源码 google-guava](https://github.com/cuiweiman/wang-wen-jun)

> [TOC]

pom.xml
```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>29.0-jre</version>
</dependency>
```
# Guava Utilities
## Joiner
Concatenate strings together with a specified delimiter：使用指定的分隔器，将字符串连接起来。  
```java
public class JoinerTest {
    private final List<String> stringList = Arrays.asList(
            "Google", "Guava", "Java", "Scala", "Kafka"
    );
    private final List<String> stringListWithNullValue = Arrays.asList(
            "Google", "Guava", "Java", "Scala", null
    );

    private final Map<String, String> stringMap = ImmutableMap.of("Hello", "Guava", "Java", "Scala");

    private final String targetFileName = "E:\\guava-joiner.txt";

    /**
     * Joiner.on("delimiter").join()：使用特定的连接器，将集合中的元素连接成字符串
     */
    @Test
    public void testJoinerOnJoin() {
        String result = Joiner.on("#").join(stringList);
        assertThat(result, equalTo("Google#Guava#Java#Scala#Kafka"));
    }

    /**
     * Joiner.on("delimiter").join()情景：集合中存在null值，空指针异常
     */
    @Test(expected = NullPointerException.class)
    public void testJoinerOnJoinWithNullValue() {
        String result = Joiner.on("#").join(stringListWithNullValue);
        System.out.println(result);
        assertThat(result, equalTo("Google#Guava#Java#Scala#Kafka"));
    }

    /**
     * Joiner.on("delimiter").skipNulls().join： 跳过null值
     */
    @Test
    public void testJoinerOnJoinWithNullValueButSkip() {
        String result = Joiner.on("#").skipNulls().join(stringListWithNullValue);
        System.out.println(result);
        assertThat(result, equalTo("Google#Guava#Java#Scala"));
    }

    /**
     * Joiner.on("delimiter").useForNull("Default").join：
     * 使用自定义字符串，代替 集合中的null元素
     */
    @Test
    public void testJoinerOnJoinWithNullValueButUseDefaultValue() {
        String result = Joiner.on("#").useForNull("Default").join(stringListWithNullValue);
        System.out.println(result);
        assertThat(result, equalTo("Google#Guava#Java#Scala#Default"));
    }

    /**
     * Joiner.on("delimiter").useForNull("Default").appendTo(sb, stringListWithNullValue)
     * 将拼接好的字符串，添加到StringBuilder中去
     */
    @Test
    public void testJoinOnAppendToStringBuilder() {
        final StringBuilder sb = new StringBuilder();
        StringBuilder resultBuilder = Joiner.on("#").useForNull("Default")
                .appendTo(sb, stringListWithNullValue);
        System.out.println(resultBuilder.toString());
        assertThat(resultBuilder, sameInstance(sb));
    }


    /**
     * appendTo 将拼接好的字符串 写入到文件中去
     */
    @Test
    public void testJoinOnAppendToWriteFile() {
        try (FileWriter writer = new FileWriter(new File(targetFileName))) {
            Joiner.on("#").useForNull("Default").appendTo(writer, stringListWithNullValue);
            assertThat(Files.isFile().test(new File(targetFileName)), equalTo(true));
        } catch (IOException e) {
            fail("append to the writer occur fetal error.");
        }
    }

    /**
     * Java8 实现 使用指定的连接器，将集合拼接成一个字符串,跳过null值
     */
    @Test
    public void testJoiningByStreamSkipNullValues() {
        Optional.ofNullable(stringListWithNullValue.stream()
                .filter(item -> item != null && !item.isEmpty())
                .collect(Collectors.joining("#")))
                .ifPresent(System.out::println);
    }

    /**
     * Java8 实现 使用指定的连接器，将集合拼接成一个字符串
     */
    @Test
    public void testJoiningByStreamUseDefaultValue() {
        Optional.ofNullable(stringListWithNullValue.stream()
                .map(item -> {
                    if (item != null && !item.isEmpty()) {
                        return "DEFAULT";
                    }
                    return item;
                }).collect(Collectors.joining("#")))
                .ifPresent(System.out::println);
    }

    /**
     * Joiner.on('delimiter').withKeyValueSeparator("=").join(map)：
     * 将 Map 的 元素，以 Key=value的形式结合成字符串，
     * 并使用 delimiter 分隔符划分开来
     */
    @Test
    public void testJoinOnWithMap() {
        Optional.ofNullable(Joiner.on('#').withKeyValueSeparator("=").join(stringMap))
                .ifPresent(System.out::println);
    }

    /**
     * Joiner.on('delimiter').withKeyValueSeparator("=").join(map)：
     * 将 Map 的 元素，以 Key=value的形式结合成字符串，
     * 并使用 delimiter 分隔符划分开来；
     * 将字符串输出到 目标文件中
     */
    @Test
    public void testJoinOnWithMapToAppendable() {
        try (FileWriter writer = new FileWriter(new File(targetFileName))) {
            Joiner.on('#').withKeyValueSeparator("=").appendTo(writer, stringMap);
            System.out.println(Files.isFile().test(new File(targetFileName)));
        } catch (IOException e) {
            fail("append to the writer occur fetal error.");
        }
    }
}

```

## Splitter
Produce substrings borken out by the provided delimiter：使用指定的分割符，分隔字符串成子串。 
```java
public class SplitterTest {

    /**
     * 根据 分隔符 分隔字符串，返回 List
     */
    @Test
    public void testSplitOnSplit() {
        List<String> result = Splitter.on("|").splitToList("hello|world");
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0), equalTo("hello"));
        assertThat(result.get(1), equalTo("world"));
        result.forEach(System.out::println);
    }

    /**
     * 根据 分隔符 分隔字符串，忽略空值，返回 List
     */
    @Test
    public void testSplitOnSplitOmitEmpty() {
        List<String> result = Splitter.on("|").splitToList("hello|world|||");
        assertThat(result.size(), equalTo(5));
        result = Splitter.on("|").omitEmptyStrings().splitToList("hello|world|||");
        assertThat(result.size(), equalTo(2));
    }

    /**
     * 分隔结果 去除空格
     */
    @Test
    public void testSplitOnSplitOmitEmptyTrimResult() {
        List<String> result = Splitter.on("|").omitEmptyStrings()
                .splitToList(" hello|world |||");
        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0), equalTo(" hello"));
        assertThat(result.get(1), equalTo("world "));
        result = Splitter.on("|").omitEmptyStrings().trimResults().splitToList(" hello|world |||");
        assertThat(result.get(0), equalTo("hello"));
        assertThat(result.get(1), equalTo("world"));
    }

    /**
     * 固定长度 字符串 分隔
     */
    @Test
    public void testSplitFixLength() {
        List<String> result = Splitter.fixedLength(4)
                .splitToList("aaaabbbbccccdddd");
        assertThat(result.size(), equalTo(4));
        assertThat(result.get(0), equalTo("aaaa"));
        assertThat(result.get(3), equalTo("dddd"));
    }

    /**
     * 根据分隔符，分割成 n 个字符串
     */
    @Test
    public void testSplitOnSplitLimit() {
        List<String> result = Splitter.on("#").limit(3)
                .splitToList("hello#world#java#google#scala");
        System.out.println(result);
        assertThat(result.size(), equalTo(3));
        assertThat(result.get(0), equalTo("hello"));
        assertThat(result.get(1), equalTo("world"));
        assertThat(result.get(2), equalTo("java#google#scala"));
    }

    /**
     * 正则表达式 分隔 字符串
     */
    @Test
    public void tetSplitOnPatternString() {
        List<String> result = Splitter.onPattern("\\|")
                .trimResults().omitEmptyStrings()
                .splitToList("Hello | World||||");
        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0), equalTo("Hello"));
        assertThat(result.get(1), equalTo("World"));
    }

    @Test
    public void tetSplitOnPattern() {
        List<String> result = Splitter.on(Pattern.compile("\\|"))
                .trimResults().omitEmptyStrings()
                .splitToList("Hello | World||||");
        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0), equalTo("Hello"));
        assertThat(result.get(1), equalTo("World"));
    }

    /**
     * 字符串  根据指定分隔符 分隔成 Map
     */
    @Test
    public void tetSplitOnSplitToMap() {
        Map<String, String> result = Splitter.on(Pattern.compile("\\|"))
                .trimResults().omitEmptyStrings()
                .withKeyValueSeparator("=")
                .split("hello=HELLO | world=WORLD|||");
        assertThat(result.size(), equalTo(2));
        assertThat(result.get("hello"), equalTo("HELLO"));
        assertThat(result.get("world"), equalTo("WORLD"));
    }
}
```

## Preconditions
Methods for asserting certain conditions you expect variables：断言。断言某些条件是否符合你期盼的方法。
```java
public class PreconditionsTest {
    List<String> list = null;
    List<String> list2 = ImmutableList.of();

    /**
     * 校验 空指针异常
     */
    @Test
    public void checkNotNull() {
        Preconditions.checkNotNull(list);
    }

    /**
     * 校验 空指针异常，并返回指定 信息
     */
    @Test
    public void checkNotNullWithMsg() {
        try {
            Preconditions.checkNotNull(list, "The list should not be null");
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 校验 空指针异常，并返回 可以格式化的 指定信息
     */
    @Test
    public void checkNotNullWithFormatMsg() {
        try {
            Preconditions.checkNotNull(list,
                    "The list should not be null and the size must be %s", 2);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testCheckArguments() {
        try {
            Preconditions.checkArgument(false, "错误");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(e.getClass(), is(IllegalArgumentException.class));
        }
    }

    @Test
    public void testCheckState() {
        try {
            Preconditions.checkState(false, "错误");
            fail("should not process to here");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(e.getClass(), is(IllegalStateException.class));
        }
    }

    @Test
    public void testCheckIndex() {
        try {
            Preconditions.checkElementIndex(5, list2.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(e.getClass(), is(IndexOutOfBoundsException.class));
        }
    }

    /**
     * JDK1.8 中的判断工具类
     */
    @Test(expected = NullPointerException.class)
    public void testByObjects() {
        Objects.requireNonNull(list);
    }

    /**
     * 语法糖 判空
     */
    @Test
    public void testAssertWithMessage() {
        try {
            assert list != null : "The list should not be null.";
        } catch (Error e) {
            System.out.println(e.getMessage());
            assertThat(e.getClass(), is(AssertionError.class));
        }
    }
}

```

## Others
- Objects、MoreObjects、ComparisonChain可以构造类中的toString()、hashCode()、equals()、compareTo()方法。***现在都不怎么好用了。***
    1. MoreObjects构造toString()方法：
    ```java
    @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues().add("menuFactory", this.menuFactory).add("version", this.version).add("releaseDate", this.releaseDate).toString();
        }
    ```
    2. Objects 构造equals()方法
    ```java
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guava guava = (Guava) o;
        return Objects.equal(this.menuFactory, guava.menuFactory) && Objects.equal(this.version, guava.version) && Objects.equal(this.releaseDate, guava.releaseDate);
    }
    ```
    3. ComparisonChain构造compareTo()方法
    ```java
    @Override
    public int compareTo(Guava o) {
        return ComparisonChain.start().compare(this.menuFactory, o.menuFactory).compare(this.version, o.version).compare(this.releaseDate, o.releaseDate).result();
    }
    ```

- Strings、Charset、CharMatcher工具类的使用
	1. Strings工具类：
	```java
	/**
     * Strings.emptyToNull()：将空字符串转换成 null。
     * Strings.nullToEmpty()：将 null转换成 空字符串；若不为null，则原值返回。
     * Strings.commonPrefix()/commonSuffix()：返回多个字符串 的公共前缀 / 公共后缀
     * Strings.repeat(string, n)：将字符串 string 重复3次，拼接后返回
     * Strings.isNullOrEmpty(string)：字符串是否为空字符串或者null
     * Strings.padStart(string,length,fixChar)：字符串长度达不到length时，前面使用Char补充
     * Strings.padEnd(string,length,fixChar)：字符串长度达不到length时，后面使用Char补充
     */
    @Test
    public void testStringsMethod() {
        assertThat(Strings.emptyToNull(""), nullValue());
        assertThat(Strings.nullToEmpty(null), equalTo(""));
        assertThat(Strings.nullToEmpty("hello"), equalTo("hello"));
        assertThat(Strings.commonPrefix("Hello", "Heat"), equalTo("He"));
        assertThat(Strings.commonSuffix("Hello", "Echo"), equalTo("o"));
        assertThat(Strings.repeat("Alex", 3), equalTo("AlexAlexAlex"));
        assertThat(Strings.isNullOrEmpty(""), equalTo(true));
        assertThat(Strings.padStart("Alex", 3, 'H'), equalTo("Alex"));
        assertThat(Strings.padStart("Alex", 7, 'H'), equalTo("HHHAlex"));
        assertThat(Strings.padEnd("Alex", 5, 'H'), equalTo("AlexH"));
    }
	```
	2. CharSets工具类
	```java
	@Test
    public void testCharsets() {
        Charset charset = StandardCharsets.UTF_8;
        assertThat(Charsets.UTF_8, equalTo(charset));
    }
	```
	3. CharMatcher工具类
	```java
	@Test
    public void testCharMatcher() {
        assertThat(CharMatcher.javaDigit().matches('5'), equalTo(true));
        assertThat(CharMatcher.javaDigit().matches('x'), equalTo(false));
        // 字符串中 有多少个 大写的 A
        assertThat(CharMatcher.is('A').countIn("Alex Sharing the Google Guava to Us"), equalTo(1));
        // 将字符串中的 空格 替换成 指定 符号
        assertThat(CharMatcher.breakingWhitespace().collapseFrom("     hello guava         ", '*'), equalTo("*hello*guava*"));
        assertThat(CharMatcher.javaDigit().or(CharMatcher.whitespace()).removeFrom("Hello 234 World JustTest 56"),equalTo("HelloWorldJustTest"));
    }
	```

# Guava Functional
## Function/Functions
```java
public class FunctionExample {

    public static void main(String[] args) throws IOException {
        // 函数式 逻辑处理
        Function<String, Integer> function = new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable String s) {
                Preconditions.checkNotNull(s, "The input String should not be null.");
                return s.length();
            }
        };
        System.out.println(function.apply("Hello"));

        // 面向接口编程，函数式处理
        process("Hello", new Handler.LengthDoubleHandler());

        // Functions.toStringFunction()
        System.out.println(Functions.toStringFunction().apply(new ServerSocket(8888)));

        // String 转 Integer，Integer再转Long
        Function<String, Double> compose = Functions.compose(new Function<Integer, Double>() {
            @Nullable
            @Override
            public Double apply(@Nullable Integer integer) {
                return Double.valueOf(integer);
            }
        }, new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable String string) {
                return string.length();
            }
        });
        System.out.println(compose.apply("HelloWorld"));
    }

    interface Handler<IN, OUT> {
        /**
         * 处理输入，并返回类型
         *
         * @param input 输入
         * @return 返回
         */
        OUT handle(IN input);

        class LengthDoubleHandler implements Handler<String, Integer> {
            @Override
            public Integer handle(String input) {
                return input.length() * 2;
            }
        }
    }


    private static void process(String str, Handler<String, Integer> handler) {
        System.out.println(handler.handle(str));
    }
}
```

## Predicate / Predicates
1. Predicate接口 继承了Java8的 Predicate；代表一个“断定式子”。常用于条件判断过滤等。
```java
@Data
static class Girl {
    int age;
    String face;
}
 
@Test
public void testPredicate() {
    Predicate predicate = new Predicate() {
        @Override
        public boolean apply(Girl input) {
            return input.getAge() >= 18;
        }
    };
    //有个18岁的漂亮姑娘
    Girl girl = new Girl(18, "nice");
    if (predicate.apply(girl)) {
        System.out.println("be my girl friend");
    } else {
        System.out.println("too young to love");
    }
    //输出结果：be my girl friend
}
```
```java
 @Test
    public void testPredicate2() {
        Predicate<Girl> agePredicate = input -> input.getAge() >= 18;
        Predicate<Girl> facePredicate = input -> input.getFace().equals("nice");
        Girl girl = new Girl(18, "ugly");

        //and：用于过滤两个Predicate都为true
        Predicate<Girl> predicate = Predicates.and(agePredicate, facePredicate);
        checkOut(predicate.apply(girl));

        //or：用于过滤其中一个Predicate为true
        predicate = Predicates.or(agePredicate, facePredicate);
        checkOut(predicate.apply(girl));

        //not：用于将指定Predicate取反
        Predicate<Girl> noneAgePredicate = Predicates.not(agePredicate);
        predicate = Predicates.and(noneAgePredicate, facePredicate);
        checkOut(predicate.test(girl));

        //compose: Function与Predicate的组合：构造一个测试用Map集合
        Map<String, Girl> map = new HashMap<>();
        map.put("love", new Girl(18, "nice"));
        map.put("miss", new Girl(16, "ugly"));
        Predicate<Girl> predicate1 = Predicates.and(agePredicate, facePredicate);
        Function<String, Girl> function1 = Functions.forMap(map);
        Predicate<String> stringPredicate = Predicates.compose(predicate1, function1);
        System.out.println(stringPredicate.apply("love"));
        System.out.println(stringPredicate.apply("miss"));
    }

    // 判断输出
    private void checkOut(boolean flag) {
        if (flag) {
            System.out.println("She fits me");
        } else {
            System.out.println("She doesn't fit me");
        }
    }
```

## Supplier / Suppliers
1. Supplier 继承了Java8的 Supplier；
2. Supplier是一个接口，有一个get()方法，常用语创建新对象。
3. 创建Supplier容器，创建对象时，不会立刻调用对象的构造方法，但是在调用get()方法时，会调用构造方法创建对象：
	```java
	public static void main(String[] args){
        Supplier<String> supplier = String::new;
        Class<? extends String> aClass = supplier.get().getClass();
        Class<? extends String> aClass1 = supplier.get().getClass();
        Assert.assertThat(aClass==aClass1, Matchers.equalTo(true));
	}
	```

4. Function/Functions/Precidate/Precidates/Supplier/Suppliers
    ```java
	public class SupplierExample {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Girl {
        int age;
        String face;
    }
    @Test
    public void testSupplier() {
        Supplier<Predicate<String>> supplier = new Supplier<Predicate<String>>() {
                @Override
                public Predicate<String> get() {
                    Map<String, Girl> map = new HashMap<>();
                    map.put("love the face", new Girl(18, "nice"));
                    map.put("love the age", new Girl(16, "ugly"));
                    Function<String, Girl> function = Functions.forMap(map);
                    Predicate<Girl> predicate = girl -> girl.getAge() >= 18;
                    Predicate<String> result = Predicates.compose(predicate, function);
                    return result;
                }
            };
            System.out.println(supplier.get().apply("love the age"));
            System.out.println(supplier.get().apply("love the face"));
        }
    }
    ```

## Stopwatch计时器和JDK的ServiceLoader
- Stopwatch计时器
```java
public void process(String orderNo) throws InterruptedException {
        log.info("start process the order {}", orderNo);
        Stopwatch stopwatch = Stopwatch.createStarted();
        TimeUnit.SECONDS.sleep(1);
        // log.info("The orderNo {} process successful and elapsed {}.", orderNo, stopwatch.stop());
        log.info("The orderNo {} process successful and elapsed {}.", orderNo, stopwatch.stop().elapsed(TimeUnit.MINUTES));
    }
```
# Guava IO
## Files工具类
```java
public class FilesTest {
    private final String SOURCE_FILE = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\source.txt";
    private final String target_file = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\target.txt";

    /**
     * 文件拷贝（推荐字节流，字符流会涉及编码问题）
     * 验证拷贝到是该文件，可以进行 md5签名验证
     */
    @Test
    public void copyFileWithGuava() throws IOException {
        File target = new File(target_file);
        File source = new File(SOURCE_FILE);
        Files.copy(source, target);
        assertThat(target.exists(), equalTo(true));
        HashCode sourceHashCode = Files.asByteSource(source).hash(Hashing.sha256());
        HashCode targetHashCode = Files.asByteSource(target).hash(Hashing.sha256());
        assertThat(sourceHashCode.equals(targetHashCode), equalTo(true));
    }

    /**
     * 文件移动（推荐字节流，字符流会涉及编码问题）
     * 验证拷贝到是该文件，可以进行 md5签名验证
     */
    @Test
    public void moveFileWithGuava() throws IOException {
        try {
            File source = new File(SOURCE_FILE);
            File target = new File(target_file);
            Files.move(source, target);
            assertThat(source.exists(), equalTo(false));
            assertThat(target.exists(), equalTo(true));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Files.move(new File(target_file), new File(SOURCE_FILE));
        }
    }

    /**
     * 按行读取 File中的内容
     */
    @Test
    public void testToString() throws IOException {
        final String expectedString = "hello Sun YY , today we will share the guava io knowledge.\n" +
                "but only for the basic usage.\n" +
                "if you wanted to get the more details information,\n" +
                "please read the guava document or source code.";
        List<String> strings = Files.readLines(new File(SOURCE_FILE), Charsets.UTF_8);
        String result = Joiner.on("\n").join(strings);
        assertThat(result, equalTo(expectedString));
    }

    /**
     * Java NIO 文件拷贝
     */
    @Test
    public void copyFileWithJdkNio2() throws IOException {
        java.nio.file.Files.copy(
                Paths.get("E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources", "io", "source.txt"),
                Paths.get("E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources", "io", "target.txt"),
                StandardCopyOption.REPLACE_EXISTING);
        assertThat(new File(target_file).exists(), equalTo(true));
    }

    /**
     * LineProcessor 读取文件内容，并按行处理
     */
    @Test
    public void testToProcessString() throws IOException {
        LineProcessor<List<Integer>> lineProcessor = new LineProcessor<List<Integer>>() {
            private final List<Integer> list = Lists.newArrayList();

            // 58, 29, 50, 46
            @Override
            public boolean processLine(String line) throws IOException {
                if (line.length() == 50) {
                    // 读到 长度等于50，就不会在往下读了
                    return false;
                }
                list.add(line.length());
                return true;
            }

            @Override
            public List<Integer> getResult() {
                return list;
            }
        };
        List<Integer> result = Files.asCharSource(new File(SOURCE_FILE), Charsets.UTF_8)
                .readLines(lineProcessor);
        System.out.println(result);
    }

    /**
     * File Hash Sha算法
     */
    @Test
    public void testFileSha() throws IOException {
        File file = new File(SOURCE_FILE);
        HashCode hash = Files.asByteSource(file).hash(Hashing.sha256());
        System.out.println(hash);
    }

    /**
     * 文件写入内容
     */
    @Test
    public void testFileWrite() throws IOException {
        final String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\testWriteFile.txt";
        File file = new File(path);
        // file.deleteOnExit();
        String content = "content test file write.";
        Files.asCharSink(file, Charsets.UTF_8).write(content);
        String read = Files.asCharSource(file, Charsets.UTF_8).read();
        assertThat(read, equalTo(content));
    }
    
    /**
     * 文件内容后面 增加 内容
     */
    @Test
    public void testFileAppend() throws IOException {
        final String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\testWriteFile.txt";
        File file = new File(path);
        // JVM运行结束时，删除文件
        file.deleteOnExit();
        String content = "content test file write.";
        CharSink charSink = Files.asCharSink(file, Charsets.UTF_8, FileWriteMode.APPEND);
        //先写入 content
        charSink.write(content);

        String actually = Files.asCharSource(file, Charsets.UTF_8).read();
        assertThat(actually, equalTo(content));

        // 再写入
        charSink.write("content2");
        actually = Files.asCharSource(file, Charsets.UTF_8).read();
        assertThat(actually, equalTo(content + "content2"));
    }
    
    /**
     * 创建一个 空文件，类似于linux中的touch命令
     */
    @Test
    public void testTouchFile() throws IOException {
        File touchFile = new File(SOURCE_FILE);
        Files.touch(touchFile);
        assertThat(touchFile.exists(), equalTo(true));
    }

	/**
     * 递归遍历 文件夹
     */
    @Test
    public void testRecursive() {
        String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main";
        File file = new File(path);
        List<File> list = Lists.newArrayList();
        this.recursiveList(file, list);
        list.forEach(System.out::println);
    }

    // 文件 递归方法
    private void recursiveList(File root, List<File> fileList) {
        if (root.isHidden()) {
            return;
        }
        /* // 获取到了文件夹、以及文件
        fileList.add(root);
        if (!root.isFile()) {
            File[] files = root.listFiles();
            for (File f : files) {
                recursiveList(f, fileList);
            }
        }*/
        // 只获取 文件
        if (root.isFile()) {
            fileList.add(root);
        } else {
            File[] files = root.listFiles();
            for (File f : files) {
                recursiveList(f, fileList);
            }
        }
    }

	/**
     * 遍历 文件夹与文件：广度优先遍历 与 深度优先遍历
     */
    @Test
    public void testFileTraverser() {
        String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main";
        File file = new File(path);
        System.out.println("广度优先遍历");
        Iterable<File> files = Files.fileTraverser().breadthFirst(file);
        // 过滤 掉文件夹，打印文件路径
        files.forEach(f -> {
            if (f.isFile()) {
                System.out.println(f);
            }
        });
        System.out.println("深度优先遍历");
        files = Files.fileTraverser().depthFirstPreOrder(file);
        files.forEach(System.out::println);
    }

    @After
    public void tearDown() {
        File file = new File(target_file);
        if (file.exists()) {
            file.delete();
        }
    }
}
```
## CharSource/CharSink 字符流
**CharSource 相当于 Reader； CharSink 相当于 Writer**  
```java
/**
     * CharSource 基本方法
     */
    @Test
    public void testCharSourceWrap() throws IOException {
        CharSource charSource = CharSource.wrap("i me the CharSource");
        String read = charSource.read();
        assertThat(read, equalTo("i me the CharSource"));
        ImmutableList<String> lines = charSource.readLines();
        assertThat(lines.size(), equalTo(1));
        assertThat(charSource.length(), equalTo(19L));
        assertThat(charSource.lengthIfKnown().get(), equalTo(19L));
    }

    @Test
    public void testConcat() throws IOException {
        CharSource charSource = CharSource.concat(
                CharSource.wrap("i me the CharSource 1"),
                CharSource.wrap("i me the CharSource 2"),
                CharSource.wrap("i me the CharSource 3")
        );
        charSource.lines().forEach(System.out::println);
    }
```
```java
	可以进入CharSink观看源码API方法;
```

## ByteSource/ByteSink 字节流
**ByteSource 对应 InputStream；ByteSink 对应 OutputStream **  
```java
public class ByteSourceTest {

    private final String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image.png";

    // 基本方法，详细方法参见源码
    @Test
    public void testAsByteSource() throws IOException {
        File file = new File(path);
        ByteSource byteSource = Files.asByteSource(file);
        byte[] readBytes = byteSource.read();
        assertThat(readBytes, is(Files.toByteArray(file)));
    }

    @Test
    public void testSliceByteSource() throws IOException {
        ByteSource wrap = ByteSource.wrap(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        ByteSource slice = wrap.slice(5, 5);
        byte[] bytes = slice.read();
        for (byte aByte : bytes) {
            System.out.println(aByte);
        }
    }
}
```
```java
public class ByteSinkTest {
    private final String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\ByteSinkTest.txt";

    @Test
    public void testByteSink() throws IOException {
        File file = new File(path);
        file.deleteOnExit();
        ByteSink byteSink = Files.asByteSink(file);
        byte[] bytes = new byte[]{0x01, 0x02};
        byteSink.write(bytes);

        byte[] expected = Files.toByteArray(file);
        assertThat(expected, is(bytes));
    }

    @Test
    public void tstFromSourceToSink() throws IOException {
        String source = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image.png";
        String target = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image_copy.png";
        File sourceFile = new File(source);
        ByteSource byteSource = Files.asByteSource(sourceFile);

        File targetFile = new File(target);
        targetFile.deleteOnExit();
        byteSource.copyTo(Files.asByteSink(targetFile));
        assertThat(targetFile.exists(), equalTo(true));

        assertThat(
                Files.asByteSource(sourceFile).hash(Hashing.sha256()).toString(),
                equalTo(Files.asByteSource(targetFile).hash(Hashing.sha256()).toString())
        );
    }
}
```

## CharStreams/ByteStreams 

## Closer 批量关闭IO流
```java
public class CloserTest {
    ByteSource byteSource = Files.asByteSource(new File("E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image.png"));

    @Test
    public void testCloser() throws IOException {
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(byteSource.openStream());
        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

    @Test
    public void testBeforeCloseIo() {
        // 关闭IO流 方式一：
        try (InputStream inputStream = byteSource.openStream()) {
            inputStream.read("InputStream自动关闭".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭IO流 方式二：
        InputStream inputStream = null;
        try {
            byteSource.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```
## BaseEncoding——Base64
```java
	/**
     * Base64 编解码
     */
    @Test
    public void testBase64EncodingAndDecode() {
        BaseEncoding baseEncoding = BaseEncoding.base64();
        String encode64 = baseEncoding.encode("hello".getBytes());
        System.out.println(encode64);
        String decode = new String(baseEncoding.decode(encode64));
        System.out.println(decode);
    }
```
**Base64编码原理：**（例）
1. 字符a，ASCII码为97，对应的二进制数为【0110 0001】；
2. Base64规则是 将ASCII的二进制从左向右，每6位分一组，最后不足六位补0，即：【011000 010000】。补了4个0.
3. 将按照Base64规则划分组的二进制数转成10进制，即 【24 16】；
4. 查看Base64编码表，发现 24——>Y，16——>Q，即字符“a”的Base64编码为 【QY==】。第2步中，补了4个0，就添加两个=，补了2个0，就末尾添加一个=。
5. 按照以上规则，可以自定义实现Base64编解码：
```java
public final class Base64 {

    // Base64 字典
    private final static String CODE_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^!";
    private final static char[] CODE_DICT = CODE_STRING.toCharArray();

    private Base64() { }

    public static String encode(String plain) {
        Preconditions.checkNotNull(plain);
        StringBuilder result = new StringBuilder();
        String binaryString = toBinary(plain);

        // Base64规则是将8位二进制数，从左到右6位一组划分，最右边不足6位补0；
        int delta = 6 - binaryString.length() % 6;
        for (int i = 0; i < delta && delta != 6; i++) {
            binaryString += "0";
        }

        // 6位一组划分,在转10进制
        for (int i = 0; i < binaryString.length() / 6; i++) {
            int begin = i * 6;
            String encodeString = binaryString.substring(begin, begin + 6);
            char encodeChar = CODE_DICT[Integer.parseInt(encodeString, 2)];
            result.append(encodeChar);
        }
        // 补了n个0，末尾添加 n/2个=
        if (delta != 6) {
            for (int i = 0; i < delta / 2; i++) {
                result.append("=");
            }
        }
        return result.toString();
    }

	/**
     * 解码
     *
     * @param encode
     * @return
     */
    public static String decode(final String encrypt) {
        StringBuilder result = new StringBuilder();
        String temp = encrypt;
        int equalIndex = temp.indexOf("=");
        if (equalIndex > 0) {
            temp = temp.substring(0, equalIndex);
        }
        String base64Binary = toBase64Binary(temp);
        // 每8位一组
        for (int i = 0; i < base64Binary.length() / 8; i++) {
            int begin = i * 8;
            String binary = base64Binary.substring(begin, begin + 8);
            char c = Character.toChars(Integer.parseInt(binary, 2))[0];
            result.append(c);
        }
        return result.toString();
    }

    /**
     * 按照Base64编码表，找到字母对应的10进制数，转换成二进制，再补齐6位；
     * Base64二进制是6位一组，正常二进制是8位一组。
     *
     * @param source 入参
     * @return 结果
     */
    private static String toBase64Binary(final String source) {
        final StringBuilder binary = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            int index = CODE_STRING.indexOf(source.charAt(i));
            String charBin = Integer.toBinaryString(index);
            int delta = 6 - charBin.length();
            for (int j = 0; j < delta; j++) {
                charBin = "0" + charBin;
            }
            binary.append(charBin);
        }
        return binary.toString();
    }


    /**
     * 获取 字符对应的ASCII 数字的二进制数，并处理成 8位。
     *
     * @param source 入参
     * @return 结果
     */
    public static String toBinary(final String source) {
        final StringBuilder binary = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            String charBin = Integer.toBinaryString(source.charAt(i));
            int delta = 8 - charBin.length();
            for (int d = 0; d < delta; d++) {
                charBin = "0" + charBin;
            }
            binary.append(charBin);
        }
        return binary.toString();
    }
}
```

# Guava EventBus
	EventBus是Guava的事件处理机制，是设计模式中的观察者模式（生产/消费者编程模型）的优雅实现。

## EventBus/AsyncEventBus 同步与异步事件总线
### 同步事件总线--简单的demo
**EventBus注册消费者，并发布事件**
```java
public class MultipleEventBus {
    public static void main(String[] args) {
        final EventBus bus = new EventBus();
        bus.register(new MultipleEventListener());
        bus.post("STRING EVENT");
        bus.post(123456);
    }
}
```
**Listener事件消费者/处理者**
```java
@Slf4j
public class MultipleEventListener {

    @Subscribe
    private void task1(String event) {
        if (log.isInfoEnabled()) {
            log.info("【task1】Received event [{}] and will take a action.", event);
        }
    }

    @Subscribe
    private void task2(String event) {
        if (log.isInfoEnabled()) {
            log.info("【task2】Received event [{}] and will take a action.", event);
        }
    }

    @Subscribe
    private void integerTask(Integer event) {
        if (log.isInfoEnabled()) {
            log.info("【intTask】Received event [{}] and will take a action.", event);
        }
    }
}
```


# Guava Concurrency
Guava Monitor, ListenableFuture, FutureCallback, SetableFuture, AsyncFunction, FutureFallback, Futures, 
RateLimiter

## Monitor 控制并发
**引入：** 多线程环境下，有时需要限制单线程访问资源，Java提供了Synchronized关键字 通过wait()方法和notifyAll()来进行线程同步，或者ReentrantLock的await()和signal()来实现。而**Guava的Monitor类可以认为是ReentrantLock的替代，操作更简单、易读** 。

```java
public class MonitorExample {

    public static void main(String[] args) {
        // Synchronized 实现生产者与消费者
        // final Synchroinzed service = new Synchroinzed();

        // Lock 实现生产者与消费者
        // final LockCondition service = new LockCondition();

        // Monitor 实现生产者与消费者
        final MonitorGuard service = new MonitorGuard();

        final AtomicInteger COUNTER = new AtomicInteger(0);

        /**
         * 生产
         */
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (; ; ) {
                    try {
                        int data = COUNTER.getAndIncrement();
                        System.out.println(currentThread() + " offer " + data);
                        service.offer(data);
                        TimeUnit.MILLISECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        /**
         * 消费
         */
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (; ; ) {
                    try {
                        int data = service.take();
                        System.out.println(currentThread() + " take " + data);
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * Monitor 实现生产者与消费者
     */
    static class MonitorGuard {

        private final LinkedList<Integer> queue = new LinkedList<>();

        private final int MAX = 10;

        private final Monitor monitor = new Monitor();

        private final Monitor.Guard CAN_OFFER = monitor.newGuard(() -> queue.size() < MAX);

        private final Monitor.Guard CAN_TAKE = monitor.newGuard(() -> !queue.isEmpty());

        public void offer(int value) {
            try {
                monitor.enterWhen(CAN_OFFER);
                queue.addLast(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                monitor.leave();
            }
        }

        public int take() {
            try {
                monitor.enterWhen(CAN_TAKE);
                return queue.removeLast();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            } finally {
                monitor.leave();
            }
        }

    }

    /**
     * Lock 实现生产者与消费者
     */
    static class LockCondition {
        private final ReentrantLock lock = new ReentrantLock();

        private final Condition FULL_CONDITION = lock.newCondition();

        private final Condition EMPTY_CONDITION = lock.newCondition();

        private final LinkedList<Integer> queue = new LinkedList<>();

        private final int MAX = 10;

        public void offer(int value) {
            try {
                lock.lock();
                while (queue.size() >= MAX) {
                    FULL_CONDITION.await();
                }
                queue.addLast(value);
                // 放入队列后，通知 消费者
                EMPTY_CONDITION.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public int take() {
            Integer value = null;
            try {
                lock.lock();
                while (queue.isEmpty()) {
                    EMPTY_CONDITION.await();
                }
                value = queue.removeFirst();
                // 取出值后，通知 生产者
                FULL_CONDITION.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return value;
        }
    }

    /**
     * Synchronized 实现生产者与消费者
     */
    static class Synchroinzed {
        private final LinkedList<Integer> queue = new LinkedList<>();

        private final int MAX = 10;

        // 放入 容器
        public void offer(int value) {
            synchronized (queue) {
                while (queue.size() >= MAX) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.addLast(value);
                queue.notifyAll();
            }
        }

        // 容器中 取出
        public int take() {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer val = queue.removeFirst();
                queue.notifyAll();
                return val;
            }
        }
    }
}
```

## RateLimiter与高并发系统限流
可以控制方法执行的频率、高并发系统限流。
常见有：漏桶算法、令牌桶算法等。  
***漏桶算法：***
> 控制数据注入到网络的速率，平滑网络上的突发流量。 把请求比作是水，水来了都先放进桶里，并以限定的速度出水（处理请求），当水来得过猛而出水不够快时就会导致水直接溢出，即拒绝服务。  

***令牌桶算法：***
>  以固定的频率向容器桶中放入令牌，例如一秒钟10枚令牌，实际业务在每次响应请求之前都从桶中获取令牌，只有取到令牌的请求才会被成功响应，获取的方式有两种：阻塞等待令牌 或 者取不到立即返回失败。令牌桶中可以保存的最大令牌数永远不会超过桶的容量。

## ListenableFuture 与 Futures.
***JDK8的CompletableFuture类更加简洁方便。***
ListenableFuture 可以设置回调函数，当提交的任务执行结束后，执行回调函数。

```java
public class ListenableFutureExample {

    static ExecutorService service = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // futureAndGet();
        // listenableFuture();
        completableFuture();
    }

    /**
     * Java8 的 CompletableFuture 更加简洁方便
     */
    public static void completableFuture() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        }, service);
        future.whenComplete((result, cause) -> {
            System.out.println("I am finished and the result is " + result);
        });
    }

    /**
     * Guava ListenableFuture 类 函数回调
     */
    public static void listenableFuture() {
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(service);
        ListenableFuture<Integer> future = listeningExecutorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        // future1.addListener(() -> System.out.println("I am finished"), service);
        // 回调函数
        Futures.addCallback(future, new MyCallBack(), service);
    }

    static class MyCallBack implements FutureCallback<Integer> {
        @Override
        public void onSuccess(@Nullable Integer result) {
            System.out.println("I am finished and the result is " + result);
        }

        @Override
        public void onFailure(Throwable throwable) {
        }
    }


    /**
     * Future 类 通过get阻塞方法，获取异步记过
     */
    public static void futureAndGet() throws ExecutionException, InterruptedException {
        Future<?> future = service.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });
        // 阻塞的方式获取并处理
        Object result = future.get();
        System.out.println(result);
    }
}
```

# Guava Cache 缓存

## LRU 最近最少使用算法
一种常用的页面置换算法，选择最近最久未使用的页面予以淘汰。
**使用LinkedHashMap的removeEldestEntry()方法可以快速实现。** 也可以使用LinkedList与HashMap实现（LinkedList存储Key的顺序，HashMap存储键值对）。

- 缓存接口（注意，这里只控制了缓存的元素个数，没有控制内存大小，容易发生OutOfMemoryException异常。配合 **软引用** 可以解决问题）
```java
public interface LruCache<K, V> {

    void put(K key, V value);

    V get(K key);

    void remove(K key);

    int size();

    void clear();

    int limit();
}

```
- LinkedHashMap.removeEldestEntry()实现
```java
public class LinkedHashLruCache<K, V> implements LruCache<K, V> {

    /**
     * 内部类形式 继承LinkedHashMap，可以避免外部类继承LinkedHashMap时，暴露Map的方法
     * 如此只暴露接口定义的方法，更好的封装。
     */
    private static class InternalLruCache<K, V> extends LinkedHashMap<K, V> {

        private final int limit;

        public InternalLruCache(int limit) {
            super(16, 0.75f, true);
            this.limit = limit;
        }

        /**
         * LRU的关键：当现元素大小 超过了 最大限制值时（返回TRUE），移除最近最少使用的 键值对。
         *
         * @param eldest 最近最少使用的元素
         * @return 返回true，则移除集合中 最近最少使用的键值对
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > limit;
        }
    }

    /**
     * 缓存限制，最多存放 limit 个元素
     */
    private final int limit;

    /**
     * 缓存容器。实际上就是 LinkedHashMap 集合。
     */
    private final InternalLruCache<K, V> internalLruCache;

    public LinkedHashLruCache(int limit) {
        Preconditions.checkArgument(limit > 0, "The limit should bigger than zero.");
        this.limit = limit;
        this.internalLruCache = new InternalLruCache<>(limit);
    }

    @Override
    public void put(K key, V value) {this.internalLruCache.put(key, value);}

    @Override
    public V get(K key) {return this.internalLruCache.get(key);}

    @Override
    public void remove(K key) {this.internalLruCache.remove(key);}

    @Override
    public int size() {return this.internalLruCache.size();}

    @Override
    public void clear() {this.internalLruCache.clear();}

    @Override
    public int limit() {return this.limit;}

    @Override
    public String toString() {return internalLruCache.toString();}
}
```

- LinkedList与HashMap的实现
```java
public class LinkedListLruCache<K, V> implements LruCache<K, V> {

    // 缓存最大容量
    private final int limit;

    // 存放 LRU缓存的 KEY
    private final LinkedList<K> keys = new LinkedList<>();

    // 存放 LRU缓存的 KEY-VALUE
    private final HashMap<K, V> cache = new HashMap<>();

    public LinkedListLruCache(int limit) {this.limit = limit;}

    @Override
    public void put(K key, V value) {
        K newKey = Preconditions.checkNotNull(key);
        V newValue = Preconditions.checkNotNull(value);

        if (keys.size() >= limit) {
            // 移除 最老的元素
            K oldKey = keys.removeFirst();
            V remove = cache.remove(newKey);
        }
        keys.add(newKey);
        cache.put(newKey, newValue);
    }

    @Override
    public V get(K key) {
        boolean exist = keys.remove(key);
        if (!exist) {
            // 删除失败，说明不存在，返回NULL。
            return null;
        }
        // 将使用过的元素 放到队尾。
        keys.addLast(key);
        return cache.get(key);
    }

    @Override
    public void remove(K key) {
        boolean exist = keys.remove(key);
        if (exist) {
            cache.remove(key);
        }
    }

    @Override
    public int size() {return keys.size();}

    @Override
    public void clear() {
        this.keys.clear();
        this.cache.clear();
    }

    @Override
    public int limit() {return this.limit;}

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (K k : keys) {
            builder.append(k).append(" = ").append(cache.get(k)).append("; ");
        }
        return builder.toString();
    }
}
```

## Java Strong/Soft/Week/PhantomReference
**即Java的 强引用、软引用、弱引用、虚引用**  
[博文介绍](https://www.cnblogs.com/skywang12345/p/3154474.html)
- ***强引用：*** 使用最普遍的引用。如果一个对象具有强引用，那垃圾回收器绝不会回收它。当内存空间不足，Java虚拟机宁愿抛出OutOfMemoryError错误，使程序异常终止，也不会靠随意回收具有强引用的对象来解决内存不足的问题。
- ***软引用：*** 一个对象只具有软引用，则内存空间足够，垃圾回收器就不会回收它；如果内存空间不足了，就会回收这些对象的内存。只要垃圾回收器没有回收它，该对象就可以被程序使用。软引用可用来实现内存敏感的高速缓存。
- ***弱引用：*** 只具有弱引用的对象拥有更短暂的生命周期。在垃圾回收器线程扫描它所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。不过，由于垃圾回收器是一个优先级很低的线程，因此不一定会很快发现那些只具有弱引用的对象。
- ***虚引用：*** 就是形同虚设，与其他几种引用都不同，虚引用并不会决定对象的生命周期。如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收器回收。  
虚引用主要用来跟踪对象被垃圾回收器回收的活动。虚引用与软引用和弱引用的一个区别在于：虚引用必须和引用队列 （ReferenceQueue）联合使用。当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之 关联的引用队列中。
```java
public class ReferenceExample {

    /**
     * 设置 VM Options：-Xmx128M -Xms64M -XX:+PrintGCDetails
     */
    public static void main(String[] args) throws InterruptedException {
        // strongRef();
        // softReference();
        // weakReference();
        softWeakPhantomRef();
    }

    /**
     * 强引用：即便抛出 OutOfMemory ，强引用也不会被GC
     */
    public static void strongRef() throws InterruptedException {
        int current = 1;
        List<Ref> contianer = new ArrayList<>();
        for (; ; ) {
            contianer.add(new Ref(current));
            current++;
            System.out.println("The  [" + current + "] Ref will be insert into container");
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }

    /**
     * 软引用：new SoftReference<>(new Object()); 创建软引用对象
     * SoftReference 可以检测到JVM内存空间是否足够，不足时就GC掉软引用
     * 因为这个特性，适合用于开发 缓存Cache(适度放大休眠时间，给JVM充足时间执行GC)
     */
    public static void softReference() throws InterruptedException {
        int current = 1;
        List<SoftReference> contianer = new ArrayList<>();
        for (; ; ) {
            contianer.add(new SoftReference<>(new Ref(current)));
            current++;
            System.out.println("The  [" + current + "] Ref will be insert into container");
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * 弱引用：只要被扫描标记，就会被GC，不论是 Minor GC还是Full GC。
     * 年轻代回收内存：Minor GC；
     * 老年代回收内存：Major GC；
     * 整个堆回收内存：Full GC。
     */
    public static void weakReference() throws InterruptedException {
        int current = 1;
        List<WeakReference<Ref>> contianer = new ArrayList<>();
        for (; ; ) {
            contianer.add(new WeakReference<>(new Ref(current)));
            current++;
            System.out.println("The  [" + current + "] Ref will be insert into container");
            TimeUnit.MILLISECONDS.sleep(200);
        }
    }

    /**
     * 软/弱/虚引用的比较.
     * System.gc()需要时间，因此休眠
     */
    public static void softWeakPhantomRef() throws InterruptedException {
        /*Ref softRef = new Ref(10);
        SoftReference<Ref> soft = new SoftReference<>(softRef);
        softRef = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(softRef);
        System.out.println("【软引用】" + soft.get().index);*/

        /*Ref weakRef = new Ref(8);
        WeakReference<Ref> weak = new WeakReference<>(weakRef);
        weakRef = null;
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(weakRef);
        System.out.println("【弱引用】被GC掉，空指针异常：" + weak.get().index);*/

        ReferenceQueue<Ref> queue = new ReferenceQueue<>();
        PhantomReference<Ref> phantom = new PhantomReference<>(new Ref(5), queue);
        System.out.println("【虚引用】：" + phantom.get());
        System.gc();
        // TimeUnit.SECONDS.sleep(1);
        Reference<? extends Ref> object = queue.remove();
        System.out.println(object);
    }

    /**
     * GC时，会扫描两次。
     * 首次标记：判断是否需要执行finalize()方法，判定是否需要执行的标准——对象是否重写了finalize(),
     * 或者虚拟机已经调用了finalize()方法，若是，则判定为不执行。
     * 第二次标记：虚拟机执行finalize()方法，对象被放置F-Queue队列，若在finalize()方法中 对象被重新引用则存活
     * 否则被标记回收。
     */
    private static class Ref {
        // 1M
        private byte[] data = new byte[1024 * 1024];

        private final int index;

        public Ref(int index) {
            this.index = index;
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println("The index [" + index + "] will be GC.");
        }
    }
}
```

### SoftReference+LRU实现 InMemoryCache（内存缓存）

```java
public class SoftReferenceLruCache<K, V> implements LruCache<K, V> {
    private static class InternalLruCache<K, V> extends LinkedHashMap<K, SoftReference<V>> {
        private final int limit;

        public InternalLruCache(int limit) {
            super(16, 0.75f, true);
            this.limit = limit;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> eldest) {return this.size() > limit;}
    }

    private final int limit;

    private final InternalLruCache<K, V> cache;

    public SoftReferenceLruCache(int limit) {
        this.limit = limit;
        this.cache = new InternalLruCache<>(limit);
    }

    @Override
    public void put(K key, V value) {this.cache.put(key, new SoftReference<>(value));}

    @Override
    public V get(K key) {
        SoftReference<V> ref = this.cache.get(key);
        if (null == ref) {
            return null;
        }
        return ref.get();
    }

    @Override
    public void remove(K key) {this.cache.remove(key);}

    @Override
    public int size() {return this.cache.size();}

    @Override
    public void clear() {this.cache.clear();}

    @Override
    public int limit() {return this.limit;}
}
```

## Guava缓存：CacheBuilder
### 基本使用
```java
public class CacheLoaderTest {
    @Test
    public void testBasic() throws Exception {
        // 定义 LoadingCache 缓存
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder().maximumSize(10)
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Employee>() {
                    @Override
                    public Employee load(String key) throws Exception {
                        return findEmployeeByName(key);
                    }
                });
        Employee employee = cache.get("Alex");
        assertThat(employee, notNullValue());
        // 休眠40s，缓存数据会在30s过期，因此又会 重新 获取。
        // TimeUnit.SECONDS.sleep(40);
        // 只会打印一条输出。没有从接口中获取，而是从Cache缓存中取值了。
        employee = cache.get("Alex");
        assertThat(employee, notNullValue());
    }

    private Employee findEmployeeByName(final String name) {
        System.out.println("The employee " + name + " is load from DB.");
        return new Employee(name, name, name);
    }
}
```

### 缓存元素逐出策略：容量回收，定时回收，引用回收。
#### CacheLoader的两种创建方式
```java
// 创建 缓存 new创建
private CacheLoader<String, Employee> createCacheLoader() {
    return new CacheLoader<String, Employee>() {
        @Override
        public Employee load(String key) {
            return findEmployeeByName(key);
        }
    };
}

// 创建 缓存 Function创建
private CacheLoader<String, Employee> createCacheLoader() {
    return CacheLoader.from(key -> new Employee(key, key, key));
}
```
#### 容量回收-maximumSize
**将要超出最大元素个数 则 根据最近最少使用算法 逐出元素。**
```java
    @Test
    public void testEvictionBySize() {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder().maximumSize(3).build(createCacheLoader());
        cache.getUnchecked("Alex");
        cache.getUnchecked("Jack");
        cache.getUnchecked("Rose");

        assertThat(cache.size(), equalTo(3L));
        cache.getUnchecked("Susan");
        assertThat(cache.size(), equalTo(3L));
        assertThat(cache.getIfPresent("Alex"), nullValue());
        assertThat(cache.getIfPresent("Susan"), notNullValue());
    }
```

#### 容量回收-maximumWeight
**将要超出最大权重，则逐出元素。不能与maximumSize并用。**
```java
    /**
     * 测试 LRU清除策略
     * maximumWeight：设置最大总权重，不能和maximumSize一起使用；
     * concurrencyLevel：并发数为1，Segment=1，那么就不会将maximumWeight分为多个Segment。
     * weigher：设置权重函数；45/1=45。
     * （一般在 将要到达 最大权重时即会被回收）
     */
    @Test
    public void testEvictingByWeight() {
        Weigher<String, Employee> weigher = (key, employee) ->
                employee.getName().length() + employee.getEmpId().length() + employee.getDept().length();
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .maximumWeight(45)
                .concurrencyLevel(1)
                .weigher(weigher)
                .build(createCacheLoader());
        // 3*5=15
        cache.getUnchecked("Gavin");
        // 3*5=15
        cache.getUnchecked("Kevin");
        // 3*5=15，一个Segment，15*3=45 达到最大权重。
        cache.getUnchecked("Allen");
        assertThat(cache.size(), equalTo(3L));
        assertThat(cache.getUnchecked("Gavin"), notNullValue());

        // 3*6=18，超出最大权重。根据最近最少使用原则，需要逐出两个旧的最少使用的元素（若只逐出1个，cache已使用权重=45，不足以放入18）
        cache.getUnchecked("123456");
        assertThat(cache.size(), equalTo(2L));
        assertThat(cache.getIfPresent("Kevin"), nullValue());
        assertThat(cache.getIfPresent("Allen"), nullValue());
    }
```
#### 定时回收
1. expireAfterAccess：指定项在一定时间内没有读写，会移除该key。
2. expireAfterWrite：在指定项在一定时间内没有 创建/更新时，会移除该key。
```java
@Test
public void testEvictingByAccessTime() throws InterruptedException {
    LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.SECONDS)
            .build(createCacheLoader());
    assertThat(cache.getUnchecked("Alex"), notNullValue());
    assertThat(cache.size(), equalTo(1L));

    // 休眠1秒，元素还会存在。
    TimeUnit.SECONDS.sleep(1);
    assertThat(cache.getIfPresent("Alex"), notNullValue());
    // 在休眠2秒，元素就会被逐出
    TimeUnit.SECONDS.sleep(2);
    assertThat(cache.getIfPresent("Alex"), nullValue());
}
```
3. refreshAfterWrite：在指定时间内没有被创建/覆盖，则指定时间过后，再次访问时，会去刷新该缓存，在新值没有到来之前，始终返回旧值。（即过了指定时间，就再不从cache中获取了。）
```java
@Test
public void testCacheRefresh() throws InterruptedException {
    LoadingCache<String, Long> cache = CacheBuilder.newBuilder()
            .refreshAfterWrite(2, TimeUnit.SECONDS)
            .build(CacheLoader.from(key -> System.currentTimeMillis()));
    cache.getUnchecked("Test");

    Long result1 = cache.getUnchecked("Test");
    TimeUnit.SECONDS.sleep(3);
    Long result2 = cache.getUnchecked("Test");
    assertThat(result1 != result2, equalTo(Boolean.TRUE));
}
```

#### 引用回收：软引用与弱引用
1. Weak Reference 弱引用：JVM发生GC（FullGC或MinorGC都会被回收掉。）
    ```java
    // 设置 VM Options：-ea -XX:+PrintGCDetails。查看GC过程。
    // WeakReference 弱引用回收。
    @Test
    public void testWeakKey() throws InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .weakValues().weakKeys().build(createCacheLoader());
        assertThat(cache.getUnchecked("Test"), notNullValue());
        // 手动触发GC，同步调用的异步方法，即不会被立即执行。因此休眠一会儿。
        System.gc();
        TimeUnit.MILLISECONDS.sleep(500);
        assertThat(cache.getIfPresent("Test"), nullValue());
        System.out.println(cache.size());
    }
    ```
2. Soft Reference 软引用：GC是若内存不足，会被回收掉。
    ```java
    /**
     * Soft Reference 软引用,内存不足时，会被回收。
     * 设置缓存元素为 1M， JVM设置128M的堆内存（缓存不到128M时即会GC），进行测试
     * VM Option  -ea: -Xmx128M -Xms64M -XX:+PrintGCDetails
     */
    @Test
    public void testSoftKey() throws InterruptedException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .softValues().build(createCacheLoader());
        int i = 0;
        for (; ; ) {
            String key = "TestKey " + i;
            String value = "Employee " + i;
            cache.put(key, new Employee(value, value, value));
            System.out.println("The Employee [" + (i++) + "] is store into cache");
            TimeUnit.MILLISECONDS.sleep(600);
        }
    }
    ```
### 其他
#### Guava Cache中对null值的处理
```java
// import com.google.common.base.Optional;
@Test
public void testLoadNullValueUseOptional() {
    LoadingCache<String, Optional<Employee>> cache = CacheBuilder.newBuilder().build(CacheLoader
            .from(key -> Objects.equals(key, "null") ? Optional.fromNullable(null) : Optional.fromNullable(new Employee(key, key, key))));
    assertThat(cache.getUnchecked("Test").get(), notNullValue());
    assertThat(cache.getUnchecked("null").orNull(), nullValue());
    Employee defaultValue = new Employee("Default", "Default", "Default");
    assertThat(defaultValue.getName(), equalTo("Default"));
}
```

#### removalListener 移除事件监听
Guava Cache在移除元素时，可以通过设置 监听器 来监听移除的动作，来获取移除元素的原因、被移除元素的Key值和Value值。
```java
    @Test
    public void testCacheRemoveNotification() {
        // 缓存监听器，一旦移除元素，就执行 自定义的方法
        RemovalListener<String, String> listener = notification -> {
            if (notification.wasEvicted()) {
                // 移除元素的原因
                assertThat(notification.getCause(), is(RemovalCause.SIZE));
                // 移除元素的 KEY 值
                assertThat(notification.getKey(), equalTo("Test"));
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                // 设置 缓存中最大元素个数
                .maximumSize(3)
                // 添加 监听器，监听 移除元素 的动作
                .removalListener(listener)
                .build(CacheLoader.from(String::toUpperCase));
        cache.getUnchecked("Test");
        cache.getUnchecked("Jack");
        cache.getUnchecked("Rose");
        cache.getUnchecked("Harry");
    }
```
#### Guava Cache的Records 命中率
统计缓存的**命中率**与丢失率
```java
// 首次获取数据时，Cache中内容为空，因此命中率为0，错失率为 1；
@Test
public void testRecordStat() {
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .recordStats().build(CacheLoader.from(String::toUpperCase));
    assertThat(cache.getUnchecked("test"), equalTo("TEST"));
    CacheStats stats = cache.stats();
    assertThat(stats.hitCount(), equalTo(0L));
    assertThat(stats.missCount(), equalTo(1L));

    assertThat(cache.getUnchecked("test"), equalTo("TEST"));
    // CacheStats对象是不可变的，因此需要重新创建。
    stats = cache.stats();
    assertThat(stats.hitCount(), equalTo(1L));
    assertThat(stats.missCount(), equalTo(1L));
    System.out.println(stats.hitRate() + "," + stats.missRate());
}
```
#### 字符串配置 CacheBuilder
以上的缓存都是配置完成之后，需要修改代码来更改的。Guava中也可以通过字符串方式来配置 CacheBuilder，由于是字符串形式的，因此也可以被配置到 yml等配置文件中去。
```java
@Test
public void testCacheSpec() {
    String spec = "maximumSize=5,recordStats";
    CacheBuilderSpec builderSpec = CacheBuilderSpec.parse(spec);
    LoadingCache<String, String> cache =
            CacheBuilder.from(builderSpec).build(CacheLoader.from(String::toUpperCase));
}
```

# Guava Collections
Java8 问世之后，Guava的集合操作优势不太明显了。Guava只是封装的一个工具，方便的时候使用即可，不需要刻意为了使用而使用。
- **FluentIterable** 抽象类集合常用API：
    1. 过滤：FluentIterable.filter();
    2. 尾部添加：FluentIterable.append();
    3. 条件全部满足：FluentIterable.allMatch();
    4. 条件部分满足：FluentIterable.anyMatch();
    5. 第一个满条件的元素：FluentIterable.firstMatch();
    6. 获取首/尾元素：FluentIterable.first();
    7. 获取前n个元素：FluentIterable.limit(3);
    8. 元素拷贝：FluentIterable.copyInto();
    9. 元素循环：FluentIterable.cycle();
    10. 元素转换：FluentIterable.transform();
    11. 元素拼接字符串：FluentIterable.join();

- **Lists工具类**
    1. 笛卡尔积：
    ```java
    @Test
    public void testCartesianProduct() {
        List<List<String>> result = Lists.cartesianProduct(
                Lists.newArrayList("1", "2"),
                Lists.newArrayList("A", "B")
        );
        System.out.println(result);
    }
    ```
    2. 元素转换：Lists.transform(list,function);
    ```java
    @Test
    public void test() {
        List<String> result = Lists.newArrayList("Scala", "Java", "Python");
        Lists.transform(result, e -> e.toUpperCase()).forEach(System.out::println);
    }
    ```
    3. 顺序颠倒：Lists.reverse()
    ```java
    @Test
    public void testReverse() {
        ArrayList<String> list = Lists.newArrayList("1", "2", "3");
        List<String> reverse = Lists.reverse(list);
        assertThat(Joiner.on(",").join(reverse), equalTo("3,2,1"));
    }
    ```
    4. 分组提高效率：Lists.partition()
    ```java
    @Test
    public void testPartition() {
        ArrayList<String> list = Lists.newArrayList("1", "2", "3","4");
        List<List<String>> result = Lists.partition(list, 3);
        System.out.println(result.get(0));
        System.out.println(result.get(1));
    }
    ```
- **Sets工具类**
    1. 生成n个子集：Sets.combinations(set, n);
    ```java
    @Test
    public void testCombinations() {
        HashSet<Integer> aSet = Sets.newHashSet(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        Set<Set<Integer>> combinations = Sets.combinations(aSet, 2);
        System.out.println(combinations.size());
        combinations.forEach(System.out::println);
    }
    ```
    2. 差集（在A中不在B中）：Sets.difference(aSet, bSet);  
       交集（取出A、B共有）：Sets.intersection(aSet, bSet);  
       并集（A、B合并）：Sets.union(aSet, bSet);
    ```java
    @Test
    public void testDifference() {
        HashSet<Integer> aSet = Sets.newHashSet(1, 2, 3, 4);
        HashSet<Integer> bSet = Sets.newHashSet(1, 3, 6);
        Sets.SetView<Integer> difference = Sets.difference(aSet, bSet);
        System.out.println(difference);
    }
    ```
- **Maps**
    1. Map的transform转换：Maps.transformValues(map,function);
    ```java
    @Test
    public void testTransform() {
        Map<String, String> map = Maps.asMap(Sets.newHashSet("1", "2", "3"), k -> k + "_value");
        Map<String, String> transform = Maps.transformValues(map, v -> v + "_transform");
        System.out.println(transform);
    }
    ```
    2. filter过滤：Maps.filterKeys(map,function) 或 filterValues(map,function)  
    Java8过滤：lambda表达式
    ```java
    @Test
    public void testFilter() {
        Map<String, String> map = Maps.asMap(Sets.newHashSet("1", "2", "3"), k -> k + "_value");
        Map<String, String> filter = Maps.filterKeys(map, k -> Lists.newArrayList("2", "3").contains(k));
        System.out.println("Guava "+filter);
        // java 8: k->k.getKey()/k.getValue() 根据 key或value过滤
        Map<String, String> filter2 = map.entrySet().stream()
                .filter(k -> Lists.newArrayList("2", "3").contains(k.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("Java8 "+filter2);
    }
    ```
- **Multimaps：** ArrayListMultimap，LinkedListMultimap。key相同，value不会被覆盖，而是按照数据结构存储成 数组或链表。
```java
@Test
public void test() {
    LinkedListMultimap<String, String> multimap = LinkedListMultimap.create();
    multimap.put("1", "1");
    multimap.put("1", "2");
    assertThat(multimap.size(), equalTo(2));
    System.out.println(multimap);
}
```

- **BiMap:** value不允许重复(抛异常：IllegalArgumentException)，key相同会覆盖value。
    1. 反转 Key-Value：HashBiMap.inverse();
    ```java
    @Test
    public void test() {
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2"); biMap.put("5", "7"); biMap.put("8", "12");
        System.out.println(biMap);
        BiMap<String, String> inverse = biMap.inverse();
        System.out.println(inverse);
    }
    ```
    2. 强制插入重复value，会覆盖key值：HashBiMap.forcePut();
    ```java
    @Test
    public void testCreateAndForcePut(){
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("1", "2");
        biMap.forcePut("2","2");
        System.out.println(biMap);
    }
    ```
- **Table**   
table的数据结构类似于：Map<String,Map<String,String>>
```java
@Test
public void testTableBasic() {
    // Table<R, C, V>：Row行，Column列，Value值
    Table<String, String, String> table = HashBasedTable.create();
    table.put("Language", "Java", "8");
    table.put("Language", "Scala", "29.0");
    table.put("SQL", "Oracle", "12C");
    table.put("SQL", "MySql", "8.0");
    System.out.println(table);
    Map<String, String> rowSql = table.row("SQL");
    assertThat(rowSql.containsKey("MySql"), equalTo(true));
    System.out.println(rowSql.get("Oracle"));

    Set<Table.Cell<String, String, String>> cells = table.cellSet();
    System.out.println("【CellSet】= " + cells);
}
```

- **Range 区间** 
    0. 无穷区间：Range.all();
    1. 闭区间[0,9]：Range.closed(0, 9);
    2. 开区间(0,9)：Range.open(0, 9);
    3. 左闭右开[0,9)：Range.closedOpen(0, 9);
    4. 左开右闭(0,0]：Range.openClosed(0, 9);
    5.  (10,+∞)：
    ```java
    Range.greaterThan(10); // 比10 大
    Range.downTo(10, BoundType.OPEN); // 最小是10，自定义开闭区间
    ```
    6.  (-∞,10)：
    ```java
    Range.lessThan(10); // 比10小
    Range.upTo(10, BoundType.OPEN); // 最大是10，自定义开闭区间
    ```
    7.  [10,+∞)：
    ```java
    Range.atLeast(10);
    Range.downTo(10, BoundType.CLOSED);
    ```
    8.  (-∞..10]：
    ```java
    Range.atMost(10);
    Range.upTo(10, BoundType.CLOSED);
    ```
    9. 字符串区间：
    ```java
    @Test
    public void testMapRange() {
        // TreeMap有序，默认根据 ASCII码表顺序
        TreeMap<String, Integer> treeMap = Maps.newTreeMap();
        treeMap.put("Scala", 1); treeMap.put("Guava", 2); treeMap.put("Java", 3); treeMap.put("Kafka", 4);
        System.out.println("初始TreeMap： " + treeMap);

        NavigableMap<String, Integer> subMap = Maps.subMap(treeMap, Range.open("Guava", "Scala"));
        System.out.println("符合区间的TreeMap："+subMap);
    }
    ```
- **RangeMap** 区间 对应 Object  
例如：0~59 得C；60~79 得B；80~100 得A；
```java
@Test
public void testRangeMap() {
    TreeRangeMap<Integer, String> gradeRange = TreeRangeMap.create();
    gradeRange.put(Range.closed(0, 59), "C");
    gradeRange.put(Range.closed(60, 79), "B");
    gradeRange.put(Range.closed(80, 100), "A");
    assertThat(gradeRange.get(82), equalTo("A"));
    System.out.println(gradeRange.get(59));
}
```

- **Immutable Collections 不可变集合**  
```java
@Test(expected = UnsupportedOperationException.class)
public void testOf() {
    ImmutableList<Integer> list = ImmutableList.of(1, 2, 3);
    assertThat(list, notNullValue());
    // 不支持修改，excepted 处理 异常
    list.add(4);
    fail();
}

/**
 * ImmutableList.copyOf(array);
 */
@Test
public void testCopy() {
    Integer[] array = {1, 2, 3, 4, 5};
    ImmutableList<Integer> immutableList = ImmutableList.copyOf(array);
    System.out.println(immutableList);
}

/**
 * Builder方法创建
 */
@Test
public void testBuilder() {
    ImmutableList<Integer> immutableList = ImmutableList.<Integer>builder().add(1).add(2).add(3)
            .addAll(Arrays.asList(4, 5)).build();
    System.out.println(immutableList);
}

@Test
public void testImmutableMap() {
    ImmutableMap<String, String> map = ImmutableMap.<String, String>builder().put("SpringCloud", "2.5").put("Dubbo", "1.8").build();
    System.out.println(map);
}
```

- **集合排序**  
1. Java8的排序方法
```java
/**
 * JDK 排序方法，按照自然顺讯排序：无法对有 null 元素的集合排序
 */
@Test
public void testJdkSort() {
    List<Integer> list = Arrays.asList(1, 5, 6, 3, null, 7, 2, 8, 0);
    Collections.sort(list);
    System.out.println(list);
}
```

2. Guava的排序方法
```java
/**
 * 集合中存在 null值 的排序方法。
 */
@Test
public void testOrderNaturalByNullFirstOrNullLast() {
    List<Integer> ordered = Arrays.asList(1, 5, 6, 3, 7, 2, 8, 0);
    Collections.sort(ordered);
    System.out.println("是否按照自然顺序排序：" + Ordering.natural().isOrdered(ordered));

    List<Integer> list = Arrays.asList(1, 5, 6, 3, null, 7, 2, 8, 0);
    Collections.sort(list, Ordering.natural().nullsFirst());
    System.out.println(list);

    Collections.sort(list, Ordering.natural().nullsLast());
    System.out.println(list);
}
```