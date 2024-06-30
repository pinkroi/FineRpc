package com.sf.fine.rpc.protocol;

import com.sf.fine.rpc.consumer.ConsumerClient;

import java.io.Serializable;
import java.util.UUID;

public class RpcRequest implements Serializable {
    private String requestId;
    private String serviceName;
    private String serviceVersion;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public static void main(String[] args) throws Exception {
        RpcRequest request = new RpcRequest();
        request.setRequestId(String.valueOf(UUID.randomUUID()));
        request.setServiceName("com.sf.fine.rpc.test.UserService");
        request.setServiceVersion("1.0.0");
        request.setMethodName("getUserName");
        request.setParameterTypes(new Class[]{Long.class});
        request.setParameters(new Object[]{12L});
        ConsumerClient client = new ConsumerClient();
        client.init();
        RpcResponse response = client.sendRequest(request);
        System.out.println(response.getResult());
    }
}
