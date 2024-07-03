package com.sf.fine.rpc.consumer;

import com.sf.fine.rpc.protocol.RpcRequest;
import com.sf.fine.rpc.protocol.RpcResponse;
import com.sf.fine.rpc.registry.ServiceRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class RpcInvocationHandler implements InvocationHandler {

    private String serviceVersion;
    private ServiceRegistry serviceRegistry;

    public RpcInvocationHandler(String serviceVersion, ServiceRegistry serviceRegistry) {
        this.serviceVersion = serviceVersion;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            switch (name) {
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "toString":
                    return proxy.getClass().getName() + "@" +
                            Integer.toHexString(System.identityHashCode(proxy)) +
                            ", with InvocationHandler " + this;
                default:
                    throw new IllegalStateException(String.valueOf(method));
            }
        }

        RpcRequest request = new RpcRequest();
        request.setRequestId(String.valueOf(UUID.randomUUID()));
        request.setServiceName(method.getDeclaringClass().getName());
        request.setServiceVersion(serviceVersion);
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        ConsumerClient client = new ConsumerClient(serviceRegistry);
        RpcResponse rpcResponse = client.sendRequest(request);
        return rpcResponse.getResult();
    }

}
