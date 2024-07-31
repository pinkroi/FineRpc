package com.sf.fine.rpc.registry;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.sf.fine.rpc.common.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class NacosServiceRegistry implements ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosServiceRegistry.class);

    private NamingService namingService;

    private static final AtomicBoolean isInit = new AtomicBoolean(false);

    public NacosServiceRegistry(String host, int port) {
        try {
            init(host, port);
        } catch (Exception e) {
            LOGGER.error("nacos naming service init error", e);
            throw new RuntimeException(e);
        }
    }

    private void init(String host, int port) throws Exception {
        if (!isInit.compareAndSet(false, true)) {
            return;
        }
        namingService = NamingFactory.createNamingService(host+":"+port);
    }

    @Override
    public void registry(ServiceMetadata serviceMetadata) throws Exception {
        namingService.registerInstance(ServiceUtils.uniqueServiceName(serviceMetadata),
                serviceMetadata.getServiceAddress(), serviceMetadata.getServicePort());
    }

    @Override
    public void unRegistry(ServiceMetadata serviceMetadata) throws Exception {

    }

    @Override
    public ServiceMetadata discovery(String serviceName) throws Exception {
        Instance instance = namingService.selectOneHealthyInstance(serviceName);
        ServiceMetadata serviceMetadata = new ServiceMetadata();
        if (null == instance) {
            return serviceMetadata;
        }
        String[] arr = serviceName.split(":");
        serviceMetadata.setServiceName(arr[0]);
        serviceMetadata.setServiceVersion(arr[1]);
        serviceMetadata.setServiceAddress(instance.getIp());
        serviceMetadata.setServicePort(instance.getPort());
        return serviceMetadata;
    }

    @Override
    public void close() throws IOException {

    }
}
