package com.sf.fine.rpc.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rpc")
public class RpcProperties {

    private String serviceAddress;
    private String servicePort;
    private String serviceRegistryAddress;
    private String serviceRegistryPort;

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }

    public String getServiceRegistryAddress() {
        return serviceRegistryAddress;
    }

    public void setServiceRegistryAddress(String serviceRegistryAddress) {
        this.serviceRegistryAddress = serviceRegistryAddress;
    }

    public String getServiceRegistryPort() {
        return serviceRegistryPort;
    }

    public void setServiceRegistryPort(String serviceRegistryPort) {
        this.serviceRegistryPort = serviceRegistryPort;
    }
}
