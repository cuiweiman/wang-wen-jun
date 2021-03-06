package com.wang.think.mybatisspring;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;

import static org.springframework.util.Assert.notNull;

/**
 * 此类简化了 Spring.xml 中对于 Mapper映射文件 的配置（参考：test/mybatis-spring/spring.xml）。
 * 通过如下配置，Spring 会将 basePackage 下的所有接口进行自动注入，而不需要给每个接口重复的配置 mapperInterface 属性来声明。
 * <p>
 * <pre class="code">
 * {@code
 * <!--当 Mapper 映射器较多时，我们可以使用 {@see MapperScannerConfigurer} 来扫描特定的包，自动帮我们成批的创建 映射器。-->
 *  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
 *      <!--basePackage属性可以为映射接口设置基本的包路径，如果有多个包路径可以用“分号”或“逗号”作为分隔符-->
 *      <property name="basePackage" value="com.wang.think.mybatisspring"/>
 *  </bean>
 * }
 * </pre>
 * <p>
 * 实现方法分析：
 * 主要分析本类实现的三个接口方法：{@link InitializingBean#afterPropertiesSet()}、
 * {@link BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(BeanDefinitionRegistry)}，
 * 以及这个接口的 父接口：{@link BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)}，
 * 即：{@link #afterPropertiesSet()}、{@link #postProcessBeanDefinitionRegistry(BeanDefinitionRegistry)}
 * 和{@link #postProcessBeanFactory(ConfigurableListableBeanFactory)}，
 * <p>
 * 经过查看可得知，实现入口主要在 {@link #postProcessBeanDefinitionRegistry(BeanDefinitionRegistry)} 方法中。
 *
 *
 * <p>
 * BeanDefinitionRegistryPostProcessor that searches recursively starting from a base package for interfaces and
 * registers them as {@code MapperFactoryBean}. Note that only interfaces with at least one method will be registered;
 * concrete classes will be ignored.
 * <p>
 * This class was a {code BeanFactoryPostProcessor} until 1.0.1 version. It changed to
 * {@code BeanDefinitionRegistryPostProcessor} in 1.0.2. See https://jira.springsource.org/browse/SPR-8269 for the
 * details.
 * <p>
 * The {@code basePackage} property can contain more than one package name, separated by either commas or semicolons.
 * <p>
 * This class supports filtering the mappers created by either specifying a marker interface or an annotation. The
 * {@code annotationClass} property specifies an annotation to search for. The {@code markerInterface} property
 * specifies a parent interface to search for. If both properties are specified, mappers are added for interfaces that
 * match <em>either</em> criteria. By default, these two properties are null, so all interfaces in the given
 * {@code basePackage} are added as mappers.
 * <p>
 * This configurer enables autowire for all the beans that it creates so that they are automatically autowired with the
 * proper {@code SqlSessionFactory} or {@code SqlSessionTemplate}. If there is more than one {@code SqlSessionFactory}
 * in the application, however, autowiring cannot be used. In this case you must explicitly specify either an
 * {@code SqlSessionFactory} or an {@code SqlSessionTemplate} to use via the <em>bean name</em> properties. Bean names
 * are used rather than actual objects because Spring does not initialize property placeholders until after this class
 * is processed.
 * <p>
 * Passing in an actual object which may require placeholders (i.e. DB user password) will fail. Using bean names defers
 * actual object creation until later in the startup process, after all placeholder substitution is completed. However,
 * note that this configurer does support property placeholders of its <em>own</em> properties. The
 * <code>basePackage</code> and bean name properties all support <code>${property}</code> style substitution.
 * <p>
 * Configuration sample:
 *
 * <pre class="code">
 * {@code
 *   <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
 *       <property name="basePackage" value="org.mybatis.spring.sample.mapper" />
 *       <!-- optional unless there are multiple session factories defined -->
 *       <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
 *   </bean>
 * }
 * </pre>
 *
 * @author Hunter Presnall
 * @author Eduardo Macarron
 * @see MapperFactoryBean
 * @see org.mybatis.spring.mapper.ClassPathMapperScanner
 */
