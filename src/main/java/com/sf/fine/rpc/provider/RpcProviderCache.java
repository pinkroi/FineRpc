package com.sf.fine.rpc.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcProviderCache {

    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    private RpcProviderCache() {}

    public static void add(String serviceName, Object target) {
        CACHE.putIfAbsent(serviceName, target);
    }

    public static Object get(String serviceName) {
        return CACHE.get(serviceName);
    }

}
