package com.sf.fine.rpc.consumer;

import com.sf.fine.rpc.registry.ServiceMetadata;
import com.sf.fine.rpc.registry.ServiceRegistryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class RpcConsumerBean implements FactoryBean {

    private final ServiceMetadata serviceMetadata = new ServiceMetadata();

    private Object target;

    public void init() {
        target = Proxy.newProxyInstance(
                serviceMetadata.getServiceInterface().getClassLoader(),
                new Class<?>[]{serviceMetadata.getServiceInterface()},
                new RpcInvocationHandler(serviceMetadata.getServiceVersion(),
                        ServiceRegistryFactory.getServiceRegistry(serviceMetadata.getServiceRegistryAddress(),
                                serviceMetadata.getServiceRegistryPort())));
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceMetadata.setServiceInterface(serviceInterface);
    }

    public void setServiceName(String serviceName) {
        this.serviceMetadata.setServiceName(serviceName);
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceMetadata.setServiceVersion(serviceVersion);
    }

    public void setServiceRegistryAddress(String serviceRegistryAddress) {
        this.serviceMetadata.setServiceRegistryAddress(serviceRegistryAddress);
    }

    public void setServiceRegistryPort(int serviceRegistryPort) {
        this.serviceMetadata.setServiceRegistryPort(serviceRegistryPort);
    }

    @Override
    public Object getObject() throws Exception {
        return target;
    }

    @Override
    public Class<?> getObjectType() {
        return serviceMetadata.getServiceInterface();
    }

}
