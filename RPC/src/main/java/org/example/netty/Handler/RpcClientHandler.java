package org.example.netty.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.Factory;
import org.example.RpcEnum;
import org.example.netty.Pojo.RpcMessage;
import org.example.netty.Pojo.RpcResponse;

public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RpcMessage){
            String messageType = ((RpcMessage) msg).getMessageType();
            if(messageType.equals(RpcEnum.MESSAGE_RESPONSE_TYPE) ) {
                try {
                    RpcResponse rpcResponse = (RpcResponse) ((RpcMessage) msg).getData();
                    Factory.unprocessedRequests.complete(rpcResponse);
                }
                finally {

                }

            }
            ctx.fireChannelRead(msg);

        }
    }
}
