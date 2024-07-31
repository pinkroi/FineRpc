package com.sf.fine.rpc.consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConsumerAutoConfig {

    @Bean
    public RpcConsumerPostProcessor rpcConsumerPostProcessor() {
        return new RpcConsumerPostProcessor();
    }

}
