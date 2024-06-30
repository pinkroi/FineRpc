package com.sf.fine.rpc.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ZookeeperServiceRegistry implements ServiceRegistry {

    private AtomicBoolean isInit = new AtomicBoolean(false);
    private CuratorFramework client;
    private final Object lock = new Object();
    private ServiceDiscovery<ServiceMetadata> serviceDiscovery;
    /**
     * 本地缓存，避免不必要的网络请求
     */
    private Map<String, ServiceProvider<ServiceMetadata>> serviceProviderCache;
    private List<Closeable> closeableProviders = new ArrayList<>();

    @Override
    public void init() throws Exception {
        if (!isInit.compareAndSet(false, true)) {
            return;
        }
        serviceProviderCache = new ConcurrentHashMap<>(256);

        this.client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        this.client.start();
        JsonInstanceSerializer<ServiceMetadata> serializer = new JsonInstanceSerializer<>(ServiceMetadata.class);
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetadata.class)
                .client(this.client)
                .serializer(serializer)
                .basePath("/rpc")
                .build();
        serviceDiscovery.start();
    }

    @Override
    public void registry(ServiceMetadata serviceMetadata) throws Exception {
        ServiceInstance<ServiceMetadata> serviceInstance = ServiceInstance
                .<ServiceMetadata>builder()
                //使用{服务名}:{服务版本}唯一标识一个服务
                .name(serviceMetadata.getServiceName()+":"+serviceMetadata.getServiceVersion())
                .address(serviceMetadata.getServiceAddress())
                .port(serviceMetadata.getServicePort())
                .payload(serviceMetadata)
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegistry(ServiceMetadata serviceMetadata) throws Exception {
        ServiceInstance<ServiceMetadata> serviceInstance = ServiceInstance
                .<ServiceMetadata>builder()
                //使用{服务名}:{服务版本}唯一标识一个服务
                .name(serviceMetadata.getServiceName()+":"+serviceMetadata.getServiceVersion())
                .address(serviceMetadata.getServiceAddress())
                .port(serviceMetadata.getServicePort())
                .payload(serviceMetadata)
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public ServiceMetadata discovery(String serviceName) throws Exception {
        ServiceProvider<ServiceMetadata> serviceProvider = serviceProviderCache.get(serviceName);
        if (null == serviceProvider) {
            synchronized (lock) {
                serviceProvider = serviceDiscovery
                        .serviceProviderBuilder()
                        .serviceName(serviceName)
                        //设置负载均衡策略，这里使用轮询
                        .providerStrategy(new RoundRobinStrategy<>())
                        .build();
                serviceProvider.start();
                closeableProviders.add(serviceProvider);
                serviceProviderCache.put(serviceName, serviceProvider);
            }
        }
        ServiceInstance<ServiceMetadata> serviceInstance = serviceProvider.getInstance();
        return serviceInstance != null ? serviceInstance.getPayload() : null;
    }

    @Override
    public void close() throws IOException {
        for (Closeable closeable : closeableProviders) {
            CloseableUtils.closeQuietly(closeable);
        }
        serviceDiscovery.close();
    }
}
