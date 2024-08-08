package org.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;


//处理输入信息
public class NettyOutClientHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof String && "stop".equals(msg)){
                ctx.channel().close();
        }else{
            super.write(ctx,msg, promise);
        }
    }
}


