package com.sf.fine.rpc.registry;

public class ServiceRegistryFactory {

    private static ServiceRegistry serviceRegistry;

    public synchronized static ServiceRegistry getServiceRegistry(String host, int port) {
        if (null == serviceRegistry) {
            serviceRegistry = new ZookeeperServiceRegistry(host, port);
            return serviceRegistry;
        }
        return serviceRegistry;
    }

}
