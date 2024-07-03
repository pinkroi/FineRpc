package com.sf.fine.rpc.consumer;

import com.sf.fine.rpc.annotation.RpcConsumer;
import com.sf.fine.rpc.common.BinderUtils;
import com.sf.fine.rpc.common.RpcProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class RpcConsumerPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware,
        BeanClassLoaderAware, ApplicationContextAware {

    private ClassLoader classLoader;
    private ConfigurableEnvironment environment;
    private ApplicationContext applicationContext;

    private Map<String, BeanDefinition> beanDefinitions = new LinkedHashMap<String, BeanDefinition>();

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Assert.notNull(environment, "environment must not be null");
        RpcProperties rpcProperties = BinderUtils.bind(environment, "spring.rpc", RpcProperties.class);
        Assert.notNull(rpcProperties, "rpcProperties must not be null");

        try {
            for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
                BeanDefinition definition = beanFactory.getBeanDefinition(beanDefinitionName);

                String beanClassName = definition.getBeanClassName();
                // 当用 @Bean 返回的类型是Object时，beanClassName是 null
                if(beanClassName != null) {
                    Class<?> clazz = ClassUtils.resolveClassName(definition.getBeanClassName(), this.classLoader);
                    ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
                        @Override
                        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                            RpcConsumerPostProcessor.this.parseElement(field, rpcProperties);
                        }
                    });
                }
            }
            for (String beanName : beanDefinitions.keySet()) {
                if (applicationContext.containsBean(beanName)) {
                    throw new IllegalArgumentException("duplicate bean name in spring context " + beanName);
                }
                BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
                registry.registerBeanDefinition(beanName, beanDefinitions.get(beanName));
            }
        } catch (Exception e) {

        }
    }

    private void parseElement(Field field, RpcProperties rpcProperties) {
        RpcConsumer annotation = AnnotationUtils.getAnnotation(field, RpcConsumer.class);
        if (annotation == null) {
            return;
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcConsumerBean.class);
        builder.setInitMethodName("init");
        builder.addPropertyValue("serviceInterface", field.getType());
        builder.addPropertyValue("serviceName", field.getType().getName());
        builder.addPropertyValue("serviceVersion", annotation.serviceVersion());

        builder.addPropertyValue("serviceRegistryAddress", rpcProperties.getServiceRegistryAddress());
        builder.addPropertyValue("serviceRegistryPort", rpcProperties.getServiceRegistryPort());

        BeanDefinition beanDefinition = builder.getBeanDefinition();

        beanDefinitions.put(field.getName(), beanDefinition);
    }

}
