package com.sf.fine.rpc.common;

import com.sf.fine.rpc.protocol.RpcRequest;
import com.sf.fine.rpc.registry.ServiceMetadata;

public class ServiceUtils {

    public static String uniqueServiceName(ServiceMetadata serviceMetadata) {
        return serviceMetadata.getServiceName()+":"+serviceMetadata.getServiceVersion();
    }

    public static String uniqueServiceName(RpcRequest request) {
        return request.getServiceName()+":"+request.getServiceVersion();
    }

    private ServiceUtils() {}

}
