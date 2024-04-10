package org.example.netty.Handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;
import org.example.Factory;
import org.example.netty.Pojo.RpcMessage;

import java.util.List;

public class RpcDecoder extends LengthFieldBasedFrameDecoder {
    public  RpcDecoder(){
        this(4*1028*1024,0,4,0,0);
    }
    public RpcDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;

            try {
                int length = frame.readInt();

                byte[] data = new byte[length];
                frame.readBytes(data);

                return Factory.serializer.deserialize(data, RpcMessage.class);


            } finally {
                ReferenceCountUtil.release(frame);
            }
        }
        return decoded;


    }
}
