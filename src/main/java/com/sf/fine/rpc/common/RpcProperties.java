package com.sf.fine.rpc.common;


//@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {

    private String serviceAddress;
    private int servicePort;
    private String serviceRegistryAddress;

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
}
