package com.wang.think.mybatisspring;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.StringUtils;

/**
 * {@link #scan(String...)}是父类的全局方法，扫描工作由 {@link #doScan(String...)}委托给了
 * {@link org.springframework.context.annotation.ClassPathBeanDefinitionScanner#doScan(java.lang.String...)}方法。
 *
 * @description: 扫描 Java 文件
 * @author: wei·man cui
 * @date: 2021/1/20 23:13
 */
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(org.mybatis.spring.mapper.ClassPathMapperScanner.class);
    private boolean addToConfig = true;
    private boolean lazyInitialization;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;
    private String sqlSessionTemplateBeanName;
    private String sqlSessionFactoryBeanName;
    private Class<? extends Annotation> annotationClass;
    private Class<?> markerInterface;
    private Class<? extends org.mybatis.spring.mapper.MapperFactoryBean> mapperFactoryBeanClass = org.mybatis.spring.mapper.MapperFactoryBean.class;

    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void setLazyInitialization(boolean lazyInitialization) {
        this.lazyInitialization = lazyInitialization;
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public void setSqlSessionTemplateBeanName(String sqlSessionTemplateBeanName) {
        this.sqlSessionTemplateBeanName = sqlSessionTemplateBeanName;
    }

    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setMapperFactoryBean(org.mybatis.spring.mapper.MapperFactoryBean<?> mapperFactoryBean) {
        this.mapperFactoryBeanClass = mapperFactoryBean == null ? org.mybatis.spring.mapper.MapperFactoryBean.class : mapperFactoryBean.getClass();
    }

    public void setMapperFactoryBeanClass(Class<? extends org.mybatis.spring.mapper.MapperFactoryBean> mapperFactoryBeanClass) {
        this.mapperFactoryBeanClass = mapperFactoryBeanClass == null ? MapperFactoryBean.class : mapperFactoryBeanClass;
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;
        if (this.annotationClass != null) {
            this.addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        if (this.markerInterface != null) {
            this.addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            this.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
                return true;
            });
        }

        this.addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            // 如果没有扫描到 任务 Java 文件，那么发出警告
            LOGGER.warn(() -> {
                return "No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.";
            });
        } else {
            // 处理 bean definition
            this.processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        // 遍历 beanDefinitions（Java的Mapper接口映射文件），构造 MapperFactoryBean 类型的 bean。
        for (Iterator var3 = beanDefinitions.iterator(); var3.hasNext(); definition.setLazyInit(this.lazyInitialization)) {
            BeanDefinitionHolder holder = (BeanDefinitionHolder) var3.next();
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            LOGGER.debug(() -> {
                return "Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName + "' mapperInterface";
            });
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(this.mapperFactoryBeanClass);
            definition.getPropertyValues().add("addToConfig", this.addToConfig);
            boolean explicitFactoryUsed = false;
            if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
                definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
                explicitFactoryUsed = true;
            } else if (this.sqlSessionFactory != null) {
                definition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
                explicitFactoryUsed = true;
            }

            if (StringUtils.hasText(this.sqlSessionTemplateBeanName)) {
                if (explicitFactoryUsed) {
                    LOGGER.warn(() -> {
                        return "Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.";
                    });
                }

                definition.getPropertyValues().add("sqlSessionTemplate", new RuntimeBeanReference(this.sqlSessionTemplateBeanName));
                explicitFactoryUsed = true;
            } else if (this.sqlSessionTemplate != null) {
                if (explicitFactoryUsed) {
                    LOGGER.warn(() -> {
                        return "Cannot use both: sqlSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.";
                    });
                }

                definition.getPropertyValues().add("sqlSessionTemplate", this.sqlSessionTemplate);
                explicitFactoryUsed = true;
            }

            if (!explicitFactoryUsed) {
                LOGGER.debug(() -> {
                    return "Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.";
                });
                definition.setAutowireMode(2);
            }
        }

    }

    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            LOGGER.warn(() -> {
                return "Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface. Bean already defined with the same name!";
            });
            return false;
        }
    }
}
