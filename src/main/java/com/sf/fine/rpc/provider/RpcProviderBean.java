package com.sf.fine.rpc.provider;

import com.sf.fine.rpc.common.ServiceUtils;
import com.sf.fine.rpc.registry.ServiceMetadata;
import com.sf.fine.rpc.registry.ServiceRegistryFactory;

public class RpcProviderBean {

    private final ServiceMetadata serviceMetadata = new ServiceMetadata();

    private Class<?> targetClass;

    public void init() {
        try {
            RpcProviderCache.add(ServiceUtils.uniqueServiceName(serviceMetadata), targetClass.newInstance());

            new Thread(
                    () -> ProviderServer.getInstance()
                            .startNettyServer(serviceMetadata.getServiceAddress(),
                                    serviceMetadata.getServicePort())
            ).start();

            ServiceRegistryFactory.getServiceRegistry(serviceMetadata.getServiceRegistryAddress(),
                    serviceMetadata.getServiceRegistryPort()).registry(serviceMetadata);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public void setServiceName(String serviceName) {
        this.serviceMetadata.setServiceName(serviceName);
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceMetadata.setServiceVersion(serviceVersion);
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceMetadata.setServiceAddress(serviceAddress);
    }

    public void setServicePort(int servicePort) {
        this.serviceMetadata.setServicePort(servicePort);
    }

    public void setServiceRegistryAddress(String serviceRegistryAddress) {
        this.serviceMetadata.setServiceRegistryAddress(serviceRegistryAddress);
    }

    public void setServiceRegistryPort(int serviceRegistryPort) {
        this.serviceMetadata.setServiceRegistryPort(serviceRegistryPort);
    }

}
