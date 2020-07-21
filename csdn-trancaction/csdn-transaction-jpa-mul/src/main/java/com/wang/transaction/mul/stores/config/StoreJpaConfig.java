package com.wang.transaction.mul.stores.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @description: 库存数据库 数据源配置
 * EnableJpaRepositories：配置jpa接口包名、实体管理工厂、事务管理器
 * @date: 2020/7/5 10:42
 * @author: wei·man cui
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.wang.transaction.mul.stores.repository",
        entityManagerFactoryRef = "storeEntityManagerFactory",
        transactionManagerRef = "storeTransactionManager")
public class StoreJpaConfig {

    @Resource
    private Environment env;

    /**
     * 库存数据源.注意前缀与application.yml中的一致
     * Primary：标识 默认使用本数据源。否则 有两个相同的 DataSource Bean，SpringBoot启动会报错。
     *
     * @return
     */
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