public class MapperScannerConfigurer
        implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {

    private String basePackage;

    private boolean addToConfig = true;

    private String lazyInitialization;

    private SqlSessionFactory sqlSessionFactory;

    private SqlSessionTemplate sqlSessionTemplate;

    private String sqlSessionFactoryBeanName;

    private String sqlSessionTemplateBeanName;

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;

    private Class<? extends MapperFactoryBean> mapperFactoryBeanClass;

    private ApplicationContext applicationContext;

    private String beanName;

    private boolean processPropertyPlaceHolders;

    private BeanNameGenerator nameGenerator;

    /**
     * This property lets you set the base package for your mapper interface files.
     * <p>
     * You can set more than one package by using a semicolon or comma as a separator.
     * <p>
     * Mappers will be searched for recursively starting in the specified package(s).
     *
     * @param basePackage base package name
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * Same as {@code MapperFactoryBean#setAddToConfig(boolean)}.
     *
     * @param addToConfig a flag that whether add mapper to MyBatis or not
     * @see MapperFactoryBean#setAddToConfig(boolean)
     */
    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    /**
     * Set whether enable lazy initialization for mapper bean.
     * <p>
     * Default is {@code false}.
     * </p>
     *
     * @param lazyInitialization Set the @{code true} to enable
     * @since 2.0.2
     */
    public void setLazyInitialization(String lazyInitialization) {
        this.lazyInitialization = lazyInitialization;
    }

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     *
     * @param annotationClass annotation class
     */
    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified interface class as a
     * parent.
     * <p>
     * Note this can be combined with annotationClass.
     *
     * @param superClass parent class
     */
    public void setMarkerInterface(Class<?> superClass) {
        this.markerInterface = superClass;
    }

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     * <p>
     *
     * @param sqlSessionTemplate a template of SqlSession
     * @deprecated Use {@link #setSqlSessionTemplateBeanName(String)} instead
     */
    @Deprecated
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     * <p>
     * Note bean names are used, not bean references. This is because the scanner loads early during the start process and
     * it is too early to build mybatis object instances.
     *
     * @param sqlSessionTemplateName Bean name of the {@code SqlSessionTemplate}
     * @since 1.1.0
     */
    public void setSqlSessionTemplateBeanName(String sqlSessionTemplateName) {
        this.sqlSessionTemplateBeanName = sqlSessionTemplateName;
    }

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     * <p>
     *
     * @param sqlSessionFactory a factory of SqlSession
     * @deprecated Use {@link #setSqlSessionFactoryBeanName(String)} instead.
     */
    @Deprecated
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     * <p>
     * Note bean names are used, not bean references. This is because the scanner loads early during the start process and
     * it is too early to build mybatis object instances.
     *
     * @param sqlSessionFactoryName Bean name of the {@code SqlSessionFactory}
     * @since 1.1.0
     */
    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryName;
    }

    /**
     * Specifies a flag that whether execute a property placeholder processing or not.
     * <p>
     * The default is {@literal false}. This means that a property placeholder processing does not execute.
     *
     * @param processPropertyPlaceHolders a flag that whether execute a property placeholder processing or not
     * @since 1.1.1
     */
    public void setProcessPropertyPlaceHolders(boolean processPropertyPlaceHolders) {
        this.processPropertyPlaceHolders = processPropertyPlaceHolders;
    }

    /**
     * The class of the {@link MapperFactoryBean} to return a mybatis proxy as spring bean.
     *
     * @param mapperFactoryBeanClass The class of the MapperFactoryBean
     * @since 2.0.1
     */
    public void setMapperFactoryBeanClass(Class<? extends MapperFactoryBean> mapperFactoryBeanClass) {
        this.mapperFactoryBeanClass = mapperFactoryBeanClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * Gets beanNameGenerator to be used while running the scanner.
     *
     * @return the beanNameGenerator BeanNameGenerator that has been configured
     * @since 1.2.0
     */
    public BeanNameGenerator getNameGenerator() {
        return nameGenerator;
    }

    /**
     * Sets beanNameGenerator to be used while running the scanner.
     *
     * @param nameGenerator the beanNameGenerator to set
     * @since 1.2.0
     */
    public void setNameGenerator(BeanNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    /**
     * 只是做了校验而已
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required");
    }

    /**
     * 故意留空
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // left intentionally blank
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.2
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        // ★★ processPropertyPlaceHolders 属性处理
        if (this.processPropertyPlaceHolders) {
            processPropertyPlaceHolders();
        }

        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        scanner.setAddToConfig(this.addToConfig);
        scanner.setAnnotationClass(this.annotationClass);
        scanner.setMarkerInterface(this.markerInterface);
        scanner.setSqlSessionFactory(this.sqlSessionFactory);
        scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
        scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
        scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setBeanNameGenerator(this.nameGenerator);
        // 这行源码在这里报错，先注释掉，以免影响其他方法的执行
        // scanner.setMapperFactoryBeanClass(this.mapperFactoryBeanClass);
        if (StringUtils.hasText(lazyInitialization)) {
            scanner.setLazyInitialization(Boolean.valueOf(lazyInitialization));
        }
        /*
         ★★ 根据之前属性的配置，注册生成对应的 过滤器，控制扫描结果。
         1. annotationClass 属性处理：若annotationClass不为空，表示用户配置了该属性，那么要根据此属性生成过滤器来达到用户想要的效果。
         而封装此属性的过滤器就是 AnnotationTypeFilter，它来保证在扫描对应Java文件时，只接受标记有注解为 annotationClass 的接口。
         2. markerInterface 属性处理：封装此属性的过滤器就是实现 AssignableTypeFilter 接口的局部类，表示扫描过程中只接受 实现了 markerInterface接口的接口。
         3. 全局默认处理：若前两个属性存在任何一个，acceptAllInterfaces 的值都会改变。但若都没有，那么 Spring 会使用默认过滤器
         实现 TypeFilter 接口的局部类，旨在接受所有的接口文件。
         4. package-info.java的处理：默认不作为逻辑实现接口，排除在外。使用 TypeFilter 接口的局部类来实现 match 方法。
         5. 定义的 过滤器都被记录在了 ClassPathMapperScanner#addIncludeFilter 和 ClassPathMapperScanner#addExcludeFilter 容器中。
         */
        scanner.registerFilters();
        // 扫描 Java 文件。
        scanner.scan(
                StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    /**
     * BeanDefinitionRegistries 在应用程序启动的早期调用，在 BeanFactoryPostProcessors 之前调用。
     * 这意味着 PropertyResourceConfigurers 将不会被加载，并且此类的属性替换都将失败。
     * 要避免这种情况，请找到上下文中定义的所有 PropertyResourceConfigurer，并在此类的 bean 定义上运行它们。然后更新值。
     * <p>
     * 1. 找到所有已经注册的 {@link PropertyResourceConfigurer} 类型的 bean；
     * 2. 模拟Spring中的环境来使用处理器。这里通过使用 new DefaultListableBeanFactory(); 来模拟 Spring 中的环境（完成处理器的调用后便失效），
     * 将映射的 bean。
     * <p>
     * BeanDefinitionRegistries are called early in application startup, before BeanFactoryPostProcessors. This means that
     * PropertyResourceConfigurers will not have been loaded and any property substitution of this class' properties will
     * fail. To avoid this, find any PropertyResourceConfigurers defined in the context and run them on this class' bean
     * definition. Then update the values.
     */
    private void processPropertyPlaceHolders() {
        Map<String, PropertyResourceConfigurer> prcs = applicationContext.getBeansOfType(PropertyResourceConfigurer.class,
                false, false);

        if (!prcs.isEmpty() && applicationContext instanceof ConfigurableApplicationContext) {
            BeanDefinition mapperScannerBean = ((ConfigurableApplicationContext) applicationContext).getBeanFactory()
                    .getBeanDefinition(beanName);

            // PropertyResourceConfigurer does not expose any methods to explicitly perform
            // property placeholder substitution. Instead, create a BeanFactory that just
            // contains this mapper scanner and post process the factory.
            DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
            factory.registerBeanDefinition(beanName, mapperScannerBean);

            for (PropertyResourceConfigurer prc : prcs.values()) {
                prc.postProcessBeanFactory(factory);
            }

            PropertyValues values = mapperScannerBean.getPropertyValues();

            this.basePackage = updatePropertyValue("basePackage", values);
            this.sqlSessionFactoryBeanName = updatePropertyValue("sqlSessionFactoryBeanName", values);
            this.sqlSessionTemplateBeanName = updatePropertyValue("sqlSessionTemplateBeanName", values);
            this.lazyInitialization = updatePropertyValue("lazyInitialization", values);
        }
        this.basePackage = Optional.ofNullable(this.basePackage).map(getEnvironment()::resolvePlaceholders).orElse(null);
        this.sqlSessionFactoryBeanName = Optional.ofNullable(this.sqlSessionFactoryBeanName)
                .map(getEnvironment()::resolvePlaceholders).orElse(null);
        this.sqlSessionTemplateBeanName = Optional.ofNullable(this.sqlSessionTemplateBeanName)
                .map(getEnvironment()::resolvePlaceholders).orElse(null);
        this.lazyInitialization = Optional.ofNullable(this.lazyInitialization).map(getEnvironment()::resolvePlaceholders)
                .orElse(null);
    }

    private Environment getEnvironment() {
        return this.applicationContext.getEnvironment();
    }

    private String updatePropertyValue(String propertyName, PropertyValues values) {
        PropertyValue property = values.getPropertyValue(propertyName);

        if (property == null) {
            return null;
        }

        Object value = property.getValue();

        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return value.toString();
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else {
            return null;
        }
    }

}
