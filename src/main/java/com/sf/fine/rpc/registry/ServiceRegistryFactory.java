package com.sf.fine.rpc.registry;

public class ServiceRegistryFactory {

    private static ServiceRegistry serviceRegistry;

    public static ServiceRegistry getServiceRegistry() {
        if (null == serviceRegistry) {
            serviceRegistry = new ZookeeperServiceRegistry();
            try {
                serviceRegistry.init();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return serviceRegistry;
        }
        return serviceRegistry;
    }

}
