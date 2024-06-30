package com.sf.fine.rpc.registry;

public class ServiceMetadata {

    private String serviceName;
    private String serviceVersion;
    private String serviceAddress;
    private int servicePort;

    public ServiceMetadata() {
    }

    public ServiceMetadata(String serviceName, String serviceVersion, String serviceAddress, int servicePort) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
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

    @Override
    public String toString() {
        return "ServiceMetadata{" +
                "serviceName='" + serviceName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", servicePort=" + servicePort +
                '}';
    }
}
