package com.wang.think.mybatisspring;

import static org.springframework.util.Assert.notNull;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.support.DaoSupport;

/**
 * 由于本类的顶级父类：{@link org.springframework.dao.support.DaoSupport} 实现了 {@link InitializingBean} 接口，因此本类的
 * 初始化方法参见 {@link DaoSupport#afterPropertiesSet()}，分析该方法之后会发现，重点方法还是本类的 {@link #checkDaoConfig()}。
 * <p>
 * 同样的，由于实现了 {@link FactoryBean} 接口，在调用 getBean() 方法获取实例时，实际上是会调用本类中的 {@link #getObject()}方法。
 * <p>
 * 当 *Mapper.java 即映射文件较多时，我们可以使用 MapperScannerConfigurer 来扫描特定的包，自动帮我们成批的创建 映射器，配置如下：
 * <pre class="code">
 * {@code
 *  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
 *      <property name="basePackage" value="com.wang.think.mybatisspring"/>
 *  </bean>
 * }
 * </pre>
 * 实现原理需前往阅读{@link org.mybatis.spring.mapper.MapperScannerConfigurer}
 * <p>
 * BeanFactory that enables injection of MyBatis mapper interfaces. It can be set up with a SqlSessionFactory or a
 * pre-configured SqlSessionTemplate.
 * <p>
 * Sample configuration:
 *
 * <pre class="code">
 * {@code
 *   <bean id="baseMapper" class="org.mybatis.spring.mapper.MapperFactoryBean" abstract="true" lazy-init="true">
 *     <property name="sqlSessionFactory" ref="sqlSessionFactory" />
 *   </bean>
 *
 *   <bean id="oneMapper" parent="baseMapper">
 *     <property name="mapperInterface" value="my.package.MyMapperInterface" />
 *   </bean>
 *
 *   <bean id="anotherMapper" parent="baseMapper">
 *     <property name="mapperInterface" value="my.package.MyAnotherMapperInterface" />
 *   </bean>
 * }
 * </pre>
 * <p>
 * Note that this factory can only inject <em>interfaces</em>, not concrete classes.
 *
 * @author Eduardo Macarron
 * @see SqlSessionTemplate
 */
public class MapperFactoryBean<T> extends SqlSessionDaoSupport implements FactoryBean<T> {

    private Class<T> mapperInterface;

    private boolean addToConfig = true;

    public MapperFactoryBean() {
        // intentionally empty
    }

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkDaoConfig() {
        // 调用 父类的 checkDaoConfig()方法，校验 sqlSessionTemplate 不为空
        /*
        所以在如下配置中：
        <bean id="userMapperPo" class="org.mybatis.spring.mapper.MapperFactoryBean">
            <property name="mapperInterface" value="com.wang.think.mybatisspring.UserPoMapper"/>
            <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
        </bean>
        如果 sqlSessionFactory 属性没有被配置，会被检测出来。就是在此处做的校验。
         */
        super.checkDaoConfig();

        /*
          映射接口的验证。检测 mapperInterface 属性不能为空。
         */
        notNull(this.mapperInterface, "Property 'mapperInterface' is required");

        Configuration configuration = getSqlSession().getConfiguration();
        if (this.addToConfig && !configuration.hasMapper(this.mapperInterface)) {
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception e) {
                logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                throw new IllegalArgumentException(e);
            } finally {
                ErrorContext.instance().reset();
            }
        }
    }

    /**
     * 获取 MapperFactoryBean 实例，Spring 通过 {@link FactoryBean#getObject} 方法
     * 封装了 MyBatis 源码中获取 Mapper 示例的方法。
     * {@inheritDoc}
     */
    @Override
    public T getObject() throws Exception {
        return getSqlSession().getMapper(this.mapperInterface);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    // ------------- mutators --------------

    /**
     * Sets the mapper interface of the MyBatis mapper
     *
     * @param mapperInterface class of the interface
     */
    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * Return the mapper interface of the MyBatis mapper
     *
     * @return class of the interface
     */
    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    /**
     * If addToConfig is false the mapper will not be added to MyBatis. This means it must have been included in
     * mybatis-config.xml.
     * <p>
     * If it is true, the mapper will be added to MyBatis in the case it is not already registered.
     * <p>
     * By default addToConfig is true.
     *
     * @param addToConfig a flag that whether add mapper to MyBatis or not
     */
    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    /**
     * Return the flag for addition into MyBatis config.
     *
     * @return true if the mapper will be added to MyBatis in the case it is not already registered.
     */
    public boolean isAddToConfig() {
        return addToConfig;
    }
}
