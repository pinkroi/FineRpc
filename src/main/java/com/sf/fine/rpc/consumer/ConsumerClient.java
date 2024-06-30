package com.sf.fine.rpc.consumer;

import com.sf.fine.rpc.protocol.RpcDecoder;
import com.sf.fine.rpc.protocol.RpcEncoder;
import com.sf.fine.rpc.protocol.RpcRequest;
import com.sf.fine.rpc.protocol.RpcResponse;
import com.sf.fine.rpc.registry.ServiceMetadata;
import com.sf.fine.rpc.registry.ServiceRegistry;
import com.sf.fine.rpc.registry.ServiceRegistryFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ConsumerClient extends SimpleChannelInboundHandler<RpcResponse> {

    private final Object obj = new Object();

    private ServiceRegistry serviceRegistry;

    private RpcResponse rpcResponse;

    private EventLoopGroup worker = new NioEventLoopGroup(4);

    private Channel channel;

    public void init() {
        this.serviceRegistry = ServiceRegistryFactory.getServiceRegistry();
    }

    public RpcResponse sendRequest(RpcRequest rpcRequest) throws Exception {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            log.debug("init consumer request...");
                            socketChannel.pipeline()
                                    .addLast(new RpcEncoder())
                                    .addLast(new RpcDecoder())
                                    .addLast(ConsumerClient.this);
                        }
                    });

            ServiceMetadata serviceMetadata = serviceRegistry.discovery(rpcRequest.getServiceName()+":"+rpcRequest.getServiceVersion());
            if (serviceMetadata == null) {
                //没有获取到服务提供方
                throw new RuntimeException("no available service provider for " + rpcRequest.getServiceName());
            }
            final ChannelFuture future = bootstrap.connect(serviceMetadata.getServiceAddress(), serviceMetadata.getServicePort()).sync();

            future.addListener((ChannelFutureListener)arg0 -> {
                if (future.isSuccess()) {
                    log.debug("connect rpc provider success");
                } else {
                    log.error("connect rpc provider fail");
                    future.cause().printStackTrace();
                    worker.shutdownGracefully(); //关闭线程组
                }
            });

            this.channel = future.channel();
            this.channel.writeAndFlush(rpcRequest).sync();
            synchronized (obj) {
                obj.wait();
            }

            return rpcResponse;
        } finally {
            close();
        }
    }

    private void close() {
        //关闭客户端套接字
        if (this.channel != null) {
            this.channel.close();
        }
        //关闭客户端线程组
        if (this.worker != null) {
            this.worker.shutdownGracefully();
        }
        log.debug("shutdown consumer...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        this.rpcResponse = rpcResponse;

        synchronized (obj) {
            obj.notifyAll();
        }
    }

}
