package com.sf.fine.rpc.protocol;

import com.sf.fine.rpc.protocol.serialize.HessionSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] data = HessionSerialize.serialize(msg);
        assert data != null;
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}
