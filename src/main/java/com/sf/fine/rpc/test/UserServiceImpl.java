package com.sf.fine.rpc.test;

import com.sf.fine.rpc.annotation.RpcProvider;
import org.springframework.stereotype.Component;

@RpcProvider(serviceInterface = UserService.class)
@Component
public class UserServiceImpl implements UserService{
    @Override
    public String getUserNameById(Long id) {
        return "jesse";
    }
}
