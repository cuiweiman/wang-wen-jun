package com.wang.boot;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.*;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Sprig事件和监听器
 * 事件是用来通知 监听事件 的 监听器 某件事情的处理状态，在SpringBoot启动过程中，除了Spring 框架的事件外，
 * SpringBoot 还会触发其它的一些事件，这些事件按照以下顺序触发：
 * 1. {@link ApplicationStartingEvent}：项目刚启动时触发，此时除了注册监听器和除湿器之外，其他所有处理器都没有开始。
 * 2. {@link ApplicationEnvironmentPreparedEvent}：上下文得到环境信息后触发，此时上下文还没有开始创建。
 * 3. {@link ApplicationPreparedEvent}：bean 的定义信息加载完成之后触发，此时 bean 还没有初始化。
 * 4. {@link ApplicationReadyEvent}：在所有 bean 初始化完毕，所有回调处理完成，系统准备处理 服务请求时，触发。
 * 5. {@link ApplicationFailedEvent}：启动过程中出现异常时触发。
 * <p>
 * SpringBoot的容器 ApplicationContext：
 * SpringBoot启动时会选择合适的 ApplicationContext 实现类，默认情况下，创建 {@link AnnotationConfigApplicationContext}
 * 如果是 WEB 应用，则会创建 {@link AnnotationConfigServletWebServerApplicationContext};
 * 如果是 webflux 应用，则会创建 {@link AnnotationConfigReactiveWebServerApplicationContext}。
 *
 * <p>
 * SpringBoot 的 {@link ApplicationRunner}接口和 {@link CommandLineRunner}接口：
 * 如果想在SpringBoot 启动时传入一些参数进行业务逻辑处理，可以去实现 ApplicationRunner 接口或者 CommandLineRunner 接口的
 * run() 方法，并且可以通过 @Order({@link Order}) 注解 保证执行顺序，Order 注解的 数值越小，执行的优先级越高。
 * <p>
 * SpringBootApplication 注解：将当前类所在包路径作为基准目录，扫描并加载基准目录下所有的类以及子目录下的类。
 * 等价于 @EnableAutoConfiguration：自动配置机制，
 * + @ComponentScan：扫描项目中所有的组件（包括 @Component、@Service、@Repository）
 * + @Configuration：是 Spring 提供的一个注解，代替传统的 xml 配置文件，将Spring框架中的 xml 文件以Java类的形式替代。
 * {@link EnableAutoConfiguration} SpringBoot全局开关，自动导入配置机制：{@link AutoConfigurationImportSelector#isEnabled(AnnotationMetadata)}
 *
 * @description:
 * @author: wei·man cui
 * @date: 2021/1/28 17:42
 */
@SpringBootApplication
public class MainApplication {
    /**
     * SpringBoot 启动方式 一
     *
     * @param args main参数
     */
    /*public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }*/

    /**
     * SpringBoot 启动方式 二
     *
     * @param args main参数
     */
    /*public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MainApplication.class);
        // 关闭 banner 打印
        // app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }*/

    /**
     * SpringBoot 启动方式 三
     * <p>
     * {@link SpringApplicationBuilder#web(WebApplicationType)}：选择服务的启动类型，可以选择性的嵌入不同的服务器。
     * WebApplicationType.NONE：应用程序不应作为web应用程序运行，也不应启动嵌入式web服务器。
     * WebApplicationType.SERVLET：应用程序应作为基于servlet的web应用程序运行，并应启动嵌入式servlet web服务器。对应 spring-webmvc
     * WebApplicationType.REACTIVE：webflux响应式类型，非阻塞的 web 框架，可以让web应用可以tcp长连接传输流数据。对应 spring-webflux
     *
     * @param args main参数
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(MainApplication.class)
                // 关闭 banner 打印
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
