package com.sf.fine.rpc.provider;

import com.sf.fine.rpc.protocol.RpcRequest;
import com.sf.fine.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;

@Slf4j
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(response.getRequestId());

        try {
            Object providerBean = ProviderServiceCache.getService(rpcRequest.getServiceName()+":"+rpcRequest.getServiceVersion());

            if (null == providerBean) {
                throw new RuntimeException("provider not exist");
            }

            Class<?> providerClass = providerBean.getClass();
            String methodName = rpcRequest.getMethodName();
            Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
            Object[] parameters = rpcRequest.getParameters();

            FastClass providerFastClass = FastClass.create(providerClass);
            int methodIndex = providerFastClass.getIndex(methodName, parameterTypes);
            Object result = providerFastClass.invoke(methodIndex, providerBean, parameters);
            response.setResult(result);
            response.success(true);
        } catch (Throwable t) {
            log.error("RpcProviderHandler#channelRead0 error errMsg={}", t.getMessage(), t);
            response.success(false);
            response.setErrMsg(t.getMessage());
        }

        channelHandlerContext.writeAndFlush(response).addListener(
                (ChannelFutureListener) channelFuture ->
                        log.debug("Send response for request " + rpcRequest.getRequestId()));
    }

}
