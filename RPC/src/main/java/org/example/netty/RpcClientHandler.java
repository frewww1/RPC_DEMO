package org.example.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.Factory;
import org.example.RpcEnum;

public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof  RpcMessage){
            String messageType = ((RpcMessage) msg).getMessageType();
            if(messageType == RpcEnum.MESSAGE_RESPONSE_TYPE){
                RpcResponse rpcResponse = (RpcResponse) ((RpcMessage) msg).getData();
                Factory.unprocessedRequests.complete(rpcResponse);
            }
        }
    }
}
