package com.sf.fine.rpc.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProviderServiceCache {

    private static final Map<String, Object> SERVICE_CACHE = new ConcurrentHashMap<>();

    private ProviderServiceCache() {}

    public static void addService(String serviceName, Object service) {
        SERVICE_CACHE.putIfAbsent(serviceName, service);
    }

    public static Object getService(String serviceName) {
        return SERVICE_CACHE.get(serviceName);
    }

}
