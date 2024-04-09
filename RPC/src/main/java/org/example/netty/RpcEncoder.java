package org.example.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.example.Factory;

public class RpcEncoder extends MessageToByteEncoder<RpcMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) throws Exception {
        byte[] data = Factory.serializer.serialize(rpcMessage);
        byteBuf.writeInt(data.length);
        byteBuf.writerIndex(4);
        byteBuf.writeBytes(data);
    }
}
