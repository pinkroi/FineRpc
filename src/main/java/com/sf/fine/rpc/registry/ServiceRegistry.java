package com.sf.fine.rpc.registry;

import java.io.IOException;

public interface ServiceRegistry {

    void registry(ServiceMetadata serviceMetadata) throws Exception;

    void unRegistry(ServiceMetadata serviceMetadata) throws Exception;

    ServiceMetadata discovery(String serviceName) throws Exception;

    void close() throws IOException;
}
