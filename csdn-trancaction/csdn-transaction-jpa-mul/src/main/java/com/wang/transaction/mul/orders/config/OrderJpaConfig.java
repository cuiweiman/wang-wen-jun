package com.wang.transaction.mul.orders.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * @description: 订单数据库 数据源配置
 * EnableJpaRepositories：配置jpa接口包名、实体管理工厂、事务管理器
 * @date: 2020/7/5 10:41
 * @author: weiman cui
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.wang.transaction.mul.orders.repository",
        enableDefaultTransactions = false,
        entityManagerFactoryRef = "orderEntityManagerFactory",
        transactionManagerRef = "orderTransactionManager")
public class OrderJpaConfig {

    @Resource
    private Environment env;

    /**
     * 订单数据源.注意前缀与application.yml中的一致
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.order")
    public DataSource orderDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(orderDataSource());
        // 实体类 包路径
        factory.setPackagesToScan(new String[]{"com.wang.transaction.mul.orders.entity"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpa = new Properties();
        jpa.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        jpa.put("hibernate.dialect", env.getProperty("spring.datasource.order.jpa.dialect"));
        factory.setJpaProperties(jpa);
        return factory;
    }

    @Bean
    public PlatformTransactionManager orderTransactionManager() {
        EntityManagerFactory factory = orderEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

}
