package org.example.netty;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.example.Factory;

import java.nio.ByteOrder;

public class RpcDecoder extends LengthFieldBasedFrameDecoder {
    public RpcDecoder(){
        this(4*1028*1024,0,4,-4,0);
    }
    public RpcDecoder( int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super( maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if(decoded instanceof ByteBuf){
            int length = ((ByteBuf) decoded).readInt();

            byte[] data = new byte[length];
            ((ByteBuf) decoded).readBytes(data);

            return Factory.serializer.deserialize(data,RpcMessage.class);
        }
        return decoded;
    }
}
