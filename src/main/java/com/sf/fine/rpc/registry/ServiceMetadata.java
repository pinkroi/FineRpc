package com.sf.fine.rpc.registry;

public class ServiceMetadata {

    private Class<?> serviceInterface;
    private String serviceName;
    private String serviceVersion;
    private String serviceAddress;
    private int servicePort;
    private String serviceRegistryAddress;
    private int serviceRegistryPort;

    public ServiceMetadata() {
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

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

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public String getServiceRegistryAddress() {
        return serviceRegistryAddress;
    }

    public void setServiceRegistryAddress(String serviceRegistryAddress) {
        this.serviceRegistryAddress = serviceRegistryAddress;
    }

    public int getServiceRegistryPort() {
        return serviceRegistryPort;
    }

    public void setServiceRegistryPort(int serviceRegistryPort) {
        this.serviceRegistryPort = serviceRegistryPort;
    }

    @Override
    public String toString() {
        return "ServiceMetadata{" +
                "serviceName='" + serviceName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", servicePort=" + servicePort +
                ", serviceRegistryAddress='" + serviceRegistryAddress + '\'' +
                ", serviceRegistryPort=" + serviceRegistryPort +
                '}';
    }
}
