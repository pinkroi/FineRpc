package com.sf.fine.rpc.provider;

import com.sf.fine.rpc.common.ServiceUtils;
import com.sf.fine.rpc.registry.ServiceMetadata;
import com.sf.fine.rpc.registry.ServiceRegistryFactory;
import com.sf.fine.rpc.test.UserService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RpcProviderBean {

    public RpcProviderBean() {
    }

    private ServiceMetadata serviceMetadata;

    private Object target;

    public void init() {
        ProviderServiceCache.addService(ServiceUtils.uniqueServiceName(serviceMetadata), target);

        new Thread(()->ProviderServer.getInstance().startNettyServer()).start();

        try {
            ServiceRegistryFactory.getServiceRegistry().registry(serviceMetadata);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        RpcProviderBean providerBean = new RpcProviderBean();
        ServiceMetadata metadata = new ServiceMetadata("com.sf.fine.rpc.test.UserService", "1.0.0", "127.0.0.1", 12300);
        providerBean.setServiceMetadata(metadata);
        providerBean.setTarget(new UserService());
        providerBean.init();
    }
}
