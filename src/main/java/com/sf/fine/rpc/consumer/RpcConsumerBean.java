package com.sf.fine.rpc.consumer;

public class RpcConsumerBean {
    private String serviceName;
    private String serviceVersion;
    private String serviceRegistryAddress;
    private Object target;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getServiceRegistryAddress() {
        return serviceRegistryAddress;
    }

    public void setServiceRegistryAddress(String serviceRegistryAddress) {
        this.serviceRegistryAddress = serviceRegistryAddress;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

}
