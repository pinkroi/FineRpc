package com.sf.fine.rpc.provider;

import com.sf.fine.rpc.annotation.RpcProvider;
import com.sf.fine.rpc.common.BinderUtils;
import com.sf.fine.rpc.common.RpcProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcProviderAnnotationRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware,
        BeanClassLoaderAware {
    private static final Logger LOG = LoggerFactory.getLogger(RpcProviderAnnotationRegister.class);

    private ClassLoader classLoader;
    private ConfigurableEnvironment environment;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Assert.notNull(environment, "environment must not be null");
        RpcProperties rpcProperties = BinderUtils.bind(environment, "spring.rpc", RpcProperties.class);
        Assert.notNull(rpcProperties, "rpcProperties must not be null");

        try {
            Class<?> targetClass = classLoader.loadClass(importingClassMetadata.getClassName());
            RpcProvider rpcProvider = targetClass.getAnnotation(RpcProvider.class);
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcProviderBean.class);
            builder.setInitMethodName("init");
            builder.addPropertyValue("serviceName", rpcProvider.serviceInterface().getName());
            builder.addPropertyValue("serviceVersion", rpcProvider.serviceVersion());

            builder.addPropertyValue("serviceAddress", rpcProperties.getServiceAddress());
            builder.addPropertyValue("servicePort", rpcProperties.getServicePort());
            builder.addPropertyValue("serviceRegistryAddress", rpcProperties.getServiceRegistryAddress());
            builder.addPropertyValue("serviceRegistryPort", rpcProperties.getServiceRegistryPort());

            builder.addPropertyValue("targetClass", targetClass);

            BeanDefinition beanDefinition = builder.getBeanDefinition();
            registry.registerBeanDefinition(rpcProvider.getClass().getName()+"@RpcProvider", beanDefinition);
        } catch (ClassNotFoundException e) {
            LOG.error("error create bean definition err={}", e.getMessage(), e);
            throw new BeanDefinitionStoreException("error create bean definition", e);
        }

    }

}
