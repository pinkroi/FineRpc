package com.sf.fine.rpc.provider;

import com.sf.fine.rpc.protocol.RpcDecoder;
import com.sf.fine.rpc.protocol.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class ProviderServer {
    private static final Logger LOG = LoggerFactory.getLogger(ProviderServer.class);

    private static ProviderServer instance;

    private ProviderServer() {}

    public synchronized static ProviderServer getInstance() {
        if (null == instance) {
            instance = new ProviderServer();
        }
        return instance;
    }

    private final AtomicBoolean isInit = new AtomicBoolean(false);

    synchronized void startNettyServer(String host, int port) {
        if (!isInit.compareAndSet(false, true)) {
            return;
        }
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                                    .addLast(new RpcEncoder())
                                    .addLast(new RpcDecoder())
                                    .addLast(new RpcProviderHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            LOG.error("ProviderServer#start error errMsg={}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
